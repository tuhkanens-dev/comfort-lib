package dev.tuhkanens.comfortlib.api

import org.spongepowered.configurate.ConfigurationNode
import java.nio.file.Path

interface ConfigAPI {
    fun create(clazz: Class<*>, directory: Path)
    fun reload()
    fun getNode(): ConfigurationNode
}

inline fun <reified T : Any> ConfigAPI.create(directory: Path) =
    create(T::class.java, directory)