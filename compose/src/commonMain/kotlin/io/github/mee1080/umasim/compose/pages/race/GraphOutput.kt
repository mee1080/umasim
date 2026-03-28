package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot2
import io.github.koalaplot.core.line.LinePlot2
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.*
import io.github.mee1080.umasim.compose.common.atoms.LabeledCheckbox
import io.github.mee1080.umasim.compose.common.atoms.TooltipSurface
import io.github.mee1080.umasim.compose.common.parts.WithTooltip
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.GraphData
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setGraphDisplaySetting
import kotlin.math.max
import kotlin.math.min

@Composable
fun GraphOutput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val graphData = state.graphData ?: return
    GraphArea(state, graphData, dispatch)
}

private val defaultLegends = listOf(
    "速度" to Color.Blue,
    "耐力" to Color(255, 128, 100),
    "走行レーン" to Color.Green,
)

private val virtualLegends = defaultLegends + listOf(
    "先頭との差" to Color(0, 255, 255),
)

@OptIn(ExperimentalKoalaPlotApi::class, ExperimentalLayoutApi::class)
@Composable
private fun GraphArea(state: AppState, graphData: GraphData, dispatch: OperationDispatcher<AppState>) {
    val frameList = graphData.frameList
    Column {
        Text("直近レース詳細", style = MaterialTheme.typography.headlineSmall)
        var verticalZoom by remember { mutableStateOf(false) }
        LabeledCheckbox(verticalZoom, { verticalZoom = it }) {
            Text("スキル数に応じて縦方向に拡大")
        }
        val height by derivedStateOf { if (verticalZoom) max(520, graphData.skillData.size * 34) else 520 }
        ChartLayout(
            modifier = Modifier.height(height.dp),
            legend = {
                val legends = if (graphData.paceMakerData.isEmpty()) defaultLegends else virtualLegends
                FlowLegend(
                    itemCount = legends.size,
                    symbol = { Symbol(shape = RectangleShape, fillBrush = SolidColor(legends[it].second)) },
                    label = { Text(legends[it].first) },
                    modifier = Modifier.border(1.dp, Color.Black).padding(8.dp)
                )
            },
            legendLocation = LegendLocation.BOTTOM,
        ) {
            val xMax = (frameList.size - 1) / 15f
            XYGraph(
                xAxisModel = rememberFloatLinearAxisModel(0f..xMax),
                yAxisModel = rememberFloatLinearAxisModel(0f..1f),
                yAxisLabels = { "" },
            ) {
                AreaPlot2(
                    data = graphData.straightData.map { Point(it.first, it.second) },
                    areaBaseline = AreaBaseline.ConstantLine(0f),
                    areaStyle = AreaStyle(SolidColor(Color(0, 128, 255)), 0.15f),
                )
                AreaPlot2(
                    data = graphData.cornerData.map { Point(it.first, it.second) },
                    areaBaseline = AreaBaseline.ConstantLine(0f),
                    areaStyle = AreaStyle(SolidColor(Color(128, 0, 255)), 0.15f),
                )
                AreaPlot2(
                    data = graphData.upSlopeData.map { Point(it.first, it.second + 0.1f) },
                    areaBaseline = AreaBaseline.ConstantLine(0.1f),
                    areaStyle = AreaStyle(SolidColor(Color(0, 255, 128)), 0.15f),
                )
                AreaPlot2(
                    data = graphData.downSlopeData.map { Point(it.first, it.second + 0.1f) },
                    areaBaseline = AreaBaseline.ConstantLine(0.1f),
                    areaStyle = AreaStyle(SolidColor(Color(255, 255, 0)), 0.15f),
                )
                if (graphData.phase0Start > 0.0) {
                    VerticalLineAnnotation(
                        graphData.phase0Start,
                        LineStyle(SolidColor(Color.Black), 1.dp, alpha = 0.8f),
                    )
                }
                VerticalLineAnnotation(graphData.phase1Start, LineStyle(SolidColor(Color.Black), 1.dp, alpha = 0.8f))
                VerticalLineAnnotation(graphData.phase2Start, LineStyle(SolidColor(Color.Black), 1.dp, alpha = 0.8f))
                HorizontalLineAnnotation(graphData.staminaZero, LineStyle(SolidColor(Color.Black), 1.dp, alpha = 0.8f))
                LinePlot2(
                    data = graphData.laneData.map { Point(it.first, it.second) },
                    lineStyle = LineStyle(SolidColor(Color.Green), 2.dp),
                )
                if (graphData.paceMakerData.isNotEmpty()) {
                    LinePlot2(
                        data = graphData.paceMakerData.map { Point(it.first, it.second) },
                        lineStyle = LineStyle(SolidColor(Color(0, 255, 255)), 2.dp),
                    )
                }
                LinePlot2(
                    data = graphData.speedData.map { Point(it.first, it.second) },
                    lineStyle = LineStyle(SolidColor(Color.Blue), 2.dp),
                )
                LinePlot2(
                    data = graphData.staminaData.map { Point(it.first, it.second) },
                    lineStyle = LineStyle(SolidColor(Color(255, 128, 100)), 2.dp),
                )
                LinePlot2(
                    data = graphData.staminaOverData.map { Point(it.first, it.second) },
                    lineStyle = LineStyle(SolidColor(Color.Red), 2.dp),
                )
                XYAnnotation(Point(0f, 0.05f), AnchorPoint.LeftMiddle) {
                    TooltipSurface(containerColor = Color(0, 0, 0, 128)) {
                        Text("直線(青)/コーナー(紫)")
                    }
                }
                XYAnnotation(Point(0f, 0.15f), AnchorPoint.LeftMiddle) {
                    TooltipSurface(containerColor = Color(0, 0, 0, 128)) {
                        Text("上り坂(緑)/下り坂(黄)")
                    }
                }
                if (graphData.skillData.isNotEmpty()) {
                    val skillMargin = min(0.1f, 0.8f / graphData.skillData.size)
                    graphData.skillData.forEachIndexed { index, skill ->
                        val top = 1f - skillMargin * index
                        XYAnnotation(Point(skill.start, top), AnchorPoint.TopLeft) {
                            WithTooltip(
                                tooltip = {
                                    Text(skill.description)
                                }
                            ) {
                                TooltipSurface(containerColor = Color(0, 0, 0, 128)) {
                                    Text(skill.name)
                                }
                            }
                        }
                        if (skill.end != null) {
                            LinePlot2(
                                data = listOf(Point(skill.start, top - 0.003f), Point(skill.end, top - 0.003f)),
                                lineStyle = LineStyle(SolidColor(Color.Red), 2.dp),
                            )
                        }
                    }
                }
            }
        }
        val setting = state.graphDisplaySetting
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LabeledCheckbox(setting.skill, { dispatch(setGraphDisplaySetting(setting.copy(skill = it))) }) {
                Text("スキル")
            }
            LabeledCheckbox(setting.temptation, { dispatch(setGraphDisplaySetting(setting.copy(temptation = it))) }) {
                Text("掛かり")
            }
            LabeledCheckbox(setting.spurting, { dispatch(setGraphDisplaySetting(setting.copy(spurting = it))) }) {
                Text("スパート開始")
            }
            LabeledCheckbox(setting.paceDownMode, { dispatch(setGraphDisplaySetting(setting.copy(paceDownMode = it))) }) {
                Text("ペースダウンモード")
            }
            LabeledCheckbox(setting.downSlopeMode, { dispatch(setGraphDisplaySetting(setting.copy(downSlopeMode = it))) }) {
                Text("下り坂モード")
            }
            LabeledCheckbox(setting.leadCompetition, { dispatch(setGraphDisplaySetting(setting.copy(leadCompetition = it))) }) {
                Text("位置取り争い")
            }
            LabeledCheckbox(setting.competeFight, { dispatch(setGraphDisplaySetting(setting.copy(competeFight = it))) }) {
                Text("追い比べ")
            }
            LabeledCheckbox(setting.conservePower, { dispatch(setGraphDisplaySetting(setting.copy(conservePower = it))) }) {
                Text("脚色十分")
            }
            LabeledCheckbox(setting.positionCompetition, { dispatch(setGraphDisplaySetting(setting.copy(positionCompetition = it))) }) {
                Text("位置取り調整")
            }
            LabeledCheckbox(setting.staminaKeep, { dispatch(setGraphDisplaySetting(setting.copy(staminaKeep = it))) }) {
                Text("持久力温存")
            }
            LabeledCheckbox(setting.secureLead, { dispatch(setGraphDisplaySetting(setting.copy(secureLead = it))) }) {
                Text("リード確保")
            }
            LabeledCheckbox(setting.staminaLimitBreak, { dispatch(setGraphDisplaySetting(setting.copy(staminaLimitBreak = it))) }) {
                Text("スタミナ勝負")
            }
            LabeledCheckbox(setting.fullSpurt, { dispatch(setGraphDisplaySetting(setting.copy(fullSpurt = it))) }) {
                Text("全開スパート")
            }
        }
    }
}