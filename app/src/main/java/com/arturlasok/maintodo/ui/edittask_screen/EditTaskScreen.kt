package com.arturlasok.maintodo.ui.edittask_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import com.arturlasok.maintodo.admob.AdMobBigBanner
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.start_screen.CategoryRow
import com.arturlasok.maintodo.ui.start_screen.StartScreenState
import com.arturlasok.maintodo.ui.start_screen.TaskDescForm
import com.arturlasok.maintodo.ui.start_screen.TaskNameForm
import com.arturlasok.maintodo.util.BackButton
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTaskScreen(navigateTo: (route: String) -> Unit,
                   taskId :Long,
                   isDarkModeOn: Boolean,
                   categoryId: Long,
                   snackMessage: (snackMessage:String) -> Unit,
                   editTaskViewModel: EditTaskViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val saveButtonEnabled = rememberSaveable { mutableStateOf(true) }
    val state by editTaskViewModel.editItemState.collectAsState()

    BackHandler(enabled = true) {
        navigateTo(Screen.Start.route+"/${categoryId}")
    }
    LaunchedEffect(key1 = true, block = {
        editTaskViewModel.getOneItemFromRoom(itemId = taskId)
        editTaskViewModel.getCategoriesFromRoom()
    })
    Column {


    Box() {

        //Back button
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth())
            {
                BackButton(
                    isDarkModeOn = isDarkModeOn,
                    modifier = Modifier,
                    onClick = { navigateTo(Screen.Start.route+"/${categoryId}") }
                )

            }
        }
        //Screen title
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp), contentAlignment = Alignment.Center) {

            Text(
                text = UiText.StringResource(
                    R.string.edittask_screen,
                    "no"
                ).asString(),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )
        }

    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TaskNameForm(
            nameValue = state.itemName,
            onValueChange = { text -> editTaskViewModel.onItemNameChange(text) },
            isDarkModeOn = isDarkModeOn,
            onDone = { keyboardController?.hide() }
        )
        CategoryRow(
            isDarkModeOn = isDarkModeOn,
            categoryRowState = rememberLazyListState(),
            categoryList = editTaskViewModel.categoriesFromRoom.collectAsState().value,
            selectedCategory = state.itemCategory,
            onClick = { itemId ->
                focusManager.clearFocus();
                editTaskViewModel.onItemCategoryChange(itemId)
            },
            startScreenUiState = StartScreenState.AddTask,
            navigateTo = { },
            numberOfItems = remember{ mutableStateListOf() },
            countItems = {}
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        TaskDescForm(
            nameValue = state.itemDescription,
            onValueChange = { text -> editTaskViewModel.onItemDescriptionChange(text) },
            isDarkModeOn = isDarkModeOn,
            onDone = { keyboardController?.hide() }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        //
        //Add button
        //
        Button(
            enabled = saveButtonEnabled.value,
            onClick = {

                saveButtonEnabled.value = false
                editTaskViewModel.onItemNameChange(state.itemName)
                editTaskViewModel.onItemCategoryChange(state.itemCategory)
                editTaskViewModel.onItemDescriptionChange(state.itemDescription)
                keyboardController?.hide()

                //verify form
                editTaskViewModel.verifyForm().onEach { formDataState ->
                    // ok
                    formDataState.second.ok?.let {
                        snackMessage(
                            UiText.StringResource(
                                R.string.edittask_updated,
                                "no"
                            ).asString(editTaskViewModel.getApplication().applicationContext)
                        )
                        //nav to start and last added category
                        navigateTo(Screen.Start.route + "/" + state.itemCategory)
                    }
                    // error
                    formDataState.second.error?.let {
                        //snack message with error
                        snackMessage(it)
                        saveButtonEnabled.value = true
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
                    R.string.editcategory_save,
                    "no"
                ).asString().uppercase(),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        AdMobBigBanner()
    }

    }
}