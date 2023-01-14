package com.arturlasok.maintodo.util

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController (
    private val scope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    init {
        cancelActiveJob()
    }
    fun getScope() = scope

    fun showSnackbar(
        scaffoldState: ScaffoldState,
        message: String,
        actionLabel: String,
    ) {
        if(snackbarJob == null) {
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
                cancelActiveJob()
            }

        }
        else
        {
            cancelActiveJob()
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short
                )
                cancelActiveJob()
            }

        }

    }
    private fun cancelActiveJob() {
        snackbarJob?.let { job->
            job.cancel()
            snackbarJob = Job()


        }
    }
}