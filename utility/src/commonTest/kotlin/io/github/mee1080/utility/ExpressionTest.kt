package io.github.mee1080.utility

import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionTest {

    @Test
    fun calcOrNull() {
        assertEquals(4, Expression("2+2").calc().toInt())
        assertEquals(10, Expression("2+2*4").calc().toInt())
        assertEquals(18, Expression("2+2*4*2").calc().toInt())
        assertEquals(32, Expression("(2+2)*4*2").calc().toInt())
        assertEquals(20, Expression("(2+2*4)*2").calc().toInt())
    }
}