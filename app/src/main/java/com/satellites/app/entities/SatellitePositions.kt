package com.satellites.app.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SatellitePositions(
    @Embedded val satellite:Satellite,
    @Relation(parentColumn = "id", entityColumn = "id")
    val position:List<Position>
)