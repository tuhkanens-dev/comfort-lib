package dev.tuhkanens.comfortlib.api

import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseType
import org.jetbrains.exposed.v1.jdbc.Database

interface DatabaseAPI {
    fun create(vararg configs: DatabaseConfig)
    fun connect()
    fun disconnect()

    fun getTypes(): List<DatabaseType>
    fun getConfig(type: DatabaseType): DatabaseConfig?
    fun getBase(type: DatabaseType): DatabaseBase?
    fun getDatabase(type: DatabaseType): Database?
}