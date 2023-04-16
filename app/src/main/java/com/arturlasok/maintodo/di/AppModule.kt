package com.arturlasok.maintodo.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.WorkerViewModel
import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.ui.start_screen.StartViewModel
import com.arturlasok.maintodo.util.CategoryUiState
import com.arturlasok.maintodo.util.ItemUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }
    @Singleton
    @Provides
    fun provideWorkerViewModel(
        app: BaseApplication,
        categoryDao: CategoryDao,
        itemDao: ItemDao,
        roomInter: RoomInter,
    ) :WorkerViewModel {
        return WorkerViewModel(
            app = app,
            categoryDao = categoryDao,
            itemDao = itemDao,
            roomInter = roomInter
        )
    }
}