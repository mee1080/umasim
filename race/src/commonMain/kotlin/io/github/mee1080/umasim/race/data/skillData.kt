/*
 * Copyright 2023 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
/*
 * This file was ported from uma-clock-emu by Romulus Urakagi Tsai(@urakagi)
 * https://github.com/urakagi/uma-clock-emu
 */
package io.github.mee1080.umasim.race.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import kotlin.math.round
import kotlin.random.Random

@OptIn(ExperimentalSerializationApi::class)
private val jsonParser = Json {
    this.allowTrailingComma = true
}

val normalSkillData by lazy {
    jsonParser.decodeFromString<List<Skill>>(rawNormalSkillData)
}

val uniqueSkillData by lazy {
    jsonParser.decodeFromString<List<Skill>>(rawUniqueSkillData)
}

val skillData by lazy {
    val skills = mutableListOf<Skill>()

    for (skill in uniqueSkillData) {
        skills += skill.copy(type = "unique")
        if (skill.noInherit != true) {
            skills += skill.toInheritSkill()
        }
    }

    for (skillWrapper in normalSkillData) {
        skills += skillWrapper.mergeVariants()
    }
    skills
}

val skillDataMap by lazy {
    skillData.associateBy { it.name }
}

fun getSkill(name: String) = skillDataMap[name]!!

private val healInherit = mapOf(
    750 to 350,
    550 to 150,
    350 to 50,
    150 to 35,
    -100 to -100,
    -300 to -300
)

private val targetSpeedInherit = mapOf(
    0.55 to 0.15,
    0.45 to 0.25,
    0.4 to 0.2,
    0.385 to 0.15,
    0.35 to 0.15,
    0.3 to 0.075,
    0.25 to 0.05,
    0.15 to 0.035,
    -0.05 to -0.05,
)

private val speedWithDecelInherit = mapOf(
    0.55 to 0.15,
    0.45 to 0.25,
    0.4 to 0.2,
    0.385 to 0.15,
    0.35 to 0.15,
    0.3 to 0.075,
    0.25 to 0.05,
    0.15 to 0.035,
    -0.05 to -0.05,
)

private val accelerationInherit = mapOf(
    0.5 to 0.3,
    0.4 to 0.2,
    0.3 to 0.1,
    0.2 to 0.05,
    0.1 to 0.05,
)

interface SkillEffect {
    val conditions: Conditions?
    val displayType: String
    val heal: Int?
    val targetSpeed: Double?
    val acceleration: Double?
    val speed: Double?
    val speedWithDecel: Double?
    val fatigue: Int?
    val passiveSpeed: Int?
    val passiveStamina: Int?
    val passivePower: Int?
    val passiveGuts: Int?
    val passiveWisdom: Int?
    val temptationRate: Int?
    val startDelay: Double?
    val trigger: String?
    val triggerRate: Double?
    val coolDownId: String
    val duration: Double?

    fun forEachEffect(action: (effect: String, value: Number) -> Unit) {
        heal?.let { action("heal", it) }
        targetSpeed?.let { action("targetSpeed", it) }
        acceleration?.let { action("acceleration", it) }
        speed?.let { action("speed", it) }
        speedWithDecel?.let { action("speedWithDecel", it) }
        fatigue?.let { action("fatigue", it) }
        passiveSpeed?.let { action("passiveSpeed", it) }
        passiveStamina?.let { action("passiveStamina", it) }
        passivePower?.let { action("passivePower", it) }
        passiveGuts?.let { action("passiveGuts", it) }
        passiveWisdom?.let { action("passiveWisdom", it) }
        temptationRate?.let { action("temptationRate", it) }
        startDelay?.let { action("startDelay", it) }
    }

    val isPassive
        get() = passiveSpeed != null ||
                passiveStamina != null ||
                passivePower != null ||
                passiveGuts != null ||
                passiveWisdom != null ||
                temptationRate != null

    fun skillType(): String {
        var newType = ""
        var hasEffect = false
        if (isPassive) {
            newType = "passive"
            hasEffect = true
        }
        if (heal != null) {
            newType = "heal"
            if (hasEffect) return "composite"
            hasEffect = true
        }
        if (targetSpeed != null || speedWithDecel != null) {
            newType = "speed"
            if (hasEffect) return "composite"
            hasEffect = true
        }
        if (acceleration != null) {
            newType = "acceleration"
            if (hasEffect) return "composite"
            hasEffect = true
        }
        if ((speed ?: 0.0) < 0.0) {
            newType = "decel"
            if (hasEffect) return "composite"
            hasEffect = true
        }
        if (fatigue != null) {
            newType = "fatigue"
            if (hasEffect) return "composite"
            hasEffect = true
        }
        if (startDelay != null) {
            newType = "gate"
            if (hasEffect) return "composite"
        }
        return newType
    }

    fun doTrigger() {
        val triggers = trigger?.let { mutableListOf(it) } ?: mutableListOf()
        if (speedWithDecel != null) {
            triggers += "{ thiz.currentSpeed += speedWithDecel }"
        }
        if (fatigue != null) {
            triggers += "{ thiz.doHeal(-fatigue) }"
        }
        triggers.forEach {
            // TODO
        }
    }
}

@Serializable
data class Skill(
    val id: Int? = null,
    val holder: Int? = null,
    val name: String? = null,
    val rarity: String? = null,
    val noInherit: Boolean? = null,

    val variants: List<Variant>? = null,
    val invokes: List<Invoke>? = null,
    private val type: String? = null,
    override val conditions: Conditions? = null,
    val emulatorTypeLimit: List<String>? = null,
    private val tooltip: String? = null,
    override val triggerRate: Double? = null,
    override val duration: Double? = null,
    val cd: Int = 500,
    // TODO 実装
    val init: String? = null,
    val check: String? = null,
    override val trigger: String? = null,

    override val heal: Int? = null,
    override val targetSpeed: Double? = null,
    override val acceleration: Double? = null,
    override val speed: Double? = null,
    override val speedWithDecel: Double? = null,
    override val fatigue: Int? = null,
    override val passiveSpeed: Int? = null,
    override val passiveStamina: Int? = null,
    override val passivePower: Int? = null,
    override val passiveGuts: Int? = null,
    override val passiveWisdom: Int? = null,
    override val temptationRate: Int? = null,
    override val startDelay: Double? = null,
) : SkillEffect {

    override val coolDownId get() = id.toString()

    fun toInheritSkill(): Skill {
        val variant = Variant(
            id = (id ?: 0) + 800000,
            name = name,
            rarity = "inherit",
            duration = duration?.let { it * 0.6 },
            heal = heal?.let { healInherit[it] },
            targetSpeed = targetSpeed?.let { targetSpeedInherit[it] },
            speedWithDecel = speedWithDecel?.let { speedWithDecelInherit[it] },
            acceleration = acceleration?.let { accelerationInherit[it] },
            invokes = invokes?.let { invokes ->
                invokes.map {
                    it.copy(
                        heal = it.heal?.let { healInherit[it] },
                        targetSpeed = it.targetSpeed?.let { targetSpeedInherit[it] },
                        speedWithDecel = it.speedWithDecel?.let { speedWithDecelInherit[it] },
                        acceleration = it.acceleration?.let { accelerationInherit[it] },
                    )
                }
            }
        )
        return copy(
            variants = listOf(variant)
        )
    }

    fun mergeVariants(): List<Skill> {
        return variants?.map {
            copy(
                id = it.id ?: id,
                name = it.name ?: name,
                invokes = it.invokes ?: invokes,
                rarity = it.rarity ?: rarity,
                type = it.type ?: type,
                tooltip = it.tooltip ?: tooltip,
                duration = it.duration ?: duration,
                holder = it.holder ?: holder,
                conditions = it.conditions ?: conditions,
                trigger = it.trigger ?: trigger,
                heal = it.heal ?: heal,
                targetSpeed = it.targetSpeed ?: targetSpeed,
                acceleration = it.acceleration ?: acceleration,
                speed = it.speed ?: speed,
                speedWithDecel = it.speedWithDecel ?: speedWithDecel,
                fatigue = it.fatigue ?: fatigue,
                passiveSpeed = it.passiveSpeed ?: passiveSpeed,
                passiveStamina = it.passiveStamina ?: passiveStamina,
                passivePower = it.passivePower ?: passivePower,
                passiveGuts = it.passiveGuts ?: passiveGuts,
                passiveWisdom = it.passiveWisdom ?: passiveWisdom,
                temptationRate = it.temptationRate ?: temptationRate,
                startDelay = it.startDelay ?: startDelay,
            )
        } ?: emptyList()
    }

    val displayTooltip by lazy {
        buildList {
            tooltip?.let { add(it) }
            duration?.let { add("${round(duration * 10) / 10}s") }
            forEachEffect { effect, value ->
                add("${effect}: ${round(value.toDouble() * 100) / 100}")
            }
        }.joinToString(" | ")
    }

    override val displayType by lazy { type ?: skillType() }
}

@Serializable
data class Variant(
    val id: Int? = null,
    val name: String? = null,

    val invokes: List<Invoke>? = null,
    val rarity: String? = null,
    val type: String? = null,
    val tooltip: String? = null,
    override val duration: Double? = null,
    val holder: Int? = null,
    override val conditions: Conditions? = null,
    val variants: List<Variant>? = null,
    // TODO 実装
    override val trigger: String? = null,

    override val heal: Int? = null,
    override val targetSpeed: Double? = null,
    override val acceleration: Double? = null,
    override val speed: Double? = null,
    override val speedWithDecel: Double? = null,
    override val fatigue: Int? = null,
    override val passiveSpeed: Int? = null,
    override val passiveStamina: Int? = null,
    override val passivePower: Int? = null,
    override val passiveGuts: Int? = null,
    override val passiveWisdom: Int? = null,
    override val temptationRate: Int? = null,
    override val startDelay: Double? = null,
) : SkillEffect {
    override val displayType by lazy { type ?: skillType() }
    override val coolDownId get() = id.toString()

    // TODO
    override val triggerRate: Double? get() = null
}

@Serializable
data class Invoke(
    val invokeNo: Int? = null,
    override val duration: Double? = null,
    override val conditions: Conditions? = null,
    override val trigger: String? = null,

    override val heal: Int? = null,
    override val targetSpeed: Double? = null,
    override val acceleration: Double? = null,
    override val speed: Double? = null,
    override val speedWithDecel: Double? = null,
    override val fatigue: Int? = null,
    override val passiveSpeed: Int? = null,
    override val passiveStamina: Int? = null,
    override val passivePower: Int? = null,
    override val passiveGuts: Int? = null,
    override val passiveWisdom: Int? = null,
    override val temptationRate: Int? = null,
    override val startDelay: Double? = null,
) : SkillEffect {
    override val displayType by lazy { skillType() }
    override val coolDownId by lazy {
        // FIXME
        Random.nextDouble().toString() + invokeNo.toString()
    }

    // TODO
    override val triggerRate: Double? get() = null
}

@Serializable
data class Conditions(
    val rotation: Int? = null,
    @Serializable(with = IntOrIntArraySerializer::class)
    val track_id: List<Int>? = null,
    val corner: Int? = null,
    @Serializable(with = PhaseSerializer::class)
    val phase: List<Int>? = null,
    val slope: Int? = null,
    val up_slope_random: Int? = null,
    val down_slope_random: Int? = null,
    val motivation: Int? = null,
    val base_speed: Int? = null,
    val base_stamina: Int? = null,
    val base_power: Int? = null,
    val base_guts: Int? = null,
    val base_wisdom: Int? = null,
    val is_lastspurt: Int? = null,
    val lastspurt: Int? = null,
    val course_distance: Int? = null,
    @Serializable(with = ArrayToStringSerializer::class)
    val remain_distance: String? = null,
    val distance_rate_random: List<Int>? = null,
    val straight_random: Int? = null,
    val is_last_straight: Int? = null,
    val straight_front_type: Int? = null,
    val is_finalstraight_random: Int? = null,
    @Serializable(with = IntOrIntArraySerializer::class)
    val corner_random: List<Int>? = null,
    val all_corner_random: Int? = null,
    val is_finalcorner_random: Int? = null,
    val is_finalcorner: Int? = null,
    val is_finalcorner_laterhalf: Int? = null,
    val phase_random: Int? = null,
    val phase_corner_random: Int? = null,
    val accumulatetime: Int? = null,
    val phase_firsthalf_random: Int? = null,
    val phase_laterhalf_random: Int? = null,
    @Serializable(with = IntOrIntArraySerializer::class)
    val distance_type: List<Int>? = null,
    val phase_firstquarter_random: Int? = null,
    val is_badstart: Int? = null,
    @Serializable(with = IntOrIntArraySerializer::class)
    val running_style: List<Int>? = null,
    val is_basis_distance: Int? = null,
    val ground_type: Int? = null,
    @Serializable(with = IntOrIntArraySerializer::class)
    val ground_condition: List<Int>? = null,
    val activate_count_all: Int? = null,
    val activate_count_start: Int? = null,
    val activate_count_heal: Int? = null,
    val is_activate_any_skill: Int? = null,
    val distance_rate_after_random: Int? = null,
    val temptation_count: Int? = null,
    val hp_per: String? = null,
    @Serializable(with = ArrayToStringSerializer::class)
    val distance_rate: String? = null,
)

private class IntOrIntArraySerializer : JsonTransformingSerializer<List<Int>>(serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element !is JsonArray) JsonArray(listOf(element)) else element
    }
}

private class PhaseSerializer : JsonTransformingSerializer<List<Int>>(serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        element as JsonPrimitive
        return if (element.isString) {
            val data = "(\\D*)(\\d*)".toRegex().find(element.content)!!
            val value = data.groupValues[2].toInt()
            val list = when (data.groupValues[1]) {
                ">=" -> (value..4).map { JsonPrimitive(it) }
                else -> throw IllegalArgumentException("Invalid operator for phase: ${data.groupValues[1]}")
            }
            JsonArray(list)
        } else {
            JsonArray(listOf(element))
        }
    }
}

private class ArrayToStringSerializer : JsonTransformingSerializer<String>(serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonArray) {
            JsonPrimitive(element.joinToString(separator = ","))
        } else if (element is JsonPrimitive && !element.isString) {
            JsonPrimitive(element.content)
        } else {
            element
        }
    }
}