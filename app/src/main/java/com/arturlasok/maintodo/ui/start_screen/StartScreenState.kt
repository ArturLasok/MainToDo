package com.arturlasok.maintodo.ui.start_screen

sealed class StartScreenState {
    object Welcome : StartScreenState()
    object AddTask : StartScreenState()
}