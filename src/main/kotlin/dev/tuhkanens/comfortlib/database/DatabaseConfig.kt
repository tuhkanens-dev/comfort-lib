package dev.tuhkanens.comfortlib.database

import java.nio.file.Path

sealed class DatabaseConfig {
    data class Sqlite(val directory: Path) : DatabaseConfig()
    object Mysql : DatabaseConfig()
}