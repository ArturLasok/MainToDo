package com.arturlasok.maintodo.ui.edittask_screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.ui.editcategory_screen.EditCategoryState
import com.arturlasok.maintodo.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter,
    private val categoryUiState: CategoryUiState,
    private val itemUiState: ItemUiState
) :ViewModel() {

    //category list
    val categoriesFromRoom = savedStateHandle.getStateFlow("categories", emptyList<CategoryToDo>())

    //edited item
    private val editedItem = savedStateHandle.getStateFlow("editedItem", ItemToDo())

    //categoryName
    private val itemName = savedStateHandle.getStateFlow("itemName","")

    //selectedCategory
    private val itemCategory = savedStateHandle.getStateFlow("itemCategory","")

    //categoryDescription
    private val itemDescription = savedStateHandle.getStateFlow("itemDescription","")

    //categoryNameError
    private val itemNameError = savedStateHandle.getStateFlow("itemNameError","")

    //categoryIconError
    private val itemCategoryError = savedStateHandle.getStateFlow("itemCategoryError","")

    val editItemState = combine(itemName,itemCategory,itemDescription,itemNameError, itemCategoryError) { name, cat, description, nameError,  categoryError ->
        EditItemState(
            itemName = name,
            itemCategory = cat,
            itemDescription = description,
            itemErrors = nameError.isNotEmpty() || categoryError.isNotEmpty()
        )


    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditItemState())

    //get application
    fun getApplication() : BaseApplication {
        return application
    }
    //get item
    fun getOneItemFromRoom(itemId: String) {
        if(itemName.value == "" && itemDescription.value=="" && itemCategory.value =="") {

            roomInter.getOneItemFromRoom(itemId).onEach { roomDataState ->

                try {
                    (roomDataState.data_recived as ItemToDo).let { itemToDo ->

                        roomInter.getOneCategoryFromRoomWithToken(itemToDo.dItemInfo).onEach {

                            try {
                                (it.data_recived as CategoryToDo).let { categoryToDo ->
                                    Log.i(TAG,"item ssh: ${categoryToDo.dCatToken}")
                                    savedStateHandle["itemCategory"] = categoryToDo.dCatToken
                                }
                            }
                            catch (e: Exception) {


                                savedStateHandle["itemCategory"] = ""

                            }

                        }.launchIn(viewModelScope).join()

                        savedStateHandle["editedItem"] = itemToDo

                        savedStateHandle["itemName"] = itemToDo.dItemTitle

                        savedStateHandle["itemDescription"] = itemToDo.dItemDescription

                    }
                } catch (e: Exception) {

                    savedStateHandle["itemName"] = ""
                    savedStateHandle["itemCategory"] = ""
                    savedStateHandle["itemDescription"] = ""
                }

            }.launchIn(viewModelScope)
        }
    }
    //on item category change
    fun onItemCategoryChange(categoryToken: String) {

        savedStateHandle["itemCategory"] = categoryToken
        categoryUiState.setSelectedCategoryToken(categoryToken)
        if(categoryToken.isEmpty())
        {
            savedStateHandle["itemCategoryError"] = "No selected icon"

        } else {

            savedStateHandle["itemCategoryError"] = ""
        }

    }
    // on item name change
    fun onItemNameChange(text: String) {
        savedStateHandle["itemName"] = text
        if(text.trim().isEmpty())
        {
            savedStateHandle["itemNameError"] = "Too short"

        } else {

            savedStateHandle["itemNameError"] = ""
        }

    }
    //on item description change
    fun onItemDescriptionChange(text: String) {
        savedStateHandle["itemDescription"] = text
    }
    //get item category error
    private fun getCategoryError() : String {
        return savedStateHandle["itemCategoryError"] ?:""
    }
    //get item name error
    private fun getNameError() : String {
        return savedStateHandle["itemNameError"] ?:""
    }
    //update category
    private suspend fun updateItemInRoom(itemToDo: ItemToDo) : RoomDataState<Boolean> {

        var data = RoomDataState.data_stored<Boolean>(true)

        roomInter.insertTaskItemToRoom(itemToDo).onEach { roomDataState ->

            data = roomDataState

        }.launchIn(viewModelScope).join()

        return data



    }

    //get item ui state from di
    fun getItemFormDi() : String {
        return itemUiState.getLastItemToken()
    }
    //get categories list
    fun getCategoriesFromRoom() {

        roomInter.getCategoryFromRoom().onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["categories"] = it
            }

        }.launchIn(viewModelScope)

    }
    fun verifyForm() : Flow<Pair<String, FormDataState<Boolean>>> = flow {

        if(getNameError().isNotEmpty() || getCategoryError().isNotEmpty()) {

            if(getNameError().isNotEmpty())
            {
                emit(Pair("",
                    FormDataState.error<Boolean>(UiText.StringResource(R.string.addtask_form_error_name,"asd").asString(application.applicationContext))))
            }
            else
            {
                emit(Pair("",
                    FormDataState.error<Boolean>(UiText.StringResource(R.string.addtask_form_error_category,"asd").asString(application.applicationContext))))
            }

        } else {

            val result = updateItemInRoom(
               editedItem.value.copy(
                   dItemTitle = itemName.value,
                   dItemInfo = itemCategory.value,
                   dItemDescription = itemDescription.value,
                   dItemEdited = System.currentTimeMillis(),
               )
            )

            // result:

            result.data_stored.let {
                //stored
                if(it == true) {
                    emit(Pair("", FormDataState(true)))

                }
            }

            result.data_recived.let {
                //do nothing = nothing received
            }

            result.data_error.let {
                //error to ui
                if(!it.isNullOrBlank()) {
                    emit(Pair("",
                        FormDataState.error<Boolean>(
                            UiText.StringResource(
                                R.string.addcategory_form_error_room,
                                "asd"
                            ).asString(application.applicationContext)
                        )
                    )
                    )
                }
            }

        }

    }


}