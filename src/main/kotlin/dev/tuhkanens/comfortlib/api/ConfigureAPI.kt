package dev.tuhkanens.comfortlib.api

interface ConfigureAPI {
    fun setProjectId(projectId: String)
    fun setVersion(version: String)
    fun getProjectId(): String
    fun getVersion(): String
}