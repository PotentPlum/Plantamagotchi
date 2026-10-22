package com.plantamagotchi.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.plantamagotchi.data.ActivePlant
import com.plantamagotchi.data.BackgroundStyle
import com.plantamagotchi.data.PotStyle
import com.plantamagotchi.data.background
import com.plantamagotchi.data.pot
import com.plantamagotchi.data.stage
import com.plantamagotchi.ui.components.ColorSwatch
import com.plantamagotchi.ui.components.PlantDisplay

@Composable
fun CustomizationScreen(
    plant: ActivePlant?,
    onHabitNameChange: (String) -> Unit,
    onPotStyleChange: (PotStyle) -> Unit,
    onBackgroundStyleChange: (BackgroundStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (plant == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val focusManager = LocalFocusManager.current
    var habitName by rememberSaveable(plant.habitName) { mutableStateOf(plant.habitName) }
    val saveHabitName = {
        onHabitNameChange(habitName)
        focusManager.clearFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PlantDisplay(
            stage = plant.stage,
            potStyle = plant.pot,
            backgroundStyle = plant.background,
            modifier = Modifier.fillMaxWidth(),
            plantSize = 120.dp,
        )

        SectionTitle("Habit")
        OutlinedTextField(
            value = habitName,
            onValueChange = { habitName = it },
            label = { Text("Habit name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { saveHabitName() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = saveHabitName,
            enabled = habitName.isNotBlank() && habitName.trim() != plant.habitName,
        ) {
            Text("Save habit name")
        }

        SectionTitle("Pot style")
        StyleChips(
            options = PotStyle.entries,
            selected = plant.pot,
            label = { it.label },
            color = { it.color },
            onSelect = onPotStyleChange,
        )

        SectionTitle("Background style")
        StyleChips(
            options = BackgroundStyle.entries,
            selected = plant.background,
            label = { it.label },
            color = { it.color },
            onSelect = onBackgroundStyleChange,
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> StyleChips(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    color: (T) -> Long,
    onSelect: (T) -> Unit,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelect(option) },
                label = { Text(label(option)) },
                leadingIcon = { ColorSwatch(color = color(option)) },
            )
        }
    }
}
