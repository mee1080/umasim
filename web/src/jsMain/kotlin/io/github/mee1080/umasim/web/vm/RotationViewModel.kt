package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.data.RaceDistance
import io.github.mee1080.umasim.data.RaceGround
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import io.github.mee1080.umasim.web.state.*
import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RotationViewModel(private val root: ViewModel) {

    companion object {
        private const val KEY_OPTION = "umasim.rotation.option"
        private const val KEY_ROTATION = "umasim.rotation.rotation"
    }

    lateinit var calculator: RaceRotationCalculator

    private val rotationState get() = root.state.rotationState!!

    fun updateGroundSetting(target: RaceGround, rank: String) {
        val newSetting = rotationState.groundSetting.toMutableMap().also {
            it[target] = RaceRotationCalculator.getRank(rank)
        }
        if (newSetting != rotationState.groundSetting) {
            init(newSetting, rotationState.distanceSetting)
        }
    }

    fun updateDistanceSetting(target: RaceDistance, rank: String) {
        val newSetting = rotationState.distanceSetting.toMutableMap().also {
            it[target] = RaceRotationCalculator.getRank(rank)
        }
        if (newSetting != rotationState.distanceSetting) {
            init(rotationState.groundSetting, newSetting)
        }
    }

    fun updateOption(option: RaceRotationCalculator.Option) {
        calculator.option = option
        calculator.calculate()
        localStorage.setItem(KEY_OPTION, Json.encodeToString(option))
        root.state = root.state.copy(
            rotationState = rotationState.copy(
                calcState = calculator.state,
                option = option,
            )
        )
    }

    fun resetRace() {
        init(rotationState.groundSetting, rotationState.distanceSetting)
    }

    fun init() {
        val groundSetting = mapOf(
            RaceGround.TURF to RaceRotationCalculator.Rank.A,
            RaceGround.DIRT to RaceRotationCalculator.Rank.NONE,
        )
        val distanceSetting = mapOf(
            RaceDistance.SHORT to RaceRotationCalculator.Rank.NONE,
            RaceDistance.MILE to RaceRotationCalculator.Rank.B,
            RaceDistance.MIDDLE to RaceRotationCalculator.Rank.A,
            RaceDistance.LONG to RaceRotationCalculator.Rank.A,
        )
        val option = localStorage.getItem(KEY_OPTION)?.let {
            Json.decodeFromString<RaceRotationCalculator.Option>(it)
        } ?: RaceRotationCalculator.Option()
        init(groundSetting, distanceSetting, option)
        val rotationLoadList = loadRotationData().keys.sorted()
        root.state = root.state.copy(
            rotationState = rotationState.copy(
                rotationLoadList = rotationLoadList,
                rotationLoadName = rotationLoadList.firstOrNull() ?: "",
            )
        )
    }

    private fun init(
        groundSetting: Map<RaceGround, RaceRotationCalculator.Rank>,
        distanceSetting: Map<RaceDistance, RaceRotationCalculator.Rank>,
        option: RaceRotationCalculator.Option = rotationState.option,
        rotation: List<String?> = emptyList(),
    ) {
        calculator = RaceRotationCalculator(groundSetting, distanceSetting, option, rotation)
        root.state = root.state.copy(
            rotationState = root.state.rotationState?.copy(
                calculator.state,
                calculator.raceSelections,
                calculator.achievements,
                groundSetting,
                distanceSetting,
                option,
            ) ?: RotationState(
                calculator.state,
                calculator.raceSelections,
                calculator.achievements,
                groundSetting,
                distanceSetting,
                option,
            )
        )
    }

    fun selectRace(turn: Int, name: String) {
        calculator.add(turn, name)
        root.state = root.state.copy(
            rotationState = rotationState.copy(
                calcState = calculator.state,
                raceSelection = calculator.raceSelections,
                achievementList = calculator.achievements,
                recommendFilter = if (rotationState.recommendFilter is TurnJustFilter) NoFilter else rotationState.recommendFilter
            )
        )
    }

    fun updateRotationSaveName(name: String) {
        root.state = root.state.copy(
            rotationState = rotationState.copy(rotationSaveName = name)
        )
    }

    fun updateRotationLoadName(name: String) {
        root.state = root.state.copy(
            rotationState = rotationState.copy(rotationLoadName = name)
        )
    }

    fun saveRotation() {
        val name = rotationState.rotationSaveName
        val data = loadRotationData().toMutableMap().apply {
            put(
                name, SavedRotation(
                    rotationState.groundSetting,
                    rotationState.distanceSetting,
                    rotationState.selectedRace.map { it?.name },
                )
            )
        }
        localStorage.setItem(KEY_ROTATION, Json.encodeToString(data))
        if (!rotationState.rotationLoadList.contains(name)) {
            root.state = root.state.copy(
                rotationState = rotationState.copy(
                    rotationLoadList = (rotationState.rotationLoadList + name).sorted(),
                    rotationLoadName = name,
                )
            )
        }
    }

    fun loadRotation() {
        val rotation = loadRotationData()[rotationState.rotationLoadName] ?: return
        init(rotation.groundSetting, rotation.distanceSetting, rotationState.option, rotation.rotation)
    }

    private fun loadRotationData(): Map<String, SavedRotation> {
        val data = localStorage.getItem(KEY_ROTATION) ?: return emptyMap()
        return Json.decodeFromString(data)
    }

    fun updateRecommendFilter(filter: RecommendFilter) {
        root.state = root.state.copy(rotationState = rotationState.copy(recommendFilter = filter))
    }
}