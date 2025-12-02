package io.github.mee1080.umasim.web.page.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.onsen.OnsenCalculator
import io.github.mee1080.umasim.scenario.onsen.OnsenStatus
import io.github.mee1080.umasim.scenario.onsen.StratumType
import io.github.mee1080.umasim.scenario.onsen.gensenData
import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.min

val linkCharaNames = Scenario.ONSEN.scenarioLink.filter { it != "保科健子" }

val charaNames = Store.charaList.map { it.charaName }.distinct().sorted()

val scheduleLabels = listOf("ジュニア", "クラシック", "シニア")

val statusRankList = listOf(
    "G" to 0,
    "F" to 100,
    "E" to 200,
    "D" to 300,
    "C" to 400,
    "B-A" to 600,
    "S-SS" to 1000,
    "UG-" to 1200,
)

val statusRanks = statusRankList.map { it.first }

val statusRankData = statusRankList.toMap()

val gensenNames = List(3) { stage ->
    gensenData.values.filter { it.turn < (stage + 1) * 24 }.map { it.name }
}

enum class OnsenDigTurnAction(
    val label: String,
    val dig: Int,
    val hp: Int,
    val hasMember: Boolean = false,
    val levelUp: Boolean = false,
) {
    Speed("スピード", 25, 20, hasMember = true, levelUp = true),
    Training("スタパワ根性", 25, 22, hasMember = true, levelUp = true),
    Wisdom("賢さ", 25, 0, hasMember = true, levelUp = true),
    Race("レース", 15, 15),
    Outing("お出かけ", 25, 0),
    PR("PR活動", 10, 20, hasMember = true),
    Goal("目標レース", 25, 0),
}

val selectableTurnActions = OnsenDigTurnAction.entries.dropLast(1)

data class OnsenDigState(
    val initial: OnsenDigInitialState = OnsenDigInitialState(),
    val schedules: List<OnsenDigSchedule> = listOf(
        OnsenDigSchedule.junior, OnsenDigSchedule.classic, OnsenDigSchedule.senior,
    ),
    val turns: List<OnsenDigTurn> = List(76) { OnsenDigTurn() },
) {
    @Serializable
    class SaveData(
        val i: OnsenDigInitialState.SaveData,
        val s: List<OnsenDigSchedule.SaveData>,
        val t: List<Int>,
    )

    val saveData get() = SaveData(initial.saveData, schedules.map { it.saveData }, turns.map { it.saveData })

    constructor(saveData: SaveData) : this(
        initial = OnsenDigInitialState(saveData.i),
        schedules = saveData.s.map { OnsenDigSchedule(it) },
        turns = saveData.t.map { OnsenDigTurn(it) },
    )
}

data class OnsenDigInitialState(
    val chara: String = "ハルウララ",
    val link: Set<String> = emptySet(),
    val factor: List<StatusType> = List(4) { StatusType.SPEED } + List(2) { StatusType.POWER },
    val superRecoveryHp: Int = 300,
) {
    @Serializable
    class SaveData(
        val c: String,
        val l: List<Int>,
        val f: List<Int>,
        val h: Int,
    )

    val saveData
        get() = SaveData(
            chara,
            link.map { linkCharaNames.indexOf(it) },
            factor.map { it.ordinal },
            superRecoveryHp,
        )

    constructor(saveData: SaveData) : this(
        chara = saveData.c,
        link = saveData.l.map { linkCharaNames[it] }.toSet(),
        factor = saveData.f.map { StatusType.entries[it] },
        superRecoveryHp = saveData.h,
    )
}

data class OnsenDigSchedule(
    val label: String,
    val firstHalfStatusRank: List<String>,
    val firstHalfTrainingLevel: Int,
    val laterHalfStatusRank: List<String>,
    val laterHalfTrainingLevel: Int,
    val gensenList: List<OnsenDigGensen>,
) {
    @Serializable
    class SaveData(
        val l: Int,
        val fs: List<Int>,
        val ft: Int,
        val ls: List<Int>,
        val lt: Int,
        val g: List<Int>,
    )

    val saveData
        get() = SaveData(
            scheduleLabels.indexOf(label),
            firstHalfStatusRank.map { statusRanks.indexOf(it) },
            firstHalfTrainingLevel,
            laterHalfStatusRank.map { statusRanks.indexOf(it) },
            laterHalfTrainingLevel,
            gensenList.map { it.saveData },
        )

    constructor(saveData: SaveData) : this(
        label = scheduleLabels[saveData.l],
        firstHalfStatusRank = saveData.fs.map { statusRanks[it] },
        firstHalfTrainingLevel = saveData.ft,
        laterHalfStatusRank = saveData.ls.map { statusRanks[it] },
        laterHalfTrainingLevel = saveData.lt,
        gensenList = saveData.g.map { OnsenDigGensen(it) },
    )

    companion object {

        val junior = OnsenDigSchedule(
            scheduleLabels[0],
            listOf("E", "F", "F", "F", "F"),
            1,
            listOf("D", "F", "D", "D", "E"),
            1,
            listOf(
                OnsenDigGensen("疾駆の湯", StratumType.SAND),
                OnsenDigGensen("明晰の湯", StratumType.ROCK),
                OnsenDigGensen("堅忍の湯", StratumType.SOIL),
            ),
        )

        val classic = OnsenDigSchedule(
            scheduleLabels[1],
            listOf("B-A", "E", "C", "D", "D"),
            2,
            listOf("S-SS", "D", "B-A", "C", "C"),
            3,
            listOf(
                OnsenDigGensen("天翔の古湯", StratumType.SOIL),
                OnsenDigGensen("駿閃の古湯", StratumType.SAND),
                OnsenDigGensen("剛脚の古湯", StratumType.SAND),
            )
        )
        val senior = OnsenDigSchedule(
            scheduleLabels[2],
            listOf("UG-", "C", "UG-", "B-A", "B-A"),
            4,
            listOf("UG-", "B-A", "UG-", "S-SS", "S-SS"),
            5,
            listOf(
                OnsenDigGensen("秘湯ゆこま", StratumType.ROCK),
                OnsenDigGensen("剛脚の古湯", StratumType.SAND),
                OnsenDigGensen("堅忍の湯", StratumType.SOIL),
                OnsenDigGensen("伝説の秘湯", StratumType.SOIL),
                OnsenDigGensen("堅忍の湯", StratumType.SOIL),
                OnsenDigGensen("健壮の古湯", StratumType.ROCK),
            )
        )
    }

    val firstHalfStatus: Status
        get() {
            val statusValues = firstHalfStatusRank.map { statusRankData[it]!! }
            return Status(statusValues[0], statusValues[1], statusValues[2], statusValues[3], statusValues[4])
        }

    val laterHalfStatus: Status
        get() {
            val statusValues = laterHalfStatusRank.map { statusRankData[it]!! }
            return Status(statusValues[0], statusValues[1], statusValues[2], statusValues[3], statusValues[4])
        }
}

data class OnsenDigGensen(
    val name: String = "疾駆の湯",
    val equipment: StratumType = StratumType.SAND,
) {
    val saveData get() = gensenNames[2].indexOf(name) * 10 + equipment.ordinal

    constructor(saveData: Int) : this(
        name = gensenNames[2][saveData / 10],
        equipment = StratumType.entries[saveData % 10],
    )
}

data class OnsenDigTurn(
    val action: OnsenDigTurnAction = OnsenDigTurnAction.Training,
    val memberCount: Int = 2,
    val bathing: Boolean = false,
    val superRecoveryTriggered: Boolean = false,

    val goal: Boolean = false,
    val superRecoveryAvailable: Boolean = false,
    val superRecoveryBathing: Boolean = false,
    val gensen: String? = null,
    val gensenRest: Int = 0,
    val digFirstTurn: Boolean = false,
    val ticket: Int = 0,
    val ticketChange: Int = 0,
    val usedHp: Int = 0,
) {
    val saveData
        get() = action.ordinal * 1000 +
                memberCount * 100 +
                (if (bathing) 10 else 0) +
                if (superRecoveryTriggered) 1 else 0

    constructor(saveData: Int) : this(
        action = OnsenDigTurnAction.entries[saveData / 1000],
        memberCount = (saveData / 100) % 10,
        bathing = ((saveData / 10) % 10) > 0,
        superRecoveryTriggered = (saveData % 10) > 0,
    )

    val displayAction = if (goal) OnsenDigTurnAction.Goal else action
}

fun OnsenDigState.calc(): OnsenDigState {
    val support = initial.link.map { Store.guestSupportCardMap[it]!! }
    val factor = initial.factor.map { it to 3 }
    val chara = Store.getChara(initial.chara, 5, 5)
    val goalTurns = Store.getGoalRaceList(chara.charaId).map { it.turn }.toSet()
    var schedule = schedules[0]
    var gensenList = schedule.gensenList.toMutableList()
    var status = schedule.firstHalfStatus
    var trainingHp = (schedule.firstHalfTrainingLevel - 1) * 5
    var onsenStatus = OnsenStatus(support, factor)
    onsenStatus = selectGensen(onsenStatus, gensenList)
    var digFirstTurn = true
    var ticket = 2
    var usedHp = 0
    var outingCount = 0
    var nextSuperRecoveryAvailable = false
    val newTurns = turns.mapIndexed { index, data ->
        val turn = index + 3
        when (turn) {
            // ジュニア後半開始
            13 -> {
                status = schedule.laterHalfStatus
                trainingHp = (schedule.laterHalfTrainingLevel - 1) * 5
            }

            // クラシック/シニア後半開始（夏合宿）
            37, 61 -> {
                status = schedule.laterHalfStatus
                trainingHp = (schedule.laterHalfTrainingLevel - 1) * 5
                ticket = min(3, ticket + 2)
            }

            // クラシック/シニア前半開始
            25, 49 -> {
                schedule = schedules[turn / 24]
                gensenList = schedule.gensenList.toMutableList()
                status = schedule.firstHalfStatus
                trainingHp = (schedule.firstHalfTrainingLevel - 1) * 5
                onsenStatus = selectGensen(onsenStatus, gensenList)
                digFirstTurn = true
            }

            // 伝説の秘湯掘削開始
            66 -> {
                onsenStatus = selectGensen(onsenStatus, gensenList)
                digFirstTurn = true
            }

            // ファイナルズ期間開始
            73 -> {
                ticket = min(3, ticket + 2)
            }
        }
        val skipDig = turn >= 73 || Scenario.ONSEN.levelUpTurns.contains(turn)
        val currentTicket = ticket
        val alwaysSuperRecovery = onsenStatus.ryokanBonus.superRecoveryGuaranteed
        var currentSuperRecoveryAvailable = nextSuperRecoveryAvailable || alwaysSuperRecovery
        var superRecoveryBathing = false
        if (data.bathing) {
            ticket -= 1
            if (currentSuperRecoveryAvailable) {
                superRecoveryBathing = true
                currentSuperRecoveryAvailable = false
                nextSuperRecoveryAvailable = false
                usedHp = 0
            }
        }
        val goal = goalTurns.contains(turn)
        val action = if (goal) OnsenDigTurnAction.Goal else data.action
        if (action == OnsenDigTurnAction.Outing) {
            outingCount += 1
            ticket = min(3, ticket + if (outingCount == 5) 2 else 1)
        } else if (action == OnsenDigTurnAction.PR) {
            ticket = min(3, ticket + 1)
        }
        if (action.hp > 0 && !currentSuperRecoveryAvailable) {
            usedHp += action.hp + (if (action.levelUp) trainingHp else 0)
            if (usedHp >= initial.superRecoveryHp) {
                currentSuperRecoveryAvailable = true
                nextSuperRecoveryAvailable = true
            } else if (data.superRecoveryTriggered) {
                nextSuperRecoveryAvailable = true
            }
        }
        if (skipDig) {
            data.copy(
                goal = goal,
                superRecoveryAvailable = currentSuperRecoveryAvailable || alwaysSuperRecovery,
                superRecoveryBathing = superRecoveryBathing,
                gensen = null,
                ticket = currentTicket,
                ticketChange = ticket - currentTicket,
                usedHp = usedHp,
            )
        } else {
            val basePoint = action.dig + if (action.hasMember) data.memberCount else 0
            val digResult = OnsenCalculator.calcDigResult(onsenStatus, status, basePoint, noLimit = true)
            onsenStatus = onsenStatus.copy(digProgress = onsenStatus.digProgress + digResult.digPoint)
            val gensen = onsenStatus.selectedGensen?.name ?: ""
            val gensenRest = (onsenStatus.selectedGensen?.totalProgress ?: 0) - onsenStatus.digProgress
            val currentDigFirstTurn = digFirstTurn
            if (onsenStatus.digFinished(0)) {
                onsenStatus = onsenStatus.copy(
                    excavatedGensen = onsenStatus.excavatedGensen + onsenStatus.selectedGensen!!,
                )
                onsenStatus = selectGensen(onsenStatus, gensenList)
                ticket = min(3, ticket + 2)
                digFirstTurn = true
            } else {
                digFirstTurn = false
            }
            if (action != OnsenDigTurnAction.Goal) {
                usedHp += digResult.digBonus.statusTotal / 5 * 3
            }
            data.copy(
                goal = goal,
                superRecoveryAvailable = currentSuperRecoveryAvailable || alwaysSuperRecovery,
                superRecoveryBathing = superRecoveryBathing,
                gensen = gensen,
                gensenRest = gensenRest,
                digFirstTurn = currentDigFirstTurn,
                ticket = currentTicket,
                ticketChange = ticket - currentTicket,
                usedHp = usedHp,
            )
        }
    }
    val result = copy(turns = newTurns)
    saveOnsenDigState(result)
    return result
}

private fun selectGensen(
    onsenStatus: OnsenStatus,
    gensenList: MutableList<OnsenDigGensen>,
): OnsenStatus {
    while (gensenList.isNotEmpty()) {
        val targetGensen = gensenList.removeFirst()
        if (onsenStatus.excavatedGensen.all { it.name != targetGensen.name } && onsenStatus.equipmentLevel[targetGensen.equipment]!! < 6) {
            val newOnsenStatus = OnsenCalculator.selectGensen(onsenStatus, gensenData[targetGensen.name]!!)
            return OnsenCalculator.selectEquipment(newOnsenStatus, targetGensen.equipment)
        }
    }
    return onsenStatus
}

private const val KEY_ONSEN_DIG_STATE = "umasim.onsenDigState"

fun loadOnsenDigState(data: String? = null): OnsenDigState {
    val loaded = (data ?: localStorage.getItem(KEY_ONSEN_DIG_STATE))?.let {
        runCatching {
            OnsenDigState(Json.decodeFromString<OnsenDigState.SaveData>(it))
        }.getOrNull()
    } ?: OnsenDigState()
    return loaded.calc()
}

fun saveOnsenDigState(state: OnsenDigState) {
    localStorage.setItem(KEY_ONSEN_DIG_STATE, Json.encodeToString(state.saveData))
}
