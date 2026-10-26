package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType.*
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult.GREAT_SUCCESS
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching.*
import kotlin.test.Test

class ShuntaisaiTrainingTest1 : ShuntaisaiTrainingTestBase(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[いとどめでたき、まつりかな]吾妻" to 4,
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[その執念は怒濤が如く]メイショウドトウ" to 4,
        "[As if Guided]エフフォーリア" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[Time of Departure]マヤノトップガン" to 0,
    )
) {

    @Test
    fun test() {
        // ジュニア2ターン目（教え伝受前）
        var baseCalcInfo = state.baseCalcInfo.initShuntaisaiStatus()

        // WS000000.png
        testTraining(
            baseCalcInfo, SPEED, 1, emptyList(),
            base = Status(12, 0, 2, 0, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000001.png
        testTraining(
            baseCalcInfo, STAMINA, 1, emptyList(), 2,
            base = Status(0, 12, 0, 3, 0, 10),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000002.png
        testTraining(
            baseCalcInfo, POWER, 1, emptyList(), 1,
            base = Status(0, 7, 13, 0, 0, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000003.png
        testTraining(
            baseCalcInfo, GUTS, 1, emptyList(), 3, 4,
            base = Status(2, 0, 3, 18, 0, 15),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000004.png
        testTraining(
            baseCalcInfo, WISDOM, 1, emptyList(), 5,
            base = Status(2, 0, 0, 0, 9, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア3ターン目以降: 流鏑馬=得意率、神旗争奪戦=ヒント、納舞=体力、大曳神輿=助力、飛駆=絆
        baseCalcInfo = baseCalcInfo
            .setTeaching(SPEED, TOKUI)
            .setTeaching(STAMINA, HINT)
            .setTeaching(POWER, TAIRYOKU)
            .setTeaching(GUTS, JORYOKU)
            .setTeaching(WISDOM, KIZUNA)

        // WS000005.png
        testTraining(
            baseCalcInfo, SPEED, 1, listOf(POWER, GUTS),
            base = Status(13, 0, 2, 0, 0, 7),
            scenario = Status(2, 0, 0, 0, 0, 1),
        )

        // WS000006.png
        testTraining(
            baseCalcInfo, STAMINA, 1, emptyList(),
            base = Status(0, 10, 0, 3, 0, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000007.png
        testTraining(
            baseCalcInfo, POWER, 1, emptyList(), 1, 2, 3, 5,
            base = Status(0, 13, 21, 0, 0, 27),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000008.png
        testTraining(
            baseCalcInfo, GUTS, 1, listOf(SPEED, GUTS, WISDOM),
            base = Status(2, 0, 2, 15, 0, 8),
            scenario = Status(0, 0, 0, 3, 0, 1),
        )

        // WS000009.png
        testTraining(
            baseCalcInfo, WISDOM, 1, emptyList(), 0, 4,
            base = Status(2, 0, 0, 0, 14, 11),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000010.png
        testTraining(
            baseCalcInfo, GUTS, 1, listOf(SPEED, GUTS), 5,
            base = Status(2, 0, 3, 16, 0, 10),
            scenario = Status(0, 0, 0, 3, 0, 2),
        )

        // WS000011.png
        testTraining(
            baseCalcInfo, WISDOM, 1, listOf(POWER, GUTS, STAMINA), 1, 4,
            base = Status(3, 0, 0, 0, 17, 16),
            scenario = Status(0, 0, 0, 0, 3, 3),
        )

        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)

        // WS000012.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, 1, listOf(SPEED), 0, 1, 3, 4,
            base = Status(44, 0, 10, 0, 0, 44),
            scenario = Status(11, 10, 10, 10, 10, 8),
        )

        // WS000013.png
        testTraining(
            baseCalcInfo, POWER, 1, listOf(GUTS, GUTS),
            base = Status(0, 7, 14, 0, 0, 9),
            scenario = Status(0, 2, 5, 0, 0, 3),
        )

        // WS000014.png
        testTraining(
            baseCalcInfo, GUTS, 1, emptyList(),
            base = Status(2, 0, 2, 15, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア合宿
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(2, 1, 2, 1, 1)

        // WS000015.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, 1, listOf(SPEED), 1, 3, friends = listOf(1, 3),
            base = Status(67, 0, 25, 0, 0, 55),
            scenario = Status(12, 10, 10, 10, 10, 15),
        )

        // WS000016.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 1, listOf(STAMINA), 2, friends = listOf(2),
            base = Status(0, 34, 0, 17, 0, 27),
            scenario = Status(10, 10, 10, 10, 10, 6),
        )

        // WS000017.png
        testEnmokuTraining(
            baseCalcInfo, POWER, 1, listOf(POWER), 0, 5,
            base = Status(0, 14, 30, 0, 0, 22),
            scenario = Status(10, 10, 10, 10, 10, 5),
        )

        // WS000018.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, 1, listOf(GUTS, GUTS),
            base = Status(3, 0, 3, 21, 0, 13),
            scenario = Status(11, 10, 11, 18, 10, 7),
        )

        // WS000019.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 1, listOf(WISDOM), 4,
            base = Status(4, 0, 0, 0, 22, 15),
            scenario = Status(10, 10, 10, 10, 11, 3),
        )

        // WS000020.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, 1, listOf(SPEED), 0, 1, 3, friends = listOf(1, 3),
            base = Status(82, 0, 29, 0, 0, 68),
            scenario = Status(12, 10, 10, 10, 10, 19),
        )

        // ジュニア9月前半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(3, 2, 3, 2, 2)

        // WS000021.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 1, listOf(SPEED, STAMINA), 0, 2, friends = listOf(2),
            base = Status(0, 44, 0, 21, 0, 36),
            scenario = Status(10, 11, 10, 10, 10, 10),
        )

        // WS000022.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, 1, listOf(GUTS, GUTS), 3,
            base = Status(5, 0, 6, 29, 0, 21),
            scenario = Status(12, 10, 12, 22, 10, 14),
        )

        // WS000023.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 1, listOf(POWER, WISDOM), 4, 5,
            base = Status(5, 0, 0, 0, 29, 21),
            scenario = Status(10, 10, 10, 10, 12, 5),
        )

        // ジュニア9月後半（助力の支え有効）
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(3, 2, 3, 3, 2)
            .setJoryokuSupport(3)

        // WS000025.png
        testTraining(
            baseCalcInfo, SPEED, 1, listOf(GUTS), 1, 3, 5, friends = listOf(1, 3),
            base = Status(60, 0, 24, 0, 0, 60),
            scenario = Status(3, 0, 1, 0, 0, 3),
        )

        // WS000026.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 1, listOf(SPEED, STAMINA), 0, 2, friends = listOf(2),
            base = Status(0, 44, 0, 21, 0, 36),
            scenario = Status(10, 13, 10, 11, 10, 12),
        )

        // WS000027.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 1, listOf(WISDOM), 4,
            base = Status(4, 0, 0, 0, 22, 15),
            scenario = Status(10, 10, 10, 10, 13, 4),
        )

        // ジュニア12月前半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(3, 3, 3, 3, 3)
            .setJoryokuSupport(0)

        // WS000028.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, 1, listOf(SPEED), 0, 1, 2, 3, friends = listOf(1, 3),
            base = Status(99, 0, 35, 0, 0, 93),
            scenario = Status(13, 10, 11, 10, 10, 32),
        )

        // クラシック: 流鏑馬=特化(得意率)、飛駆=研鑽(絆)、第1回進捗発表会大成功
        baseCalcInfo = baseCalcInfo
            .setTeaching(SPEED, TOKKA, TOKUI)
            .setTeaching(WISDOM, KENSAN, KIZUNA)
            .setPresentationBonus(0, GREAT_SUCCESS)
            .setShuntaisaiLevels(4, 3, 3, 3, 3)

        // WS000029.png
        testTraining(
            baseCalcInfo, SPEED, 2, listOf(SPEED, STAMINA), 1, 5, friends = listOf(1),
            base = Status(38, 0, 10, 0, 0, 29),
            scenario = Status(8, 0, 0, 0, 0, 1),
        )

        // WS000030.png
        testTraining(
            baseCalcInfo, STAMINA, 2, listOf(GUTS, GUTS), 2, 4, friends = listOf(2),
            base = Status(0, 35, 0, 16, 0, 32),
            scenario = Status(0, 1, 0, 0, 0, 1),
        )

        // WS000031.png
        testTraining(
            baseCalcInfo, POWER, 2, emptyList(),
            base = Status(0, 7, 14, 0, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000032.png
        testTraining(
            baseCalcInfo, GUTS, 2, listOf(WISDOM),
            base = Status(2, 0, 3, 18, 0, 8),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // WS000033.png
        testTraining(
            baseCalcInfo, WISDOM, 2, emptyList(), 0, 3,
            base = Status(5, 0, 0, 0, 17, 17),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // クラシック1月後半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(4, 3, 3, 3, 3)

        // WS000034.png
        testTraining(
            baseCalcInfo, SPEED, 2, listOf(GUTS), 1, 3, 4, friends = listOf(1, 3),
            base = Status(57, 0, 21, 0, 0, 48),
            scenario = Status(12, 0, 1, 0, 0, 2),
        )

        // WS000035.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 2, listOf(POWER, STAMINA), 0, 2, friends = listOf(2),
            base = Status(0, 60, 0, 27, 0, 41),
            scenario = Status(10, 15, 10, 12, 10, 17),
        )

        // クラシック2月前半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(4, 3, 4, 3, 3)

        // WS000036.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 2, listOf(STAMINA), 2, 3, 4, friends = listOf(2),
            base = Status(0, 65, 0, 30, 0, 49),
            scenario = Status(10, 40, 10, 15, 10, 26),
        )

        // WS000037.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, 2, listOf(GUTS, GUTS), 0,
            base = Status(6, 0, 3, 39, 0, 20),
            scenario = Status(12, 10, 11, 28, 10, 15),
        )

        // クラシック2月後半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(4, 4, 4, 3, 3)

        // WS000038.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, 2, listOf(GUTS, GUTS), 0, 2, 4,
            base = Status(8, 0, 4, 54, 0, 30),
            scenario = Status(13, 10, 11, 47, 10, 23),
        )

        // クラシック3月前半（助力の支え有効）
        baseCalcInfo = baseCalcInfo
            .copy(motivation = 1)
            .setShuntaisaiLevels(4, 4, 4, 4, 3)
            .setJoryokuSupport(3)

        // WS000039.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 2, listOf(STAMINA, WISDOM), 0, 4, friends = listOf(4),
            base = Status(12, 0, 0, 0, 41, 28),
            scenario = Status(11, 10, 10, 10, 30, 13),
        )

        // クラシック4月前半（助力の支え有効）
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(4, 4, 4, 4, 4)

        // WS000040.png
        testEnmokuTraining(
            baseCalcInfo, POWER, 2, listOf(POWER), 0, 5,
            base = Status(0, 16, 38, 0, 0, 22),
            scenario = Status(10, 10, 11, 10, 10, 9),
        )

        // クラシック5月前半
        baseCalcInfo = baseCalcInfo
            .setJoryokuSupport(0)

        // WS000041.png
        testTraining(
            baseCalcInfo, SPEED, 3, listOf(SPEED, GUTS), 1, friends = listOf(1),
            base = Status(31, 0, 8, 0, 0, 20),
            scenario = Status(6, 0, 0, 0, 0, 1),
        )

        // WS000042.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 3, listOf(WISDOM), 1, 4, friends = listOf(4),
            base = Status(10, 0, 0, 0, 44, 33),
            scenario = Status(12, 10, 10, 10, 38, 22),
        )

        // WS000043.png
        testEnmokuTraining(
            baseCalcInfo, POWER, 2, listOf(POWER), 0, 2, 5,
            base = Status(0, 21, 47, 0, 0, 31),
            scenario = Status(10, 10, 10, 10, 10, 12),
        )

        // クラシック5月後半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(4, 4, 4, 4, 5)

        // WS000044.png（画面表示はスピード+10、1200を超える分が半分になっている）
        testTraining(
            baseCalcInfo, SPEED, 3, listOf(GUTS), 1, 2, 3, friends = listOf(1, 3),
            base = Status(62, 0, 21, 0, 0, 57),
            scenario = Status(13, 0, 1, 0, 0, 2),
        )

        // WS000045.png
        testTraining(
            baseCalcInfo, WISDOM, 3, emptyList(), 4, friends = listOf(4),
            base = Status(3, 0, 0, 0, 24, 15),
            scenario = Status(0, 0, 0, 0, 1, 0),
        )

        // クラシック合宿: 神旗争奪戦と納舞の教えを付け替え
        baseCalcInfo = baseCalcInfo
            .copy(motivation = 2)
            .setTeaching(STAMINA, TAIRYOKU)
            .setTeaching(POWER, HINT)
            .setShuntaisaiLevels(5, 4, 5, 4, 5)

        // WS000046.png（画面表示はスピード下段+44、上段+40）
        testEnmokuTraining(
            baseCalcInfo, SPEED, 3, listOf(SPEED), 1, 3, friends = listOf(1, 3),
            base = Status(88, 0, 25, 0, 0, 59),
            scenario = Status(79, 10, 20, 10, 10, 65),
        )

        // WS000047.png（画面表示はスピード上段+5）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 2, listOf(STAMINA), 2, friends = listOf(2),
            base = Status(0, 47, 0, 22, 0, 31),
            scenario = Status(10, 14, 10, 12, 10, 16),
        )

        // WS000048.png（画面表示はスピード上段+5）
        testEnmokuTraining(
            baseCalcInfo, POWER, 2, listOf(POWER), 0, 5, friends = listOf(5),
            base = Status(0, 22, 55, 0, 0, 32),
            scenario = Status(10, 12, 16, 10, 10, 21),
        )

        // WS000049.png（画面表示はスピード下段+2、上段+6）
        testEnmokuTraining(
            baseCalcInfo, GUTS, 2, listOf(GUTS, GUTS), 4,
            base = Status(4, 0, 3, 42, 0, 23),
            scenario = Status(12, 10, 11, 41, 10, 21),
        )

        // WS000050.png（画面表示はスピード下段+4、上段+5）
        testEnmokuTraining(
            baseCalcInfo, WISDOM, 3, listOf(WISDOM), 4, friends = listOf(4),
            base = Status(8, 0, 0, 0, 37, 24),
            scenario = Status(10, 10, 10, 10, 26, 15),
        )

        // クラシック9月後半
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(6, 6, 6, 5, 6)

        // WS000051.png（画面表示はスピード下段+4、上段+7）
        testEnmokuTraining(
            baseCalcInfo, GUTS, 2, listOf(GUTS, GUTS), 0, 3,
            base = Status(8, 0, 7, 52, 0, 31),
            scenario = Status(14, 10, 13, 47, 10, 32),
        )

        // クラシック10月後半（助力の支え有効）
        baseCalcInfo = baseCalcInfo
            .setShuntaisaiLevels(6, 6, 6, 6, 6)
            .setJoryokuSupport(3)

        // WS000052.png（画面表示はスピード上段+5）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, 2, listOf(STAMINA), 0, 1, 2, 4, friends = listOf(2),
            base = Status(0, 91, 0, 41, 0, 79),
            scenario = Status(10, 64, 10, 21, 10, 80),
        )

        // シニア: 流鏑馬=不屈、神旗争奪戦=協力(助力)、納舞=特化(得意率)、大曳神輿=研鑽(絆)、飛駆=両道、第2回進捗発表会大成功
        // 不屈と両道の転成元（ヒント、体力）はどちらか不明だが、計算には影響しない
        baseCalcInfo = baseCalcInfo
            .setTeaching(SPEED, FUKUTSU, TAIRYOKU)
            .setTeaching(STAMINA, KYORYOKU, JORYOKU)
            .setTeaching(POWER, TOKKA, TOKUI)
            .setTeaching(GUTS, KENSAN, KIZUNA)
            .setTeaching(WISDOM, RYODO, HINT)
            .setPresentationBonus(1, GREAT_SUCCESS)
            .setShuntaisaiLevels(7, 7, 6, 6, 6)
            .setJoryokuSupport(0)

        // WS000053.png（画面表示はスピード下段+18、上段+5）
        testTraining(
            baseCalcInfo, SPEED, 4, listOf(POWER, STAMINA), 1, friends = listOf(1),
            base = Status(36, 0, 11, 0, 0, 22),
            scenario = Status(10, 0, 1, 0, 0, 3),
        )

        // WS000054.png
        testTraining(
            baseCalcInfo, STAMINA, 3, emptyList(), 0, 2, 3, friends = listOf(2),
            base = Status(0, 45, 0, 18, 0, 39),
            scenario = Status(0, 7, 0, 3, 0, 6),
        )

        // WS000055.png
        testTraining(
            baseCalcInfo, POWER, 3, listOf(GUTS, GUTS, WISDOM), 2,
            base = Status(0, 12, 23, 0, 0, 15),
            scenario = Status(0, 3, 4, 0, 0, 3),
        )

        // WS000056.png（画面表示はスピード下段+1）
        testTraining(
            baseCalcInfo, GUTS, 3, listOf(SPEED, GUTS, POWER),
            base = Status(2, 0, 3, 21, 0, 9),
            scenario = Status(0, 0, 0, 2, 0, 0),
        )

        // WS000057.png（画面表示はスピード下段+4、上段+1）
        testTraining(
            baseCalcInfo, WISDOM, 4, listOf(STAMINA, SPEED, WISDOM), 4, 5, friends = listOf(4),
            base = Status(8, 0, 0, 0, 41, 26),
            scenario = Status(2, 0, 0, 0, 11, 4),
        )
    }
}
