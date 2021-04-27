package com.vpopov.jpapp.ui.details.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.repository.DetailsRepository
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins.setInitMainThreadSchedulerHandler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins.setIoSchedulerHandler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


class CityViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        setIoSchedulerHandler { Schedulers.trampoline() }
        setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testCityViewModel() {
        val detailsRepository = Mockito.mock(DetailsRepository::class.java)
        val cityName = "Tokio"

        Mockito.`when`(detailsRepository.getCity(cityName)).then {
            Single.just(City(cityName, "", ""))
        }
        val viewModel = CityViewModel(detailsRepository, cityName)
        Assert.assertNull(viewModel.city.value)
        viewModel.onStart()
        Assert.assertEquals(City("Tokio", "", ""), viewModel.city.value)
    }
}