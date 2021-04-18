package com.vpopov.jpapp.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food

@JsonClass(generateAdapter = true)
data class NPointResponse(
    @field:Json(name = "foods") val foods: List<Food>,
    @field:Json(name = "cities") val cities: List<City>
)