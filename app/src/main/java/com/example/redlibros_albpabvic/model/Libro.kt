package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    @SerialName("idlibro") val idlibro: Int = 0,
    @SerialName("nombre") val nombre: String,
    @SerialName("autor") val autor: String? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("editorial") val editorial: String? = null
)