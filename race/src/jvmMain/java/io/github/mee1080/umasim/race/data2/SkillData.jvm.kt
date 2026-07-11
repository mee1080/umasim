package io.github.mee1080.umasim.race.data2

import java.io.File

actual fun loadLocalSkillDatatring(): String {
    return File("../data/skill_data.txt").readText()
}