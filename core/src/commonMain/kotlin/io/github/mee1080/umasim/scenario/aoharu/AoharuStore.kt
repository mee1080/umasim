package io.github.mee1080.umasim.scenario.aoharu

import io.github.mee1080.umasim.data.*

class AoharuStore(
    teamMemberSource: String,
) {
    private val training = aoharuTrainingCharaData
        .groupBy { it.type }
        .mapValues { entry -> entry.value.associateBy { it.count } }

    fun getTraining(type: StatusType, count: Int) = training[type]!![count]

    fun getBurn(type: StatusType, isLink: Boolean) =
        training[type]!![if (isLink) AoharuTraining.COUNT_BURN_LINK else AoharuTraining.COUNT_BURN]!!

    private val trainingTeam = aoharuTrainingTeamData
        .groupBy { it.type }
        .mapValues { entry -> entry.value.associateBy { it.level } }

    fun getTrainingTeam(type: StatusType, level: Int) = trainingTeam[type]!![level]!!

    private val burnTeam = aoharuBurnTeamData
        .associateBy { it.type }

    fun getBurnTeam(type: StatusType) = burnTeam[type]!!

    private val teamMemberList by lazy { TeamMemberLoader.load(teamMemberSource) }

    fun getTeamMember(supportCardId: Int) = teamMemberList.firstOrNull { it.supportCardId == supportCardId }

    fun getGuest(name: String) = teamMemberList.firstOrNull { it.rarity == 1 && it.chara == name }

    fun getShuffledGuest() = teamMemberList.filter { it.rarity == 1 }.shuffled()

    val teamStatusRank = aoharuTeamStatusRank
        .associateBy { it.rank }
}

private val aoharuTrainingCharaData = listOf(
    AoharuTraining(toSupportType("スピード"), 1, Status(0, 0, 0, 0, 0, 0, 0)),
    AoharuTraining(toSupportType("スピード"), 2, Status(2, 0, 0, 0, 0, 0, -1)),
    AoharuTraining(toSupportType("スピード"), 3, Status(3, 0, 1, 0, 0, 1, -2)),
    AoharuTraining(toSupportType("スピード"), 4, Status(5, 0, 2, 0, 0, 2, -3)),
    AoharuTraining(toSupportType("スピード"), 5, Status(7, 0, 3, 0, 0, 3, -4)),
    AoharuTraining(toSupportType("スピード"), 6, Status(15, 0, 7, 0, 0, 0, -5)),
    AoharuTraining(toSupportType("スピード"), 7, Status(20, 0, 10, 0, 0, 0, -5)),
    AoharuTraining(toSupportType("スタミナ"), 1, Status(0, 0, 0, 0, 0, 0, 0)),
    AoharuTraining(toSupportType("スタミナ"), 2, Status(0, 2, 0, 0, 0, 0, -1)),
    AoharuTraining(toSupportType("スタミナ"), 3, Status(0, 3, 0, 1, 0, 1, -2)),
    AoharuTraining(toSupportType("スタミナ"), 4, Status(0, 5, 0, 2, 0, 2, -3)),
    AoharuTraining(toSupportType("スタミナ"), 5, Status(0, 7, 0, 3, 0, 3, -4)),
    AoharuTraining(toSupportType("スタミナ"), 6, Status(0, 15, 0, 7, 0, 0, -5)),
    AoharuTraining(toSupportType("スタミナ"), 7, Status(0, 20, 0, 10, 0, 0, -5)),
    AoharuTraining(toSupportType("パワー"), 1, Status(0, 0, 0, 0, 0, 0, 0)),
    AoharuTraining(toSupportType("パワー"), 2, Status(0, 0, 2, 0, 0, 0, -1)),
    AoharuTraining(toSupportType("パワー"), 3, Status(0, 1, 3, 0, 0, 1, -2)),
    AoharuTraining(toSupportType("パワー"), 4, Status(0, 2, 5, 0, 0, 2, -3)),
    AoharuTraining(toSupportType("パワー"), 5, Status(0, 3, 7, 0, 0, 3, -4)),
    AoharuTraining(toSupportType("パワー"), 6, Status(0, 7, 15, 0, 0, 0, -5)),
    AoharuTraining(toSupportType("パワー"), 7, Status(0, 10, 20, 0, 0, 0, -5)),
    AoharuTraining(toSupportType("根性"), 1, Status(0, 0, 0, 0, 0, 0, 0)),
    AoharuTraining(toSupportType("根性"), 2, Status(0, 0, 0, 2, 0, 0, -1)),
    AoharuTraining(toSupportType("根性"), 3, Status(1, 0, 1, 2, 0, 1, -2)),
    AoharuTraining(toSupportType("根性"), 4, Status(2, 0, 1, 4, 0, 2, -3)),
    AoharuTraining(toSupportType("根性"), 5, Status(2, 0, 2, 6, 0, 3, -4)),
    AoharuTraining(toSupportType("根性"), 6, Status(3, 0, 3, 15, 0, 0, -5)),
    AoharuTraining(toSupportType("根性"), 7, Status(5, 0, 5, 20, 0, 0, -5)),
    AoharuTraining(toSupportType("賢さ"), 1, Status(0, 0, 0, 0, 0, 0, 0)),
    AoharuTraining(toSupportType("賢さ"), 2, Status(0, 0, 0, 0, 1, 0, 0)),
    AoharuTraining(toSupportType("賢さ"), 3, Status(0, 0, 0, 0, 2, 1, 0)),
    AoharuTraining(toSupportType("賢さ"), 4, Status(1, 0, 0, 0, 3, 2, 0)),
    AoharuTraining(toSupportType("賢さ"), 5, Status(2, 0, 0, 0, 4, 3, 0)),
    AoharuTraining(toSupportType("賢さ"), 6, Status(2, 0, 0, 0, 10, 5, 5)),
    AoharuTraining(toSupportType("賢さ"), 7, Status(5, 0, 0, 0, 15, 5, 5)),
)

private val aoharuTrainingTeamData = listOf(
    AoharuTrainingTeam(toSupportType("スピード"), 1, Status(30, 5, 15, 5, 5), Status(40, 10, 25, 10, 10)),
    AoharuTrainingTeam(toSupportType("スピード"), 2, Status(40, 6, 20, 6, 6), Status(50, 11, 30, 11, 11)),
    AoharuTrainingTeam(toSupportType("スピード"), 3, Status(50, 7, 25, 7, 7), Status(60, 12, 35, 12, 12)),
    AoharuTrainingTeam(toSupportType("スピード"), 4, Status(60, 8, 30, 8, 8), Status(70, 13, 40, 13, 13)),
    AoharuTrainingTeam(toSupportType("スピード"), 5, Status(70, 9, 35, 9, 9), Status(80, 14, 45, 14, 14)),
    AoharuTrainingTeam(toSupportType("スタミナ"), 1, Status(5, 30, 5, 15, 5), Status(10, 40, 10, 25, 10)),
    AoharuTrainingTeam(toSupportType("スタミナ"), 2, Status(6, 40, 6, 20, 6), Status(11, 50, 11, 30, 11)),
    AoharuTrainingTeam(toSupportType("スタミナ"), 3, Status(7, 50, 7, 25, 7), Status(12, 60, 12, 35, 12)),
    AoharuTrainingTeam(toSupportType("スタミナ"), 4, Status(8, 60, 8, 30, 8), Status(13, 70, 13, 40, 13)),
    AoharuTrainingTeam(toSupportType("スタミナ"), 5, Status(9, 70, 9, 35, 9), Status(14, 80, 14, 45, 14)),
    AoharuTrainingTeam(toSupportType("パワー"), 1, Status(5, 15, 30, 5, 5), Status(10, 25, 40, 10, 10)),
    AoharuTrainingTeam(toSupportType("パワー"), 2, Status(6, 20, 40, 6, 6), Status(11, 30, 50, 11, 11)),
    AoharuTrainingTeam(toSupportType("パワー"), 3, Status(7, 25, 50, 7, 7), Status(12, 35, 60, 12, 12)),
    AoharuTrainingTeam(toSupportType("パワー"), 4, Status(8, 30, 60, 8, 8), Status(13, 40, 70, 13, 13)),
    AoharuTrainingTeam(toSupportType("パワー"), 5, Status(9, 35, 70, 9, 9), Status(14, 45, 80, 14, 14)),
    AoharuTrainingTeam(toSupportType("根性"), 1, Status(13, 5, 13, 30, 5), Status(23, 10, 23, 40, 10)),
    AoharuTrainingTeam(toSupportType("根性"), 2, Status(16, 6, 16, 40, 6), Status(26, 11, 26, 50, 11)),
    AoharuTrainingTeam(toSupportType("根性"), 3, Status(19, 7, 19, 50, 7), Status(29, 12, 29, 60, 12)),
    AoharuTrainingTeam(toSupportType("根性"), 4, Status(22, 8, 22, 60, 8), Status(32, 13, 32, 70, 13)),
    AoharuTrainingTeam(toSupportType("根性"), 5, Status(25, 9, 25, 70, 9), Status(35, 14, 35, 80, 14)),
    AoharuTrainingTeam(toSupportType("賢さ"), 1, Status(15, 5, 5, 5, 30), Status(25, 10, 10, 10, 40)),
    AoharuTrainingTeam(toSupportType("賢さ"), 2, Status(20, 6, 6, 6, 40), Status(30, 11, 11, 11, 50)),
    AoharuTrainingTeam(toSupportType("賢さ"), 3, Status(25, 7, 7, 7, 50), Status(35, 12, 12, 12, 60)),
    AoharuTrainingTeam(toSupportType("賢さ"), 4, Status(30, 8, 8, 8, 60), Status(40, 13, 13, 13, 70)),
    AoharuTrainingTeam(toSupportType("賢さ"), 5, Status(35, 9, 9, 9, 70), Status(45, 14, 14, 14, 80)),
)

private val aoharuBurnTeamData = listOf(
    AoharuBurnTeam(toSupportType("スピード"), Status(150, 80, 110, 80, 70)),
    AoharuBurnTeam(toSupportType("スタミナ"), Status(80, 150, 80, 110, 70)),
    AoharuBurnTeam(toSupportType("パワー"), Status(80, 110, 150, 80, 70)),
    AoharuBurnTeam(toSupportType("根性"), Status(90, 80, 90, 150, 70)),
    AoharuBurnTeam(toSupportType("賢さ"), Status(110, 80, 80, 80, 150)),
)

private val aoharuTeamStatusRank = listOf(
    AoharuTeamStatusRank(0, "S", 610, 400, 450, 50, 5),
    AoharuTeamStatusRank(1, "A", 510, 350, 400, 50, 4),
    AoharuTeamStatusRank(2, "B", 420, 350, 400, 50, 3),
    AoharuTeamStatusRank(3, "C", 340, 300, 350, 0, 3),
    AoharuTeamStatusRank(4, "D", 260, 250, 300, 0, 2),
    AoharuTeamStatusRank(5, "E", 200, 150, 200, 0, 2),
    AoharuTeamStatusRank(6, "F", 150, 50, 100, 0, 1),
    AoharuTeamStatusRank(7, "G", 0, 0, 0, 0, 1),
)

internal val aoharuTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(8, 0, 4, 0, 0, 4, -19)),
    TrainingBase(toSupportType("S"), 2, 524, Status(9, 0, 4, 0, 0, 4, -20)),
    TrainingBase(toSupportType("S"), 3, 528, Status(10, 0, 4, 0, 0, 4, -21)),
    TrainingBase(toSupportType("S"), 4, 532, Status(11, 0, 5, 0, 0, 4, -23)),
    TrainingBase(toSupportType("S"), 5, 536, Status(12, 0, 6, 0, 0, 4, -25)),
    TrainingBase(toSupportType("P"), 1, 516, Status(0, 4, 9, 0, 0, 4, -20)),
    TrainingBase(toSupportType("P"), 2, 520, Status(0, 4, 10, 0, 0, 4, -21)),
    TrainingBase(toSupportType("P"), 3, 524, Status(0, 4, 11, 0, 0, 4, -22)),
    TrainingBase(toSupportType("P"), 4, 528, Status(0, 5, 12, 0, 0, 4, -24)),
    TrainingBase(toSupportType("P"), 5, 532, Status(0, 6, 13, 0, 0, 4, -26)),
    TrainingBase(toSupportType("G"), 1, 532, Status(3, 0, 3, 6, 0, 4, -20)),
    TrainingBase(toSupportType("G"), 2, 536, Status(3, 0, 3, 7, 0, 4, -21)),
    TrainingBase(toSupportType("G"), 3, 540, Status(3, 0, 3, 8, 0, 4, -22)),
    TrainingBase(toSupportType("G"), 4, 544, Status(4, 0, 3, 9, 0, 4, -24)),
    TrainingBase(toSupportType("G"), 5, 548, Status(4, 0, 4, 10, 0, 4, -26)),
    TrainingBase(toSupportType("H"), 1, 507, Status(0, 8, 0, 6, 0, 4, -20)),
    TrainingBase(toSupportType("H"), 2, 511, Status(0, 9, 0, 6, 0, 4, -21)),
    TrainingBase(toSupportType("H"), 3, 515, Status(0, 10, 0, 6, 0, 4, -22)),
    TrainingBase(toSupportType("H"), 4, 519, Status(0, 11, 0, 7, 0, 4, -24)),
    TrainingBase(toSupportType("H"), 5, 523, Status(0, 12, 0, 8, 0, 4, -26)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 6, 5, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 7, 5, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 8, 5, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 9, 5, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 10, 5, 5)),
)
