package net.ooder.examples.skilla.config;

import net.ooder.examples.skilla.service.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppLifecycleManager implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AppLifecycleManager.class);

    private final DiscoveryService discoveryService;

    @Autowired
    public AppLifecycleManager(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Application is shutting down, performing cleanup operations");

        try {
            // Cleanup operations
            logger.info("Cleanup operations completed");
        } catch (Exception e) {
            logger.error("Error during application cleanup: {}", e.getMessage(), e);
        }
    }
}
