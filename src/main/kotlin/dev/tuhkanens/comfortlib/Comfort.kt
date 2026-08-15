package dev.tuhkanens.comfortlib

import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.api.implementation.DatabaseImpl
import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.api.MessagesAPI
import dev.tuhkanens.comfortlib.api.UpdateAPI
import dev.tuhkanens.comfortlib.api.implementation.ConfigImpl
import dev.tuhkanens.comfortlib.api.implementation.MessagesImpl
import dev.tuhkanens.comfortlib.api.implementation.UpdateImpl
import org.slf4j.Logger
import java.nio.file.Path

object Comfort {

    private lateinit var logger: Logger
    private lateinit var directory: Path

    fun onEnable(logger: Logger, directory: Path) {
        this.logger = logger
        this.directory = directory

        ComfortAPI.apply {
            if (hasClass("org.spongepowered.configurate") &&
                hasClass("org.spongepowered.configurate.yaml"))
            {
                register<ConfigAPI>(ConfigImpl())

                if (hasClass("net.kyori.adventure.text.minimessage") &&
                    hasClass("net.kyori.adventure.text.serializer.legacy"))
                {
                    register<MessagesAPI>(MessagesImpl())
                }
            }

            if (hasClass("org.jetbrains.exposed.v1.core") &&
                hasClass("org.jetbrains.exposed.v1.jdbc") &&
                hasClass("org.jetbrains.exposed.v1.migration.jdbc"))
            {
                register<DatabaseAPI>(DatabaseImpl())
            }

            if (hasClass("com.google.gson")) {
                register<UpdateAPI>(UpdateImpl())
            }
        }
    }

    fun getLogger(): Logger {
        return logger
    }

    fun getDirectory(): Path {
        return directory
    }

    private fun hasClass(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

}