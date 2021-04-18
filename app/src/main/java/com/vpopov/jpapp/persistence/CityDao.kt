package com.vpopov.jpapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vpopov.jpapp.model.City
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCities(currencyList: List<City>): Completable

    @Query("SELECT * FROM City")
    fun getCities(): Single<List<City>>

    @Query("SELECT * FROM City WHERE name = :name_")
    fun getCity(name_: String): Single<City>
}