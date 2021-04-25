package com.vpopov.jpapp.ui.details.food

import androidx.lifecycle.*
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.DetailsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.CompositeDisposable

class FoodViewModel @AssistedInject constructor(
    private val detailsRepository: DetailsRepository,
    @Assisted private val name: String
) : ViewModel(), LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()
    val food: MutableLiveData<Food> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (food.value == null) {
            detailsRepository.getFood(name).subscribe { item ->
                food.value = item
            }.addTo(compositeDisposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        compositeDisposable.clear()
    }

    @AssistedFactory
    interface Factory {
        fun create(name: String): FoodViewModel
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