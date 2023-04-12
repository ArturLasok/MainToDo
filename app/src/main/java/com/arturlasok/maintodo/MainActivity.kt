package com.arturlasok.maintodo

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.PermissionChecker.checkSelfPermission
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
import com.arturlasok.maintodo.admob.AdMobMainBanner
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.util.*
import com.arturlasok.maintodo.interactors.util.MainTimeDate.convertMillisToHourFromGmt
import com.arturlasok.maintodo.interactors.util.MainTimeDate.utcTimeZoneOffsetMillis
import com.arturlasok.maintodo.navigation.NavigationComponent
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.theme.MainToDoTheme
import com.arturlasok.maintodo.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.analytics.FirebaseAnalytics
import com.judemanutd.autostarter.AutoStartPermissionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDate.Companion.fromEpochDays
import kotlinx.datetime.toKotlinLocalTime
import java.text.DateFormat
import java.time.*
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


// Datastore init
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ustawienia")

//Permission
const val PERMISSION_REQUEST_SCHEDULE_EXACT_ALARMS = 0
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //Internet Monitor
    @Inject
    lateinit var isOnline: isOnline
    //viewModel
    private val viewModel: MainActivityViewModel by viewModels()
    //from intent task id
    private val intentTaskId = mutableStateOf(0L)
    //snackbar controller
    private val snackbarController = SnackbarController(lifecycleScope)
    //Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    //Rodo check
    private lateinit var consentInformation: ConsentInformation
    private lateinit var consentForm: ConsentForm
    fun checkRodo() {
        // ADMOB RODO
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                if (consentInformation.isConsentFormAvailable()) {
                    loadRodoForm();
                }
            },
            {
                // Handle the error.
            })
    }
    open fun loadRodoForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm -> this@MainActivity.consentForm = consentForm

                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(
                        this@MainActivity
                    ) { // Handle dismissal by reloading form.
                        loadRodoForm()
                    }
                }

            }
        ) {
            // Handle the error
        }
    }
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //ui State
        var thisuiState: MainActivityUiState by mutableStateOf(MainActivityUiState.SplashScreen)
        //auto start alert
        val autoStartAlert = mutableStateOf(false)

        //Splash screen
        splashScreen.setKeepOnScreenCondition {
          when(thisuiState) {
              MainActivityUiState.SplashScreen -> true
              is MainActivityUiState.Loading -> false
              is MainActivityUiState.ScreenReady -> false

          }
        }
        //Data Store app added to autostart
        val IS_AUTOSTART = booleanPreferencesKey("app_added_to_autostart")
        val autostartFromStore: Flow<Boolean> = applicationContext.dataStore.data.map { pref->
            pref[IS_AUTOSTART] ?: false
        }
        //Data Store confirm task status
        val IS_CONFIRM_STATUS = booleanPreferencesKey("confirm_task_status")
        val confirmTaskFromStore: Flow<Boolean> = applicationContext.dataStore.data.map { pref->
            pref[IS_CONFIRM_STATUS] ?: false
        }
        //Data Store dark theme // 0 - default, 1 - light, 2 - dark
        val IS_DARK_THEME = intPreferencesKey("dark_theme_on")
        val dataFromStore : Flow<Int> =  applicationContext.dataStore.data.map { pref->
            pref[IS_DARK_THEME] ?: 0
        }
        //Internet On?
        isOnline.runit()
        // ADS init
        MobileAds.initialize(this) {}
        var adRequest = AdRequest.Builder().build()

        //check intents
        checkIntents(intent)
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
          //  listOf(Color(0xFFFFD6FA), Color(0xFFFFEBFA))
            listOf(Color(0xFFB5E6CD), Color(0xFFFFEBFA))
        )
        val darkGradient = Brush.verticalGradient(
            listOf( Color(0xFF000000), Color(0xFF181414))
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
        //analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContent {
            //navController
            val navController = rememberNavController()
            //accompanist for system bars controller
            val systemUiController = rememberSystemUiController()
            // data store for dark theme
            val dataStoreDarkTheme = dataFromStore.collectAsState(0)
            //confirmation task setting
            val confirmTaskStore = confirmTaskFromStore.collectAsState(initial = false)
            //autostart is set
            val autostartFromDataStore = autostartFromStore.collectAsState(initial = false)
            MainToDoTheme(dataStoreDarkTheme.value) {
                //theme?
                when(dataStoreDarkTheme.value) {

                    //default
                    0 -> {
                        if(isSystemInDarkTheme()) {
                            selectedGradient = darkGradient
                            systemUiController.setStatusBarColor(Color(0xFF000000))
                            systemUiController.setNavigationBarColor(Color(0xFF181414))
                        }
                        else {
                            selectedGradient = lightGradient
                            //systemUiController.setStatusBarColor(Color(0xFFFFD6FA))
                            systemUiController.setStatusBarColor(Color(0xFFB5E6CD))
                            systemUiController.setNavigationBarColor(Color(0xFFFFEBFA))
                        }
                    }
                    //light
                    1 -> {
                        selectedGradient = lightGradient
                       // systemUiController.setStatusBarColor(Color(0xFFFFD6FA))
                        systemUiController.setStatusBarColor(Color(0xFFB5E6CD))
                        systemUiController.setNavigationBarColor(Color(0xFFFFEBFA))

                    }
                    //dark
                    2 -> {
                        selectedGradient = darkGradient
                        systemUiController.setStatusBarColor(Color(0xFF000000))
                        systemUiController.setNavigationBarColor(Color(0xFF181414))
                    }
                }

                //Screen is ready!
                LaunchedEffect(key1 = true, block = {
                    viewModel.setScreenIsReady(true)


                        viewModel.rescheduleAllAlarms(
                            addAlarm = { item->
                                addAlarm(
                                    item.dItemRemindTime+ utcTimeZoneOffsetMillis(
                                        millisToDateAndHour(item.dItemRemindTime)
                                    ),
                                    item.dItemDeliveryTime+ utcTimeZoneOffsetMillis(
                                        millisToDateAndHour(item.dItemDeliveryTime)
                                    ),
                                    item.dItemTitle,
                                    item.dItemDescription,
                                    item.dItemToken,
                                    item.dItemId ?: 0
                                )
                            }
                        )






                } )
                //intent task id is passed from notification
                LaunchedEffect(key1 = intentTaskId.value, block = {
                    if(intentTaskId.value>0) {
                        navController.navigate(Screen.Start.route)

                    }
                } )

                //scaffoldState init
                val scaffoldState = rememberScaffoldState()
                checkRodo()

                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = {scaffoldState.snackbarHostState},
                    topBar = {
                    },
                    bottomBar = {
                        AdMobMainBanner()
                    }
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
                                end = statusBarPaddingLeft.dp,
                                bottom = it.calculateBottomPadding()
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
                            AutoStartInfo(
                                autoStartAlert = autoStartAlert.value,
                                closeAutoStartAlert = { autoStartAlert.value = false},
                                //TODO
                                //is_autostart_set = autostartFromDataStore.value,
                                is_autostart_set = true,
                                setAutostart = {
                                    lifecycleScope.launch {
                                        applicationContext.dataStore.edit { settings->
                                            settings[IS_AUTOSTART] = true
                                        }
                                    }
                                },
                                isAutoStartPermissionAvailable =AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this@MainActivity),
                                getAutoStartPermission = { AutoStartPermissionHelper.getInstance().getAutoStartPermission(this@MainActivity) }
                            )
                            NavigationComponent(
                                taskIdFromIntent = intentTaskId,
                                setTaskIdFromIntent = { id-> intentTaskId.value = id },
                                navController = navController,
                                addAlarm = { time, beganTime, name, desc, token, id->
                                    autoStartAlert.value = true
                                    addAlarm(
                                        time = time,
                                        beganTime = beganTime,
                                        name = name,
                                        desc = desc,
                                        token = token,
                                        taskId = id
                                        )

                                },
                                removeAlarm = { taskId->
                                    removeAlarm(taskId)
                                },
                                getPermission = { checkScheduleExactAlarmsPermission() },
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
                                confirmationTaskSetting = confirmTaskStore.value,
                                changeConfirmationTaskSetting= {
                                   lifecycleScope.launch {
                                       applicationContext.dataStore.edit { settings->
                                           settings[IS_CONFIRM_STATUS] = !confirmTaskStore.value

                                       }
                                   }
                                },
                                changeDarkMode = { newVal ->
                                    lifecycleScope.launch {
                                        applicationContext.dataStore.edit { settings->
                                            settings[IS_AUTOSTART] = false
                                        }
                                    }


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
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntents(intent)
    }
    //onResume
    override fun onResume() {

        super.onResume()
    }
    //ALARMS
    private fun addAlarm(time: Long, beganTime: Long, name: String, desc:String, token: String, taskId: Long) {

            if(time>MainTimeDate.systemCurrentTimeInMillis()) {
            //Log.i(TAG,"REMIND ALARM alarm remind${convertMillisToHourFromGmt(time)} Task name $name, token $token, id: ${taskId.unaryMinus()}")
            makeAlarm(time,name,desc,token,taskId.unaryMinus())

        }


            if(beganTime>MainTimeDate.systemCurrentTimeInMillis()){
            //Log.i(TAG,"Beg: ${beganTime} =? sys: ${MainTimeDate.systemCurrentTimeInMillis()} // ${convertMillisToHourFromGmt(MainTimeDate.systemCurrentTimeInMillis())}DELIVERY BEGAN ALARM  beganTime Local: ${convertMillisToHourFromGmt(beganTime)} Task name $name, token $token, id: $taskId ")
            makeAlarm(beganTime,name,desc,token,taskId)

        }
        else {
            // no action
        }

    }
    private fun removeAlarm(id: Int) {
        //Log.i(TAG,"remove alarm $id")
        val alarmManager = this@MainActivity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, id,  intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)


    }

    private fun makeAlarm(alarmTime: Long, taskName: String, taskDesc: String,taskToken: String, taskId: Long) {
        //Log.i(TAG,"ALARM TIME ZONE OFFSET(s): ${ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds}")
        val taskInfo = ItemToDo(dItemTitle = taskName, dItemId = taskId, dItemToken = taskToken, dItemDescription = taskDesc)
        // creating alarmManager instance
        val alarmManager = this@MainActivity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
        intent.putExtra("task_info", taskInfo)
        val pendingIntentX = PendingIntent.getBroadcast(this@MainActivity, (taskInfo.dItemId?.toInt()?: 0),  intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntentX)
        pendingIntentX.cancel()
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, (taskInfo.dItemId?.toInt()?: 0),  intent, PendingIntent.FLAG_IMMUTABLE)
        val mainActivityIntent = Intent(this@MainActivity, MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(this@MainActivity, (taskInfo.dItemId?.toInt()?: 0), mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
        // creating clockInfo instance
        val clockInfo = AlarmManager.AlarmClockInfo(alarmTime, basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }
    //PERMISSION FOR SCHEDULE EXACT ALARMS
    private fun checkScheduleExactAlarmsPermission() {
        if(checkSelfPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)==PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"Permission granted for set exact alarms")
        }
        else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM),
            PERMISSION_REQUEST_SCHEDULE_EXACT_ALARMS)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_SCHEDULE_EXACT_ALARMS) {

            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted


                Log.i(
                    TAG,
                    "Permission granted to Exact alarms"
                )
            } else {
                // Permission request was denied

                Log.i(
                    TAG,
                    "Permission NOT!!!! granted to Exact alarms"
                )
            }
        }
    }
    //Check intents
    fun checkIntents(intent: Intent?) {

        if (intent != null) {
            Log.i(TAG, "Take ${intent.toString()} // ${intent.getLongExtra("show_task",0)}")
            val intentExtraTaskId = intent.getLongExtra("show_task",0)
            if(intentExtraTaskId>0) {
                intentTaskId.value = intentExtraTaskId

            }
        }

    }

}

