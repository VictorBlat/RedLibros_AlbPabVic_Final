package com.example.redlibros_albpabvic.Rutas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.redlibros_albpabvic.view.BibliotecaScreen
import com.example.redlibros_albpabvic.view.LoginScreen
import com.example.redlibros_albpabvic.viewModel.BibliotecaViewModel
import com.example.redlibros_albpabvic.viewModel.LoginViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    bibliotecaViewModel: BibliotecaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login.route
    ) {
        composable(NavRoutes.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Biblioteca.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.Biblioteca.route) {
            BibliotecaScreen(
                viewModel = bibliotecaViewModel
            )
        }
    }
}
