package com.arturlasok.maintodo.ui.start_screen

data class NewTaskState(
    val taskName: String = "",
    val taskCategory: String= "",
    val taskDesc: String = "",
    val taskErrors: Boolean = false
)
