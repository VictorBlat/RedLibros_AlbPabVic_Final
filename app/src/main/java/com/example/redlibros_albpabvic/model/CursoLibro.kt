package com.example.redlibros_albpabvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "curso_libro")
data class CursoLibro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idcurso: Int,
    val idlibro: Int
)
