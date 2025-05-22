/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.web.page.graph.*
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.utility.Expression
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.replaced
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.browser.localStorage
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.max

class GraphViewModel(private val root: ViewModel) {

    companion object {
        private const val KEY_FACTOR_LIST = "umasim.graph.factorList"

        private const val tickLengthBase = 10

        private const val maxTickCount = 10

        private fun supportToGraphEntry(
            card: SupportCard,
            simulationResults: Map<Int, List<Map<String, Double>>>,
        ): GraphEntry? {
            val initialStatus = card.initialStatus(emptyList())
            val noSpecialUniqueCondition = WebConstants.noSpecialUniqueCondition(card)
            val withSpecialUniqueCondition = WebConstants.withSpecialUniqueCondition
            val simulationResult = simulationResults[card.id]?.get(card.talent) ?: return null
            return GraphEntry(
                simulationResult + mapOf(
                    "initialRelation" to card.initialRelation.toDouble(),
                    "initialSpeed" to initialStatus.speed.toDouble(),
                    "initialStamina" to initialStatus.stamina.toDouble(),
                    "initialPower" to initialStatus.power.toDouble(),
                    "initialGuts" to initialStatus.guts.toDouble(),
                    "initialWisdom" to initialStatus.wisdom.toDouble(),
                    "initialSkillPt" to initialStatus.skillPt.toDouble(),
                    "friend" to card.friendFactor(noSpecialUniqueCondition),
                    "friend2" to card.friendFactor(withSpecialUniqueCondition),
                    "motivation" to card.motivationFactor(noSpecialUniqueCondition).toDouble(),
                    "motivation2" to card.motivationFactor(withSpecialUniqueCondition).toDouble(),
                    "training" to card.trainingFactor(noSpecialUniqueCondition).toDouble(),
                    "training2" to card.trainingFactor(withSpecialUniqueCondition).toDouble(),
                    "speedBonus" to card.getBaseBonus(StatusType.SPEED, noSpecialUniqueCondition).toDouble(),
                    "speedBonus2" to card.getBaseBonus(StatusType.SPEED, withSpecialUniqueCondition).toDouble(),
                    "staminaBonus" to card.getBaseBonus(StatusType.STAMINA, noSpecialUniqueCondition).toDouble(),
                    "staminaBonus2" to card.getBaseBonus(StatusType.STAMINA, withSpecialUniqueCondition).toDouble(),
                    "powerBonus" to card.getBaseBonus(StatusType.POWER, noSpecialUniqueCondition).toDouble(),
                    "powerBonus2" to card.getBaseBonus(StatusType.POWER, withSpecialUniqueCondition).toDouble(),
                    "gutsBonus" to card.getBaseBonus(StatusType.GUTS, noSpecialUniqueCondition).toDouble(),
                    "gutsBonus2" to card.getBaseBonus(StatusType.GUTS, withSpecialUniqueCondition).toDouble(),
                    "wisdomBonus" to card.getBaseBonus(StatusType.WISDOM, noSpecialUniqueCondition).toDouble(),
                    "wisdomBonus2" to card.getBaseBonus(StatusType.WISDOM, withSpecialUniqueCondition).toDouble(),
                    "skillPtBonus" to card.getBaseBonus(StatusType.SKILL, noSpecialUniqueCondition).toDouble(),
                    "skillPtBonus2" to card.getBaseBonus(StatusType.SKILL, withSpecialUniqueCondition).toDouble(),
                    "race" to card.race.toDouble(),
                    "fan" to card.fan.toDouble(),
                    "specialityRate" to card.specialtyRate(0, noSpecialUniqueCondition).toDouble(),
                    "specialityRate2" to card.specialtyRate(0, withSpecialUniqueCondition).toDouble(),
                    "hintLevel" to card.hintLevel.toDouble(),
                    "hintFrequency" to card.hintFrequency,
                    "wisdomFriendRecovery" to card.wisdomFriendRecovery(noSpecialUniqueCondition).toDouble(),
                    "wisdomFriendRecovery2" to card.wisdomFriendRecovery(withSpecialUniqueCondition).toDouble(),
                )
            )
        }

    }

    private val state get() = root.state.graphState

    private fun updateState(update: GraphState.() -> GraphState) {
        root.state = root.state.copy(graphState = state.update())
    }

    init {
        val factorList = localStorage.getItem(KEY_FACTOR_LIST)?.let {
            Json.decodeFromString<List<GraphFactor>>(it)
        } ?: emptyList()
        val initialFactorList = factorList.map { factor ->
            val template = graphFactorTemplates.firstOrNull { it.id == factor.templateId } ?: graphFactorTemplates[0]
            factor.copy(template = template).updateExpression()
        }.ifEmpty {
            defaultGraphFactors.map { it.updateExpression() }
        }

        updateState { copy(factorList = initialFactorList) }
    }

    fun saveFactorList() {
        localStorage.setItem(KEY_FACTOR_LIST, Json.encodeToString(state.factorList))
    }

    fun setGraphTarget(target: GraphTarget) {
        updateState { copy(target = target, targetPath = target.path) }
        root.scope.launch {
            generateGraphData()
        }
    }

    suspend fun generateGraphData() {
        updateState { copy(loading = true, loadError = false) }
        val target = if (state.target.path == state.targetPath) state.target else {
            graphTargetCandidates.firstOrNull { it.path == state.targetPath } ?: graphTargetCandidates[0]
        }
        val simulationResults = loadCsv(target.path)
        if (simulationResults == null) {
            updateState { copy(loading = false, loadError = true) }
            return
        }
        val baseData = Store.supportList
            .filter { it.type == target.type && it.rarity >= 2 }
            .groupBy { it.id }
            .mapNotNull { (id, cardList) ->
                val entries = cardList.sortedBy { it.talent }.mapNotNull { supportToGraphEntry(it, simulationResults) }
                if (entries.isEmpty()) return@mapNotNull null
                GraphRowData(id, cardList.first().name, entries)
            }
            .sortedByDescending { it.id }

        updateState {
            copy(
                loading = false,
                target = target,
                baseData = baseData,
            ).calcDisplayData()
        }
    }

    private suspend fun loadCsv(path: String): Map<Int, List<Map<String, Double>>>? = kotlin.runCatching {
        val text = HttpClient(Js)
            .get("https://mee1080.github.com/umasim/data/simulation/$path.csv")
            .bodyAsText()
        val lines = text.split("\n")
        val result = mutableMapOf<Int, MutableList<Map<String, Double>>>()
        for (line in lines) {
            val data = line.trim().split(",")
            if (data.size < 9) continue
            val id = data[0].toInt()
            val index = data[1].toInt()
            val list = result.getOrPut(id) { MutableList(5) { emptyMap() } }
            list[index] = mapOf(
                "speed" to data[2].toDouble(),
                "stamina" to data[3].toDouble(),
                "power" to data[4].toDouble(),
                "guts" to data[5].toDouble(),
                "wisdom" to data[6].toDouble(),
                "skillPt" to data[7].toDouble(),
                "totalHintLevel" to data[8].toDouble(),
            )
        }
        result
    }.onFailure { it.printStackTrace() }.getOrNull()

    fun addGraphFactor() {
        updateState {
            copy(factorList = factorList + GraphFactor())
        }
    }

    fun deleteGraphFactor(index: Int) {
        updateState {
            copy(factorList = factorList.filterIndexed { i, _ -> i != index }).calcDisplayData()
        }
    }

    fun updateCoefficient(index: Int, value: Double) {
        updateState {
            copy(
                factorList = factorList.replaced(index) {
                    it.copy(coefficient = value)
                }
            ).calcDisplayData()
        }
    }

    fun updateExpression(index: Int, expressionInput: String) {
        updateState {
            copy(factorList = factorList.replaced(index) {
                it.copy(expressionInput = expressionInput).updateExpression()
            }).calcDisplayData()
        }
    }

    fun selectFactorTemplate(index: Int, template: GraphFactorTemplate) {
        updateState {
            copy(factorList = factorList.replaced(index) {
                it.copy(templateId = template.id, template = template).updateExpression()
            }).calcDisplayData()
        }
    }

    private fun GraphFactor.updateExpression(): GraphFactor {
        return if (template.isManualInput) {
            if (expressionInput.trim().isEmpty()) {
                copy(expressionError = "", expression = null)
            } else {
                var expressionError = ""
                val expression = Expression.parseOrNull(expressionInput) { error ->
                    expressionError = error.message ?: ""
                }?.let { parsedExpression ->
                    val testData =
                        state.baseData.firstOrNull()?.data?.firstOrNull()?.params ?: return@let parsedExpression
                    val check = parsedExpression.calcOrNull(testData) {
                        expressionError = it.message ?: ""
                    }
                    if (check == null) null else parsedExpression
                }
                copy(expressionError = expressionError, expression = expression)
            }
        } else {
            copy(expressionError = "", expression = template.expression)
        }
    }

    private fun GraphState.calcDisplayData(): GraphState {
        val values = baseData.filter { it.visible }.map { data ->
            data to data.data.map { entry ->
                factorList.sumOf { factor ->
                    calc(entry, factor)
                }
            }
        }
        val maxValue = max(tickLengthBase.toDouble(), values.maxOfOrNull { it.second.max() } ?: 0.0)
        val minValue = max(0.0, values.minOfOrNull { it.second.first() } ?: 0.0)
        val tickLength = ((maxValue - minValue) / maxTickCount / tickLengthBase + 1).toInt() * tickLengthBase
        val maxTick = (maxValue / tickLength + 1.0).toInt() * tickLength
        val minTick = (minValue / tickLength).toInt() * tickLength
        val tickCount = (maxTick - minTick) / tickLength

        val displayData = values.map { (data, value) ->
            GraphRowDisplayData(
                data,
                value.map { (it - minTick) / (maxTick - minTick) },
            )
        }.applyIf(sortOrder == GraphSortOrder.VALUE) {
            sortedByDescending { it.rates.max() }
        }
        val labels = List(tickCount + 1) { (minTick + it * tickLength).toString() }

        return copy(displayData = displayData, labels = labels)
    }

    private fun calc(data: GraphEntry, factor: GraphFactor): Double {
        if (factor.coefficient == 0.0 || factor.expression == null) {
            return 0.0
        }
        return (factor.expression.calcOrNull(data.params) ?: 0.0) * factor.coefficient
    }

    fun openGraphDataDialog(data: GraphRowDisplayData, index: Int) {
        updateState {
            copy(dialogData = data.data to index)
        }
    }

    fun closeGraphDialog() {
        updateState {
            copy(dialogData = null, filterDialog = false)
        }
    }

    fun toggleGraphVisibility(index: Int) {
        updateState {
            copy(baseData = baseData.replaced(index) { it.copy(visible = !it.visible) }).calcDisplayData()
        }
    }

    fun openGraphFilterDialog() {
        updateState {
            copy(filterDialog = true)
        }
    }

    fun setGraphSortOrder(sortOrder: GraphSortOrder) {
        updateState {
            copy(sortOrder = sortOrder).calcDisplayData()
        }
    }

}