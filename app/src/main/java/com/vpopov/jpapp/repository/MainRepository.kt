package com.vpopov.jpapp.repository

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.network.NPointClient
import com.vpopov.jpapp.persistence.CityDao
import com.vpopov.jpapp.persistence.FoodDao
import com.vpopov.jpapp.util.CharSequenceContainer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val nPointClient: NPointClient,
    private val cityDao: CityDao,
    private val foodDao: FoodDao
) {
    // Fetch and cache data when a subscription occurs
    private var cashe: Single<NPointClient.Response>? = null

    fun fetchFoods(): Single<Response<List<Food>>> {
        // Check if foods are available in the database
        return foodDao.getFoods()
            .flatMap {
                if (it.isEmpty()) getFoodsFromAPI() // Fetch from the API
                else Single.just(Response.Success(it))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchCities(): Single<Response<List<City>>> {
        // Check if cities are available in the database
        return cityDao.getCities()
            .flatMap {
                if (it.isEmpty()) getCitiesFromAPI() // Fetch from the API
                else Single.just(Response.Success(it))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // Invalidate cache
    fun reset() {
        cashe = nPointClient
            .fetchData()
            .cache()
    }

    private fun getFoodsFromAPI(): Single<Response<List<Food>>> {
        val foods: ArrayList<Food> = ArrayList()
        return getCache().flatMap {
            when (it) {
                // If success, save data to the database
                is NPointClient.Response.Success -> Completable
                    .fromRunnable { foods.addAll(it.foods) }
                    .andThen(foodDao.insertFoods(foods))
                    .andThen(Single.just(Response.Success(foods)))

                // Return API error
                is NPointClient.Response.Failure -> Single.just(Response.Error(it.error))
            }
        }
    }

    private fun getCitiesFromAPI(): Single<Response<List<City>>> {
        val cities: ArrayList<City> = ArrayList()
        return getCache().flatMap {
            when (it) {
                // If success, save data to the database
                is NPointClient.Response.Success -> Completable
                    .fromRunnable { cities.addAll(it.cities) }
                    .andThen(cityDao.insertCities(cities))
                    .andThen(Single.just(Response.Success(cities)))

                // Return API error
                is NPointClient.Response.Failure -> Single.just(Response.Error(it.error))
            }
        }
    }

    private fun getCache(): Single<NPointClient.Response> {
        return cashe ?: nPointClient
            .fetchData()
            .cache()
            .apply { cashe = this }
    }

    sealed class Response<T> {
        data class Success<T>(
            val items: T
        ) : Response<T>()

        data class Error<T>(
            val message: CharSequenceContainer
        ) : Response<T>()
    }
}