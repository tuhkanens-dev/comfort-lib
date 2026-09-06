package dev.tuhkanens.comfortlib.database.base.provider;

import dev.tuhkanens.comfortlib.ComfortAPI;
import dev.tuhkanens.comfortlib.database.base.DatabaseBase;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Path;

public class SQLiteBase extends DatabaseBase {

    private final Path directory;

    public SQLiteBase(Path directory) {
        this.directory = directory;
    }

    @Override
    protected Jdbi createConnection() {
        String databaseName = ComfortAPI.CONFIG
                .getNode()
                .node("database", "database")
                .getString("comfort");

        Path databasePath = directory.resolve(databaseName + ".db");
        return Jdbi.create("jdbc:sqlite:" + databasePath);
    }
}
