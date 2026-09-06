package dev.tuhkanens.comfortlib.database.base;

public class DatabaseData {

    private final DatabaseBase base;
    private boolean forced;

    public DatabaseData(DatabaseBase base, boolean isForced) {
        this.base = base;
        this.forced = isForced;
    }

    public DatabaseBase getBase() {
        return base;
    }

    public boolean getForced() {
        return forced;
    }

    public void setForced(boolean isForced) {
        this.forced = isForced;
    }

}
