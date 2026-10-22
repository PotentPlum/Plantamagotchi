package com.plantamagotchi.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.plantamagotchi.MainActivity
import com.plantamagotchi.R
import com.plantamagotchi.data.ActivePlant
import com.plantamagotchi.data.BackgroundStyle
import com.plantamagotchi.data.PlantRepository
import com.plantamagotchi.data.PlantStage
import com.plantamagotchi.data.PotStyle
import com.plantamagotchi.data.background
import com.plantamagotchi.data.pot
import com.plantamagotchi.data.stage

private val NightSky = Color(0xFF1A1F3A)
private val NightText = Color(0xFFE8EAF6)
private val PlantShade = Color(0x8C0D1030)
private val ButtonGreen = Color(0xFF2E7D32)
private val ButtonNight = Color(0xFF3949AB)

class PlantWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = PlantRepository.getInstance(context)
        repository.ensureActivePlant()

        provideContent {
            // Observing the Room Flow keeps the widget in sync with every DB write,
            // including logs made from the widget's own "Log Win" button.
            val plant by repository.activePlant.collectAsState(initial = null)
            PlantWidgetContent(plant = plant, isNight = isNightTime())
        }
    }
}

@Composable
private fun PlantWidgetContent(plant: ActivePlant?, isNight: Boolean) {
    val backgroundStyle = plant?.background ?: BackgroundStyle.MEADOW
    val potStyle = plant?.pot ?: PotStyle.TERRACOTTA
    val stage = plant?.stage ?: PlantStage.SEED

    val dayBackground = Color(backgroundStyle.color)
    val backgroundColor = if (isNight) lerp(dayBackground, NightSky, 0.75f) else dayBackground
    val textColor = if (isNight) NightText else Color(backgroundStyle.textColor)
    val textProvider = ColorProvider(textColor)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(backgroundColor)
            .cornerRadius(20.dp)
            .padding(12.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isNight) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    provider = ImageProvider(R.drawable.ic_moon),
                    contentDescription = null,
                    modifier = GlanceModifier.size(14.dp),
                )
                Spacer(GlanceModifier.width(4.dp))
                Text(
                    text = "Sleeping… zzz",
                    style = TextStyle(color = textProvider, fontSize = 12.sp),
                )
            }
            Spacer(GlanceModifier.height(4.dp))
        }

        Text(
            text = plant?.habitName ?: "Planting…",
            style = TextStyle(color = textProvider, fontSize = 16.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
        )

        Spacer(GlanceModifier.height(6.dp))

        Box(contentAlignment = Alignment.Center) {
            Image(
                provider = ImageProvider(stage.drawableRes),
                contentDescription = stage.label,
                modifier = GlanceModifier.size(72.dp),
            )
            if (isNight) {
                // Dark tint over the plant while it sleeps.
                Box(
                    modifier = GlanceModifier
                        .size(72.dp)
                        .background(PlantShade)
                        .cornerRadius(12.dp),
                ) {}
            }
        }

        // The pot the plant sits in.
        Box(
            modifier = GlanceModifier
                .width(56.dp)
                .height(14.dp)
                .background(Color(potStyle.color))
                .cornerRadius(4.dp),
        ) {}

        Spacer(GlanceModifier.height(6.dp))

        Text(
            text = "${plant?.logCount ?: 0} wins · ${stage.label}",
            style = TextStyle(color = textProvider, fontSize = 12.sp),
        )

        Spacer(GlanceModifier.height(8.dp))

        Button(
            text = "Log Win",
            onClick = actionRunCallback<LogWinAction>(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorProvider(if (isNight) ButtonNight else ButtonGreen),
                contentColor = ColorProvider(Color.White),
            ),
        )
    }
}
