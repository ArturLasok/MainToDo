package com.arturlasok.maintodo.domain.model

data class CategoryToDo(
    val dCatId : Int? = null,
    val dCatName : String = "",
    val dCatDescription : String = "",
    val dCatToken: String = "",
    val dCatIcon: String = "",
    val dCatSort: Int = 0,
    val dCatFav: Boolean = false
)
