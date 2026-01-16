package com.example.redlibros_albpabvic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.redlibros_albpabvic.Rutas.AppNavGraph
import com.example.redlibros_albpabvic.model.SupabaseLibroRepository
import com.example.redlibros_albpabvic.model.SupabaseUserRepository
import com.example.redlibros_albpabvic.ui.theme.RedLibros_AlbPabVicTheme
import com.example.redlibros_albpabvic.viewModel.BibliotecaViewModel
import com.example.redlibros_albpabvic.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepository = SupabaseUserRepository()
        val libroRepository = SupabaseLibroRepository()

        val loginViewModel = LoginViewModel(userRepository)
        val bibliotecaViewModel = BibliotecaViewModel(libroRepository)

        setContent {
            RedLibros_AlbPabVicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        bibliotecaViewModel = bibliotecaViewModel
                    )
                }
            }
        }
    }
}