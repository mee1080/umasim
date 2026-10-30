package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType.*
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching.*
import kotlin.test.Test

class ShuntaisaiTrainingTest4 : ShuntaisaiTrainingTestBase(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 4, 5),
    supportCardList = listOf(
        "[巫女]吾妻" to 1,
        "[American Dream]カジノドライヴ" to 4,
        "[謹製ッ！特大夢にんじん！]秋川理事長" to 4,
        "[共に描くキラメキ]都留岐涼花" to 4,
        "[一杯のノスタルジア]駿川たづな" to 2,
        "[本能は吼えているか！？]タッカーブライン" to 1,
    )
) {

    @Test
    fun test() {
        // WS000000～WS000005.png: サポカ詳細
        // 全て友人サポカのため、演目担当は全てゲスト（R吾妻のため演目Lv上限は6）
        // ジュニア: 流鏑馬=得意率、神旗争奪戦=助力、納舞=絆、大曳神輿=体力、飛駆=ヒント
        var baseCalcInfo = state.baseCalcInfo.initShuntaisaiStatus()
            .copy(motivation = 2)
            .setTeaching(SPEED, TOKUI)
            .setTeaching(STAMINA, JORYOKU)
            .setTeaching(POWER, KIZUNA)
            .setTeaching(GUTS, TAIRYOKU)
            .setTeaching(WISDOM, HINT)
            .setTurn(11)

        // WS000006.png: ジュニア デビュー前
        testTraining(
            baseCalcInfo, SPEED, 2, listOf(SPEED), 1,
            base = Status(19, 0, 3, 0, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア10月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(19)
            .setShuntaisaiLevels(2, 2, 2, 2, 2)

        // WS000007.png
        testTraining(
            baseCalcInfo, STAMINA, 2, listOf(STAMINA), 0,
            base = Status(0, 15, 0, 6, 0, 9),
            scenario = Status(0, 3, 0, 1, 0, 1),
        )

        // ジュニア11月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(21)

        // WS000008.png
        testTraining(
            baseCalcInfo, WISDOM, 2, listOf(STAMINA), 1,
            base = Status(3, 0, 0, 0, 13, 9),
            scenario = Status(0, 0, 0, 0, 2, 1),
        )

        // WS000009～WS000011.png: 教え転成（得意率→研鑽、体力→特化）
        // クラシック: 流鏑馬=研鑽(得意率)、神旗争奪戦=助力、納舞=絆、大曳神輿=特化(体力)、飛駆=ヒント
        // クラシック3月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(30)
            .setTeaching(SPEED, KENSAN, TOKUI)
            .setTeaching(GUTS, TOKKA, TAIRYOKU)
            .setShuntaisaiLevels(3, 2, 2, 2, 2)

        // WS000012.png
        testTraining(
            baseCalcInfo, SPEED, 3, listOf(SPEED), 2,
            base = Status(22, 0, 3, 0, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック9月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(41)
            .setShuntaisaiLevels(4, 3, 3, 3, 3)

        // WS000013.png
        testTraining(
            baseCalcInfo, STAMINA, 3, listOf(SPEED, POWER, GUTS), 3,
            base = Status(0, 20, 0, 7, 0, 12),
            scenario = Status(0, 1, 0, 0, 0, 0),
        )

        // WS000014.png
        testTraining(
            baseCalcInfo, WISDOM, 3, listOf(SPEED), 2,
            base = Status(4, 0, 0, 0, 15, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック12月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(48)
            // カジノドライヴは固有効果の発動条件（絆60以上）を満たしている
            .setRelation(1, 80)

        // WS000015.png
        testTraining(
            baseCalcInfo, STAMINA, 4, listOf(WISDOM), 1,
            base = Status(0, 19, 0, 8, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000016.png
        testTraining(
            baseCalcInfo, WISDOM, 4, emptyList(), 4,
            base = Status(4, 0, 0, 0, 15, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000017～WS000020.png
        // シニア: 流鏑馬=研鑽(得意率)、神旗争奪戦=両道(助力)、納舞=協力(絆)、大曳神輿=特化(体力)、飛駆=不屈(ヒント)
        // シニア6月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(60)
            .setTeaching(STAMINA, RYODO, JORYOKU)
            .setTeaching(POWER, KYORYOKU, KIZUNA)
            .setTeaching(WISDOM, FUKUTSU, HINT)
            .setShuntaisaiLevels(5, 4, 3, 3, 4)

        // WS000021.png
        testTraining(
            baseCalcInfo, WISDOM, 5, listOf(SPEED, WISDOM), 2,
            base = Status(6, 0, 0, 0, 18, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // シニア10月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(68)
            .setShuntaisaiLevels(6, 5, 4, 4, 5)

        // WS000022.png
        testTraining(
            baseCalcInfo, STAMINA, 5, listOf(SPEED, STAMINA), 2,
            base = Status(0, 21, 0, 10, 0, 12),
            scenario = Status(0, 4, 0, 2, 0, 2),
        )

        // シニア11月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(70)

        // WS000023.png
        testTraining(
            baseCalcInfo, SPEED, 4, listOf(SPEED), 2,
            base = Status(24, 0, 5, 0, 0, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )
    }
}
