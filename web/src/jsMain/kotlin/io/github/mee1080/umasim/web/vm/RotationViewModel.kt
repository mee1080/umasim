package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.data.RaceDistance
import io.github.mee1080.umasim.data.RaceGround
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import io.github.mee1080.umasim.web.state.RotationState

class RotationViewModel(private val root: ViewModel) {

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
        init(groundSetting, distanceSetting)
    }

    private fun init(
        groundSetting: Map<RaceGround, RaceRotationCalculator.Rank>,
        distanceSetting: Map<RaceDistance, RaceRotationCalculator.Rank>,
    ) {
        calculator = RaceRotationCalculator(groundSetting, distanceSetting)
        root.state = root.state.copy(
            rotationState = RotationState(
                calculator.state,
                calculator.raceSelections,
                calculator.achievements,
                groundSetting,
                distanceSetting,
            )
        )
    }

    fun selectRace(turn: Int, name: String) {
        calculator.add(turn, name)
        root.state = root.state.copy(
            rotationState = rotationState.copy(
                calculator.state,
                calculator.raceSelections,
                calculator.achievements,
            )
        )
    }
}