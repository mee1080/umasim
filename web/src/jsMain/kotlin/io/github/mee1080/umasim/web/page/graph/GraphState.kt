package io.github.mee1080.umasim.web.page.graph

import androidx.compose.runtime.Immutable
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.utility.Expression
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
data class GraphState(
    val loading: Boolean = true,
    val loadError: Boolean = false,
    val targetPath: String = graphTargetCandidates.first().path,
    val target:GraphTarget = graphTargetCandidates.first(),
    val baseData: List<GraphRowData> = emptyList(),
    val factorList: List<GraphFactor> = emptyList(),
    val displayData: List<GraphRowDisplayData> = emptyList(),
    val labels: List<String> = emptyList(),
    val dialogData: Pair<GraphRowData, Int>? = null,
    val filterDialog: Boolean = false,
    val sortOrder: GraphSortOrder = GraphSortOrder.ID,
)

data class GraphTarget(
    val path: String,
    val type: StatusType,
    val displayName: String,
)

val graphTargetCandidates = listOf(
    GraphTarget("legend_r_speed", StatusType.SPEED, "スピード 伝説赤スピ2スタ2賢さ1"),
    GraphTarget("legend_r_stamina", StatusType.STAMINA, "スタミナ 伝説赤スピ2スタ2賢さ1"),
    GraphTarget("legend_r_wisdom", StatusType.WISDOM, "賢さ 伝説赤スピ2スタ2賢さ1"),

    GraphTarget("speed_20241130", StatusType.SPEED, "スピード メカスピ2スタ2パワ1賢さ1"),
    GraphTarget("power_20241227", StatusType.POWER, "パワー メカスピ2スタ2パワ1賢さ1"),
    GraphTarget("guts_20241210", StatusType.GUTS, "根性 畑スピ1パワ1根性2賢さ1"),
    GraphTarget("wisdom_20241202", StatusType.WISDOM, "賢さ メカスピ2スタ2パワ1賢さ1"),
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

    GraphFactorTemplate(21, "スピード得意性能", "speed + power + skillPt"),
    GraphFactorTemplate(31, "スタミナ得意性能", "stamina + guts + skillPt"),
    GraphFactorTemplate(41, "パワー得意性能", "power + stamina + skillPt"),
    GraphFactorTemplate(51, "根性得意性能", "guts + speed + power + skillPt"),
    GraphFactorTemplate(61, "賢さ得意性能", "wisdom + speed + skillPt"),
)

val defaultGraphFactors = List(7) {
    val template = graphFactorTemplates[it + 1]
    GraphFactor(templateId = template.id, template = template)
}
