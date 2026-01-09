package com.example.redlibros_albpabvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class Transaccion(
    @PrimaryKey(autoGenerate = true) val idtransaccion: Int = 0,
    val idalumno: Int,
    val idlibro: Int,
    val fecha: Long // timestamp en millis
)
