package com.example.redlibros_albpabvic.model

interface UserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun getAlumnoByUserId(iduser: Int): Alumno?
    suspend fun getAlumnoById(idalumno: Int): Alumno?
    suspend fun getTransaccionesByAlumno(idalumno: Int): List<TransaccionConLibro>
    suspend fun marcarDevolucion(idtransaccion: Int)
    suspend fun getCursosDelAlumno(idalumno: Int): List<Int>
    suspend fun getLibrosPorCursos(idsCursos: List<Int>): List<Libro>
}

data class TransaccionConLibro(
    val idtransaccion: Int,
    val libro: Libro,
    val fechaPrestamo: String,
    val fechaDevolucion: String?
)