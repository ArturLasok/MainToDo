package com.arturlasok.maintodo.ui.edittask_screen

data class EditItemState(
val itemName: String = "",
val itemCategory:Long = -1L,
val itemDescription : String = "",
val itemErrors: Boolean = false
)
