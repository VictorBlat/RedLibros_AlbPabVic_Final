package com.example.redlibros_albpabvic.model

import com.example.redlibros_albpabvic.data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SupabaseUserRepository : UserRepository {
    private val client = SupabaseClient.client

    override suspend fun getUserByUsername(username: String): User? {
        return try {
            client.from("users")
                .select(Columns.ALL) {
                    filter { eq("username", username) }
                    limit(1)
                }
                .decodeSingleOrNull<User>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAlumnoByUserId(iduser: Int): Alumno? {
        return try {
            client.from("alumnos")
                .select(Columns.ALL) {
                    filter { eq("iduser", iduser) }
                    limit(1)
                }
                .decodeSingleOrNull<Alumno>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAlumnoById(idalumno: Int): Alumno? {
        return try {
            client.from("alumnos")
                .select(Columns.ALL) {
                    filter { eq("idalumno", idalumno) }
                    limit(1)
                }
                .decodeSingleOrNull<Alumno>()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getTransaccionesByAlumno(idalumno: Int): List<TransaccionConLibro> {
        return try {
            val transacciones = client.from("transacciones")
                .select(Columns.ALL) {
                    filter { eq("idalumno", idalumno) }
                }
                .decodeList<Transaccion>()

            transacciones.mapNotNull { transaccion ->
                try {
                    val libro = client.from("libros")
                        .select(Columns.ALL) {
                            filter { eq("idlibro", transaccion.idlibro) }
                            limit(1)
                        }
                        .decodeSingle<Libro>()

                    TransaccionConLibro(
                        idtransaccion = transaccion.idtransaccion,
                        libro = libro,
                        fechaPrestamo = transaccion.fechaPrestamo,
                        fechaDevolucion = transaccion.fechaDevolucion
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun marcarDevolucion(idtransaccion: Int) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val updateData = buildJsonObject {
                put("fecha_devolucion", currentDate)
            }

            client.from("transacciones").update(updateData) {
                filter { eq("idtransaccion", idtransaccion) }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    // NUEVO - Obtener cursos del alumno
    override suspend fun getCursosDelAlumno(idalumno: Int): List<Int> {
        return try {
            val alumnoCursos = client.from("alumno_curso")
                .select(Columns.ALL) {
                    filter { eq("idalumno", idalumno) }
                }
                .decodeList<AlumnoCursoDb>()

            alumnoCursos.map { it.idcurso }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // NUEVO - Obtener libros por cursos
    override suspend fun getLibrosPorCursos(idsCursos: List<Int>): List<Libro> {
        return try {
            if (idsCursos.isEmpty()) return emptyList()

            // Obtener todos los curso_libro de los cursos del alumno
            val cursoLibros = client.from("curso_libro")
                .select(Columns.ALL)
                .decodeList<CursoLibroDb>()
                .filter { it.idcurso in idsCursos }

            val idsLibros = cursoLibros.map { it.idlibro }.distinct()

            if (idsLibros.isEmpty()) return emptyList()

            // Obtener los libros
            val libros = client.from("libros")
                .select(Columns.ALL)
                .decodeList<Libro>()
                .filter { it.idlibro in idsLibros }

            libros
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// Clases auxiliares para deserialización
@Serializable
data class AlumnoCursoDb(
    @SerialName("id") val id: Int,
    @SerialName("idalumno") val idalumno: Int,
    @SerialName("idcurso") val idcurso: Int
)

@Serializable
data class CursoLibroDb(
    @SerialName("id") val id: Int,
    @SerialName("idcurso") val idcurso: Int,
    @SerialName("idlibro") val idlibro: Int
)