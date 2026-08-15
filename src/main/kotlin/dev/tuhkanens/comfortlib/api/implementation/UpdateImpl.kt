package dev.tuhkanens.comfortlib.api.implementation

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.tuhkanens.comfortlib.Comfort
import dev.tuhkanens.comfortlib.ComfortAPI
import dev.tuhkanens.comfortlib.api.ConfigAPI
import dev.tuhkanens.comfortlib.api.UpdateAPI
import dev.tuhkanens.comfortlib.update.UpdateCheckResult
import java.lang.module.ModuleDescriptor
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class UpdateImpl : UpdateAPI {

    private var enabledCheckUpdates: Boolean = false

    private var projectId: String = "UNKNOWN"
    private var version: String = "UNKNOWN"

    private val client: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(15L)).build()

    override fun checkUpdates(): UpdateCheckResult {
        if (!getEnabled()) return UpdateCheckResult.Unavailable
        val logger = Comfort.getLogger()
        try {
            val projectId: String = getProjectId()
            val currentVersion: String = getVersion()

            val responseBody: String? = client.send(
                HttpRequest.newBuilder()
                    .uri(
                        URI.create("https://api.modrinth.com/v2/project/$projectId/version")
                    )
                    .header("User-Agent", "$projectId-$currentVersion")
                    .timeout(Duration.ofSeconds(30L))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            ).body()

            if (responseBody == null) {
                logger.error("Update check failed: empty response")
                return UpdateCheckResult.Unavailable
            } else {
                val body: JsonElement = JsonParser.parseString(responseBody)
                if (body.isJsonArray && !body.asJsonArray.isEmpty) {
                    val latestVersion: JsonObject = body.asJsonArray.get(0)
                        .asJsonObject
                    val latestVersionString = latestVersion.get("version_number").asString
                    val compare = compareVersions(latestVersionString, currentVersion)
                    if (compare > 0) {
                        logger.warn("Update available: $latestVersionString (current: $currentVersion)")
                        return UpdateCheckResult.Result(true, latestVersionString, false)
                    } else if (compare < 0) {
                        logger.info("You are ahead of release ($currentVersion)")
                        return UpdateCheckResult.Result(false, currentVersion, true)
                    } else {
                        logger.info("You are up to date ($currentVersion)")
                        return UpdateCheckResult.Result(false, latestVersionString, false)
                    }
                } else {
                    logger.error("Update check failed: unexpected response")
                    return UpdateCheckResult.Unavailable
                }
            }
        } catch (e: Exception) {
            logger.error("Update check failed: ${e.printStackTrace()}")
            return UpdateCheckResult.Unavailable
        }
    }

    private fun compareVersions(latest: String, current: String): Int {
        try {
            val latestVersion: ModuleDescriptor.Version = ModuleDescriptor.Version.parse(latest)
            val currentVersion: ModuleDescriptor.Version = ModuleDescriptor.Version.parse(current)
            return latestVersion.compareTo(currentVersion)
        } catch (_: Exception) {
            return latest.compareTo(current)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabledCheckUpdates = enabled
    }

    override fun getEnabled(): Boolean {
        val checkUpdates = ComfortAPI.get<ConfigAPI>().getNode().node("check-updates").getBoolean(true)
        if (checkUpdates) setEnabled(true)
        return enabledCheckUpdates
    }

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