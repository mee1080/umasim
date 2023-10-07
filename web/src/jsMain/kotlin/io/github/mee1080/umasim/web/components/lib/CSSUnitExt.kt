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

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnitRel
import org.jetbrains.compose.web.css.CSSUnitValueTyped

@Suppress("ClassName")
interface CSSUnitExt {

    interface dvw : CSSUnitRel
    interface dvh : CSSUnitRel

    companion object {
        inline val dvw get() = "dvw".unsafeCast<dvw>()
        inline val dvh get() = "dvh".unsafeCast<dvh>()
    }
}

val Number.dvw get(): CSSSizeValue<CSSUnitExt.dvw> = CSSUnitValueTyped(this.toFloat(), CSSUnitExt.dvw)
val Number.dvh get(): CSSSizeValue<CSSUnitExt.dvh> = CSSUnitValueTyped(this.toFloat(), CSSUnitExt.dvh)
