package com.arturlasok.maintodo.ui.editcategory_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.ui.addcategory_screen.NewCategoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter
) :ViewModel() {


    //categoryName
    private val categoryName = savedStateHandle.getStateFlow("categoryName","")

    //selectedIcon
    private val selectedIcon = savedStateHandle.getStateFlow("selectedIcon",-1)

    //categoryDescription
    private val categoryDescription = savedStateHandle.getStateFlow("categoryDescription","")

    //categoryNameError
    private val categoryNameError = savedStateHandle.getStateFlow("categoryNameError","")

    //categoryIconError
    private val categoryIconError = savedStateHandle.getStateFlow("categoryIconError","")

    val editCategoryState = combine(categoryName,selectedIcon,categoryDescription,categoryNameError, categoryIconError) { name, icon, description, nameError, iconError ->
        EditCategoryState(
            categoryName = name,
            categoryIcon = icon,
            categoryDescription = description,
            categoryErrors = nameError.isNotEmpty() || iconError.isNotEmpty()
        )


    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditCategoryState())

    //get categories list
    fun getOneCategoryFromRoom(categoryId: Long) {

        roomInter.getOneCategoryFromRoom(categoryId).onEach { roomDataState ->

            try {
                (roomDataState.data_recived as CategoryToDo).let { categoryToDo ->

                    savedStateHandle["categoryName"] = categoryToDo.dCatName
                    savedStateHandle["categoryIcon"] = categoryToDo.dCatIcon
                    savedStateHandle["categoryDescription"] = categoryToDo.dCatDescription

                }
            }
            catch (e: Exception) {

                savedStateHandle["categoryName"] = ""
                savedStateHandle["categoryIcon"] = -1
                savedStateHandle["categoryDescription"] = ""
            }

        }.launchIn(viewModelScope)

    }

}