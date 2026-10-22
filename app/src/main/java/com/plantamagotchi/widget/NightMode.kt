package com.plantamagotchi.widget

import java.time.LocalTime

private const val NIGHT_START_HOUR = 20 // 8 PM
private const val NIGHT_END_HOUR = 6 // 6 AM

/** The plant "sleeps" between 8 PM (inclusive) and 6 AM (exclusive). */
fun isNightTime(time: LocalTime = LocalTime.now()): Boolean =
    time.hour >= NIGHT_START_HOUR || time.hour < NIGHT_END_HOUR
