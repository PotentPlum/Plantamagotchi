package com.plantamagotchi.data

import android.content.Context
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlin.random.Random

/**
 * Single source of truth for plant data, shared by the Compose app and the Glance widget.
 */
class PlantRepository(
    private val database: AppDatabase,
    private val random: Random = Random.Default,
    private val clock: () -> Long = System::currentTimeMillis,
) {
    private val dao = database.plantDao()
    private val plantId = ActivePlant.DEFAULT_ID

    /** Emits the active plant, creating the initial seed on first use. */
    val activePlant: Flow<ActivePlant?> =
        dao.observeActivePlant(plantId).onStart { ensureActivePlant() }

    val terrarium: Flow<List<TerrariumPlant>> = dao.observeTerrarium()

    suspend fun ensureActivePlant() {
        dao.insertActivePlantIfMissing(
            ActivePlant(
                id = plantId,
                habitName = ActivePlant.DEFAULT_HABIT_NAME,
                plantType = PlantType.random(random).name,
            )
        )
    }

    /** Records one completed habit by incrementing the active plant's logCount. */
    suspend fun logWin() {
        ensureActivePlant()
        dao.incrementLogCount(plantId)
    }

    /**
     * Moves a mature plant into the terrarium, resets the active plant to 0 logs and
     * rolls a new random plant type (simulating a fresh seed). Returns false if the
     * plant isn't ready to harvest yet.
     */
    suspend fun harvest(): Boolean = database.withTransaction {
        val plant = dao.getActivePlant(plantId)
        if (plant == null || !plant.canHarvest) return@withTransaction false
        dao.insertTerrariumPlant(
            TerrariumPlant(
                habitName = plant.habitName,
                plantType = plant.plantType,
                dateHarvested = clock(),
            )
        )
        val newType = PlantType.random(random, exclude = plant.type)
        dao.resetActivePlant(plantId, newType.name)
        true
    }

    suspend fun updateHabitName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) dao.updateHabitName(plantId, trimmed)
    }

    suspend fun updatePotStyle(style: PotStyle) = dao.updatePotStyle(plantId, style.name)

    suspend fun updateBackgroundStyle(style: BackgroundStyle) =
        dao.updateBackgroundStyle(plantId, style.name)

    companion object {
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(context: Context): PlantRepository =
            instance ?: synchronized(this) {
                instance ?: PlantRepository(AppDatabase.getInstance(context))
                    .also { instance = it }
            }
    }
}
