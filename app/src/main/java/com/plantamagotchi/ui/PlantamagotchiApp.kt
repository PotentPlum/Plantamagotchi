package com.plantamagotchi.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plantamagotchi.ui.screens.CustomizationScreen
import com.plantamagotchi.ui.screens.DashboardScreen
import com.plantamagotchi.ui.screens.TerrariumScreen

private enum class Destination(val title: String, val icon: ImageVector) {
    DASHBOARD("Garden", Icons.Filled.Home),
    TERRARIUM("Terrarium", Icons.Filled.Star),
    CUSTOMIZE("Customize", Icons.Filled.Edit),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantamagotchiApp(viewModel: PlantViewModel = viewModel()) {
    val plant by viewModel.activePlant.collectAsStateWithLifecycle()
    val terrarium by viewModel.terrarium.collectAsStateWithLifecycle()
    var destination by rememberSaveable { mutableStateOf(Destination.DASHBOARD) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(destination.title) }) },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == destination,
                        onClick = { destination = item },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (destination) {
            Destination.DASHBOARD -> DashboardScreen(
                plant = plant,
                onLogWin = viewModel::logWin,
                onHarvest = viewModel::harvest,
                modifier = contentModifier,
            )
            Destination.TERRARIUM -> TerrariumScreen(
                plants = terrarium,
                modifier = contentModifier,
            )
            Destination.CUSTOMIZE -> CustomizationScreen(
                plant = plant,
                onHabitNameChange = viewModel::updateHabitName,
                onPotStyleChange = viewModel::updatePotStyle,
                onBackgroundStyleChange = viewModel::updateBackgroundStyle,
                modifier = contentModifier,
            )
        }
    }
}
