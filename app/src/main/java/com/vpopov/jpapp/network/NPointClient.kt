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
    fun fetchData(): Single<Pair<List<Food>, List<City>>> {
        return nPointService.fetchData().map {
            it.foods to it.cities
        }
    }
}