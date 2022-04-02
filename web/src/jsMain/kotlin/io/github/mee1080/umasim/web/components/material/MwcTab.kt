@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcTab(
    label: String? = null,
    icon: String? = null,
    attrs: AttrsScope<MwcTabElement>.() -> Unit = {},
) {
    MwcTab {
        label?.let { label(it) }
        icon?.let { icon(it) }
        attrs()
    }
}

@Composable
fun MwcTab(
    attrs: AttrsScope<MwcTabElement>.() -> Unit,
) {
    TagElement("mwc-tab", attrs, null)
}

abstract external class MwcTabElement : HTMLElement

fun AttrsScope<MwcTabElement>.label(value: String) = attr("label", value)
fun AttrsScope<MwcTabElement>.icon(value: String) = attr("icon", value)
fun AttrsScope<MwcTabElement>.hasImageIcon() = attr("hasImageIcon", "")
fun AttrsScope<MwcTabElement>.indicatorIcon(value: String) = attr("indicatorIcon", value)
fun AttrsScope<MwcTabElement>.fadingIndicator() = attr("isFadingIndicator", "")
fun AttrsScope<MwcTabElement>.minWidth() = attr("minWidth", "")
fun AttrsScope<MwcTabElement>.binWidthIndicator() = attr("isMinWidthIndicator", "")
fun AttrsScope<MwcTabElement>.stacked() = attr("stacked", "")