package com.vpopov.jpapp.ui.details.city

import androidx.lifecycle.*
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.repository.DetailsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CityViewModel @AssistedInject constructor(
    private val detailsRepository: DetailsRepository,
    @Assisted private val name: String
) : ViewModel(), LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()
    val city: MutableLiveData<City> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        detailsRepository.getCity(name).subscribe { item ->
            city.value = item
        }.addTo(compositeDisposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStop() {
        compositeDisposable.clear()
    }

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