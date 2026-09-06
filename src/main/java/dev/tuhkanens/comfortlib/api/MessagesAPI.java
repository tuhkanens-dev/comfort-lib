package dev.tuhkanens.comfortlib.api;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.List;

public interface MessagesAPI {
    void create(Class<?> clazz, Path directory);
    void reload();

    ConfigurationNode getNode();
    String getString(String path);
    List<String> getStringList(String path);
    String getStringLore(String path);

    String getMessage(String path, TagResolver... resolvers);
    List<String> getMessageList(String path, TagResolver... resolvers);
    String getMessageLore(String path, TagResolver... resolvers);
}