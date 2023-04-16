package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.interactors.util.MainTimeDate.convertMillisToDayOfWeek
import com.arturlasok.maintodo.interactors.util.MainTimeDate.convertMillisToWeekNumber
import com.arturlasok.maintodo.interactors.util.MainTimeDate.epochDaysLocalNow
import com.arturlasok.maintodo.interactors.util.MainTimeDate.localFormDate
import com.arturlasok.maintodo.interactors.util.MainTimeDate.localTimeNowInMilliseconds
import com.arturlasok.maintodo.interactors.util.MainTimeDate.systemCurrentTimeInMillis
import com.arturlasok.maintodo.interactors.util.MainTimeDate.utcTimeZoneOffsetMillis
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.interactors.util.RoomDataState.Companion.data_error
import com.arturlasok.maintodo.interactors.util.RoomDataState.Companion.data_stored
import com.arturlasok.maintodo.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.datetime.toKotlinLocalTime
import java.time.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter,
    private val itemUiState: ItemUiState,
    private val categoryUiState: CategoryUiState,
) : ViewModel() {



    //MONTHS
    val months =
        listOf(
            UiText.StringResource(R.string.month1, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month2, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month3, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month4, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month5, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month6, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month7, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month8, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month9, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month10, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month11, "asd").asString(getApplication().applicationContext),
            UiText.StringResource(R.string.month12, "asd").asString(getApplication().applicationContext),

            )

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
    private val taskDate = savedStateHandle.getStateFlow("taskDate", epochDaysLocalNow() )
    private val notDate = savedStateHandle.getStateFlow("notDate",0L)

    private val taskTime = savedStateHandle.getStateFlow("taskTime",localTimeNowInMilliseconds())
    private val notTime = savedStateHandle.getStateFlow("notTime",0L)
    private val taskDateTimeError = savedStateHandle.getStateFlow("taskDateTimeError",0)
    //LAZY ITEM LIST
    val prevItem : MutableState<ItemToDo> = mutableStateOf(ItemToDo())
    val actualItem: MutableState<ItemToDo> = mutableStateOf(ItemToDo())
    val nextItem: MutableState<ItemToDo> = mutableStateOf(ItemToDo())
    val firstViItemIndex  = mutableStateOf(0)

    init {
        getCategoriesFromRoom()
        if(savedStateHandle.getStateFlow("selectedCategory","").value.isEmpty()) {  savedStateHandle["selectedCategory"] = categoryUiState.getSelectedCategoryToken() }
        else { savedStateHandle.getStateFlow("selectedCategory","") }
        if(savedStateHandle.getStateFlow("newTaskCategory","").value.isEmpty()) {  savedStateHandle["newTaskCategory"] = categoryUiState.getSelectedCategoryToken() }
        else { savedStateHandle.getStateFlow("newTaskCategory","") }
        getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: "")

    }
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
        val timeInMilis =  systemCurrentTimeInMillis()
        val dayOfWeek = convertMillisToDayOfWeek(timeInMilis)

        val dayNames = listOf<String>(
            UiText.StringResource(R.string.day7,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day1,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day2,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day3,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day4,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day5,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day6,"asd").asString(application.applicationContext),

        )

        return localFormDate(timeInMilis) + " " + dayNames[dayOfWeek-1]

    }
    //current week number
    fun weekNumber() : Int {
        return convertMillisToWeekNumber(systemCurrentTimeInMillis()).toInt()
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
    //get last item id in room
    fun getLastItemIdInRoom() : Long {
        return itemUiState.getLastItemId()
    }
    //get categories list
    private fun getCategoriesFromRoom() {

        roomInter.getCategoryFromRoom().onEach { roomDataState ->
            Log.i(TAG,"Category list room data state: ${roomDataState.toString()}")
            roomDataState.data_recived.let {
                Log.i(TAG,"Category list: data recived:  ${it.toString()}")
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
    @Suppress("UNCHECKED_CAST")
    fun getTaskItemsFromRoom(categoryToken: String) {

        roomInter.getTasksFromRoom(categoryToken).onEach {  roomDataState ->

            roomDataState.data_recived.let { it as MutableList<ItemToDo>
                it.onEach {
                    it.dItemDeliveryTime = it.dItemDeliveryTime+utcTimeZoneOffsetMillis(
                        millisToDateAndHour(it.dItemDeliveryTime)
                    )
                    if(it.dItemRemindTime!=0L) {
                    it.dItemRemindTime = it.dItemRemindTime+ utcTimeZoneOffsetMillis(
                        millisToDateAndHour(it.dItemRemindTime)
                    ) }
                }
                savedStateHandle["tasks"] = it.sortedBy { it.dItemDeliveryTime }
                    .sortedBy { it.dItemCompleted }
            }
            Log.i(TAG,"response GETTED!")
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
    //delete all completed tasks
    suspend fun deleteAllCompletedTasks() : FormDataState<Boolean> {

        var response = FormDataState.ok<Boolean>(true)

        if (selectedCategory.value.isEmpty()) {
            roomInter.deleteAllItemsCompleted().onEach { roomResponse ->

                roomResponse.data_stored.let {
                    if (it == true) {
                        response = FormDataState.ok<Boolean>(true)
                        getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: "")
                    }
                    roomResponse.data_error.let {
                        if (it == "room_error") {
                            response = FormDataState.error(
                                UiText.StringResource(
                                    R.string.addcategory_form_error_room,
                                    "asd"
                                ).asString(application.applicationContext)
                            )
                        }
                    }
                }

        }.launchIn(viewModelScope).join()

    } else {
            roomInter.deleteAllItemsCompletedWithCategory(selectedCategory.value).onEach { roomResponse ->

                roomResponse.data_stored.let {
                    if (it == true) {
                        response = FormDataState.ok<Boolean>(true)
                        getTaskItemsFromRoom(savedStateHandle["selectedCategory"] ?: "")
                    }
                    roomResponse.data_error.let {
                        if (it == "room_error") {
                            response = FormDataState.error(
                                UiText.StringResource(
                                    R.string.addcategory_form_error_room,
                                    "asd"
                                ).asString(application.applicationContext)
                            )
                        }
                    }
                }

            }.launchIn(viewModelScope).join()
        }

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
                    dItemAdded = systemCurrentTimeInMillis(),
                    dItemEdited = systemCurrentTimeInMillis(),
                    dItemImported =  "date:"+TimeUnit.DAYS.toMillis(taskDate.value),
                    dItemExported =  "time:"+(taskTime.value),
                    dItemToken = getRandomToken(),
                    dItemGroup = "empty",
                    dItemInfo = newTaskState.value.taskCategory.toString(),
                    dItemWhyFailed = millisToDateAndHour((TimeUnit.DAYS.toMillis(taskDate.value))+(taskTime.value)),
                    dItemDeliveryTime = (TimeUnit.DAYS.toMillis(taskDate.value))+(taskTime.value),
                    dItemRemindTime = if(notDate.value==0L) { 0L } else { (TimeUnit.DAYS.toMillis(notDate.value))+(notTime.value)},
                    dItemLimitTime = systemCurrentTimeInMillis()
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

    //reset new task state
    fun resetNewTaskState(){
            savedStateHandle["taskDate"] = epochDaysLocalNow()
            savedStateHandle["taskTime"] = localTimeNowInMilliseconds()
            savedStateHandle["notDate"] = 0L
            savedStateHandle["notTime"] = 0L
    }
    //create task item
    private suspend fun createTaskItemInRoom(taskItem: ItemToDo) : RoomDataState<Boolean> {

        var data = RoomDataState.data_stored<Boolean>(true)

        if(taskItem.dItemDeliveryTime==0L) {
            roomInter.insertTaskItemToRoom(taskItem.copy(dItemDeliveryTime = systemCurrentTimeInMillis())).onEach { roomDataState ->

                data = roomDataState

            }.launchIn(viewModelScope).join()
        } else {

            roomInter.insertTaskItemToRoom(taskItem).onEach { roomDataState ->

                data = roomDataState

            }.launchIn(viewModelScope).join()
        }
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
    //update task from accept data store id
    @Suppress("UNCHECKED_CAST")
    suspend fun updateTaskItemDataStoreId(selectedCategory: String)  {

                    //getTaskItemsFromRoom(selectedCategory)
                    roomInter.getTasksFromRoom(selectedCategory).onEach { roomDataState ->

                        roomDataState.data_recived.let { it as MutableList<ItemToDo>


                            it.onEach {
                                it.dItemDeliveryTime = it.dItemDeliveryTime+utcTimeZoneOffsetMillis(
                                    millisToDateAndHour(it.dItemDeliveryTime)
                                )
                                if(it.dItemRemindTime!=0L) {
                                    it.dItemRemindTime = it.dItemRemindTime+ utcTimeZoneOffsetMillis(
                                        millisToDateAndHour(it.dItemRemindTime)
                                    ) }
                            }


                            savedStateHandle["tasks"] = it.sortedBy { it.dItemDeliveryTime }
                                .sortedBy { it.dItemCompleted }
                        }
                        Log.i(TAG,"response GETTED!")
                    }.launchIn(viewModelScope).join()





    }
    //update task item Completion in room
    @Suppress("UNCHECKED_CAST")
    suspend fun updateTaskItemCompletion(taskToDo: ItemToDo, selectedCategory: String) : String {
        var isDone = ""
        roomInter.updateTaskItemCompletion(taskToDo).onEach {
            it.data_stored.let {
                if(it==true) {

                    isDone = taskToDo.dItemToken
                    //getTaskItemsFromRoom(selectedCategory)
                    roomInter.getTasksFromRoom(selectedCategory).onEach { roomDataState ->

                        roomDataState.data_recived.let { it as MutableList<ItemToDo>


                            it.onEach {
                                it.dItemDeliveryTime = it.dItemDeliveryTime+utcTimeZoneOffsetMillis(
                                    millisToDateAndHour(it.dItemDeliveryTime)
                                )
                                if(it.dItemRemindTime!=0L) {
                                    it.dItemRemindTime = it.dItemRemindTime+ utcTimeZoneOffsetMillis(
                                        millisToDateAndHour(it.dItemRemindTime)
                                    ) }
                            }


                            savedStateHandle["tasks"] = it.sortedBy { it.dItemDeliveryTime }
                                .sortedBy { it.dItemCompleted }
                        }
                        Log.i(TAG,"response GETTED!")
                    }.launchIn(viewModelScope).join()


                }
            }
            it.data_error.let {
                if(it=="room_error") {
                    isDone=""
                }
            }
        }.launchIn(viewModelScope).join()
        return isDone


    }
    //get index of token
    fun getin(token: String) : Int {
        val new : MutableList<ItemToDo> =  savedStateHandle["tasks"] ?: mutableListOf()
        return new.indexOf(new.find {
            it.dItemToken == token
        })
    }
    //set new DateTime
    fun setNewTaskDate(newTaskDate: Long) {
        Log.i(TAG,"Alarm set new task day: ${newTaskDate}  localnow: ${epochDaysLocalNow()}")
        //if time of new task is older then time now it is reset to null 0L
        if(newTaskDate== epochDaysLocalNow()) {


            if(taskTime.value.toInt()<localTimeNowInMilliseconds()) {

                //setNewTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
                setNewTaskTime(localTimeNowInMilliseconds())
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
        Log.i(TAG,"Calendar alarm set date: ${TimeUnit.DAYS.toMillis(newTaskDate)}")
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


    }
    //set new notDateTime
    fun setNotTaskDate(newNotDate: Long) {

        if(newNotDate== epochDaysLocalNow()) {

            if(notTime.value.toInt()<localTimeNowInMilliseconds().toInt()) {
                setNotTaskTime(localTimeNowInMilliseconds())
            }


        }
        //if date of notification is after new task date , not reset to date of task
        if(LocalDate.ofEpochDay(taskDate.value)==LocalDate.ofEpochDay(newNotDate))   {


            if(taskTime.value<notTime.value && (taskTime.value!=0L)) {
                setNotTaskTime(taskTime.value)
            }
        }

        savedStateHandle["notDate"] = newNotDate


    }
    fun setNotTaskTime(newNotTime: Long) {
        //if date of notification is after new task date is not reset to date of task
        if(LocalDate.ofEpochDay(taskDate.value)==LocalDate.ofEpochDay(notDate.value))   {

            //if((taskTime.value<newNotTime && (taskTime.value!=0L)) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.now().toEpochDay() ) {
            if((taskTime.value<newNotTime && (taskTime.value!=0L)) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.ofEpochDay(notDate.value).toEpochDay()) {
                //setNotTaskTime(taskTime.value)
                savedStateHandle["notTime"] = taskTime.value

            } else {savedStateHandle["notTime"] = newNotTime

            }
        } else{ savedStateHandle["notTime"] = newNotTime

        }



    }
    fun getTimePickerInitialTaskTime() {

            if((LocalTime.now().isBefore(LocalTime.parse(millisToHourOfDay(taskTime.value))) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.now().toEpochDay())
        //if((LocalTime.now().isBefore(LocalTime.parse("${mainTimeDate.convertMillisToHourOfDay(taskTime.value)}:${mainTimeDate.convertMillisecondsToMinutesOfHourInt(taskTime.value)}")) && LocalDate.ofEpochDay(taskDate.value).toEpochDay()==LocalDate.now().toEpochDay())
                || LocalDate.ofEpochDay(taskDate.value).toEpochDay()>LocalDate.now().toEpochDay())
            {
                //nothing

            } else {
                setNewTaskTime(localTimeNowInMilliseconds())
                //setNewTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())

            }

    }
    fun getDatePickerInitialTaskDate() {
       if(taskDate.value!=0L)
        {
            Log.i(TAG,"alarm calndar NOT set initial!")
            //nothing
       }
        else
        {
            Log.i(TAG,"alarm calndar set initial!")
            setNewTaskDate(epochDaysLocalNow())

        }

    }
    fun getTimePickerInitialNotTime() {
        if (notTime.value == 0L) {
            if(notDate.value  != 0L) {
                setNotTaskTime(localTimeNowInMilliseconds())  }
            else {
               //nothing NO TIME
            }
        }
        else {
            //check if time is before time now and date is the same!?
            if(notTime.value.toInt()<LocalTime.now().toKotlinLocalTime().toMillisecondOfDay() && LocalDate.ofEpochDay(notDate.value).toEpochDay()==LocalDate.now().toEpochDay()) {
                setNotTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
            } else {
                //nothing
            }

        }
    }
    fun setfirstViIndex(index: Int) {
        Log.i(TAG,"DATEINFO: Cooooolect set")
        firstViItemIndex.value = index
        prevActNext()
    }
    fun prevActNext() {
        prevItem.value = try {
            Log.i(TAG,"prev try:")
            tasksFromRoom.value[firstViItemIndex.value-1]
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG,"prev Exception: ${e.message}")
            ItemToDo()
        }
        actualItem.value= try {
            Log.i(TAG,"act try: ")
            tasksFromRoom.value[firstViItemIndex.value]
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG,"actual Exception: ${e.message}")
            ItemToDo()
        }
        nextItem.value  = try {
            Log.i(TAG,"next try: ")
            tasksFromRoom.value[firstViItemIndex.value + 1]
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG,"next Exception: ${e.message}")
            ItemToDo()
        }

    }


}