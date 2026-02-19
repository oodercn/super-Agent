
package net.ooder.sdk.service.network.link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkMonitor {
    
    private static final Logger log = LoggerFactory.getLogger(LinkMonitor.class);
    
    private final LinkManager linkManager;
    private final Map<String, LinkStats> linkStats;
    private final ScheduledExecutorService scheduler;
    private final List<LinkMonitorListener> listeners;
    
    private long checkInterval = 10000;
    private long latencyThreshold = 500;
    private double packetLossThreshold = 0.1;
    private volatile boolean running;
    
    public LinkMonitor(LinkManager linkManager) {
        this.linkManager = linkManager;
        this.linkStats = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.listeners = new ArrayList<>();
    }
    
    public void start() {
        running = true;
        scheduler.scheduleAtFixedRate(this::checkLinks, 0, checkInterval, TimeUnit.MILLISECONDS);
        log.info("Link monitor started");
    }
    
    public void stop() {
        running = false;
        scheduler.shutdown();
        log.info("Link monitor stopped");
    }
    
    private void checkLinks() {
        if (!running) return;
        
        List<Link> links = linkManager.getAllLinks();
        for (Link link : links) {
            checkLink(link);
        }
    }
    
    private void checkLink(Link link) {
        LinkStats stats = linkStats.computeIfAbsent(link.getLinkId(), k -> new LinkStats());
        
        long latency = measureLatency(link);
        double packetLoss = calculatePacketLoss(link);
        
        stats.setLastLatency(latency);
        stats.setPacketLoss(packetLoss);
        stats.setLastCheckTime(System.currentTimeMillis());
        
        LinkQuality quality = evaluateQuality(latency, packetLoss);
        stats.setQuality(quality);
        
        if (quality == LinkQuality.POOR) {
            notifyListeners(link, quality, stats);
            log.warn("Link quality degraded: {} (latency={}ms, loss={}%)", 
                link.getLinkId(), latency, packetLoss * 100);
        }
    }
    
    private long measureLatency(Link link) {
        String targetHost = extractTargetHost(link);
        if (targetHost == null || targetHost.isEmpty()) {
            return -1;
        }
        
        try {
            long start = System.nanoTime();
            java.net.InetAddress address = java.net.InetAddress.getByName(targetHost);
            boolean reachable = address.isReachable((int) Math.min(latencyThreshold, 5000));
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            
            if (reachable) {
                return elapsed;
            } else {
                return -1;
            }
        } catch (Exception e) {
            log.debug("Latency measurement failed for {}: {}", targetHost, e.getMessage());
            return -1;
        }
    }
    
    private double calculatePacketLoss(Link link) {
        LinkStats stats = linkStats.get(link.getLinkId());
        if (stats == null || stats.getPacketsSent() == 0) {
            return 0.0;
        }
        
        long sent = stats.getPacketsSent();
        long received = stats.getPacketsReceived();
        
        if (sent <= received) {
            return 0.0;
        }
        
        return (double) (sent - received) / sent;
    }
    
    private String extractTargetHost(Link link) {
        if (link.getMetadata() != null) {
            Object host = link.getMetadata().get("targetHost");
            if (host != null) {
                return host.toString();
            }
        }
        return link.getTargetId();
    }
    
    private LinkQuality evaluateQuality(long latency, double packetLoss) {
        if (latency > latencyThreshold || packetLoss > packetLossThreshold) {
            return LinkQuality.POOR;
        } else if (latency > latencyThreshold / 2 || packetLoss > packetLossThreshold / 2) {
            return LinkQuality.FAIR;
        } else {
            return LinkQuality.GOOD;
        }
    }
    
    public LinkStats getStats(String linkId) {
        return linkStats.get(linkId);
    }
    
    public void recordPacketSent(String linkId) {
        LinkStats stats = linkStats.computeIfAbsent(linkId, k -> new LinkStats());
        stats.incrementPacketsSent();
    }
    
    public void recordPacketReceived(String linkId) {
        LinkStats stats = linkStats.get(linkId);
        if (stats != null) {
            stats.incrementPacketsReceived();
        }
    }
    
    public void addListener(LinkMonitorListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners(Link link, LinkQuality quality, LinkStats stats) {
        for (LinkMonitorListener listener : listeners) {
            try {
                listener.onLinkQualityChange(link, quality, stats);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void setCheckInterval(long interval) { this.checkInterval = interval; }
    public void setLatencyThreshold(long threshold) { this.latencyThreshold = threshold; }
    public void setPacketLossThreshold(double threshold) { this.packetLossThreshold = threshold; }
}
