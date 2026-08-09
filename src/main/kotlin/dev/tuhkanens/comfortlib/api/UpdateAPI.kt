package dev.tuhkanens.comfortlib.api

import dev.tuhkanens.comfortlib.update.UpdateCheckResult

interface UpdateAPI {
    fun setEnabled(enabled: Boolean)
    fun getEnabled(): Boolean
    fun checkUpdates(): UpdateCheckResult
}