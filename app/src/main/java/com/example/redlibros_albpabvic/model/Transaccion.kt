package com.example.redlibros_albpabvic.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaccion(
    @SerialName("idtransaccion") val idtransaccion: Int = 0,
    @SerialName("idalumno") val idalumno: Int,
    @SerialName("idlibro") val idlibro: Int,
    @SerialName("iduser_profesor") val iduserProfesor: Int,
    @SerialName("fecha_prestamo") val fechaPrestamo: String,
    @SerialName("fecha_devolucion") val fechaDevolucion: String? = null
)