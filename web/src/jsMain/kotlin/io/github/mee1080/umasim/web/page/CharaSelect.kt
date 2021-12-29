package io.github.mee1080.umasim.web.page

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.LabeledSelect
import io.github.mee1080.umasim.web.state.State
import io.github.mee1080.umasim.web.state.WebConstants
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun CharaSelect(model: ViewModel, state: State) {
    H2 { Text("育成キャラ") }
    LabeledSelect("", WebConstants.displayCharaList, state.selectedChara, model::updateChara)
}