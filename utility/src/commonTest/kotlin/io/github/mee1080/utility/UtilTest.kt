package io.github.mee1080.utility

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UtilTest {

    @Test
    fun applyIfBoolean() {
        val data = 1
        assertEquals(2, data.applyIf(true) { this + 1 })
        assertEquals(1, data.applyIf(false) { this + 1 })
    }

    @Test
    fun applyIfLambda() {
        val data = 1
        assertEquals(2, data.applyIf({ it == 1 }) { this + 1 })
        assertEquals(1, data.applyIf({ it != 1 }) { this + 1 })
        val contract1: Int
        assertEquals(2, data.applyIf({ contract1 = it; contract1 == 1 }) { this + 1 })
        assertEquals(1, contract1)
        val contract2: Int
        assertEquals(1, data.applyIf({ contract2 = it; contract2 != 1 }) { this + 1 })
        assertEquals(1, contract2)
    }

    @Test
    fun applyIfNotNull() {
        val data = 1
        assertEquals(2, data.applyIfNotNull(1) { this + it })
        assertEquals(1, data.applyIfNotNull(null as Int?) { this + it })
    }

    @Test
    fun mapIf() {
        val data = listOf(1, 2, 3)
        assertEquals(listOf(1, 3, 3), data.mapIf({ it == 2 }) { it + 1 })
    }

    @Test
    fun mapIfIndexed() {
        val data = listOf(1, 2, 3)
        assertEquals(listOf(1, 3, 3), data.mapIfIndexed({ index, _ -> index == 1 }) { _, it -> it + 1 })
    }

    @Test
    fun replacedList() {
        val data = listOf(1, 2, 3)
        assertEquals(listOf(1, 2, 4), data.replaced(2, 4))
    }

    @Test
    fun replacedMap() {
        val data = mapOf(1 to 1, 2 to 2, 3 to 3)
        assertEquals(mapOf(1 to 1, 2 to 4, 3 to 3), data.replaced(2, 4))
    }

    @Test
    fun plusMinusInt() {
        assertEquals(-1..1, 0.plusMinus(1))
        assertEquals(1..5, (3).plusMinus(2))
        assertEquals(-4..2, (-1).plusMinus(3))
    }

    @Test
    fun plusMinusLong() {
        assertEquals(-1L..1L, 0L.plusMinus(1L))
        assertEquals(1L..5L, (3L).plusMinus(2L))
        assertEquals(-4L..2L, (-1L).plusMinus(3L))
    }

    @Test
    fun randomString() {
        val range = 'a'..'s'
        val result1 = range.randomString(100)
        val result2 = range.randomString(100)
        assertEquals(100, result1.length, result1)
        result1.forEach { assertContains(range, it) }
        assertEquals(100, result2.length, result1)
        result2.forEach { assertContains(range, it) }
        assertNotEquals(result1, result2)
    }

    @Test
    fun floatSecondToTimeString() {
        assertEquals("-", Float.NaN.secondToTimeString())
        assertEquals("-", Float.NEGATIVE_INFINITY.secondToTimeString())
        assertEquals("-", Float.POSITIVE_INFINITY.secondToTimeString())
        assertEquals("0:00.000", 0f.secondToTimeString())
        assertEquals("0:01.000", 1f.secondToTimeString())
        assertEquals("0:01.500", 1.5f.secondToTimeString())
        assertEquals("0:59.999", 59.9994f.secondToTimeString())
        assertEquals("1:00.000", 59.9999f.secondToTimeString())
        assertEquals("1:00.000", 60.0001f.secondToTimeString())
        assertEquals("3:05.001", 185.001f.secondToTimeString())
        assertEquals("1:03:05.700", 3785.7f.secondToTimeString())
        assertEquals("-0:01.000", (-1f).secondToTimeString())
        assertEquals("-1:03:05.700", (-3785.7f).secondToTimeString())
    }

    @Test
    fun doubleSecondToTimeString() {
        assertEquals("-", Double.NaN.secondToTimeString())
        assertEquals("-", Double.NEGATIVE_INFINITY.secondToTimeString())
        assertEquals("-", Double.POSITIVE_INFINITY.secondToTimeString())
        assertEquals("0:00.000", 0.0.secondToTimeString())
        assertEquals("0:01.000", 1.0.secondToTimeString())
        assertEquals("0:01.500", 1.5.secondToTimeString())
        assertEquals("0:59.999", 59.9994.secondToTimeString())
        assertEquals("1:00.000", 59.9999.secondToTimeString())
        assertEquals("1:00.000", 60.0001.secondToTimeString())
        assertEquals("3:05.001", 185.001.secondToTimeString())
        assertEquals("1:03:05.700", 3785.7.secondToTimeString())
        assertEquals("-0:01.000", (-1.0).secondToTimeString())
        assertEquals("-1:03:05.700", (-3785.7).secondToTimeString())
    }

    @Test
    fun intZeroPad() {
        assertEquals("0", 0.zeroPad(1))
        assertEquals("01", 1.zeroPad(2))
        assertEquals("15", 15.zeroPad(2))
    }

    @Test
    fun longZeroPad() {
        assertEquals("0", 0L.zeroPad(1))
        assertEquals("01", 1L.zeroPad(2))
        assertEquals("15", 15L.zeroPad(2))
    }

    @Test
    fun floatRoundToString() {
        assertEquals("-", Float.NaN.roundToString())
        assertEquals("0", 0f.roundToString(0))
        assertEquals("0", 0.123456f.roundToString(0))
        assertEquals("0.1", 0.123456f.roundToString(1))
        assertEquals("0.12", 0.123456f.roundToString(2))
        assertEquals("0.123", 0.123456f.roundToString(3))
        assertEquals("0.1235", 0.123456f.roundToString(4))
        assertEquals("0.12346", 0.123456f.roundToString(5))
    }

    @Test
    fun doubleRoundToString() {
        assertEquals("-", Double.NaN.roundToString())
        assertEquals("0", 0.0.roundToString(0))
        assertEquals("0", 0.123456.roundToString(0))
        assertEquals("0.1", 0.123456.roundToString(1))
        assertEquals("0.12", 0.123456.roundToString(2))
        assertEquals("0.123", 0.123456.roundToString(3))
        assertEquals("0.1235", 0.123456.roundToString(4))
        assertEquals("0.12346", 0.123456.roundToString(5))
    }

    @Test
    fun floatToPercentString() {
        assertEquals("-", Float.NaN.toPercentString())
        assertEquals("0%", 0f.toPercentString(0))
        assertEquals("12%", 0.123456f.toPercentString(0))
        assertEquals("12.3%", 0.123456f.toPercentString(1))
        assertEquals("12.35%", 0.123456f.toPercentString(2))
        assertEquals("12.346%", 0.123456f.toPercentString(3))
        assertEquals("12.3456%", 0.123456f.toPercentString(4))
    }

    @Test
    fun doubleToPercentString() {
        assertEquals("-", Double.NaN.toPercentString())
        assertEquals("0%", 0.0.toPercentString(0))
        assertEquals("12%", 0.123456.toPercentString(0))
        assertEquals("12.3%", 0.123456.toPercentString(1))
        assertEquals("12.35%", 0.123456.toPercentString(2))
        assertEquals("12.346%", 0.123456.toPercentString(3))
        assertEquals("12.3456%", 0.123456.toPercentString(4))
    }
}
