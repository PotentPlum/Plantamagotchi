# Plantamagotchi

A daily habit tracker where every habit "win" helps a plant grow. Log wins from the
app or straight from the interactive home screen widget, harvest mature plants and
collect them in your terrarium.

## Tech stack

- Kotlin, Jetpack Compose (Material 3)
- Jetpack Glance for the interactive home screen widget
- Room for local persistence
- Gradle Kotlin DSL with a version catalog (`gradle/libs.versions.toml`)

## Features

- **Widget** (`widget/PlantWidget.kt`): shows the plant, habit name and a **Log Win**
  button. Tapping it runs `LogWinAction`, which increments `logCount` in Room without
  opening the app; the widget observes the database and re-renders immediately.
  - Growth stages: 0–5 seed, 6–12 sprout, 13–20 vegetative, 21+ mature.
  - Between 8 PM and 6 AM the widget darkens and shows a "sleeping" moon overlay.
- **Garden (dashboard)**: current plant, progress and a **Harvest** button that unlocks
  at 21 wins. Harvesting moves the plant into the terrarium, resets the log count and
  rolls a new random plant type.
- **Terrarium**: grid of every harvested plant.
- **Customize**: rename the habit and pick pot and background styles (also shown on
  the widget).

## Building

Requires JDK 17 and the Android SDK (API 35).

```sh
./gradlew assembleDebug      # APK at app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest  # unit tests
```

## Releases

Pushing a tag that starts with `v` (for example `git tag v1.0.0 && git push origin v1.0.0`)
runs `.github/workflows/android.yml`, which builds the debug APK and attaches it to a
GitHub Release.
