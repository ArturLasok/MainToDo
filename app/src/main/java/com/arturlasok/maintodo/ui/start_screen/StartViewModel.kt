package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.runtime.MutableState
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
import com.arturlasok.maintodo.interactors.util.RoomDataState.Companion.data_stored
import com.arturlasok.maintodo.util.FormDataState
import com.arturlasok.maintodo.util.UiText
import com.arturlasok.maintodo.util.milisToDayOfWeek
import com.arturlasok.maintodo.util.millisToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter
) : ViewModel() {


    init {
        getCategoriesFromRoom()
        getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: -1L)
    }
    //CATEGORY
    val selectedCategoryRowIndex =  mutableStateOf(0)
    //selected category -1 All visible
    val selectedCategory = savedStateHandle.getStateFlow("selectedCategory",-1L)
    // category list from db
    val categoriesFromRoom = savedStateHandle.getStateFlow("categories", emptyList<CategoryToDo>())


    //SCREEN
    //start screen ui stat
    val startScreenUiState :MutableState<StartScreenState> = mutableStateOf(StartScreenState.Welcome)

    //TASKS
    //new task name
    private val newTaskName = savedStateHandle.getStateFlow("newTaskName","")
    //new task desc
    private val newTaskDesc = savedStateHandle.getStateFlow("newTaskDesc","")
    //new task category
    private val newTaskCategory = savedStateHandle.getStateFlow("newTaskCategory",-1L)
    //new task name error
    private val newTaskNameError = savedStateHandle.getStateFlow("newTaskNameError","")
    //new task category error
    private val newTaskCategoryError = savedStateHandle.getStateFlow("newTaskCategoryError","")
    // category list from db
    val tasksFromRoom = savedStateHandle.getStateFlow("tasks", mutableListOf<ItemToDo>())

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
    //get categories list
    private fun getCategoriesFromRoom() {

        roomInter.getCategoryFromRoom().onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["categories"] = it
            }

        }.launchIn(viewModelScope)

    }
    //get task items list
    fun getTaskItemsFromRoom(categoryId: Long) {

        roomInter.getTasksFromRoom(categoryId).onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["tasks"] = it
            }

        }.launchIn(viewModelScope)

    }
    //selected category
    fun setSelectedCategory(categoryId: Long) {

        savedStateHandle["selectedCategory"] = categoryId
        savedStateHandle["newTaskCategory"] = categoryId

        getTaskItemsFromRoom(categoryId)

    }
    //set start screen UI state
    fun setStartScreenUiState(newState: StartScreenState) {
        startScreenUiState.value = newState
    }
    //get one from category list
    fun getOneFromCategoryList(categoryId: Long) : CategoryToDo {

       return categoriesFromRoom.value.find {
            it.dCatId == categoryId
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
    fun onNewTaskCategoryChange(categoryId: Long) {

        savedStateHandle["newTaskCategory"] = categoryId
        if(categoryId<0) {
            savedStateHandle["newTaskCategoryError"] = "no selected category"
        } else
        {
            savedStateHandle["newTaskCategoryError"] = ""
        }

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
                    dItemDeliveryTime = 0,
                    dItemRemindTime = System.currentTimeMillis(),
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
    suspend fun updateTaskItemCompletion(taskToDo: ItemToDo, selectedCategory: Long) : Boolean {
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