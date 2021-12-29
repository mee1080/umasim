package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.web.components.GroupedSelect
import io.github.mee1080.umasim.web.components.LabeledCheckbox
import io.github.mee1080.umasim.web.components.LabeledRadioGroup
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.onClickOrTouch
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.unsetWidth
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.roundToInt

@Composable
fun RootPage(model: ViewModel, state: State) {
    H2 { Text("育成シナリオ") }
    LabeledRadioGroup("scenario", "シナリオ：", WebConstants.scenarioList, state.selectedScenario, model::updateScenario)
    H2 { Text("育成キャラ") }
    LabeledSelect("", WebConstants.displayCharaList, state.selectedChara, model::updateChara)
    H2 { Text("サポートカード") }
    Div({ style { paddingBottom(16.px) } }) {
        TextInput(state.supportFilter) {
            placeholder("カード名、スキルヒントでフィルタ (空白区切りでAnd検索)")
            size(60)
            onInput { model.updateSupportFilter(it.value) }
        }
        Button({
            if (state.supportFilterApplied) {
                disabled()
            }
            onClickOrTouch { model.applyFilter() }
        }) { Text("フィルタ適用") }
    }
    Div {
        (0..1).forEach { row ->
            Div({ classes(AppStyle.supportCardArea) }) {
                state.supportSelectionList.slice((row * 3)..(row * 3 + 2)).forEachIndexed { offset, item ->
                    val index = row * 3 + offset
                    Div({
                        when (item.card?.type) {
                            StatusType.SPEED -> rgb(69, 196, 255)
                            StatusType.STAMINA -> rgb(255, 144, 127)
                            StatusType.POWER -> rgb(255, 185, 21)
                            StatusType.GUTS -> rgb(255, 144, 186)
                            StatusType.WISDOM -> rgb(32, 216, 169)
                            StatusType.FRIEND -> rgb(255, 211, 108)
                            else -> null
                        }?.let {
                            style {
                                background("linear-gradient(170deg, #ffffff00, #ffffff00 70%, $it)")
                            }
                        }
                    }) {
                        GroupedSelect(
                            "",
                            state.getSupportSelection(index),
                            item.selectedSupport,
                            {
                                classes(AppStyle.supportCard)
                                if (state.isFriendTraining(index)) {
                                    classes(AppStyle.friendSupportCard)
                                }
                            },
                            { model.updateSupport(index, it) },
                        ) {
                            Div({ classes("after") })
                        }
                        LabeledRadioGroup(
                            "talent$index",
                            "上限解放：",
                            WebConstants.supportTalentList,
                            item.supportTalent,
                        ) { model.updateSupportTalent(index, it) }
                        Div {
                            LabeledCheckbox("join$index", "練習参加", item.join) { model.updateJoin(index, it) }
                            LabeledCheckbox("friend$index", "友情", item.friend) { model.updateFriend(index, it) }
                        }
                    }
                }
            }
        }
    }
    if (state.scenario == Scenario.AOHARU) {
        H3 { Text("サポカ外参加人数") }
        Div {
            Button({ onClickOrTouch { model.updateTeamJoinCount(-1) } }) { Text("-") }
            Span({ style { padding(8.px) } }) { Text(state.teamJoinCount.toString()) }
            Button({ onClickOrTouch { model.updateTeamJoinCount(1) } }) { Text("+") }
        }
    }
    H2 { Text("トレーニング上昇量") }
    LabeledRadioGroup("motivation", "やる気：", WebConstants.motivationList, state.motivation, model::updateMotivation)
    LabeledRadioGroup(
        "training",
        "種別　：",
        WebConstants.displayTrainingTypeList,
        state.selectedTrainingType,
        model::updateTrainingType
    )
    LabeledRadioGroup("level", "レベル：", WebConstants.trainingLevelList, state.trainingLevel, model::updateTrainingLevel)
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("5ステ合計") }
            }
            Tr {
                Td { Text(state.trainingResult.speed.toString()) }
                Td { Text(state.trainingResult.stamina.toString()) }
                Td { Text(state.trainingResult.power.toString()) }
                Td { Text(state.trainingResult.guts.toString()) }
                Td { Text(state.trainingResult.wisdom.toString()) }
                Td { Text(state.trainingResult.skillPt.toString()) }
                Td { Text(state.trainingResult.hp.toString()) }
                Td { Text(state.trainingResult.statusTotal.toString()) }
            }
        }
    }
//        LabeledCheckbox("trainingParamTest", "トレーニング設定調査", model.trainingParamTest != null) {
//            model.updateTrainingParamTest(it)
//        }
//        model.trainingParamTest?.let {
//            TrainingParamTest(it)
//        }
    if (state.trainingImpact.isNotEmpty()) {
        H3 { Text("サポカ影響度") }
        Div {
            Table({ classes(AppStyle.table) }) {
                Tr {
                    Th({
                        style {
                            property("border", "none")
                        }
                        unsetWidth()
                    }) { }
                    Th { Text("スピード") }
                    Th { Text("スタミナ") }
                    Th { Text("パワー") }
                    Th { Text("根性") }
                    Th { Text("賢さ") }
                    Th { Text("スキルPt") }
                    Th { Text("体力") }
                    Th { Text("5ステ合計") }
                }
                state.trainingImpact.forEach { (name, status) ->
                    Tr {
                        Td({
                            unsetWidth()
                        }) { Text(name) }
                        Td { Text(status.speed.toString()) }
                        Td { Text(status.stamina.toString()) }
                        Td { Text(status.power.toString()) }
                        Td { Text(status.guts.toString()) }
                        Td { Text(status.wisdom.toString()) }
                        Td { Text(status.skillPt.toString()) }
                        Td { Text(status.hp.toString()) }
                        Td { Text(status.statusTotal.toString()) }
                    }
                }
            }
        }
        Div { Text("※計算式： 上昇量 - 対象カードが練習不参加時の上昇量") }
    }
    H3 { Text("期待値（練習配置率を考慮）") }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
                Th { Text("スキルPt") }
                Th { Text("体力") }
                Th { Text("5ステ合計") }
            }
            Tr {
                Td { Text(((state.expectedResult.speed * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.stamina * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.power * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.guts * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.wisdom * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.skillPt * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.hp * 100).roundToInt() / 100.0).toString()) }
                Td { Text(((state.expectedResult.statusTotal * 100).roundToInt() / 100.0).toString()) }
            }
        }
        Div { Text("※練習参加チェックボックスを無視して、練習配置率に応じて参加/不参加を決めた場合の期待値") }
    }
    H2 { Text("編成情報") }
    H3 { Text("レースボーナス合計：${state.totalRaceBonus}") }
    H3 { Text("ファンボーナス合計：${state.totalFanBonus}") }
    H3 { Text("初期ステータスアップ") }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th { Text("スピード") }
                Th { Text("スタミナ") }
                Th { Text("パワー") }
                Th { Text("根性") }
                Th { Text("賢さ") }
            }
            Tr {
                Td { Text(state.initialStatus.speed.toString()) }
                Td { Text(state.initialStatus.stamina.toString()) }
                Td { Text(state.initialStatus.power.toString()) }
                Td { Text(state.initialStatus.guts.toString()) }
                Td { Text(state.initialStatus.wisdom.toString()) }
            }
        }
    }
    H3 { Text("得意率・絆・ヒント率") }
    Div {
        Table({ classes(AppStyle.table) }) {
            Tr {
                Th({
                    style {
                        property("border", "none")
                    }
                    unsetWidth()
                }) { }
                Th { Text("得意練習配置率") }
                Th { Text("初期絆") }
                Th { Text("必要絆上げ回数") }
                Th { Text("ヒント発生率") }
            }
            state.supportSelectionList.filter { it.isSelected }.forEach {
                Tr {
                    Td({
                        unsetWidth()
                    }) { Text(it.name) }
                    Td { Text("${(it.specialtyRate * 1000).roundToInt() / 10.0}%") }
                    Td { Text(it.initialRelation.toString()) }
                    Td { Text(it.relationUpCount.toString()) }
                    Td { Text("${(it.hintRate * 1000).roundToInt() / 10.0}%") }
                }
            }
        }
        Div { Text("※得意練習配置率とヒント発生率は推定値、必要絆上げ回数はイベントとヒント除く") }
    }
    H3 { Text("獲得可能スキルヒント（イベント除く）") }
    Div {
        state.availableHint.forEach {
            Div { Text("${it.key} ： ${it.value.joinToString(", ")}") }
        }
    }
    when (state.scenario) {
        Scenario.URA -> UraSimulation(model)
        Scenario.AOHARU -> AoharuSimulation(model.aoharuSimulationViewModel)
    }
    Hr { style { marginTop(16.px) } }
    A(
        href = "https://github.com/mee1080/umasim/blob/main/Library/web.md",
        attrs = {
            target(ATarget.Blank)
            attr("rel", "noreferrer noopener")
        }
    ) { Text("使用ライブラリ") }
}