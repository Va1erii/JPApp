package com.vpopov.jpapp.repository

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.persistence.CityDao
import com.vpopov.jpapp.persistence.FoodDao
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val cityDao: CityDao
) {
    fun getFood(name: String): Single<Food> {
        return foodDao.getFood(name)
    }

    fun getCity(name: String): Single<City> {
        return cityDao.getCity(name)
    }
}