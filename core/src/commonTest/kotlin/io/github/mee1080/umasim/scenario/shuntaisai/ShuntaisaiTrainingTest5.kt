package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType.*
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult.GREAT_SUCCESS
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching.*
import kotlin.test.Test

class ShuntaisaiTrainingTest5 : ShuntaisaiTrainingTestBase(
    chara = Triple("[ローゼスドリーム]ライスシャワー", 4, 5),
    supportCardList = listOf(
        "[いとどめでたき、まつりかな]吾妻" to 4,
        "[うららか・ぱっしょん♪]ハルウララ" to 1,
        "[トレセン学園]ニシノフラワー" to 4,
        "[トレセン学園]スイープトウショウ" to 4,
        "[トレセン学園]ルーラーシップ" to 4,
        "[トレセン学園]フォーエバーヤング" to 4,
    )
) {

    @Test
    fun test() {
        // WS000000～WS000005.png: サポカ詳細
        // スピード2、スタミナ1、パワー1、賢さ1、友人（吾妻）のため、サポカは得意演目、ゲストは根性2人、他1人ずつ
        // 理事長等のNPC（演目アイコンなし）がトレーニングに現れている場合があるが、上昇量に影響しない
        // ジュニア: 大曳神輿=助力（流鏑馬、神旗争奪戦、納舞、飛駆は絆、得意率、ヒント、体力のいずれか、上昇量に影響なし）
        var baseCalcInfo = state.baseCalcInfo.initShuntaisaiStatus()
            .copy(motivation = 2)
            .setPerformance(1, POWER)
            .setPerformance(2, SPEED)
            .setPerformance(3, SPEED)
            .setPerformance(4, STAMINA)
            .setPerformance(5, WISDOM)
            .setTeaching(SPEED, TAIRYOKU)
            .setTeaching(STAMINA, TOKUI)
            .setTeaching(POWER, HINT)
            .setTeaching(GUTS, JORYOKU)
            .setTeaching(WISDOM, KIZUNA)
            .setTurn(11)
            .setShuntaisaiLevels(2, 1, 1, 1, 1)

        // WS000006.png: ジュニア デビュー前
        testTraining(
            baseCalcInfo, POWER, 2, listOf(STAMINA), 4,
            base = Status(0, 10, 13, 0, 0, 9),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア9月前半（合宿の演目稽古で助力の支えを獲得済み）
        baseCalcInfo = baseCalcInfo
            .setTurn(17)
            .setShuntaisaiLevels(3, 3, 2, 3, 2)
            .setJoryokuSupport(1)

        // WS000007.png
        testTraining(
            baseCalcInfo, GUTS, 2, listOf(GUTS), 1,
            base = Status(2, 0, 2, 21, 0, 12),
            scenario = Status(0, 0, 0, 6, 0, 3),
        )

        // WS000008～WS000009.png: 教え転成（体力→特化、得意率→研鑽）
        // クラシック: 流鏑馬=特化(体力)、神旗争奪戦=助力、納舞=ヒント、大曳神輿=研鑽(得意率)、飛駆=絆
        // 第1回進捗発表会大成功、助力の支えあり
        baseCalcInfo = baseCalcInfo
            .setTurn(25)
            .setTeaching(SPEED, TOKKA, TAIRYOKU)
            .setTeaching(STAMINA, JORYOKU)
            .setTeaching(POWER, HINT)
            .setTeaching(GUTS, KENSAN, TOKUI)
            .setTeaching(WISDOM, KIZUNA)
            .setShuntaisaiLevels(3, 3, 3, 4, 3)
            .setPresentationBonus(0, GREAT_SUCCESS)

        // WS000010.png
        testTraining(
            baseCalcInfo, POWER, 3, listOf(GUTS, WISDOM), 2, 4,
            base = Status(0, 12, 18, 0, 0, 11),
            scenario = Status(0, 3, 6, 0, 0, 3),
        )

        // WS000011.png
        testTraining(
            baseCalcInfo, GUTS, 3, listOf(STAMINA),
            base = Status(2, 0, 2, 21, 0, 8),
            scenario = Status(0, 0, 0, 6, 0, 2),
        )

        // WS000012.png
        testTraining(
            baseCalcInfo, SPEED, 2, emptyList(), 1,
            base = Status(17, 0, 2, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック6月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(36)
            .setShuntaisaiLevels(4, 4, 4, 4, 5)
            .setJoryokuSupport(0)

        // WS000013.png
        testTraining(
            baseCalcInfo, GUTS, 4, emptyList(),
            base = Status(3, 0, 2, 21, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック9月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(41)
            .setShuntaisaiLevels(5, 5, 5, 5, 6)

        // WS000014.png
        testTraining(
            baseCalcInfo, POWER, 4, emptyList(),
            base = Status(0, 9, 14, 0, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック11月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(46)
            .setShuntaisaiLevels(6, 5, 5, 5, 6)

        // WS000015.png
        testTraining(
            baseCalcInfo, SPEED, 3, listOf(SPEED),
            base = Status(17, 0, 2, 0, 0, 8),
            scenario = Status(1, 0, 0, 0, 0, 0),
        )

        // シニア: 流鏑馬=不屈(助力)、納舞=協力、大曳神輿=研鑽（上昇量からの推定）
        // 流鏑馬と納舞の不屈と協力は逆の可能性あり、他の演目は上昇量に影響なし
        // 第2回進捗発表会大成功（各演目にゲスト1人ずつ追加）
        // シニア1月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(49)
            .setTeaching(SPEED, FUKUTSU, JORYOKU)
            .setTeaching(STAMINA, RYODO, TAIRYOKU)
            .setTeaching(POWER, KYORYOKU, HINT)
            .setShuntaisaiLevels(7, 5, 5, 5, 6)
            .setPresentationBonus(1, GREAT_SUCCESS)

        // WS000016.png
        testTraining(
            baseCalcInfo, SPEED, 4, listOf(SPEED),
            base = Status(18, 0, 3, 0, 0, 8),
            scenario = Status(3, 0, 0, 0, 0, 1),
        )

        // シニア1月後半（大曳神輿の演目稽古で助力の支えを獲得）
        baseCalcInfo = baseCalcInfo
            .setTurn(50)
            .setShuntaisaiLevels(7, 5, 5, 6, 6)
            .setJoryokuSupport(3)

        // WS000017.png
        testTraining(
            baseCalcInfo, GUTS, 5, listOf(SPEED, SPEED, GUTS),
            base = Status(4, 0, 2, 26, 0, 9),
            scenario = Status(1, 0, 0, 11, 0, 4),
        )

        // シニア2月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(51)
            .setShuntaisaiLevels(7, 5, 6, 6, 6)
            .setJoryokuSupport(2)

        // WS000018.png
        testTraining(
            baseCalcInfo, POWER, 5, listOf(GUTS), 1, 2, friends = listOf(1, 2),
            base = Status(0, 15, 23, 0, 0, 16),
            scenario = Status(0, 3, 5, 0, 0, 3),
        )

        // シニア4月前半（好調）
        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)
            .setTurn(55)
            .setShuntaisaiLevels(7, 5, 6, 6, 7)
            .setJoryokuSupport(0)

        // WS000019.png
        testTraining(
            baseCalcInfo, SPEED, 5, emptyList(), 1, friends = listOf(1),
            base = Status(18, 0, 3, 0, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}
