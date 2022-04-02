@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.material

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

@Composable
fun <T> MwcTabBar(
    selection: List<T>,
    selectedItem: T? = null,
    itemToLabel: (T) -> String? = { null },
    itemToIcon: (T) -> String? = { null },
    onSelect: (T) -> Unit = {},
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
) {
    val activeIndex = selection.indexOfFirst { it == selectedItem }.coerceAtLeast(0)
    MwcTabBar(activeIndex, attrs) {
        selection.forEach { item ->
            key(item) {
                MwcTab(label = itemToLabel(item), icon = itemToIcon(item)) {
                    onClick { onSelect(item) }
                }
            }
        }
    }
}

@Composable
fun MwcTextTabBar(
    selection: List<String>,
    selectedItem: String? = null,
    onSelect: (String) -> Unit = {},
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
) {
    val activeIndex = selection.indexOf(selectedItem ?: "").coerceAtLeast(0)
    MwcTabBar(activeIndex, attrs) {
        selection.forEach { item ->
            key(item) {
                MwcTab(label = item) {
                    onClick { onSelect(item) }
                }
            }
        }
    }
}

@Composable
fun MwcIconTabBar(
    selection: List<String>,
    selectedItem: String? = null,
    onSelect: (String) -> Unit = {},
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
) {
    val activeIndex = selection.indexOf(selectedItem ?: "").coerceAtLeast(0)
    MwcTabBar(activeIndex, attrs) {
        selection.forEach { item ->
            key(item) {
                MwcTab(icon = item) {
                    onClick { onSelect(item) }
                }
            }
        }
    }
}

@Composable
fun MwcTabBar(
    selection: List<Pair<String, String>>,
    selectedItem: String? = null,
    onSelect: (String) -> Unit = {},
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
) {
    val activeIndex = selection.indexOfFirst { it.first == selectedItem }.coerceAtLeast(0)
    MwcTabBar(activeIndex, attrs) {
        selection.forEach { item ->
            key(item) {
                MwcTab(label = item.first, icon = item.second) {
                    onClick { onSelect(item.first) }
                }
            }
        }
    }
}

@Composable
fun MwcTabBar(
    activeIndex: Int,
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
    content: ContentBuilder<MwcTabBarElement>,
) {
    MwcTabBar({
        activeIndex(activeIndex)
        attrs()
    }, content)
}

@Composable
fun MwcTabBar(
    attrs: AttrsScope<MwcTabBarElement>.() -> Unit = {},
    content: ContentBuilder<MwcTabBarElement>,
) {
    TagElement("mwc-tab-bar", attrs, content)
}

abstract external class MwcTabBarElement : HTMLElement

fun AttrsScope<MwcTabBarElement>.activeIndex(value: Int) = attr("activeIndex", value.toString())