package com.example.redlibros_albpabvic.model

import kotlinx.coroutines.flow.Flow

interface LibroRepository {
    fun getAllLibros(): Flow<List<Libro>>
    fun getLibrosByAutor(autor: String): Flow<List<Libro>>
    suspend fun insertLibro(libro: Libro)
    suspend fun updateLibro(libro: Libro)
    suspend fun deleteLibro(libro: Libro)
}