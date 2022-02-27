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
package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.dom.*

@Composable
fun RaceBonus(model: ViewModel, state: State) {
    H3 { Text("レースボーナス合計：${state.totalRaceBonus}") }
    var totalStatus = 0
    var totalSkillPt = 0
    Table {
        Tr {
            Th { Text("種別") }
            Th { Text("回数") }
            Th { Text("ステ―タス") }
            Th { Text("スキルPt") }
            if (state.scenario == Scenario.CLIMAX) {
                Th { Text("使用アイテム") }
            }
            Th { Text("合計ステ") }
            Th { Text("合計スキルPt") }
        }
        state.raceSetting.forEach { race ->
            Tr {
                Th { Text(race.label) }
                if (race.editable) {
                    Td {
                        TextInput(race.raceCount) {
                            size(5)
                            onInput { model.updateRaceCount(race, it.value) }
                        }
                    }
                } else {
                    Td { Text(race.raceCount) }
                }
                Td { Text("${state.calcRaceStatus(race.statusValue)}(${race.statusValue}) × ${race.statusCount}") }
                Td { Text("${state.calcRaceStatus(race.skillPt)}(${race.skillPt})") }
                if (state.scenario == Scenario.CLIMAX) {
                    Td {
                        race.item.forEach { (label, count) ->
                            Text("$label ")
                            TextInput(count) {
                                size(5)
                                onInput { model.updateRaceItemCount(race, label, it.value) }
                            }
                        }
                    }
                }
                val status = state.calcRaceStatus(race)
                totalStatus += status.first
                totalSkillPt += status.second
                Td { Text(status.first.toString()) }
                Td { Text(status.second.toString()) }
            }
        }
    }
    H3 { Text("総合計 ステータス: $totalStatus / スキルPt: $totalSkillPt") }
}