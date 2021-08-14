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
package io.github.mee1080.umasim.data

import io.github.mee1080.umasim.simulation.TrainingInfo

object Store {

    private lateinit var charaSource: String
    private lateinit var supportSource: String
    private lateinit var trainingSource: String

    fun load(
        charaSource: String,
        supportSource: String,
        trainingSource: String,
    ) {
        Store.charaSource = charaSource
        Store.supportSource = supportSource
        Store.trainingSource = trainingSource
    }

    val charaList get() = lazy { CharaLoader.load(charaSource) }.value
    val supportList get() = lazy { SupportCardLoader.load(supportSource) }.value
    val trainingList get() = lazy { TrainingLoader.load(trainingSource) }.value

    fun getSupport(vararg target: Pair<Int, Int>) = supportList
        .filter { target.contains(it.id to it.talent) }

    fun getSupportByName(vararg target: Pair<String, Int>) = supportList
        .filter { target.contains(it.name to it.talent) }

    fun getSupport(id: Int, talent: Int) = getSupport(id to talent).first()

    fun getSupportOrNull(id: Int, talent: Int) = getSupport(id to talent).firstOrNull()

    fun getSupportByName(vararg name: String) = getSupportByName(*(name.map { it to 4 }.toTypedArray()))

    fun getSupportByName(name: String, talent: Int) = getSupportByName(name to talent).first()

    fun getChara(name: String, rarity: Int, rank: Int) = charaList
        .firstOrNull { it.name == name && it.rarity == rarity && it.rank == rank }
        ?: charaList.first { it.charaName == name && it.rarity == rarity && it.rank == rank }

    fun getChara(id: Int, rarity: Int, rank: Int) = charaList
        .first { it.id == id && it.rarity == rarity && it.rank == rank }

    fun getCharaOrNull(id: Int, rarity: Int, rank: Int) = charaList
        .firstOrNull { it.id == id && it.rarity == rarity && it.rank == rank }

    val trainingInfo
        get() = trainingList
            .groupBy { it.type }
            .mapValues { entry -> TrainingInfo(entry.key, entry.value.sortedBy { it.level }) }

    fun getTraining(type: StatusType) = trainingInfo[type]!!
}
