package com.arturlasok.maintodo.ui.editcategory_screen

import androidx.activity.compose.BackHandler
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
import com.arturlasok.maintodo.admob.AdMobBigBanner
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.addcategory_screen.CategoryDescForm
import com.arturlasok.maintodo.ui.addcategory_screen.CategoryNameForm
import com.arturlasok.maintodo.ui.addcategory_screen.CategorySelector
import com.arturlasok.maintodo.util.BackButton
import com.arturlasok.maintodo.util.RemoveAlert
import com.arturlasok.maintodo.util.TrashButton
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCategoryScreen(
    navigateTo: (route: String) -> Unit,
    categoryId :Long,
    isDarkModeOn: Boolean,
    snackMessage: (snackMessage:String) -> Unit,
    editCategoryViewModel: EditCategoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val saveButtonEnabled = rememberSaveable { mutableStateOf(true) }
    val state by editCategoryViewModel.editCategoryState.collectAsState()
    val removeAlert = rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = true) {
        navigateTo(Screen.Start.route+"/${categoryId}")
    }

    LaunchedEffect(key1 = true, block = {
        editCategoryViewModel.getOneCategoryFromRoom(categoryId)
    })
    Column {

        if(removeAlert.value) {
            RemoveAlert(
                question =  UiText.StringResource(
                    R.string.edit_alert_question,
                    "no"
                ).asString() ,
                onYes = {
                        //remove
                        scope.launch {
                            val deleteResponse = editCategoryViewModel.deleteCategory(categoryId)
                            deleteResponse.ok.let {
                                if(it==true) {
                                    removeAlert.value = false
                                    snackMessage(UiText.StringResource(
                                        R.string.editcategory_snack_deleted,
                                        "no"
                                    ).asString(editCategoryViewModel.getApplication().applicationContext))
                                    navigateTo(Screen.Start.route+"/-1L")
                                }
                            }
                            deleteResponse.error.let {
                                if (it != null) {
                                    if(it.isNotEmpty()) {
                                        removeAlert.value = false
                                        snackMessage(it)
                                    }
                                }
                            }
                        }
                        },
                onCancel = {
                        //cancel
                        removeAlert.value = false })
            {
                //on dismiss
                removeAlert.value = false
            }
        }



        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth())
                {
                    BackButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { navigateTo(Screen.Start.route+"/${categoryId}") }
                    )
                    TrashButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { removeAlert.value = true}
                    )
                }
            }
            //Screen title
            Box(Modifier.fillMaxWidth().height(48.dp), contentAlignment = Alignment.Center) {

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
                        .padding(0.dp)
                        .fillMaxSize()

                ) {
                    CategoryNameForm(
                        nameValue = state.categoryName,
                        onValueChange = editCategoryViewModel::onCategoryNameChange,
                        onDone = { keyboardController?.hide(); focusManager.clearFocus() }
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
                    focusManager.clearFocus();
                    editCategoryViewModel.onIconSelectionChange(newVal)
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
                        .padding(0.dp)
                        .fillMaxSize()

                ) {
                    CategoryDescForm(
                        nameValue = state.categoryDescription,
                        onValueChange = editCategoryViewModel::onCategoryDescriptionChange,
                        onDone = { keyboardController?.hide(); focusManager.clearFocus() }
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
                enabled = saveButtonEnabled.value,
                onClick = {

                    saveButtonEnabled.value = false
                    editCategoryViewModel.onCategoryNameChange(state.categoryName)
                    editCategoryViewModel.onCategoryDescriptionChange(state.categoryDescription)
                    editCategoryViewModel.onIconSelectionChange(state.categoryIcon)
                    keyboardController?.hide()

                    //verify form
                    editCategoryViewModel.verifyForm(categoryId).onEach { formDataState ->
                        // ok
                        formDataState.second.ok?.let {
                            snackMessage(
                                UiText.StringResource(
                                    R.string.editcategory_saved,
                                    "no"
                                ).asString(editCategoryViewModel.getApplication().applicationContext)
                            )
                            //nav to start and last added category
                            navigateTo(Screen.Start.route+"/"+categoryId)
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