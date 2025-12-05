package com.example.redlibros_albpabvic.model

data class Book(
    val title: String,
    val curso: String,
    val isbn: String,
    val year: String = "1990",
    var disponible: Boolean = true
)
