@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLButtonElement

@Composable
fun MwcButton(
    attrs: AttrBuilderContext<MwcButtonElement>? = null,
    content: ContentBuilder<MwcButtonElement>? = null
) {
    TagElement("mwc-button", attrs, content)
}

abstract external class MwcButtonElement : HTMLButtonElement

fun AttrsScope<MwcButtonElement>.ariaHaspopup(value: String) = attr("aria-haspopup", value)
fun AttrsScope<MwcButtonElement>.icon(value: String) = attr("icon", value)
fun AttrsScope<MwcButtonElement>.label(value: String) = attr("label", value)
fun AttrsScope<MwcButtonElement>.raised() = attr("raised", "")
fun AttrsScope<MwcButtonElement>.unElevated() = attr("unelevated", "")
fun AttrsScope<MwcButtonElement>.outlined() = attr("outlined", "")
fun AttrsScope<MwcButtonElement>.dense() = attr("dense", "")
fun AttrsScope<MwcButtonElement>.disabled() = attr("disabled", "")
fun AttrsScope<MwcButtonElement>.trailingIcon() = attr("trailingIcon", "")
fun AttrsScope<MwcButtonElement>.expandContent() = attr("expandContent", "")
fun AttrsScope<MwcButtonElement>.fullWidth() = attr("fullwidth", "")