package dev.tuhkanens.comfortlib.implementation;

import dev.tuhkanens.comfortlib.api.ConfigAPI;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigImpl implements ConfigAPI {

    private ConfigurationNode root;
    private Path path;

    @Override
    public void create(Class<?> clazz, Path directory) {
        String fileName = "config.yml";

        path = directory.resolve(fileName);

        if (!Files.exists(path)) {
            try (InputStream input = clazz.getClassLoader().getResourceAsStream(fileName)) {
                if (input == null) {
                    throw new IllegalStateException("Default config.yml not found in resources!");
                }
                Files.createDirectories(directory);
                Files.copy(input, path);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to copy config.yml", e);
            }
        }

        load();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public ConfigurationNode getNode() {
        return root;
    }

    private void load() {
        YamlConfigurationLoader yaml = YamlConfigurationLoader.builder()
                .path(path)
                .build();
        try {
            root = yaml.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

}
