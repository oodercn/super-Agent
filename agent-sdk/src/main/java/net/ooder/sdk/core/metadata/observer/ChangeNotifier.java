
package net.ooder.sdk.core.metadata.observer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.metadata.ChangeRecord;
import net.ooder.sdk.api.metadata.FourDimensionMetadata;

public class ChangeNotifier {
    
    private static final Logger log = LoggerFactory.getLogger(ChangeNotifier.class);
    
    private final List<MetadataObserver> globalObservers;
    private final Map<String, List<MetadataObserver>> resourceObservers;
    
    public ChangeNotifier() {
        this.globalObservers = new CopyOnWriteArrayList<>();
        this.resourceObservers = new ConcurrentHashMap<>();
    }
    
    public void addObserver(MetadataObserver observer) {
        globalObservers.add(observer);
        log.debug("Added global metadata observer: {}", observer.getClass().getSimpleName());
    }
    
    public void removeObserver(MetadataObserver observer) {
        globalObservers.remove(observer);
    }
    
    public void addObserver(String resourceId, MetadataObserver observer) {
        resourceObservers.computeIfAbsent(resourceId, k -> new CopyOnWriteArrayList<>()).add(observer);
        log.debug("Added resource-specific observer for: {}", resourceId);
    }
    
    public void removeObserver(String resourceId, MetadataObserver observer) {
        List<MetadataObserver> observers = resourceObservers.get(resourceId);
        if (observers != null) {
            observers.remove(observer);
        }
    }
    
    public void notifyCreated(String resourceId, FourDimensionMetadata metadata) {
        for (MetadataObserver observer : globalObservers) {
            try {
                observer.onMetadataCreated(resourceId, metadata);
            } catch (Exception e) {
                log.warn("Observer notification failed for create event", e);
            }
        }
        
        List<MetadataObserver> resourceSpecific = resourceObservers.get(resourceId);
        if (resourceSpecific != null) {
            for (MetadataObserver observer : resourceSpecific) {
                try {
                    observer.onMetadataCreated(resourceId, metadata);
                } catch (Exception e) {
                    log.warn("Resource-specific observer notification failed", e);
                }
            }
        }
    }
    
    public void notifyUpdated(String resourceId, FourDimensionMetadata oldMetadata, FourDimensionMetadata newMetadata) {
        for (MetadataObserver observer : globalObservers) {
            try {
                observer.onMetadataUpdated(resourceId, oldMetadata, newMetadata);
            } catch (Exception e) {
                log.warn("Observer notification failed for update event", e);
            }
        }
        
        List<MetadataObserver> resourceSpecific = resourceObservers.get(resourceId);
        if (resourceSpecific != null) {
            for (MetadataObserver observer : resourceSpecific) {
                try {
                    observer.onMetadataUpdated(resourceId, oldMetadata, newMetadata);
                } catch (Exception e) {
                    log.warn("Resource-specific observer notification failed", e);
                }
            }
        }
    }
    
    public void notifyDeleted(String resourceId) {
        for (MetadataObserver observer : globalObservers) {
            try {
                observer.onMetadataDeleted(resourceId);
            } catch (Exception e) {
                log.warn("Observer notification failed for delete event", e);
            }
        }
        
        List<MetadataObserver> resourceSpecific = resourceObservers.get(resourceId);
        if (resourceSpecific != null) {
            for (MetadataObserver observer : resourceSpecific) {
                try {
                    observer.onMetadataDeleted(resourceId);
                } catch (Exception e) {
                    log.warn("Resource-specific observer notification failed", e);
                }
            }
        }
    }
    
    public void notifyChangeRecorded(ChangeRecord changeRecord) {
        for (MetadataObserver observer : globalObservers) {
            try {
                observer.onChangeRecorded(changeRecord);
            } catch (Exception e) {
                log.warn("Observer notification failed for change record", e);
            }
        }
    }
    
    public int getGlobalObserverCount() {
        return globalObservers.size();
    }
    
    public int getResourceObserverCount(String resourceId) {
        List<MetadataObserver> observers = resourceObservers.get(resourceId);
        return observers != null ? observers.size() : 0;
    }
}
