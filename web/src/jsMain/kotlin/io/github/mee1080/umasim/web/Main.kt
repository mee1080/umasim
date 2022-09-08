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
import io.github.mee1080.umasim.web.components.material.MwcTabBar
import io.github.mee1080.umasim.web.components.material.initLibraries
import io.github.mee1080.umasim.web.page.RootPage
import io.github.mee1080.umasim.web.page.rotation.RotationPage
import io.github.mee1080.umasim.web.state.Page
import io.github.mee1080.umasim.web.style.AppStyle
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

fun main() {
    StoreLoader.load()
    initLibraries()

    renderComposable(rootElementId = "root") {
        val scope = rememberCoroutineScope()
        val model = remember { ViewModel(scope) }
        Style(AppStyle)
        MwcTabBar(
            Page.values().asList(),
            model.state.page,
            { it.displayName },
            { it.icon },
            onSelect = { model.navigate(it) },
        )
        when (model.state.page) {
            Page.Top -> RootPage(model, model.state)
            Page.Rotation -> RotationPage(model.rotationViewModel, model.state.rotationState)
        }
    }
}