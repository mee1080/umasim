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
package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel

@Composable
fun TrainingSetting(model: ViewModel, state: State) {
    HideBlock("トレーニング設定", true) {
        NestedHideBlock("特殊固有") {
            SliderEntry("ファン数：", state.fanCount, 0, 200000, 10000) {
                model.updateFanCount(it.toInt())
            }
            SliderEntry("体力：", state.hp, 0, 120) {
                model.updateHp(it.toInt())
            }
            SliderEntry("体力最大値：", state.maxHp, 100, 120) {
                model.updateMaxHp(it.toInt())
            }
            SliderEntry("絆合計：", state.totalRelation, 0, 600) {
                model.updateTotalRelation(it.toInt())
            }
            SliderEntry("速度スキル数：", state.speedSkillCount, 0, 5) {
                model.updateSpeedSkillCount(it.toInt())
            }
            SliderEntry("回復スキル数：", state.healSkillCount, 0, 3) {
                model.updateHealSkillCount(it.toInt())
            }
            SliderEntry("加速スキル数：", state.accelSkillCount, 0, 3) {
                model.updateAccelSkillCount(it.toInt())
            }
            SliderEntry("合計トレLv：", state.totalTrainingLevel, 5, 20) {
                model.updateTotalTrainingLevel(it.toInt())
            }
        }
        if (state.scenario == Scenario.GRAND_LIVE) {
            LiveTrainingSetting(model, state.trainingLiveState)
        }
        if (state.scenario == Scenario.GM) {
            GmTrainingSetting(model, state.gmState)
        }
        if (state.scenario == Scenario.LARC) {
            LArcTrainingSetting(model, state.lArcState)
        }
        if (state.scenario == Scenario.UAF) {
            UafTrainingSetting(model, state.uafState)
        }
        if (state.scenario == Scenario.COOK) {
            CookTrainingSetting(model, state.cookState)
        }
        if (state.scenario == Scenario.MECHA) {
            MechaTrainingSetting(model, state.mechaState)
        }
        if (state.scenario == Scenario.BC) {
            BcTrainingSetting(model, state.bcState)
        }
    }
}
