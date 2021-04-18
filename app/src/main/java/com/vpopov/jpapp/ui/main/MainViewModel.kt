package com.vpopov.jpapp.ui.main

import androidx.lifecycle.*
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.MainRepository
import com.vpopov.jpapp.repository.MainRepository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(), LifecycleObserver {
    private val compositeDisposable = CompositeDisposable()

    val foods: MutableLiveData<List<Food>> = MutableLiveData()
    val cities: MutableLiveData<List<City>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (foods.value == null) {
            mainRepository.fetchFoods().subscribe { response ->
                when (response) {
                    is Response.Success -> foods.value = response.items
                    is Response.Error -> error.value = response.message
                }
                loading.value = false
            }.addTo(compositeDisposable)
        }
        if (cities.value == null) {
            mainRepository.fetchCities().subscribe { response ->
                when (response) {
                    is Response.Success -> cities.value = response.items
                    is Response.Error -> error.value = response.message
                }
                loading.value = false
            }.addTo(compositeDisposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }

}