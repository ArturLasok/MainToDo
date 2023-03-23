package com.arturlasok.maintodo

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReScheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val workerViewModel: WorkerViewModel
) : Worker(context,workerParameters) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }


}