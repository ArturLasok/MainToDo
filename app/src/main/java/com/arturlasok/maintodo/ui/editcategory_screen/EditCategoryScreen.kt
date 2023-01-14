package com.arturlasok.maintodo.ui.editcategory_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.settings_screen.BackButton
import com.arturlasok.maintodo.util.UiText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCategoryScreen(
    navigateTo: (route: String) -> Unit,
    categoryId :Long,
    isDarkModeOn: Boolean,
    snackMessage: (snackMessage:String) -> Unit,
    editCategoryViewModel: EditCategoryViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val saveButtonEnabled = rememberSaveable { mutableStateOf(true) }
    val state by editCategoryViewModel.editCategoryState.collectAsState()


    LaunchedEffect(key1 = true, block = {
        editCategoryViewModel.getOneCategoryFromRoom(categoryId)
    })
    Column {

        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                BackButton(
                    isDarkModeOn = isDarkModeOn,
                    modifier = Modifier,
                    //TODO nav back to catid opened
                    onClick = { navigateTo(Screen.Start.route) }
                )
            }
            //Screen title
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {

                Text(
                    text = UiText.StringResource(
                        R.string.app_editcategory,
                        "no"
                    ).asString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary
                )
            }

        }
    }
}