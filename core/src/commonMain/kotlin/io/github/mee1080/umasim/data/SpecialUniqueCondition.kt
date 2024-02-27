package io.github.mee1080.umasim.data

data class SpecialUniqueCondition(
    val trainingType: StatusType,
    val trainingLevel: Int,
    val totalTrainingLevel: Int,
    val relation: Int,
    val supportCount: Map<StatusType, Int>,
    val fanCount: Int,
    val status: Status,
    val totalRelation: Int,
    val trainingSupportCount: Int,
    val speedSkillCount: Int,
    val healSkillCount: Int,
    val accelSkillCount: Int,
    val friendTraining: Boolean,
    val friendCount: Int,
) {
    val supportTypeCount = supportCount.size
}
