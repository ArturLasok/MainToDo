package com.arturlasok.maintodo.di

import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.util.ItemUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Singleton
    @Provides
    fun providesRoomInter(
        categoryDao: CategoryDao,
        itemDao: ItemDao,
        itemUiState: ItemUiState
    ) : RoomInter {
        return RoomInter(
            categoryDao = categoryDao,
            itemDao = itemDao,
            itemUiState = itemUiState
        )
    }

    @Singleton
    @Provides
    fun providesMainTimeDate(): MainTimeDate {
        return MainTimeDate()
    }


}