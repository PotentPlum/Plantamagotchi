package com.plantamagotchi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.plantamagotchi.data.BackgroundStyle
import com.plantamagotchi.data.PlantStage
import com.plantamagotchi.data.PotStyle

/** Renders a plant in its pot on top of its chosen background. */
@Composable
fun PlantDisplay(
    stage: PlantStage,
    potStyle: PotStyle,
    backgroundStyle: BackgroundStyle,
    modifier: Modifier = Modifier,
    plantSize: Dp = 160.dp,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(backgroundStyle.color)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(stage.drawableRes),
                contentDescription = stage.label,
                modifier = Modifier.size(plantSize),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(plantSize / 6)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp, topStart = 4.dp, topEnd = 4.dp))
                    .background(Color(potStyle.color)),
            )
        }
    }
}

/** A small round color swatch, used to preview pot and background styles. */
@Composable
fun ColorSwatch(color: Long, modifier: Modifier = Modifier, size: Dp = 18.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(50))
            .background(Color(color)),
    )
}
