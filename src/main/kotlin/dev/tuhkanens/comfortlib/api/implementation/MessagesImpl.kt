package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.api.MessagesAPI
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Files
import java.nio.file.Path

class MessagesImpl : MessagesAPI {

    private val miniMessage = MiniMessage.miniMessage()

    private val legacySerializer: LegacyComponentSerializer = LegacyComponentSerializer.legacySection()

    private lateinit var root: ConfigurationNode
    private lateinit var yaml: YamlConfigurationLoader
    private lateinit var path: Path

    companion object {
        private const val FILE_NAME = "messages.yml"
    }

    override fun create(clazz: Class<*>, directory: Path) {
        if (!Files.exists(path)) {
            val inputStream = this::class.java.classLoader.getResourceAsStream(FILE_NAME)
                ?: throw IllegalStateException("Default messages.yml not found in core resources!")

            inputStream.use { stream ->
                Files.copy(stream, path)
            }
        }

        load()

    }

    override fun getString(path: String): String {
        val keys = path.split(".").toTypedArray()
        val string = root.node(*keys).getString(path) ?: path
        return string
    }

    override fun getStringList(path: String): List<String> {
        val keys = path.split(".").toTypedArray()
        val lines = root.node(*keys).getList(String::class.java) ?: emptyList()
        return lines
    }

    override fun getStringLore(path: String): String {
        val string = this.getStringList(path).joinToString("\n")
        return string
    }

    override fun getMessage(path: String, vararg resolvers: TagResolver): String {
        val string = this.getString(path)
        val component = miniMessage.deserialize(string, *resolvers)
        return legacySerializer.serialize(component)
    }

    override fun getMessageList(path: String, vararg resolvers: TagResolver): List<String> {
        return getStringList(path).map { line ->
            val component = miniMessage.deserialize(line, *resolvers)
            legacySerializer.serialize(component)
        }
    }

    override fun getMessageLore(path: String, vararg resolvers: TagResolver): String {
        val string = getMessageList(path).joinToString("\n")
        val component = miniMessage.deserialize(string, *resolvers)
        return legacySerializer.serialize(component)
    }

    override fun getNode(): ConfigurationNode = root
    override fun reload() = load()

    private fun load() {
        yaml = YamlConfigurationLoader.builder()
            .path(path)
            .build()
        root = yaml.load()
    }

}