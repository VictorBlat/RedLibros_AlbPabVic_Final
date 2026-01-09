package com.example.redlibros_albpabvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cursos")
data class Curso(
    @PrimaryKey(autoGenerate = true) val idcurso: Int = 0,
    val nombre: String
)
