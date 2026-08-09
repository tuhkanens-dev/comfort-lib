package dev.tuhkanens.comfortlib.database.base

import dev.tuhkanens.comfortlib.ComfortAPI
import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.database.DatabaseBase
import org.jetbrains.exposed.v1.jdbc.Database
import java.nio.file.Path

class SQLiteBase(private val directory: Path) : DatabaseBase() {
    override fun createConnection(): Database {
        val databaseName = ComfortAPI.getAPI<ConfigAPI>().getNode().node("database", "database").getString("comfort")
        val path = directory.resolve("$databaseName.db").toString()
        return Database.connect("jdbc:sqlite:$path", driver = "org.sqlite.JDBC")
    }
}