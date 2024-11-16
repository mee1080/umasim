package io.github.mee1080.umasim.scenario

import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.data.TeamMemberData
import io.github.mee1080.umasim.data.TrainingBase
import io.github.mee1080.umasim.scenario.aoharu.AoharuCalculator
import io.github.mee1080.umasim.scenario.aoharu.AoharuScenarioEvents
import io.github.mee1080.umasim.scenario.aoharu.aoharuTrainingData
import io.github.mee1080.umasim.scenario.climax.ClimaxCalculator
import io.github.mee1080.umasim.scenario.climax.ClimaxScenarioEvents
import io.github.mee1080.umasim.scenario.climax.climaxTrainingData
import io.github.mee1080.umasim.scenario.cook.CookCalculator
import io.github.mee1080.umasim.scenario.cook.CookScenarioEvents
import io.github.mee1080.umasim.scenario.cook.cookTrainingData
import io.github.mee1080.umasim.scenario.gm.GmCalculator
import io.github.mee1080.umasim.scenario.gm.GmScenarioEvents
import io.github.mee1080.umasim.scenario.gm.gmTrainingData
import io.github.mee1080.umasim.scenario.larc.LArcCalculator
import io.github.mee1080.umasim.scenario.larc.LArcMemberState
import io.github.mee1080.umasim.scenario.larc.LArcScenarioEvents
import io.github.mee1080.umasim.scenario.larc.lArcTrainingData
import io.github.mee1080.umasim.scenario.live.GrandLiveScenarioEvents
import io.github.mee1080.umasim.scenario.live.LiveCalculator
import io.github.mee1080.umasim.scenario.live.liveTrainingData
import io.github.mee1080.umasim.scenario.mecha.MechaCalculator
import io.github.mee1080.umasim.scenario.mecha.MechaScenarioEvents
import io.github.mee1080.umasim.scenario.mecha.mechaTrainingData
import io.github.mee1080.umasim.scenario.uaf.UafCalculator
import io.github.mee1080.umasim.scenario.uaf.UafScenarioEvents
import io.github.mee1080.umasim.scenario.uaf.uafTrainingData
import io.github.mee1080.umasim.scenario.ura.UraScenarioEvents
import io.github.mee1080.umasim.scenario.ura.uraTrainingData
import io.github.mee1080.umasim.simulation2.AoharuMemberState
import io.github.mee1080.umasim.simulation2.AoharuNotMemberState
import io.github.mee1080.umasim.simulation2.ScenarioMemberState

enum class Scenario(
    val scenarioNumber: Int,
    val displayName: String,
    val trainingData: List<TrainingBase>,
    val scenarioEvents: () -> ScenarioEvents = { CommonScenarioEvents() },
    val trainingAutoLevelUp: Boolean = true,
    val guestMember: Boolean = false,
    val turn: Int = 78,
    val levelUpTurns: List<Int> = listOf(37, 38, 39, 40, 61, 62, 63, 64),
    val hasSecondTrainingStatus: Boolean = true,
    val scenarioLink: Set<String> = emptySet(),
    val calculator: ScenarioCalculator = ScenarioCalculator.Default,
) {
    URA(
        scenarioNumber = 1,
        displayName = "URA",
        trainingData = uraTrainingData,
        scenarioEvents = { UraScenarioEvents() },
        hasSecondTrainingStatus = false,
    ),

    AOHARU(
        scenarioNumber = 2,
        displayName = "アオハル",
        trainingData = aoharuTrainingData,
        scenarioEvents = { AoharuScenarioEvents() },
        trainingAutoLevelUp = false,
        guestMember = true,
        scenarioLink = setOf(
            "タイキシャトル", "マチカネフクキタル", "ライスシャワー", "ハルウララ",
        ),
        calculator = AoharuCalculator,
    ) {
        override fun memberState(card: SupportCard): ScenarioMemberState {
            return Store.Aoharu.getTeamMember(card.id)?.let { memberState(it) } ?: AoharuNotMemberState
        }

        override fun memberState(member: TeamMemberData): ScenarioMemberState {
            return AoharuMemberState(
                member = member,
                status = member.initialStatus,
                maxStatus = member.maxStatus,
                aoharuTrainingCount = 0,
                aoharuIcon = false,
            )
        }
    },

    CLIMAX(
        scenarioNumber = 4,
        displayName = "クライマックス",
        trainingData = climaxTrainingData,
        scenarioEvents = { ClimaxScenarioEvents() },
        hasSecondTrainingStatus = false,
        calculator = ClimaxCalculator,
    ),

    GRAND_LIVE(
        scenarioNumber = 3,
        displayName = "グランドライブ",
        trainingData = liveTrainingData,
        scenarioEvents = { GrandLiveScenarioEvents() },
        guestMember = true,
        scenarioLink = setOf(
            "スマートファルコン", "サイレンススズカ", "アグネスタキオン", "ミホノブルボン",
        ),
        calculator = LiveCalculator,
    ),

    GM(
        scenarioNumber = 5,
        displayName = "グランドマスターズ",
        trainingData = gmTrainingData,
        scenarioEvents = { GmScenarioEvents() },
        trainingAutoLevelUp = false,
        calculator = GmCalculator,
    ),

    LARC(
        scenarioNumber = 6,
        displayName = "プロジェクトL'Arc",
        trainingData = lArcTrainingData,
        scenarioEvents = { LArcScenarioEvents() },
        guestMember = true,
        turn = 67,
        levelUpTurns = listOf(37, 38, 39, 40, 42, 61, 62, 63, 64, 66),
        scenarioLink = setOf(
            "ゴールドシップ", "エルコンドルパサー", "サトノダイヤモンド", "マンハッタンカフェ",
            "シリウスシンボリ", "ナカヤマフェスタ", "タップダンスシチー", "オルフェーヴル",
        ),
        calculator = LArcCalculator,
    ) {
        override fun memberState(card: SupportCard) = LArcMemberState()
        override fun memberState(member: TeamMemberData) = LArcMemberState()
    },

    UAF(
        scenarioNumber = 7,
        displayName = "U.A.F.",
        trainingData = uafTrainingData,
        scenarioEvents = { UafScenarioEvents() },
        trainingAutoLevelUp = false,
        scenarioLink = setOf(
            "メジロライアン", "ウイニングチケット", "トーセンジョーダン",
            "ヤエノムテキ", "ナリタトップロード", "都留岐涼花",
        ),
        calculator = UafCalculator,
    ),

    COOK(
        scenarioNumber = 8,
        displayName = "収穫ッ！満腹ッ！大豊食祭",
        trainingData = cookTrainingData,
        scenarioEvents = { CookScenarioEvents() },
        guestMember = true,
        scenarioLink = setOf(
            "スペシャルウィーク", "ヒシアケボノ", "ライスシャワー",
            "ニシノフラワー", "カツラギエース", "秋川理事長",
        ),
        calculator = CookCalculator,
    ),

    MECHA(
        scenarioNumber = 9,
        displayName = "走れ！メカウマ娘",
        trainingData = mechaTrainingData,
        scenarioEvents = { MechaScenarioEvents() },
        guestMember = true,
        scenarioLink = setOf(
            "ビワハヤヒデ", "ナリタタイシン", "エアシャカール", "シンボリクリスエス", "タニノギムレット",
        ),
        calculator = MechaCalculator,
    )

    ;

    open fun memberState(card: SupportCard) = ScenarioMemberState(this)
    open fun memberState(member: TeamMemberData) = ScenarioMemberState(this)
}
