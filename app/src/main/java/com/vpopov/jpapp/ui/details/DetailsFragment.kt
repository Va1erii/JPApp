package com.vpopov.jpapp.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpopov.jpapp.repository.DetailsRepository
import com.vpopov.jpapp.ui.details.city.CityDetailsViewModel
import com.vpopov.jpapp.ui.details.food.FoodDetailsViewModel
import javax.inject.Inject

abstract class DetailsFragment<T> : Fragment() {
    @Inject
    lateinit var detailsRepository: DetailsRepository

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified VM : DetailsViewModel<T>> provideViewModel(name: String) =
        viewModels<VM> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return when (modelClass.name) {
                        CityDetailsViewModel::class.java.name -> CityDetailsViewModel(
                            detailsRepository,
                            name
                        )
                        FoodDetailsViewModel::class.java.name -> FoodDetailsViewModel(
                            detailsRepository,
                            name
                        )
                        else -> throw IllegalArgumentException("Unknown view model")
                    } as T
                }
            }
        }
}