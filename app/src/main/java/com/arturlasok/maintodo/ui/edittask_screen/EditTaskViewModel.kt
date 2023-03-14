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
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit
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

    //DATE & TIME
    private val taskDate = savedStateHandle.getStateFlow("taskDate", LocalDate.now().toEpochDay())
    private val notDate = savedStateHandle.getStateFlow("notDate",0L)
    private val taskTime = savedStateHandle.getStateFlow("taskTime", LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
    private val notTime = savedStateHandle.getStateFlow("notTime",0L)
    private val taskDateTimeError = savedStateHandle.getStateFlow("taskDateTimeError",0)

    val editDateTimeState = combine(taskDate,notDate,taskTime,notTime,taskDateTimeError) {
            taskDate,notDate, taskTime, notTime, taskDateTimeError ->
        EditDateTimeState(
            taskToken = "",
            taskDateTimeError = taskDateTimeError,
            taskDate = taskDate,
            taskTime= taskTime,
            notDate = notDate,
            notTime = notTime,
        )

    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),EditDateTimeState())

    //get application
    fun getApplication() : BaseApplication {
        return application
    }
    fun getLastItemSelected() : String {
        return itemUiState.getLastItemToken()
    }
    //get last item id in room
    fun getLastItemIdInRoom() : Long {
        return itemUiState.getLastItemId()
    }
    //g
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


                      savedStateHandle["taskDate"] = TimeUnit.MILLISECONDS.toDays(itemToDo.dItemDeliveryTime)

                      savedStateHandle["taskTime"] = LocalTime.parse(millisToHour(itemToDo.dItemDeliveryTime)).toKotlinLocalTime().toMillisecondOfDay()

                      savedStateHandle["notDate"] =  TimeUnit.MILLISECONDS.toDays(itemToDo.dItemRemindTime)

                       savedStateHandle["notTime"] = LocalTime.parse(millisToHour(itemToDo.dItemRemindTime)).toKotlinLocalTime().toMillisecondOfDay()

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
                   dItemDeliveryTime = (TimeUnit.DAYS.toMillis(taskDate.value))+(taskTime.value-3600000),
                   dItemRemindTime = (TimeUnit.DAYS.toMillis(notDate.value))+(notTime.value-3600000),
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
    //set new DateTime
    fun setNewTaskDate(newTaskDate: Long) {
        //if time of new task is older then time now it is reset to null 0L
        if(newTaskDate== LocalDate.now().toEpochDay()) {

            if(taskTime.value.toInt()<LocalTime.now().toKotlinLocalTime().toMillisecondOfDay()) {
                setNewTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
            }

        }
        //if date of notification is after new task date is not reset to date of task
        if(LocalDate.ofEpochDay(newTaskDate)<LocalDate.ofEpochDay(notDate.value))   {
            setNotTaskDate(newTaskDate)

            if(taskTime.value.toInt()<notTime.value.toInt()) {
                setNotTaskTime(taskTime.value)
            }
        }
        //if date of notification is after new task date is not reset to date of task
        if(LocalDate.ofEpochDay(newTaskDate)==LocalDate.ofEpochDay(notDate.value))   {


            if(taskTime.value<notTime.value && (taskTime.value!=0L)) {
                setNotTaskTime(taskTime.value)
            }
        }
        savedStateHandle["taskDate"] = newTaskDate

    }
    fun setNewTaskTime(newTaskTime: Long) {
        //if date of notification is after new task date is not reset to date of task
        savedStateHandle["taskTime"] = newTaskTime
        if(LocalDate.ofEpochDay(taskDate.value)==LocalDate.ofEpochDay(notDate.value))   {


            //if(newTaskTime<notTime.value && (taskTime.value!=0L) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.now().toEpochDay() ) {
            if(newTaskTime<notTime.value && (taskTime.value!=0L) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.ofEpochDay(notDate.value).toEpochDay()) {
                setNotTaskTime(newTaskTime)
            }
        }

        Log.i(TAG,"sese12 TASK TIME: ${millisToHour(newTaskTime)}")

    }
    //set new notDateTime
    fun setNotTaskDate(newNotDate: Long) {

        if(newNotDate== LocalDate.now().toEpochDay()) {

            if(notTime.value.toInt()<LocalTime.now().toKotlinLocalTime().toMillisecondOfDay()) {
                setNotTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
            }


        }
        //if date of notification is after new task date , not reset to date of task
        if(LocalDate.ofEpochDay(taskDate.value)==LocalDate.ofEpochDay(newNotDate))   {


            if(taskTime.value<notTime.value && (taskTime.value!=0L)) {
                setNotTaskTime(taskTime.value)
            }
        }

        savedStateHandle["notDate"] = newNotDate
        Log.i(TAG,"sese12 NOTIFICATION DATE: ${newNotDate}")

    }
    fun setNotTaskTime(newNotTime: Long) {
        //if date of notification is after new task date is not reset to date of task
        if(LocalDate.ofEpochDay(taskDate.value)==LocalDate.ofEpochDay(notDate.value))   {

            //if((taskTime.value<newNotTime && (taskTime.value!=0L)) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.now().toEpochDay() ) {
            if((taskTime.value<newNotTime && (taskTime.value!=0L)) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.ofEpochDay(notDate.value).toEpochDay()) {
                //setNotTaskTime(taskTime.value)
                savedStateHandle["notTime"] = taskTime.value
                Log.i(TAG,"sese12 NOTIFICATION TIME: ${millisToHour(taskTime.value)}")
            } else {savedStateHandle["notTime"] = newNotTime
                Log.i(TAG,"sese12 NOTIFICATION TIME: ${millisToHour(newNotTime)}")
            }
        } else{ savedStateHandle["notTime"] = newNotTime
            Log.i(TAG,"sese12 NOTIFICATION TIME: ${millisToHour(newNotTime)}")
        }



    }

}