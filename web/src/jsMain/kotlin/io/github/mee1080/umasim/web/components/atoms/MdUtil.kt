package io.github.mee1080.umasim.web.components.atoms

import io.github.mee1080.umasim.web.components.lib.CSSVar
import io.github.mee1080.umasim.web.components.lib.setVar
import io.github.mee1080.umasim.web.components.lib.stringValue
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*

fun StyleScope.primaryToSecondary() {
    setVar(MdSysColor.primary, MdSysColor.secondary)
    setVar(MdSysColor.onPrimary, MdSysColor.onSecondary)
}

fun StyleScope.primaryToTertiary() {
    setVar(MdSysColor.primary, MdSysColor.tertiary)
    setVar(MdSysColor.onPrimary, MdSysColor.onTertiary)
}

fun AttrsScope<*>.secondary() = style { primaryToSecondary() }

fun AttrsScope<*>.tertiary() = style { primaryToTertiary() }

@Suppress("unused", "ConstPropertyName")
object MdClass {
    const val primary = "primary"
    const val primaryText = "primary-text"
    const val onPrimary = "on-primary"
    const val onPrimaryText = "on-primary-text"
    const val primaryContainer = "primary-container"
    const val primaryContainerText = "primary-container-text"
    const val onPrimaryContainer = "on-primary-container"
    const val onPrimaryContainerText = "on-primary-container-text"
    const val primaryFixed = "primary-fixed"
    const val primaryFixedText = "primary-fixed-text"
    const val onPrimaryFixed = "on-primary-fixed"
    const val onPrimaryFixedText = "on-primary-fixed-text"
    const val primaryFixedDim = "primary-fixed-dim"
    const val primaryFixedDimText = "primary-fixed-dim-text"
    const val onPrimaryFixedVariant = "on-primary-fixed-variant"
    const val onPrimaryFixedVariantText = "on-primary-fixed-variant-text"
    const val secondary = "secondary"
    const val secondaryText = "secondary-text"
    const val onSecondary = "on-secondary"
    const val onSecondaryText = "on-secondary-text"
    const val secondaryContainer = "secondary-container"
    const val secondaryContainerText = "secondary-container-text"
    const val onSecondaryContainer = "on-secondary-container"
    const val onSecondaryContainerText = "on-secondary-container-text"
    const val secondaryFixed = "secondary-fixed"
    const val secondaryFixedText = "secondary-fixed-text"
    const val onSecondaryFixed = "on-secondary-fixed"
    const val onSecondaryFixedText = "on-secondary-fixed-text"
    const val secondaryFixedDim = "secondary-fixed-dim"
    const val secondaryFixedDimText = "secondary-fixed-dim-text"
    const val onSecondaryFixedVariant = "on-secondary-fixed-variant"
    const val onSecondaryFixedVariantText = "on-secondary-fixed-variant-text"
    const val tertiary = "tertiary"
    const val tertiaryText = "tertiary-text"
    const val onTertiary = "on-tertiary"
    const val onTertiaryText = "on-tertiary-text"
    const val tertiaryContainer = "tertiary-container"
    const val tertiaryContainerText = "tertiary-container-text"
    const val onTertiaryContainer = "on-tertiary-container"
    const val onTertiaryContainerText = "on-tertiary-container-text"
    const val tertiaryFixed = "tertiary-fixed"
    const val tertiaryFixedText = "tertiary-fixed-text"
    const val onTertiaryFixed = "on-tertiary-fixed"
    const val onTertiaryFixedText = "on-tertiary-fixed-text"
    const val tertiaryFixedDim = "tertiary-fixed-dim"
    const val tertiaryFixedDimText = "tertiary-fixed-dim-text"
    const val onTertiaryFixedVariant = "on-tertiary-fixed-variant"
    const val onTertiaryFixedVariantText = "on-tertiary-fixed-variant-text"
    const val error = "error"
    const val errorText = "error-text"
    const val errorContainer = "error-container"
    const val errorContainerText = "error-container-text"
    const val onError = "on-error"
    const val onErrorText = "on-error-text"
    const val onErrorContainer = "on-error-container"
    const val onErrorContainerText = "on-error-container-text"
    const val background = "background"
    const val backgroundText = "background-text"
    const val onBackground = "on-background"
    const val onBackgroundText = "on-background-text"
    const val outline = "outline"
    const val outlineText = "outline-text"
    const val inverseOnSurface = "inverse-on-surface"
    const val inverseOnSurfaceText = "inverse-on-surface-text"
    const val inverseSurface = "inverse-surface"
    const val inverseSurfaceText = "inverse-surface-text"
    const val inversePrimary = "inverse-primary"
    const val inversePrimaryText = "inverse-primary-text"
    const val shadow = "shadow"
    const val shadowText = "shadow-text"
    const val surfaceTint = "surface-tint"
    const val surfaceTintText = "surface-tint-text"
    const val outlineVariant = "outline-variant"
    const val outlineVariantText = "outline-variant-text"
    const val scrim = "scrim"
    const val scrimText = "scrim-text"
    const val surface = "surface"
    const val surfaceText = "surface-text"
    const val onSurface = "on-surface"
    const val onSurfaceText = "on-surface-text"
    const val surfaceVariant = "surface-variant"
    const val surfaceVariantText = "surface-variant-text"
    const val onSurfaceVariant = "on-surface-variant"
    const val onSurfaceVariantText = "on-surface-variant-text"
    const val surfaceContainerHighest = "surface-container-highest"
    const val surfaceContainerHighestText = "surface-container-highest-text"
    const val surfaceContainerHigh = "surface-container-high"
    const val surfaceContainerHighText = "surface-container-high-text"
    const val surfaceContainer = "surface-container"
    const val surfaceContainerText = "surface-container-text"
    const val surfaceContainerLow = "surface-container-low"
    const val surfaceContainerLowText = "surface-container-low-text"
    const val surfaceContainerLowest = "surface-container-lowest"
    const val surfaceContainerLowestText = "surface-container-lowest-text"
    const val surfaceDim = "surface-dim"
    const val surfaceDimText = "surface-dim-text"
    const val surfaceBright = "surface-bright"
    const val surfaceBrightText = "surface-bright-text"
}

@Suppress("unused")
object MdSysColor {
    val primary = CSSVar<CSSColorValue>("--md-sys-color-primary")
    val onPrimary = CSSVar<CSSColorValue>("--md-sys-color-on-primary")
    val primaryContainer = CSSVar<CSSColorValue>("--md-sys-color-primary-container")
    val onPrimaryContainer = CSSVar<CSSColorValue>("--md-sys-color-on-primary-container")
    val primaryFixed = CSSVar<CSSColorValue>("--md-sys-color-primary-fixed")
    val onPrimaryFixed = CSSVar<CSSColorValue>("--md-sys-color-on-primary-fixed")
    val primaryFixedDim = CSSVar<CSSColorValue>("--md-sys-color-primary-fixed-dim")
    val onPrimaryFixedVariant = CSSVar<CSSColorValue>("--md-sys-color-on-primary-fixed-variant")
    val secondary = CSSVar<CSSColorValue>("--md-sys-color-secondary")
    val onSecondary = CSSVar<CSSColorValue>("--md-sys-color-on-secondary")
    val secondaryContainer = CSSVar<CSSColorValue>("--md-sys-color-secondary-container")
    val onSecondaryContainer = CSSVar<CSSColorValue>("--md-sys-color-on-secondary-container")
    val secondaryFixed = CSSVar<CSSColorValue>("--md-sys-color-secondary-fixed")
    val onSecondaryFixed = CSSVar<CSSColorValue>("--md-sys-color-on-secondary-fixed")
    val secondaryFixedDim = CSSVar<CSSColorValue>("--md-sys-color-secondary-fixed-dim")
    val onSecondaryFixedVariant = CSSVar<CSSColorValue>("--md-sys-color-on-secondary-fixed-variant")
    val tertiary = CSSVar<CSSColorValue>("--md-sys-color-tertiary")
    val onTertiary = CSSVar<CSSColorValue>("--md-sys-color-on-tertiary")
    val tertiaryContainer = CSSVar<CSSColorValue>("--md-sys-color-tertiary-container")
    val onTertiaryContainer = CSSVar<CSSColorValue>("--md-sys-color-on-tertiary-container")
    val tertiaryFixed = CSSVar<CSSColorValue>("--md-sys-color-tertiary-fixed")
    val onTertiaryFixed = CSSVar<CSSColorValue>("--md-sys-color-on-tertiary-fixed")
    val tertiaryFixedDim = CSSVar<CSSColorValue>("--md-sys-color-tertiary-fixed-dim")
    val onTertiaryFixedVariant = CSSVar<CSSColorValue>("--md-sys-color-on-tertiary-fixed-variant")
    val error = CSSVar<CSSColorValue>("--md-sys-color-error")
    val errorContainer = CSSVar<CSSColorValue>("--md-sys-color-error-container")
    val onError = CSSVar<CSSColorValue>("--md-sys-color-on-error")
    val onErrorContainer = CSSVar<CSSColorValue>("--md-sys-color-on-error-container")
    val background = CSSVar<CSSColorValue>("--md-sys-color-background")
    val onBackground = CSSVar<CSSColorValue>("--md-sys-color-on-background")
    val outline = CSSVar<CSSColorValue>("--md-sys-color-outline")
    val inverseOnSurface = CSSVar<CSSColorValue>("--md-sys-color-inverse-on-surface")
    val inverseSurface = CSSVar<CSSColorValue>("--md-sys-color-inverse-surface")
    val inversePrimary = CSSVar<CSSColorValue>("--md-sys-color-inverse-primary")
    val shadow = CSSVar<CSSColorValue>("--md-sys-color-shadow")
    val surfaceTint = CSSVar<CSSColorValue>("--md-sys-color-surface-tint")
    val outlineVariant = CSSVar<CSSColorValue>("--md-sys-color-outline-variant")
    val scrim = CSSVar<CSSColorValue>("--md-sys-color-scrim")
    val surface = CSSVar<CSSColorValue>("--md-sys-color-surface")
    val onSurface = CSSVar<CSSColorValue>("--md-sys-color-on-surface")
    val surfaceVariant = CSSVar<CSSColorValue>("--md-sys-color-surface-variant")
    val onSurfaceVariant = CSSVar<CSSColorValue>("--md-sys-color-on-surface-variant")
    val surfaceContainerHighest = CSSVar<CSSColorValue>("--md-sys-color-surface-container-highest")
    val surfaceContainerHigh = CSSVar<CSSColorValue>("--md-sys-color-surface-container-high")
    val surfaceContainer = CSSVar<CSSColorValue>("--md-sys-color-surface-container")
    val surfaceContainerLow = CSSVar<CSSColorValue>("--md-sys-color-surface-container-low")
    val surfaceContainerLowest = CSSVar<CSSColorValue>("--md-sys-color-surface-container-lowest")
    val surfaceDim = CSSVar<CSSColorValue>("--md-sys-color-surface-dim")
    val surfaceBright = CSSVar<CSSColorValue>("--md-sys-color-surface-bright")
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
class MdSysTypeScale private constructor(
    name: String,
) {
    val fontFamilyName = CSSVar<StylePropertyString>("--md-sys-typescale-$name-font-family-name")
    val fontFamilyStyle = CSSVar<StylePropertyString>("--md-sys-typescale-$name-font-family-style")
    val fontWeight = CSSVar<StylePropertyNumber>("--md-sys-typescale-$name-font-weight")
    val fontSize = CSSVar<CSSNumeric>("--md-sys-typescale-$name-font-size")
    val lineHeight = CSSVar<CSSNumeric>("--md-sys-typescale-$name-line-height")
    val letterSpacing = CSSVar<CSSNumeric>("--md-sys-typescale-$name-letter-spacing")

    operator fun invoke(scope: StyleScope) {
        scope.fontFamily(fontFamilyName.stringValue)
        // FIXME 設定方法確認
//        scope.fontStyle(fontFamilyStyle.stringValue)
        // FIXME 不正な値が入っている(400px等)
//        scope.fontWeight(fontWeight.intValue)
        scope.fontSize(fontSize.value)
        scope.lineHeight(lineHeight.value)
        scope.letterSpacing(letterSpacing.value)
    }

    companion object {
        val displayLarge = MdSysTypeScale("display-large")
        val displayMedium = MdSysTypeScale("display-medium")
        val displaySmall = MdSysTypeScale("display-small")
        val headlineLarge = MdSysTypeScale("headline-large")
        val headlineMedium = MdSysTypeScale("headline-medium")
        val headlineSmall = MdSysTypeScale("headline-small")
        val bodyLarge = MdSysTypeScale("body-large")
        val bodyMedium = MdSysTypeScale("body-medium")
        val bodySmall = MdSysTypeScale("body-small")
        val labelLarge = MdSysTypeScale("label-large")
        val labelMedium = MdSysTypeScale("label-medium")
        val labelSmall = MdSysTypeScale("label-small")
        val titleLarge = MdSysTypeScale("title-large")
        val titleMedium = MdSysTypeScale("title-medium")
        val titleSmall = MdSysTypeScale("title-small")
    }
}

fun StyleScope.typeScale(value: MdSysTypeScale) = value(this)