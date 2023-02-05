package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.delay

@Composable
fun CategoryDetails(
    visible: Boolean,
    selectedCategoryDetails: CategoryToDo,
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    modifier: Modifier,
) {
    val localVisible = rememberSaveable{ mutableStateOf(false) }
    LaunchedEffect(key1 = visible, block = {

        if(localVisible.value) {
            localVisible.value = visible
        } else {
            delay(500)
            localVisible.value = visible
        }
    })
    //Animate category details
    AnimatedVisibility(
        visible = localVisible.value
        ,
        enter = slideInHorizontally(
            initialOffsetX = {
                it
            },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
                delayMillis = 0
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            elevation = 4.dp,
            color = if (isDarkModeOn) {
                MaterialTheme.colors.onSecondary
            } else {
                Color.White
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if(!selectedCategoryDetails.dCatName.isNullOrEmpty())
                        {
                            selectedCategoryDetails.dCatName
                        }
                        else {
                            UiText.StringResource(
                                R.string.all_tasks,
                                "no"
                            ).asString()
                             },
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (selectedCategoryDetails.dCatDescription?.isNotEmpty() == true) {
                        Text(
                            text = selectedCategoryDetails.dCatDescription ?: "",
                            style = MaterialTheme.typography.h4

                        )
                    }
                }
                if(selectedCategoryDetails.dCatId != null) {
                    SettingsButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier.padding(2.dp).alpha(0.5f),
                        light_img = R.drawable.edit_light,
                        dark_img = R.drawable.edit_light,
                        onClick = { navigateTo(Screen.EditCategory.route) }
                    )
                }
            }
        }

    }
}