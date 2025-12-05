package com.example.redlibros_albpabvic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.redlibros_albpabvic.model.Libro
import com.example.redlibros_albpabvic.ui.theme.RedLibros_AlbPabVicTheme
import com.example.redlibros_albpabvic.view.Login.Login
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedLibros_AlbPabVicTheme {
                var libros by remember { mutableStateOf(mutableListOf<Libro>(
                    Libro("Vencer al dragón", "Barbara Hambly", "978-8413147000"),
                    Libro("Los diablos", "Joe Abercrombie", "978-8411489836"),
                    Libro("Los jardines de la luna", "Steven Erikson", "978-8418037528"),
                    Libro("El camino de los reyes", "Brandon Sanderson", "978-8466657662")
                )) }
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = Login.route) {
                    composable(route = Login.route) {
                        LoginScreen(navController, loginViewModel())
                    }
                    composable(Main.route) {
                        LibraryScreen(navController, books) { isbn ->
                            books = books.map { book ->
                                if (book.isbn == isbn)
                                    book.copy(isFavorite = !book.isFavorite)
                                else
                                    book
                            }.toMutableList()
                        }
                    }
                    composable(Add.route) {
                        AddBookScreen(navController) { books.add(it) }
                    }
                }
            }
        }
    }

}