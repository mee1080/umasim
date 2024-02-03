package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.compose.common.parts.HideBlock
import io.github.mee1080.umasim.compose.common.parts.NumberInput
import io.github.mee1080.umasim.compose.common.parts.WithPersistentTooltip
import io.github.mee1080.umasim.race.calc2.RaceSetting
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.skillData2
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.NOT_SELECTED
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.*


@Composable
fun SkillInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    HideBlock(
        header = { Text("スキル") },
        initialOpen = true,
        headerClosed = {
            Text("スキル：${state.setting.hasSkills.joinToString(", ") { it.name }}")
        },
    ) {
        SkillSetting(state, dispatch)
    }
}

@Composable
private fun SkillSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val skillIdSet = state.skillIdSet
    val setting = state.setting
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        UniqueSkillSetting(state.charaName, setting.uniqueLevel, skillIdSet, dispatch)
        val passiveSkills = groupedSkills["passive"]
        if (passiveSkills != null) {
            TypeSkillSetting("パッシブ", passiveSkills, skillIdSet, setting, dispatch)
        }
        val healSkills = groupedSkills["heal"]
        if (healSkills != null) {
            TypeSkillSetting("回復", healSkills, skillIdSet, setting, dispatch)
        }
        val speedSkills = groupedSkills["speed"]
        if (speedSkills != null) {
            TypeSkillSetting("速度", speedSkills, skillIdSet, setting, dispatch)
        }
        val accelerationSkills = groupedSkills["acceleration"]
        if (accelerationSkills != null) {
            TypeSkillSetting("加速", accelerationSkills, skillIdSet, setting, dispatch)
        }
        val multiSkill = groupedSkills["multi"]
        if (multiSkill != null) {
            TypeSkillSetting("複合", multiSkill, skillIdSet, setting, dispatch)
        }
        val gateSkills = groupedSkills["gate"]
        if (gateSkills != null) {
            TypeSkillSetting("ゲート", gateSkills, skillIdSet, setting, dispatch)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SkillChip(skill: SkillData, selected: Boolean, dispatch: OperationDispatcher<AppState>) {
    WithPersistentTooltip(
        text = {
            Column {
                skill.messages.forEach { Text(it) }
            }
        },
        delayTime = 500L,
    ) {
        FilterChip(
            selected = selected,
            onClick = { dispatch(toggleSkill(skill)) },
            label = { Text(skill.name) },
            leadingIcon = { if (selected) Icon(Icons.Default.Check, "Selected") },
            modifier = Modifier.applyTooltip(),
        )
    }
}

private val charaList =
    listOf(NOT_SELECTED) + skillData2.mapNotNull { it.holder }.distinct().sortedBy { it.substring(it.indexOf(']')) }

private val groupedSkills = skillData2
    .filter { it.rarity !in listOf("unique", "evo") }
    .groupBy { it.type }
    .mapValues { skillsByType ->
        skillsByType.value.groupBy { it.rarity }.mapValues { skills ->
            skills.value.sortedBy { it.id }
        }
    }

@Composable
private fun UniqueSkillSetting(
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
                onSelect = { dispatch(setCharaName(it)) },
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
                        SkillChip(uniqueSkill, selected, dispatch)
                        NumberInput(uniqueLevel, { dispatch(setUniqueLevel(it)) }, min = 1, max = 6, enabled = selected)
                    }
                }
                val evoSkills = charaToEvoSkills[charaName]
                if (evoSkills?.isNotEmpty() == true) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        evoSkills.forEach { skill ->
                            SkillChip(skill, skillIdSet.contains(skill.id), dispatch)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeSkillSetting(
    title: String,
    skills: Map<String, List<SkillData>>,
    skillIdSet: Set<String>,
    setting: RaceSetting,
    dispatch: OperationDispatcher<AppState>
) {
    HideBlock(
        header = { Text(title) },
        headerBackground = MaterialTheme.colorScheme.tertiaryContainer,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val rareSkills = skills["rare"]?.filterBySetting(setting)
            if (!rareSkills.isNullOrEmpty()) {
                SkillFlowRow("レア", rareSkills, skillIdSet, dispatch)
            }
            val normalSkills = skills["normal"]?.filterBySetting(setting)
            if (!normalSkills.isNullOrEmpty()) {
                SkillFlowRow("通常", normalSkills, skillIdSet, dispatch)
            }
            val specialSkills = skills["special"]?.filterBySetting(setting)
            if (!specialSkills.isNullOrEmpty()) {
                SkillFlowRow("特殊", specialSkills, skillIdSet, dispatch)
            }
        }
    }
}

private fun List<SkillData>.filterBySetting(setting: RaceSetting): List<SkillData> {
    val track = setting.trackDetail
    return filter { skill ->
        skill.invokes.any {
            it.targetRunningStyle.emptyOrContains(setting.basicRunningStyle.value)
                    && it.targetRotation.emptyOrContains(track.turn)
                    && it.targetGroundType.emptyOrContains(track.surface)
                    && it.targetDistanceType.emptyOrContains(track.distanceType)
                    && it.targetTrackId.emptyOrContains(track.raceTrackId)
                    && it.targetBasisDistance.emptyOrContains(track.isBasisDistance)
        }
    }
}

private fun Set<Int>.emptyOrContains(value: Int) = isEmpty() || contains(value)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillFlowRow(
    title: String,
    skills: List<SkillData>,
    skillIdSet: Set<String>,
    dispatch: OperationDispatcher<AppState>,
) {
    Text(title, style = MaterialTheme.typography.headlineSmall)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        skills.forEach { skill ->
            SkillChip(skill, skillIdSet.contains(skill.id), dispatch)
        }
    }
}