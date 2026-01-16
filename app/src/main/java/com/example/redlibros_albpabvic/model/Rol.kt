package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rol(
    @SerialName("idrol") val idrol: Int = 0,
    @SerialName("nombre") val nombre: String
)