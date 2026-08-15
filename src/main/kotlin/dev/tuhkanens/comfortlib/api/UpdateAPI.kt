package dev.tuhkanens.comfortlib.api

import dev.tuhkanens.comfortlib.update.UpdateCheckResult

interface UpdateAPI {
    fun setEnabled(enabled: Boolean)
    fun setProjectId(projectId: String)
    fun setVersion(version: String)

    fun getEnabled(): Boolean
    fun getProjectId(): String
    fun getVersion(): String

    fun checkUpdates(): UpdateCheckResult
}