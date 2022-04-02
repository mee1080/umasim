package io.github.mee1080.umasim.web.components.material

import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.StyleScope


fun initLibraries() {
    require("@material/mwc-button")
    require("@material/mwc-list/mwc-list-item.js")
    require("@material/mwc-select")
    require("@material/mwc-textfield")
    require("@material/mwc-formfield")
    require("@material/mwc-radio")
    require("@material/mwc-checkbox")
    require("@material/mwc-tab")
    require("@material/mwc-tab-bar")
}

val StyleScope.themePrimary get() = CssVar(this, "--mdc-theme-primary") { Color("#6200ee") }
val StyleScope.themeOnPrimary get() = CssVar(this, "--mdc-theme-on-primary") { Color("#fff") }
val StyleScope.themeSecondary get() = CssVar(this, "--mdc-theme-secondary") { Color("#018786") }
val StyleScope.themeOnSecondary get() = CssVar(this, "--mdc-theme-on-secondary") { Color("#fff") }

fun StyleScope.primaryToSecondary() {
    themePrimary(themeSecondary)
    themeOnPrimary(themeOnSecondary)
}

fun AttrsScope<*>.secondary() = style { primaryToSecondary() }