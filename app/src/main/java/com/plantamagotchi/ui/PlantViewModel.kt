package com.plantamagotchi.ui

import android.app.Application
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.plantamagotchi.data.ActivePlant
import com.plantamagotchi.data.BackgroundStyle
import com.plantamagotchi.data.PlantRepository
import com.plantamagotchi.data.PotStyle
import com.plantamagotchi.data.TerrariumPlant
import com.plantamagotchi.widget.PlantWidget
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlantRepository.getInstance(application)

    val activePlant: StateFlow<ActivePlant?> = repository.activePlant
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val terrarium: StateFlow<List<TerrariumPlant>> = repository.terrarium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    fun logWin() = updateAndRefreshWidget { repository.logWin() }

    fun harvest() = updateAndRefreshWidget {
        if (repository.harvest()) {
            _messages.emit("Harvested! Your plant moved to the terrarium and a mystery seed was planted.")
        }
    }

    fun updateHabitName(name: String) = updateAndRefreshWidget { repository.updateHabitName(name) }

    fun updatePotStyle(style: PotStyle) = updateAndRefreshWidget { repository.updatePotStyle(style) }

    fun updateBackgroundStyle(style: BackgroundStyle) =
        updateAndRefreshWidget { repository.updateBackgroundStyle(style) }

    /** Runs a DB write and then re-renders any home screen widgets. */
    private fun updateAndRefreshWidget(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
            PlantWidget().updateAll(getApplication<Application>())
        }
    }
}
