package com.arturlasok.maintodo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.System.getConfiguration
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.arturlasok.maintodo.navigation.NavigationComponent
import com.arturlasok.maintodo.ui.theme.MainToDoTheme
import com.arturlasok.maintodo.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


// Datastore init
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ustawienia")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //Internet Monitor
    @Inject
    lateinit var isOnline: isOnline
    //viewModel
    private val viewModel: MainActivityViewModel by viewModels()
    //snackbar controller
    private val snackbarController = SnackbarController(lifecycleScope)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //ui State
        var thisuiState: MainActivityUiState by mutableStateOf(MainActivityUiState.SplashScreen)


        //Splash screen
        splashScreen.setKeepOnScreenCondition {
          when(thisuiState) {
              MainActivityUiState.SplashScreen -> true
              is MainActivityUiState.Loading -> false
              is MainActivityUiState.ScreenReady -> false

          }
        }

        //Data Store dark theme // 0 - default, 1 - light, 2 - dark
        val IS_DARK_THEME = intPreferencesKey("dark_theme_on")
        val dataFromStore : Flow<Int> =  applicationContext.dataStore.data.map { pref->
            pref[IS_DARK_THEME] ?: 0
        }
        //Internet On?
        isOnline.runit()

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        thisuiState = it
                        Log.i(TAG,"Data is changed in MA")
                    }.launchIn(this)
            }
        }

        //gradient for themes
        val lightGradient = Brush.verticalGradient(
            listOf(Color(0xFFFFD6FA), Color(0xFFFFEBFA))
        )
        val darkGradient = Brush.verticalGradient(
            listOf( Color(0xFF0A0A0A), Color(0xFF181414))
        )
        //gradient for selected theme
        var selectedGradient = lightGradient

        //padding top when landscape only
        var statusBarPaddingTop = 0
        var statusBarPaddingLeft = 0
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            statusBarPaddingTop = 16
            statusBarPaddingLeft = 48
        }

        setContent {
            //navController
            val navController = rememberNavController()
            //accompanist for system bars controller
            val systemUiController = rememberSystemUiController()
            // data store for dark theme
            val dataStoreDarkTheme = dataFromStore.collectAsState(0)
            MainToDoTheme(dataStoreDarkTheme.value) {
                //theme?
                when(dataStoreDarkTheme.value) {

                    //default
                    0 -> {
                        if(isSystemInDarkTheme()) {
                            selectedGradient = darkGradient
                            systemUiController.setStatusBarColor(Color(0xFF0A0A0A))
                            systemUiController.setNavigationBarColor(Color(0xFF181414))
                        }
                        else {
                            selectedGradient = lightGradient
                            systemUiController.setStatusBarColor(Color(0xFFFFD6FA))
                            systemUiController.setNavigationBarColor(Color(0xFFFFEBFA))
                        }
                    }
                    //light
                    1 -> {
                        selectedGradient = lightGradient
                        systemUiController.setStatusBarColor(Color(0xFFFFD6FA))
                        systemUiController.setNavigationBarColor(Color(0xFFFFEBFA))

                    }
                    //dark
                    2 -> {
                        selectedGradient = darkGradient
                        systemUiController.setStatusBarColor(Color(0xFF0A0A0A))
                        systemUiController.setNavigationBarColor(Color(0xFF181414))
                    }
                }

                //Screen is ready!
                LaunchedEffect(key1 = true, block = {
                    viewModel.setScreenIsReady(true)

                } )
                //scaffoldState init
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = {scaffoldState.snackbarHostState},
                )
                {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                selectedGradient
                            )
                            .padding(
                                top = statusBarPaddingTop.dp,
                                start = statusBarPaddingLeft.dp,
                                end = statusBarPaddingLeft.dp
                            )

                    ) {
                        DefaultSnackbar(
                            snackbarHostState = scaffoldState.snackbarHostState,
                            onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                            modifier = Modifier
                                .zIndex(1.0f)
                                .padding(
                                    top = statusBarPaddingTop.dp
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                //.zIndex(0.9f)
                                .padding(16.dp)
                        ) {

                            NavigationComponent(
                                navController = navController,
                                snackMessage = {

                                    messageToShow: String -> snackbarController.getScope().launch {


                                    launch {

                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = messageToShow,
                                            actionLabel = "OK"
                                        )

                                    }
                                }
                                },
                                setCurrentDestination = { newDestination ->
                                    viewModel.setCurrentDestinationRoute(newDestination)
                                },
                                currentDestination = navController.currentDestination?.route
                                    ?: "Start",
                                isDarkModeOn = dataStoreDarkTheme.value,
                                changeDarkMode = { newVal ->
                                    lifecycleScope.launch {
                                        applicationContext.dataStore.edit { settings ->
                                            val currentStoreValue = newVal
                                            settings[IS_DARK_THEME] = currentStoreValue
                                            viewModel.setDarkActiveTo(
                                                settings[IS_DARK_THEME] ?: 0
                                            )
                                        }
                                    }
                                },
                                runLink = { link ->
                                    val intentLink = Intent(Intent.ACTION_VIEW)
                                    intentLink.data = Uri.parse(link)
                                    startActivity(intentLink)

                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

