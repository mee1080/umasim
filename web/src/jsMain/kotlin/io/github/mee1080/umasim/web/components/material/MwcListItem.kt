package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun MwcListItem(
    value: String,
    key: Any,
    attrs: AttrBuilderContext<MwcListItemElement>? = null,
    content: ContentBuilder<MwcListItemElement>? = null
) {
    key(key) {
        TagElement("mwc-list-item", {
            attrs?.invoke(this)
            value(value)
        }, content)
    }
}

abstract external class MwcListItemElement : HTMLElement

fun AttrsScope<MwcListItemElement>.group(value: String) =
    attr("group", value)

fun AttrsScope<MwcListItemElement>.value(value: String) =
    attr("value", value)

fun AttrsScope<MwcListItemElement>.tabindex(value: Int) =
    attr("tabindex", value.toString())

fun AttrsScope<MwcListItemElement>.disabled() =
    attr("disabled", "")

fun AttrsScope<MwcListItemElement>.twoline() =
    attr("twoline", "")

fun AttrsScope<MwcListItemElement>.activated() =
    attr("activated", "")

fun AttrsScope<MwcListItemElement>.selected() =
    attr("selected", "")