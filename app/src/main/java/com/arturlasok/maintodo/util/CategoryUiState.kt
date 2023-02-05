package com.arturlasok.maintodo.util

import com.arturlasok.maintodo.domain.model.CategoryToDo


object CategoryUiState {

    private var tempCategory: CategoryToDo = CategoryToDo()
    private var selectedCategoryToken: String =""

    fun setTempCategory(categoryToDo: CategoryToDo) {
        tempCategory = categoryToDo
    }
    fun getTempCategory() : CategoryToDo {
        return tempCategory
    }
    fun setSelectedCategoryToken(categoryToken: String) {
        selectedCategoryToken = categoryToken
    }
    fun getSelectedCategoryToken() :String {
        return selectedCategoryToken
    }

}
