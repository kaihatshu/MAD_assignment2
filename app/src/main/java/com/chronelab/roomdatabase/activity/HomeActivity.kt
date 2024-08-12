package com.chronelab.roomdatabase.activity


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.TypeConverter
import com.chronelab.roomdatabase.RoomApplication
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.roomdatabase.entity.Post
import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import android.content.Intent
import com.chronelab.roomdatabase.NotificationActivity
import com.chronelab.roomdatabase.UserProfileActivity
import com.chronelab.roomdatabase.model.SessionManager
import com.chronelab.roomdatabase.model.SessionViewModel
import com.chronelab.roomdatabase.model.SessionViewModelFactory


@Suppress("DEPRECATION")
class HomeActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var sessionViewModel: SessionViewModel
    private var sessionCheckJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        val user = intent.getSerializableExtra("key_user") as? User
            ?: sessionManager.getUser()
            ?: throw IllegalStateException("User data not found")

        sessionViewModel = ViewModelProvider(
            this,
            SessionViewModelFactory(sessionManager)
        )[SessionViewModel::class.java]

        sessionViewModel.startSession(user)
        startSessionCheck()

        setContent {
            RoomDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SchoolConnectHomeScreen(
                        logout = { logout() },
                        user = user,
                        viewModel = sessionViewModel
                    )
                }
            }
        }
    }

    private fun startSessionCheck() {
        sessionCheckJob = lifecycleScope.launch {
            while (isActive) {
                delay(60000) // Check every minute
                if (!sessionViewModel.isSessionActive()) {
                    logout()
                    break
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sessionViewModel.isSessionActive()) {
            sessionViewModel.updateLastActivity()
        } else {
            logout()
        }
    }

    private fun logout() {
        sessionViewModel.endSession()
        sessionCheckJob?.cancel()
        Log.i("HomeActivity", "User logged out.")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        sessionCheckJob?.cancel()
    }
}

// Main composable functions

@Composable
fun SchoolConnectHomeScreen(
    logout: () -> Unit,
    user: User,
    viewModel: SessionViewModel // Ensure this parameter name matches
) {


    LaunchedEffect(Unit) {
        while (true) {
            delay(60000) // Update every minute
            viewModel.updateLastActivity()
        }
    }

    Scaffold(
        topBar = { HomeTopBar(logout) },
        bottomBar = { HomeBottomNavigation() },
        content = { padding -> HomeContent(padding, user) }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(logout: () -> Unit) {
    val context = LocalContext.current

    TopAppBar(
        title = { Text("SchoolConnect", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFE91E63),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = {
                context.startActivity(Intent(context, NotificationActivity::class.java))
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = {
                context.startActivity(Intent(context, UserProfileActivity::class.java))
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
            IconButton(onClick = logout) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}



@Composable
fun HomeContent(padding: PaddingValues, user: User) {
    val context = LocalContext.current
    val roomApp = context.applicationContext as RoomApplication
    val postRepository = roomApp.databaseContainer.postRepositoryInterface
    var postsList by remember { mutableStateOf<List<Post>>(emptyList()) }
    var newPostContent by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        postRepository.getAllPostsStream().collectLatest { posts ->
            postsList = posts
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        // Create Post Section
        OutlinedTextField(
            value = newPostContent,
            onValueChange = { newPostContent = it },
            label = { Text("What's on your mind?") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                if (newPostContent.isNotBlank()) {
                    val newPost = Post(
                        userId = user.id,
                        content = newPostContent,
                        username = user.username,
                        timestamp = System.currentTimeMillis()
                    )
                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        postRepository.insertPost(newPost)
                        newPostContent = "" // Clear the input field after posting
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text("Post")
        }

        // Posts List
        LazyColumn {
            items(postsList.sortedByDescending { it.timestamp }) { post ->
                PostCard(
                    post = post,
                    user = user,
                    onComment = { commentText ->
                        val newComment = Comment(
                            postId = post.id,
                            userId = user.id,
                            username = user.username,
                            content = commentText,
                            timestamp = System.currentTimeMillis()
                        )
                        (context as? ComponentActivity)?.lifecycleScope?.launch {
                            postRepository.addComment(newComment)
                            // Refresh the post to include the new comment
                            val updatedPost = post.copy(comments = post.comments + newComment)
                            postRepository.updatePost(updatedPost)
                        }
                    },
                    onEdit = { newContent ->
                        // Allow only the post owner to edit the post
                        if (post.userId == user.id) {
                            (context as? ComponentActivity)?.lifecycleScope?.launch {
                                val updatedPost = post.copy(content = newContent)
                                postRepository.updatePost(updatedPost)
                            }
                        } else {
                            Log.i("SchoolConnectApp", "Only the post owner can edit the post.")
                        }
                    },
                    onLike = {
                        (context as? ComponentActivity)?.lifecycleScope?.launch {
                            if (!post.likedByCurrentUser) {
                                val updatedPost = post.copy(
                                    likeCount = post.likeCount + 1,
                                    likedByCurrentUser = true
                                )
                                postRepository.updatePost(updatedPost)
                            }
                        }
                    },
                    onUnlike = {
                        (context as? ComponentActivity)?.lifecycleScope?.launch {
                            if (post.likedByCurrentUser) {
                                val updatedPost = post.copy(
                                    likeCount = maxOf(0, post.likeCount - 1),
                                    likedByCurrentUser = false
                                )
                                postRepository.updatePost(updatedPost)
                            }
                        }
                    },
                    onDelete = {
                        // Allow only the post owner to delete the post
                        if (post.userId == user.id) {
                            (context as? ComponentActivity)?.lifecycleScope?.launch {
                                postRepository.deletePost(post)
                            }
                        } else {
                            Log.i("SchoolConnectApp", "Only the post owner can delete the post.")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun PostCard(
    post: Post,
    user: User,
    onComment: (String) -> Unit,
    onEdit: (String) -> Unit,
    onLike: () -> Unit,
    onUnlike: () -> Unit,
    onDelete: () -> Unit
) {
    var showCommentInput by remember { mutableStateOf(false) }
    var showEditInput by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var editText by remember { mutableStateOf(post.content) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Use the username from the post object instead of the userId
            Text(text = "Posted by ${post.username}", fontWeight = FontWeight.Bold)

            if (showEditInput) {
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Edit post content") }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showEditInput = false }) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onEdit(editText)
                        showEditInput = false
                    }) {
                        Text("Save")
                    }
                }
            } else {
                Text(text = post.content)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        tint = if (post.likedByCurrentUser) Color.Blue else Color.Gray
                    )
                }
                Text(text = "${post.likeCount} likes")
                IconButton(onClick = onUnlike) {
                    Icon(
                        imageVector = Icons.Filled.ThumbDown,
                        contentDescription = "Unlike",
                        tint = if (!post.likedByCurrentUser) Color.Red else Color.Gray
                    )
                }
                IconButton(onClick = { showCommentInput = !showCommentInput }) {
                    Icon(imageVector = Icons.Filled.Comment, contentDescription = "Comment")
                }
                // Allow only the post owner to edit or delete the post
                if (post.userId == user.id) {
                    IconButton(onClick = { showEditInput = true }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }

            if (showCommentInput) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add a comment...") }
                    )
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onComment(commentText)
                                commentText = ""
                                showCommentInput = false
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Send Comment")
                    }
                }
            }

            // Display comments
            post.comments.forEach { comment ->
                CommentItem(comment)
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Use the username from the comment object instead of the userId
        Text(
            text = "${comment.username}: ${comment.content}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

class CommentListConverter {
    @TypeConverter
    fun fromString(value: String): List<Comment> {
        val listType = object : TypeToken<List<Comment>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Comment>): String {
        return Gson().toJson(list)
    }
}




@Composable
fun HomeBottomNavigation(currentRoute: String = "home") {
    val context = LocalContext.current

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentRoute == "search",
            onClick = {
                if (currentRoute != "search") {
                    val intent = Intent(context, SearchActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Email, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = currentRoute == "messages",
            onClick = {
                if (currentRoute != "messages") {
                    val intent = Intent(context, MessagesActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
    }
}