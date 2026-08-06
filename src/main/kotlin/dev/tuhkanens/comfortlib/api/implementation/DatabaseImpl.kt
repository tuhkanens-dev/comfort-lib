package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseType
import dev.tuhkanens.comfortlib.database.base.MySQLBase
import dev.tuhkanens.comfortlib.database.base.SQLiteBase
import org.jetbrains.exposed.v1.jdbc.Database
import java.util.concurrent.ConcurrentHashMap

class DatabaseImpl : DatabaseAPI {

    private val configs: ConcurrentHashMap<DatabaseType, DatabaseConfig> = ConcurrentHashMap()
    private val bases: ConcurrentHashMap<DatabaseType, DatabaseBase> = ConcurrentHashMap()

    override fun create(vararg configs: DatabaseConfig) {
        for (config in configs) {
            val type = when (config) {
                is DatabaseConfig.Sqlite -> {
                    val base = SQLiteBase(config.directory)
                    bases[DatabaseType.SQLITE] = base
                    DatabaseType.SQLITE
                }
                is DatabaseConfig.Mysql -> {
                    val base = MySQLBase(config.configuration)
                    bases[DatabaseType.SQLITE] = base
                    DatabaseType.MYSQL
                }
            }
            this.configs[type] = config
        }
    }

    override fun getTypes(): List<DatabaseType> {
        return configs.keys.toList()
    }

    override fun getConfig(type: DatabaseType): DatabaseConfig? {
        return configs[type]
    }

    override fun getBase(type: DatabaseType): DatabaseBase? {
        return bases[type]
    }

    override fun getDatabase(type: DatabaseType): Database? {
        return getBase(type)?.getDatabase()
    }

    override fun connect() {
        for (type in getTypes()) {
            getBase(type)?.connect()
        }
    }

    override fun disconnect() {
        for (type in getTypes()) {
            getBase(type)?.disconnect()
        }
    }

}