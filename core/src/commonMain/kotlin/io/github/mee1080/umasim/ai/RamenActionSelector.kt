package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.simulation2.Action
import io.github.mee1080.umasim.simulation2.ScenarioActionParam
import io.github.mee1080.umasim.simulation2.SerializableActionSelectorGenerator
import io.github.mee1080.umasim.simulation2.SimulationState
import kotlinx.serialization.Serializable

class RamenActionSelector(
    vararg val options: Option = deafultOptions.toTypedArray(),
) : BaseActionSelector3<RamenActionSelector.Option, RamenActionSelector.Context>() {

    companion object {
        const val DEBUG = false
        const val FORCE = 10000000.0

        val deafultOptions = buildList {
            val base = Option(
                status = 100,
                wisdom = 90,
                skillPt = 1000,
                hp = 600,
                motivation = 1000,
                relation = 10000,
                outingRelation = 24000,
                hpKeep = 1000,
                risk = 350,
            )
            add(base)
            add(
                base.copy(
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 700,
                    risk = 300,
                )
            )
            add(
                base.copy(
                    wisdom = 90,
                    hp = 750,
                    hpKeep = 1200,
                    risk = 350,
                )
            )
            add(
                base.copy(
                    wisdom = 100,
                    hp = 600,
                    hpKeep = 1100,
                    risk = 400,
                )
            )
        }
    }

    @Serializable
    data class Option(
        val status: Int = 100,
        override val wisdom: Int = 80,
        override val skillPt: Int = 1000,
        override val hp: Int = 400,
        override val motivation: Int = 1000,
        override val relation: Int = 10000,
        override val outingRelation: Int = 20000,
        override val hpKeep: Int = 900,
        override val risk: Int = 300,
    ) : SerializableActionSelectorGenerator, BaseOption {
        override val speed get() = status
        override val stamina get() = status
        override val power get() = status
        override val guts get() = status
        override val maxSleep get() = 0

        override fun generateSelector() = RamenActionSelector(this)
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
        // TODO
        return 0.0
    }

    override fun selectFromScore(context: Context): Pair<Action, Double> {
        // TODO
        return super.selectFromScore(context)
    }

    override suspend fun calcScenarioActionScore(
        context: Context,
        action: Action
    ): Double? {
        // TODO
        return null
    }
}
