package com.arturlasok.maintodo.ui.editcategory_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.ui.addcategory_screen.NewCategoryState
import com.arturlasok.maintodo.util.CategoryUiState
import com.arturlasok.maintodo.util.FormDataState
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter,
    private val categoryUiState: CategoryUiState
) :ViewModel() {


    //selected category
    val selectedCategory = savedStateHandle.getStateFlow("selectedCategory","")
    //categoryName
    private val categoryName = savedStateHandle.getStateFlow("categoryName","")

    //selectedIcon
    private val selectedIcon = savedStateHandle.getStateFlow("categoryIcon",-1)

    //categoryDescription
    private val categoryDescription = savedStateHandle.getStateFlow("categoryDescription","")

    //categoryNameError
    private val categoryNameError = savedStateHandle.getStateFlow("categoryNameError","")

    //categoryIconError
    private val categoryIconError = savedStateHandle.getStateFlow("categoryIconError","")

    init{
        if(savedStateHandle.getStateFlow("selectedCategory","").value.isEmpty()) {  savedStateHandle["selectedCategory"] = categoryUiState.getSelectedCategoryToken() }
        else { savedStateHandle.getStateFlow("selectedCategory","") }
    }

    val editCategoryState = combine(categoryName,selectedIcon,categoryDescription,categoryNameError, categoryIconError) { name, icon, description, nameError, iconError ->
        EditCategoryState(
            categoryName = name,
            categoryIcon = icon,
            categoryDescription = description,
            categoryErrors = nameError.isNotEmpty() || iconError.isNotEmpty()
        )


    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditCategoryState())

    //get application
    fun getApplication() : BaseApplication {
        return application
    }
    //set di last item
    fun setLastCategorySelected(categoryToken: String) {
       categoryUiState.setSelectedCategoryToken(categoryToken)
    }
    //get di last item
    fun getCategoryTokenFromDi() : String {
        return categoryUiState.getSelectedCategoryToken()
    }
    //get categories list
    fun getOneCategoryFromRoom(categoryToken: String) {
        if(categoryName.value == "" && categoryDescription.value=="" && selectedIcon.value ==-1) {
            roomInter.getOneCategoryFromRoom(categoryToken).onEach { roomDataState ->

                try {
                    (roomDataState.data_recived as CategoryToDo).let { categoryToDo ->

                        savedStateHandle["categoryName"] = categoryToDo.dCatName
                        savedStateHandle["categoryIcon"] = categoryToDo.dCatIcon
                        savedStateHandle["categoryDescription"] = categoryToDo.dCatDescription

                    }
                } catch (e: Exception) {

                    savedStateHandle["categoryName"] = ""
                    savedStateHandle["categoryIcon"] = ""
                    savedStateHandle["categoryDescription"] = ""
                }

            }.launchIn(viewModelScope)
        }
    }
    //on icon selection change
    fun onIconSelectionChange(icon: Int) {

        savedStateHandle["categoryIcon"] = icon
        if(icon==-1)
        {
            savedStateHandle["categoryIconError"] = "No selected icon"

        } else {

            savedStateHandle["categoryIconError"] = ""
        }

    }
    // on category name change
    fun onCategoryNameChange(text: String) {
        savedStateHandle["categoryName"] = text
        if(text.trim().isEmpty())
        {
            savedStateHandle["categoryNameError"] = "Too short"

        } else {

            savedStateHandle["categoryNameError"] = ""
        }

    }
    //on category description change
    fun onCategoryDescriptionChange(text: String) {
        savedStateHandle["categoryDescription"] = text
    }
    //get icon error
    private fun getIconError() : String {
        return savedStateHandle["categoryIconError"] ?:""
    }
    //get name error
    private fun getNameError() : String {
        return savedStateHandle["categoryNameError"] ?:""
    }
    //delete category
    suspend fun deleteCategory(categoryToken:String) : FormDataState<Boolean> {

        var response = FormDataState.ok<Boolean>(true)

        roomInter.deleteCategory(categoryToken).onEach { roomResponse ->

            roomResponse.data_stored.let {
                if(it == true) {
                    response = FormDataState.ok<Boolean>(true)
                }
            roomResponse.data_error.let {
                if(it=="room_error") {
                    response = FormDataState.error(UiText.StringResource(
                        R.string.addcategory_form_error_room,
                        "asd"
                    ).asString(application.applicationContext))
                }
            }
            }



        }.launchIn(viewModelScope).join()
        return response

    }
    //update category
    private suspend fun updateCategoryInRoom(categoryToDo: CategoryToDo) : RoomDataState<Boolean> {

        var data = RoomDataState.data_stored<Boolean>(true)

        roomInter.insertCategoryToRoomWithOldToken(categoryToDo).onEach { roomDataState ->

            data = roomDataState

        }.launchIn(viewModelScope).join()

        return data



    }
    fun verifyForm(categoryToken: String) : Flow<Pair<String, FormDataState<Boolean>>> = flow {

        if(getNameError().isNotEmpty() || getIconError().isNotEmpty()) {

            if(getNameError().isNotEmpty())
            {
                emit(Pair("",
                    FormDataState.error<Boolean>(UiText.StringResource(R.string.addcategory_form_error_name,"asd").asString(application.applicationContext))))
            }
            else
            {
                emit(Pair("",
                    FormDataState.error<Boolean>(UiText.StringResource(R.string.addcategory_form_error_icon,"asd").asString(application.applicationContext))))
            }

        } else {

            val result = updateCategoryInRoom(
                CategoryToDo(
                    dCatId = null,
                    dCatName = editCategoryState.value.categoryName,
                    dCatDescription = editCategoryState.value.categoryDescription,
                    dCatIcon = editCategoryState.value.categoryIcon,
                    dCatToken = categoryToken,
                    dCatSort = 0,
                    dCatFav = false
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