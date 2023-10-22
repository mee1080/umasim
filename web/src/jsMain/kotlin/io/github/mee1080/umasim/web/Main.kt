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
package io.github.mee1080.umasim.web

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.web.components.atoms.*
import io.github.mee1080.umasim.web.components.lib.ROOT_ELEMENT_ID
import io.github.mee1080.umasim.web.components.lib.dvh
import io.github.mee1080.umasim.web.components.lib.dvw
import io.github.mee1080.umasim.web.page.lesson.LessonPage
import io.github.mee1080.umasim.web.page.rotation.RotationPage
import io.github.mee1080.umasim.web.page.simulation.SimulationPage
import io.github.mee1080.umasim.web.page.top.RootPage
import io.github.mee1080.umasim.web.state.Page
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

fun main() {
    StoreLoader.load()

    renderComposable(rootElementId = ROOT_ELEMENT_ID) {
        val scope = rememberCoroutineScope()
        val model = remember { ViewModel(scope) }
        Style(AppStyle)
        Div({
            classes(MdClass.background, MdClass.onBackgroundText)
            style {
                position(Position.Relative)
                width(100.dvw)
                height(100.dvh)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                typeScale(MdSysTypeScale.bodyLarge)
            }
        }) {
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    flexGrow(1)
                    overflowY("scroll")
                }
            }) {
                when (model.state.page) {
                    Page.Top -> RootPage(model, model.state)
                    Page.Rotation -> RotationPage(model.rotationViewModel, model.state.rotationState)
                    Page.Lesson -> LessonPage(model.lessonViewModel, model.state.lessonState)
                    Page.Simulation -> SimulationPage(model.state)
                }
            }
            Div({
                style {
                    position(Position.Relative)
                }
            }) {
                MdElevation(3)
                MdDivider()
                MdPrimaryTabs(
                    selection = Page.entries,
//                    selection = Page.entries.filter { it != Page.Simulation },
                    selectedItem = model.state.page,
                    itemToLabel = { it.displayName },
                    itemToIcon = { it.icon },
                    onSelect = { model.navigate(it) },
                )
            }
        }
    }
}