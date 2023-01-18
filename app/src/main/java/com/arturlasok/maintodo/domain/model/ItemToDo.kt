package com.arturlasok.maintodo.domain.model

data class ItemToDo(
    val dItemId: Long? = null,
    val dItemTitle: String = "",
    val dItemAuthor: String = "",
    val dItemDescription: String = "",
    val dItemImportance: Int = 0,
    val dItemCompleted: Boolean = false,
    val dItemAdded: Long = 0L,
    val dItemEdited: Long = 0L,
    val dItemImported: String = "",
    val dItemExported: String = "",
    val dItemToken: String = "",
    val dItemGroup: String = "",
    val dItemInfo: String = "",
    val dItemWhyFailed: String = "",
    val dItemDeliveryTime: Long = 0L,
    val dItemRemindTime: Long = 0L,
    val dItemLimitTime: Long = 0L
)
