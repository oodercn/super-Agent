package net.ooder.nexus.service;

import net.ooder.sdk.api.network.LinkInfo;
import net.ooder.sdk.api.network.LinkQualityInfo;
import net.ooder.sdk.api.network.LinkListener;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface NexusNetworkService {

    LinkInfo createLink(String sourceId, String targetId, String linkType);
    
    CompletableFuture<LinkInfo> createLinkAsync(String sourceId, String targetId, String linkType);
    
    Optional<LinkInfo> getLink(String linkId);
    
    Optional<LinkInfo> getLink(String sourceId, String targetId);
    
    void removeLink(String linkId);
    
    List<LinkInfo> getAllLinks();
    
    List<LinkInfo> getLinksFrom(String sourceId);
    
    List<LinkInfo> getLinksTo(String targetId);
    
    int getLinkCount();
    
    boolean hasLink(String sourceId, String targetId);
    
    LinkQualityInfo getLinkQuality(String linkId);
    
    void enableQualityMonitor(long intervalMs);
    
    void disableQualityMonitor();
    
    boolean isQualityMonitorEnabled();
    
    List<LinkInfo> findOptimalPath(String sourceId, String targetId);
    
    List<List<LinkInfo>> findAllPaths(String sourceId, String targetId, int maxPaths);
    
    Map<String, Object> getNetworkStats();
    
    void addLinkListener(LinkListener listener);
    
    void removeLinkListener(LinkListener listener);
}
