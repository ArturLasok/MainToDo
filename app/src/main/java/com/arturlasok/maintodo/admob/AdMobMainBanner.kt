package com.arturlasok.maintodo.admob

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdMobMainBanner() {

    Spacer(modifier = Modifier
        .height(10.dp)
        .fillMaxWidth())
    Column(
        modifier = Modifier.height(50.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        val isInEditMode = LocalInspectionMode.current
        if (isInEditMode) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Advert Here",
            )
        } else {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-6396271874155408/3182260592"
                        //context.getString(R.string.ad_id_banner)
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }


    }


}



