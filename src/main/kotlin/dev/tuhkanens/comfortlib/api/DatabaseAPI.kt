package dev.tuhkanens.comfortlib.api

import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseData
import dev.tuhkanens.comfortlib.database.DatabaseType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database

interface DatabaseAPI {
    fun connect()
    fun disconnect()

    fun <T> transaction(type: DatabaseType, block: () -> T): T
    fun <T> transaction(vararg type: DatabaseType, force: Boolean = false, block: () -> T): T
    fun <T> transaction(force: Boolean = false, block: () -> T): T

    fun setDatabases(vararg config: DatabaseConfig)
    fun setForceDatabases(vararg config: DatabaseConfig)
    fun setTables(vararg table: Table)

    fun setForce(type: DatabaseType, isForced: Boolean)

    fun getTables(): Array<Table>
    fun getTypes(): List<DatabaseType>
    fun getConfig(type: DatabaseType): DatabaseConfig?

    fun getAllBases(): List<DatabaseBase>
    fun getUnforcedBases(): List<DatabaseBase>
    fun getForcedBases(): List<DatabaseBase>

    fun getBaseData(type: DatabaseType): DatabaseData?
    fun getBase(type: DatabaseType): DatabaseBase?

    fun getDatabase(type: DatabaseType): Database?

    fun getProvider(): DatabaseType
    fun isProvider(type: DatabaseType): Boolean

    fun isForced(type: DatabaseType): Boolean
}