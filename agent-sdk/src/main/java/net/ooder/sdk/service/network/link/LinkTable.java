
package net.ooder.sdk.service.network.link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkTable {
    
    private final Map<String, List<Link>> outgoingLinks;
    private final Map<String, List<Link>> incomingLinks;
    
    public LinkTable() {
        this.outgoingLinks = new ConcurrentHashMap<>();
        this.incomingLinks = new ConcurrentHashMap<>();
    }
    
    public void addEntry(String sourceId, String targetId, Link link) {
        outgoingLinks.computeIfAbsent(sourceId, k -> new ArrayList<>()).add(link);
        incomingLinks.computeIfAbsent(targetId, k -> new ArrayList<>()).add(link);
    }
    
    public void removeEntry(String sourceId, String targetId) {
        List<Link> outgoing = outgoingLinks.get(sourceId);
        if (outgoing != null) {
            outgoing.removeIf(l -> l.getTargetId().equals(targetId));
        }
        
        List<Link> incoming = incomingLinks.get(targetId);
        if (incoming != null) {
            incoming.removeIf(l -> l.getSourceId().equals(sourceId));
        }
    }
    
    public List<Link> getLinksFrom(String sourceId) {
        List<Link> links = outgoingLinks.get(sourceId);
        return links != null ? new ArrayList<>(links) : new ArrayList<>();
    }
    
    public List<Link> getLinksTo(String targetId) {
        List<Link> links = incomingLinks.get(targetId);
        return links != null ? new ArrayList<>(links) : new ArrayList<>();
    }
    
    public int getOutgoingCount(String sourceId) {
        List<Link> links = outgoingLinks.get(sourceId);
        return links != null ? links.size() : 0;
    }
    
    public int getIncomingCount(String targetId) {
        List<Link> links = incomingLinks.get(targetId);
        return links != null ? links.size() : 0;
    }
    
    public void clear() {
        outgoingLinks.clear();
        incomingLinks.clear();
    }
}
