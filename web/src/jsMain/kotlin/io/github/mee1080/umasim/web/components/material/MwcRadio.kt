@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement

@Composable
fun <T : Any> MwcRadioGroup(
    selection: List<T>,
    selectedItem: T?,
    attrs: AttrBuilderContext<HTMLSpanElement>? = null,
    radioAttrs: MwcRadioAttrsScope.() -> Unit = {},
    labelAttrs: AttrsScope<MwcFormFieldElement>.() -> Unit = {},
    onSelect: (T) -> Unit = {},
    itemToKey: (T) -> Any = { it },
    itemToLabel: (T) -> String = { it.toString() }
) {
    val name = remember { generateId() }
    Span(attrs) {
        selection.forEach { item ->
            key(itemToKey(item)) {
                MwcRadio(
                    itemToLabel(item),
                    item == selectedItem,
                    labelAttrs,
                ) {
                    name(name)
                    onSelect { onSelect(item) }
                    radioAttrs()
                }
            }
        }
    }
}

@Composable
fun MwcRadio(
    label: String,
    checked: Boolean = false,
    labelAttrs: AttrsScope<MwcFormFieldElement>.() -> Unit = {},
    attrs: MwcRadioAttrsScope.() -> Unit = {},
) {
    MwcFormField(label, labelAttrs) {
        MwcRadio(checked, attrs)
    }
}

@Composable
fun MwcRadio(
    checked: Boolean = false,
    attrs: MwcRadioAttrsScope.() -> Unit = {},
) {
    TagElement<MwcRadioElement>("mwc-radio", {
        val scope = MwcRadioAttrsScope(this)
        attrs.invoke(scope)
        if (checked) checked()
    }, null)
}

abstract external class MwcRadioElement : HTMLElement

class MwcRadioAttrsScope(
    attrsScope: AttrsScope<MwcRadioElement>
) : AttrsScope<MwcRadioElement> by attrsScope {
    fun onSelect(
        listener: () -> Unit
    ) {
        addEventListener("change") {
            if (InputType.Radio.inputValue(it.nativeEvent)) {
                listener()
            }
        }
    }
}

fun AttrsScope<MwcRadioElement>.checked() = attr("checked", "")
fun AttrsScope<MwcRadioElement>.disabled() = attr("disabled", "")
fun AttrsScope<MwcRadioElement>.name(value: String) = attr("name", value)
fun AttrsScope<MwcRadioElement>.value(value: String) = attr("value", value)
fun AttrsScope<MwcRadioElement>.global() = attr("global", "")
fun AttrsScope<MwcRadioElement>.reducedTouchTarget() = attr("reducedTouchTarget", "")
