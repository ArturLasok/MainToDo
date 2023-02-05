package com.arturlasok.maintodo.util

import com.arturlasok.maintodo.domain.model.ItemToDo

object ItemUiState {
    private var tempItem: ItemToDo = ItemToDo()
    private var lastItemToken: String = ""

    fun setTempItem(itemToDo: ItemToDo) {
        tempItem = itemToDo
    }
    fun getTempItem() : ItemToDo {
        return tempItem
    }

    fun setlastItemToken(itemToken: String) {
        lastItemToken =itemToken
    }
    fun getLastItemToken() : String {
        return lastItemToken
    }

}