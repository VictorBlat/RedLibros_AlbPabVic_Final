package com.example.redlibros_albpabvic.Rutas

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Biblioteca : NavRoutes("biblioteca")
}
