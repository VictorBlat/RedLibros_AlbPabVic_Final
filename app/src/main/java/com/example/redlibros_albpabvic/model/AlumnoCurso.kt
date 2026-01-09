package com.example.redlibros_albpabvic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alumno_curso")
data class AlumnoCurso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idalumno: Int,
    val idcurso: Int
)
