package dev.tuhkanens.comfortlib.api.implementation

import dev.tuhkanens.comfortlib.api.ConfigureAPI

class ConfigureImpl : ConfigureAPI {

    private var projectId: String = "UNKNOWN"
    private var version: String = "UNKNOWN"

    override fun setProjectId(projectId: String) {
        this.projectId = projectId
    }

    override fun setVersion(version: String) {
        this.version = version
    }

    override fun getProjectId(): String {
        return projectId
    }

    override fun getVersion(): String {
        return version
    }

}