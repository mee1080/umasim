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
package io.github.mee1080.umasim.gui.vm

import io.github.mee1080.umasim.util.SaveDataConverter
import java.io.File


object SaveDataLoader {

    private const val SAVE_FILE = "settings.json"

    private const val DEBUG = false

    fun save(model: ViewModel) {
        try {
            if (DEBUG) println(createData(model))
            File(SAVE_FILE).writeText(createData(model).toJson(), Charsets.UTF_8)
        } catch (e: Exception) {
            if (DEBUG) e.printStackTrace()
        }
    }

    fun load(model: ViewModel) {
        try {
            if (DEBUG) println(SaveData.fromJson(File(SAVE_FILE).readText(Charsets.UTF_8)))
            applyData(model, SaveData.fromJson(File(SAVE_FILE).readText(Charsets.UTF_8)))
        } catch (e: Exception) {
            if (DEBUG) e.printStackTrace()
        }
    }

    private fun createData(model: ViewModel) = SaveData(
        SaveDataConverter.charaToString(model.selectedChara),
        SaveDataConverter.supportCardListToString(model.selectedSupportList),
        model.supportPresets.map { it.first to SaveDataConverter.supportCardListToString(it.second) },
        model.simulationTurn,
        model.simulationCount,
        model.simulationThread,
        model.simulationSetting.option,
    )

    private fun applyData(model: ViewModel, data: SaveData) {
        model.selectedChara = SaveDataConverter.stringToChara(data.chara)
        SaveDataConverter.stringToSupportCardList(data.support).forEachIndexed { index, supportCard ->
            model.selectedSupportList[index] = supportCard
        }
        model.supportPresets.clear()
        model.supportPresets.addAll(data.supportPresets.map { it.first to SaveDataConverter.stringToSupportCardList(it.second) })
        model.updateSimulationTurn(data.simulationTurn)
        model.updateSimulationCount(data.simulationCount)
        model.updateSimulationThread(data.simulationThread)
        model.simulationSetting.updateOption(data.simulationOption)
    }
}