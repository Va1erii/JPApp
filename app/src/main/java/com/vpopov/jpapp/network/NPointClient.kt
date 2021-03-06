package com.vpopov.jpapp.network

import androidx.annotation.WorkerThread
import com.vpopov.jpapp.R
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.util.CharSequenceContainer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NPointClient @Inject constructor(
    private val nPointService: NPointService,
    private val timeout: Long = 8000
) {

    @WorkerThread
    fun fetchData(): Single<Response> {
        return nPointService.fetchData()
            .subscribeOn(Schedulers.io())
            .map<Response> { Response.Success(it.foods, it.cities) }
            .timeout(timeout, TimeUnit.MILLISECONDS)
            .onErrorReturn { throwable ->
                // Here we could provide API error code if it supported
                Response.Failure(CharSequenceContainer(R.string.api_response_error))
            }
    }

    sealed class Response {
        data class Success(
            val foods: List<Food>,
            val cities: List<City>
        ) : Response()

        data class Failure(val error: CharSequenceContainer) : Response()
    }
}