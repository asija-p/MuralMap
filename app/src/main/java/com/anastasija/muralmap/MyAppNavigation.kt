package com.anastasija.muralmap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anastasija.muralmap.auth.AuthViewModel
import com.anastasija.muralmap.pages.HomePage
import com.anastasija.muralmap.pages.LoginPage
import com.anastasija.muralmap.pages.SignupPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
       composable("login"){
           LoginPage(modifier, navController, authViewModel)
       }

        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }

        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
    })
}