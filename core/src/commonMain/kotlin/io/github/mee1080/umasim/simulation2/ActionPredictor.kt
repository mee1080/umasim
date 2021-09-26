package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.data.trainingType
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

fun SimulationState.predict(): List<Action> {
    val supportPosition = member.groupBy { it.position }
    return mutableListOf(
        *(training.map {
            calcTrainingResult(it, supportPosition[it.type] ?: emptyList())
        }).toTypedArray(),
        *(if (isLevelUpTurn) arrayOf(
            Sleep(
                listOf(
                    Status(hp = 40, motivation = 1) to 1
                )
            )
        ) else arrayOf(
            Sleep(
                listOf(
                    Status(hp = 70) to 25,
                    Status(hp = 50) to 62,
                    Status(hp = 30) to 10,
                    Status(hp = 30, motivation = -1) to 3,
                )
            ),
            Outing(
                null,
                listOf(
                    Status(motivation = 2) to 10,
                    Status(hp = 10, motivation = 1) to 4,
                    Status(hp = 20, motivation = 1) to 2,
                    Status(hp = 30, motivation = 1) to 1,
                    Status(hp = 10, motivation = 1) to 8,
                )
            )
        ))
    ).map { adjustRange(it) }
}

private fun SimulationState.calcTrainingResult(
    training: TrainingState,
    support: List<MemberState>,
): Action {
    val failureRate = calcTrainingFailureRate(training.current, support)
    val successStatus = Calculator.calcTrainingSuccessStatus(chara, training.current, status.motivation, support)
    val successCandidate = calcTrainingHint(support)
        .map { successStatus + it to 100 - failureRate }
    val failureRateValue = failureRate * successCandidate.size
    val failureCandidate = when {
        failureRate == 0 -> {
            emptyList()
        }
        training.type == StatusType.WISDOM -> {
            listOf(Status(hp = successStatus.hp) to failureRateValue)
        }
        failureRate >= 30 -> {
            val target = trainingType.copyOf().apply { shuffle() }
                .slice(0..1).map { it to -10 }.toTypedArray()
            listOf(
                Status(hp = 10, motivation = -2).add(training.type to -10, *target) to failureRateValue
            )
        }
        else -> {
            listOf(Status(motivation = -1).add(training.type to -5) to failureRateValue)
        }
    }
    return Training(
        training.type,
        failureRate,
        training.currentLevel,
        support,
        successCandidate + failureCandidate
    )
}

private fun SimulationState.calcTrainingHint(support: List<MemberState>): List<Status> {
    val hintSupportList = support.filter { it.hint }
    if (hintSupportList.isEmpty()) return listOf(Status())
    val hintSupport = hintSupportList.random()
    val hintList = hintSupport.card.skills.filter { status.skillHint[it] != 5 }
        .map { Status(skillHint = mapOf(it to 1 + hintSupport.card.hintLevel)) }.toTypedArray()
    return listOf(*hintList, hintSupport.card.hintStatus)
}

private fun SimulationState.calcTrainingFailureRate(training: TrainingBase, support: List<MemberState>): Int {
    val base = (status.hp - status.maxHp) * (status.hp * 10 - training.failureRate) / 400.0
    val supported = base * support.map { it.card.failureRate }.fold(1.0) { acc, d -> acc * d }
    val supportedInRange = max(0, min(99, ceil(supported).toInt()))
    return max(0, min(100, supportedInRange + conditionFailureRate))
}

fun SimulationState.adjustRange(action: Action) = action.updateCandidate(
    action.resultCandidate.map { (status + it.first).adjustRange() - status to it.second }
)