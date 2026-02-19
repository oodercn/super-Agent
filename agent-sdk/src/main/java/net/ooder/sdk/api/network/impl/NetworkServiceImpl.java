package net.ooder.sdk.api.network.impl;

import net.ooder.sdk.api.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class NetworkServiceImpl implements NetworkService {
    
    private static final Logger log = LoggerFactory.getLogger(NetworkServiceImpl.class);
    
    private final Map<String, LinkInfo> links;
    private final Map<String, LinkQualityInfo> linkQualities;
    private final List<LinkListener> listeners;
    private final ExecutorService executor;
    private ScheduledExecutorService qualityMonitor;
    private long monitorInterval;
    
    public NetworkServiceImpl() {
        this.links = new ConcurrentHashMap<String, LinkInfo>();
        this.linkQualities = new ConcurrentHashMap<String, LinkQualityInfo>();
        this.listeners = new CopyOnWriteArrayList<LinkListener>();
        this.executor = Executors.newCachedThreadPool();
        log.info("NetworkServiceImpl initialized");
    }
    
    @Override
    public LinkInfo createLink(String sourceId, String targetId, LinkInfo.LinkType type) {
        String linkId = "link-" + UUID.randomUUID().toString().substring(0, 8);
        
        LinkInfo link = new LinkInfo();
        link.setLinkId(linkId);
        link.setSourceId(sourceId);
        link.setTargetId(targetId);
        link.setType(type);
        link.setStatus(LinkInfo.LinkStatus.ACTIVE);
        link.setCreateTime(System.currentTimeMillis());
        
        links.put(linkId, link);
        
        LinkQualityInfo quality = new LinkQualityInfo();
        quality.setLinkId(linkId);
        quality.setLatency(10 + (int)(Math.random() * 50));
        quality.setPacketLoss(Math.random() * 0.01);
        quality.setBandwidth(1000 + (long)(Math.random() * 9000));
        quality.setScore(90 + (int)(Math.random() * 10));
        linkQualities.put(linkId, quality);
        
        notifyLinkCreated(link);
        log.info("Created link: {} -> {} ({})", sourceId, targetId, type);
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
        for (LinkInfo link : links.values()) {
            if (link.getSourceId().equals(sourceId) && link.getTargetId().equals(targetId)) {
                return Optional.of(link);
            }
        }
        return Optional.empty();
    }
    
    @Override
    public void removeLink(String linkId) {
        LinkInfo removed = links.remove(linkId);
        if (removed != null) {
            linkQualities.remove(linkId);
            notifyLinkRemoved(removed.getLinkId());
            log.info("Removed link: {}", linkId);
        }
    }
    
    @Override
    public List<LinkInfo> getAllLinks() {
        return new ArrayList<LinkInfo>(links.values());
    }
    
    @Override
    public List<LinkInfo> getLinksFrom(String sourceId) {
        List<LinkInfo> result = new ArrayList<LinkInfo>();
        for (LinkInfo link : links.values()) {
            if (link.getSourceId().equals(sourceId)) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public List<LinkInfo> getLinksTo(String targetId) {
        List<LinkInfo> result = new ArrayList<LinkInfo>();
        for (LinkInfo link : links.values()) {
            if (link.getTargetId().equals(targetId)) {
                result.add(link);
            }
        }
        return result;
    }
    
    @Override
    public int getLinkCount() {
        return links.size();
    }
    
    @Override
    public boolean hasLink(String sourceId, String targetId) {
        return getLink(sourceId, targetId).isPresent();
    }
    
    @Override
    public LinkQualityInfo getLinkQuality(String linkId) {
        return linkQualities.get(linkId);
    }
    
    @Override
    public void enableQualityMonitor(long intervalMs) {
        if (qualityMonitor != null) {
            disableQualityMonitor();
        }
        
        this.monitorInterval = intervalMs;
        qualityMonitor = Executors.newSingleThreadScheduledExecutor();
        qualityMonitor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateAllQualities();
            }
        }, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
        
        log.info("Quality monitoring enabled with interval: {}ms", intervalMs);
    }
    
    @Override
    public void disableQualityMonitor() {
        if (qualityMonitor != null) {
            qualityMonitor.shutdown();
            qualityMonitor = null;
            log.info("Quality monitoring disabled");
        }
    }
    
    @Override
    public boolean isQualityMonitorEnabled() {
        return qualityMonitor != null && !qualityMonitor.isShutdown();
    }
    
    @Override
    public void updateLinkQuality(String linkId, int latency, double packetLoss) {
        LinkQualityInfo quality = linkQualities.get(linkId);
        if (quality != null) {
            quality.setLatency(latency);
            quality.setPacketLoss(packetLoss);
            quality.setScore(calculateQualityScore(latency, packetLoss));
            notifyQualityChanged(linkId, quality);
        }
    }
    
    @Override
    public List<LinkInfo> findOptimalPath(String sourceId, String targetId) {
        List<LinkInfo> direct = getLinksFrom(sourceId);
        for (LinkInfo link : direct) {
            if (link.getTargetId().equals(targetId)) {
                return Arrays.asList(link);
            }
        }
        
        Map<String, LinkInfo> predecessors = new HashMap<String, LinkInfo>();
        Set<String> visited = new HashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        
        queue.add(sourceId);
        visited.add(sourceId);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (LinkInfo link : getLinksFrom(current)) {
                String next = link.getTargetId();
                if (!visited.contains(next)) {
                    visited.add(next);
                    predecessors.put(next, link);
                    
                    if (next.equals(targetId)) {
                        return reconstructPath(predecessors, targetId);
                    }
                    
                    queue.add(next);
                }
            }
        }
        
        return new ArrayList<LinkInfo>();
    }
    
    @Override
    public List<List<LinkInfo>> findAllPaths(String sourceId, String targetId, int maxPaths) {
        List<List<LinkInfo>> allPaths = new ArrayList<List<LinkInfo>>();
        findPathsDFS(sourceId, targetId, new ArrayList<LinkInfo>(), new HashSet<String>(), allPaths, maxPaths);
        return allPaths;
    }
    
    private void findPathsDFS(String current, String target, List<LinkInfo> path, Set<String> visited,
                              List<List<LinkInfo>> allPaths, int maxPaths) {
        if (allPaths.size() >= maxPaths) return;
        
        if (current.equals(target)) {
            allPaths.add(new ArrayList<LinkInfo>(path));
            return;
        }
        
        visited.add(current);
        
        for (LinkInfo link : getLinksFrom(current)) {
            if (!visited.contains(link.getTargetId())) {
                path.add(link);
                findPathsDFS(link.getTargetId(), target, path, visited, allPaths, maxPaths);
                path.remove(path.size() - 1);
            }
        }
        
        visited.remove(current);
    }
    
    private List<LinkInfo> reconstructPath(Map<String, LinkInfo> predecessors, String target) {
        List<LinkInfo> path = new ArrayList<LinkInfo>();
        String current = target;
        
        while (predecessors.containsKey(current)) {
            LinkInfo link = predecessors.get(current);
            path.add(0, link);
            current = link.getSourceId();
        }
        
        return path;
    }
    
    @Override
    public void addLinkListener(LinkListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeLinkListener(LinkListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public NetworkStats getNetworkStats() {
        NetworkStats stats = new NetworkStats();
        stats.setTotalLinks(links.size());
        
        int active = 0, degraded = 0, failed = 0;
        double totalLatency = 0;
        double totalPacketLoss = 0;
        
        for (LinkInfo link : links.values()) {
            if (link.getStatus() == LinkInfo.LinkStatus.ACTIVE) active++;
            else if (link.getStatus() == LinkInfo.LinkStatus.DEGRADED) degraded++;
            else failed++;
            
            LinkQualityInfo quality = linkQualities.get(link.getLinkId());
            if (quality != null) {
                totalLatency += quality.getLatency();
                totalPacketLoss += quality.getPacketLoss();
            }
        }
        
        stats.setActiveLinks(active);
        stats.setDegradedLinks(degraded);
        stats.setFailedLinks(failed);
        
        if (links.size() > 0) {
            stats.setAverageLatency(totalLatency / links.size());
            stats.setAveragePacketLoss(totalPacketLoss / links.size());
        }
        
        return stats;
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down NetworkService");
        disableQualityMonitor();
        executor.shutdown();
        links.clear();
        linkQualities.clear();
        log.info("NetworkService shutdown complete");
    }
    
    private void updateAllQualities() {
        for (String linkId : links.keySet()) {
            LinkQualityInfo quality = linkQualities.get(linkId);
            if (quality != null) {
                int latency = quality.getLatency() + (int)(Math.random() * 10 - 5);
                double packetLoss = Math.max(0, quality.getPacketLoss() + (Math.random() * 0.01 - 0.005));
                
                quality.setLatency(Math.max(1, latency));
                quality.setPacketLoss(Math.min(1, packetLoss));
                quality.setScore(calculateQualityScore(latency, packetLoss));
                
                notifyQualityChanged(linkId, quality);
            }
        }
    }
    
    private int calculateQualityScore(int latency, double packetLoss) {
        int score = 100;
        score -= Math.min(30, latency / 5);
        score -= (int)(packetLoss * 1000);
        return Math.max(0, Math.min(100, score));
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
}
