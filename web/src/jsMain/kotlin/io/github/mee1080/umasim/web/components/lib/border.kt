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


fun StyleScope.borderTop(borderBuild: CSSBorder.() -> Unit) {
    property("border-top", CSSBorder().apply(borderBuild))
}

fun StyleScope.borderTop(
    width: CSSLengthValue? = null,
    style: LineStyle? = null,
    color: CSSColorValue? = null
) {
    borderTop {
        width?.let { width(it) }
        style?.let { style(it) }
        color?.let { color(it) }
    }
}

fun StyleScope.borderBottom(borderBuild: CSSBorder.() -> Unit) {
    property("border-bottom", CSSBorder().apply(borderBuild))
}

fun StyleScope.borderBottom(
    width: CSSLengthValue? = null,
    style: LineStyle? = null,
    color: CSSColorValue? = null
) {
    borderBottom {
        width?.let { width(it) }
        style?.let { style(it) }
        color?.let { color(it) }
    }
}

fun StyleScope.borderLeft(borderBuild: CSSBorder.() -> Unit) {
    property("border-left", CSSBorder().apply(borderBuild))
}

fun StyleScope.borderLeft(
    width: CSSLengthValue? = null,
    style: LineStyle? = null,
    color: CSSColorValue? = null
) {
    borderLeft {
        width?.let { width(it) }
        style?.let { style(it) }
        color?.let { color(it) }
    }
}

fun StyleScope.borderRight(borderBuild: CSSBorder.() -> Unit) {
    property("border-right", CSSBorder().apply(borderBuild))
}

fun StyleScope.borderRight(
    width: CSSLengthValue? = null,
    style: LineStyle? = null,
    color: CSSColorValue? = null
) {
    borderRight {
        width?.let { width(it) }
        style?.let { style(it) }
        color?.let { color(it) }
    }
}
