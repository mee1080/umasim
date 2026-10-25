package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.StatusType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ShuntaisaiStatusTest {

    @Test
    fun testGauge() {
        var status = ShuntaisaiStatus(turn = 5)
        status = status.addGauges(mapOf(StatusType.SPEED to 3, StatusType.WISDOM to 1))
        assertEquals(3, status.gauge(StatusType.SPEED))
        assertEquals(1, status.gauge(StatusType.WISDOM))
        assertFalse(status.isPractice(StatusType.SPEED))

        // 最大5で止まる
        status = status.addGauge(StatusType.SPEED, 4)
        assertEquals(shuntaisaiMaxGauge, status.gauge(StatusType.SPEED))
        assertTrue(status.isPractice(StatusType.SPEED))

        // 最も低い演目に+1（同値の場合は先頭）
        status = status.addLowestGauge()
        assertEquals(1, status.gauge(StatusType.STAMINA))
    }

    @Test
    fun testPractice() {
        var status = ShuntaisaiStatus(turn = 5).addGauge(StatusType.POWER, 5)
        status = status.executePractice(StatusType.POWER)
        assertEquals(2, status.level(StatusType.POWER))
        assertEquals(0, status.gauge(StatusType.POWER))

        // ゲージ未満なら演目稽古にならない
        status = status.executePractice(StatusType.POWER)
        assertEquals(2, status.level(StatusType.POWER))

        // 合宿中は全て演目稽古
        val camp = ShuntaisaiStatus(turn = 13)
        assertTrue(camp.isCamp)
        assertEquals(5, StatusType.entries.count { camp.isPractice(it) })
        assertEquals(2, camp.executePractice(StatusType.GUTS).level(StatusType.GUTS))
    }

    @Test
    fun testLevelLimit() {
        var status = ShuntaisaiStatus(maxLevel = shuntaisaiDefaultMaxLevel)
        repeat(10) { status = status.levelUp(StatusType.SPEED) }
        assertEquals(shuntaisaiDefaultMaxLevel, status.level(StatusType.SPEED))

        status = ShuntaisaiStatus(maxLevel = shuntaisaiAzumaMaxLevel)
        repeat(10) { status = status.levelUp(StatusType.SPEED) }
        assertEquals(shuntaisaiAzumaMaxLevel, status.level(StatusType.SPEED))

        // 吾妻お出かけ: 最も低い演目Lv+1
        status = status.levelUpLowest()
        assertEquals(2, status.level(StatusType.STAMINA))
        assertEquals(1, status.minLevel)
    }

    @Test
    fun testTeaching() {
        var status = ShuntaisaiStatus()
            .assignTeaching(ShuntaisaiTeaching.KIZUNA, StatusType.SPEED)
            .assignTeaching(ShuntaisaiTeaching.TOKUI, StatusType.STAMINA)
            .assignTeaching(ShuntaisaiTeaching.HINT, StatusType.POWER)
            .assignTeaching(ShuntaisaiTeaching.JORYOKU, StatusType.GUTS)
            .assignTeaching(ShuntaisaiTeaching.TAIRYOKU, StatusType.WISDOM)
        assertEquals(StatusType.POWER, status.teachingTarget(ShuntaisaiTeaching.HINT))
        assertEquals(
            ShuntaisaiTeaching.TOKUI.effect.specialityRate,
            status.teachingEffect(StatusType.STAMINA).specialityRate,
        )

        // ジュニアの教えは全て転成対象
        assertEquals(5, status.transformTargets(ShuntaisaiTeaching.KENSAN).size)

        status = status.transformTeaching(ShuntaisaiTeaching.KENSAN, StatusType.SPEED)
        val slot = status.teachings[StatusType.SPEED]!!
        assertEquals(ShuntaisaiTeaching.KENSAN, slot.teaching)
        assertEquals(ShuntaisaiTeaching.KIZUNA, slot.transformedFrom)
        assertEquals("研鑽(絆)", slot.displayName)
        // 転成済みの教えは再度消費できない
        assertFalse(status.transformTargets(ShuntaisaiTeaching.TOKKA).contains(StatusType.SPEED))
        // 同じ時期の教えは消費できない
        status = status.transformTeaching(ShuntaisaiTeaching.TOKKA, StatusType.STAMINA)
        assertEquals(3, status.transformTargets(ShuntaisaiTeaching.KYORYOKU).size)
        assertNull(status.teachings[StatusType.GUTS]?.transformedFrom)
    }

    @Test
    fun testTeachingEffect() {
        val slot = ShuntaisaiTeachingSlot(ShuntaisaiTeaching.KYORYOKU, ShuntaisaiTeaching.TOKKA)
        val effect = slot.effect
        assertEquals(ShuntaisaiTeaching.KYORYOKU.effect.friendBonus, effect.friendBonus)
        assertEquals(ShuntaisaiTeaching.TOKKA.effect.mainStatusUp, effect.mainStatusUp)
        assertEquals(1, effect.addMember)
    }

    @Test
    fun testPresentation() {
        val status = ShuntaisaiStatus()
        assertEquals(ShuntaisaiPresentationResult.FAILURE, status.judgePresentation(0))

        val success = status.copy(levels = status.levels.mapValues { 3 })
        assertEquals(ShuntaisaiPresentationResult.SUCCESS, success.judgePresentation(0))
        assertEquals(ShuntaisaiPresentationResult.FAILURE, success.judgePresentation(1))

        val great = status.copy(levels = status.levels.mapValues { 4 })
        assertEquals(ShuntaisaiPresentationResult.GREAT_SUCCESS, great.judgePresentation(0))

        // 1つでも目標未達なら失敗
        val partial = great.levelUp(StatusType.SPEED).copy(levels = great.levels + (StatusType.WISDOM to 2))
        assertEquals(ShuntaisaiPresentationResult.FAILURE, partial.judgePresentation(0))
    }

    @Test
    fun testPracticeStatus() {
        val junior = shuntaisaiPracticeStatus(0, 1)
        assertTrue(junior.speed >= 10)
        assertTrue(junior.skillPt > 0)
        // 範囲外のLvは丸める
        assertEquals(shuntaisaiPracticeStatus(2, 10), shuntaisaiPracticeStatus(3, 12))
    }
}
