package com.example.redlibros_albpabvic.model

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun getUserByUsername(username: String): User?
}