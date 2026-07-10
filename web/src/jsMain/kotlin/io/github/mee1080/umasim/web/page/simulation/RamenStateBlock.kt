package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.ramen.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun RamenStateBlock(ramenStatus: RamenStatus) {
    Div {
        Text("調理Pt: ${ramenStatus.excitementPt} / ${ramenStatus.targetExcitePt} (地区ランク: ${ramenStatus.regionRank}、ランクボーナス: +${ramenStatus.regionRankBonus}%)")
    }
    Div {
        val regionsText = if (ramenStatus.selectedRegions.isEmpty()) {
            "未選択"
        } else {
            ramenStatus.selectedRegions.joinToString(" -> ") { it.displayName }
        }
        Text("選択中の地区: $regionsText")
    }
    Div {
        val gaugesText = RamenTipType.entries.filter { it != RamenTipType.HIDDEN }.joinToString(", ") {
            "${it.displayName}: ${ramenStatus.gauges[it] ?: 0}/7"
        }
        Text("ゲージ状況 - $gaugesText")
    }
    Div {
        val tipsText = RamenTipType.entries.joinToString(", ") {
            "${it.displayName}: ${ramenStatus.tips[it] ?: 0}"
        }
        Text("所持している調理のコツ - $tipsText")
    }
    if (ramenStatus.tipHistory.isNotEmpty()) {
        Div {
            Text("コツ獲得履歴: " + ramenStatus.tipHistory.joinToString(" -> ") { it.displayName })
        }
    }
    Div {
        val activeTastingText = ramenStatus.activeTastingRegion?.let { (region, bonus) ->
            "${region.displayName} (ランクボーナス: +${bonus}%)"
        } ?: "なし"
        Text("発動中の試食効果: $activeTastingText")
    }
    if (ramenStatus.trainingTip.isNotEmpty()) {
        Div {
            val trainingTipText = ramenStatus.trainingTip.entries.joinToString(", ") {
                "${it.key.displayName}: ${it.value.displayName}"
            }
            Text("トレーニングコツ配置: $trainingTipText")
        }
    }
    Div {
        val rmj = ramenStatus.rmjBonus
        Text("RMJボーナス - トレーニング効果: +${rmj.trainingEffect}%, 友情ボーナス: +${rmj.friendBonus}%, 得意率アップ: +${rmj.specialityRateUp}, ヒント発生率アップ: +${rmj.hintRateUp}%")
    }
}
