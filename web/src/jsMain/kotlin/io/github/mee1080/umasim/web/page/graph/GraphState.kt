package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
data class GraphState(
    val baseData: List<GraphRowData> = emptyList(),
    val factorList: List<GraphFactor> = emptyList(),
    val displayData: List<GraphRowDisplayData> = emptyList(),
    val labels: List<String> = emptyList(),
    val dialogData: Pair<GraphRowData, Int>? = null,
    val filterDialog: Boolean = false,
    val sortOrder: GraphSortOrder = GraphSortOrder.ID,
)

enum class GraphSortOrder(val displayName: String) {
    ID("ID順"),
    VALUE("評価値順"),
}

@Immutable
data class GraphRowData(
    val id: Int,
    val label: String,
    val data: List<GraphEntry>,
    val visible: Boolean = true,
)

@Immutable
data class GraphEntry(
    val params: Map<String, Double> = emptyMap(),
)

@Immutable
data class GraphRowDisplayData(
    val data: GraphRowData,
    val rates: List<Double>,
) {
    val label get() = data.label
}

@Serializable
data class GraphFactor(
    val coefficient: Double = 1.0,
    val expressionInput: String = "",
    val expressionError: String = "",
    val templateId: Int = 0,
    @Transient
    val expression: Expression? = null,
    @Transient
    val template: GraphFactorTemplate = graphFactorTemplates[0],
)

private fun defaultGraphFactor(coefficient: Double, expressionInput: String): GraphFactor {
    return GraphFactor(coefficient, expressionInput, expression = Expression.parseOrNull(expressionInput))
}

@Immutable
data class GraphFactorTemplate(
    val id: Int,
    val name: String,
    val expressionInput: String,
) {
    val isManualInput = expressionInput.isEmpty()

    val expression by lazy {
        Expression.parseOrNull(expressionInput)
    }
}

val graphFactorTemplates = listOf(
    GraphFactorTemplate(0, "手入力", ""),
    GraphFactorTemplate(1, "a", "a"),
    GraphFactorTemplate(2, "b", "b"),
    GraphFactorTemplate(3, "c", "c"),
    GraphFactorTemplate(4, "d", "d"),
    GraphFactorTemplate(5, "e", "e"),
    GraphFactorTemplate(6, "aとbの積", "a*b"),
)

val factorA = defaultGraphFactor(1.0, "a")
val factorB = defaultGraphFactor(1.0, "b")
val factorC = defaultGraphFactor(1.0, "c")
val factorD = defaultGraphFactor(1.0, "d")
val factorE = defaultGraphFactor(1.0, "e")
