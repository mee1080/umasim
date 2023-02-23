/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.util

fun <T : R, R> T.applyIf(
    condition: Boolean,
    action: T.() -> R,
) = if (condition) action() else this

fun <T : R, R, D> T.applyIfNotNull(
    data: D?,
    action: T.(D) -> R,
) = data?.let { action(it) } ?: this

fun <T> List<T>.mapIf(
    condition: (T) -> Boolean,
    action: T.() -> T
) = map { if (condition(it)) it.action() else it }

fun <T> List<T>.replace(
    targetIndex: Int,
    action: T.() -> T
) = mapIndexed { index, item -> if (index == targetIndex) item.action() else item }