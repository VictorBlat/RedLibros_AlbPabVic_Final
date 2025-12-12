package com.example.redlibros_albpabvic.view

import com.example.redlibros_albpabvic.R.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.redlibros_albpabvic.Rutas.Routes.Main
import com.example.redlibros_albpabvic.viewModel.loginViewModel

@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: loginViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 24.dp)) {
            Header(Modifier.align(Alignment.TopStart))
            Body(Modifier.align(Alignment.Center), navController, loginViewModel)
            Footer(Modifier.align(Alignment.BottomCenter))
        }
}

    @Composable
    fun Footer(modifier: Modifier) {
        Text(text = "Version 1.0.20251110",
            modifier = modifier)
    }

    @Composable
    fun Header(modifier: Modifier) {
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = "Close APP",
            modifier = modifier)
    }

    @Composable
    fun Body(modifier: Modifier, navController: NavHostController, loginViewModel: loginViewModel) {
        val mail by loginViewModel.email.observeAsState("")
        val isLoginValid by loginViewModel.isLoginEnabled.observeAsState(false)
        Column(modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Logo()
            Spacer(modifier = Modifier.height(12.dp))
            Email(mail) {loginViewModel.onLoginChange(it)}
            Spacer(modifier = Modifier.height(12.dp))
            LoginButton(isLoginValid, navController)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    @Composable
    fun Email(mail: String, onValueChange: (String) -> Unit) {
        TextField(value = mail,
            onValueChange = { onValueChange(it) },
            maxLines = 1,
            singleLine = true,
            placeholder = { Text("email@example.com")}
        )
    }


    @Composable
    fun LoginButton(ena:Boolean, navController:NavHostController){
        Button(enabled = ena,
            onClick = { navController.navigate(Main.route) }) {
            Text("LOGIN")
        }
    }

    @Composable
    fun Logo(){
        Image(painter = painterResource(drawable.redlibros),
            contentDescription = "Logo")
    }
