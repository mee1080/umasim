package io.github.mee1080.umasim.scenario.climax

import io.github.mee1080.umasim.data.*

class ClimaxStore {
    val shopItem = shopItemData

    private val shopItemMap = shopItem.associateBy { it.name }

    fun getShopItem(name: String) = shopItemMap[name]!!

    val raceAchievement = raceAchievementData
}

private val shopItemData = listOf(
    StatusItem("スピードのメモ帳", 10, Status(speed = 3)),
    StatusItem("スタミナのメモ帳", 10, Status(stamina = 3)),
    StatusItem("パワーのメモ帳", 10, Status(power = 3)),
    StatusItem("根性のメモ帳", 10, Status(guts = 3)),
    StatusItem("賢さのメモ帳", 10, Status(wisdom = 3)),
    StatusItem("スピード戦術書", 15, Status(speed = 7)),
    StatusItem("スタミナ戦術書", 15, Status(stamina = 7)),
    StatusItem("パワー戦術書", 15, Status(power = 7)),
    StatusItem("根性戦術書", 15, Status(guts = 7)),
    StatusItem("賢さ戦術書", 15, Status(wisdom = 7)),
    StatusItem("スピード秘伝書", 30, Status(speed = 15)),
    StatusItem("スタミナ秘伝書", 30, Status(stamina = 15)),
    StatusItem("パワー秘伝書", 30, Status(power = 15)),
    StatusItem("根性秘伝書", 30, Status(guts = 15)),
    StatusItem("賢さ秘伝書", 30, Status(wisdom = 15)),
    StatusItem("バイタル20", 35, Status(hp = 20)),
    StatusItem("バイタル40", 55, Status(hp = 40)),
    StatusItem("バイタル65", 75, Status(hp = 65)),
    StatusItem("ロイヤルビタージュース", 70, Status(hp = 100, motivation = -1)),
    StatusItem("エネドリンクMAX", 30, Status(maxHp = 4, hp = 5)),
    StatusItem("ロングエネドリンクMAX", 50, Status(maxHp = 8)),
    StatusItem("プレーンカップケーキ", 30, Status(motivation = 1)),
    StatusItem("スイートカップケーキ", 55, Status(motivation = 2)),
    UniqueItem("おいしい猫缶", 10),
    UniqueItem("にんじんBBQセット", 40),
    AddConditionItem("プリティーミラー", 150, "愛嬌○"),
    AddConditionItem("名物記者の双眼鏡", 150, "注目株"),
    AddConditionItem("効率練習のススメ", 150, "練習上手○"),
    AddConditionItem("博学帽子", 280, "切れ者"),
    RemoveConditionItem("すやすや安眠枕", 15, listOf("夜ふかし気味")),
    RemoveConditionItem("ポケットスケジュール帳", 15, listOf("なまけ癖")),
    RemoveConditionItem("うるおいハンドクリーム", 15, listOf("肌荒れ")),
    RemoveConditionItem("スリムスキャナー", 15, listOf("太り気味")),
    RemoveConditionItem("アロマディフューザー", 15, listOf("片頭痛")),
    RemoveConditionItem("練習改善DVD", 15, listOf("練習ベタ")),
    RemoveConditionItem(
        "ナンデモナオール",
        40,
        listOf("夜ふかし気味", "なまけ癖", "肌荒れ", "太り気味", "片頭痛", "練習ベタ")
    ),
    TrainingLevelItem("スピードトレーニング嘆願書", 150, toSupportType("スピード")),
    TrainingLevelItem("スタミナトレーニング嘆願書", 150, toSupportType("スタミナ")),
    TrainingLevelItem("パワートレーニング嘆願書", 150, toSupportType("パワー")),
    TrainingLevelItem("根性トレーニング嘆願書", 150, toSupportType("根性")),
    TrainingLevelItem("賢さトレーニング嘆願書", 150, toSupportType("賢さ")),
    UniqueItem("リセットホイッスル", 20),
    MegaphoneItem("チアメガホン", 40, 20, 4),
    MegaphoneItem("スパルタメガホン", 55, 40, 3),
    MegaphoneItem("ブートキャンプメガホン", 70, 60, 2),
    WeightItem("スピードアンクルウェイト", 50, 50, 20, toSupportType("スピード")),
    WeightItem("スタミナアンクルウェイト", 50, 50, 20, toSupportType("スタミナ")),
    WeightItem("パワーアンクルウェイト", 50, 50, 20, toSupportType("パワー")),
    WeightItem("根性アンクルウェイト", 50, 50, 20, toSupportType("根性")),
    UniqueItem("健康祈願のお守り", 40),
    RaceBonusItem("蹄鉄ハンマー・匠", 25, 20),
    RaceBonusItem("蹄鉄ハンマー・極", 40, 35),
    FanBonusItem("三色ペンライト", 15, 50),
)

private val raceAchievementData = listOf(
    RaceAchievement(
        "レジェンドウマ娘", "末脚",
        AnotherAchievementCondition("カリスマウマ娘", "ヒロインウマ娘"),
        AnotherAchievementCondition("春の覇者"),
        AnotherAchievementCondition("秋の覇者"),
    ),
    RaceAchievement(
        "神速マイラー", "マイル直線○",
        AnotherAchievementCondition("高速マイラー"),
        RaceNameCondition("桜花賞", "ヴィクトリアマイル"),
        RaceNameCondition(1, "朝日杯フューチュリティステークス", "阪神ジュベナイルフィリーズ"),
    ),
    RaceAchievement(
        "高速マイラー", 15,
        RaceNameCondition("NHKマイルカップ", "安田記念", "マイルチャンピオンシップ"),
    ),
    RaceAchievement(
        "ベストウマ娘", 15,
        AnotherAchievementCondition("カリスマウマ娘"),
        RaceNameCondition(2, "大阪杯", "天皇賞（春）", "宝塚記念", "天皇賞（秋）", "ジャパンカップ", "有馬記念"),
    ),
    RaceAchievement(
        "ワンダフルウマ娘", 15,
        AnotherAchievementCondition("カリスマウマ娘"),
        RaceCondition { turn <= 48 && (name == "ジャパンカップ" || name == "有馬記念") },
    ),
    RaceAchievement(
        "カリスマウマ娘", 10,
        RaceNameCondition("皐月賞", "東京優駿（日本ダービー）", "菊花賞"),
    ),
    RaceAchievement(
        "ダートGⅠ覇者", "レコメンド",
        RaceCondition(9) { (grade == RaceGrade.G1 || grade == RaceGrade.FINALS) && ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "ダートGⅠ帝王", 15,
        RaceCondition(5) { (grade == RaceGrade.G1 || grade == RaceGrade.FINALS) && ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "ダートGⅠ怪物", 10,
        RaceCondition(4) { (grade == RaceGrade.G1 || grade == RaceGrade.FINALS) && ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "ダートGⅠ強者", 10,
        RaceCondition(3) { (grade == RaceGrade.G1 || grade == RaceGrade.FINALS) && ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "クイーンウマ娘", 15,
        AnotherAchievementCondition("プリンセスウマ娘"),
        RaceNameCondition("ヴィクトリアマイル", "阪神ジュベナイルフィリーズ"),
        RaceCondition { turn >= 49 && name == "エリザベス女王杯" },
    ),
    RaceAchievement(
        "プリンセスウマ娘", 10,
        AnotherAchievementCondition("ヒロインウマ娘"),
        RaceCondition { turn <= 48 && name == "エリザベス女王杯" },
    ),
    RaceAchievement(
        "ヒロインウマ娘", 10,
        RaceNameCondition("桜花賞", "オークス", "秋華賞"),
    ),
    RaceAchievement(
        "光速スプリンター", 15,
        AnotherAchievementCondition("凄腕スプリンター"),
        RaceNameCondition("安田記念", "マイルチャンピオンシップ"),
    ),
    RaceAchievement(
        "凄腕スプリンター", 10,
        RaceNameCondition("高松宮記念", "スプリンターズステークス"),
    ),
    RaceAchievement(
        "盾の覇者", 10,
        RaceNameCondition("天皇賞（春）", "天皇賞（秋）"),
    ),
    RaceAchievement(
        "春の覇者", 10,
        RaceNameCondition("大阪杯", "天皇賞（春）", "宝塚記念"),
    ),
    RaceAchievement(
        "秋の覇者", 10,
        RaceCondition { name == "天皇賞（秋）" && turn >= 49 },
        RaceCondition { name == "ジャパンカップ" && turn >= 49 },
        RaceCondition { name == "有馬記念" && turn >= 49 },
    ),
    RaceAchievement(
        "ダート達人", 10,
        RaceCondition(15) { ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "ダート玄人", 10,
        RaceCondition(10) { ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "ダート上手", 5,
        RaceCondition(5) { ground == RaceGround.DIRT },
    ),
    RaceAchievement(
        "根幹距離の覇者", 10,
        RaceCondition(10) { grade != RaceGrade.FINALS && basicDistance },
    ),
    RaceAchievement(
        "非根幹距離の覇者", 10,
        RaceCondition(10) { grade != RaceGrade.FINALS && !basicDistance },
    ),
    RaceAchievement(
        "ダートスプリンター", 10,
        RaceCondition { turn <= 48 && name == "JBCスプリント" },
        RaceCondition { turn >= 49 && name == "JBCスプリント" },
    ),
    RaceAchievement(
        "可憐なウマ娘", 5,
        RaceNameEndCondition(3, "ウマ娘ステークス"),
    ),
    RaceAchievement(
        "ダートの新星", 5,
        RaceNameCondition("ユニコーンステークス", "ジャパンダートダービー", "レパードステークス"),
    ),
    RaceAchievement(
        "ワールドウマ娘", 5,
        RaceNameCondition(
            3,
            "サウジアラビアロイヤルカップ",
            "ニュージーランドトロフィー",
            "ブラジルカップ",
            "アルゼンチン共和国杯",
            "アメリカJCC",
            "ジャパンカップ",
            "ジャパンダートダービー"
        ),
    ),
    RaceAchievement(
        "ジュニアウマ娘", 5,
        RaceCondition(3) { name.endsWith("ジュニアステークス") },
    ),
    RaceAchievement(
        "名人ウマ娘", 5,
        RaceCondition { ground == RaceGround.TURF && distanceType == RaceDistance.SHORT },
        RaceCondition { ground == RaceGround.TURF && distanceType == RaceDistance.MILE },
        RaceCondition { ground == RaceGround.TURF && distanceType == RaceDistance.MIDDLE },
        RaceCondition { ground == RaceGround.TURF && distanceType == RaceDistance.LONG },
    ),
    RaceAchievement(
        "達人ウマ娘", 5,
        RaceCondition { ground == RaceGround.DIRT && distanceType == RaceDistance.SHORT },
        RaceCondition { ground == RaceGround.DIRT && distanceType == RaceDistance.MILE },
        RaceCondition { ground == RaceGround.DIRT && distanceType == RaceDistance.MIDDLE },
    ),
    RaceAchievement(
        "ベテランウマ娘", 5,
        RaceCondition(10) { grade.ordinal >= RaceGrade.OPEN.ordinal },
    ),
    RaceAchievement(
        "北海道マスター", 5,
        RaceCondition(3) { grade.ordinal >= RaceGrade.G3.ordinal && (courseName == "札幌" || courseName == "函館") },
    ),
    RaceAchievement(
        "東北マスター", 5,
        RaceCondition(3) { grade.ordinal >= RaceGrade.G3.ordinal && (courseName == "福島" || courseName == "新潟" || courseName == "盛岡") },
    ),
    RaceAchievement(
        "関東マスター", 5,
        RaceCondition(3) { grade.ordinal >= RaceGrade.G3.ordinal && (courseName == "東京" || courseName == "中山" || courseName == "大井" || courseName == "川崎" || courseName == "船橋") },
    ),
    RaceAchievement(
        "西日本マスター", 5,
        RaceCondition(3) { grade.ordinal >= RaceGrade.G3.ordinal && (courseName == "中京" || courseName == "阪神" || courseName == "京都") },
    ),
    RaceAchievement(
        "小倉マスター", 5,
        RaceCondition(2) { grade.ordinal >= RaceGrade.G3.ordinal && courseName == "小倉" },
    ),
).reversed()

internal val climaxTrainingData = listOf(
    TrainingBase(toSupportType("S"), 1, 520, Status(8, 0, 4, 0, 0, 2, -19)),
    TrainingBase(toSupportType("S"), 2, 524, Status(9, 0, 4, 0, 0, 2, -20)),
    TrainingBase(toSupportType("S"), 3, 528, Status(10, 0, 4, 0, 0, 2, -21)),
    TrainingBase(toSupportType("S"), 4, 532, Status(11, 0, 5, 0, 0, 2, -23)),
    TrainingBase(toSupportType("S"), 5, 536, Status(12, 0, 6, 0, 0, 2, -25)),
    TrainingBase(toSupportType("P"), 1, 516, Status(0, 4, 6, 0, 0, 2, -18)),
    TrainingBase(toSupportType("P"), 2, 520, Status(0, 4, 7, 0, 0, 2, -19)),
    TrainingBase(toSupportType("P"), 3, 524, Status(0, 4, 8, 0, 0, 2, -20)),
    TrainingBase(toSupportType("P"), 4, 528, Status(0, 5, 9, 0, 0, 2, -22)),
    TrainingBase(toSupportType("P"), 5, 532, Status(0, 6, 10, 0, 0, 2, -24)),
    TrainingBase(toSupportType("G"), 1, 532, Status(3, 0, 3, 6, 0, 2, -20)),
    TrainingBase(toSupportType("G"), 2, 536, Status(3, 0, 3, 7, 0, 2, -21)),
    TrainingBase(toSupportType("G"), 3, 540, Status(3, 0, 3, 8, 0, 2, -22)),
    TrainingBase(toSupportType("G"), 4, 544, Status(4, 0, 3, 9, 0, 2, -24)),
    TrainingBase(toSupportType("G"), 5, 548, Status(4, 0, 4, 10, 0, 2, -26)),
    TrainingBase(toSupportType("H"), 1, 507, Status(0, 7, 0, 3, 0, 2, -17)),
    TrainingBase(toSupportType("H"), 2, 511, Status(0, 8, 0, 3, 0, 2, -18)),
    TrainingBase(toSupportType("H"), 3, 515, Status(0, 9, 0, 3, 0, 2, -19)),
    TrainingBase(toSupportType("H"), 4, 519, Status(0, 10, 0, 4, 0, 2, -21)),
    TrainingBase(toSupportType("H"), 5, 523, Status(0, 11, 0, 5, 0, 2, -23)),
    TrainingBase(toSupportType("W"), 1, 320, Status(2, 0, 0, 0, 6, 3, 5)),
    TrainingBase(toSupportType("W"), 2, 321, Status(2, 0, 0, 0, 7, 3, 5)),
    TrainingBase(toSupportType("W"), 3, 322, Status(2, 0, 0, 0, 8, 3, 5)),
    TrainingBase(toSupportType("W"), 4, 323, Status(3, 0, 0, 0, 9, 3, 5)),
    TrainingBase(toSupportType("W"), 5, 324, Status(4, 0, 0, 0, 10, 3, 5)),
)
