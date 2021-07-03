/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.web.style

import org.jetbrains.compose.web.css.*

object AppStyle : StyleSheet() {
    val table by style {
        property("border-collapse", "collapse")
    }
    val tableHeader by style {
        border(1.px, LineStyle.Solid, Color("black"))
        width(80.px)
    }
    val tableValue by style {
        property("text-align", "right")
        border(1.px, LineStyle.Solid, Color("black"))
        width(80.px)
    }
}