package com.arturlasok.maintodo.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arturlasok.maintodo.ui.addcategory_screen.AddCategoryScreen
import com.arturlasok.maintodo.ui.editcategory_screen.EditCategoryScreen
import com.arturlasok.maintodo.ui.settings_screen.SettingsScreen
import com.arturlasok.maintodo.ui.start_screen.StartScreen
import com.arturlasok.maintodo.util.SnackbarController

@Composable
fun NavigationComponent(
    navController: NavHostController,
    snackMessage:(text: String) -> Unit,
    setCurrentDestination:(route: String) -> Unit,
    currentDestination: String,
    isDarkModeOn: Int,
    changeDarkMode:(newVal: Int) -> Unit,
    runLink:(runlink: String) -> Unit,
) {

    NavHost(
    navController = navController,
    startDestination = Screen.Start.route
    ) {
        //--
        // Main Screen
        composable(Screen.Start.route) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            StartScreen(
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme()
            )
        }
        //--
        // Settings Screen
        composable(Screen.Settings.route) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            SettingsScreen(
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn,
                changeDarkMode = { newVal-> changeDarkMode(newVal) },
                runLink  = { runlink -> runLink(runlink) }
            )
        }
        //--
        // AddCategory Screen
        composable(Screen.AddCategory.route) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            AddCategoryScreen(
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
        //--
        // EditCategory Screen
        composable(
            route = Screen.EditCategory.route+"/{categoryId}",
            arguments = listOf(navArgument(name = "categoryId") {
                type = NavType.LongType
                defaultValue = -1
            })
            ) { backStackEntry ->

            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: -1L

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            EditCategoryScreen(
                navigateTo = { route -> navController.navigate(route) },
                categoryId = categoryId,
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
    }
}