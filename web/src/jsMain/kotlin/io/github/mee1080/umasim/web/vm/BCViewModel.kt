package io.github.mee1080.umasim.web.vm

import io.github.mee1080.umasim.web.state.BCState

class BCViewModel(private val root: ViewModel) {
    fun update(update: BCState.() -> BCState) {
        root.update { copy(bcState = bcState.update()) }
    }
}
