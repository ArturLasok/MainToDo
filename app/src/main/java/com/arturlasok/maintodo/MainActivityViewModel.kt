package com.arturlasok.maintodo

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {
    //dark mode
    private val isDarkActive = savedStateHandle.getStateFlow("isDarkActive",0)
    //isScreen ready
    private val isScreenIsAvailable = savedStateHandle.getStateFlow("isScreenIsAvailable", false)
    //actual destination
    private val currentDestination = savedStateHandle.getStateFlow("currentDestination","")

    val uiState : Flow<MainActivityUiState> = combine(isScreenIsAvailable, currentDestination) { isScreenIsAvailable, currentDestination ->
        Log.i(TAG,"Data is changed VM")

        if(!isScreenIsAvailable) { MainActivityUiState.SplashScreen } else {
        MainActivityUiState.ScreenReady(true) }



    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), MainActivityUiState.SplashScreen)

    fun setDarkActiveTo(newVal:Int) {
       savedStateHandle["isDarkActive"] = newVal
    }
    fun setScreenIsReady(newVal: Boolean) {
        savedStateHandle["isScreenIsAvailable"] = newVal
    }
    //Set Current Dest
    fun setCurrentDestinationRoute(route: String)
    {
        savedStateHandle["currentDestination"] = route
    }
    //Get Current Dest
    fun getCurrentDestinationRoute() : StateFlow<String>
    {
        return  currentDestination
    }

}