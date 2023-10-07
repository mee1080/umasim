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
package io.github.mee1080.umasim.web.components.lib

import org.jetbrains.compose.web.css.StyleScope

enum class UserSelectStyle(val attrValue: String) {

    /**
     *  The text of the element and its sub-elements is not selectable.
     *  Note that the Selection object can contain these elements.
     */
    None("none"),

    /**
     * The used value of auto is determined as follows:
     *
     * On the ::before and ::after pseudo elements, the used value is none
     * If the element is an editable element, the used value is contain
     * Otherwise, if the used value of user-select on the parent of this element is all, the used value is all
     * Otherwise, if the used value of user-select on the parent of this element is none, the used value is none
     * Otherwise, the used value is text
     */
    Auto("auto"),

    /**
     * The text can be selected by the user.
     */
    Text("text"),

    /**
     * The content of the element shall be selected atomically:
     * If a selection would contain part of the element,
     * then the selection must contain the entire element including all its descendants.
     * If a double-click or context-click occurred in sub-elements,
     * the highest ancestor with this value will be selected.
     */
    All("all"),

    /**
     * Enables selection to start within the element;
     * however, the selection will be contained by the bounds of that element.
     */
    Contain("contain"),
}

fun StyleScope.userSelect(value: UserSelectStyle) = property("user-select", value.attrValue)
