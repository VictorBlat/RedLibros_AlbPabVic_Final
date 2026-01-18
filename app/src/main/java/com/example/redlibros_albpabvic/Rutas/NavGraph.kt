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
    onLoginSuccess: () -> Unit,
    bibliotecaViewModel: BibliotecaViewModel?
) {
    NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
        composable(NavRoutes.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    onLoginSuccess() // Crear el BibliotecaViewModel
                    navController.navigate(NavRoutes.Biblioteca.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.Biblioteca.route) {
            // Solo mostrar BibliotecaScreen si el ViewModel existe
            bibliotecaViewModel?.let { viewModel ->
                BibliotecaScreen(
                    viewModel = viewModel,
                    onLogout = {
                        navController.navigate(NavRoutes.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}