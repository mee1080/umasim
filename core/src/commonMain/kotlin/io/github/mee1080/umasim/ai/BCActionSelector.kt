package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.bc.BCCalculator
import io.github.mee1080.umasim.scenario.bc.BCTeamParameter
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class BCActionSelector(
    vararg val options: Option = deafultOptions.toTypedArray(),
) : BaseActionSelector3<BCActionSelector.Option, BCActionSelector.Context>() {

    companion object {
        const val DEBUG = false
        const val FORCE = 10000000.0

        val deafultOptions = buildList {
            val base = Option(
                status = 100,
                wisdom = 90,
                skillPt = 1000,
                hp = 600,
                motivation = 1000,
                relation = 10000,
                outingRelation = 24000,
                wisdomRelation = 16000,
                hpKeep = 1000,
                risk = 350,
                dreamGauge = listOf(26000, 25000, 24000),
                dreamGaugeMax = 0,
            )
            add(base)
            add(
                base.copy(
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 700,
                    risk = 300,
                    dreamGauge = listOf(18000, 17000, 16000),
                    dreamGaugeMax = 0,
                )
            )
            add(
                base.copy(
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 1200,
                    risk = 350,
                    dreamGauge = listOf(10000, 9000, 8000),
                    dreamGaugeMax = 0,
                )
            )
            add(
                base.copy(
                    wisdom = 100,
                    hp = 600,
                    hpKeep = 1100,
                    risk = 400,
                    dreamGauge = listOf(12000, 11000, 10000),
                    dreamGaugeMax = 0,
                )
            )
        }
    }

    @Serializable
    data class Option(
        val status: Int = 100,
        override val wisdom: Int = 80,
        override val skillPt: Int = 1000,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        val wisdomRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,
        val dreamGauge: List<Int> = listOf(10000, 9000, 8000),
        val dreamGaugeMax: Int = -5000,
        val dreamTrainingTarget: List<StatusType> = listOf(
            StatusType.WISDOM, StatusType.WISDOM,
            StatusType.SPEED, StatusType.SPEED,
            StatusType.SPEED, StatusType.SPEED,
            StatusType.POWER, StatusType.POWER,
            StatusType.STAMINA, StatusType.STAMINA,
            StatusType.STAMINA, StatusType.STAMINA, StatusType.STAMINA, StatusType.STAMINA,
        ),
        val teamParameter: List<Pair<BCTeamParameter, Int>> = listOf(
            BCTeamParameter.Mental to 3,
            BCTeamParameter.Physical to 3,
            BCTeamParameter.Mental to 5,
            BCTeamParameter.Physical to 4,
            BCTeamParameter.Technique to 5,
            BCTeamParameter.Mental to 8,
            BCTeamParameter.Physical to 8,
            BCTeamParameter.Technique to 8,
        ),
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val speed get() = status
        override val stamina get() = status
        override val power get() = status
        override val guts get() = status
        override val maxSleep get() = 0

        override fun generateSelector() = BCActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(
        option: Option,
        state: SimulationState,
    ) : BaseContext<Option>(option, state) {
        val memberRank = state.bcStatus?.teamMember?.sortedBy {
            it.memberRank * 100 + it.dreamGauge
        }?.mapIndexed { index, member -> member.charaName to index }?.toMap()

        val dreamGaugeMaxCount = state.bcStatus?.teamMember?.count { it.dreamGauge >= 3 } ?: 0

        val nextTeamParameter by lazy {
            val current = state.bcStatus?.teamParameter ?: return@lazy BCTeamParameter.Mental
            option.teamParameter.first {
                current[it.first]!! < it.second
            }.first
        }

        val nextDreamTraining = if (state.bcStatus?.dreamsTrainingActive != true) null else {
            val turn = state.turn
            val bcStatus = state.bcStatus!!
            val index = if (turn >= 61) {
                13 - bcStatus.dreamsTrainingCount
            } else {
                2 * ((turn - 1) / 12) + 1 - bcStatus.dreamsTrainingCount
            }
            if (DEBUG) println("nextDreamTraining turn=$turn dreamsTrainingCount=${bcStatus.dreamsTrainingCount} index=$index target=${option.dreamTrainingTarget[index]}")
            option.dreamTrainingTarget[index]
        }
    }

    override fun getContext(state: SimulationState): Context {
        val option = when {
            state.turn <= 12 -> options[0]
            state.turn <= 24 -> options.getOrElse(1) { options[0] }
            state.turn <= 48 -> options.getOrElse(2) { options[0] }
            else -> options.getOrElse(3) { options[0] }
        }
        return Context(option, state)
    }

    override fun getRelationFactor(
        option: Option,
        target: MemberState
    ): Int {
        return if (target.card.type == StatusType.WISDOM) option.wisdomRelation else {
            super.getRelationFactor(option, target)
        }
    }

    override fun calcScenarioScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam?
    ): Double {
        val param = scenarioActionParam as? BCActionParam ?: return 0.0
        val memberRank = context.memberRank ?: return 0.0
        val option = context.option
        return param.member.sumOf {
            option.dreamGauge[memberRank[it.charaName]!!] + (if (it.dreamGauge >= 3) option.dreamGaugeMax else 0)
        }.toDouble()
    }

    override fun selectFromScore(context: Context): Pair<Action, Double> {
        val state = context.state
        val bcStatus = state.bcStatus ?: return super.selectFromScore(context)
        val selected = context.selectionWithScore.maxBy { it.second }
        val selectedAction = selected.first
        val friend = context.selectionWithScore.firstOrNull { (it.first as? Outing)?.support != null }?.first
        val dream = context.selectionWithScore.firstOrNull { it.first is BCDreamsTraining }?.first
        val restGauge = bcStatus.teamMember.sumOf { 3 - it.dreamGauge }
        if (DEBUG) {
            println("selectFromScore")
            println("selected: ${selected.second} ${selected.first.toShortString()}")
            println("friend: ${friend?.toShortString()}")
            println("dream: ${dream?.toShortString()}")
        }

        if (dream != null) {
            // 夢トレ回数が溢れるなら夢トレ
            if (DEBUG) println("turn=${state.turn} dreamsTrainingCount=${bcStatus.dreamsTrainingCount}")
            if (state.turn >= 69 - bcStatus.dreamsTrainingCount) return dream to FORCE

            val phaseLastTurn = ((state.turn - 1) / 12 + 1) * 12
            val restTurn = phaseLastTurn - state.turn + 1 - state.goalRace.count {
                it.turn in state.turn..phaseLastTurn
            }
            if (DEBUG) println("phaseLastTurn=$phaseLastTurn restTurn=$restTurn")
            if (restTurn <= bcStatus.dreamsTrainingCount) return dream to FORCE

            // レース選択するほどの下振れの場合は夢トレ
            if (DEBUG) println("isRace=${selectedAction is Race}")
            if (selectedAction is Race) return dream to FORCE

            if (selectedAction is Training) {
                val totalMemberCount = selectedAction.support.size + bcStatus.teamMember.count {
                    it.position == selectedAction.type
                }
                if (DEBUG) println("totalMemberCount: $totalMemberCount dreamGaugeMaxCount=${context.dreamGaugeMaxCount}")

                if (state.turn <= 12) {
                    // ジュニア前半
                    val canFriend = state.support.any { !it.outingType && it.friendTrainingEnabled }
                    if (DEBUG) println("canFriend: $canFriend")

                    // 友情可能で3人未満の非友情の場合は夢トレ
                    if (canFriend && !selectedAction.friendTraining && totalMemberCount < 3) return dream to FORCE

                    // 友情可能で2人以上ゲージMAXの場合は夢トレ
                    if (canFriend && context.dreamGaugeMaxCount >= 2) return dream to FORCE

                } else if (state.turn <= 24) {
                    // ジュニア後半、W友情可能判定
                    val hasDoubleFriend = state.support.filter { it.friendTrainingEnabled }
                        .groupBy { it.card.type }.any { it.value.count() >= 2 }
                    if (hasDoubleFriend) {
                        // 非友情で5人未満の場合は夢トレ
                        if (!selectedAction.friendTraining && totalMemberCount < 5) return dream to FORCE

                        // 友情で3人未満の場合は夢トレ
                        if (totalMemberCount < 3) return dream to FORCE
                    }

                } else {
                    // クラシック以降
                    // ゲージMAX1人以下、非友情で5人未満の場合は夢トレ
                    if (context.dreamGaugeMaxCount <= 1 && !selectedAction.friendTraining && totalMemberCount < 5) return dream to FORCE

                    // ゲージMAX1人以下、友情で3人未満の場合は夢トレ
                    if (context.dreamGaugeMaxCount <= 1 && totalMemberCount < 3) return dream to FORCE
                }
            }

            if (state.turn >= 25 && selectedAction is Sleep && restGauge <= 2) {
                // クラシック以降お休みでお出かけ分のDREAMSゲージが空いていない場合は夢トレ
                return dream to FORCE
            }
        }
        if (friend != null && restGauge >= 2) {
            // お休みで友人お出かけが可能な場合は友人お出かけ
            if (selectedAction is Sleep) return friend to FORCE

            // 体力が減っていてレース選択するほどの下振れの場合は友人お出かけ
            if (state.status.hp <= 70 && selectedAction is Race) return friend to FORCE

            // 体力が減っていてランクが最も低いキャラのゲージが2以上上がるならお出かけ
            if (restGauge >= 3) {
//                val outingStep = state.member.first { it.outingType }.supportState?.outingStep ?: Int.MAX_VALUE
//                val requiredStep = when {
//                    state.turn >= 40 -> 2
//                    state.turn >= 30 -> 2
//                    state.turn >= 20 -> 2
//                    else -> 2
//                }
                if (state.status.hp <= 50) {
                    val memberRank = context.memberRank
                    if (memberRank != null) {
                        val nextBcStatus = BCCalculator.addLowestDreamGauge(bcStatus)
                        val targetName = context.memberRank.entries.first { it.value == 0 }.key
                        val currentGauge = bcStatus.teamMember.first { it.charaName == targetName }.dreamGauge
                        val nextGauge = nextBcStatus.teamMember.first { it.charaName == targetName }.dreamGauge
                        if (nextGauge - currentGauge >= 2) return friend to FORCE
                    }
                }
            }
        }
        if (DEBUG) println("return selected")
        return selected
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        // レースは基本的に選ばない
        if (action is Race) return 1.0
        if (action is BCTeamParameterUp) {
            return if (action.parameter == context.nextTeamParameter) FORCE else 0.0
        }
        if (context.nextDreamTraining != null) {
            return if ((action as? Training)?.type == context.nextDreamTraining) FORCE else 0.0
        }
        return null
    }
}
