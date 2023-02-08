package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter,
    private val itemUiState: ItemUiState,
    private val categoryUiState: CategoryUiState
) : ViewModel() {


    init {
        getCategoriesFromRoom()
        if(savedStateHandle.getStateFlow("selectedCategory","").value.isEmpty()) {  savedStateHandle["selectedCategory"] = categoryUiState.getSelectedCategoryToken() }
        else { savedStateHandle.getStateFlow("selectedCategory","") }
        if(savedStateHandle.getStateFlow("newTaskCategory","").value.isEmpty()) {  savedStateHandle["newTaskCategory"] = categoryUiState.getSelectedCategoryToken() }
        else { savedStateHandle.getStateFlow("newTaskCategory","") }
        getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: "")

    }
    //CATEGORY
    val selectedCategoryRowIndex =  mutableStateOf(0)
    //selected category empty All visible
    val selectedCategory = savedStateHandle.getStateFlow("selectedCategory","")
    // category list from db
    val categoriesFromRoom = savedStateHandle.getStateFlow("categories", emptyList<CategoryToDo>())
    //number of items in category
    val counter : SnapshotStateList<Pair<String, Int>> =  mutableStateListOf()

    //SCREEN
    //start screen ui stat
    val startScreenUiState :MutableState<StartScreenState> = mutableStateOf(StartScreenState.Welcome)

    //TASKS
    //new task name
    private val newTaskName = savedStateHandle.getStateFlow("newTaskName","")
    //new task desc
    private val newTaskDesc = savedStateHandle.getStateFlow("newTaskDesc","")
    //new task category
    private val newTaskCategory = savedStateHandle.getStateFlow("newTaskCategory","")
    //new task name error
    private val newTaskNameError = savedStateHandle.getStateFlow("newTaskNameError","")
    //new task category error
    private val newTaskCategoryError = savedStateHandle.getStateFlow("newTaskCategoryError","")
    // item list from db
    val tasksFromRoom = savedStateHandle.getStateFlow("tasks", mutableListOf<ItemToDo>())
    //DATE & TIME
    private val taskDate = savedStateHandle.getStateFlow("taskDate",0L)
    private val notDate = savedStateHandle.getStateFlow("notDate",0L)
    private val taskTime = savedStateHandle.getStateFlow("taskTime",0L)
    private val notTime = savedStateHandle.getStateFlow("notTime",0L)
    private val taskDateTimeError = savedStateHandle.getStateFlow("taskDateTimeError",0)

    val newDateTimeState = combine(taskDate,notDate,taskTime,notTime,taskDateTimeError) {
            taskDate,notDate, taskTime, notTime, taskDateTimeError ->
        NewDateTimeState(
            taskToken = "",
            taskDateTimeError = taskDateTimeError,
            taskDate = taskDate,
            taskTime= taskTime,
            notDate = notDate,
            notTime = notTime
        )

    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),NewDateTimeState())


    //new task state
    val newTaskState = combine(newTaskName,newTaskDesc,newTaskCategory,newTaskNameError,newTaskCategoryError) {
       taskName,taskDesc,taskCategory, nameError, categoryError ->
        NewTaskState(
            taskName = taskName,
            taskDesc = taskDesc,
            taskCategory = taskCategory,
            taskErrors = nameError.isNotEmpty() || categoryError.isNotEmpty()
        )
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),NewTaskState())


    //current date to start screen ui
    fun dateWithNameOfDayWeek() : String {
        val timeInMilis =  System.currentTimeMillis()
        val dayOfWeek = milisToDayOfWeek(timeInMilis)

        val dayNames = listOf<String>(
            UiText.StringResource(R.string.day7,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day1,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day2,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day3,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day4,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day5,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day6,"asd").asString(application.applicationContext),

        )

        return millisToDate(timeInMilis) + " " + dayNames[dayOfWeek-1]

    }
    //get application
    fun getApplication() : BaseApplication {
        return application
    }
    //set di last item
    fun setLastItemSelected(itemToken: String) {
        itemUiState.setlastItemToken(itemToken)
    }
    //get last di item
    fun getLastItemSelected() : String {
        return itemUiState.getLastItemToken()
    }
    //get categories list
    private fun getCategoriesFromRoom() {

        roomInter.getCategoryFromRoom().onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["categories"] = it
            }
            //badges counter
            categoriesFromRoom.value.onEach { ctd->

                if(counter.map {
                        it.first
                    }.contains(ctd.dCatToken)) {


                }
                else {

                    counter.add(Pair(ctd.dCatToken ?:"",0))
                }
            }

        }.launchIn(viewModelScope)

    }
    // task count
    fun getTaskCount(categoryToken: String) : Flow<Int> = roomInter.getCount(categoryToken)

    //get task items list
    fun getTaskItemsFromRoom(categoryToken: String) {

        roomInter.getTasksFromRoom(categoryToken).onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["tasks"] = it
            }

        }.launchIn(viewModelScope)

    }
    //selected category
    fun setSelectedCategory(categoryToken: String) {

        savedStateHandle["selectedCategory"] = categoryToken
        savedStateHandle["newTaskCategory"] = categoryToken

        Log.i(TAG,"set seleceted category $categoryToken")
        categoryUiState.setSelectedCategoryToken(categoryToken)

        getTaskItemsFromRoom(categoryToken)

    }
    //set new DateTime
    fun setNewTaskDate(newTaskDate: Long) {
        savedStateHandle["taskDate"] = newTaskDate
    }
    fun setNewTaskTime(newTaskTime: Long) {
        savedStateHandle["taskTime"] = newTaskTime
    }
    //set new notDateTime
    fun setNotTaskDate(newNotDate: Long) {
        savedStateHandle["notDate"] = newNotDate
    }
    fun setNotTaskTime(newNotTime: Long) {
        savedStateHandle["notTime"] = newNotTime
    }
    //set new errorDateTime
    fun setErrorTaskDateTime(error: Int) {
        savedStateHandle["taskDateTimeError"] = error
    }
    //set start screen UI state
    fun setStartScreenUiState(newState: StartScreenState) {
        startScreenUiState.value = newState
    }
    //get one from category list
    fun getOneFromCategoryList(categoryToken: String) : CategoryToDo {

       return categoriesFromRoom.value.find {
            it.dCatToken == categoryToken
        } ?: CategoryToDo()

    }
    //new task name change
    fun onNewTaskNameChange(text: String) {
        savedStateHandle["newTaskName"] = text
        if(text.trim().isEmpty()) {

            savedStateHandle["newTaskNameError"] = "Too short"

        } else {

            savedStateHandle["newTaskNameError"]  =  ""
        }

    }
    //new task description change
    fun onNewTaskDescChange(text: String) {

        savedStateHandle["newTaskDesc"] = text

    }
    //new task category change
    fun onNewTaskCategoryChange(categoryToken: String) {

        savedStateHandle["newTaskCategory"] = categoryToken
        if(categoryToken.isEmpty()) {
            savedStateHandle["newTaskCategoryError"] = "no selected category"
        } else
        {
            savedStateHandle["newTaskCategoryError"] = ""
        }

    }
    //delete category
    suspend fun deleteTask(itemId: Long) : FormDataState<Boolean> {

        var response = FormDataState.ok<Boolean>(true)

        roomInter.deleteItem(itemId).onEach { roomResponse ->

            roomResponse.data_stored.let {
                if(it == true) {
                    response = FormDataState.ok<Boolean>(true)
                    getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: "")
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
    //get new task name error
    private fun getNewTaskNameError() : String {
        return savedStateHandle["newTaskNameError"] ?: ""
    }
    //get new task category error
    private fun getNewTaskCategoryError(): String {
        return savedStateHandle["newTaskCategoryError"] ?: ""
    }
    //new task form verification
    fun verifyForm() : Flow<FormDataState<Boolean>> = flow {

        if(getNewTaskNameError().isNotEmpty() ||getNewTaskCategoryError().isNotEmpty()) {

            if(getNewTaskNameError().isNotEmpty())
            {
                emit(FormDataState.error<Boolean>(UiText.StringResource(R.string.addtask_form_error_name,"asd").asString(application.applicationContext)))
            }
            else
            {
                emit(FormDataState.error<Boolean>(UiText.StringResource(R.string.addtask_form_error_category,"asd").asString(application.applicationContext)))
            }

        } else {

            val result = createTaskItemInRoom(
                ItemToDo(
                    dItemId = null,
                    dItemTitle = newTaskState.value.taskName,
                    dItemAuthor = "null",
                    dItemDescription = newTaskState.value.taskDesc,
                    dItemImportance = 1,
                    dItemCompleted = false,
                    dItemAdded = System.currentTimeMillis(),
                    dItemEdited = System.currentTimeMillis(),
                    dItemImported = "false",
                    dItemExported = "false",
                    dItemToken = getRandomToken(),
                    dItemGroup = "empty",
                    dItemInfo = newTaskState.value.taskCategory.toString(),
                    dItemWhyFailed = "empty",
                    dItemDeliveryTime = (TimeUnit.DAYS.toMillis(taskDate.value))+taskTime.value,
                    dItemRemindTime = (TimeUnit.DAYS.toMillis(notDate.value))+notTime.value,
                    dItemLimitTime = System.currentTimeMillis()
                )
            )

            // result:

            result.data_stored.let {
                //stored
                if(it == true) {

                    emit(FormDataState(true))

                }
            }

            result.data_recived.let {
                //do nothing = nothing received
            }

            result.data_error.let {
                //error to ui
                if(!it.isNullOrBlank()) {
                    emit(
                        FormDataState.error<Boolean>(
                            UiText.StringResource(
                                R.string.addcategory_form_error_room,
                                "asd"
                            ).asString(application.applicationContext)
                        )
                    )

                }
            }

        }

    }
    //create task item
    private suspend fun createTaskItemInRoom(taskItem: ItemToDo) : RoomDataState<Boolean> {

        var data = RoomDataState.data_stored<Boolean>(true)

        roomInter.insertTaskItemToRoom(taskItem).onEach { roomDataState ->

            data = roomDataState

        }.launchIn(viewModelScope).join()

        return data



    }
    //get random token
    private fun getRandomToken() :String {
        val unixTime = System.currentTimeMillis() / 1000L
        val allowedChars = ('A'..'Z') + ('a'..'s') + ('0'..'9')
        val random : () -> String = fun() : String {
            return (1..32)
                .map { allowedChars.random() }
                .joinToString("")
        }
        return "$unixTime"+"time"+random.invoke()
    }
    //update task item Completion in room
    suspend fun updateTaskItemCompletion(taskToDo: ItemToDo, selectedCategory: String) : Boolean {
        var isDone = false
        roomInter.updateTaskItemCompletion(taskToDo).onEach {
            it.data_stored.let {
                if(it==true) {
                    isDone = true
                  getTaskItemsFromRoom(selectedCategory)
                    //val taskIndex = tasksFromRoom.value.indexOf(taskToDo)

                   //tasksFromRoom.value[taskIndex] = taskToDo

                    //savedStateHandle["tasks"] = tasksFromRoom.value
                }
            }
            it.data_error.let {
                if(it=="room_error") {
                    isDone=false
                }
            }
        }.launchIn(viewModelScope).join()
        return isDone


    }
}