package com.example.redlibros_albpabvic.Rutas

sealed class Routes(val route:String) {
    object Login : Routes("LoginScreen")
    object Main : Routes("MainScreen")
    object Add : Routes("AddBookScreen")
}