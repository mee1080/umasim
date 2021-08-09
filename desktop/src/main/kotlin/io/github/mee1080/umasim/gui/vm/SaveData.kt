package io.github.mee1080.umasim.gui.vm

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.util.SaveDataConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class SaveData(
    val chara: String,
    val support: String,
    val simulationTurn: Int,
    val simulationCount: Int,
    val simulationThread: Int,
    val simulationOption: FactorBasedActionSelector.Option,
    val version: Int = 1,
) {

    companion object {
        private const val SAVE_FILE = "settings.json"

        fun save(model: ViewModel) {
            try {
                File(SAVE_FILE).writeText(SaveData(model).toJson(), Charsets.UTF_8)
            } catch (_: Exception) {
                // ignore
            }
        }

        fun load(model: ViewModel) {
            try {
                fromJson(File(SAVE_FILE).readText(Charsets.UTF_8)).applyTo(model)
            } catch (_: Exception) {
                // ignore
            }
        }

        fun fromJson(json: String) = Json.decodeFromString<SaveData>(json)
    }

    fun toJson() = Json.encodeToString(this)

    constructor(model: ViewModel) : this(
        SaveDataConverter.charaToString(model.selectedChara),
        SaveDataConverter.supportCardListToString(model.selectedSupportList),
        model.simulationTurn,
        model.simulationCount,
        model.simulationThread,
        model.simulationSetting.option,
    )

    fun applyTo(model: ViewModel) {
        model.selectChara(SaveDataConverter.stringToChara(chara))
        SaveDataConverter.stringToSupportCardList(support).forEachIndexed { index, supportCard ->
            model.selectedSupportList[index] = supportCard
        }
        model.updateSimulationTurn(simulationTurn)
        model.updateSimulationCount(simulationCount)
        model.updateSimulationThread(simulationThread)
        model.simulationSetting.updateOption(simulationOption)
    }
}