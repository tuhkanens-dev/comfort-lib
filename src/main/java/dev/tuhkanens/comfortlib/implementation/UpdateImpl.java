package dev.tuhkanens.comfortlib.implementation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tuhkanens.comfortlib.Comfort;
import dev.tuhkanens.comfortlib.api.UpdateAPI;
import dev.tuhkanens.comfortlib.result.UpdateResult;
import org.slf4j.Logger;

import java.lang.module.ModuleDescriptor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UpdateImpl implements UpdateAPI {

    private boolean enabledCheckUpdates = true;

    private String projectId = "UNKNOWN";
    private String version = "UNKNOWN";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15L)).build();

    @Override
    public void setEnabled(boolean enabled) {
        this.enabledCheckUpdates = enabled;
    }

    @Override
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean getEnabled() {
        return enabledCheckUpdates;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public UpdateResult checkUpdates() {
        if (!getEnabled()) return UpdateResult.Unavailable.INSTANCE;
        Logger logger = Comfort.getInstance().getLogger();
        try {
            String projectId = getProjectId();
            String currentVersion = getVersion();

            String responseBody = client.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create("https://api.modrinth.com/v2/project/$projectId/version"))
                            .header("User-Agent", "$projectId-$currentVersion")
                            .timeout(Duration.ofSeconds(30L))
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body();

            if (responseBody == null) {
                logger.error("Update check failed: empty response");
                return UpdateResult.Unavailable.INSTANCE;
            } else {
                JsonElement body = JsonParser.parseString(responseBody);
                if (body.isJsonArray() && !body.getAsJsonArray().isEmpty()) {
                    JsonObject latestVersion = body.getAsJsonArray().get(0).getAsJsonObject();
                    String latestVersionString = latestVersion.get("version_number").getAsString();
                    int compare = compareVersions(latestVersionString, currentVersion);
                    if (compare > 0) {
                        logger.warn("Update available: {} (current: {})", latestVersionString, currentVersion);
                        return new UpdateResult.Result(true, latestVersionString, false);
                    } else if (compare < 0) {
                        logger.info("You are ahead of release ({})", currentVersion);
                        return new UpdateResult.Result(false, latestVersionString, true);
                    } else {
                        logger.info("You are up to date ({})", currentVersion);
                        return new UpdateResult.Result(false, latestVersionString, false);
                    }
                } else {
                    logger.info("Update check failed: unexpected response");
                    return UpdateResult.Unavailable.INSTANCE;
                }
            }
        } catch (Exception e) {
            logger.error("Update check failed: {}", e.getMessage());
            return UpdateResult.Unavailable.INSTANCE;
        }
    }

    private int compareVersions(String latest, String current) {
        try {
            ModuleDescriptor.Version latestVersion = ModuleDescriptor.Version.parse(latest);
            ModuleDescriptor.Version currentVersion = ModuleDescriptor.Version.parse(current);
            return latestVersion.compareTo(currentVersion);
        } catch (Exception e) {
            return latest.compareTo(current);
        }
    }
}
