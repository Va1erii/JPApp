package com.vpopov.jpapp.di

import com.vpopov.jpapp.network.NPointClient
import com.vpopov.jpapp.persistence.CityDao
import com.vpopov.jpapp.persistence.FoodDao
import com.vpopov.jpapp.repository.DetailsRepository
import com.vpopov.jpapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        nPointClient: NPointClient,
        cityDao: CityDao,
        foodDao: FoodDao
    ): MainRepository {
        return MainRepository(nPointClient, cityDao, foodDao)
    }

    @Provides
    @ViewModelScoped
    fun provideDetailsRepository(
        foodDao: FoodDao,
        cityDao: CityDao
    ): DetailsRepository {
        return DetailsRepository(foodDao, cityDao)
    }
}