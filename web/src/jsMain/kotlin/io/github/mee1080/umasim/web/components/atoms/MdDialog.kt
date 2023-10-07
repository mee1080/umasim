/*
 * Copyright 2023 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:Suppress("unused")

package io.github.mee1080.umasim.web.components.atoms

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.lib.Slot
import io.github.mee1080.umasim.web.components.lib.addEventListener
import io.github.mee1080.umasim.web.components.lib.require
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementScope
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/dialog/dialog.js")
    }
}

@Composable
fun MdDialog(
    open: Boolean,
    onPrimaryButton: (() -> Unit)? = null,
    onSecondaryButton: (() -> Unit)? = null,
    onCanceled: (() -> Unit)? = null,
    onClosed: (() -> Unit)? = null,
    primaryButtonLabel: String = "OK",
    secondaryButtonLabel: String = "Cancel",
    attrs: AttrBuilderContext<MdDialogElement>? = null,
    headline: ContentBuilder<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null,
) {
    val hasActions = onPrimaryButton != null || onSecondaryButton != null
    MdDialog(
        open,
        {
            onCanceled?.let { onCancel(it) }
            onClosed?.let { onClosed { it() } }
            attrs?.invoke(this)
        },
        headline,
        if (!hasActions) null else {
            {
                if (onSecondaryButton != null) {
                    MdTextButton(secondaryButtonLabel) {
                        onClick { onSecondaryButton() }
                    }
                }
                if (onPrimaryButton != null) {
                    MdFilledTonalButton(primaryButtonLabel) {
                        onClick { onPrimaryButton() }
                    }
                }
            }
        },
        content
    )
}

@Composable
fun MdDialog(
    open: Boolean,
    attrs: AttrBuilderContext<MdDialogElement>? = null,
    headline: ContentBuilder<HTMLElement>? = null,
    actions: ContentBuilder<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null,
) {
    MdDialog({
        if (open) open()
        attrs?.invoke(this)
    }) {
        Slot("headline", headline)
        Slot("content", content)
        Slot("actions", actions)
    }
}

@Composable
private fun MdDialog(
    attrs: AttrBuilderContext<MdDialogElement>? = null,
    content: @Composable ElementScope<MdDialogElement>.() -> Unit
) {
    TagElement<MdDialogElement>("md-dialog", {
        attrs?.invoke(this)
    }, content)
}

abstract external class MdDialogElement : HTMLElement

fun AttrsScope<MdDialogElement>.open() = attr("open", "")
fun AttrsScope<MdDialogElement>.type(value: String) = attr("type", value)
fun AttrsScope<MdDialogElement>.alert() = type("alert")

fun AttrsScope<MdDialogElement>.onOpen(listener: () -> Unit) = addEventListener("open", listener)
fun AttrsScope<MdDialogElement>.onOpened(listener: () -> Unit) = addEventListener("opened", listener)
fun AttrsScope<MdDialogElement>.onClose(listener: (returnValue: String) -> Unit) =
    addEventListener("close", { returnValue }, listener)

fun AttrsScope<MdDialogElement>.onClosed(listener: (returnValue: String) -> Unit) =
    addEventListener("closed", { returnValue }, listener)

fun AttrsScope<MdDialogElement>.onCancel(listener: () -> Unit) = addEventListener("cancel", listener)
