package com.vpopov.jpapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vpopov.jpapp.model.Food
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFoods(currencyList: List<Food>): Completable

    @Query("SELECT * FROM Food")
    fun getFoods(): Single<List<Food>>

    @Query("SELECT * FROM Food WHERE name = :name_")
    fun getFood(name_: String): Single<Food>
}