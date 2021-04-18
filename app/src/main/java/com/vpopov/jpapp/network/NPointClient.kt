package com.vpopov.jpapp.network

import androidx.annotation.WorkerThread
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NPointClient @Inject constructor(
    private val nPointService: NPointService
) {
    // RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
    @WorkerThread
    fun fetchData(): Single<Response> {
        return nPointService.fetchData()
            .map<Response> { Response.Success(it.foods, it.cities) }
            .onErrorReturn { Response.Failure(it?.message ?: "Unknown error") }
    }

    sealed class Response {
        data class Success(
            val foods: List<Food>,
            val cities: List<City>
        ) : Response()

        data class Failure(val error: String) : Response()
    }
}