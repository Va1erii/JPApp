package com.vpopov.jpapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.model.Food

@Database(entities = [City::class, Food::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun foodDao(): FoodDao
}