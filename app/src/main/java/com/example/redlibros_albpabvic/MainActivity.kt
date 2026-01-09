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
import com.example.redlibros_albpabvic.model.OfflineLibroRepository
import com.example.redlibros_albpabvic.model.OfflineUserRepository
import com.example.redlibros_albpabvic.model.RedLibrosDatabase
import com.example.redlibros_albpabvic.ui.theme.RedLibros_AlbPabVicTheme
import com.example.redlibros_albpabvic.viewModel.BibliotecaViewModel
import com.example.redlibros_albpabvic.viewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = RedLibrosDatabase.getDatabase(this)
        val userRepository = OfflineUserRepository(db.userDao())
        val libroRepository = OfflineLibroRepository(db.libroDao())

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
