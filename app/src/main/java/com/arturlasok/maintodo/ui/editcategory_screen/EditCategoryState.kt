package com.arturlasok.maintodo.ui.editcategory_screen

data class EditCategoryState(
val categoryName: String = "",
val categoryIcon: Int = -1,
val categoryDescription : String = "",
val categoryErrors: Boolean = false
)
