package io.github.mee1080.umasim.race.data2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val jsonParser = Json { allowTrailingComma = true }

val skillData2 by lazy {
    jsonParser.decodeFromString<List<SkillData>>(rawSkillData)
}

@Serializable
data class SkillData(
    val id: Int,
    val name: String,
    val rarity: String,
    val type: String,
    val sp: Int = 0,
    val activateLot: Int = 1,
    val holder: String? = null,
    val invokes: List<Invoke> = emptyList(),
    val description: List<String> = emptyList(),
)

@Serializable
data class Invoke(
    val conditions: List<List<SkillCondition>> = emptyList(),
    val preConditions: List<List<SkillCondition>> = emptyList(),
    val effects: List<SkillEffect> = emptyList(),
    val cd: Double = 500.0,
    val duration: Double = 0.0,
) {
    val isPassive by lazy {
        passiveSpeed > 0 || passiveStamina > 0 || passivePower > 0 || passiveGuts > 0 || passiveWisdom > 0 || temptationRate > 0
    }

    val passiveSpeed by lazy {
        effects.filter { it.type == "passiveSpeed" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveStamina by lazy {
        effects.filter { it.type == "passiveStamina" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passivePower by lazy {
        effects.filter { it.type == "passivePower" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveGuts by lazy {
        effects.filter { it.type == "passiveGuts" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val passiveWisdom by lazy {
        effects.filter { it.type == "passiveWisdom" || it.type == "passiveAll" }.sumOf { it.value } / 10000
    }

    val temptationRate by lazy {
        effects.filter { it.type == "temptationRate" }.sumOf { it.value } / 10000
    }

    val targetSpeed by lazy {
        effects.filter { it.type == "targetSpeed" }.sumOf { it.value } / 10000.0
    }

    val speedWithDecel by lazy {
        effects.filter { it.type == "speedWithDecel" }.sumOf { it.value } / 10000.0
    }

    val speed by lazy {
        effects.filter { it.type == "currentSpeed" }.sumOf { it.value } / 10000.0
    }

    val acceleration by lazy {
        effects.filter { it.type == "acceleration" }.sumOf { it.value } / 10000.0
    }

    val startAdd by lazy {
        effects.filter { it.type == "startAdd" }.sumOf { it.value } / 10000.0
    }

    val startMultiply by lazy {
        effects.filter { it.type == "startMultiply" }.sumOf { it.value } / 10000.0
    }
}

@Serializable
data class SkillCondition(
    val type: String,
    val operator: String,
    val value: Int,
) {
    val check: (Int) -> Boolean by lazy {
        when (operator) {
            "==" -> { target -> target == value }
            "!=" -> { target -> target != value }
            ">=" -> { target -> target >= value }
            "<=" -> { target -> target <= value }
            ">" -> { target -> target > value }
            "<" -> { target -> target < value }
            else -> { _ -> false }
        }
    }
}

@Serializable
data class SkillEffect(
    val type: String,
    val value: Int,
    val special: String = "",
    val additional: String = "",
)
