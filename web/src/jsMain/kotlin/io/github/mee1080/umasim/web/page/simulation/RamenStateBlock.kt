package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.ramen.RamenStatus
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun RamenStateBlock(ramenStatus: RamenStatus) {
    Div {
        val text = buildString {
            append("地域選択: ${ramenStatus.selectedRegions.joinToString(",") { it.regionName }}")
            append("(${ramenStatus.baseGauge.noodleGauge}/${ramenStatus.baseGauge.soupGauge}/${ramenStatus.baseGauge.toppingGauge})")
            append(", 盛り上がりPt: ${ramenStatus.excitementPt}/${ramenStatus.targetExcitePt} (ボーナス ${ramenStatus.regionRank}段階)")
        }
        Text(text)
    }
    Div {
        val text = buildString {
            append("ゲージ: ${ramenStatus.gauges.values.joinToString("/")}")
            append(", コツ: ${ramenStatus.tips.values.joinToString("/")}")
            append(", 隠し味: ${ramenStatus.hiddenTips}")
        }
        Text(text)
    }
    ramenStatus.activeTastingRegion?.let {
        Text("試食会: ${it.first.displayName}")
    }
}
