package com.arturlasok.maintodo.domain.model


data class CategoryToDo(
    val dCatId : Long? = null,
    val dCatName : String? = "",
    val dCatDescription : String? = "",
    val dCatToken: String? = "",
    val dCatIcon: Int? = 0,
    val dCatSort: Int? = 0,
    val dCatFav: Boolean? = false
) : java.io.Serializable
