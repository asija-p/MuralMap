package com.anastasija.muralmap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anastasija.muralmap.ui.auth.AuthViewModel
import com.anastasija.muralmap.ui.pages.HomePage
import com.anastasija.muralmap.ui.pages.login.LoginScreen
import com.anastasija.muralmap.ui.pages.signup.SignupScreen

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
       composable("login"){
           LoginScreen(modifier, navController, authViewModel)
       }

        composable("signup") {
            SignupScreen(modifier, navController, authViewModel)
        }

        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
    })
}