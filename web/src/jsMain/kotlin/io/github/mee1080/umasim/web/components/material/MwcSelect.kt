@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import io.github.mee1080.umasim.web.components.material.generateId
import io.github.mee1080.umasim.web.components.material.getElementById
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLElement

@Composable
fun <T : Any> MwcSelect(
    selection: List<T>,
    selectedItem: T?,
    attrs: AttrBuilderContext<MwcSelectElement>? = null,
    onSelect: (T) -> Unit = {},
    itemToValue: (T) -> String = { it.toString() },
    itemToKey: (T) -> Any = { it },
    itemContent: @Composable ElementScope<MwcListItemElement>.(T) -> Unit = { Text(itemToValue(it)) },
) {
    val id = remember { generateId() }
    val currentSelected = selectedItem ?: selection.firstOrNull() ?: return
    MwcSelect({
        id(id)
        attrs?.invoke(this)
        value(itemToValue(currentSelected))
    }) {
        selection.forEach { item ->
            MwcListItem(itemToValue(item), itemToKey(item), {
                onClick { onSelect(item) }
            }) {
                itemContent(item)
            }
        }
    }
    LaunchedEffect(currentSelected) {
        getElementById<MwcSelectElement>(id).value = itemToValue(currentSelected)
    }
}

@Composable
private fun MwcSelect(
    attrs: AttrBuilderContext<MwcSelectElement>? = null,
    content: ContentBuilder<MwcSelectElement>? = null
) {
    TagElement("mwc-select", attrs, content)
}

abstract external class MwcSelectElement : HTMLElement {
    var value: String
}

fun AttrsScope<MwcSelectElement>.value(value: String) =
    attr("value", value)

fun AttrsScope<MwcSelectElement>.label(value: String) =
    attr("label", value)

fun AttrsScope<MwcSelectElement>.naturalMenuWidth() =
    attr("naturalMenuWidth", "")

fun AttrsScope<MwcSelectElement>.fixedMenuPosition() =
    attr("fixedMenuPosition", "")

fun AttrsScope<MwcSelectElement>.icon(value: String) =
    attr("icon", value)

fun AttrsScope<MwcSelectElement>.disabled() =
    attr("disabled", "")

fun AttrsScope<MwcSelectElement>.outlined() =
    attr("outlined", "")

fun AttrsScope<MwcSelectElement>.helper(value: String) =
    attr("helper", value)

fun AttrsScope<MwcSelectElement>.required() =
    attr("required", "")

fun AttrsScope<MwcSelectElement>.validationMessage(value: String) =
    attr("validationMessage", value)