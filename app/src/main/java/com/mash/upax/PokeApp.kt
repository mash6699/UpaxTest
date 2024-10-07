package com.mash.upax

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mash.upax.ui.navigation.AppNavHost

@Composable
fun PokeApp(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}