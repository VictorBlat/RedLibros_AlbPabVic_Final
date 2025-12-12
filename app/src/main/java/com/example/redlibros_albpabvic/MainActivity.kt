package com.example.redlibros_albpabvic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.redlibros_albpabvic.Rutas.Routes.Add
import com.example.redlibros_albpabvic.Rutas.Routes.Login
import com.example.redlibros_albpabvic.Rutas.Routes.Main
import com.example.redlibros_albpabvic.ui.theme.RedLibros_AlbPabVicTheme
import com.example.redlibros_albpabvic.view.LoginScreen
import com.example.redlibros_albpabvic.viewModel.loginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedLibros_AlbPabVicTheme {
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = Login.route) {
                    composable(route = Login.route) {
                        LoginScreen(navController, loginViewModel())
                    }
                    composable(Main.route) {}
                    composable(Add.route) {}
                }
            }
        }
    }
}