package com.arturlasok.maintodo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WorkerViewModel @Inject constructor(
    private val app: BaseApplication,
    private val categoryDao: CategoryDao,
    private val itemDao: ItemDao,
    private val roomInter: RoomInter
) : ViewModel() {



    fun rescheduleAllAlarms(addAlarm:(item: ItemToDo)->Unit ) {
        Log.i(TAG,"Boot rescheduling")

       roomInter.getTasksNotCompletedFromRoom().onEach { taskList->

           taskList.onEach { task->

               if(task.dItemRemindTime>MainTimeDate.systemCurrentTimeInMillis() || task.dItemDeliveryTime>MainTimeDate.systemCurrentTimeInMillis()) {
                   Log.i(TAG,"Rescheduling task ${task.dItemTitle}")
                   addAlarm(task)
               }
           }

       }.launchIn(viewModelScope)


    }


}