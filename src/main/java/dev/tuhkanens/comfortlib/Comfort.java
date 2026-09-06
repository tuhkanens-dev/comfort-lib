package dev.tuhkanens.comfortlib;

import org.slf4j.Logger;

import java.nio.file.Path;

public class Comfort {

    private static final Comfort INSTANCE = new Comfort();

    private Logger logger;
    private Path directory;

    private Comfort() {}

    public static Comfort getInstance() {
        return INSTANCE;
    }

    public void onEnable(Logger logger, Path directory) {
        this.logger = logger;
        this.directory = directory;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDirectory() {
        return directory;
    }

}
