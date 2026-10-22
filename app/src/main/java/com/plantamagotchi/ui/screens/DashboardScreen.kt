package com.plantamagotchi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plantamagotchi.data.ActivePlant
import com.plantamagotchi.data.PlantStage
import com.plantamagotchi.data.background
import com.plantamagotchi.data.canHarvest
import com.plantamagotchi.data.pot
import com.plantamagotchi.data.stage
import com.plantamagotchi.data.type
import com.plantamagotchi.ui.components.PlantDisplay

@Composable
fun DashboardScreen(
    plant: ActivePlant?,
    onLogWin: () -> Unit,
    onHarvest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (plant == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val threshold = PlantStage.HARVEST_THRESHOLD
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = plant.habitName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "${plant.type.label} · ${plant.stage.label}",
            style = MaterialTheme.typography.titleMedium,
            color = Color(plant.type.accentColor),
        )

        PlantDisplay(
            stage = plant.stage,
            potStyle = plant.pot,
            backgroundStyle = plant.background,
            modifier = Modifier.fillMaxWidth(),
        )

        LinearProgressIndicator(
            progress = { (plant.logCount.toFloat() / threshold).coerceAtMost(1f) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = if (plant.canHarvest) {
                "${plant.logCount} wins — ready to harvest!"
            } else {
                "${plant.logCount} / $threshold wins until harvest"
            },
            style = MaterialTheme.typography.bodyLarge,
        )

        Button(onClick = onLogWin, modifier = Modifier.fillMaxWidth()) {
            Text("Log Win")
        }
        FilledTonalButton(
            onClick = onHarvest,
            enabled = plant.canHarvest,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Harvest")
        }
    }
}
