package com.example.redlibros_albpabvic.model

import android.util.Log
import com.example.redlibros_albpabvic.data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseUserRepository : UserRepository {
    private val client = SupabaseClient.client
    private val TAG = "SupabaseUserRepo"

    override suspend fun insertUser(user: User) {
        try {
            Log.d(TAG, "=== INICIO REGISTRO ===")
            Log.d(TAG, "URL: ${client.supabaseUrl}")
            Log.d(TAG, "Usuario: ${user.username}")
            Log.d(TAG, "Password length: ${user.password.length}")

            // Intentar con buildJsonObject
            val userData = buildJsonObject {
                put("username", user.username)
                put("password", user.password)
            }

            Log.d(TAG, "Datos a enviar: $userData")

            val response = client.from("users").insert(userData)

            Log.d(TAG, "Usuario registrado exitosamente")
            Log.d(TAG, "=== FIN REGISTRO ===")
        } catch (e: Exception) {
            Log.e(TAG, "Error detallado al registrar usuario:")
            Log.e(TAG, "Tipo de error: ${e::class.simpleName}")
            Log.e(TAG, "Mensaje: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            throw Exception("No se pudo registrar el usuario. Verifica tu conexión a internet y que Supabase esté configurado correctamente.")
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return try {
            Log.d(TAG, "=== INICIO LOGIN ===")
            Log.d(TAG, "Buscando usuario: $username")

            val result = client.from("users")
                .select(Columns.ALL) {
                    filter {
                        eq("username", username)
                    }
                    limit(1)
                }
                .decodeSingleOrNull<User>()

            if (result != null) {
                Log.d(TAG, "Usuario encontrado: ${result.username} (ID: ${result.iduser})")
            } else {
                Log.d(TAG, "Usuario no encontrado: $username")
            }

            Log.d(TAG, "=== FIN LOGIN ===")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error al buscar usuario:")
            Log.e(TAG, "Mensaje: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            null
        }
    }
}