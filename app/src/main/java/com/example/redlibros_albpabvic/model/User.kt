package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("iduser") val iduser: Int = 0,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)