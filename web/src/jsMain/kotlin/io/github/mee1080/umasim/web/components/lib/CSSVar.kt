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
package io.github.mee1080.umasim.web.components.lib

import org.jetbrains.compose.web.css.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * CSS変数クラス
 *
 * 実装経緯
 * 1. Compose HTML 標準での、CSS変数の設定方法
 *   1. 変数名を文字列で指定し、StyleScope.variableメソッドを呼び出す
 *   2. CSSStyleVariableオブジェクトを生成し、StyleScope内で、CSSStyleVariable.invokeメソッドを呼び出す
 *      内部では、CSSStyleVariableオブジェクトに保存された変数名で、1.のメソッドを呼び出している
 * 2. CSSStyleVariableオブジェクトの生成方法
 *   1. 変数名を文字列で指定し、CSSStyleVariableクラスのコンストラクタを呼び出す
 *   2. トップレベルのvariableメソッドで、CSSStyleVariableオブジェクトを取得する移譲プロパティを作成する
 *      内部では、プロパティ名の文字列を変数名として、1.のコンストラクタを呼び出している
 * 3. StyleScope.variableメソッドの不具合
 *   1. Elementのstyleブロック内で呼び出す場合、変数名の先頭に"--"が必要、ないと設定されない
 *   2. StyleSheetのstyleブロック内で呼び出す場合、変数名の先頭に"--"が不要、あると設定されない
 * 4. CSSStyleVariableオブジェクトを使用する場合の課題
 *   1. 変数名の先頭に"--"が必要なので、variableメソッドは使用不可
 *   2. 3.の不具合により、ElementとStyleSheet両方で動作させることができない
 *
 * Issue
 * https://github.com/JetBrains/compose-multiplatform/issues/3762
 */
open class CSSVar<T : StylePropertyValue>(
    private val name: String,
    private val fallbackValue: (() -> T)? = null,
) {

    val value get() = (fallbackValue?.let { "var($name, ${it()})" } ?: "var($name)").unsafeCast<T>()

    fun value(defaultValue: T) = ("var($name, $defaultValue)").unsafeCast<T>()

    operator fun invoke(scope: StyleScope, value: T) {
        scope.variable(variableName(scope), value)
    }

    operator fun invoke(scope: StyleScope, other: CSSVar<T>) {
        scope.variable(variableName(scope), other.value)
    }

    operator fun invoke(scope: StyleScope, other: CSSVar<T>, defaultValue: T) {
        scope.variable(variableName(scope), other.value(defaultValue))
    }

    private fun variableName(scope: StyleScope) = if (scope is CSSBuilder) name.substring(2) else name
}

val CSSVar<StylePropertyNumber>.intValue get() = value.unsafeCast<Number>().toInt()

operator fun CSSVar<StylePropertyNumber>.invoke(scope: StyleScope, value: Number) =
    invoke(scope, StylePropertyValue(value))

operator fun CSSVar<StylePropertyNumber>.invoke(
    scope: StyleScope,
    other: CSSVar<StylePropertyNumber>,
    defaultValue: Number,
) = invoke(scope, other, StylePropertyValue(defaultValue))

val CSSVar<StylePropertyString>.stringValue get() = value.unsafeCast<String>()

operator fun CSSVar<StylePropertyString>.invoke(scope: StyleScope, value: String) =
    invoke(scope, StylePropertyValue(value))

operator fun CSSVar<StylePropertyString>.invoke(
    scope: StyleScope,
    other: CSSVar<StylePropertyString>,
    defaultValue: String,
) = invoke(scope, other, StylePropertyValue(defaultValue))

fun <T : StylePropertyValue> StyleScope.setVar(cssVar: CSSVar<T>, value: T) = cssVar(this, value)
fun <T : StylePropertyValue> StyleScope.setVar(cssVar: CSSVar<T>, other: CSSVar<T>) = cssVar(this, other)
fun <T : StylePropertyValue> StyleScope.setVar(cssVar: CSSVar<T>, other: CSSVar<T>, defaultValue: T) =
    cssVar(this, other, defaultValue)

fun StyleScope.setVar(cssVar: CSSVar<StylePropertyNumber>, value: Number) = cssVar(this, value)
fun StyleScope.setVar(cssVar: CSSVar<StylePropertyNumber>, other: CSSVar<StylePropertyNumber>, defaultValue: Number) =
    cssVar(this, other, defaultValue)

fun StyleScope.setVar(cssVar: CSSVar<StylePropertyString>, value: String) = cssVar(this, value)
fun StyleScope.setVar(cssVar: CSSVar<StylePropertyString>, other: CSSVar<StylePropertyString>, defaultValue: String) =
    cssVar(this, other, defaultValue)

class ScopedCSSVar<T : StylePropertyValue>(
    private val scope: StyleScope,
    name: String,
    fallbackValue: (() -> T)? = null,
) : CSSVar<T>(name, fallbackValue) {
    operator fun invoke(value: T) = invoke(scope, value)
    operator fun invoke(other: CSSVar<T>) = invoke(scope, other)
    operator fun invoke(other: CSSVar<T>, defaultValue: T) = invoke(scope, other, defaultValue)
}

class CSSVarProperty<T : StylePropertyValue>(private val name: String) : ReadWriteProperty<StyleScope, T> {

    override fun getValue(thisRef: StyleScope, property: KProperty<*>) = "var($name)".unsafeCast<T>()

    override fun setValue(thisRef: StyleScope, property: KProperty<*>, value: T) {
        thisRef.variable(variableName(thisRef), value)
    }

    private fun variableName(scope: StyleScope) = if (scope is CSSBuilder) name.substring(2) else name
}