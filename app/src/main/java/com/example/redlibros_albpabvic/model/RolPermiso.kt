package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RolPermiso(
    @SerialName("id") val id: Int = 0,
    @SerialName("idrol") val idrol: Int,
    @SerialName("idpermiso") val idpermiso: Int
)