package com.arturlasok.maintodo.ui.edittask_screen

data class EditItemState(
val itemName: String = "",
val itemCategory:String = "",
val itemDescription : String = "",
val itemErrors: Boolean = false
)
