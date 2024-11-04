package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.scenario.larc.LArcStatus

data class LArcState(
    val expectations: Int = 0,
    val overseas: Boolean = false,
    val overseasTurfAptitude: Int = 0,
    val longchampAptitude: Int = 0,
    val lifeRhythm: Int = 0,
    val nutritionManagement: Int = 0,
    val frenchSkill: Int = 0,
    val overseasExpedition: Int = 0,
    val strongHeart: Int = 0,
    val mentalStrength: Int = 0,
    val hopeOfLArc: Int = 0,
) {
    fun toLArcStatus() = LArcStatus(
        supporterPt = expectations * 1700,
        overseasTurfAptitude = overseasTurfAptitude,
        longchampAptitude = longchampAptitude,
        lifeRhythm = lifeRhythm,
        nutritionManagement = nutritionManagement,
        frenchSkill = frenchSkill,
        overseasExpedition = overseasExpedition,
        strongHeart = strongHeart,
        mentalStrength = mentalStrength,
        hopeOfLArc = hopeOfLArc,
    )
}