package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.util.UiText
import com.arturlasok.maintodo.util.milisToDayOfWeek
import com.arturlasok.maintodo.util.millisToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.nio.file.Files.find
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val application: BaseApplication,
    private val savedStateHandle: SavedStateHandle,
    private val roomInter: RoomInter
) : ViewModel() {


    init {
        getCategoriesFromRoom()
    }
    val selectedCategoryRowIndex =  mutableStateOf(0)
    //selected category -1 All visible
    val selectedCategory = savedStateHandle.getStateFlow("selectedCategory",-1L)
    // category list from db
    val categoriesFromRoom = savedStateHandle.getStateFlow("categories", emptyList<CategoryToDo>())
    //start screen ui stat
    val startScreenUiState :MutableState<StartScreenState> = mutableStateOf(StartScreenState.Welcome)


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
    //get categories list
    private fun getCategoriesFromRoom() {

        roomInter.getCategoryFromRoom().onEach { roomDataState ->

            roomDataState.data_recived.let {
                savedStateHandle["categories"] = it
            }

        }.launchIn(viewModelScope)

    }
    //selected category
    fun setSelectedCategory(categoryId: Long) {

        savedStateHandle["selectedCategory"] = categoryId

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

}