package com.arturlasok.maintodo.util


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arturlasok.maintodo.R


@Composable
fun AutoStartInfo(
    autoStartAlert: Boolean,
    closeAutoStartAlert:() -> Unit,
    isAutoStartPermissionAvailable: Boolean,
    is_autostart_set: Boolean,
    setAutostart:() -> Unit,
    getAutoStartPermission:() -> Unit,
) {
    val secondQ = remember { mutableStateOf(false) }
    if(autoStartAlert && isAutoStartPermissionAvailable && !is_autostart_set && !secondQ.value) {
        RemoveAlert(question = UiText.StringResource(
            R.string.autostartinfo,
            "no"
        ).asString(), onYes = { getAutoStartPermission(); secondQ.value = true }, onCancel = { closeAutoStartAlert() }) {

        }
    }
    if(secondQ.value) {
        YesNoAlert(question = UiText.StringResource(
            R.string.autostartsecondq,
            "no"
        ).asString(), onYes = {
            setAutostart(); closeAutoStartAlert(); secondQ.value = false


        }, onNo = { closeAutoStartAlert();  secondQ.value = false }) {

        }

    }

}