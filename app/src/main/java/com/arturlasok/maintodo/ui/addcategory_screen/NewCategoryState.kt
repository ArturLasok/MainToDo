package com.arturlasok.maintodo.ui.addcategory_screen

data class NewCategoryState(
    val categoryName: String = "",
    val categoryIcon: Int = -1,
    val categoryDescription : String = "",
    val categoryErrors: Boolean = false
)
