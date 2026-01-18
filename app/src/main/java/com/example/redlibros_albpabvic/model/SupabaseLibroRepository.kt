package com.example.redlibros_albpabvic.model

import com.example.redlibros_albpabvic.data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupabaseLibroRepository : LibroRepository {
    private val client = SupabaseClient.client

    override fun getLibrosPorIds(idsLibros: List<Int>): Flow<List<Libro>> = flow {
        while (true) {
            try {
                if (idsLibros.isEmpty()) {
                    emit(emptyList())
                } else {
                    val result = client.from("libros")
                        .select()
                        .decodeList<Libro>()
                        .filter { it.idlibro in idsLibros }
                    emit(result.sortedBy { it.nombre })
                }
            } catch (e: Exception) {
                emit(emptyList())
            }
            delay(2000)
        }
    }

    override fun getLibrosByAutor(autor: String, idsLibros: List<Int>): Flow<List<Libro>> = flow {
        while (true) {
            try {
                if (idsLibros.isEmpty()) {
                    emit(emptyList())
                } else {
                    val result = client.from("libros")
                        .select()
                        .decodeList<Libro>()
                        .filter {
                            it.idlibro in idsLibros &&
                                    (autor.isBlank() || it.autor?.contains(autor, ignoreCase = true) == true)
                        }
                    emit(result.sortedBy { it.nombre })
                }
            } catch (e: Exception) {
                emit(emptyList())
            }
            delay(2000)
        }
    }
}