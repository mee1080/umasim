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
import io.github.mee1080.umasim.web.components.lib.CSSVar
import io.github.mee1080.umasim.web.components.lib.require
import io.github.mee1080.umasim.web.components.lib.setVar
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

private val initializer = object {
    init {
        require("@material/web/elevation/elevation.js")
    }
}

@Composable
fun MdElevation(level: Number? = null) {
    TagElement<MdElevationElement>("md-elevation", level?.let { { style { mdElevationLevel(it) } } }, null)
}

fun StyleScope.mdElevation(width: CSSNumeric, height: CSSNumeric, level: Number, shadowColor: CSSColorValue? = null) {
    position(Position.Relative)
    width(width)
    height(height)
    mdElevationLevel(level)
    shadowColor?.let { setVar(MdElevationElementVariables.shadowColor, it) }
}

fun StyleScope.mdElevationLevel(level: Number) {
    setVar(MdElevationElementVariables.elevationLevel, level)
}

abstract external class MdElevationElement : HTMLElement

object MdElevationElementVariables {
    /**
     * 0 .. 5
     */
    val elevationLevel = CSSVar<StylePropertyNumber>("--md-elevation-level")

    val shadowColor = CSSVar<CSSColorValue>("--md-elevation-shadow-color")
}