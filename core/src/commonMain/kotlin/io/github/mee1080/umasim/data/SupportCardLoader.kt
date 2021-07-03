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

object SupportCardLoader {

    @Suppress("UNUSED_CHANGED_VALUE")
    fun load(text: String): List<SupportCard> {
        val list = mutableListOf<SupportCard>()
        text.split("\n").forEach {
            val data = it.trim().split("\t")
            if (data.size >= 2) {
                var i = 0
                list.add(
                    SupportCard(
                        data[i++].toInt(),
                        data[i++],
                        data[i++],
                        data[i++].toInt(),
                        data[i++].toInt(),
                        data[i++].toInt(),
                        toSupportType(data[i++]),
                        SupportCard.SupportStatus(
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                        ),
                        SupportCard.SupportStatus(
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                            data[i++].toInt(),
                        ),
                        data.getOrNull(i++)?.split(", ") ?: emptyList(),
                        readHintStatus(data.getOrNull(i)),
                    )
                )
            }
        }
        return list
    }

    private fun readHintStatus(value: String?) = value
        ?.split(", ")
        ?.map { it.split(":") }
        ?.map {
            when (toSupportType(it[0])) {
                StatusType.SPEED -> Status(speed = it[1].toInt())
                StatusType.STAMINA -> Status(stamina = it[1].toInt())
                StatusType.POWER -> Status(power = it[1].toInt())
                StatusType.GUTS -> Status(guts = it[1].toInt())
                StatusType.WISDOM -> Status(wisdom = it[1].toInt())
                else -> Status()
            }
        }?.reduce { acc, status -> acc + status }
        ?: Status()
}
