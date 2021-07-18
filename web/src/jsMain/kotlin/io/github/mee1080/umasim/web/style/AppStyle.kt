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
import org.jetbrains.compose.web.css.selectors.plus

object AppStyle : StyleSheet() {
    val table by style {
        property("border-collapse", "collapse")

        self + " th" style {
            border(1.px, LineStyle.Solid, Color("black"))
            width(80.px)
        }

        self + " td" style {
            property("text-align", "right")
            border(1.px, LineStyle.Solid, Color("black"))
            width(80.px)
        }
    }

    val supportCardArea by style {
        display(DisplayStyle.Flex)

        self + " div" style {
            flexGrow(1)
            property("flex-basis", "30%")
        }
    }

    val supportCard by style {
        position(Position.Relative)
        self + " .after" style {
            position(Position.Absolute)
            property("z-index", "-1")
            top(0.px)
            left(0.px)
            height((100).percent)
            width((100).percent)
            opacity(0.0)
            borderRadius(50.px)
            property("background", "linear-gradient(to right, #e0c000, #ea4335)")
            property("filter", "blur(5px)")
            property("transition", "all 0.5s")
        }
    }

    val friendSupportCard by style {
        self + " .after" style {
            opacity(0.3)
        }
    }
}