package dev.tuhkanens.comfortlib.api

import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database

interface DatabaseAPI {
    fun connect()
    fun disconnect()

    fun transaction(type: DatabaseType, block: () -> Unit)
    fun transaction(vararg type: DatabaseType, block: () -> Unit)

    fun setForceDatabases(vararg config: DatabaseConfig)
    fun setDatabases(vararg config: DatabaseConfig)
    fun setTables(vararg table: Table)

    fun getTables(): Array<Table>
    fun getTypes(): List<DatabaseType>
    fun getConfig(type: DatabaseType): DatabaseConfig?
    fun getBase(type: DatabaseType): DatabaseBase?
    fun getDatabase(type: DatabaseType): Database?
}