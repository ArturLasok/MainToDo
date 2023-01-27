package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun StartCategoryButton(
    modifier: Modifier,
    sizeImage: Int,
    sizeCircle: Int,
    image: Int,
    imageDesc: String,
    text: String,
    imageModifier: Modifier,
    isDarkModeOn: Boolean,
    clicked:() -> Unit,
    selected: Boolean,
    ifSelected:() -> Unit,
    startScreenState : StartScreenState,
    numberOfItems: Int,
    countItems:() -> Unit
) {
    if(selected) { ifSelected() }
    AnimatedVisibility(
        visible = startScreenState==StartScreenState.Welcome,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchedEffect(key1 = true, block = {
            countItems()
        })
            val addBadgeSize = if(selected) { if(!isDarkModeOn) { -10 } else { 10 }} else { 10 }
            Column(
                modifier = modifier.width((sizeCircle).dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BadgedBox(modifier = Modifier.width((sizeImage+addBadgeSize).dp),
                    badge = {
                        if(numberOfItems>0) {
                            Badge {

                                Text(
                                    text = numberOfItems.toString(),
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "$numberOfItems new notifications"
                                        }
                                )
                            }
                        }
                    }) {

                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(sizeCircle.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDarkModeOn) {
                                MaterialTheme.colors.onSecondary
                            } else {
                                MaterialTheme.colors.onPrimary
                            }
                        )
                        .clickable(onClick = { clicked() })


                ) {

                        Image(
                            contentScale = ContentScale.Crop,
                            bitmap = ImageBitmap.imageResource(id = image),
                            contentDescription = imageDesc,
                            modifier = imageModifier
                                .size(sizeImage.dp)
                                .zIndex(0.9f),
                            colorFilter = if (isDarkModeOn) {
                                if (selected) {
                                    null
                                    //ColorFilter.tint(MaterialTheme.colors.primaryVariant)

                                } else {
                                    ColorFilter.tint(Color.DarkGray)
                                }
                            } else {
                                if(selected) { null } else {
                                     ColorFilter.tint(Color.LightGray)
                                }
                            }
                        )



                }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        text = text,
                        style = MaterialTheme.typography.h5.copy(fontWeight = if(selected) {
                            FontWeight.Bold} else {
                            FontWeight.Normal}),
                        color =  MaterialTheme.colors.primary ,
                        textAlign = TextAlign.Center,


                    )



        }

    }
}