package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.NexusNetworkService;
import net.ooder.sdk.api.network.LinkInfo;
import net.ooder.sdk.api.network.LinkQualityInfo;
import net.ooder.sdk.api.network.LinkListener;
import net.ooder.sdk.api.network.NetworkService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NexusNetworkServiceImpl implements NexusNetworkService {

    private static final Logger log = LoggerFactory.getLogger(NexusNetworkServiceImpl.class);

    private final NetworkService sdkNetworkService;
    private final List<LinkListener> listeners = new CopyOnWriteArrayList<LinkListener>();

    @Autowired
    public NexusNetworkServiceImpl(@Autowired(required = false) NetworkService sdkNetworkService) {
        this.sdkNetworkService = sdkNetworkService;
        log.info("NexusNetworkServiceImpl initialized with SDK 0.7.2 NetworkService: {}", sdkNetworkService != null ? "available" : "not available");
    }

    @Override
    public LinkInfo createLink(String sourceId, String targetId, String linkType) {
        log.info("Creating link: {} -> {} of type {}", sourceId, targetId, linkType);
        
        if (sdkNetworkService == null) {
            log.warn("NetworkService not available");
            return null;
        }
        
        try {
            LinkInfo.LinkType type = LinkInfo.LinkType.valueOf(linkType);
            return sdkNetworkService.createLink(sourceId, targetId, type);
        } catch (Exception e) {
            log.error("Failed to create link", e);
            return null;
        }
    }

    @Override
    public CompletableFuture<LinkInfo> createLinkAsync(String sourceId, String targetId, String linkType) {
        log.info("Creating link async: {} -> {} of type {}", sourceId, targetId, linkType);
        
        return CompletableFuture.supplyAsync(() -> createLink(sourceId, targetId, linkType));
    }

    @Override
    public Optional<LinkInfo> getLink(String linkId) {
        log.info("Getting link: {}", linkId);
        
        if (sdkNetworkService == null) {
            return Optional.empty();
        }
        
        try {
            return sdkNetworkService.getLink(linkId);
        } catch (Exception e) {
            log.error("Failed to get link: {}", linkId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<LinkInfo> getLink(String sourceId, String targetId) {
        log.info("Getting link: {} -> {}", sourceId, targetId);
        
        if (sdkNetworkService == null) {
            return Optional.empty();
        }
        
        try {
            List<LinkInfo> links = sdkNetworkService.getAllLinks();
            return links.stream()
                .filter(l -> l.getSourceId().equals(sourceId) && l.getTargetId().equals(targetId))
                .findFirst();
        } catch (Exception e) {
            log.error("Failed to get link", e);
            return Optional.empty();
        }
    }

    @Override
    public void removeLink(String linkId) {
        log.info("Removing link: {}", linkId);
        
        if (sdkNetworkService != null) {
            try {
                sdkNetworkService.removeLink(linkId);
            } catch (Exception e) {
                log.error("Failed to remove link: {}", linkId, e);
            }
        }
    }

    @Override
    public List<LinkInfo> getAllLinks() {
        log.info("Getting all links");
        
        if (sdkNetworkService == null) {
            return new ArrayList<LinkInfo>();
        }
        
        try {
            return sdkNetworkService.getAllLinks();
        } catch (Exception e) {
            log.error("Failed to get all links", e);
            return new ArrayList<LinkInfo>();
        }
    }

    @Override
    public List<LinkInfo> getLinksFrom(String sourceId) {
        log.info("Getting links from: {}", sourceId);
        
        List<LinkInfo> allLinks = getAllLinks();
        List<LinkInfo> result = new ArrayList<LinkInfo>();
        for (LinkInfo link : allLinks) {
            if (link.getSourceId().equals(sourceId)) {
                result.add(link);
            }
        }
        return result;
    }

    @Override
    public List<LinkInfo> getLinksTo(String targetId) {
        log.info("Getting links to: {}", targetId);
        
        List<LinkInfo> allLinks = getAllLinks();
        List<LinkInfo> result = new ArrayList<LinkInfo>();
        for (LinkInfo link : allLinks) {
            if (link.getTargetId().equals(targetId)) {
                result.add(link);
            }
        }
        return result;
    }

    @Override
    public int getLinkCount() {
        return getAllLinks().size();
    }

    @Override
    public boolean hasLink(String sourceId, String targetId) {
        return getLink(sourceId, targetId).isPresent();
    }

    @Override
    public LinkQualityInfo getLinkQuality(String linkId) {
        log.info("Getting link quality: {}", linkId);
        
        if (sdkNetworkService == null) {
            return null;
        }
        
        try {
            return sdkNetworkService.getLinkQuality(linkId);
        } catch (Exception e) {
            log.error("Failed to get link quality: {}", linkId, e);
            return null;
        }
    }

    @Override
    public void enableQualityMonitor(long intervalMs) {
        log.info("Enabling quality monitor with interval: {}ms", intervalMs);
        
        if (sdkNetworkService != null) {
            try {
                sdkNetworkService.enableQualityMonitor(intervalMs);
            } catch (Exception e) {
                log.error("Failed to enable quality monitor", e);
            }
        }
    }

    @Override
    public void disableQualityMonitor() {
        log.info("Disabling quality monitor");
        
        if (sdkNetworkService != null) {
            try {
                sdkNetworkService.disableQualityMonitor();
            } catch (Exception e) {
                log.error("Failed to disable quality monitor", e);
            }
        }
    }

    @Override
    public boolean isQualityMonitorEnabled() {
        if (sdkNetworkService == null) {
            return false;
        }
        
        try {
            return sdkNetworkService.isQualityMonitorEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<LinkInfo> findOptimalPath(String sourceId, String targetId) {
        log.info("Finding optimal path: {} -> {}", sourceId, targetId);
        
        if (sdkNetworkService == null) {
            return new ArrayList<LinkInfo>();
        }
        
        try {
            return sdkNetworkService.findOptimalPath(sourceId, targetId);
        } catch (Exception e) {
            log.error("Failed to find optimal path", e);
            return new ArrayList<LinkInfo>();
        }
    }

    @Override
    public List<List<LinkInfo>> findAllPaths(String sourceId, String targetId, int maxPaths) {
        log.info("Finding all paths: {} -> {} (max: {})", sourceId, targetId, maxPaths);
        
        List<List<LinkInfo>> paths = new ArrayList<List<LinkInfo>>();
        List<LinkInfo> optimalPath = findOptimalPath(sourceId, targetId);
        if (!optimalPath.isEmpty()) {
            paths.add(optimalPath);
        }
        return paths;
    }

    @Override
    public Map<String, Object> getNetworkStats() {
        log.info("Getting network stats");
        
        Map<String, Object> stats = new HashMap<String, Object>();
        stats.put("totalLinks", getLinkCount());
        stats.put("timestamp", System.currentTimeMillis());
        
        if (sdkNetworkService != null) {
            try {
                NetworkService.NetworkStats sdkStats = sdkNetworkService.getNetworkStats();
                stats.put("activeLinks", sdkStats.getActiveLinks());
                stats.put("failedLinks", sdkStats.getFailedLinks());
            } catch (Exception e) {
                log.error("Failed to get network stats", e);
            }
        }
        
        return stats;
    }

    @Override
    public void addLinkListener(LinkListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLinkListener(LinkListener listener) {
        listeners.remove(listener);
    }
}
