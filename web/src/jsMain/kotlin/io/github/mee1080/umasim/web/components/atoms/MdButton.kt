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
import io.github.mee1080.umasim.web.components.lib.*
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.TagElement
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/button/elevated-button.js")
        require("@material/web/button/filled-button.js")
        require("@material/web/button/outlined-button.js")
        require("@material/web/button/text-button.js")
        require("@material/web/button/filled-tonal-button.js")
    }
}

@Composable
private fun <T : MdButtonElement> MdButton(
    type: String,
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<T>? = null,
) {
    TagElement<T>("md-$type-button", {
        attrs?.invoke(this)
    }) {
        if (icon != null) {
            MdIcon(icon) { slot("icon") }
        }
        Text(label)
    }
}

@Composable
fun MdElevatedButton(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdElevatedButtonElement>? = null,
) {
    MdButton("elevated", label, icon, attrs)
}

@Composable
fun MdFilledButton(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdFilledButtonElement>? = null,
) {
    MdButton("filled", label, icon, attrs)
}

@Composable
fun MdEOutlinedButton(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdOutlinedButtonElement>? = null,
) {
    MdButton("outlined", label, icon, attrs)
}

@Composable
fun MdTextButton(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdTextButtonElement>? = null,
) {
    MdButton("text", label, icon, attrs)
}

@Composable
fun MdFilledTonalButton(
    label: String,
    icon: String? = null,
    attrs: AttrBuilderContext<MdFilledTonalButtonElement>? = null,
) {
    MdButton("filled-tonal", label, icon, attrs)
}

abstract external class MdButtonElement : HTMLElement
abstract external class MdElevatedButtonElement : MdButtonElement
abstract external class MdFilledButtonElement : MdButtonElement
abstract external class MdFilledTonalButtonElement : MdButtonElement
abstract external class MdOutlinedButtonElement : MdButtonElement
abstract external class MdTextButtonElement : MdButtonElement

/** Whether or not the button is disabled. */
fun AttrsScope<MdButtonElement>.disabled() = attr("disabled", "")

/** The URL that the link button points to. */
fun AttrsScope<MdButtonElement>.href(value: String) = attr("href", value)

/** Where to display the linked href URL for a link button. Common options include _blank to open in a new tab. */
fun AttrsScope<MdButtonElement>.target(value: ATarget) = attr("target", value.targetStr)

/** Whether to render the icon at the inline end of the label rather than the inline start. */
fun AttrsScope<MdButtonElement>.trailingIcon() = attr("trailing-icon", "")

/** Whether to display the icon or not. */
fun AttrsScope<MdButtonElement>.hasIcon() = attr("has-icon", "")

fun AttrsScope<MdButtonElement>.type(value: ButtonType) = attr("type", value.str)
fun AttrsScope<MdButtonElement>.value(value: String) = attr("value", value)
fun AttrsScope<MdButtonElement>.name(value: String) = attr("name", value)
fun AttrsScope<MdButtonElement>.form(value: String) = attr("form", value)

// [1] StyleScopeの拡張関数でCSS変数を設定
// pros: 使用側は最低限の記述量
// cons: StyleSheetでは不具合のため設定できない
fun TypedStyleScope<MdElevatedButtonElement>.containerColor(value: CSSColorValue) =
    variable("--md-elevated-button-container-color", value)

// [2] 変数でCSS変数オブジェクトを保持、StyleScopeの拡張プロパティでCSS変数オブジェクトを取得
// pros: context receiverが使用可能になれば、使用側の記述内容は[1]と同等
// cons: 名前衝突回避のためライブラリ側の記述量が増える
//       StyleScopeの拡張プロパティを使用しなければ記述量が少なくなるが、名前衝突回避が使用側に影響する
val filledButtonContainerColor = CSSVar<CSSColorValue>("--md-filled-button-container-color")

val TypedStyleScope<MdFilledButtonElement>.containerColor get() = filledButtonContainerColor

// [3] publicオブジェクトでCSS変数オブジェクトを保持、StyleScopeの拡張プロパティでCSS変数保持オブジェクトを取得
// pros: CSS変数保持オブジェクトを使用して、親ElementやStyleSheetで設定可能
//       1変数あたりのライブラリ側の記述量が少ない
// cons: 使用側の記述量がやや増える
object MdFilledTonalButtonVariables {
    val containerColor = CSSVar<CSSColorValue>("--md-filled-tonal-button-container-color")
}

val TypedStyleScope<MdFilledTonalButtonElement>.variables get() = MdFilledTonalButtonVariables

// [4] StyleScopeの拡張プロパティでCSS変数オブジェクトを生成
// pros: 1変数あたりのライブラリ側の記述量が少ない
// cons: 毎回オブジェクト生成が必要
val TypedStyleScope<MdOutlinedButtonElement>.buttonOutlineColor
    get() = ScopedCSSVar<CSSColorValue>(this, "--md-outlined-button-outline-color")

// [5] 使用側で変数名を指定してCSS変数を設定する
// pros: ライブラリ側の実装不要
// cons: 使用側でAPI調査が必要

// [6] StyleScopeの拡張プロパティとしてReadWritePropertyを追加
// pros: 1変数あたりのライブラリ側の記述量が少ない
// cons: 通常の記述方法と差がある、トップレベルでメモリを消費する
var TypedStyleScope<MdTextButtonElement>.labelTextColor by CSSVarProperty<CSSColorValue>("--md-text-button-label-text-color")
