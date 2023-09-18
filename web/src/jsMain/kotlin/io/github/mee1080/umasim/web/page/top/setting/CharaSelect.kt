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
package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.material.MwcSelect
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun CharaSelect(model: ViewModel, state: State) {
    H2 { Text("育成ウマ娘") }
    MwcSelect(
        WebConstants.charaList,
        state.chara,
        {
            style {
                width(600.px)
                maxWidth(100.percent)
            }
        },
        onSelect = model::updateChara,
        itemToValue = { "${it.name} (${it.speedBonus},${it.staminaBonus},${it.powerBonus},${it.gutsBonus},${it.wisdomBonus})" },
    )
}