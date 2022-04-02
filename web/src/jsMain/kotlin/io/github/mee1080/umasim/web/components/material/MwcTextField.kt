@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcTextField(
    value: Number?,
    attrs: (MwcTextFieldAttrsScope<Number?>.() -> Unit)? = null,
) = MwcTextFieldImpl(InputType.Number, value.toString(), attrs)

@Composable
fun MwcTextField(
    value: String,
    attrs: (MwcTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MwcTextFieldImpl(InputType.Text, value, attrs)

@Composable
fun MwcTextField(
    type: InputType<String>,
    value: String,
    attrs: (MwcTextFieldAttrsScope<String>.() -> Unit)? = null,
) = MwcTextFieldImpl(type, value, attrs)

@Composable
private fun <T> MwcTextFieldImpl(
    type: InputType<T>,
    value: String,
    attrs: (MwcTextFieldAttrsScope<T>.() -> Unit)?,
) {
    TagElement<MwcTextFieldElement>("mwc-textfield", {
        val scope = MwcTextFieldAttrsScope(type, this)
        attrs?.invoke(scope)
        value(value)
    }, null)
}

abstract external class MwcTextFieldElement : HTMLElement

class MwcTextFieldAttrsScope<ValueType>(
    val inputType: InputType<ValueType>,
    attrsScope: AttrsScope<MwcTextFieldElement>,
) : AttrsScope<MwcTextFieldElement> by attrsScope {

    init {
        attr("type", inputType.typeStr)
    }

    fun onInput(
        listener: (ValueType) -> Unit
    ) {
        addEventListener("input") {
            listener(inputType.inputValue(it.nativeEvent))
        }
    }
}

fun AttrsScope<MwcTextFieldElement>.value(value: String) = attr("value", value)
fun AttrsScope<MwcTextFieldElement>.label(value: String) = attr("label", value)
fun AttrsScope<MwcTextFieldElement>.placeholder(value: String) = attr("placeholder", value)
fun AttrsScope<MwcTextFieldElement>.prefix(value: String) = attr("prefix", value)
fun AttrsScope<MwcTextFieldElement>.suffix(value: String) = attr("suffix", value)
fun AttrsScope<MwcTextFieldElement>.icon(value: String) = attr("icon", value)
fun AttrsScope<MwcTextFieldElement>.iconTrailing(value: String) = attr("iconTrailing", value)
fun AttrsScope<MwcTextFieldElement>.disabled() = attr("disabled", "")
fun AttrsScope<MwcTextFieldElement>.charCounter() = attr("charCounter", "")
fun AttrsScope<MwcTextFieldElement>.outlined() = attr("outlined", "")
fun AttrsScope<MwcTextFieldElement>.helper(value: String) = attr("helper", value)
fun AttrsScope<MwcTextFieldElement>.helperPersistent() = attr("helperPersistent", "")
fun AttrsScope<MwcTextFieldElement>.required() = attr("required", "")
fun AttrsScope<MwcTextFieldElement>.maxLength(value: Int) = attr("maxLength", value.toString())
fun AttrsScope<MwcTextFieldElement>.validationMessage(value: String) = attr("validationMessage", value)
fun AttrsScope<MwcTextFieldElement>.pattern(value: String) = attr("pattern", value)
fun AttrsScope<MwcTextFieldElement>.min(value: String) = attr("min", value)
fun AttrsScope<MwcTextFieldElement>.max(value: String) = attr("max", value)
fun AttrsScope<MwcTextFieldElement>.size(value: Int) = attr("size", value.toString())
fun AttrsScope<MwcTextFieldElement>.step(value: Int) = attr("step", value.toString())
fun AttrsScope<MwcTextFieldElement>.autoValidate() = attr("autoValidate", "")
fun AttrsScope<MwcTextFieldElement>.validateOnInitialRender() = attr("validateOnInitialRender", "")
fun AttrsScope<MwcTextFieldElement>.name(value: String) = attr("name", value)