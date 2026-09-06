package dev.tuhkanens.comfortlib.database.table;

import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final String name;
    private final List<String> columns = new ArrayList<>();

    private Table(String name) {
        this.name = name;
    }

    public void create(Jdbi jdbi) {
        String sql = """
                CREATE TABLE IF NOT EXISTS %s (
                    %s
                )
                """.formatted(name, String.join(",\n", columns)
        );

        jdbi.useHandle(handle -> handle.execute(sql));
    }

    public Table column(String name, ColumnType type) {
        columns.add(name + " " + type.sql());
        return this;
    }

    public Table primaryKey(String column) {
        columns.add("PRIMARY KEY (" + column + ")");
        return this;
    }

    public String getName() {
        return name;
    }

}
