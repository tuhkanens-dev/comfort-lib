package dev.tuhkanens.comfortlib.implementation;

import dev.tuhkanens.comfortlib.api.MessagesAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class MessagesImpl implements MessagesAPI {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacySection();

    private ConfigurationNode root;
    private Path path;

    @Override
    public void create(Class<?> clazz, Path directory) {
        String fileName = "messages.yml";

        path = directory.resolve(fileName);

        if (!Files.exists(path)) {
            try (InputStream input = clazz.getClassLoader().getResourceAsStream(fileName)) {
                if (input == null) {
                    throw new IllegalStateException("Default messages.yml not found in resources");
                }
                Files.createDirectories(directory);
                Files.copy(input, path);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to copy messages.yml", e);
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

    @Override
    public String getString(String path) {
        String[] keys = path.split("\\.");
        return root.node((Object[]) keys).getString(path);
    }

    @Override
    public List<String> getStringList(String path) {
        String[] keys = path.split("\\.");
        try {
            return root.node((Object[]) keys).getList(String.class);
        } catch (SerializationException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String getStringLore(String path) {
        List<String> lines = getStringList(path);
        return String.join("\n", lines);
    }

    @Override
    public String getMessage(String path, TagResolver... resolvers) {
        String string = getString(path);
        Component component = miniMessage.deserialize(string, resolvers);
        return legacyComponentSerializer.serialize(component);
    }

    @Override
    public List<String> getMessageList(String path, TagResolver... resolvers) {
        return getStringList(path)
                .stream()
                .map(line -> {
                   Component component = miniMessage.deserialize(line, resolvers);
                   return legacyComponentSerializer.serialize(component);
                })
                .toList();
    }

    @Override
    public String getMessageLore(String path, TagResolver... resolvers) {
        String string = getStringLore(path);
        Component component = miniMessage.deserialize(string, resolvers);
        return legacyComponentSerializer.serialize(component);
    }

    private void load() {
        YamlConfigurationLoader yaml = YamlConfigurationLoader.builder()
                .path(path)
                .build();
        try {
            root = yaml.load();
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to load file", e);
        }
    }
}
