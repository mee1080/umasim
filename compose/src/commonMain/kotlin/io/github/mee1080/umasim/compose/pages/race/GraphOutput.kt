package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.*
import io.github.mee1080.umasim.compose.common.atoms.TooltipSurface
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.GraphData
import kotlin.math.min

@Composable
fun GraphOutput(state: AppState) {
    val graphData = state.graphData ?: return
    GraphArea(graphData)
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun GraphArea(graphData: GraphData) {
    val frameList = graphData.frameList
    Column {
        Text("直近レース詳細", style = MaterialTheme.typography.headlineSmall)
        XYGraph(
            xAxisModel = rememberLinearAxisModel(0f..(frameList.size - 1) / 15f),
            yAxisModel = rememberLinearAxisModel(0f..1f),
            yAxisLabels = { "" },
            modifier = Modifier.height(400.dp),
        ) {
            AreaPlot(
                data = graphData.straightData.map { Point(it.first, it.second) },
                areaBaseline = AreaBaseline.ConstantLine(0f),
                areaStyle = AreaStyle(SolidColor(Color(0, 128, 255)), 0.15f),
            )
            AreaPlot(
                data = graphData.cornerData.map { Point(it.first, it.second) },
                areaBaseline = AreaBaseline.ConstantLine(0f),
                areaStyle = AreaStyle(SolidColor(Color(128, 0, 255)), 0.15f),
            )
            AreaPlot(
                data = graphData.upSlopeData.map { Point(it.first, it.second + 0.1f) },
                areaBaseline = AreaBaseline.ConstantLine(0.1f),
                areaStyle = AreaStyle(SolidColor(Color(0, 255, 128)), 0.15f),
            )
            AreaPlot(
                data = graphData.downSlopeData.map { Point(it.first, it.second + 0.1f) },
                areaBaseline = AreaBaseline.ConstantLine(0.1f),
                areaStyle = AreaStyle(SolidColor(Color(255, 255, 0)), 0.15f),
            )
            LinePlot(
                data = graphData.speedData.map { Point(it.first, it.second) },
                lineStyle = LineStyle(SolidColor(Color.Blue), 2.dp),
            )
            LinePlot(
                data = graphData.staminaData.map { Point(it.first, it.second) },
                lineStyle = LineStyle(SolidColor(Color(255, 128, 100)), 2.dp),
            )
            LinePlot(
                data = graphData.staminaOverData.map { Point(it.first, it.second) },
                lineStyle = LineStyle(SolidColor(Color.Red), 2.dp),
            )
            XYAnnotation(Point(0f, 0.05f), AnchorPoint.LeftMiddle) {
                TooltipSurface {
                    Text("直線(青)/コーナー(紫)")
                }
            }
            XYAnnotation(Point(0f, 0.15f), AnchorPoint.LeftMiddle) {
                TooltipSurface {
                    Text("上り坂(緑)/下り坂(黄)")
                }
            }
            if (graphData.skillData.isNotEmpty()) {
                val skillMargin = min(0.1f, 0.8f / graphData.skillData.size)
                graphData.skillData.forEachIndexed { index, (position, name) ->
                    XYAnnotation(Point(position, 1f - skillMargin * index), AnchorPoint.TopLeft) {
                        TooltipSurface {
                            Text(name)
                        }
                    }
                }
            }
        }
    }
}