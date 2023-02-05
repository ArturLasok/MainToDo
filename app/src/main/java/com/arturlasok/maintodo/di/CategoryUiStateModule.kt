package com.arturlasok.maintodo.di

import com.arturlasok.maintodo.util.CategoryUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryUiStateModule {

    @Singleton
    @Provides
    fun provideCategoryUiState() : CategoryUiState {
        return CategoryUiState
    }

}