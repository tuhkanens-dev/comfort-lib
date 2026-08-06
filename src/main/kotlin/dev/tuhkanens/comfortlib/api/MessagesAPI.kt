package dev.tuhkanens.comfortlib.api

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.spongepowered.configurate.ConfigurationNode
import java.nio.file.Path

interface MessagesAPI {
    fun create(clazz: Class<*>, directory: Path)
    fun reload()
    fun getNode(): ConfigurationNode
    fun getString(path: String): String
    fun getStringList(path: String): List<String>
    fun getStringLore(path: String): String
    fun getMessage(path: String, vararg resolvers: TagResolver): String
    fun getMessageList(path: String, vararg resolvers: TagResolver): List<String>
    fun getMessageLore(path: String, vararg resolvers: TagResolver): String
}

inline fun <reified T : Any> MessagesAPI.create(directory: Path) =
    create(T::class.java, directory)