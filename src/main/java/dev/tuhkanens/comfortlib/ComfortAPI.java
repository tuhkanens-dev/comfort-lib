package dev.tuhkanens.comfortlib;

import dev.tuhkanens.comfortlib.api.ConfigAPI;
import dev.tuhkanens.comfortlib.api.DatabaseAPI;
import dev.tuhkanens.comfortlib.api.MessagesAPI;
import dev.tuhkanens.comfortlib.api.UpdateAPI;
import dev.tuhkanens.comfortlib.implementation.ConfigImpl;
import dev.tuhkanens.comfortlib.implementation.DatabaseImpl;
import dev.tuhkanens.comfortlib.implementation.MessagesImpl;
import dev.tuhkanens.comfortlib.implementation.UpdateImpl;

public final class ComfortAPI {

    private ComfortAPI() {}

    public static ConfigAPI CONFIG;
    public static MessagesAPI MESSAGES;
    public static UpdateAPI UPDATE;
    public static DatabaseAPI DATABASE;

    static {
        if (hasClass("org.spongepowered.configurate.ConfigurationNode") &&
                hasClass("org.spongepowered.configurate.yaml.YamlConfigurationLoader"))
        {
            CONFIG = new ConfigImpl();

            if (hasClass("net.kyori.adventure.text.minimessage.MiniMessage") &&
                    hasClass("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer"))
            {
                MESSAGES = new MessagesImpl();
            }
        }

        if (hasClass("com.google.gson.Gson")) {
            UPDATE = new UpdateImpl();
        }

        if (hasClass("org.jdbi.v3.core.Jdbi")) {
            DATABASE = new DatabaseImpl();
        }
    }

    private static boolean hasClass(String name) {
        try {
            Class.forName(name, false, ComfortAPI.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
