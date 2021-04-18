package com.vpopov.jpapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vpopov.jpapp.extension.addTo
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val cities: Observable<List<City>> = mainRepository.fetchCities().toObservable()
    val foods: Observable<List<Food>> = mainRepository.fetchFoods().toObservable()

    val cit: MutableLiveData<List<City>> = MutableLiveData()

    fun start() {
        mainRepository.fetchCities().subscribe { cities, t2 ->
            cit.value = cities
        }.addTo(compositeDisposable)
    }

    fun stop() {
        compositeDisposable.clear()
    }

}