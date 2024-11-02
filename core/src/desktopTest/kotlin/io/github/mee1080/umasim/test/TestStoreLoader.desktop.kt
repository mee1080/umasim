package io.github.mee1080.umasim.test

import io.github.mee1080.umasim.data.Store
import java.io.File
import java.io.FileNotFoundException

private var loaded = false

actual fun loadTestStore() {
    if (!loaded) {
        TestStoreLoader.load()
        loaded = true
    }
}

private object TestStoreLoader {

    private const val FILE_BASE = "../data/"

    private val names = arrayOf("chara.txt", "support_card.txt", "team_member.txt", "goal_race.txt", "race.txt")

    fun load(dataDir: String? = null) {
        val base = File(dataDir ?: FILE_BASE)
        val files = names.map { File(base, it) }
        if (files.all { it.canRead() }) {
            Store.load(
                files[0].readText(),
                files[1].readText(),
                files[2].readText(),
                files[3].readText(),
                files[4].readText(),
            )
        } else {
            throw FileNotFoundException(base.absolutePath)
        }
    }
}
