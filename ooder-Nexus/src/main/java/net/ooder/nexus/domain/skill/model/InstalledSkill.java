package net.ooder.nexus.domain.skill.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 已安装技能实体类
 * 表示从 skillcenter 下载并安装的技能包
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class InstalledSkill implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String skillId;
    private String skillName;
    private String description;
    private String version;
    private String category;
    private String author;
    private String icon;
    private List<String> tags;
    private String jarPath;
    private String configPath;
    private String mainClass;
    private SkillStatus status;
    private SkillSource source;
    private Date installTime;
    private Date lastRunTime;
    private int runCount;
    private Map<String, Object> config;
    private Map<String, Object> metadata;
    private String routeAgentId;
    private String endAgentId;
    private List<String> requiredCapabilities;

    public enum SkillStatus {
        INSTALLED,
        RUNNING,
        STOPPED,
        ERROR,
        UPDATING
    }

    public enum SkillSource {
        SKILLCENTER,
        LOCAL,
        EXTERNAL
    }

    public InstalledSkill() {
        this.status = SkillStatus.INSTALLED;
        this.source = SkillSource.SKILLCENTER;
        this.runCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public SkillStatus getStatus() {
        return status;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }

    public SkillSource getSource() {
        return source;
    }

    public void setSource(SkillSource source) {
        this.source = source;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getRouteAgentId() {
        return routeAgentId;
    }

    public void setRouteAgentId(String routeAgentId) {
        this.routeAgentId = routeAgentId;
    }

    public String getEndAgentId() {
        return endAgentId;
    }

    public void setEndAgentId(String endAgentId) {
        this.endAgentId = endAgentId;
    }

    public List<String> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public void setRequiredCapabilities(List<String> requiredCapabilities) {
        this.requiredCapabilities = requiredCapabilities;
    }

    public void incrementRunCount() {
        this.runCount++;
        this.lastRunTime = new Date();
    }

    public boolean isRunning() {
        return SkillStatus.RUNNING.equals(status);
    }

    public boolean canRun() {
        return SkillStatus.INSTALLED.equals(status) || SkillStatus.STOPPED.equals(status);
    }
}
