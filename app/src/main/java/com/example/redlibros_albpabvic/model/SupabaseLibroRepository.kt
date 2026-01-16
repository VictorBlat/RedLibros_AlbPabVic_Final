package com.example.redlibros_albpabvic.model

import com.example.redlibros_albpabvic.data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupabaseLibroRepository : LibroRepository {
    private val client = SupabaseClient.client

    override fun getAllLibros(): Flow<List<Libro>> = flow {
        while (true) {
            try {
                val result = client.from("libros")
                    .select()
                    .decodeList<Libro>()
                emit(result.sortedBy { it.nombre })
            } catch (e: Exception) {
                emit(emptyList())
            }
            delay(2000)
        }
    }

    override fun getLibrosByAutor(autor: String): Flow<List<Libro>> = flow {
        while (true) {
            try {
                val result = if (autor.isBlank()) {
                    client.from("libros")
                        .select()
                        .decodeList<Libro>()
                } else {
                    client.from("libros")
                        .select {
                            filter {
                                ilike("autor", "%$autor%")
                            }
                        }
                        .decodeList<Libro>()
                }
                emit(result.sortedBy { it.nombre })
            } catch (e: Exception) {
                emit(emptyList())
            }
            delay(2000)
        }
    }

    override suspend fun insertLibro(libro: Libro) {
        try {
            val libroData = mapOf(
                "nombre" to libro.nombre,
                "autor" to libro.autor,
                "isbn" to libro.isbn,
                "editorial" to libro.editorial
            )
            client.from("libros").insert(libroData)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateLibro(libro: Libro) {
        try {
            val libroData = mapOf(
                "nombre" to libro.nombre,
                "autor" to libro.autor,
                "isbn" to libro.isbn,
                "editorial" to libro.editorial
            )
            client.from("libros").update(libroData) {
                filter {
                    eq("idlibro", libro.idlibro)
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteLibro(libro: Libro) {
        try {
            client.from("libros").delete {
                filter {
                    eq("idlibro", libro.idlibro)
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}