package com.example.redlibros_albpabvic.model

import kotlinx.coroutines.flow.Flow

class OfflineLibroRepository(
    private val libroDao: LibroDao
) : LibroRepository {

    override fun getAllLibros(): Flow<List<Libro>> = libroDao.getAllLibros()

    override fun getLibrosByAutor(autor: String): Flow<List<Libro>> =
        libroDao.getLibrosByAutor(autor)

    override fun getLibrosFavoritos(): Flow<List<Libro>> =
        libroDao.getLibrosFavoritos()

    override suspend fun insertLibro(libro: Libro) = libroDao.insert(libro)

    override suspend fun updateLibro(libro: Libro) = libroDao.update(libro)

    override suspend fun deleteLibro(libro: Libro) = libroDao.delete(libro)
}
