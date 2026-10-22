package com.plantamagotchi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A fully grown plant that was harvested and moved into the terrarium. */
@Entity(tableName = "terrarium_plant")
data class TerrariumPlant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitName: String,
    val plantType: String,
    /** Epoch milliseconds of the moment the plant was harvested. */
    val dateHarvested: Long,
)

val TerrariumPlant.type: PlantType get() = PlantType.fromKey(plantType)
