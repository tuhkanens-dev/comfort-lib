package dev.tuhkanens.comfortlib

import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.api.ConfigureAPI
import dev.tuhkanens.comfortlib.api.implementation.DatabaseImpl
import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.api.MessagesAPI
import dev.tuhkanens.comfortlib.api.UpdateAPI
import dev.tuhkanens.comfortlib.api.implementation.ConfigImpl
import dev.tuhkanens.comfortlib.api.implementation.ConfigureImpl
import dev.tuhkanens.comfortlib.api.implementation.MessagesImpl
import dev.tuhkanens.comfortlib.api.implementation.UpdateImpl
import org.slf4j.Logger

object Comfort {

    private lateinit var logger: Logger

    fun setInstance(logger: Logger) {
        this.logger = logger

        ComfortAPI.apply {
            registerAPI<ConfigureAPI>(ConfigureImpl())
            registerAPI<ConfigAPI>(ConfigImpl())
            registerAPI<MessagesAPI>(MessagesImpl())
            registerAPI<DatabaseAPI>(DatabaseImpl())
            registerAPI<UpdateAPI>(UpdateImpl())
        }
    }

    fun getLogger(): Logger {
        return logger
    }

}