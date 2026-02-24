package io.github.mee1080.umasim.web.page.top.setting

import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.rankToString
import io.github.mee1080.umasim.web.components.atoms.MdCheckbox
import io.github.mee1080.umasim.web.components.atoms.MdRadioGroup
import io.github.mee1080.umasim.web.components.atoms.onChange
import io.github.mee1080.umasim.web.components.parts.DivFlexCenter
import io.github.mee1080.umasim.web.components.parts.NestedHideBlock
import io.github.mee1080.umasim.web.components.parts.SliderEntry
import io.github.mee1080.umasim.web.state.BCState
import io.github.mee1080.umasim.web.state.WebConstants.trainingTypeList
import io.github.mee1080.umasim.web.vm.ViewModel
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun BcTrainingSetting(model: ViewModel, state: BCState) {
    NestedHideBlock(Scenario.BC.displayName) {
        H4 { Text("チームパラメータ") }
        SliderEntry("フィジカル：", state.physicalLevel, 1, 8) {
            model.updateBC { copy(physicalLevel = it.toInt()) }
        }
        SliderEntry("テクニック：", state.techniqueLevel, 1, 8) {
            model.updateBC { copy(techniqueLevel = it.toInt()) }
        }
        SliderEntry("メンタル：", state.mentalLevel, 1, 8) {
            model.updateBC { copy(mentalLevel = it.toInt()) }
        }

        DivFlexCenter {
            MdCheckbox("DREAMSトレーニング中", state.dreamsTrainingActive) {
                onChange { model.updateBC { copy(dreamsTrainingActive = it) } }
            }
        }

        H4 { Text("チームメンバー") }
        state.teamMembers.forEachIndexed { index, member ->
            Div({ style { marginTop(8.px); padding(8.px); border(1.px, LineStyle.Solid, Color.lightgray); borderRadius(4.px) } }) {
                Div({ style { fontWeight("bold") } }) { Text(member.charaName) }
                DivFlexCenter {
                    SliderEntry("ランク", member.memberRank, 0, 16) { value ->
                        model.updateBC {
                            copy(teamMembers = teamMembers.toMutableList().also {
                                it[index] = it[index].copy(memberRank = value.toInt())
                            })
                        }
                    }
                    Span({ style { marginLeft(8.px) } }) {
                        Text("(${rankToString.getOrElse(member.memberRank) { "" }})")
                    }
                }

                DivFlexCenter {
                    MdCheckbox("ドリームゲージ最大", member.dreamGaugeMax) {
                        onChange { value ->
                            model.updateBC {
                                copy(teamMembers = teamMembers.toMutableList().also {
                                    it[index] = it[index].copy(dreamGaugeMax = value)
                                })
                            }
                        }
                    }
                }

                DivFlexCenter {
                    Text("配置：")
                    MdRadioGroup(
                        trainingTypeList,
                        member.position,
                        onSelect = { value ->
                            model.updateBC {
                                copy(teamMembers = teamMembers.toMutableList().also {
                                    it[index] = it[index].copy(position = value)
                                })
                            }
                        },
                        itemToLabel = { it.displayName }
                    )
                }
            }
        }
    }
}
