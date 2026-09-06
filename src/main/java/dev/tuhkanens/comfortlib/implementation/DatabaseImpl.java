package dev.tuhkanens.comfortlib.implementation;

import dev.tuhkanens.comfortlib.ComfortAPI;
import dev.tuhkanens.comfortlib.api.DatabaseAPI;
import dev.tuhkanens.comfortlib.database.base.DatabaseBase;
import dev.tuhkanens.comfortlib.database.base.DatabaseConfig;
import dev.tuhkanens.comfortlib.database.base.DatabaseData;
import dev.tuhkanens.comfortlib.database.base.DatabaseType;
import dev.tuhkanens.comfortlib.database.base.provider.MySQLBase;
import dev.tuhkanens.comfortlib.database.base.provider.SQLiteBase;
import dev.tuhkanens.comfortlib.database.table.Table;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseImpl implements DatabaseAPI {

    private final ConcurrentHashMap<DatabaseType, DatabaseConfig> configs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<DatabaseType, DatabaseData> bases = new ConcurrentHashMap<>();
    private final List<Table> tables = new ArrayList<>();

    @Override
    public void setDatabases(Collection<DatabaseConfig> configs) {
        setDatabases(false, configs);
    }

    @Override
    public void setForceDatabases(Collection<DatabaseConfig> configs) {
        setDatabases(true, configs);
    }

    private void setDatabases(boolean isForced, Collection<DatabaseConfig> configs) {
        for (DatabaseConfig config : configs) {
            DatabaseType type;
            switch (config) {
                case DatabaseConfig.Sqlite sqlite -> {
                    if (!isForced && !isProvider(DatabaseType.SQLITE)) continue;
                    DatabaseBase base = new SQLiteBase(sqlite.directory());
                    type = DatabaseType.SQLITE;
                    this.bases.put(type, new DatabaseData(base, isForced));
                }
                case DatabaseConfig.Mysql mysql -> {
                    if (!isForced && !isProvider(DatabaseType.MYSQL)) continue;
                    DatabaseBase base = new MySQLBase();
                    type = DatabaseType.MYSQL;
                    this.bases.put(type, new DatabaseData(base, isForced));
                }
            }
            this.configs.put(type, config);
        }
    }

    @Override
    public void setTables(Collection<Table> tables) {
        this.tables.clear();
        this.tables.addAll(tables);
    }

    @Override
    public void setForce(DatabaseType type, boolean isForced) {
        getDataBase(type).setForced(isForced);
    }

    @Override
    public List<Table> getTables() {
        return tables;
    }

    @Override
    public List<DatabaseType> getTypes() {
        return new ArrayList<>(configs.keySet());
    }

    @Override
    public DatabaseConfig getConfig(DatabaseType type) {
        return configs.get(type);
    }

    @Override
    public DatabaseData getDataBase(DatabaseType type) {
        return bases.get(type);
    }

    @Override
    public DatabaseBase getBase(DatabaseType type) {
        return getDataBase(type).getBase();
    }

    @Override
    public Jdbi getDatabase(DatabaseType type) {
        return getBase(type).getDatabase();
    }

    @Override
    public List<DatabaseData> getDataBases() {
        return new ArrayList<>(bases.values());
    }

    @Override
    public List<DatabaseBase> getAllBases() {
        return getDataBases()
                .stream()
                .map(DatabaseData::getBase)
                .toList();
    }

    @Override
    public List<DatabaseBase> getUnforcedBases() {
        return getDataBases()
                .stream()
                .filter(base -> !base.getForced())
                .map(DatabaseData::getBase)
                .toList();
    }

    @Override
    public List<DatabaseBase> getForcedBases() {
        return getDataBases()
                .stream()
                .filter(DatabaseData::getForced)
                .map(DatabaseData::getBase)
                .toList();
    }

    @Override
    public DatabaseType getProvider() {
        String rawProvider = ComfortAPI.CONFIG
                .getNode()
                .node("database", "provider")
                .getString("sqlite")
                .toUpperCase();
        try {
            return DatabaseType.valueOf(rawProvider);
        } catch (IllegalArgumentException e) {
            return DatabaseType.SQLITE;
        }
    }

    @Override
    public boolean isProvider(DatabaseType type) {
        return getProvider() == type;
    }

    @Override
    public boolean getForced(DatabaseType type) {
        return getDataBase(type).getForced();
    }

    @Override
    public void connect() {
        getAllBases().forEach(DatabaseBase::connect);

        if (tables.isEmpty()) return;

        for (DatabaseBase base : getAllBases()) {
            Jdbi database = base.getDatabase();
            for (Table table : tables) {
                table.create(database);
            }
        }
    }

    @Override
    public void disconnect() {
        for (DatabaseType type : DatabaseType.values()) {
            getBase(type).disconnect();
        }
    }

    @Override
    public void execute(Jdbi database, String sql) {
        database.useHandle(handle -> handle.execute(sql));
    }

    @Override
    public void execute(DatabaseType type, String sql) {
        execute(getDatabase(type), sql);
    }

    @Override
    public void execute(Collection<DatabaseType> types, String sql) {
        for (DatabaseType type : types) {
            execute(type, sql);
        }
    }

    @Override
    public void execute(Collection<DatabaseType> types, boolean isForced, String sql) {
        for (DatabaseType type : types) {
            if (getForced(type) == isForced) {
                execute(type, sql);
            }
        }
    }

    @Override
    public void execute(Jdbi database, Collection<String> sql) {
        for (String s : sql) {
            execute(database, s);
        }
    }

    @Override
    public void execute(DatabaseType type, Collection<String> sql) {
        execute(getDatabase(type), sql);
    }

    @Override
    public void execute(Collection<DatabaseType> types, Collection<String> sql) {
        for (DatabaseType type : types) {
            execute(type, sql);
        }
    }

    @Override
    public void execute(Collection<DatabaseType> types, boolean isForced, Collection<String> sql) {
        for (DatabaseType type : types) {
            if (getForced(type) == isForced) {
                execute(type, sql);
            }
        }
    }
}
