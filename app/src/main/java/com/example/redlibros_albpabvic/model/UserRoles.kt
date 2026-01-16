package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRoles(
    @SerialName("id") val id: Int = 0,
    @SerialName("iduser") val iduser: Int,
    @SerialName("idrol") val idrol: Int
)