package dev.tuhkanens.comfortlib.database.base;

import org.jdbi.v3.core.Jdbi;

public abstract class DatabaseBase {

    private Jdbi database;

    protected abstract Jdbi createConnection();
    protected void closeConnection() {}

    public void connect() {
        database = createConnection();
    }

    public void disconnect() {
        if (database == null) return;
        closeConnection();
    }

    public Jdbi getDatabase() {
        return database;
    }

}
