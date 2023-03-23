package com.arturlasok.maintodo.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.arturlasok.maintodo.BaseApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UstawieniaDataStore @Inject constructor(app: BaseApplication) {

    val DARK_OPT = intPreferencesKey("dark_theme_on")

    val CONFIRM_TASK_STATUS = booleanPreferencesKey("confirm_task_status")

    val APP_ADDED_TO_AUTOSTART = booleanPreferencesKey("app_added_to_autostart")

}