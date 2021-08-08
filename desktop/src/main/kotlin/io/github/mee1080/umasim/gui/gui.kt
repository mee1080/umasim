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
package io.github.mee1080.umasim.gui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.mee1080.umasim.gui.component.FactorBasedActionSelectorSetting
import io.github.mee1080.umasim.gui.panel.DefaultPanel

fun openGui(args: Array<String>) = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1024.dp, height = 768.dp),
        title = "ウマ娘育成シミュレータ",
    ) {
        val scope = rememberCoroutineScope()
        val model by remember { mutableStateOf(ViewModel(scope)) }

        MaterialTheme {
            when (model.viewState) {
                ViewModel.ViewState.SIMULATION_SETTING -> {
                    FactorBasedActionSelectorSetting(model.simulationSetting) {
                        model.viewState = ViewModel.ViewState.DEFAULT
                    }
                }
                else -> DefaultPanel(model)
            }
        }
    }
}
