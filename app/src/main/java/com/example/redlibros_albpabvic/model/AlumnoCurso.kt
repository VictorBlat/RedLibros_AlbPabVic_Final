package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlumnoCurso(
    @SerialName("id") val id: Int = 0,
    @SerialName("idalumno") val idalumno: Int,
    @SerialName("idcurso") val idcurso: Int
)