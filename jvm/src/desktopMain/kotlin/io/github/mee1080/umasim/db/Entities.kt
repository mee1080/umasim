package io.github.mee1080.umasim.db

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.Summary
import java.sql.PreparedStatement

interface Entity {
    val insertSql: String
    fun fillInsert(statement: PreparedStatement)
}

class SimulationTarget(val text: String) : Entity {

    companion object {
        val name = "simulationTarget"

        val createTableSql = """
            CREATE TABLE $name (
                targetId INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL
            )
        """.trimIndent()

        val insertSql = """
            INSERT INTO $name (text) VALUES (?)
        """.trimIndent()
    }

    override val insertSql get() = Companion.insertSql

    override fun fillInsert(statement: PreparedStatement) {
        statement.setString(1, text)
    }
}

class SimulationResult(val targetId: Long, val summary: Summary) : Entity {

    companion object {
        val name = "simulationResult"

        val createTableSql = """
            CREATE TABLE $name (
                targetId INTEGER NOT NULL,
                speed INTEGER NOT NULL,
                stamina INTEGER NOT NULL,
                power INTEGER NOT NULL,
                guts INTEGER NOT NULL,
                wisdom INTEGER NOT NULL,
                skillPt INTEGER NOT NULL,
                skillHintCount INTEGER NOT NULL,
                speedTraining INTEGER NOT NULL,
                speedTrainingFriend0 INTEGER NOT NULL,
                speedTrainingFriend1 INTEGER NOT NULL,
                speedTrainingFriend2 INTEGER NOT NULL,
                speedTrainingFriend3 INTEGER NOT NULL,
                speedTrainingFriend4 INTEGER NOT NULL,
                speedTrainingFriend5 INTEGER NOT NULL,
                staminaTraining INTEGER NOT NULL,
                staminaTrainingFriend0 INTEGER NOT NULL,
                staminaTrainingFriend1 INTEGER NOT NULL,
                staminaTrainingFriend2 INTEGER NOT NULL,
                staminaTrainingFriend3 INTEGER NOT NULL,
                staminaTrainingFriend4 INTEGER NOT NULL,
                staminaTrainingFriend5 INTEGER NOT NULL,
                powerTraining INTEGER NOT NULL,
                powerTrainingFriend0 INTEGER NOT NULL,
                powerTrainingFriend1 INTEGER NOT NULL,
                powerTrainingFriend2 INTEGER NOT NULL,
                powerTrainingFriend3 INTEGER NOT NULL,
                powerTrainingFriend4 INTEGER NOT NULL,
                powerTrainingFriend5 INTEGER NOT NULL,
                gutsTraining INTEGER NOT NULL,
                gutsTrainingFriend0 INTEGER NOT NULL,
                gutsTrainingFriend1 INTEGER NOT NULL,
                gutsTrainingFriend2 INTEGER NOT NULL,
                gutsTrainingFriend3 INTEGER NOT NULL,
                gutsTrainingFriend4 INTEGER NOT NULL,
                gutsTrainingFriend5 INTEGER NOT NULL,
                wisdomTraining INTEGER NOT NULL,
                wisdomTrainingFriend0 INTEGER NOT NULL,
                wisdomTrainingFriend1 INTEGER NOT NULL,
                wisdomTrainingFriend2 INTEGER NOT NULL,
                wisdomTrainingFriend3 INTEGER NOT NULL,
                wisdomTrainingFriend4 INTEGER NOT NULL,
                wisdomTrainingFriend5 INTEGER NOT NULL,
                trainingHintCount INTEGER NOT NULL,
                sleepCount INTEGER NOT NULL,
                outingCount INTEGER NOT NULL
            )
        """.trimIndent()

        val insertSql = """
            INSERT INTO $name VALUES (${Array(46) { "?" }.joinToString(",")})
        """.trimIndent()
    }

    override val insertSql get() = Companion.insertSql

    override fun fillInsert(statement: PreparedStatement) {
        var index = 1
        statement.setLong(index++, targetId)
        statement.setInt(index++, summary.status.speed)
        statement.setInt(index++, summary.status.stamina)
        statement.setInt(index++, summary.status.power)
        statement.setInt(index++, summary.status.guts)
        statement.setInt(index++, summary.status.wisdom)
        statement.setInt(index++, summary.status.skillPt)
        statement.setInt(index++, summary.status.skillHint.size)
        statement.setInt(index++, summary.trainingCount[StatusType.SPEED]!!)
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![0])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![1])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![2])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![3])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![4])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.SPEED]!![5])
        statement.setInt(index++, summary.trainingCount[StatusType.STAMINA]!!)
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![0])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![1])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![2])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![3])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![4])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.STAMINA]!![5])
        statement.setInt(index++, summary.trainingCount[StatusType.POWER]!!)
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![0])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![1])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![2])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![3])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![4])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.POWER]!![5])
        statement.setInt(index++, summary.trainingCount[StatusType.GUTS]!!)
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![0])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![1])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![2])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![3])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![4])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.GUTS]!![5])
        statement.setInt(index++, summary.trainingCount[StatusType.WISDOM]!!)
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![0])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![1])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![2])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![3])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![4])
        statement.setInt(index++, summary.trainingFriendCount[StatusType.WISDOM]!![5])
        statement.setInt(index++, summary.trainingHintCount)
        statement.setInt(index++, summary.sleepCount)
        statement.setInt(index++, summary.outingCount)
    }
}
