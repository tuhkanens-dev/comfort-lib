package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseType
import dev.tuhkanens.comfortlib.database.base.MySQLBase
import dev.tuhkanens.comfortlib.database.base.SQLiteBase
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import java.util.concurrent.ConcurrentHashMap

class DatabaseImpl : DatabaseAPI {

    private val configs: ConcurrentHashMap<DatabaseType, DatabaseConfig> = ConcurrentHashMap()
    private val bases: ConcurrentHashMap<DatabaseType, DatabaseBase> = ConcurrentHashMap()
    private val tables: MutableSet<Table> = ConcurrentHashMap.newKeySet()

    override fun setDatabases(vararg config: DatabaseConfig) {
        for (c in config) {
            val type = when (c) {
                is DatabaseConfig.Sqlite -> {
                    val base = SQLiteBase(c.directory)
                    bases[DatabaseType.SQLITE] = base
                    DatabaseType.SQLITE
                }
                is DatabaseConfig.Mysql -> {
                    val base = MySQLBase(c.configuration)
                    if (!c.configuration.node("database", "provider").getString("sqlite").equals("mysql")) return
                    bases[DatabaseType.MYSQL] = base
                    DatabaseType.MYSQL
                }
            }
            this.configs[type] = c
        }
    }

    override fun setTables(vararg table: Table) {
        this.tables.addAll(table)
    }

    override fun getTables(): Array<Table> {
        return tables.toTypedArray()
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
        bases.values.forEach { it.connect() }

        if (tables.isNotEmpty()) {
            val tablesArray = tables.toTypedArray()
            for ((_, base) in bases) {
                val db = base.getDatabase()
                transaction(db) {
                    val statements = MigrationUtils.statementsRequiredForDatabaseMigration(*tablesArray)
                    if (statements.isNotEmpty()) {
                        statements.forEach { exec(it) }
                    } else {
                        SchemaUtils.create(*tablesArray)
                    }
                }
            }
        }
    }

    override fun disconnect() {
        for (type in getTypes()) {
            getBase(type)?.disconnect()
        }
    }

    override fun transaction(type: DatabaseType, block: () -> Unit) {
        val db = getDatabase(type) ?: error("Database $type is not connected")
        transaction(db) {
            block()
        }
    }

    override fun transaction(vararg type: DatabaseType, block: () -> Unit) {
        if (type.isEmpty()) return
        fun executeNested(index: Int) {
            if (index >= type.size) {
                block()
                return
            }
            val db = getDatabase(type[index]) ?: error("Database ${type[index]} is not connected")
            transaction(db) {
                executeNested(index + 1)
            }
        }

        executeNested(0)
    }

}