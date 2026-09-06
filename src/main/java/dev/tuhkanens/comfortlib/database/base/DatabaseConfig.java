package dev.tuhkanens.comfortlib.database.base;

import java.nio.file.Path;

public sealed interface DatabaseConfig {

    record Sqlite(
            Path directory
    ) implements DatabaseConfig {}

    enum Mysql implements DatabaseConfig {
        INSTANCE
    }

}
