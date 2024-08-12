package com.chronelab.roomdatabase.roomdatabase.repository



import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.roomdatabase.dao.UserDao

class UserRepository(private val userDao: UserDao) : UserRepositoryInterface {

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUser(email: String, password: String): User? {
        return userDao.getUser(email, password)
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
}
