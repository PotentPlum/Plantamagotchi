package com.plantamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.plantamagotchi.ui.PlantamagotchiApp
import com.plantamagotchi.ui.theme.PlantamagotchiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantamagotchiTheme {
                PlantamagotchiApp()
            }
        }
    }
}
