package com.vpopov.jpapp.network

import com.vpopov.jpapp.network.response.NPointResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface NPointService {
    @GET("a2b63ef226c08553b2f9")
    fun fetchData(): Single<NPointResponse>
}