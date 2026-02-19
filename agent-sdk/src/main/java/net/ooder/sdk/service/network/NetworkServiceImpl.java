package net.ooder.sdk.service.network;

import net.ooder.sdk.api.network.LinkInfo;
import net.ooder.sdk.api.network.LinkListener;
import net.ooder.sdk.api.network.LinkQualityInfo;
import net.ooder.sdk.api.network.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Network Service Implementation
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class NetworkServiceImpl implements NetworkService {

    private static final Logger log = LoggerFactory.getLogger(NetworkServiceImpl.class);

    private final Map<String, LinkInfo> links;
    private final Map<String, List<LinkInfo>> outgoingLinks;
    private final Map<String, List<LinkInfo>> incomingLinks;
    private final List<LinkListener> listeners;
    private final ExecutorService executor;
    private final ScheduledExecutorService monitorExecutor;

    private ScheduledFuture<?> monitorTask;
    private long monitorInterval;
    private final AtomicBoolean monitorEnabled;

    public NetworkServiceImpl() {
        this.links = new ConcurrentHashMap<String, LinkInfo>();
        this.outgoingLinks = new ConcurrentHashMap<String, List<LinkInfo>>();
        this.incomingLinks = new ConcurrentHashMap<String, List<LinkInfo>>();
        this.listeners = new CopyOnWriteArrayList<LinkListener>();
        this.executor = Executors.newCachedThreadPool();
        this.monitorExecutor = Executors.newSingleThreadScheduledExecutor();
        this.monitorEnabled = new AtomicBoolean(false);
        log.info("NetworkServiceImpl initialized");
    }

    @Override
    public LinkInfo createLink(String sourceId, String targetId, LinkInfo.LinkType type) {
        String linkId = generateLinkId(sourceId, targetId);
        log.debug("Creating link: {} -> {} ({})", sourceId, targetId, type);

        LinkInfo link = new LinkInfo();
        link.setLinkId(linkId);
        link.setSourceId(sourceId);
        link.setTargetId(targetId);
        link.setType(type);
        link.setCreateTime(System.currentTimeMillis());
        link.setLastActiveTime(System.currentTimeMillis());
        link.setStatus(LinkInfo.LinkStatus.ACTIVE);

        LinkQualityInfo quality = new LinkQualityInfo();
        quality.setLinkId(linkId);
        quality.setLatency(10);
        quality.setPacketLoss(0.0);
        quality.setBandwidth(1000.0);
        quality.calculateScore();
        link.setQuality(quality);

        links.put(linkId, link);

        outgoingLinks.computeIfAbsent(sourceId, k -> new CopyOnWriteArrayList<LinkInfo>()).add(link);
        incomingLinks.computeIfAbsent(targetId, k -> new CopyOnWriteArrayList<LinkInfo>()).add(link);

        notifyLinkCreated(link);
        log.info("Link created: {}", linkId);
        return link;
    }

    @Override
    public CompletableFuture<LinkInfo> createLinkAsync(String sourceId, String targetId, LinkInfo.LinkType type) {
        return CompletableFuture.supplyAsync(() -> createLink(sourceId, targetId, type), executor);
    }

    @Override
    public Optional<LinkInfo> getLink(String linkId) {
        return Optional.ofNullable(links.get(linkId));
    }

    @Override
    public Optional<LinkInfo> getLink(String sourceId, String targetId) {
        String linkId = generateLinkId(sourceId, targetId);
        return getLink(linkId);
    }

    @Override
    public void removeLink(String linkId) {
        log.debug("Removing link: {}", linkId);
        LinkInfo link = links.remove(linkId);
        if (link != null) {
            List<LinkInfo> outgoing = outgoingLinks.get(link.getSourceId());
            if (outgoing != null) {
                outgoing.removeIf(l -> l.getLinkId().equals(linkId));
            }

            List<LinkInfo> incoming = incomingLinks.get(link.getTargetId());
            if (incoming != null) {
                incoming.removeIf(l -> l.getLinkId().equals(linkId));
            }

            notifyLinkRemoved(linkId);
            log.info("Link removed: {}", linkId);
        }
    }

    @Override
    public List<LinkInfo> getAllLinks() {
        return new ArrayList<LinkInfo>(links.values());
    }

    @Override
    public List<LinkInfo> getLinksFrom(String sourceId) {
        List<LinkInfo> result = outgoingLinks.get(sourceId);
        return result != null ? new ArrayList<LinkInfo>(result) : new ArrayList<LinkInfo>();
    }

    @Override
    public List<LinkInfo> getLinksTo(String targetId) {
        List<LinkInfo> result = incomingLinks.get(targetId);
        return result != null ? new ArrayList<LinkInfo>(result) : new ArrayList<LinkInfo>();
    }

    @Override
    public int getLinkCount() {
        return links.size();
    }

    @Override
    public boolean hasLink(String sourceId, String targetId) {
        return links.containsKey(generateLinkId(sourceId, targetId));
    }

    @Override
    public LinkQualityInfo getLinkQuality(String linkId) {
        LinkInfo link = links.get(linkId);
        return link != null ? link.getQuality() : null;
    }

    @Override
    public void enableQualityMonitor(long intervalMs) {
        if (monitorEnabled.compareAndSet(false, true)) {
            this.monitorInterval = intervalMs;
            monitorTask = monitorExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    monitorLinks();
                }
            }, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
            log.info("Quality monitoring enabled with interval: {}ms", intervalMs);
        }
    }

    @Override
    public void disableQualityMonitor() {
        if (monitorEnabled.compareAndSet(true, false)) {
            if (monitorTask != null) {
                monitorTask.cancel(false);
            }
            log.info("Quality monitoring disabled");
        }
    }

    @Override
    public boolean isQualityMonitorEnabled() {
        return monitorEnabled.get();
    }

    @Override
    public void updateLinkQuality(String linkId, int latency, double packetLoss) {
        LinkInfo link = links.get(linkId);
        if (link != null) {
            LinkQualityInfo quality = link.getQuality();
            quality.setLatency(latency);
            quality.setPacketLoss(packetLoss);
            quality.setLastCheckTime(System.currentTimeMillis());
            quality.calculateScore();

            LinkInfo.LinkStatus oldStatus = link.getStatus();
            if (quality.getQualityLevel().getLevel() <= LinkQualityInfo.QualityLevel.POOR.getLevel()) {
                link.setStatus(LinkInfo.LinkStatus.DEGRADED);
            } else {
                link.setStatus(LinkInfo.LinkStatus.ACTIVE);
            }

            if (oldStatus != link.getStatus()) {
                notifyStatusChanged(linkId, link.getStatus().name());
            }

            notifyQualityChanged(linkId, quality);
            log.debug("Link quality updated: {} -> {}", linkId, quality);
        }
    }

    private void monitorLinks() {
        log.debug("Monitoring {} links", links.size());
        for (LinkInfo link : links.values()) {
            try {
                int latency = measureLatency(link);
                double packetLoss = measurePacketLoss(link);
                updateLinkQuality(link.getLinkId(), latency, packetLoss);
            } catch (Exception e) {
                log.warn("Failed to monitor link: {}", link.getLinkId(), e);
            }
        }
    }
    
    private int measureLatency(LinkInfo link) {
        String targetId = link.getTargetId();
        long startTime = System.currentTimeMillis();
        
        try {
            String host = extractHostFromTargetId(targetId);
            if (host != null) {
                java.net.InetAddress address = java.net.InetAddress.getByName(host);
                if (address.isReachable(5000)) {
                    long endTime = System.currentTimeMillis();
                    return (int) (endTime - startTime);
                }
            }
        } catch (Exception e) {
            log.trace("Ping failed for {}: {}", targetId, e.getMessage());
        }
        
        return link.getQuality() != null ? link.getQuality().getLatency() : 100;
    }
    
    private double measurePacketLoss(LinkInfo link) {
        int successCount = 0;
        int totalAttempts = 3;
        
        String targetId = link.getTargetId();
        String host = extractHostFromTargetId(targetId);
        
        if (host == null) {
            return link.getQuality() != null ? link.getQuality().getPacketLoss() : 0.0;
        }
        
        for (int i = 0; i < totalAttempts; i++) {
            try {
                java.net.InetAddress address = java.net.InetAddress.getByName(host);
                if (address.isReachable(3000)) {
                    successCount++;
                }
            } catch (Exception e) {
                // Count as failed attempt
            }
        }
        
        return 1.0 - ((double) successCount / totalAttempts);
    }
    
    private String extractHostFromTargetId(String targetId) {
        if (targetId == null) return null;
        
        if (targetId.contains("@")) {
            String[] parts = targetId.split("@");
            if (parts.length > 1) {
                return parts[1].split(":")[0];
            }
        }
        
        if (targetId.matches(".*\\d+\\.\\d+\\.\\d+\\.\\d+.*")) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(targetId);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        
        return null;
    }

    @Override
    public List<LinkInfo> findOptimalPath(String sourceId, String targetId) {
        log.debug("Finding optimal path: {} -> {}", sourceId, targetId);

        if (hasLink(sourceId, targetId)) {
            Optional<LinkInfo> directLink = getLink(sourceId, targetId);
            if (directLink.isPresent()) {
                List<LinkInfo> path = new ArrayList<LinkInfo>();
                path.add(directLink.get());
                return path;
            }
        }

        Map<String, LinkInfo> previous = new HashMap<String, LinkInfo>();
        Map<String, Double> distances = new HashMap<String, Double>();
        Set<String> visited = new HashSet<String>();
        PriorityQueue<String> queue = new PriorityQueue<String>(11, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Double.compare(distances.getOrDefault(a, Double.MAX_VALUE), 
                                       distances.getOrDefault(b, Double.MAX_VALUE));
            }
        });

        distances.put(sourceId, 0.0);
        queue.add(sourceId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(targetId)) {
                return reconstructPath(previous, targetId);
            }

            List<LinkInfo> neighbors = getLinksFrom(current);
            for (LinkInfo link : neighbors) {
                String neighbor = link.getTargetId();
                if (visited.contains(neighbor)) continue;

                double linkCost = calculateLinkCost(link);
                double newDist = distances.get(current) + linkCost;

                if (newDist < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, link);
                    queue.add(neighbor);
                }
            }
        }

        log.debug("No path found: {} -> {}", sourceId, targetId);
        return new ArrayList<LinkInfo>();
    }

    private double calculateLinkCost(LinkInfo link) {
        LinkQualityInfo quality = link.getQuality();
        double latencyFactor = quality.getLatency() / 100.0;
        double lossFactor = quality.getPacketLoss() * 10;
        double scoreFactor = (100 - quality.getScore()) / 100;
        return 1 + latencyFactor + lossFactor + scoreFactor;
    }

    private List<LinkInfo> reconstructPath(Map<String, LinkInfo> previous, String targetId) {
        List<LinkInfo> path = new ArrayList<LinkInfo>();
        String current = targetId;

        while (previous.containsKey(current)) {
            LinkInfo link = previous.get(current);
            path.add(0, link);
            current = link.getSourceId();
        }

        log.debug("Path reconstructed with {} links", path.size());
        return path;
    }

    @Override
    public List<List<LinkInfo>> findAllPaths(String sourceId, String targetId, int maxPaths) {
        List<List<LinkInfo>> allPaths = new ArrayList<List<LinkInfo>>();
        findPathsDFS(sourceId, targetId, new HashSet<String>(), new ArrayList<LinkInfo>(), allPaths, maxPaths);
        return allPaths;
    }

    private void findPathsDFS(String current, String targetId, Set<String> visited, 
                              List<LinkInfo> path, List<List<LinkInfo>> allPaths, int maxPaths) {
        if (allPaths.size() >= maxPaths) return;
        if (current.equals(targetId)) {
            allPaths.add(new ArrayList<LinkInfo>(path));
            return;
        }

        visited.add(current);
        List<LinkInfo> neighbors = getLinksFrom(current);
        for (LinkInfo link : neighbors) {
            String neighbor = link.getTargetId();
            if (!visited.contains(neighbor)) {
                path.add(link);
                findPathsDFS(neighbor, targetId, visited, path, allPaths, maxPaths);
                path.remove(path.size() - 1);
            }
        }
        visited.remove(current);
    }

    @Override
    public void addLinkListener(LinkListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLinkListener(LinkListener listener) {
        listeners.remove(listener);
    }

    private void notifyLinkCreated(LinkInfo link) {
        for (LinkListener listener : listeners) {
            try {
                listener.onLinkCreated(link);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }

    private void notifyLinkRemoved(String linkId) {
        for (LinkListener listener : listeners) {
            try {
                listener.onLinkRemoved(linkId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }

    private void notifyQualityChanged(String linkId, LinkQualityInfo quality) {
        for (LinkListener listener : listeners) {
            try {
                listener.onQualityChanged(linkId, quality);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }

    private void notifyStatusChanged(String linkId, String status) {
        for (LinkListener listener : listeners) {
            try {
                listener.onStatusChanged(linkId, status);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }

    @Override
    public NetworkStats getNetworkStats() {
        NetworkStats stats = new NetworkStats();
        stats.setTotalLinks(links.size());

        int active = 0, degraded = 0, failed = 0;
        double totalLatency = 0, totalLoss = 0;

        for (LinkInfo link : links.values()) {
            switch (link.getStatus()) {
                case ACTIVE: active++; break;
                case DEGRADED: degraded++; break;
                case FAILED: failed++; break;
                default: break;
            }
            totalLatency += link.getQuality().getLatency();
            totalLoss += link.getQuality().getPacketLoss();
        }

        stats.setActiveLinks(active);
        stats.setDegradedLinks(degraded);
        stats.setFailedLinks(failed);
        stats.setAverageLatency(links.isEmpty() ? 0 : totalLatency / links.size());
        stats.setAveragePacketLoss(links.isEmpty() ? 0 : totalLoss / links.size());

        return stats;
    }

    @Override
    public void shutdown() {
        log.info("Shutting down NetworkService");
        disableQualityMonitor();
        executor.shutdown();
        monitorExecutor.shutdown();
        links.clear();
        outgoingLinks.clear();
        incomingLinks.clear();
        listeners.clear();
        log.info("NetworkService shutdown complete");
    }

    private String generateLinkId(String sourceId, String targetId) {
        return sourceId + "->" + targetId;
    }
}
