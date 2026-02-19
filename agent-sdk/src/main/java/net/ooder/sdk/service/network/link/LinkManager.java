
package net.ooder.sdk.service.network.link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkManager {
    
    private static final Logger log = LoggerFactory.getLogger(LinkManager.class);
    
    private final Map<String, Link> links;
    private final LinkTable linkTable;
    private final List<LinkEventListener> listeners;
    
    private long linkTimeout = 60000;
    private int maxLinks = 1000;
    
    public LinkManager() {
        this.links = new ConcurrentHashMap<>();
        this.linkTable = new LinkTable();
        this.listeners = new ArrayList<>();
    }
    
    public Link createLink(String sourceId, String targetId, LinkType type) {
        String linkId = generateLinkId(sourceId, targetId);
        
        if (links.size() >= maxLinks) {
            cleanupStaleLinks();
        }
        
        Link link = new Link();
        link.setLinkId(linkId);
        link.setSourceId(sourceId);
        link.setTargetId(targetId);
        link.setType(type);
        link.setCreateTime(System.currentTimeMillis());
        link.setLastActive(System.currentTimeMillis());
        link.setStatus(LinkStatus.ACTIVE);
        
        links.put(linkId, link);
        linkTable.addEntry(sourceId, targetId, link);
        
        notifyListeners("CREATE", link);
        log.debug("Created link: {} -> {} ({})", sourceId, targetId, type);
        
        return link;
    }
    
    public Link getLink(String linkId) {
        return links.get(linkId);
    }
    
    public Link getLink(String sourceId, String targetId) {
        String linkId = generateLinkId(sourceId, targetId);
        return links.get(linkId);
    }
    
    public void removeLink(String linkId) {
        Link link = links.remove(linkId);
        if (link != null) {
            linkTable.removeEntry(link.getSourceId(), link.getTargetId());
            notifyListeners("REMOVE", link);
            log.debug("Removed link: {}", linkId);
        }
    }
    
    public void updateLinkActivity(String linkId) {
        Link link = links.get(linkId);
        if (link != null) {
            link.setLastActive(System.currentTimeMillis());
        }
    }
    
    public List<Link> getLinksFrom(String sourceId) {
        return linkTable.getLinksFrom(sourceId);
    }
    
    public List<Link> getLinksTo(String targetId) {
        return linkTable.getLinksTo(targetId);
    }
    
    public List<Link> getAllLinks() {
        return new ArrayList<>(links.values());
    }
    
    public boolean hasLink(String sourceId, String targetId) {
        return links.containsKey(generateLinkId(sourceId, targetId));
    }
    
    public int getLinkCount() {
        return links.size();
    }
    
    private void cleanupStaleLinks() {
        long now = System.currentTimeMillis();
        links.entrySet().removeIf(entry -> {
            Link link = entry.getValue();
            boolean stale = now - link.getLastActive() > linkTimeout;
            if (stale) {
                linkTable.removeEntry(link.getSourceId(), link.getTargetId());
                notifyListeners("TIMEOUT", link);
            }
            return stale;
        });
    }
    
    private String generateLinkId(String sourceId, String targetId) {
        return sourceId + "->" + targetId;
    }
    
    public void addListener(LinkEventListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(String event, Link link) {
        for (LinkEventListener listener : listeners) {
            try {
                listener.onLinkEvent(event, link);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void setLinkTimeout(long timeout) { this.linkTimeout = timeout; }
    public void setMaxLinks(int max) { this.maxLinks = max; }
}
