package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.navigation.Screen

@Composable
fun CategoryDetails(
    selectedCategoryDetails: CategoryToDo,
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    modifier: Modifier,
) {

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = 10.dp,
        color = if(isDarkModeOn) { MaterialTheme.colors.onSecondary } else { Color.White }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth(fraction = 0.9f)) {
                Text(
                    text = selectedCategoryDetails.dCatName ?: "",
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = selectedCategoryDetails.dCatDescription ?: "",
                    style = MaterialTheme.typography.h4

                )
            }
            SettingsButton(isDarkModeOn = isDarkModeOn, modifier = Modifier.padding(2.dp).alpha(0.3f)) {
                navigateTo(Screen.EditCategory.route+"/${selectedCategoryDetails.dCatId}")
            }

        }
    }


}