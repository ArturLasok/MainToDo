package com.arturlasok.maintodo.ui.addcategory_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.settings_screen.BackButton
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddCategoryScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    snackMessage: (snackMessage:String) -> Unit,
    addCategoryViewModel: AddCategoryViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val state by addCategoryViewModel.newCategoryState.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val addButtonEnabled = rememberSaveable { mutableStateOf(true) }


    Column {

        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                BackButton(
                    isDarkModeOn = isDarkModeOn,
                    modifier = Modifier,
                    onClick = { navigateTo(Screen.Start.route) }
                )
            }
            //Screen title
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {

                Text(
                    text = UiText.StringResource(
                        R.string.app_addcategory,
                        "no"
                    ).asString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary
                )
            }

        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {


            //
            //add name surface
            //
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Text(
                text = UiText.StringResource(
                    R.string.addcategory_name,
                    "no"
                ).asString(),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 10.dp, bottom = 4.dp),
                color = MaterialTheme.colors.primary
            )
            Surface(
                modifier = Modifier
                    .padding(4.dp)
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = if (isDarkModeOn) {
                    MaterialTheme.colors.onSecondary
                } else {
                    MaterialTheme.colors.surface
                },
                elevation = 10.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxSize()

                ) {
                    CategoryNameForm(
                        nameValue = state.categoryName,
                        onValueChange = addCategoryViewModel::onCategoryNameChange,
                        onDone = { keyboardController?.hide() }
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Text(
                text = UiText.StringResource(
                    R.string.addcategory_icon,
                    "no"
                ).asString(),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 10.dp),
                color = MaterialTheme.colors.primary
            )

            //
            //Icon selector
            //
            CategorySelector(
                isDarkModeOn = isDarkModeOn,
                selected = state.categoryIcon,
                selectedNewVal = { newVal ->
                    focusManager.clearFocus(); addCategoryViewModel.onIconSelectionChange(
                    newVal
                )
                }
            )
            //
            //add description surface
            //
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            Text(
                text = UiText.StringResource(
                    R.string.addcategory_desc,
                    "no"
                ).asString(),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 10.dp, bottom = 4.dp),
                color = MaterialTheme.colors.primary
            )
            Surface(
                modifier = Modifier
                    .padding(4.dp)
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = if (isDarkModeOn) {
                    MaterialTheme.colors.onSecondary
                } else {
                    MaterialTheme.colors.surface
                },
                elevation = 10.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .fillMaxSize()

                ) {
                    CategoryDescForm(
                        nameValue = state.categoryDescription,
                        onValueChange = addCategoryViewModel::onCategoryDescriptionChange,
                        onDone = { keyboardController?.hide() }
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
            //
            //Add button
            //
            Button(
                enabled = addButtonEnabled.value,
                onClick = {
                    addButtonEnabled.value = false
                    addCategoryViewModel.onCategoryNameChange(state.categoryName)
                    addCategoryViewModel.onIconSelectionChange(state.categoryIcon)
                    keyboardController?.hide()

                    addCategoryViewModel.verifyForm().onEach { formDataState ->

                        formDataState.ok?.let {
                            snackMessage(
                                UiText.StringResource(
                                    R.string.addcategory_form_save,
                                    "no"
                                ).asString(addCategoryViewModel.getApplication().applicationContext)
                            )
                            navigateTo(Screen.Start.route)
                        }
                        formDataState.error?.let {
                            snackMessage(it)
                            addButtonEnabled.value = true
                        }

                    }.launchIn(scope)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = UiText.StringResource(
                        R.string.addcategory_add,
                        "no"
                    ).asString().uppercase(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}
