package com.vpopov.jpapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class City(
    @field:Json(name = "name") @PrimaryKey val name: String,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "description") val description: String
)