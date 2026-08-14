package dev.tuhkanens.comfortlib.database

import dev.tuhkanens.comfortlib.ComfortAPI
import dev.tuhkanens.comfortlib.api.DatabaseAPI
import org.jetbrains.exposed.v1.core.Table

abstract class DatabaseBootstrap {

    private val api: DatabaseAPI = ComfortAPI.get<DatabaseAPI>()

    open val bases: Array<DatabaseConfig> = emptyArray()
    open val forceBases: Array<DatabaseConfig> = emptyArray()
    open val tables: Array<Table> = emptyArray()

    open fun connect() {
        if (bases.isEmpty()) return
        api.setDatabases(*bases)
        api.setForceDatabases(*forceBases)
        api.setTables(*tables)
        api.connect()
    }

    open fun disconnect() {
        api.disconnect()
    }

}