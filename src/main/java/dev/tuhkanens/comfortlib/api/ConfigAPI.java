package dev.tuhkanens.comfortlib.api;

import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;

public interface ConfigAPI {
    void create(Class<?> clazz, Path directory);
    void reload();
    ConfigurationNode getNode();
}
