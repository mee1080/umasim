package io.github.mee1080.umasim.gui.vm

import io.github.mee1080.umasim.util.SaveDataConverter
import java.io.File


object SaveDataLoader {

    private const val SAVE_FILE = "settings.json"

    fun save(model: ViewModel) {
        try {
            File(SAVE_FILE).writeText(createData(model).toJson(), Charsets.UTF_8)
        } catch (_: Exception) {
            // ignore
        }
    }

    fun load(model: ViewModel) {
        try {
            applyData(model, SaveData.fromJson(File(SAVE_FILE).readText(Charsets.UTF_8)))
        } catch (_: Exception) {
            // ignore
        }
    }

    private fun createData(model: ViewModel) = SaveData(
        SaveDataConverter.charaToString(model.selectedChara),
        SaveDataConverter.supportCardListToString(model.selectedSupportList),
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
        model.updateSimulationTurn(data.simulationTurn)
        model.updateSimulationCount(data.simulationCount)
        model.updateSimulationThread(data.simulationThread)
        model.simulationSetting.updateOption(data.simulationOption)
    }
}