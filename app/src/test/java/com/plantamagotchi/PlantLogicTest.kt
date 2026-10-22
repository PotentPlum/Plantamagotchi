package com.plantamagotchi

import com.plantamagotchi.data.ActivePlant
import com.plantamagotchi.data.PlantStage
import com.plantamagotchi.data.PlantType
import com.plantamagotchi.data.canHarvest
import com.plantamagotchi.widget.isNightTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalTime
import kotlin.random.Random

class PlantLogicTest {

    @Test
    fun stageThresholds() {
        assertEquals(PlantStage.SEED, PlantStage.fromLogCount(0))
        assertEquals(PlantStage.SEED, PlantStage.fromLogCount(5))
        assertEquals(PlantStage.SPROUT, PlantStage.fromLogCount(6))
        assertEquals(PlantStage.SPROUT, PlantStage.fromLogCount(12))
        assertEquals(PlantStage.VEGETATIVE, PlantStage.fromLogCount(13))
        assertEquals(PlantStage.VEGETATIVE, PlantStage.fromLogCount(20))
        assertEquals(PlantStage.MATURE, PlantStage.fromLogCount(21))
        assertEquals(PlantStage.MATURE, PlantStage.fromLogCount(100))
    }

    @Test
    fun harvestRequiresTwentyOneLogs() {
        assertFalse(ActivePlant(habitName = "Read", logCount = 20).canHarvest)
        assertTrue(ActivePlant(habitName = "Read", logCount = 21).canHarvest)
    }

    @Test
    fun nightIsBetweenEightPmAndSixAm() {
        assertFalse(isNightTime(LocalTime.of(19, 59)))
        assertTrue(isNightTime(LocalTime.of(20, 0)))
        assertTrue(isNightTime(LocalTime.of(0, 0)))
        assertTrue(isNightTime(LocalTime.of(5, 59)))
        assertFalse(isNightTime(LocalTime.of(6, 0)))
        assertFalse(isNightTime(LocalTime.of(12, 0)))
    }

    @Test
    fun randomPlantTypeAvoidsExcludedType() {
        val random = Random(42)
        repeat(100) {
            assertNotEquals(PlantType.CACTUS, PlantType.random(random, exclude = PlantType.CACTUS))
        }
    }
}
