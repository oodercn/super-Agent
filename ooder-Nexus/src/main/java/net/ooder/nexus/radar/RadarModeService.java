package net.ooder.nexus.radar;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Radar Mode Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface RadarModeService {

    CompletableFuture<List<DiscoveredResource>> discoverResources(String type);

    CompletableFuture<List<DiscoveredSkill>> searchSkills(String query);

    CompletableFuture<List<DiscoveredNode>> discoverNodes();

    CompletableFuture<DiscoveryReport> fullScan();

    CompletableFuture<Void> enableRadarMode();

    CompletableFuture<Void> disableRadarMode();

    CompletableFuture<RadarStatus> getRadarStatus();

    CompletableFuture<List<DiscoveredResource>> getResourceCache();

    CompletableFuture<Void> clearCache();
}

class DiscoveredResource {
    private String resourceId;
    private String name;
    private String type;
    private String endpoint;
    private String source;
    private long discoveredTime;

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public long getDiscoveredTime() { return discoveredTime; }
    public void setDiscoveredTime(long discoveredTime) { this.discoveredTime = discoveredTime; }
}

class DiscoveredSkill {
    private String skillId;
    private String name;
    private String description;
    private String version;
    private String source;
    private double rating;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}

class DiscoveredNode {
    private String nodeId;
    private String name;
    private String endpoint;
    private String status;
    private long lastSeen;

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
}

class DiscoveryReport {
    private long scanTime;
    private int resourcesFound;
    private int skillsFound;
    private int nodesFound;
    private List<String> sources;

    public long getScanTime() { return scanTime; }
    public void setScanTime(long scanTime) { this.scanTime = scanTime; }
    public int getResourcesFound() { return resourcesFound; }
    public void setResourcesFound(int resourcesFound) { this.resourcesFound = resourcesFound; }
    public int getSkillsFound() { return skillsFound; }
    public void setSkillsFound(int skillsFound) { this.skillsFound = skillsFound; }
    public int getNodesFound() { return nodesFound; }
    public void setNodesFound(int nodesFound) { this.nodesFound = nodesFound; }
    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }
}

class RadarStatus {
    private boolean enabled;
    private long lastScanTime;
    private int cacheSize;
    private String mode;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public long getLastScanTime() { return lastScanTime; }
    public void setLastScanTime(long lastScanTime) { this.lastScanTime = lastScanTime; }
    public int getCacheSize() { return cacheSize; }
    public void setCacheSize(int cacheSize) { this.cacheSize = cacheSize; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
}
