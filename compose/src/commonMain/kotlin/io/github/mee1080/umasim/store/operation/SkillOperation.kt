package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.skillData2
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation

internal val charaToUniqueSkill = skillData2.filter { it.rarity == "unique" }.associateBy { it.holder!! }

internal val charaToEvoSkills = skillData2.filter { it.rarity == "evo" }.groupBy { it.holder!! }

fun setCharaName(charaName: String) = DirectOperation<AppState> { state ->
    val deleteSkills = state.setting.hasSkills.filter { it.holder != null }.toSet()
    val deleteSkillIds = deleteSkills.map { it.id }.toSet()
    val addSkills = buildSet {
        charaToUniqueSkill[charaName]?.let { add(it) }
//        charaToEvoSkills[charaName]?.let { addAll(it) }
    }
    val addSkillIds = addSkills.map { it.id }.toSet()
    state.copy(
        charaName = charaName,
        skillIdSet = state.skillIdSet - deleteSkillIds + addSkillIds,
    ).updateSetting {
        it.copy(hasSkills = it.hasSkills - deleteSkills + addSkills)
    }
}

fun setUniqueLevel(uniqueLevel: Int) = DirectOperation<AppState> { state ->
    state.updateSetting { it.copy(uniqueLevel = uniqueLevel) }
}

fun toggleSkill(skillData: SkillData) = DirectOperation<AppState> { state ->
    if (state.skillIdSet.contains(skillData.id)) {
        state.copy(skillIdSet = state.skillIdSet - skillData.id).updateSetting {
            it.copy(hasSkills = it.hasSkills - skillData)
        }
    } else {
        state.copy(skillIdSet = state.skillIdSet + skillData.id).updateSetting {
            it.copy(hasSkills = it.hasSkills + skillData)
        }
    }
}

fun clearSkill() = DirectOperation<AppState> { state ->
    state.copy(skillIdSet = emptySet()).updateSetting { it.copy(hasSkills = emptyList()) }
}