package com.mash.upax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mash.upax.ui.screens.detail.DetailDestination
import com.mash.upax.ui.screens.detail.DetailScreen
import com.mash.upax.ui.screens.home.HomeDestination
import com.mash.upax.ui.screens.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemDetail = {
                    navController.navigate("${DetailDestination.route}/${it}")
                },
            )
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailDestination.pokemonName) {
                type = NavType.StringType
            })
        ) {
            DetailScreen(
                navigateBack = { navController.navigateUp() })
        }
    }


}