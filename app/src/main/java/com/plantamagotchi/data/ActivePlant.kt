package com.plantamagotchi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The plant currently being grown. The app tracks a single active plant whose
 * row always uses [ActivePlant.DEFAULT_ID].
 */
@Entity(tableName = "active_plant")
data class ActivePlant(
    @PrimaryKey val id: Int = DEFAULT_ID,
    val habitName: String,
    val logCount: Int = 0,
    val plantType: String = PlantType.FERN.name,
    val potStyle: String = PotStyle.TERRACOTTA.name,
    val backgroundStyle: String = BackgroundStyle.MEADOW.name,
) {
    companion object {
        const val DEFAULT_ID = 1
        const val DEFAULT_HABIT_NAME = "Drink water"
    }
}

val ActivePlant.stage: PlantStage get() = PlantStage.fromLogCount(logCount)
val ActivePlant.canHarvest: Boolean get() = logCount >= PlantStage.HARVEST_THRESHOLD
val ActivePlant.type: PlantType get() = PlantType.fromKey(plantType)
val ActivePlant.pot: PotStyle get() = PotStyle.fromKey(potStyle)
val ActivePlant.background: BackgroundStyle get() = BackgroundStyle.fromKey(backgroundStyle)
