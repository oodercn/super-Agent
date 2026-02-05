package net.ooder.nexus.service;

import net.ooder.nexus.config.NexusServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NexusServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(NexusServiceFactory.class);

    @Autowired
    private MockNexusService mockService;

    @Autowired
    private RealNexusService realService;

    @Autowired
    private NexusServiceConfig config;

    private INexusService currentService;
    private final Map<String, INexusService> serviceMap = new ConcurrentHashMap<>();

    public NexusServiceFactory() {
        log.info("Initializing NexusServiceFactory");
    }

    @Autowired
    public void init() {
        log.info("Registering services in factory");
        serviceMap.put("MOCK", mockService);
        serviceMap.put("REAL", realService);
        
        String type = config.normalizeServiceType(config.getServiceType());
        currentService = serviceMap.get(type);
        if (currentService == null) {
            log.warn("Invalid service type: {}, defaulting to MOCK", type);
            currentService = mockService;
        }
        
        log.info("Current service type: {}", type);
        log.info("Service switch enabled: {}", config.isSwitchEnabled());
        log.info("NexusServiceFactory initialized successfully");
    }

    public INexusService getService() {
        return currentService;
    }

    public INexusService getService(String type) {
        String serviceType = config.normalizeServiceType(type);
        INexusService service = serviceMap.get(serviceType);
        if (service == null) {
            log.warn("Service type not found: {}, returning current service", serviceType);
            return currentService;
        }
        return service;
    }

    public void switchService(String type) {
        if (!config.isSwitchEnabled()) {
            log.error("Service switching is disabled");
            throw new IllegalStateException("Service switching is disabled");
        }
        
        String serviceType = config.normalizeServiceType(type);
        if (!config.isValidServiceType(serviceType)) {
            log.error("Cannot switch to invalid service type: {}", serviceType);
            throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }
        
        INexusService newService = serviceMap.get(serviceType);
        if (newService == null) {
            log.error("Service not found: {}", serviceType);
            throw new IllegalArgumentException("Service not found: " + serviceType);
        }
        
        log.info("Switching service from {} to {}", getServiceType(), serviceType);
        currentService = newService;
        config.setServiceType(serviceType);
        log.info("Service switched successfully to {}", serviceType);
    }

    public String getServiceType() {
        if (currentService instanceof MockNexusService) {
            return "MOCK";
        } else if (currentService instanceof RealNexusService) {
            return "REAL";
        }
        return "UNKNOWN";
    }

    public Map<String, Object> getServiceInfo() {
        Map<String, Object> info = new java.util.HashMap<>();
        info.put("currentServiceType", getServiceType());
        info.put("availableServices", serviceMap.keySet());
        info.put("configuredServiceType", config.getServiceType().toUpperCase());
        info.put("switchEnabled", config.isSwitchEnabled());
        info.put("requireAuth", config.isRequireAuth());
        info.put("timestamp", System.currentTimeMillis());
        return info;
    }

    public boolean isMockService() {
        return currentService instanceof MockNexusService;
    }

    public boolean isRealService() {
        return currentService instanceof RealNexusService;
    }

    public boolean isSwitchEnabled() {
        return config.isSwitchEnabled();
    }
}