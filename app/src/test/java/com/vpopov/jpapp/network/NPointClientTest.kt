package com.vpopov.jpapp.network

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.network.NPointClient.Response
import com.vpopov.jpapp.network.response.NPointResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.TimeUnit


class NPointClientTest {
    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testFetchDataSuccess() {
        val nPointService = Mockito.mock(NPointService::class.java)
        val response = NPointResponse(
            listOf(Food("food", "")),
            listOf(City("city", "", "description"))
        )
        Mockito
            .`when`(nPointService.fetchData())
            .then { Single.just(response) }
        val client = NPointClient(nPointService)

        val testObserver = client.fetchData().test()
        testObserver.awaitCount(1)
        testObserver.assertValues(
            Response.Success(
                listOf(Food("food", "")),
                listOf(City("city", "", "description"))
            )
        )
        testObserver.assertComplete()
    }

    @Test
    fun testFetchDataTimeOut() {
        val nPointService = Mockito.mock(NPointService::class.java)
        val response = NPointResponse(
            listOf(Food("food", "")),
            listOf(City("city", "", "description"))
        )
        Mockito
            .`when`(nPointService.fetchData())
            .then {
                Observable.timer(5, TimeUnit.SECONDS)
                    .flatMapSingle { Single.just(response) }
                    .singleOrError()
            }
        val client = NPointClient(nPointService, 3000)
        val testObserver = client.fetchData().test()
        testObserver.awaitCount(1)
        testObserver.assertValue { it is Response.Failure }
        testObserver.assertComplete()
    }

    @Test
    fun testFetchDataError() {
        val nPointService = Mockito.mock(NPointService::class.java)
        Mockito
            .`when`(nPointService.fetchData())
            .then { Single.error<Response>(IllegalStateException()) }
        val client = NPointClient(nPointService)
        val testObserver = client.fetchData().test()
        testObserver.awaitCount(1)
        testObserver.assertValue { it is Response.Failure }
        testObserver.assertComplete()
    }
}