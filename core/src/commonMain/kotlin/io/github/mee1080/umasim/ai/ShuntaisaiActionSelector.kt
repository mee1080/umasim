package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

open class ShuntaisaiActionSelector(
    vararg val options: Option = arrayOf(Option()),
) : BaseActionSelector3<ShuntaisaiActionSelector.Option, ShuntaisaiActionSelector.Context>() {

    companion object {
        const val DEBUG = false

        val defaultOption = Option()

        val defaultGenerator = object : ActionSelectorGenerator {
            override fun generateSelector() = ShuntaisaiActionSelector()
        }
    }

    @Serializable
    data class Option(
        val status: Int = 100,
        override val speed: Int = status,
        override val stamina: Int = status,
        override val power: Int = status,
        override val guts: Int = status,
        override val wisdom: Int = 80,
        override val skillPt: Int = 100,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,
        val gaugeScore: Int = 200,
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val maxSleep get() = 0

        override fun generateSelector() = ShuntaisaiActionSelector(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    class Context(
        option: Option,
        state: SimulationState,
    ) : BaseContext<Option>(option, state)

    override fun getContext(state: SimulationState): Context {
        val option = when {
            state.turn <= 24 -> options[0]
            state.turn <= 48 -> options.getOrElse(1) { options[0] }
            state.turn <= 72 -> options.getOrElse(2) { options[0] }
            else -> options.getOrElse(3) { options[0] }
        }
        return Context(option, state)
    }

    override fun calcScenarioScore(
        context: Context,
        action: Action,
        scenarioActionParam: ScenarioActionParam?
    ): Double {
        val param = scenarioActionParam as? ShuntaisaiActionParam ?: return 0.0
        // TODO 仮実装: 演目ゲージ上昇量のみ評価
        return param.gaugeUp.values.sum() * context.option.gaugeScore.toDouble()
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        return when (action) {
            // TODO 仮実装: 先頭の候補を選択（付け替えは現在の割り当て先が先頭）
            is ShuntaisaiAssignTeaching -> 0.0
            is ShuntaisaiTransformTeaching -> 0.0
            else -> null
        }
    }
}
