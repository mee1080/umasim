package io.github.mee1080.umasim.web.page.onsen

import androidx.compose.runtime.*
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.onsen.StratumType
import io.github.mee1080.umasim.web.components.LabeledCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdDivider
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.lib.ScopedStyleSheet
import io.github.mee1080.umasim.web.components.lib.install
import io.github.mee1080.umasim.web.components.parts.HideBlock
import io.github.mee1080.utility.removedAt
import io.github.mee1080.utility.replaced
import org.jetbrains.compose.web.attributes.Draggable
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.math.max

@Composable
fun OnsenDigPage() {
    var state by remember { mutableStateOf(loadOnsenDigState()) }
    var divider by remember { mutableStateOf(0.0) }
    Div({ classes(S.wrapper) }) {
        Div({ classes(S.settingArea) }) {
            InitialSetting(state.initial) { data ->
                state = state.copy(initial = data).calc()
            }
            Schedule("ジュニア", 0, state.schedules[0]) {
                state = state.copy(schedules = state.schedules.replaced(0, it)).calc()
            }
            Schedule("クラシック", 1, state.schedules[1]) {
                state = state.copy(schedules = state.schedules.replaced(1, it)).calc()
            }
            Schedule("シニア", 2, state.schedules[2]) {
                state = state.copy(schedules = state.schedules.replaced(2, it)).calc()
            }
        }
        MdDivider {
            classes(S.divider)
            draggable(Draggable.True)
            var current = 0.0
            onDragStart { current = it.pageY }
            onDrag {
                if (it.pageY > 100.0) {
                    divider = divider + current - it.pageY
                }
                current = it.pageY
            }
        }
        Div({
            classes(S.calendarArea)
            style { height(divider.px + 60.percent) }
        }) {
            Calendar(state.turns) { index, data ->
                state = state.copy(turns = state.turns.replaced(index, data)).calc()
            }
        }
    }
}

@Composable
private fun InitialSetting(setting: OnsenDigInitialState, update: (OnsenDigInitialState) -> Unit) {
    HideBlock("初期設定", true) {
        Div({ classes(S.initialSettingBox) }) {
            Div({ classes(S.settingHeader) }) { Text("育成ウマ娘") }
            Div({ classes(S.settingEntry) }) {
                Select({
                    onChange {
                        val value = it.value ?: return@onChange
                        update(setting.copy(chara = value))
                    }
                }) {
                    charaNames.forEach {
                        Option(it, attrs = {
                            if (setting.chara == it) selected()
                        }) { Text(it) }
                    }
                }
            }
            Div({ classes(S.settingHeader) }) { Text("シナリオリンク") }
            Div({ classes(S.settingEntry) }) {
                linkCharaNames.forEach { name ->
                    LabeledCheckbox(name, name, setting.link.contains(name)) { checked ->
                        if (checked) {
                            update(setting.copy(link = setting.link + name))
                        } else {
                            update(setting.copy(link = setting.link - name))
                        }
                    }
                }
            }
            Div({ classes(S.settingHeader) }) { Text("因子") }
            Div({ classes(S.settingEntry) }) {
                repeat(6) { index ->
                    Select({
                        onChange {
                            val value = it.value ?: return@onChange
                            update(setting.copy(factor = setting.factor.replaced(index, StatusType.valueOf(value))))
                        }
                    }) {
                        trainingType.forEach { type ->
                            Option(type.name, {
                                if (setting.factor[index] == type) selected()
                            }) { Text(type.displayName) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Schedule(label: String, stage: Int, schedule: OnsenDigSchedule, update: (OnsenDigSchedule) -> Unit) {
    HideBlock(label, true) {
        Div({ classes(S.settingHeader) }) { Text("前半ステータス") }
        Div({ classes(S.settingEntry) }) {
            StatusSelect(schedule.firstHalfStatusRank) { index, rank ->
                update(schedule.copy(firstHalfStatusRank = schedule.firstHalfStatusRank.replaced(index, rank)))
            }
        }
        Div({ classes(S.settingHeader) }) { Text("前半トレーニングLv") }
        Div({ classes(S.settingEntry) }) {
            TrainingLevelInput(schedule.firstHalfTrainingLevel) {
                update(schedule.copy(firstHalfTrainingLevel = it))
            }
        }
        Div({ classes(S.settingHeader) }) { Text("後半ステータス") }
        Div({ classes(S.settingEntry) }) {
            StatusSelect(schedule.laterHalfStatusRank) { index, rank ->
                update(schedule.copy(laterHalfStatusRank = schedule.laterHalfStatusRank.replaced(index, rank)))
            }
        }
        Div({ classes(S.settingHeader) }) { Text("後半トレーニングLv") }
        Div({ classes(S.settingEntry) }) {
            TrainingLevelInput(schedule.laterHalfTrainingLevel) {
                update(schedule.copy(laterHalfTrainingLevel = it))
            }
        }
        Div({ classes(S.settingHeader) }) { Text("掘削順") }
        Div({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                rowGap(8.px)
            }
        }) {
            GensenSelect(stage, schedule.gensenList, delete = { index ->
                update(schedule.copy(gensenList = schedule.gensenList.removedAt(index)))
            }) { index, gensen ->
                update(schedule.copy(gensenList = schedule.gensenList.replaced(index, gensen)))
            }
            Div {
                Button({
                    onClick {
                        update(schedule.copy(gensenList = schedule.gensenList + OnsenDigGensen()))
                    }
                }) { Text("追加") }
            }
        }
    }
}

@Composable
private fun StatusSelect(data: List<String>, update: (Int, String) -> Unit) {
    data.forEachIndexed { index, rank ->
        Select({
            onChange {
                val value = it.value ?: return@onChange
                update(index, value)
            }
        }) {
            statusRanks.forEach {
                Option(it, {
                    if (rank == it) selected()
                }) { Text(it) }
            }
        }
    }
}

@Composable
private fun TrainingLevelInput(value: Int, update: (Int) -> Unit) {
    Button({
        onClick { update(value - 1) }
        if (value <= 1) disabled()
    }) { Text("-") }
    Text(value.toString())
    Button({
        onClick { update(value + 1) }
        if (value >= 5) disabled()
    }) { Text("+") }
}

@Composable
private fun GensenSelect(
    stage: Int,
    gensenList: List<OnsenDigGensen>,
    delete: (Int) -> Unit,
    update: (Int, OnsenDigGensen) -> Unit,
) {
    gensenList.forEachIndexed { index, gensen ->
        Div({ classes(S.settingEntry) }) {
            Button({
                onClick { delete(index) }
            }) { Text("削除") }
            Select({
                onChange {
                    val value = it.value ?: return@onChange
                    update(index, gensen.copy(name = value))
                }
            }) {
                gensenNames[stage].forEach { name ->
                    Option(name, {
                        if (gensen.name == name) selected()
                    }) { Text(name) }
                }
            }
            Select({
                onChange {
                    val value = it.value ?: return@onChange
                    update(index, gensen.copy(equipment = StratumType.valueOf(value)))
                }
            }) {
                StratumType.entries.forEach { equipment ->
                    Option(equipment.name, {
                        if (gensen.equipment == equipment) selected()
                    }) { Text(equipment.displayName) }
                }
            }
        }
    }
}

@Composable
private fun Calendar(turns: List<OnsenDigTurn>, update: (Int, OnsenDigTurn) -> Unit) {
    var digOnly by remember { mutableStateOf(false) }
    Div {
        MdCheckbox("掘削スケジュールのみ表示", digOnly) {
            onChange { digOnly = it }
        }
    }
    Div({
        classes(S.calendarBox)
        if (digOnly) classes(S.digOnly)
    }) {
        Div({ classes(S.calendarHeader) }) { Text("ジュニア") }
        Div({ classes(S.calendarMonth) }) {
            Div { Text("残11") }
        }
        Div()
        Div()
        turns.forEachIndexed { index, data ->
            if (index % 4 == 2) {
                when (index) {
                    22 -> Div({ classes(S.calendarHeader) }) { Text("クラシック") }
                    46 -> Div({ classes(S.calendarHeader) }) { Text("シニア") }
                    70 -> Div({ classes(S.calendarHeader) }) { Text("ファイナルズ") }
                }
                if (index <= 6) {
                    Div({ classes(S.calendarMonth) }) { Text("残${9 - index}") }
                } else if (index <= 66) {
                    Div({ classes(S.calendarMonth) }) { Text("${(index / 2 + 2) % 12}月") }
                } else {
                    Div({ classes(S.calendarMonth) })
                }
            }
            Turn(data) {
                update(index, it)
            }
        }
    }
}

@Composable
private fun Turn(data: OnsenDigTurn, update: (OnsenDigTurn) -> Unit) {
    Div({
        classes(S.turnBox)
        if (data.goal) classes(S.turnBoxGoal)
    }) {
        Div({
            classes(S.turnRow)
            actionColor[data.displayAction]?.let { actionColor ->
                style { backgroundColor(actionColor) }
            }
        }) {
            if (data.goal) {
                Text(data.displayAction.label)
            } else {
                Select({
                    onChange {
                        val value = it.value ?: return@onChange
                        update(data.copy(action = OnsenDigTurnAction.valueOf(value)))
                    }
                }) {
                    selectableTurnActions.forEach { action ->
                        Option(action.name, {
                            if (data.action == action) selected()
                        }) { Text(action.label) }
                    }
                }
            }
        }
        Div({ classes(S.turnRow) }) {
            Text("人数")
            Button({
                onClick { update(data.copy(memberCount = data.memberCount - 1)) }
                if (!data.displayAction.hasMember || data.memberCount <= 0) disabled()
            }) { Text("-") }
            Text(data.memberCount.toString())
            Button({
                onClick { update(data.copy(memberCount = data.memberCount + 1)) }
                if (!data.displayAction.hasMember || data.memberCount >= 5) disabled()
            }) { Text("+") }
        }
        Div({
            classes(S.turnRow)
            if (data.bathing) {
                style {
                    val backgroundColor = when {
                        data.ticket <= 0 -> rgba(255, 0, 0, 0.6)
                        data.superRecoveryBathing -> rgba(255, 163, 41, 0.4)
                        else -> rgba(58, 219, 64, 0.4)
                    }
                    backgroundColor(backgroundColor)
                }
            }
        }) {
            LabeledCheckbox("bathing", "入浴", data.bathing) {
                update(data.copy(bathing = it))
            }
            repeat(data.ticket) {
                Div({ classes(S.ticket) })
            }
            if (data.ticketChange > 0) Div { Text("+${data.ticketChange}") }
            if (data.ticketChange < 0) Div { Text("${data.ticketChange}") }
        }
        Div({
            classes(S.digTurnRow)
            if (data.gensen != null) {
                style { backgroundColor(gensenColor[data.gensen]!!) }
            }
        }) {
            if (data.gensen != null) {
                if (data.digFirstTurn) {
                    Text(data.gensen)
                    Text(" / ")
                }
                if (data.gensenRest > 0) {
                    Text("残 ${data.gensenRest}")
                } else {
                    Text("掘削完了")
                    Text(" 余剰${-data.gensenRest}")
                }
            }
        }
        Div({
            classes(S.turnRow)
            style { backgroundColor(hsla(max(0, 250 - data.usedHp), 100, 50, 0.4)) }
        }) {
            if (data.superRecoveryAvailable) {
                Text("超回復可能")
            } else {
                Text("消費 ${data.usedHp}")
                LabeledCheckbox("superRecovery", "超回復", data.superRecoveryTriggered) {
                    update(data.copy(superRecoveryTriggered = it))
                }
            }
        }
    }
}

private val gensenColor = mapOf(
    "疾駆の湯" to rgba(26, 139, 235, 0.4),
    "堅忍の湯" to rgba(238, 117, 122, 0.4),
    "明晰の湯" to rgba(30, 199, 153, 0.4),
    "駿閃の古湯" to rgba(172, 113, 183, 0.4),
    "剛脚の古湯" to rgba(251, 171, 7, 0.4),
    "健壮の古湯" to rgba(229, 107, 94, 0.4),
    "天翔の古湯" to rgba(98, 100, 221, 0.4),
    "秘湯ゆこま" to rgba(247, 109, 178, 0.4),
    "伝説の秘湯" to rgba(249, 209, 71, 0.4),
)

private val actionColor = mapOf(
//    OnsenDigTurnAction.Speed to rgba(49,184,255,0.4),
//    OnsenDigTurnAction.Training to rgba(255,166,22,0.4),
//    OnsenDigTurnAction.Wisdom to rgba(16,200,141,0.4),
    OnsenDigTurnAction.Race to rgba(49, 184, 255, 0.6),
    OnsenDigTurnAction.Outing to rgba(248, 168, 17, 0.6),
    OnsenDigTurnAction.PR to rgba(239, 112, 82, 0.6),
    OnsenDigTurnAction.Goal to rgba(246, 95, 148, 0.4),
)

private val S = object : ScopedStyleSheet() {

    val wrapper by style {
        height(100.percent)
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val settingArea by style {
        flexGrow(1)
        flexShrink(1)
        overflowY("scroll")
    }

    val divider by style {
        height(8.px)
        flexShrink(0)
        cursor("row-resize")
    }

    val calendarArea by style {
        flexShrink(0)
        overflowY("scroll")
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
    }

    val settingHeader by style {
        fontWeight(700)
        marginTop(8.px)
    }

    val settingEntry by style {
        display(DisplayStyle.Flex)
        flexWrap(FlexWrap.Wrap)
        columnGap(8.px)
    }

    val initialSettingBox by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val calendarBox by style {
        display(DisplayStyle.Grid)
        width(720.px)
        gridTemplateColumns("auto repeat(4,1fr)")
    }

    val calendarHeader by style {
        gridColumn(1, 6)
        fontWeight(700)
        textAlign("center")
        fontSize(1.2.em)
        padding(16.px)
    }

    val calendarMonth by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val turnBox by style {
        border {
            width(2.px)
            style(LineStyle.Solid)
            color(rgb(32, 32, 32))
        }
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val turnBoxGoal by style {
        border {
            width(2.px)
            style(LineStyle.Solid)
            color(Color.red)
        }
        backgroundColor(rgba(255, 0, 0, 0.1))
    }

    val turnRow by style {
        padding(0.px, 8.px)
        height(32.px)
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        columnGap(8.px)
    }

    val digOnly by style {
        desc(self, ".$turnRow").style {
            display(DisplayStyle.None)
        }
    }

    val digTurnRow by style {
        padding(0.px, 8.px)
        height(32.px)
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        columnGap(8.px)
    }

    val ticket by style {
        border {
            width(2.px)
            style(LineStyle.Solid)
            color(rgb(98, 75, 54))
        }
        borderRadius(4.px)
        width(16.px)
        height(24.px)
        backgroundColor(rgb(248, 97, 11))
    }
}.install()
