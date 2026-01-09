package com.example.redlibros_albpabvic.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LibroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libro: Libro)

    @Update
    suspend fun update(libro: Libro)

    @Delete
    suspend fun delete(libro: Libro)

    @Query("SELECT * FROM libros ORDER BY nombre")
    fun getAllLibros(): Flow<List<Libro>>

    @Query("SELECT * FROM libros WHERE autor LIKE '%' || :autor || '%' ORDER BY nombre")
    fun getLibrosByAutor(autor: String): Flow<List<Libro>>

    @Query("SELECT * FROM libros WHERE favorito = 1 ORDER BY nombre")
    fun getLibrosFavoritos(): Flow<List<Libro>>
}
