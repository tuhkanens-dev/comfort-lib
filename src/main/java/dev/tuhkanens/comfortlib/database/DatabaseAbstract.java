package dev.tuhkanens.comfortlib.database;

import dev.tuhkanens.comfortlib.ComfortAPI;
import dev.tuhkanens.comfortlib.api.DatabaseAPI;
import dev.tuhkanens.comfortlib.database.base.DatabaseConfig;
import dev.tuhkanens.comfortlib.database.table.Table;

import java.util.List;

public abstract class DatabaseAbstract {

    protected final DatabaseAPI api = ComfortAPI.DATABASE;

    protected abstract List<DatabaseConfig> getBases();
    protected abstract List<DatabaseConfig> getForcedBases();
    protected abstract List<Table> getTables();

    protected void connect() {
        List<DatabaseConfig> bases = getBases();

        if (bases.isEmpty()) return;
        api.setDatabases(bases);
        api.setForceDatabases(getForcedBases());
        api.setTables(getTables());
        api.connect();
    }

    protected void disconnect() {
        api.disconnect();
    }

}
