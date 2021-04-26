package com.vpopov.jpapp.ui.main

import androidx.lifecycle.*
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.MainRepository
import com.vpopov.jpapp.repository.MainRepository.Response
import com.vpopov.jpapp.util.CharSequenceContainer
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
    val error: MutableLiveData<CharSequenceContainer> = MutableLiveData(CharSequenceContainer(""))

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (foods.value == null) {
            fetchFoods()
        }
        if (cities.value == null) {
            fetchCities()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        compositeDisposable.clear()
    }

    fun retry() {
        error.value = CharSequenceContainer("")
        mainRepository.reset()
        fetchFoods()
        fetchCities()
    }

    private fun fetchFoods() {
        mainRepository.fetchFoods().subscribe { response ->
            when (response) {
                is Response.Success -> foods.value = response.items
                is Response.Error -> error.value = response.message
            }
        }.addTo(compositeDisposable)
    }

    private fun fetchCities() {
        mainRepository.fetchCities().subscribe { response ->
            when (response) {
                is Response.Success -> cities.value = response.items
                is Response.Error -> error.value = response.message
            }
        }.addTo(compositeDisposable)
    }
}