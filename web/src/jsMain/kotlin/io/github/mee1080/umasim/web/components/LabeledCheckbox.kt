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
package io.github.mee1080.umasim.web.components

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.target
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.checked
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLInputElement

@Composable
fun LabeledCheckbox(name: String, label: String, checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    Label {
        Input(InputType.Checkbox, "1") {
            name(name)
            onChange { onCheckedChanged(it.target<HTMLInputElement>().checked) }
            if (checked) checked()
        }
        Text(label)
    }
}