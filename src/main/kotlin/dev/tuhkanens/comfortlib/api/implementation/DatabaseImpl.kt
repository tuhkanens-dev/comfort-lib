package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.ComfortAPI
import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.database.DatabaseBase
import dev.tuhkanens.comfortlib.database.DatabaseConfig
import dev.tuhkanens.comfortlib.database.DatabaseData
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
    private val bases: ConcurrentHashMap<DatabaseType, DatabaseData> = ConcurrentHashMap()
    private val tables: MutableSet<Table> = ConcurrentHashMap.newKeySet()

    override fun setDatabases(vararg config: DatabaseConfig) {
        setDatabases(*config, force = false)
    }

    override fun setForceDatabases(vararg config: DatabaseConfig) {
        setDatabases(*config, force = true)
    }

    private fun setDatabases(vararg config: DatabaseConfig, force: Boolean = false) {
        if (config.isEmpty()) return
        for (c in config) {
            val type = when (c) {
                is DatabaseConfig.Sqlite -> {
                    if (!force && !isProvider(DatabaseType.SQLITE)) return
                    val base = SQLiteBase(c.directory)
                    bases[DatabaseType.SQLITE] = DatabaseData(base, force)
                    DatabaseType.SQLITE
                }
                is DatabaseConfig.Mysql -> {
                    if (!force && !isProvider(DatabaseType.MYSQL)) return
                    val base = MySQLBase()
                    bases[DatabaseType.MYSQL] = DatabaseData(base, force)
                    DatabaseType.MYSQL
                }
            }
            this.configs[type] = c
        }
    }

    override fun setTables(vararg table: Table) {
        this.tables.addAll(table)
    }

    override fun setForce(type: DatabaseType, isForced: Boolean) {
        getBaseData(type)?.isForced = isForced
    }

    override fun getTables(): Array<Table> {
        return tables.toTypedArray()
    }

    override fun getTypes(): List<DatabaseType> {
        return bases.keys.toList()
    }

    override fun getConfig(type: DatabaseType): DatabaseConfig? {
        return configs[type]
    }

    override fun getAllBases(): List<DatabaseBase> {
        return bases.values
            .map { it.base }
    }

    override fun getUnforcedBases(): List<DatabaseBase> {
        return bases.values
            .filter { !it.isForced }
            .map { it.base }
    }

    override fun getForcedBases(): List<DatabaseBase> {
        return bases.values
            .filter { it.isForced }
            .map { it.base }
    }

    override fun getBaseData(type: DatabaseType): DatabaseData? {
        return bases[type]
    }

    override fun getBase(type: DatabaseType): DatabaseBase? {
        return getBaseData(type)?.base
    }

    override fun getDatabase(type: DatabaseType): Database? {
        return getBase(type)?.getDatabase()
    }

    override fun getProvider(): DatabaseType {
        val rawProvider = ComfortAPI.get<ConfigAPI>()
            .getNode()
            .node("database", "provider")
            .getString("sqlite")
            .uppercase()
        return runCatching { DatabaseType.valueOf(rawProvider) }
            .getOrDefault(DatabaseType.SQLITE)
    }

    override fun isProvider(type: DatabaseType): Boolean {
        return getProvider() == type
    }

    override fun isForced(type: DatabaseType): Boolean {
        return getBaseData(type)?.isForced ?: false
    }

    override fun connect() {
        bases.values.forEach { it.base.connect() }

        if (tables.isNotEmpty()) {
            val tablesArray = tables.toTypedArray()
            for (data in bases.values) {
                val db = data.base.getDatabase()
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
        for (type in DatabaseType.entries) {
            getBase(type)?.disconnect()
        }
    }

    override fun <T> transaction(type: DatabaseType, block: () -> T): T {
        val db = getDatabase(type) ?: throw IllegalStateException("Database $type is not connected")
        return transaction(db) {
            block()
        }
    }

    override fun <T> transaction(vararg type: DatabaseType, force: Boolean, block: () -> T): T {
        if (type.isEmpty()) {
            return block()
        }
        fun executeNested(index: Int): T {
            if (index >= type.size) {
                return block()
            }
            val currentType = type[index]
            val db = getDatabase(currentType) ?: throw IllegalStateException("Database $currentType is not connected")
            val data = getBaseData(currentType) ?: throw IllegalStateException("DatabaseData for $currentType not found")

            if (force != data.isForced) {
                return executeNested(index + 1)
            }

            return transaction(db) {
                executeNested(index + 1)
            }
        }

        return executeNested(0)
    }

    override fun <T> transaction(force: Boolean, block: () -> T): T {
        val bases = bases.keys.toTypedArray()
        return transaction(*bases, force = force) {
            block()
        }
    }

}