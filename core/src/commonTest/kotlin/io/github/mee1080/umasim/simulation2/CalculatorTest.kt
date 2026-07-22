/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.test.loadTestStore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CalculatorTest {

    @BeforeTest
    fun setUp() {
        loadTestStore()
    }

    @Test
    fun testEventTrainingEffect() {
        val baseState = Simulator(
            scenario = Scenario.URA,
            chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
            supportCardList = Store.getSupportByName("[一杯のノスタルジア]駿川たづな" to 4)
        ).initialState

        val infoDefault = baseState.baseCalcInfo.copy(
            training = baseState.training[0].current, // SPEED training
            eventTrainingEffect = 0
        )
        val resultDefault = Calculator.calcTrainingSuccessStatus(infoDefault)

        val infoWithEffect = infoDefault.copy(
            eventTrainingEffect = 20
        )
        val resultWithEffect = Calculator.calcTrainingSuccessStatus(infoWithEffect)

        // Check that training with eventTrainingEffect = 20 has strictly higher speed gain than default
        println("Default Speed Gain: ${resultDefault.speed}")
        println("With Effect Speed Gain: ${resultWithEffect.speed}")
        assertTrue(resultWithEffect.speed > resultDefault.speed, "Speed gain should be higher when eventTrainingEffect is 20")
    }
}
