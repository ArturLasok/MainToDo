package com.arturlasok.maintodo.navigation


import android.util.Log
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
import com.arturlasok.maintodo.util.TAG

@Composable
fun NavigationComponent(
    navController: NavHostController,
    addAlarm:(time: Long, beganTime:Long,name: String,desc:String, token: String, id:Long) -> Unit,
    removeAlarm:(id: Int) -> Unit,
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
    startDestination = Screen.Start.route
    ) {
        //--
        // Main Screen
        composable(
            route= Screen.Start.route) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            Log.i(TAG,"NAV--------------------------- recompose:")
            StartScreen(
                addAlarm = { time,beganTime, name, desc, token, id ->  addAlarm(time,beganTime, name,desc,token, id) },
                removeAlarm = { taskid-> removeAlarm(taskid) },
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                confirmationTaskSetting = confirmationTaskSetting,
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
        //--
        // Settings Screen
        composable(
            route=Screen.Settings.route) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            SettingsScreen(
                navigateTo = { route -> navController.navigate(route) },
                navigateUp = { navController.popBackStack()},
                confirmationTaskSetting = confirmationTaskSetting,
                changeConfirmationTaskSetting = { changeConfirmationTaskSetting() },
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
            route = Screen.EditTask.route
        ) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            EditTaskScreen(
                addAlarm = { time,beganTime, name, desc, token, id ->  addAlarm(time,beganTime, name,desc,token, id) },
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
        //--
        // EditCategory Screen
        composable(
            route = Screen.EditCategory.route
            ) {

            navController.currentDestination?.route?.let { newRoute->
                setCurrentDestination(newRoute)
            }
            EditCategoryScreen(
                navigateTo = { route -> navController.navigate(route) },
                isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                snackMessage = { snackMessage-> snackMessage(snackMessage) }
            )
        }
    }
}