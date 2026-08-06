package dev.tuhkanens.comfortlib.database

import org.jetbrains.exposed.v1.jdbc.Database

abstract class DatabaseBase {

    private lateinit var database: Database

    protected abstract fun createConnection(): Database
    protected open fun closeConnection() {}

    fun connect() {
        database = createConnection()
    }

    fun disconnect() {
        if (!::database.isInitialized) return
        closeConnection()
    }

    fun getDatabase(): Database {
        return database
    }

}