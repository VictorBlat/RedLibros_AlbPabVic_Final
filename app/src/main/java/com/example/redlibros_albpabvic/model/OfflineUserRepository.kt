package com.example.redlibros_albpabvic.model

class OfflineUserRepository(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun getUserByUsername(username: String): User? =
        userDao.getUserByUsername(username)
}
