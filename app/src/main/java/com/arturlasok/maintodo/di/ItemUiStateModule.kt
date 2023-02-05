package com.arturlasok.maintodo.di

import com.arturlasok.maintodo.util.ItemUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemUiStateModule {

    @Singleton
    @Provides
    fun provideItemUiState() : ItemUiState {
        return ItemUiState
    }
}