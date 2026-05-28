package com.example.amphibians.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WoodAmberDark,          // Helles Holz als Hauptakzent
    secondary = MossGreenDark,        // Moosgrün für Abwechslung
    tertiary = WoodBeigeLight,        // Ganz helles Holz als dritter Akzent
    background = JungleDarkBg,        // Dunkelgrüner Dschungel-Hintergrund
    surface = DeepForestDark,         // Etwas helleres Grün für Karten/Listen
    onPrimary = JungleDarkBg,         // Dunkler Text auf hellen Holz-Buttons
    onBackground = WoodBeigeLight,    // Heller Holzton für Text auf dem Hintergrund
    onSurface = WoodBeigeLight        // Heller Holzton für Text auf Karten
)

private val LightColorScheme = lightColorScheme(
    primary = JungleGreenLight,       // Dunkelgrün als Hauptfarbe für Buttons
    secondary = MossGreenLight,       // Moosgrün für sekundäre Elemente
    tertiary = SoftWoodLight,         // Akzentfarbe
    background = WoodBeigeLight,      // Helles Holz als Haupt-Hintergrund
    surface = SoftWoodLight,          // Etwas dunkleres Holz für Karten/Listen
    onPrimary = WoodBeigeLight,       // Heller Text auf dunkelgrünen Buttons
    onBackground = JungleGreenLight,  // Dunkelgrüner Text auf hellem Holz-Hintergrund
    onSurface = JungleGreenLight      // Dunkelgrüner Text auf Karten
)

@Composable
fun AmphibiansTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Auf 'false' gesetzt, damit deine Dschungelfarben erzwungen werden
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
