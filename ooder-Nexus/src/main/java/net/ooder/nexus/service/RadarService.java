package net.ooder.nexus.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 雷达服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供公共资源扫描和发现能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>公共技能发现</li>
 *   <li>公共资源扫描</li>
 *   <li>节点发现</li>
 *   <li>资源推荐</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface RadarService {

    /**
     * 启动雷达模式
     *
     * @return 启动结果
     */
    CompletableFuture<Void> startRadarMode();

    /**
     * 停止雷达模式
     *
     * @return 停止结果
     */
    CompletableFuture<Void> stopRadarMode();

    /**
     * 检查是否处于雷达模式
     *
     * @return 是否雷达模式
     */
    boolean isRadarModeActive();

    /**
     * 扫描公共技能
     *
     * @param category 分类（可选）
     * @return 技能列表
     */
    CompletableFuture<List<PublicSkill>> scanPublicSkills(String category);

    /**
     * 扫描公共资源
     *
     * @param resourceType 资源类型（可选）
     * @return 资源列表
     */
    CompletableFuture<List<PublicResource>> scanPublicResources(String resourceType);

    /**
     * 发现附近节点
     *
     * @return 节点列表
     */
    CompletableFuture<List<DiscoveredNode>> discoverNearbyNodes();

    /**
     * 搜索资源
     *
     * @param keyword 关键词
     * @return 搜索结果
     */
    CompletableFuture<List<SearchResult>> searchResources(String keyword);

    /**
     * 获取推荐资源
     *
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    CompletableFuture<List<RecommendedResource>> getRecommendedResources(int limit);

    /**
     * 添加雷达扫描监听器
     *
     * @param listener 监听器
     */
    void addRadarListener(RadarListener listener);

    /**
     * 移除雷达扫描监听器
     *
     * @param listener 监听器
     */
    void removeRadarListener(RadarListener listener);

    /**
     * 雷达扫描监听器
     */
    interface RadarListener {
        void onNodeDiscovered(DiscoveredNode node);
        void onResourceFound(PublicResource resource);
        void onScanProgress(int progress, String stage);
    }

    /**
     * 公共技能
     */
    class PublicSkill {
        private String skillId;
        private String skillName;
        private String description;
        private String category;
        private String version;
        private String author;
        private double rating;
        private int downloadCount;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        public int getDownloadCount() { return downloadCount; }
        public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }
    }

    /**
     * 公共资源
     */
    class PublicResource {
        private String resourceId;
        private String resourceName;
        private String resourceType;
        private String description;
        private String provider;
        private String accessUrl;
        private long size;

        public String getResourceId() { return resourceId; }
        public void setResourceId(String resourceId) { this.resourceId = resourceId; }
        public String getResourceName() { return resourceName; }
        public void setResourceName(String resourceName) { this.resourceName = resourceName; }
        public String getResourceType() { return resourceType; }
        public void setResourceType(String resourceType) { this.resourceType = resourceType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getAccessUrl() { return accessUrl; }
        public void setAccessUrl(String accessUrl) { this.accessUrl = accessUrl; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
    }

    /**
     * 发现的节点
     */
    class DiscoveredNode {
        private String nodeId;
        private String nodeName;
        private String nodeType;
        private String address;
        private int port;
        private String status;
        private long lastSeen;

        public String getNodeId() { return nodeId; }
        public void setNodeId(String nodeId) { this.nodeId = nodeId; }
        public String getNodeName() { return nodeName; }
        public void setNodeName(String nodeName) { this.nodeName = nodeName; }
        public String getNodeType() { return nodeType; }
        public void setNodeType(String nodeType) { this.nodeType = nodeType; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    }

    /**
     * 搜索结果
     */
    class SearchResult {
        private String resultId;
        private String resultType;
        private String title;
        private String description;
        private double relevance;

        public String getResultId() { return resultId; }
        public void setResultId(String resultId) { this.resultId = resultId; }
        public String getResultType() { return resultType; }
        public void setResultType(String resultType) { this.resultType = resultType; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getRelevance() { return relevance; }
        public void setRelevance(double relevance) { this.relevance = relevance; }
    }

    /**
     * 推荐资源
     */
    class RecommendedResource {
        private String resourceId;
        private String resourceName;
        private String resourceType;
        private String reason;
        private double score;

        public String getResourceId() { return resourceId; }
        public void setResourceId(String resourceId) { this.resourceId = resourceId; }
        public String getResourceName() { return resourceName; }
        public void setResourceName(String resourceName) { this.resourceName = resourceName; }
        public String getResourceType() { return resourceType; }
        public void setResourceType(String resourceType) { this.resourceType = resourceType; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
    }
}
