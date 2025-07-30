package com.denchic45.rickandmorty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.denchic45.rickandmorty.R

val onestFont = FontFamily(
    Font(R.font.onest_light, FontWeight.Light),
    Font(R.font.onest_regular, FontWeight.Normal),
    Font(R.font.onest_medium, FontWeight.Medium),
    Font(R.font.onest_semibold, FontWeight.SemiBold),
    Font(R.font.onest_bold, FontWeight.Bold),
)

val robotoFont = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_semibold, FontWeight.SemiBold),
    Font(R.font.roboto_bold, FontWeight.Bold),
)


val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = onestFont),
    displayMedium = baseline.displayMedium.copy(fontFamily = onestFont),
    displaySmall = baseline.displaySmall.copy(fontFamily = onestFont),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = onestFont),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = onestFont),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = onestFont),
    titleLarge = baseline.titleLarge.copy(fontFamily = onestFont),
    titleMedium = baseline.titleMedium.copy(fontFamily = onestFont),
    titleSmall = baseline.titleSmall.copy(fontFamily = onestFont),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = robotoFont),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = robotoFont),
    bodySmall = baseline.bodySmall.copy(fontFamily = robotoFont),
    labelLarge = baseline.labelLarge.copy(fontFamily = robotoFont),
    labelMedium = baseline.labelMedium.copy(fontFamily = robotoFont),
    labelSmall = baseline.labelSmall.copy(fontFamily = robotoFont),
)