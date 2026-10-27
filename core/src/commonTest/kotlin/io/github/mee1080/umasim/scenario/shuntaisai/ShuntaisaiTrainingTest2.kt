package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType.*
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult.SUCCESS
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiTeaching.*
import kotlin.test.Test

class ShuntaisaiTrainingTest2 : ShuntaisaiTrainingTestBase(
    chara = Triple("[初うらら♪さくさくら]ハルウララ", 5, 5),
    supportCardList = listOf(
        "[いとどめでたき、まつりかな]吾妻" to 4,
        "[緋色の君へ風が吹く]ダイワスカーレット" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[無垢の白妙]デアリングタクト" to 4,
        "[百花の願いをこの胸に]サトノダイヤモンド" to 4,
        "[燦爛]メジロラモーヌ" to 3,
    )
) {

    @Test
    fun test() {
        // WS000000～WS000005.png: サポカ詳細
        // WS000006.png: ジュニア3ターン目の教え伝受
        //  賢さサポカ5枚（うち1枚はシナリオリンクのダイワスカーレット）のため、
        //  ダイワスカーレット=流鏑馬、フォーエバーヤング・デアリングタクト=神旗争奪戦、サトノダイヤモンド・メジロラモーヌ=飛駆
        //  ゲストはパワー2人、他1人ずつ
        //  流鏑馬=ヒント、神旗争奪戦=得意率、納舞=体力、大曳神輿=助力、飛駆=絆
        var baseCalcInfo = state.baseCalcInfo.initShuntaisaiStatus()
            .copy(motivation = 2)
            .setPerformance(1, SPEED)
            .setPerformance(2, STAMINA)
            .setPerformance(3, STAMINA)
            .setPerformance(4, WISDOM)
            .setPerformance(5, WISDOM)
            .setTeaching(SPEED, HINT)
            .setTeaching(STAMINA, TOKUI)
            .setTeaching(POWER, TAIRYOKU)
            .setTeaching(GUTS, JORYOKU)
            .setTeaching(WISDOM, KIZUNA)
            .setTurn(8)
            .setRelation(0, 60)
            .setRelation(3, 60)
            .setRelation(4, 60)

        // WS000007.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 25, 0, 11, 0, 25),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000008.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(GUTS, WISDOM), 0, 4, 5,
            base = Status(8, 0, 0, 0, 30, 20),
            scenario = Status(11, 10, 10, 10, 19, 4),
        )

        // WS000009.png: 助力の支え獲得

        // ジュニア デビュー前（助力の支え有効）
        baseCalcInfo = baseCalcInfo
            .setTurn(10)
            .setShuntaisaiLevels(1, 1, 1, 1, 2)
            .setJoryokuSupport(2)
            .setRelation(0, 80)
            .setRelation(1, 80)
            .setRelation(3, 80)
            .setRelation(4, 80)

        // WS000010.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(GUTS, SPEED), 1,
            base = Status(25, 0, 5, 0, 0, 15),
            scenario = Status(16, 10, 11, 10, 10, 3),
        )

        // WS000011.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 0, 2, 3, 4,
            base = Status(0, 37, 0, 16, 0, 39),
            scenario = Status(10, 13, 10, 10, 10, 1),
        )

        // WS000012.png: ログ（助力の支えがなくなった）

        // ジュニア合宿
        baseCalcInfo = baseCalcInfo
            .setTurn(13)
            .setJoryokuSupport(0)
            .setRelation(2, 60)
            .setRelation(5, 60)

        // WS000013.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(24, 0, 5, 0, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000014.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 25, 0, 11, 0, 25),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000015.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER),
            base = Status(0, 7, 22, 0, 0, 13),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000016.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS),
            base = Status(3, 0, 3, 20, 0, 12),
            scenario = Status(10, 10, 10, 14, 10, 2),
        )

        // WS000017.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 0, 4, 5, friends = listOf(4),
            base = Status(10, 0, 0, 0, 38, 28),
            scenario = Status(10, 10, 10, 10, 17, 0),
        )

        // ジュニア合宿2ターン目
        baseCalcInfo = baseCalcInfo
            .setTurn(14)
            .setRelation(0, 100)
            .setRelation(5, 80)

        // WS000018.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(24, 0, 5, 0, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000019.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 25, 0, 11, 0, 25),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000020.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER),
            base = Status(0, 7, 22, 0, 0, 13),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000021.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS),
            base = Status(3, 0, 3, 20, 0, 12),
            scenario = Status(10, 10, 10, 14, 10, 2),
        )

        // WS000022.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 0, 4, 5, friends = listOf(4, 5),
            base = Status(13, 0, 0, 0, 51, 37),
            scenario = Status(10, 10, 10, 10, 19, 1),
        )

        // ジュニア9月前半（合宿後）
        baseCalcInfo = baseCalcInfo
            .setTurn(17)
            .setShuntaisaiLevels(2, 2, 2, 2, 3)
            .setRelation(1, 100)
            .setRelation(3, 100)

        // WS000023.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(24, 0, 5, 0, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000024.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(POWER, STAMINA), 2, 3,
            base = Status(0, 26, 0, 12, 0, 26),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000025.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS),
            base = Status(3, 0, 3, 20, 0, 12),
            scenario = Status(10, 10, 10, 14, 10, 2),
        )

        // WS000026.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(POWER, WISDOM), 0, 4, 5, friends = listOf(4, 5),
            base = Status(14, 0, 0, 0, 53, 39),
            scenario = Status(10, 10, 10, 10, 23, 1),
        )

        // ジュニア9月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(18)
            .setShuntaisaiLevels(2, 2, 2, 2, 4)

        // WS000027.png
        testTraining(
            baseCalcInfo, WISDOM, 2, listOf(POWER), 5, friends = listOf(5),
            base = Status(3, 0, 0, 0, 18, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア11月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(21)

        // WS000028.png（1人目は編成外のキャラ）
        testTraining(
            baseCalcInfo, WISDOM, 3, emptyList(), 5, friends = listOf(5),
            base = Status(3, 0, 0, 0, 19, 12),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // ジュニア11月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(22)
            .setRelation(2, 80)

        // WS000029.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(24, 0, 5, 0, 0, 15),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000030.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 26, 0, 12, 0, 26),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000031.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER),
            base = Status(0, 7, 22, 0, 0, 13),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000032.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS), 0,
            base = Status(6, 0, 3, 24, 0, 16),
            scenario = Status(11, 10, 10, 15, 10, 3),
        )

        // WS000033.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 4, 5, friends = listOf(4, 5),
            base = Status(8, 0, 0, 0, 43, 29),
            scenario = Status(10, 10, 10, 10, 24, 1),
        )

        // WS000034.png: 教え転成（絆→特化、得意率→研鑽）
        // クラシック: 神旗争奪戦=研鑽(得意率)、飛駆=特化(絆)、第1回進捗発表会成功
        baseCalcInfo = baseCalcInfo
            .setTurn(25)
            .setTeaching(STAMINA, KENSAN, TOKUI)
            .setTeaching(WISDOM, TOKKA, KIZUNA)
            .setPresentationBonus(0, SUCCESS)
            .setShuntaisaiLevels(2, 2, 2, 2, 5)

        // WS000035.png
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(31, 0, 5, 0, 0, 16),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000036.png
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 37, 0, 16, 0, 29),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000037.png
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER), 0,
            base = Status(0, 14, 37, 0, 0, 20),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000038.png
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS),
            base = Status(3, 0, 3, 31, 0, 15),
            scenario = Status(10, 10, 10, 16, 10, 3),
        )

        // WS000039.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 4, 5, friends = listOf(4, 5),
            base = Status(11, 0, 0, 0, 49, 32),
            scenario = Status(10, 10, 10, 10, 50, 1),
        )

        // クラシック1月後半
        baseCalcInfo = baseCalcInfo
            .setTurn(26)
            .setShuntaisaiLevels(2, 2, 2, 2, 6)

        // WS000040.png（編成外のキャラのみ）
        testTraining(
            baseCalcInfo, WISDOM, 4, emptyList(),
            base = Status(3, 0, 0, 0, 13, 7),
            scenario = Status(0, 0, 0, 0, 0, 0),
        )

        // クラシック2月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(27)

        // WS000041.png（1人目は編成外のキャラ）
        testTraining(
            baseCalcInfo, WISDOM, 4, listOf(WISDOM), 5, friends = listOf(5),
            base = Status(5, 0, 0, 0, 21, 12),
            scenario = Status(0, 0, 0, 0, 4, 0),
        )

        // クラシック3月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(29)

        // WS000042.png
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 4, 5, friends = listOf(4, 5),
            base = Status(11, 0, 0, 0, 49, 32),
            scenario = Status(10, 10, 10, 10, 52, 2),
        )

        // クラシック5月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(33)
            .setShuntaisaiLevels(2, 2, 2, 2, 7)

        // WS000043.png（1人目は編成外のキャラ、画面表示は賢さ下段+13、上段+1）
        testTraining(
            baseCalcInfo, WISDOM, 5, emptyList(), 4, friends = listOf(4),
            base = Status(5, 0, 0, 0, 27, 13),
            scenario = Status(0, 0, 0, 0, 3, 0),
        )

        // WS000044.png: クラシック合宿の教え付け替え
        // 流鏑馬=ヒント、神旗争奪戦=特化(絆)、納舞=体力、大曳神輿=助力、飛駆=研鑽(得意率)
        baseCalcInfo = baseCalcInfo
            .setTurn(37)
            .setTeaching(SPEED, HINT)
            .setTeaching(STAMINA, TOKKA, KIZUNA)
            .setTeaching(POWER, TAIRYOKU)
            .setTeaching(GUTS, JORYOKU)
            .setTeaching(WISDOM, KENSAN, TOKUI)
            .setShuntaisaiLevels(2, 2, 2, 2, 8)

        // WS000045.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(31, 0, 5, 0, 0, 16),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000046.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 37, 0, 16, 0, 29),
            scenario = Status(10, 33, 10, 10, 10, 0),
        )

        // WS000047.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER), 0,
            base = Status(0, 14, 37, 0, 0, 20),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000048.png（絆の転成効果でフォーエバーヤングが追加で出現、画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS), 2,
            base = Status(4, 0, 3, 40, 0, 22),
            scenario = Status(10, 10, 10, 29, 10, 4),
        )

        // WS000049.png（画面表示は賢さ下段+24、上段+14）
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 4, 5, friends = listOf(4, 5),
            base = Status(11, 0, 0, 0, 49, 32),
            scenario = Status(14, 10, 10, 10, 29, 12),
        )

        // クラシック9月前半
        baseCalcInfo = baseCalcInfo
            .setTurn(41)
            .setShuntaisaiLevels(3, 3, 3, 3, 9)
            .setRelation(2, 100)

        // WS000050.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 37, 0, 16, 0, 29),
            scenario = Status(10, 34, 10, 10, 10, 0),
        )

        // WS000051.png: 教え転成（体力→協力、助力→不屈、ヒント→両道）
        // WS000052.png: シニアの教え付け替え、第2回進捗発表会成功
        // 流鏑馬=特化(絆)、神旗争奪戦=協力(体力)、納舞=研鑽(得意率)、大曳神輿=不屈(助力)、飛駆=両道(ヒント)
        baseCalcInfo = baseCalcInfo
            .copy(speedSkillCount = 4)
            .setTurn(49)
            .setTeaching(SPEED, TOKKA, KIZUNA)
            .setTeaching(STAMINA, KYORYOKU, TAIRYOKU)
            .setTeaching(POWER, KENSAN, TOKUI)
            .setTeaching(GUTS, FUKUTSU, JORYOKU)
            .setTeaching(WISDOM, RYODO, HINT)
            .setPresentationBonus(1, SUCCESS)
            .setShuntaisaiLevels(3, 3, 3, 3, 10)

        // WS000053.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1,
            base = Status(31, 0, 5, 0, 0, 16),
            scenario = Status(23, 10, 10, 10, 10, 0),
        )

        // WS000054.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 2, 3,
            base = Status(0, 37, 0, 16, 0, 29),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000055.png（協力の教えでフォーエバーヤングとデアリングタクトが追加で出現、画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER), 2, 3,
            base = Status(0, 15, 46, 0, 0, 31),
            scenario = Status(10, 10, 10, 10, 10, 0),
        )

        // WS000056.png（画面表示は賢さ上段+5）
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS), 0,
            base = Status(6, 0, 3, 37, 0, 19),
            scenario = Status(11, 10, 10, 17, 10, 3),
        )

        // WS000057.png（画面表示は賢さ下段+27、上段+24）
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 4, 5, friends = listOf(4, 5),
            base = Status(12, 0, 0, 0, 54, 35),
            scenario = Status(29, 10, 10, 10, 49, 25),
        )

        // URAファイナルズ、午式駿大祭本番成功
        baseCalcInfo = baseCalcInfo
            .copy(speedSkillCount = 5)
            .setTurn(73)
            .setPresentationBonus(2, SUCCESS)
            .setShuntaisaiLevels(4, 4, 4, 4, 10)

        // WS000058.png（画面表示はスピード下段+23、上段+15、賢さは最大値のため+0）
        testEnmokuTraining(
            baseCalcInfo, SPEED, listOf(SPEED), 1, 2, 3,
            base = Status(47, 0, 7, 0, 0, 32),
            scenario = Status(31, 10, 10, 10, 10, 0),
        )

        // WS000059.png（画面表示はスピード上段+5、賢さは最大値のため+0）
        testEnmokuTraining(
            baseCalcInfo, STAMINA, listOf(STAMINA), 1, 2, 3,
            base = Status(0, 43, 0, 19, 0, 36),
            scenario = Status(10, 19, 10, 10, 10, 0),
        )

        // WS000060.png（画面表示はスピード上段+5、賢さは最大値のため+0）
        testEnmokuTraining(
            baseCalcInfo, POWER, listOf(POWER, POWER), 2, 5,
            base = Status(0, 16, 48, 0, 0, 30),
            scenario = Status(10, 16, 17, 10, 10, 4),
        )

        // WS000061.png（画面表示はスピード下段+2、上段+6、賢さは最大値のため+0）
        testEnmokuTraining(
            baseCalcInfo, GUTS, listOf(GUTS), 4,
            base = Status(4, 0, 3, 39, 0, 20),
            scenario = Status(12, 10, 11, 23, 10, 7),
        )

        // WS000062.png（画面表示はスピード下段+18、上段+39、賢さは最大値のため計算値）
        testEnmokuTraining(
            baseCalcInfo, WISDOM, listOf(WISDOM), 0, 3, 4, 5, friends = listOf(3, 4, 5),
            base = Status(37, 0, 0, 0, 100, 87),
            scenario = Status(78, 10, 10, 10, 97, 75),
        )
    }
}
