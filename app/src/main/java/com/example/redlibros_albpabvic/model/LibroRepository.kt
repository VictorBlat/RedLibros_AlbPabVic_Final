package com.example.redlibros_albpabvic.model

import kotlinx.coroutines.flow.Flow

interface LibroRepository {
    fun getLibrosPorIds(idsLibros: List<Int>): Flow<List<Libro>> // NUEVO
    fun getLibrosByAutor(autor: String, idsLibros: List<Int>): Flow<List<Libro>> // ACTUALIZADO
}