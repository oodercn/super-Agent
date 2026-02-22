package net.ooder.sdk.skill.packageManager.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class InstalledSkill {

    private String skillId;
    private String name;
    private String version;
    private String type;
    private InstallMode installMode;
    private SkillStatus status;
    private String installPath;
    private Instant installTime;
    private Instant lastStartTime;
    private List<Capability> capabilities;
    private List<SceneInfo> scenes;
    private Map<String, String> config;
    private SkillConnectionInfo connectionInfo;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InstallMode getInstallMode() {
        return installMode;
    }

    public void setInstallMode(InstallMode installMode) {
        this.installMode = installMode;
    }

    public SkillStatus getStatus() {
        return status;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    public Instant getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Instant installTime) {
        this.installTime = installTime;
    }

    public Instant getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(Instant lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    public List<SceneInfo> getScenes() {
        return scenes;
    }

    public void setScenes(List<SceneInfo> scenes) {
        this.scenes = scenes;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public SkillConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(SkillConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }
}
