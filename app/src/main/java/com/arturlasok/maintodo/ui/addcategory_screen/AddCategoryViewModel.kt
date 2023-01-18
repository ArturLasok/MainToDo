package com.arturlasok.maintodo.ui.addcategory_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.util.FormDataState
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter
): ViewModel() {

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

    //categoryDescError
    private val categoryDescError = savedStateHandle.getStateFlow("categoryDescError","")


    val newCategoryState = combine(categoryName,selectedIcon,categoryDescription,categoryNameError, categoryIconError) { name, icon, description, nameError, iconError ->
        NewCategoryState(
            categoryName = name,
            categoryIcon = icon,
            categoryDescription = description,
            categoryErrors = nameError.isNotEmpty() || iconError.isNotEmpty()
        )


    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewCategoryState())


    // on category name change
    fun onCategoryNameChange(text: String) {
        savedStateHandle["categoryName"] = text.trim()
        if(text.trim().isEmpty())
        {
            savedStateHandle["categoryNameError"] = "Too short"

        } else {

            savedStateHandle["categoryNameError"] = ""
        }

    }
    //on icon selection change
    fun onIconSelectionChange(icon: Int) {

        savedStateHandle["selectedIcon"] = icon
        if(icon==-1)
        {
            savedStateHandle["categoryIconError"] = "No selected icon"

        } else {

            savedStateHandle["categoryIconError"] = ""
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
    //get application
    fun getApplication() : BaseApplication {
        return application
    }
    //form verification
    fun verifyForm() : Flow<Pair<Long,FormDataState<Boolean>>> = flow {

        if(getNameError().isNotEmpty() || getIconError().isNotEmpty()) {

            if(getNameError().isNotEmpty())
            {
                emit(Pair(-1L,FormDataState.error<Boolean>(UiText.StringResource(R.string.addcategory_form_error_name,"asd").asString(application.applicationContext))))
            }
            else
            {
                emit(Pair(-1L,FormDataState.error<Boolean>(UiText.StringResource(R.string.addcategory_form_error_icon,"asd").asString(application.applicationContext))))
            }

        } else {

            val result = createCategoryInRoom(
                CategoryToDo(
                    dCatId = null,
                    dCatName = newCategoryState.value.categoryName,
                    dCatDescription = newCategoryState.value.categoryDescription,
                    dCatIcon = newCategoryState.value.categoryIcon,
                    dCatToken = "empty",
                    dCatSort = 0,
                    dCatFav = false
                )
            )

            // result:

            result.data_stored.let {
                //stored
                if(it == true) {
                    val lastCategoryId =  getLastCategoryId()
                    emit(Pair(lastCategoryId,FormDataState(true)))

                }
            }

            result.data_recived.let {
                //do nothing = nothing received
            }

            result.data_error.let {
                //error to ui
                if(!it.isNullOrBlank()) {
                    emit(Pair(-1L,
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
    private suspend fun createCategoryInRoom(categoryToDo: CategoryToDo) : RoomDataState<Boolean> {

        var data = RoomDataState.data_stored<Boolean>(true)

        roomInter.insertCategoryToRoom(categoryToDo).onEach { roomDataState ->

            Log.i(TAG,"Recived in vm from inter:"+" Error:" + roomDataState.data_error+ "stored:"+roomDataState.data_stored)
           data = roomDataState

        }.launchIn(viewModelScope).join()

        return data



    }
    suspend fun getLastCategoryId() : Long {

         var data = -1L

        roomInter.getLastCategoryId().onEach {

            it.data_recived.let { id->
                data =id as Long
            }
            it.data_error.let { error_string ->
                if(error_string=="room_error") { data = -1L }
            }


        }.launchIn(viewModelScope).join()

        return data

    }


}