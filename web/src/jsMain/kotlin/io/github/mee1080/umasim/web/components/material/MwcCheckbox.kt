@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcCheckbox(
    label: String,
    checked: Boolean = false,
    labelAttrs: AttrsScope<MwcFormFieldElement>.() -> Unit = {},
    attrs: MwcCheckboxAttrsScope.() -> Unit = {},
) {
    MwcFormField(label, labelAttrs) {
        MwcCheckbox(checked, attrs)
    }
}

@Composable
fun MwcCheckbox(
    checked: Boolean = false,
    attrs: MwcCheckboxAttrsScope.() -> Unit = {},
) {
    TagElement<MwcCheckboxElement>("mwc-checkbox", {
        val scope = MwcCheckboxAttrsScope(this)
        attrs.invoke(scope)
        if (checked) checked()
    }, null)
}

abstract external class MwcCheckboxElement : HTMLElement

class MwcCheckboxAttrsScope(
    attrsScope: AttrsScope<MwcCheckboxElement>
) : AttrsScope<MwcCheckboxElement> by attrsScope {
    fun onChange(
        listener: (Boolean) -> Unit
    ) {
        addEventListener("change") {
            listener(InputType.Checkbox.inputValue(it.nativeEvent))
        }
    }
}

fun AttrsScope<MwcCheckboxElement>.checked() = attr("checked", "")
fun AttrsScope<MwcCheckboxElement>.indeterminate() = attr("indeterminate", "")
fun AttrsScope<MwcCheckboxElement>.disabled() = attr("disabled", "")
fun AttrsScope<MwcCheckboxElement>.value(value: String) = attr("value", value)
fun AttrsScope<MwcCheckboxElement>.reducedTouchTarget() = attr("reducedTouchTarget", "")