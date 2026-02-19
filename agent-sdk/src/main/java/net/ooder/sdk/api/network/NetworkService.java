package net.ooder.sdk.api.network;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Network Service Interface
 *
 * <p>Provides network link management and quality monitoring.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Link creation and management</li>
 *   <li>Quality monitoring</li>
 *   <li>Optimal path calculation</li>
 *   <li>Link event notifications</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * NetworkService network = new NetworkServiceImpl();
 *
 * // Create link
 * Link link = network.createLink("node1", "node2", LinkType.DIRECT);
 *
 * // Get link quality
 * LinkQualityInfo quality = network.getLinkQuality(link.getLinkId());
 *
 * // Find optimal path
 * List&lt;Link&gt; path = network.findOptimalPath("node1", "node5");
 *
 * // Enable quality monitoring
 * network.enableQualityMonitor(30000);
 *
 * // Add listener
 * network.addLinkListener(new LinkListener() {
 *     void onQualityChanged(String linkId, LinkQualityInfo quality) {
 *         log.info("Quality changed: {} -> {}", linkId, quality);
 *     }
 * });
 * </pre>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface NetworkService {

    // ==================== Link Management ====================

    /**
     * Create a link between two nodes
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @param type     link type
     * @return created link
     */
    LinkInfo createLink(String sourceId, String targetId, LinkInfo.LinkType type);

    /**
     * Create link asynchronously
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @param type     link type
     * @return CompletableFuture with link
     */
    CompletableFuture<LinkInfo> createLinkAsync(String sourceId, String targetId, LinkInfo.LinkType type);

    /**
     * Get link by ID
     *
     * @param linkId link ID
     * @return Optional link
     */
    Optional<LinkInfo> getLink(String linkId);

    /**
     * Get link by source and target
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @return Optional link
     */
    Optional<LinkInfo> getLink(String sourceId, String targetId);

    /**
     * Remove a link
     *
     * @param linkId link ID
     */
    void removeLink(String linkId);

    /**
     * Get all links
     *
     * @return list of all links
     */
    List<LinkInfo> getAllLinks();

    /**
     * Get links from a source node
     *
     * @param sourceId source node ID
     * @return list of links
     */
    List<LinkInfo> getLinksFrom(String sourceId);

    /**
     * Get links to a target node
     *
     * @param targetId target node ID
     * @return list of links
     */
    List<LinkInfo> getLinksTo(String targetId);

    /**
     * Get link count
     *
     * @return total link count
     */
    int getLinkCount();

    /**
     * Check if link exists
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @return true if exists
     */
    boolean hasLink(String sourceId, String targetId);

    // ==================== Quality Monitoring ====================

    /**
     * Get link quality
     *
     * @param linkId link ID
     * @return link quality info
     */
    LinkQualityInfo getLinkQuality(String linkId);

    /**
     * Enable quality monitoring
     *
     * @param intervalMs monitoring interval in milliseconds
     */
    void enableQualityMonitor(long intervalMs);

    /**
     * Disable quality monitoring
     */
    void disableQualityMonitor();

    /**
     * Check if quality monitoring is enabled
     *
     * @return true if enabled
     */
    boolean isQualityMonitorEnabled();

    /**
     * Update link quality manually
     *
     * @param linkId link ID
     * @param latency latency in milliseconds
     * @param packetLoss packet loss ratio (0.0 - 1.0)
     */
    void updateLinkQuality(String linkId, int latency, double packetLoss);

    // ==================== Path Finding ====================

    /**
     * Find optimal path between two nodes
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @return list of links forming the optimal path
     */
    List<LinkInfo> findOptimalPath(String sourceId, String targetId);

    /**
     * Find all paths between two nodes
     *
     * @param sourceId source node ID
     * @param targetId target node ID
     * @param maxPaths maximum number of paths to return
     * @return list of paths (each path is a list of links)
     */
    List<List<LinkInfo>> findAllPaths(String sourceId, String targetId, int maxPaths);

    // ==================== Event Listeners ====================

    /**
     * Add link listener
     *
     * @param listener the listener
     */
    void addLinkListener(LinkListener listener);

    /**
     * Remove link listener
     *
     * @param listener the listener
     */
    void removeLinkListener(LinkListener listener);

    // ==================== Statistics ====================

    /**
     * Get network statistics
     *
     * @return network statistics
     */
    NetworkStats getNetworkStats();

    // ==================== Lifecycle ====================

    /**
     * Shutdown the service
     */
    void shutdown();

    /**
     * Network Statistics
     */
    class NetworkStats {
        private int totalLinks;
        private int activeLinks;
        private int degradedLinks;
        private int failedLinks;
        private double averageLatency;
        private double averagePacketLoss;
        private long totalBytesTransmitted;
        private long totalBytesReceived;

        public int getTotalLinks() { return totalLinks; }
        public void setTotalLinks(int totalLinks) { this.totalLinks = totalLinks; }

        public int getActiveLinks() { return activeLinks; }
        public void setActiveLinks(int activeLinks) { this.activeLinks = activeLinks; }

        public int getDegradedLinks() { return degradedLinks; }
        public void setDegradedLinks(int degradedLinks) { this.degradedLinks = degradedLinks; }

        public int getFailedLinks() { return failedLinks; }
        public void setFailedLinks(int failedLinks) { this.failedLinks = failedLinks; }

        public double getAverageLatency() { return averageLatency; }
        public void setAverageLatency(double averageLatency) { this.averageLatency = averageLatency; }

        public double getAveragePacketLoss() { return averagePacketLoss; }
        public void setAveragePacketLoss(double averagePacketLoss) { this.averagePacketLoss = averagePacketLoss; }

        public long getTotalBytesTransmitted() { return totalBytesTransmitted; }
        public void setTotalBytesTransmitted(long totalBytesTransmitted) { this.totalBytesTransmitted = totalBytesTransmitted; }

        public long getTotalBytesReceived() { return totalBytesReceived; }
        public void setTotalBytesReceived(long totalBytesReceived) { this.totalBytesReceived = totalBytesReceived; }
    }
}
