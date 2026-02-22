package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 已安装技能DTO
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class InstalledSkillDTO implements Serializable {
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
    private String status;
    private String source;
    private Date installTime;
    private Date lastRunTime;
    private int runCount;
    private Map<String, Object> config;
    private String routeAgentId;
    private String endAgentId;
    private List<String> requiredCapabilities;
    private boolean running;
    private boolean canRun;

    public InstalledSkillDTO() {}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }
}
