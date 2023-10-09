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

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.io.File

object StoreLoader {

    private const val FILE_BASE = "data/"
    private const val URL_BASE = "https://raw.githubusercontent.com/mee1080/umasim/main/data/"
    private const val FORCE_NETWORK = false

    private val names = arrayOf("chara.txt", "support_card.txt", "team_member.txt", "goal_race.txt", "race.txt")

    fun load(dataDir: String? = null) {
        val base = File(dataDir ?: FILE_BASE)
        val files = names.map { File(base, it) }
        if (!FORCE_NETWORK && files.all { it.canRead() }) {
            Store.load(
                files[0].readText(),
                files[1].readText(),
                files[2].readText(),
                files[3].readText(),
                files[4].readText(),
            )
        } else {
            System.err.println("not found ${base.absolutePath} ${base.list().contentToString()}")
            runBlocking {
                val data = HttpClient(CIO).use { client ->
                    names.map { client.get(URL_BASE + it).bodyAsText() }
                }
                if (FORCE_NETWORK) {
                    data.forEach { println(it) }
                }
                Store.load(data[0], data[1], data[2], data[3], data[4])
            }
        }
    }
}