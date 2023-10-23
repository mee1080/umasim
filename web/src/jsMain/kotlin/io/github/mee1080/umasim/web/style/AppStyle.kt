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

import io.github.mee1080.umasim.web.components.atoms.MdSysColor
import io.github.mee1080.umasim.web.components.lib.GridRepeatStyle
import io.github.mee1080.umasim.web.components.lib.gridTemplateColumns
import io.github.mee1080.umasim.web.components.lib.minMax
import io.github.mee1080.umasim.web.components.lib.repeatGrid
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.Nth

object AppStyle : StyleSheet() {
    val table by style {
        property("border-collapse", "collapse")

        self + " th" style {
            border(1.px, LineStyle.Solid, Color("black"))
            width(80.px)
        }

        self + " td" style {
            textAlign("right")
            border(1.px, LineStyle.Solid, Color("black"))
            width(80.px)
        }
    }

    val supportCardArea by style {
        display(DisplayStyle.Grid)
        gridTemplateColumns(repeatGrid(GridRepeatStyle.AutoFit, minMax(360.px, 1.fr)))
        maxWidth(1440.px)
        gap(16.px)
        padding(16.px)
    }

    val supportCard by style {
        self + " select" style {
            maxWidth(100.percent)
        }
    }

    val friendSupportCard by style {
        self + " .after" style {
            opacity(0.3)
        }
    }

    val supportCardTable by style {
        property("border-collapse", "collapse")
        "th" style {
            border(1.px, LineStyle.Solid, Color("black"))
            property("writing-mode", "vertical-rl")
            textAlign("right")
        }
        "td" style {
            textAlign("right")
            border(1.px, LineStyle.Solid, Color("black"))
        }
    }

    val statusTable by style {
        display(DisplayStyle.Grid)
        gap(1.px)
        self + " > div" style {
            border(1.px, LineStyle.Solid, MdSysColor.outlineVariant.value)
        }
    }
}