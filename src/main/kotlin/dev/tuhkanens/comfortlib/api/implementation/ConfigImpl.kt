package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.api.ConfigAPI
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Files
import java.nio.file.Path

class ConfigImpl : ConfigAPI {

    private lateinit var root: ConfigurationNode
    private lateinit var yaml: YamlConfigurationLoader
    private lateinit var path: Path

    companion object {
        private const val FILE_NAME = "config.yml"
    }

    override fun create(clazz: Class<*>, directory: Path) {
        path = directory.resolve(FILE_NAME)

        Files.createDirectories(directory)

        if (!Files.exists(path)) {
            val inputStream = clazz.classLoader.getResourceAsStream(FILE_NAME)
                ?: throw IllegalStateException("Default config.yml not found in core resources!")
            inputStream.use { stream ->
                Files.copy(stream, path)
            }
        }

        load()
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