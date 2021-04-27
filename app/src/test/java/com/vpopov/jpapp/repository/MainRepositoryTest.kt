package com.vpopov.jpapp.repository

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.network.NPointClient
import com.vpopov.jpapp.network.NPointClient.Response.Failure
import com.vpopov.jpapp.network.NPointClient.Response.Success
import com.vpopov.jpapp.persistence.CityDao
import com.vpopov.jpapp.persistence.FoodDao
import com.vpopov.jpapp.repository.MainRepository.Response
import com.vpopov.jpapp.util.CharSequenceContainer
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins.setInitMainThreadSchedulerHandler
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins.setIoSchedulerHandler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainRepositoryTest {
    private lateinit var foodDao: FoodDao
    private lateinit var cityDao: CityDao
    private lateinit var nPointClient: NPointClient

    @Before
    fun setup() {
        setIoSchedulerHandler { Schedulers.trampoline() }
        setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        foodDao = Mockito.mock(FoodDao::class.java)
        Mockito.`when`(foodDao.insertFoods(Mockito.anyList())).then { Completable.complete() }

        cityDao = Mockito.mock(CityDao::class.java)
        Mockito.`when`(cityDao.insertCities(Mockito.anyList())).then { Completable.complete() }

        nPointClient = Mockito.mock(NPointClient::class.java)
    }

    @Test
    fun `testFetchFoods - No local data, success API response`() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(foodDao.getFoods()).then { Single.just<List<Food>>(emptyList()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))

        // Check order. Repository should check the local storage before API invocation
        // Also, check that it saves the response to the local storage
        val inOrder = Mockito.inOrder(foodDao, nPointClient)
        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(foodDao, Mockito.calls(1)).insertFoods(Mockito.anyList())
    }

    @Test
    fun `testFetchFoods - No local data, error API response`() {
        val apiResponse = Failure(CharSequenceContainer("ERROR"))
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(foodDao.getFoods()).then { Single.just<List<Food>>(emptyList()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Error(apiResponse.error))

        // Check order. Repository should check the local storage before API invocation
        // It should not save a error data to the local storage
        val inOrder = Mockito.inOrder(foodDao, nPointClient)
        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(foodDao, Mockito.never()).insertFoods(Mockito.anyList())
    }

    @Test
    fun `testFetchFoods - Has local data`() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(foodDao.getFoods()).then { Single.just(foods) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))

        // Check order. Repository should check the local storage before API invocation
        // It should not call API
        val inOrder = Mockito.inOrder(foodDao, nPointClient)
        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.never()).fetchData()
        inOrder.verify(foodDao, Mockito.never()).insertFoods(Mockito.anyList())
    }

    @Test
    fun `testFetchCities - No local data, success API response`() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(cityDao.getCities()).then { Single.just<List<City>>(emptyList()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchCities().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(cities))

        // Check order. Repository should check the local storage before API invocation
        // Also, check that it saves the response to the local storage
        val inOrder = Mockito.inOrder(cityDao, nPointClient)
        inOrder.verify(cityDao, Mockito.calls(1)).getCities()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(cityDao, Mockito.calls(1)).insertCities(Mockito.anyList())
    }

    @Test
    fun `testFetchCities - No local data, error API response`() {
        val apiResponse = Failure(CharSequenceContainer("ERROR"))
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(cityDao.getCities()).then { Single.just<List<City>>(emptyList()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchCities().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Error(apiResponse.error))

        // Check order. Repository should check the local storage before API invocation
        // It should not save a error data to the local storage
        val inOrder = Mockito.inOrder(cityDao, nPointClient)
        inOrder.verify(cityDao, Mockito.calls(1)).getCities()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(cityDao, Mockito.never()).insertCities(Mockito.anyList())
    }

    @Test
    fun `testFetchCities - Has local data`() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(cityDao.getCities()).then { Single.just(cities) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        val testObserver = repository.fetchCities().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(cities))

        // Check order. Repository should check the local storage before API invocation
        // It should not call API
        val inOrder = Mockito.inOrder(cityDao, nPointClient)
        inOrder.verify(cityDao, Mockito.calls(1)).getCities()
        inOrder.verify(nPointClient, Mockito.never()).fetchData()
        inOrder.verify(cityDao, Mockito.never()).insertCities(Mockito.anyList())
    }

    @Test
    fun testCache() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(foodDao.getFoods()).then { Single.just(emptyList<Food>()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        var testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))
        testObserver.assertComplete()

        testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))
        testObserver.assertComplete()

        // Invocation order
        val inOrder = Mockito.inOrder(foodDao, nPointClient)
        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(foodDao, Mockito.calls(1)).insertFoods(Mockito.anyList())

        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.never()).fetchData()
    }

    @Test
    fun testRetry() {
        val foods = listOf(Food("food", ""))
        val cities = listOf(City("city", "", "description"))
        val apiResponse = Success(foods, cities)
        Mockito.`when`(nPointClient.fetchData()).then { Single.just(apiResponse) }
        Mockito.`when`(foodDao.getFoods()).then { Single.just(emptyList<Food>()) }

        val repository = MainRepository(nPointClient, cityDao, foodDao)
        var testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))
        testObserver.assertComplete()

        // Invocation order
        val inOrder = Mockito.inOrder(foodDao, nPointClient)
        inOrder.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder.verify(nPointClient, Mockito.calls(1)).fetchData()
        inOrder.verify(foodDao, Mockito.calls(1)).insertFoods(Mockito.anyList())

        // Reset prepares RxJava chain and it will not call NPointClient.fetchData()
        // BUT Mockito.inOrder thinks that it was invoked. So here we separate InOrders
        repository.reset()

        testObserver = repository.fetchFoods().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(Response.Success(foods))
        testObserver.assertComplete()

        val inOrder2 = Mockito.inOrder(foodDao, nPointClient)
        inOrder2.verify(foodDao, Mockito.calls(1)).getFoods()
        inOrder2.verify(nPointClient, Mockito.calls(1)).fetchData()
    }
}