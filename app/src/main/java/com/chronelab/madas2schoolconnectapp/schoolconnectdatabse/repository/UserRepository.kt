package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository




import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.UserDao

class UserRepository(private val userDao: UserDao) : UserRepositoryInterface{

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUser(email: String, password: String): User? {
        return userDao.getUser(email, password)
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }


    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}



