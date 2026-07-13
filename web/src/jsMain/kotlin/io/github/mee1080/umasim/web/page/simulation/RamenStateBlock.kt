package io.github.mee1080.umasim.web.page.simulation

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenStatus
import io.github.mee1080.umasim.simulation2.*
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

@Composable
fun RamenParamInfo(state: SimulationState, action: Action) {
    val ramenStatus = state.ramenStatus ?: return
    val param = action.candidates.firstOrNull { it.first.success }
        ?.first?.scenarioActionParam as? RamenActionParam ?: return
    Div {
        val list = buildList {
            add("麺+${param.noodleGauge} (${ramenStatus.noodle + param.noodleGauge})")
            add("スープ+${param.soupGauge} (${ramenStatus.soup + param.soupGauge})")
            add("トッピング+${param.toppingGauge} (${ramenStatus.topping + param.toppingGauge})")
        }
        Text(list.joinToString(", "))
    }
}

@Composable
fun RamenSelectRegionInfo(action: RamenSelectRegion) {
    val reg = action.region
    Div {
        Text("必要コツ - 麺: ${reg.noodle}, スープ: ${reg.soup}, トッピング: ${reg.topping}")
    }
    RamenRegionInfo(reg)
}

@Composable
fun RamenTastingInfo(action: RamenTasting) {
    if (action.changeHiddenTips.isNotEmpty()) {
        Div {
            Text("隠し味変更: ${action.changeHiddenTips.joinToString(", ") { it.displayName }}")
        }
    }
    Div {
        Text("消費コツ - 麺: ${action.useNoodle}, スープ: ${action.useSoup}, トッピング: ${action.useTopping}")
    }
    RamenRegionInfo(action.region)
}

@Composable
fun RamenRegionInfo(reg: RamenRegion) {
    Div {
        val effects = buildList {
            if (reg.targetTypes.isNotEmpty()) add("対象：${reg.targetTypes.joinToString("/") { it.displayName }}")
            if (reg.trainingEffect > 0) add("トレーニング効果: +${reg.trainingEffect}%")
            if (reg.skillPtTrainingEffect > 0) add("スキルPt効果: +${reg.skillPtTrainingEffect}%")
            if (reg.friendBonus > 0) add("友情ボーナス: +${reg.friendBonus}%")
            if (reg.hintCount > 0) add("ヒント獲得数: +${reg.hintCount}")
            if (reg.addMember > 0) add("サポカ追加配置: +${reg.addMember}")
            if (reg.targetAll) add("全トレーニング対象")
            if (reg.targetStatusLimitOver > 0) add("上限突破: +${reg.targetStatusLimitOver}")
            if (reg.hintSkill.isNotEmpty()) add("スキルヒント: ${reg.hintSkill}")
        }
        Text("試食効果：${effects.joinToString(", ")}")
    }
}