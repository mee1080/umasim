package io.github.mee1080.umasim.db

import java.io.Closeable
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class Database(path: String) : Closeable {

    private val connection = DriverManager.getConnection("jdbc:sqlite:$path")

    private val statement = connection.createStatement()

    fun <T> withTransaction(action: (DatabaseExecutor) -> T): T {
        connection.autoCommit = false
        try {
            val result = action(DatabaseExecutor(connection))
            connection.commit()
            return result
        } catch (e: Exception) {
            connection.rollback()
            throw e
        } finally {
            connection.autoCommit = true
        }
    }

    fun <T> useWithTransaction(action: (DatabaseExecutor) -> T) = use { withTransaction(action) }

    override fun close() {
        connection.close()
    }
}

class DatabaseExecutor(private val connection: Connection) {

    fun <T> select(sql: String, parse: (ResultSet) -> T): List<T> = connection.createStatement().executeQuery(sql).use {
        val list = mutableListOf<T>()
        while (it.next()) {
            list.add(parse(it))
        }
        list
    }

    fun selectFirstColumns(sql: String) = select(sql) { it.getString(1) }

    fun selectTsv(sql: String) = select(sql) { result ->
        (1..result.metaData.columnCount).joinToString("\t") {
            result.getString(it) ?: ""
        }
    }

    fun selectMap(sql: String) = select(sql) { result ->
        val columns = (1..result.metaData.columnCount)
            .associateWith { result.metaData.getColumnName(it) }
        (1..result.metaData.columnCount).associate {
            columns[it]!! to result.getString(it)
        }
    }

    fun execute(sql: String) = connection.createStatement().execute(sql)

    fun <T> execute(sql: String, data: T, fill: (PreparedStatement, T) -> Unit): Boolean {
        val statement = connection.prepareStatement(sql)
        fill(statement, data)
        return statement.execute()
    }

    fun <T> execute(sql: String, dataList: Collection<T>, fill: (PreparedStatement, T) -> Unit) {
        val statement = connection.prepareStatement(sql)
        dataList.forEach {
            fill(statement, it)
            statement.execute()
        }
    }

    fun insert(entity: Entity): Long {
        val statement = connection.prepareStatement(entity.insertSql)
        entity.fillInsert(statement)
        statement.execute()
        return statement.generatedKeys.use { it.getLong(1) }
    }

    fun insert(entities: Collection<Entity>) {
        if (entities.isEmpty()) return
        val statement = connection.prepareStatement(entities.first().insertSql)
        entities.forEach {
            it.fillInsert(statement)
            statement.execute()
        }
    }

    private fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {
        try {
            return block(this)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            close()
        }
    }
}