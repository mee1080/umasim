package io.github.mee1080.umasim.db

import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class SimulationResultRepository(private val path: String) {

    init {
        if (File(path).exists() && !File(path).delete()) {
            throw IOException()
        }
        Database(path).withTransaction { db ->
            db.execute(SimulationTarget.createTableSql)
            db.execute(SimulationResult.createTableSql)
        }
    }

    suspend fun save(target: String, result: Collection<Summary>) = withContext(Dispatchers.IO) {
        Database(path).withTransaction { db ->
            val targetId = db.insert(SimulationTarget(target)) ?: throw IOException()
            db.insert(result.map { SimulationResult(targetId, it) })
        }
    }
}