package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Immutable
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.utility.Expression
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Immutable
data class GraphState(
    val loading: Boolean = true,
    val loadError: Boolean = false,
    val targetCandidates: List<GraphTarget> = emptyList(),
    val targetPath: String = "",
    val target: GraphTarget = GraphTarget("", StatusType.SPEED, ""),
    val baseData: List<GraphRowData> = emptyList(),
    val factorList: List<GraphFactor> = emptyList(),
    val displayData: List<GraphRowDisplayData> = emptyList(),
    val labels: List<String> = emptyList(),
    val dialogData: Pair<GraphRowData, Int>? = null,
    val filterDialog: Boolean = false,
    val sortOrder: GraphSortOrder = GraphSortOrder.ID,
)

@Suppress("unused")
@Serializable
data class GraphTarget(
    val path: String,
    val type: StatusType,
    val displayName: String,
) {
    companion object {
        fun fromJson(json: String) = Json.decodeFromString<GraphTarget>(json)
        fun fromJsonList(json: String) = Json.decodeFromString<List<GraphTarget>>(json)
    }

    fun toJson() = Json.encodeToString(this)
}

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

    GraphFactorTemplate(1, "スピード", "speed"),
    GraphFactorTemplate(2, "スタミナ", "stamina"),
    GraphFactorTemplate(3, "パワー", "power"),
    GraphFactorTemplate(4, "根性", "guts"),
    GraphFactorTemplate(5, "賢さ", "wisdom"),
    GraphFactorTemplate(6, "スキルPt", "skillPt"),
    GraphFactorTemplate(7, "合計ヒントLv", "totalHintLevel"),

    GraphFactorTemplate(11, "5ステSP合計", "speed + stamina + power + guts + wisdom + skillPt"),
    GraphFactorTemplate(
        12,
        "5ステSP合計+ヒント×5",
        "speed + stamina + power + guts + wisdom + skillPt + totalHintLevel * 5"
    ),
    GraphFactorTemplate(
        13,
        "5ステ合計+SP×2+ヒント×5",
        "speed + stamina + power + guts + wisdom + skillPt * 2 + totalHintLevel * 5"
    ),

    GraphFactorTemplate(21, "スピード得意性能", "speed + power + skillPt"),
    GraphFactorTemplate(31, "スタミナ得意性能", "stamina + guts + skillPt"),
    GraphFactorTemplate(41, "パワー得意性能", "power + stamina + skillPt"),
    GraphFactorTemplate(51, "根性得意性能", "guts + speed + power + skillPt"),
    GraphFactorTemplate(61, "賢さ得意性能", "wisdom + speed + skillPt"),
)

val defaultGraphFactors = listOf(
    GraphFactor(templateId = 13, template = graphFactorTemplates.first { it.id == 13 })
)
