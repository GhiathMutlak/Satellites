package com.satellites.app.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SatelliteDetails(
    @Embedded val satellite:Satellite,
    @Relation(parentColumn = "id", entityColumn = "id")
    val details: Details
)