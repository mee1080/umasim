package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.compose.common.parts.NumberInput
import io.github.mee1080.umasim.compose.common.parts.WithTooltip
import io.github.mee1080.umasim.race.calc2.NOT_SELECTED
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.skillData2
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.*


@Composable
fun SkillInput(virtual: Boolean, state: AppState, dispatch: OperationDispatcher<AppState>) {
    val hasSkills by derivedStateOf { state.hasSkills(virtual) }
    HideBlock(
        header = { Text("スキル") },
        initialOpen = true,
        headerClosed = {
            Text("スキル：${hasSkills.joinToString(", ") { it.name }}")
        },
    ) {
        SkillSetting(virtual, state, dispatch)
    }
}

@Composable
private fun SkillSetting(virtual: Boolean, state: AppState, dispatch: OperationDispatcher<AppState>) {
    val skillIdSet by derivedStateOf { state.skillIdSet(virtual) }
    val chara by derivedStateOf { state.chara(virtual) }
    val setting by derivedStateOf { state.setting }
    var filter by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MyButton({ dispatch(clearSkill(virtual)) }) { Text("すべてのスキルを削除") }
        UniqueSkillSetting(virtual, chara.charaName, chara.uniqueLevel, skillIdSet, dispatch)
        SkillFilter(filter) { filter = it }
        if (filter.isEmpty()) {
            val passiveSkills = groupedSkills["passive"]
            if (passiveSkills != null) {
                TypeSkillSetting(virtual, "パッシブ", passiveSkills, skillIdSet, setting, dispatch)
            }
            val healSkills = groupedSkills["heal"]
            if (healSkills != null) {
                TypeSkillSetting(virtual, "回復", healSkills, skillIdSet, setting, dispatch)
            }
            val speedSkills = groupedSkills["speed"]
            if (speedSkills != null) {
                TypeSkillSetting(virtual, "速度", speedSkills, skillIdSet, setting, dispatch)
            }
            val accelerationSkills = groupedSkills["acceleration"]
            if (accelerationSkills != null) {
                TypeSkillSetting(virtual, "加速", accelerationSkills, skillIdSet, setting, dispatch)
            }
            val multiSkill = groupedSkills["multi"]
            if (multiSkill != null) {
                TypeSkillSetting(virtual, "複合", multiSkill, skillIdSet, setting, dispatch)
            }
            val gateSkills = groupedSkills["other"]
            if (gateSkills != null) {
                TypeSkillSetting(virtual, "その他", gateSkills, skillIdSet, setting, dispatch)
            }
        } else {
            val skills = notUniqueSkills.filter { it.name.contains(filter) }
            SkillFlowRow(virtual, "", skills, skillIdSet, dispatch)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillFilter(
    value: String,
    onChange: (String) -> Unit,
) {
    var inputValue by remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        inputValue = value
    }
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("フィルタ：")
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = Modifier.width(256.dp),
        )
        MyButton({ onChange(inputValue) }) {
            Text("反映")
        }
        if (value.isNotEmpty()) {
            MyButton({ onChange("") }) {
                Text("クリア")
            }
        }
    }
}

@Composable
private fun SkillChip(virtual: Boolean, skill: SkillData, selected: Boolean, dispatch: OperationDispatcher<AppState>) {
    WithTooltip(
        tooltip = {
            Column {
                skill.messages.forEach { Text(it) }
            }
        },
    ) {
        FilterChip(
            selected = selected,
            onClick = { dispatch(toggleSkill(virtual, skill)) },
            label = { Text(skill.name) },
            leadingIcon = { if (selected) Icon(Icons.Default.Check, "Selected") },
        )
    }
}

private val charaList =
    listOf(NOT_SELECTED) + skillData2.mapNotNull { it.holder }.distinct().sortedBy { it.substring(it.indexOf(']')) }

private val notUniqueSkills = skillData2
    .filter { it.rarity !in listOf("unique", "evo") }

private val groupedSkills = notUniqueSkills
    .groupBy { it.type }
    .mapValues { skillsByType ->
        skillsByType.value.groupBy { it.rarity }.mapValues { skills ->
            skills.value.sortedBy { it.id }
        }
    }

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UniqueSkillSetting(
    virtual: Boolean,
    charaName: String,
    uniqueLevel: Int,
    skillIdSet: Set<String>,
    dispatch: OperationDispatcher<AppState>,
) {
    HideBlock(
        header = { Text("固有/進化") },
        initialOpen = true,
        headerBackground = MaterialTheme.colorScheme.tertiaryContainer,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectBox(
                charaList, charaName,
                onSelect = { dispatch(setCharaName(virtual, it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("キャラ") },
            )
            if (charaName != NOT_SELECTED) {
                val uniqueSkill = charaToUniqueSkill[charaName]
                if (uniqueSkill != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val selected = skillIdSet.contains(uniqueSkill.id)
                        SkillChip(virtual, uniqueSkill, selected, dispatch)
                        NumberInput(
                            uniqueLevel,
                            { dispatch(setUniqueLevel(virtual, it)) },
                            min = 1,
                            max = 6,
                            enabled = selected
                        )
                    }
                }
                val evoSkills = charaToEvoSkills[charaName]
                if (evoSkills?.isNotEmpty() == true) {
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        evoSkills.forEach { skill ->
                            SkillChip(virtual, skill, skillIdSet.contains(skill.id), dispatch)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeSkillSetting(
    virtual: Boolean,
    title: String,
    skills: Map<String, List<SkillData>>,
    skillIdSet: Set<String>,
    setting: RaceSetting,
    dispatch: OperationDispatcher<AppState>
) {
    HideBlock(
        header = { Text(title) },
        headerBackground = MaterialTheme.colorScheme.tertiaryContainer,
        headerClosed = {
            val hasSkills = skills.values.flatten().filter { skillIdSet.contains(it.id) }
            Text("$title：${hasSkills.joinToString(", ") { it.name }}")
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val inheritSkills = skills["inherit"]?.filterBySetting(virtual, setting)
            if (!inheritSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "継承", inheritSkills, skillIdSet, dispatch)
            }
            val scenarioSkills = skills["scenario"]?.filterBySetting(virtual, setting)
            if (!scenarioSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "シナリオ進化", scenarioSkills, skillIdSet, dispatch)
            }
            val rareSkills = skills["rare"]?.filterBySetting(virtual, setting)
            if (!rareSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "レア", rareSkills, skillIdSet, dispatch)
            }
            val normalSkills = skills["normal"]?.filterBySetting(virtual, setting)
            if (!normalSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "通常", normalSkills, skillIdSet, dispatch)
            }
            val specialSkills = skills["special"]?.filterBySetting(virtual, setting)
            if (!specialSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "特殊", specialSkills, skillIdSet, dispatch)
            }
            val minusSkills = skills["minus"]?.filterBySetting(virtual, setting)
            if (!minusSkills.isNullOrEmpty()) {
                SkillFlowRow(virtual, "マイナス", minusSkills, skillIdSet, dispatch)
            }
        }
    }
}

private fun List<SkillData>.filterBySetting(virtual: Boolean, setting: RaceSetting): List<SkillData> {
    val style by derivedStateOf { if (virtual) setting.virtualLeader.basicRunningStyle else setting.basicRunningStyle }
    val track by derivedStateOf { setting.trackDetail }
    return filter { skill ->
        skill.invokes.any {
            it.targetRunningStyle.emptyOrContains(style.value)
                    && it.targetRotation.emptyOrContains(track.turn)
                    && it.targetGroundType.emptyOrContains(track.surface)
                    && it.targetDistanceType.emptyOrContains(track.distanceType)
                    && it.targetTrackId.emptyOrContains(track.raceTrackId)
                    && it.targetBasisDistance.emptyOrContains(track.isBasisDistance)
                    && it.targetCornerCount.emptyOrContains(track.corners.size)
        }
    }
}

private fun Set<Int>.emptyOrContains(value: Int) = isEmpty() || contains(value)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillFlowRow(
    virtual: Boolean,
    title: String,
    skills: List<SkillData>,
    skillIdSet: Set<String>,
    dispatch: OperationDispatcher<AppState>,
) {
    if (title.isNotEmpty()) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        skills.forEach { skill ->
            SkillChip(virtual, skill, skillIdSet.contains(skill.id), dispatch)
        }
    }
}