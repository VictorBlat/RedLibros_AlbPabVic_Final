package com.example.redlibros_albpabvic.model

// Singleton para mantener la sesión del usuario
object UserSession {
    var iduser: Int = 0
    var username: String = ""
    var idalumno: Int? = null // Si es null, es profesor
    var isProfesor: Boolean = false

    fun clear() {
        iduser = 0
        username = ""
        idalumno = null
        isProfesor = false
    }

    fun isAlumno(): Boolean = idalumno != null
}