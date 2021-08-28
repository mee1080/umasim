package io.github.mee1080.umasim.web.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation.Calculator
import io.github.mee1080.umasim.simulation.Support

class TrainingParamTestModel {

    var speed by mutableStateOf(0)

    var stamina by mutableStateOf(0)

    var power by mutableStateOf(0)

    var guts by mutableStateOf(0)

    var wisdom by mutableStateOf(0)

    var skillPt by mutableStateOf(0)

    var result by mutableStateOf(Status())

    fun calculate(
        chara: Chara,
        type: StatusType,
        motivation: Int,
        support: List<Support>
    ) {
        result = Calculator.calcTrainingSuccessStatus(
            chara, type, motivation, support, Status(
                speed = speed,
                stamina = stamina,
                power = power,
                guts = guts,
                wisdom = wisdom,
                skillPt = skillPt,
            )
        )
    }
}