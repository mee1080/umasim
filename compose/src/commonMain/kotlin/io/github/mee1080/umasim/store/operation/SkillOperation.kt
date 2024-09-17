package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.skillData2
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.DirectOperation
import io.github.mee1080.utility.applyIf

internal val charaToUniqueSkill = skillData2.filter { it.rarity == "unique" }.associateBy { it.holder!! }

internal val charaToEvoSkills = skillData2.filter { it.rarity == "evo" }.groupBy { it.holder!! }

fun AppState.updateSkillIdSet(virtual: Boolean, action: (Set<String>) -> Set<String>): AppState {
    return if (virtual) {
        copy(virtualSkillIdSet = action(virtualSkillIdSet))
    } else {
        copy(skillIdSet = action(skillIdSet))
    }
}

fun setCharaName(virtual: Boolean, charaName: String) = DirectOperation<AppState> { state ->
    val uniqueSkill = charaToUniqueSkill[charaName]
    val deleteSkills = state.hasSkills(virtual).filter {
        it.holder != null || it.name == uniqueSkill?.name
    }.toSet()
    val deleteSkillIds = deleteSkills.map { it.id }.toSet()
    val addSkills = buildSet {
        uniqueSkill?.let { add(it) }
//        charaToEvoSkills[charaName]?.let { addAll(it) }
    }
    val addSkillIds = addSkills.map { it.id }.toSet()
    state.updateSkillIdSet(virtual) {
        it - deleteSkillIds + addSkillIds
    }.updateUmaStatus(virtual) {
        it.copy(
            charaName = charaName,
            hasSkills = it.hasSkills - deleteSkills + addSkills,
        )
    }.applyIf(!virtual) {
        copy(contributionTargets = state.contributionTargets - deleteSkillIds)
    }
}

fun setUniqueLevel(virtual: Boolean, uniqueLevel: Int) = DirectOperation<AppState> { state ->
    state.updateUmaStatus(virtual) { it.copy(uniqueLevel = uniqueLevel) }
}

fun toggleSkill(virtual: Boolean, skillData: SkillData) = DirectOperation<AppState> { state ->
    if (state.skillIdSet(virtual).contains(skillData.id)) {
        state.updateSkillIdSet(virtual) {
            it - skillData.id
        }.updateUmaStatus(virtual) {
            it.copy(hasSkills = it.hasSkills - skillData)
        }.applyIf(!virtual) {
            copy(contributionTargets = state.contributionTargets - skillData.id)
        }
    } else {
        val groupSkills = state.hasSkills(virtual).filter { it.group == skillData.group }.toSet()
        val groupSkillIds = groupSkills.map { it.id }.toSet()
        state.updateSkillIdSet(virtual) {
            it + skillData.id - groupSkillIds
        }.updateUmaStatus(virtual) {
            it.copy(hasSkills = it.hasSkills + skillData - groupSkills)
        }.applyIf(!virtual) {
            copy(contributionTargets = state.contributionTargets - groupSkillIds)
        }
    }
}

fun clearSkill(virtual: Boolean) = DirectOperation<AppState> { state ->
    state.updateSkillIdSet(virtual) { emptySet() }.updateUmaStatus(virtual) { it.copy(hasSkills = emptyList()) }
}

fun setSkillCategoryView(categoryView: Boolean) = DirectOperation<AppState> { state ->
    state.copy(skillCategoryView = categoryView)
}

fun setSkillDisplayMinus(categoryView: Boolean) = DirectOperation<AppState> { state ->
    state.copy(skillDisplayMinus = categoryView)
}
