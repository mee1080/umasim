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
    private lateinit var teamMemberSource: String

    fun load(
        charaSource: String,
        supportSource: String,
        teamMemberSource: String,
    ) {
        Store.charaSource = charaSource
        Store.supportSource = supportSource
        Store.teamMemberSource = teamMemberSource
    }

    val charaList by lazy { CharaLoader.load(charaSource) }
    val supportList by lazy { SupportCardLoader.load(supportSource) }
    private val trainingList = trainingData
    val scenarioLink = scenarioLinkData

    fun getTrainingList(scenario: Scenario) = trainingList.filter { it.scenario == scenario }

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

    fun getTrainingInfo(scenario: Scenario) = getTrainingList(scenario)
        .groupBy { it.type }
        .mapValues { entry -> TrainingInfo(entry.key, entry.value.sortedBy { it.level }) }

    fun getTraining(scenario: Scenario, type: StatusType) = getTrainingInfo(scenario)[type]!!

    fun isScenarioLink(scenario: Scenario, charaName: String) = scenarioLink[scenario]?.contains(charaName) ?: false

    object Aoharu {
        private val training = aoharuTrainingData
            .groupBy { it.type }
            .mapValues { entry -> entry.value.associateBy { it.count } }

        fun getTraining(type: StatusType, count: Int) = training[type]!![count]!!

        fun getBurn(type: StatusType, isLink: Boolean) =
            training[type]!![if (isLink) AoharuTraining.COUNT_BURN_LINK else AoharuTraining.COUNT_BURN]!!

        private val trainingTeam = aoharuTrainingTeamData
            .groupBy { it.type }
            .mapValues { entry -> entry.value.associateBy { it.level } }

        fun getTrainingTeam(type: StatusType, level: Int) = trainingTeam[type]!![level]!!

        private val burnTeam = aoharuBurnTeamData
            .associateBy { it.type }

        fun getBurnTeam(type: StatusType) = burnTeam[type]!!

        private val teamMemberList by lazy { TeamMemberLoader.load(teamMemberSource) }

        fun getTeamMember(supportCardId: Int) = teamMemberList.firstOrNull { it.supportCardId == supportCardId }

        fun getGuest(name: String) = teamMemberList.firstOrNull { it.rarity == 1 && it.chara == name }

        fun getShuffledGuest() = teamMemberList.filter { it.rarity == 1 }.shuffled()

        val teamStatusRank = aoharuTeamStatusRank
            .associateBy { it.rank }
    }

}
