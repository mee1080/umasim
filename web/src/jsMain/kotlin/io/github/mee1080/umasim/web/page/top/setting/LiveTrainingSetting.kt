package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.state.TrainingLiveState
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun LiveTrainingSetting(model: ViewModel, state: TrainingLiveState) {
    NestedHideBlock("グランドライブ") {
        Div {
            Span { Text("トレーニング上昇量：") }
            Text("スピード")
            TextInput(state.speed) {
                size(10)
                onInput { model.updateLiveSpeed(it.value) }
            }
            Text("スタミナ")
            TextInput(state.stamina) {
                size(10)
                onInput { model.updateLiveStamina(it.value) }
            }
            Text("パワー")
            TextInput(state.power) {
                size(10)
                onInput { model.updateLivePower(it.value) }
            }
            Text("根性")
            TextInput(state.guts) {
                size(10)
                onInput { model.updateLiveGuts(it.value) }
            }
            Text("賢さ")
            TextInput(state.wisdom) {
                size(10)
                onInput { model.updateLiveWisdom(it.value) }
            }
            Text("スキルPt")
            TextInput(state.skillPt) {
                size(10)
                onInput { model.updateLiveSkillPt(it.value) }
            }
        }
        Div {
            Span { Text("友情トレーニング獲得量アップ：") }
            TextInput(state.friendTrainingUpInput) {
                size(10)
                onInput { model.updateLiveFriend(it.value) }
            }
        }
        Div {
            Span { Text("得意率アップ：") }
            TextInput(state.specialityRateUpInput) {
                size(10)
                onInput { model.updateLiveSpecialityRate(it.value) }
            }
            Span { Text("※サポカの得意率に加算で実装") }
        }
    }
}