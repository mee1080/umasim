/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.web.page.share

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusValues
import io.github.mee1080.umasim.data.motivationToString
import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.lib.gridTemplateColumns
import io.github.mee1080.umasim.web.components.lib.repeatGrid
import io.github.mee1080.umasim.web.style.AppStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

@Composable
fun StatusTable(
    status: StatusValues,
    summary: Boolean = false,
    attrs: AttrBuilderContext<HTMLElement>? = null,
) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexWrap(FlexWrap.Wrap)
        }
        attrs?.invoke(this)
    }) {
        Div({
            classes(AppStyle.statusTable)
            style {
                gridTemplateColumns(repeatGrid(5, 64.px))
            }
        }) {
            Div({ style { textAlign("center") } }) { Text("スピ") }
            Div({ style { textAlign("center") } }) { Text("スタ") }
            Div({ style { textAlign("center") } }) { Text("パワ") }
            Div({ style { textAlign("center") } }) { Text("根性") }
            Div({ style { textAlign("center") } }) { Text("賢さ") }

            StatusValue(status.speed, summary)
            StatusValue(status.stamina, summary)
            StatusValue(status.power, summary)
            StatusValue(status.guts, summary)
            StatusValue(status.wisdom, summary)
        }
        Div({
            classes(AppStyle.statusTable)
            style {
                gridTemplateColumns(repeatGrid(3, 64.px))
            }
        }) {
            Div({ style { textAlign("center") } }) { Text("SP") }
            Div({ style { textAlign("center") } }) { Text("体力") }
            Div({ style { textAlign("center") } }) { Text("やる気") }

            Div({ style { textAlign("right") } }) { Text(status.skillPt.toString()) }
            Div({ style { textAlign("right") } }) { Text("${status.hp}") }
            if (summary) {
                Div({ style { textAlign("right") } }) { Text(motivationToString(status.motivation.toInt())) }
            } else {
                Div({ style { textAlign("right") } }) { Text(status.motivation.toString()) }
            }
        }
    }
    if (summary && status is Status && status.skillHint.isNotEmpty()) {
        Div({
            style {
                display(DisplayStyle.Flex)
                flexWrap(FlexWrap.Wrap)
                border(1.px, LineStyle.Solid, MdSysColor.outlineVariant.value)
            }
        }) {
            status.skillHint.forEach {
                Div({ style { marginRight(4.px) } }) { Text("${it.key}Lv${it.value},") }
            }
        }
    }
}

@Composable
private fun StatusValue(value: Number, summary: Boolean) {
    if (summary) {
        Div(({ style { textAlign("center") } })) {
            val intValue = value.toInt().let {
                if (it > 1200) (it - 1200) / 2 + 1200 else it
            }
            val params = when {
                intValue > 1200 -> {
                    val rank = when {
                        intValue >= 1800 -> "A"
                        intValue >= 1700 -> "B"
                        intValue >= 1600 -> "C"
                        intValue >= 1500 -> "D"
                        intValue >= 1400 -> "E"
                        intValue >= 1300 -> "F"
                        else -> "G"
                    }
                    "U$rank" to Color.white
                }

                intValue >= 1150 -> "SS+" to Color.yellow
                intValue >= 1100 -> "SS" to Color.yellow
                intValue >= 1050 -> "S+" to Color.yellow
                intValue >= 1000 -> "S" to Color.yellow
                intValue >= 900 -> "A+" to Color.orangered
                intValue >= 800 -> "A" to Color.orangered
                intValue >= 700 -> "B+" to Color.deeppink
                intValue >= 600 -> "B" to Color.deeppink
                intValue >= 500 -> "C+" to Color.green
                intValue >= 400 -> "C" to Color.green
                intValue >= 350 -> "D+" to Color.deepskyblue
                intValue >= 300 -> "D" to Color.deepskyblue
                intValue >= 250 -> "E+" to Color.purple
                intValue >= 200 -> "E" to Color.purple
                intValue >= 150 -> "F+" to Color.indigo
                intValue >= 100 -> "F" to Color.indigo
                intValue >= 50 -> "G+" to Color.gray
                else -> "G" to Color.gray
            }
            Div({
                style {
                    color(params.second)
                    textAlign("center")
                    if (intValue >= 1000) {
                        backgroundColor(Color.black)
                    } else {
                        backgroundColor(Color.white)
                    }
                }
            }) {
                Text(params.first)
            }
            Div { Text(intValue.toString()) }
        }
    } else {
        Div({ style { textAlign("right") } }) {
            Text(value.toString())
        }
    }
}