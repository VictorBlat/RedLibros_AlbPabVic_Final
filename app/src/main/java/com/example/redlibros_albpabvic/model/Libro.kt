package com.example.redlibros_albpabvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros")
data class Libro(
    @PrimaryKey(autoGenerate = true) val idlibro: Int = 0,
    val nombre: String,
    val autor: String,
    val isbn: String,
    val favorito: Boolean = false
)
