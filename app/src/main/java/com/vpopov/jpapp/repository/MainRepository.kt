package com.vpopov.jpapp.repository

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.network.NPointClient
import com.vpopov.jpapp.persistence.CityDao
import com.vpopov.jpapp.persistence.FoodDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainRepository @Inject constructor(
    nPointClient: NPointClient,
    private val cityDao: CityDao,
    private val foodDao: FoodDao
) {
    // Fetch and cache data when a view model subscribes
    private var data: Single<Pair<List<Food>, List<City>>> = nPointClient
        .fetchData()
        .cache()

    fun fetchFoods(): Single<List<Food>> {
        // Check if foods are available in the database
        return foodDao.getFoods()
            .flatMap {
                // If no data in the database. Fetch or get cached response and save
                if (it.isEmpty()) persistFoods()
                else Single.just(it)

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchCities(): Single<List<City>> {
        // Check if cities are available in the database
        return cityDao.getCities()
            .flatMap {
                // If no data in the database. Fetch or get cached response and save
                if (it.isEmpty()) persistCities()
                else Single.just(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun persistFoods(): Single<List<Food>> {
        val foods = ArrayList<Food>()
        return data.map { it.first }
            .flatMapCompletable {
                foodDao.insertFoods(it)
                    .andThen(Completable.fromRunnable { foods.addAll(it) })
            }
            .andThen(Single.just(foods))
    }

    private fun persistCities(): Single<List<City>> {
        val cities = ArrayList<City>()
        return data.map { it.second }
            .flatMapCompletable {
                cityDao.insertCities(it)
                    .andThen(Completable.fromRunnable { cities.addAll(it) })
            }
            .andThen(Single.just(cities))
    }
}