package dev.tuhkanens.comfortlib.api;

import dev.tuhkanens.comfortlib.database.base.DatabaseBase;
import dev.tuhkanens.comfortlib.database.base.DatabaseConfig;
import dev.tuhkanens.comfortlib.database.base.DatabaseData;
import dev.tuhkanens.comfortlib.database.base.DatabaseType;
import dev.tuhkanens.comfortlib.database.table.Table;
import org.jdbi.v3.core.Jdbi;

import java.util.Collection;
import java.util.List;

public interface DatabaseAPI {
    void connect();
    void disconnect();

    void setDatabases(Collection<DatabaseConfig> configs);
    void setForceDatabases(Collection<DatabaseConfig> configs);
    void setTables(Collection<Table> tables);

    void setForce(DatabaseType type, boolean isForced);

    DatabaseConfig getConfig(DatabaseType type);
    DatabaseData getDataBase(DatabaseType type);
    DatabaseBase getBase(DatabaseType type);
    Jdbi getDatabase(DatabaseType type);

    List<Table> getTables();
    List<DatabaseType> getTypes();

    List<DatabaseData> getDataBases();

    List<DatabaseBase> getAllBases();
    List<DatabaseBase> getUnforcedBases();
    List<DatabaseBase> getForcedBases();

    boolean getForced(DatabaseType type);

    DatabaseType getProvider();
    boolean isProvider(DatabaseType type);

    void execute(Jdbi database, String sql);
    void execute(DatabaseType type, String sql);
    void execute(Collection<DatabaseType> types, String sql);
    void execute(Collection<DatabaseType> types, boolean isForced, String sql);

    void execute(Jdbi database, Collection<String> sql);
    void execute(DatabaseType type, Collection<String> sql);
    void execute(Collection<DatabaseType> types, Collection<String> sql);
    void execute(Collection<DatabaseType> types, boolean isForced, Collection<String> sql);
}
