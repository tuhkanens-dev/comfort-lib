package dev.tuhkanens.comfortlib.database.table;

public enum ColumnType {
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    VARCHAR("VARCHAR(255)"),
    TEXT("TEXT"),
    BOOLEAN("BOOLEAN"),
    DOUBLE("DOUBLE");

    private final String sql;

    ColumnType(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
