package com.plantamagotchi.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.plantamagotchi.data.PlantRepository

/**
 * Runs in the background when "Log Win" is tapped on the widget: increments the
 * active plant's logCount without opening the app, then refreshes every widget.
 */
class LogWinAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        PlantRepository.getInstance(context).logWin()
        PlantWidget().updateAll(context)
    }
}
