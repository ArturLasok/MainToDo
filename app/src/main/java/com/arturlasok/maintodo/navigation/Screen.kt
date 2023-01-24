package com.arturlasok.maintodo.navigation

sealed class Screen(val route: String) {

    object Start : Screen("Start")

    object Settings : Screen("Settings")

    object AddCategory : Screen("AddCategory")

    object EditCategory : Screen("EditCategory")

    //object AddTask : Screen("AddTask")

    object EditTask : Screen("EditTask")
}
