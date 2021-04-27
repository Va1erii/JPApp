package com.vpopov.jpapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.MainRepository
import com.vpopov.jpapp.repository.MainRepository.Response
import com.vpopov.jpapp.util.CharSequenceContainer
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testMainViewModelSuccess() {
        val repository = Mockito.mock(MainRepository::class.java)
        val foods = listOf(Food("Sushi", ""))
        val cities = listOf(City("Osaka", "", ""))

        Mockito.`when`(repository.fetchFoods()).then { Single.just(Response.Success(foods)) }
        Mockito.`when`(repository.fetchCities()).then { Single.just(Response.Success(cities)) }

        val viewModel = MainViewModel(repository)
        Assert.assertNull(viewModel.foods.value)
        Assert.assertNull(viewModel.cities.value)

        viewModel.onStart()
        Assert.assertEquals(foods, viewModel.foods.value)
        Assert.assertEquals(cities, viewModel.cities.value)
    }

    @Test
    fun testMainViewModelError() {
        val repository = Mockito.mock(MainRepository::class.java)
        val errorFoods = Response.Error<List<Food>>(CharSequenceContainer("Error"))
        val errorCities = Response.Error<List<City>>(CharSequenceContainer("Error"))
        Mockito.`when`(repository.fetchFoods()).then { Single.just(errorFoods) }
        Mockito.`when`(repository.fetchCities()).then { Single.just(errorCities) }

        val viewModel = MainViewModel(repository)
        Assert.assertNull(viewModel.foods.value)
        Assert.assertNull(viewModel.cities.value)

        viewModel.onStart()
        Assert.assertNull(viewModel.foods.value)
        Assert.assertNull(viewModel.cities.value)
        Assert.assertEquals(CharSequenceContainer("Error"), viewModel.error.value)
    }

    @Test
    fun testMainViewModelRetry() {
        val repository = Mockito.mock(MainRepository::class.java)
        val errorFoods = Response.Error<List<Food>>(CharSequenceContainer("Error"))
        val errorCities = Response.Error<List<City>>(CharSequenceContainer("Error"))
        Mockito.`when`(repository.fetchFoods()).then { Single.just(errorFoods) }
        Mockito.`when`(repository.fetchCities()).then { Single.just(errorCities) }
        Mockito.`when`(repository.reset()).then { }

        val viewModel = MainViewModel(repository)
        Assert.assertNull(viewModel.foods.value)
        Assert.assertNull(viewModel.cities.value)

        viewModel.onStart()
        Assert.assertNull(viewModel.foods.value)
        Assert.assertNull(viewModel.cities.value)
        Assert.assertEquals(CharSequenceContainer("Error"), viewModel.error.value)

        viewModel.retry()
        val inOrder = Mockito.inOrder(repository)
        inOrder.verify(repository).fetchFoods()
        inOrder.verify(repository).fetchCities()
        inOrder.verify(repository).reset()
    }
}