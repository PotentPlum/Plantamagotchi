package com.plantamagotchi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("SELECT * FROM active_plant WHERE id = :id LIMIT 1")
    fun observeActivePlant(id: Int): Flow<ActivePlant?>

    @Query("SELECT * FROM active_plant WHERE id = :id LIMIT 1")
    suspend fun getActivePlant(id: Int): ActivePlant?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivePlantIfMissing(plant: ActivePlant)

    @Query("UPDATE active_plant SET logCount = logCount + 1 WHERE id = :id")
    suspend fun incrementLogCount(id: Int)

    @Query("UPDATE active_plant SET logCount = 0, plantType = :plantType WHERE id = :id")
    suspend fun resetActivePlant(id: Int, plantType: String)

    @Query("UPDATE active_plant SET habitName = :habitName WHERE id = :id")
    suspend fun updateHabitName(id: Int, habitName: String)

    @Query("UPDATE active_plant SET potStyle = :potStyle WHERE id = :id")
    suspend fun updatePotStyle(id: Int, potStyle: String)

    @Query("UPDATE active_plant SET backgroundStyle = :backgroundStyle WHERE id = :id")
    suspend fun updateBackgroundStyle(id: Int, backgroundStyle: String)

    @Query("SELECT * FROM terrarium_plant ORDER BY dateHarvested DESC")
    fun observeTerrarium(): Flow<List<TerrariumPlant>>

    @Insert
    suspend fun insertTerrariumPlant(plant: TerrariumPlant): Long
}
