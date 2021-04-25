package com.vpopov.jpapp.ui.details.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpopov.jpapp.repository.DetailsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CityViewModel @AssistedInject constructor(
    private val detailsRepository: DetailsRepository,
    @Assisted val name: String
) : ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(name: String): CityViewModel
    }

    companion object {
        fun provideViewModelFactory(factory: Factory, name: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return factory.create(name) as T
                }
            }
        }
    }
}