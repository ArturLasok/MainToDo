package com.arturlasok.maintodo.interactors.util

class RoomDataState<out T>(
    val data_stored: Boolean? = null,
    val data_recived: Any? = null,
    val data_error: String? = null,
) {
    companion object {
        fun <T> data_stored(data_stored: Boolean) : RoomDataState<T>
        {
            return RoomDataState(data_stored = data_stored)
        }
        fun <T> data_recived(data_recived: Any) : RoomDataState<T>
        {
            return  RoomDataState(data_recived = data_recived)
        }
        fun <T> data_error(data_error: String) : RoomDataState<T> {

            return RoomDataState(data_error =data_error)

        }
    }

}