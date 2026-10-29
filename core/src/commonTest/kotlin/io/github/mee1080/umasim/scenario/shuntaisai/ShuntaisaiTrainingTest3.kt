package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType.*
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult.GREAT_SUCCESS
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult.SUCCESS
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching.*
import kotlin.test.Test

class ShuntaisaiTrainingTest3 : ShuntaisaiTrainingTestBase(
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
        // ジュニア: 流鏑馬=絆、大曳神輿=助力（神旗争奪戦、納舞、飛駆は得意率、体力、ヒントのいずれか、上昇量に影響なし）
        var baseCalcInfo = state.baseCalcInfo.initShuntaisaiStatus()
            .copy(motivation = 2)
            .setPerformance(1, POWER)
            .setPerformance(2, SPEED)
            .setPerformance(3, SPEED)
            .setPerformance(4, STAMINA)
            .setPerformance(5, WISDOM)
            .setTeaching(SPEED, KIZUNA)
            .setTeaching(STAMINA, TOKUI)
            .setTeaching(POWER, TAIRYOKU)
            .setTeaching(GUTS, JORYOKU)
            .setTeaching(WISDOM, HINT)
            .setTurn(13)

        // ジュニア合宿（演目Lv1）

        // WS000006.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 2, 3,
            base = Status(26, 0, 4, 0, 0, 15),
            scenario = Status(12, 10, 10, 10, 10, 3),
        )

        // WS000007.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 0, 4,
            base = Status(0, 27, 0, 11, 0, 18),
            scenario = Status(10, 10, 10, 10, 10, 3),
        )

        // WS000008.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER), 1,
            base = Status(0, 9, 19, 0, 0, 16),
            scenario = Status(10, 10, 10, 10, 10, 3),
        )

        // WS000009.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS, GUTS),
            base = Status(3, 0, 2, 23, 0, 13),
            scenario = Status(11, 10, 10, 19, 10, 7),
        )

        // WS000010.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 5,
            base = Status(4, 0, 0, 0, 16, 11),
            scenario = Status(10, 10, 10, 10, 10, 2),
        )

        // ジュニア11月前半（演目Lv2）
        baseCalcInfo = baseCalcInfo
            .setTurn(21)
            .setShuntaisaiLevels(2, 2, 2, 2, 2)

        // WS000011.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 2, 3,
            base = Status(26, 0, 4, 0, 0, 15),
            scenario = Status(13, 10, 10, 10, 10, 3),
        )

        // WS000012.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 0, 4,
            base = Status(0, 27, 0, 11, 0, 18),
            scenario = Status(10, 10, 10, 10, 10, 4),
        )

        // WS000013.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER), 1,
            base = Status(0, 9, 19, 0, 0, 16),
            scenario = Status(10, 10, 10, 10, 10, 4),
        )

        // WS000014.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS, GUTS),
            base = Status(3, 0, 2, 23, 0, 13),
            scenario = Status(11, 10, 10, 20, 10, 8),
        )

        // WS000015.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 5,
            base = Status(4, 0, 0, 0, 16, 11),
            scenario = Status(10, 10, 10, 10, 10, 2),
        )

        // WS000016.png: 教え転成（体力→特化、得意率→研鑽）
        // クラシック: 流鏑馬=特化(体力)、神旗争奪戦=助力、納舞=絆、大曳神輿=研鑽(得意率)、飛駆=ヒント、第1回進捗発表会成功
        baseCalcInfo = baseCalcInfo
            .setTurn(25)
            .setTeaching(SPEED, TOKKA, TAIRYOKU)
            .setTeaching(STAMINA, JORYOKU)
            .setTeaching(POWER, KIZUNA)
            .setTeaching(GUTS, KENSAN, TOKUI)
            .setTeaching(WISDOM, HINT)
            .setPresentationBonus(0, SUCCESS)

        // WS000017.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 2, 3,
            base = Status(34, 0, 4, 0, 0, 17),
            scenario = Status(16, 10, 10, 10, 10, 4),
        )

        // WS000018.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 4,
            base = Status(0, 32, 0, 13, 0, 16),
            scenario = Status(10, 24, 10, 15, 10, 11),
        )

        // WS000019.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER), 0, 1,
            base = Status(0, 14, 32, 0, 0, 24),
            scenario = Status(10, 10, 13, 10, 10, 6),
        )

        // WS000020.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS, GUTS),
            base = Status(3, 0, 2, 36, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 3),
        )

        // WS000021.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 5,
            base = Status(5, 0, 0, 0, 19, 12),
            scenario = Status(10, 10, 10, 10, 10, 3),
        )

        // クラシック合宿（演目Lv3）
        baseCalcInfo = baseCalcInfo
            .setTurn(37)
            .setShuntaisaiLevels(3, 3, 3, 3, 3)
            .setRelation(0, 80)
            .setRelation(1, 100)

        // WS000022.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 0, 2, 3,
            base = Status(42, 0, 5, 0, 0, 22),
            scenario = Status(18, 10, 10, 10, 10, 6),
        )

        // WS000023.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 4,
            base = Status(0, 32, 0, 13, 0, 16),
            scenario = Status(10, 25, 10, 16, 10, 12),
        )

        // WS000024.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER), 1, friends = listOf(1),
            base = Status(0, 13, 31, 0, 0, 22),
            scenario = Status(10, 10, 15, 10, 10, 7),
        )

        // WS000025.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS, GUTS),
            base = Status(3, 0, 2, 36, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 4),
        )

        // WS000026.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 5,
            base = Status(5, 0, 0, 0, 19, 12),
            scenario = Status(10, 10, 10, 10, 10, 3),
        )

        // シニア: 流鏑馬=不屈(ヒント)、神旗争奪戦=両道(助力)、納舞=協力(絆)、大曳神輿=研鑽(得意率)、飛駆=特化(体力)
        // 第2回進捗発表会大成功（各演目にゲスト1人ずつ追加）
        // 不屈、両道の転成元はヒント、助力のいずれか（友情トレーニングのみのため上昇量に影響なし）
        baseCalcInfo = baseCalcInfo
            .setTurn(51)
            .setShuntaisaiLevels(5, 5, 5, 5, 5)
            .setTeaching(SPEED, FUKUTSU, HINT)
            .setTeaching(STAMINA, RYODO, JORYOKU)
            .setTeaching(POWER, KYORYOKU, KIZUNA)
            .setTeaching(GUTS, KENSAN, TOKUI)
            .setTeaching(WISDOM, TOKKA, TAIRYOKU)
            .setPresentationBonus(1, GREAT_SUCCESS)
            .setRelation(0, 100)
            .setRelation(2, 100)
            .setRelation(3, 100)

        // WS000027.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED, SPEED), 0, 2, 3, friends = listOf(2, 3),
            base = Status(58, 0, 7, 0, 0, 30),
            scenario = Status(19, 10, 11, 10, 10, 22),
        )

        // シニア4月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(55)
            .setRelation(4, 100)

        // WS000028.png（ハルウララは協力の教えで追加出現）
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED, SPEED), 1, 2, 3, friends = listOf(2, 3),
            base = Status(51, 0, 7, 0, 0, 30),
            scenario = Status(29, 10, 11, 10, 10, 24),
        )

        // WS000029.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA, STAMINA), 4, friends = listOf(4),
            base = Status(0, 38, 0, 16, 0, 20),
            scenario = Status(10, 22, 10, 18, 10, 18),
        )

        // WS000030.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER), 1, friends = listOf(1),
            base = Status(0, 14, 33, 0, 0, 23),
            scenario = Status(10, 14, 39, 10, 10, 22),
        )

        // WS000031.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS, GUTS, GUTS), 0,
            base = Status(6, 0, 3, 45, 0, 21),
            scenario = Status(10, 10, 10, 10, 10, 10),
        )

        // WS000032.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM, WISDOM), 5,
            base = Status(5, 0, 0, 0, 20, 13),
            scenario = Status(10, 10, 10, 10, 15, 6),
        )
    }
}
