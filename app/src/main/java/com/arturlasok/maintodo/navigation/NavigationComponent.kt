package com.arturlasok.maintodo.navigation


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arturlasok.maintodo.ui.addcategory_screen.AddCategoryScreen
import com.arturlasok.maintodo.ui.editcategory_screen.EditCategoryScreen
import com.arturlasok.maintodo.ui.edittask_screen.EditTaskScreen
import com.arturlasok.maintodo.ui.settings_screen.SettingsScreen
import com.arturlasok.maintodo.ui.start_screen.StartScreen

@Composable
fun NavigationComponent(
    navController: NavHostController,
    snackMessage:(text: String) -> Unit,
    setCurrentDestination:(route: String) -> Unit,
    currentDestination: String,
    isDarkModeOn: Int,
    confirmationTaskSetting: Boolean,
    changeConfirmationTaskSetting:() -> Unit,
    changeDarkMode:(newVal: Int) -> Unit,
    runLink:(runlink: String) -> Unit,
) {

    NavHost(
    navController = navController,
    startDestination = Screen.Start.route+"/{categoryId}"
    ) {
        //--
        // Main Screen
        composable(
            route= Screen.Start.route+"/{categoryId}",
            arguments = listOf(
                navArgument(name = "categoryId") {
                type = NavType.LongType
                defaultValue = -1
            },
            )
            ) { backStackEntry ->

            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: -1L

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            StartScreen(
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                confirmationTaskSetting = confirmationTaskSetting,
                selectedFromNav = categoryId,
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
        //--
        // Settings Screen
        composable(
            route=Screen.Settings.route+"/{categoryId}",
            arguments = listOf(navArgument(name = "categoryId") {
                type = NavType.LongType
                defaultValue = -1
            })
            ) { backStackEntry ->

            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: -1L

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            SettingsScreen(
                navigateTo = { route -> navController.navigate(route) },
                navigateUp = { navController.popBackStack()},
                confirmationTaskSetting = confirmationTaskSetting,
                changeConfirmationTaskSetting = { changeConfirmationTaskSetting() },
                categoryId = categoryId,
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
        // EditTask Screen
        composable(
            route = Screen.EditTask.route+"/{categoryId}+{itemId}",
            arguments = listOf(
                navArgument(name = "categoryId") {
                type = NavType.LongType
                defaultValue = -1
            },
                navArgument(name = "itemId") {
                    type = NavType.LongType
                    defaultValue = -1
             })
        ) { backStackEntry ->

            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: -1L
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: -1L

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            EditTaskScreen(
                navigateTo = { route -> navController.navigate(route) },
                taskId = itemId,
                categoryId = categoryId,
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