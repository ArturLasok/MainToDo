package com.arturlasok.maintodo

sealed class MainActivityUiState {
    object Loading : MainActivityUiState()
    data class ScreenReady(val data: Boolean) : MainActivityUiState()

}
