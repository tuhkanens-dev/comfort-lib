package dev.tuhkanens.comfortlib.database.base

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.tuhkanens.comfortlib.ComfortAPI
import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.database.DatabaseBase
import org.jetbrains.exposed.v1.jdbc.Database
import org.spongepowered.configurate.ConfigurationNode

class MySQLBase : DatabaseBase() {

    private lateinit var dataSource: HikariDataSource

    override fun createConnection(): Database {
        val node = ComfortAPI.get<ConfigAPI>().getNode().node("database")

        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://${node.node("host").getString("localhost")}:${node.node("port").getInt(3306)}/${node.node("database").getString("comfort")}?useSSL=false&characterEncoding=utf8"
            username = node.node("user").getString("root") ?: "root"
            password = node.node("password").getString("") ?: ""
            maximumPoolSize = 10
            minimumIdle = 2
            connectionTimeout = 30_000
            idleTimeout = 600_000
            maxLifetime = 1_800_000
        }
        dataSource = HikariDataSource(config)
        return Database.connect(dataSource)
    }

    override fun closeConnection() {
        if (!::dataSource.isInitialized) return
        dataSource.close()
    }

}