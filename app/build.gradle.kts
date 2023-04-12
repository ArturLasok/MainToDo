plugins {
    id("com.android.application")
    id("kotlin-kapt")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.arturlasok.maintodo"
    compileSdk = 32
    defaultConfig {
        applicationId = "com.arturlasok.maintodo"
        minSdk = 26
        targetSdk = 32
        versionCode = 15
        versionName = "15.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
         isMinifyEnabled = false


        }

    }
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //Room
    val room_version = "2.4.3"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    //Splash
    implementation("androidx.core:core-splashscreen:1.0.0")
    //Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.18.0")
    //data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")
    //compose
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    //kotlin date time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    //hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    //Work Manager
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    //nav
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:31.2.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    //admob
    implementation ("com.google.android.gms:play-services-ads:21.4.0")
    //fb messaging
    implementation ("com.google.android.ump:user-messaging-platform:2.0.0")
    //dateTimepicker
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    implementation ("com.github.judemanutd:autostarter:1.1.0")
}