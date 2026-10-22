package com.plantamagotchi.data

import androidx.annotation.DrawableRes
import com.plantamagotchi.R
import kotlin.random.Random

/** Growth stage derived from the number of logged wins. */
enum class PlantStage(val label: String, @DrawableRes val drawableRes: Int) {
    SEED("Seed", R.drawable.ic_plant_seed),
    SPROUT("Sprout", R.drawable.ic_plant_sprout),
    VEGETATIVE("Vegetative", R.drawable.ic_plant_vegetative),
    MATURE("Mature", R.drawable.ic_plant_mature);

    companion object {
        const val HARVEST_THRESHOLD = 21

        fun fromLogCount(logCount: Int): PlantStage = when {
            logCount <= 5 -> SEED
            logCount <= 12 -> SPROUT
            logCount <= 20 -> VEGETATIVE
            else -> MATURE
        }
    }
}

/** The species of the plant, re-rolled each time a plant is harvested. */
enum class PlantType(val label: String, val accentColor: Long) {
    FERN("Fern", 0xFF2E7D32),
    CACTUS("Cactus", 0xFF558B2F),
    SUNFLOWER("Sunflower", 0xFFF9A825),
    TULIP("Tulip", 0xFFD81B60),
    BONSAI("Bonsai", 0xFF6D4C41),
    LAVENDER("Lavender", 0xFF7E57C2);

    companion object {
        fun fromKey(key: String): PlantType = entries.firstOrNull { it.name == key } ?: FERN

        /** Picks a random plant type, avoiding [exclude] when possible to make the RNG feel fresh. */
        fun random(random: Random = Random.Default, exclude: PlantType? = null): PlantType {
            val candidates = entries.filter { it != exclude }.ifEmpty { entries }
            return candidates[random.nextInt(candidates.size)]
        }
    }
}

enum class PotStyle(val label: String, val color: Long) {
    TERRACOTTA("Terracotta", 0xFFC0603A),
    CERAMIC("Ceramic", 0xFFECEFF1),
    MIDNIGHT("Midnight", 0xFF283593),
    MOSS("Moss", 0xFF689F38);

    companion object {
        fun fromKey(key: String): PotStyle = entries.firstOrNull { it.name == key } ?: TERRACOTTA
    }
}

enum class BackgroundStyle(val label: String, val color: Long, val textColor: Long) {
    MEADOW("Meadow", 0xFFDCEDC8, 0xFF1B5E20),
    SUNSET("Sunset", 0xFFFFCCBC, 0xFF4E342E),
    OCEAN("Ocean", 0xFFB3E5FC, 0xFF01579B),
    DESERT("Desert", 0xFFFFF3C4, 0xFF5D4037),
    FOREST("Forest", 0xFF2E5E3A, 0xFFF1F8E9);

    companion object {
        fun fromKey(key: String): BackgroundStyle = entries.firstOrNull { it.name == key } ?: MEADOW
    }
}
