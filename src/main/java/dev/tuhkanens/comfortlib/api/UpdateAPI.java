package dev.tuhkanens.comfortlib.api;

import dev.tuhkanens.comfortlib.result.UpdateResult;

public interface UpdateAPI {
    void setEnabled(boolean enabled);
    void setProjectId(String projectId);
    void setVersion(String version);

    boolean getEnabled();
    String getProjectId();
    String getVersion();

    UpdateResult checkUpdates();
}
