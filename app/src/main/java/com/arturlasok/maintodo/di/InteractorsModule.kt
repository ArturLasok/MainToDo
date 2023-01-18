package com.arturlasok.maintodo.di

import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.interactors.RoomInter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun providesRoomInter(
        categoryDao: CategoryDao,
        itemDao: ItemDao
    ) : RoomInter {
        return RoomInter(
            categoryDao = categoryDao,
            itemDao = itemDao
        )
    }


}