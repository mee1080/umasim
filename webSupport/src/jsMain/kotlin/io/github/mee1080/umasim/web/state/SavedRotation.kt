package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.RaceDistance
import io.github.mee1080.umasim.data.RaceGround
import io.github.mee1080.umasim.rotation.RaceRotationCalculator
import kotlinx.serialization.Serializable

@Serializable
class SavedRotation(
    private val groundSettingData: Map<String, String>,
    private val distanceSettingData: Map<String, String>,
    val rotation: List<String?>,
) {
    constructor(
        groundSetting: Map<RaceGround, RaceRotationCalculator.Rank>,
        distanceSetting: Map<RaceDistance, RaceRotationCalculator.Rank>,
        rotation: List<String?>,
    ) : this(
        groundSetting.mapKeys { it.key.name }.mapValues { it.value.name },
        distanceSetting.mapKeys { it.key.name }.mapValues { it.value.name },
        rotation,
    )

    val groundSetting
        get() = groundSettingData.mapKeys { RaceGround.valueOf(it.key) }
            .mapValues { RaceRotationCalculator.Rank.valueOf(it.value) }

    val distanceSetting
        get() = distanceSettingData.mapKeys { RaceDistance.valueOf(it.key) }
            .mapValues { RaceRotationCalculator.Rank.valueOf(it.value) }
}