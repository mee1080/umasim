/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Lesson
import io.github.mee1080.umasim.data.StatusType
import kotlinx.serialization.json.Json

interface ActionSelector {

    fun init(state: SimulationState) {}

    suspend fun select(state: SimulationState, selection: List<Action>): Action

    suspend fun selectWithScore(state: SimulationState, selection: List<Action>): Pair<Action, List<Double>> =
        select(state, selection) to emptyList()

    fun selectOuting(selection: List<Action>) = selection.firstOrNull { it is Outing } ?: selectSleep(selection)

    fun selectSleep(selection: List<Action>) = selection.first { it is Sleep } as Sleep

    fun selectTraining(selection: List<Action>, type: StatusType) =
        selection.first { it is Training && it.type == type } as Training

    fun selectBeforeLiveLesson(state: SimulationState): Lesson? = null
}

interface ActionSelectorGenerator {
    fun generateSelector(): ActionSelector
}

interface SerializableActionSelectorGenerator : ActionSelectorGenerator {
    companion object {
        private val defaultSerializer = Json {
            encodeDefaults = true
            ignoreUnknownKeys = false
            prettyPrint = true
        }
    }

    val serializer get() = defaultSerializer
    fun serialize(): String
    fun deserialize(serialized: String): SerializableActionSelectorGenerator
}
