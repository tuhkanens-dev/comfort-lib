package dev.tuhkanens.comfortlib.database

import org.spongepowered.configurate.ConfigurationNode
import java.nio.file.Path

sealed class DatabaseConfig {
    data class Sqlite(val directory: Path) : DatabaseConfig()
    data class Mysql(val configuration: ConfigurationNode) : DatabaseConfig()
}