
package net.ooder.sdk.core.scene.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapabilityRegistry {
    
    private static final Logger log = LoggerFactory.getLogger(CapabilityRegistry.class);
    
    private final Map<String, Capability> capabilities;
    private final Map<String, CapabilityProvider> providers;
    private final Map<String, List<CapabilityConsumer.CapabilityListener>> listeners;
    
    public CapabilityRegistry() {
        this.capabilities = new ConcurrentHashMap<>();
        this.providers = new ConcurrentHashMap<>();
        this.listeners = new ConcurrentHashMap<>();
    }
    
    public void registerProvider(CapabilityProvider provider) {
        providers.put(provider.getProviderId(), provider);
        
        List<Capability> providerCapabilities = provider.getCapabilities();
        for (Capability cap : providerCapabilities) {
            registerCapability(cap);
        }
        
        log.info("Registered provider: {} with {} capabilities", 
            provider.getProviderId(), providerCapabilities.size());
    }
    
    public void unregisterProvider(String providerId) {
        CapabilityProvider provider = providers.remove(providerId);
        if (provider != null) {
            for (Capability cap : provider.getCapabilities()) {
                unregisterCapability(cap.getCapabilityId());
            }
            log.info("Unregistered provider: {}", providerId);
        }
    }
    
    public void registerCapability(Capability capability) {
        capabilities.put(capability.getCapabilityId(), capability);
        notifyListeners(capability.getCapabilityId(), "added", capability);
        log.debug("Registered capability: {}", capability.getCapabilityId());
    }
    
    public void unregisterCapability(String capabilityId) {
        Capability removed = capabilities.remove(capabilityId);
        if (removed != null) {
            notifyListeners(capabilityId, "removed", null);
            log.debug("Unregistered capability: {}", capabilityId);
        }
    }
    
    public Capability getCapability(String capabilityId) {
        return capabilities.get(capabilityId);
    }
    
    public List<Capability> getAllCapabilities() {
        return new ArrayList<>(capabilities.values());
    }
    
    public List<Capability> findByName(String name) {
        List<Capability> result = new ArrayList<>();
        for (Capability cap : capabilities.values()) {
            if (cap.getName().equals(name)) {
                result.add(cap);
            }
        }
        return result;
    }
    
    public List<Capability> findByType(CapabilityType type) {
        List<Capability> result = new ArrayList<>();
        for (Capability cap : capabilities.values()) {
            if (cap.getType() == type) {
                result.add(cap);
            }
        }
        return result;
    }
    
    public List<Capability> findByTag(String tag) {
        List<Capability> result = new ArrayList<>();
        for (Capability cap : capabilities.values()) {
            if (cap.getTags().contains(tag)) {
                result.add(cap);
            }
        }
        return result;
    }
    
    public List<Capability> findByProvider(String providerId) {
        List<Capability> result = new ArrayList<>();
        for (Capability cap : capabilities.values()) {
            if (providerId.equals(cap.getProviderId())) {
                result.add(cap);
            }
        }
        return result;
    }
    
    public List<Capability> query(String query) {
        List<Capability> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Capability cap : capabilities.values()) {
            if (matchesQuery(cap, lowerQuery)) {
                result.add(cap);
            }
        }
        
        return result;
    }
    
    private boolean matchesQuery(Capability cap, String query) {
        if (cap.getName().toLowerCase().contains(query)) return true;
        if (cap.getDescription().toLowerCase().contains(query)) return true;
        for (String tag : cap.getTags()) {
            if (tag.toLowerCase().contains(query)) return true;
        }
        return false;
    }
    
    public CapabilityProvider getProvider(String providerId) {
        return providers.get(providerId);
    }
    
    public List<CapabilityProvider> getAllProviders() {
        return new ArrayList<>(providers.values());
    }
    
    public void subscribe(String capabilityId, CapabilityConsumer.CapabilityListener listener) {
        listeners.computeIfAbsent(capabilityId, k -> new ArrayList<>()).add(listener);
    }
    
    public void unsubscribe(String capabilityId, CapabilityConsumer.CapabilityListener listener) {
        List<CapabilityConsumer.CapabilityListener> list = listeners.get(capabilityId);
        if (list != null) {
            list.remove(listener);
        }
    }
    
    private void notifyListeners(String capabilityId, String event, Capability capability) {
        List<CapabilityConsumer.CapabilityListener> list = listeners.get(capabilityId);
        if (list != null) {
            for (CapabilityConsumer.CapabilityListener listener : list) {
                try {
                    if ("removed".equals(event)) {
                        listener.onCapabilityRemoved(capabilityId);
                    } else {
                        listener.onCapabilityChanged(capability);
                    }
                } catch (Exception e) {
                    log.warn("Listener notification failed", e);
                }
            }
        }
    }
    
    public int getCapabilityCount() {
        return capabilities.size();
    }
    
    public int getProviderCount() {
        return providers.size();
    }
}
