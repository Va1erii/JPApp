package com.vpopov.jpapp.ui.details.food

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.DetailsRepository
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class FoodViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testCityViewModel() {
        val detailsRepository = Mockito.mock(DetailsRepository::class.java)
        val foodName = "Sushi"

        Mockito.`when`(detailsRepository.getFood(foodName)).then {
            Single.just(Food(foodName, ""))
        }
        val viewModel = FoodViewModel(detailsRepository, foodName)
        Assert.assertNull(viewModel.food.value)
        viewModel.onStart()
        Assert.assertEquals(Food("Sushi", ""), viewModel.food.value)
    }
}