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
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel

@Composable
fun ScenarioSelect(model: ViewModel, state: State) {
    HideBlock(
        header = "育成シナリオ",
        headerClosed = "育成シナリオ: ${state.scenario.displayName}"
    ) {
        MdRadioGroup(
            WebConstants.scenarioList,
            state.scenario,
            onSelect = model::updateScenario,
            itemToLabel = { it.displayName },
        )
    }
}