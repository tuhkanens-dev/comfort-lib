package dev.tuhkanens.comfortlib

import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.api.implementation.DatabaseImpl
import dev.tuhkanens.comfortlib.api.DatabaseAPI
import dev.tuhkanens.comfortlib.api.MessagesAPI
import dev.tuhkanens.comfortlib.api.implementation.ConfigImpl
import dev.tuhkanens.comfortlib.api.implementation.MessagesImpl

object Comfort {

    fun onLoad() {
        ComfortAPI.apply {
            registerAPI<DatabaseAPI>(DatabaseImpl())
            registerAPI<ConfigAPI>(ConfigImpl())
            registerAPI<MessagesAPI>(MessagesImpl())
        }
    }

}