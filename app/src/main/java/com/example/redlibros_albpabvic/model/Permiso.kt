package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permiso(
    @SerialName("idpermiso") val idpermiso: Int = 0,
    @SerialName("nombre") val nombre: String,
    @SerialName("descripcion") val descripcion: String? = null
)