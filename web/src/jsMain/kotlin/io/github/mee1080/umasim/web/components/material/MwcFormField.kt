@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcFormField(
    label: String,
    attrs: AttrsScope<MwcFormFieldElement>.() -> Unit = {},
    component: @Composable ElementScope<MwcFormFieldElement>.() -> Unit,
) {
    TagElement("mwc-formfield", {
        attr("label", label)
        attrs()
    }, component)
}

abstract external class MwcFormFieldElement : HTMLElement

fun AttrsScope<MwcFormFieldElement>.label(value: String) = attr("label", value)
fun AttrsScope<MwcFormFieldElement>.alignEnd() = attr("alignEnd", "")
fun AttrsScope<MwcFormFieldElement>.spaceBetween() = attr("spaceBetween", "")
fun AttrsScope<MwcFormFieldElement>.nowrap() = attr("nowrap", "")