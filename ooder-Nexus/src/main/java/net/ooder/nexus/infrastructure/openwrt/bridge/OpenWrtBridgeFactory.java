package net.ooder.nexus.infrastructure.openwrt.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenWrtBridgeFactory {

    private static final Logger log = LoggerFactory.getLogger(OpenWrtBridgeFactory.class);

    private static volatile OpenWrtBridge instance;
    private static final Object lock = new Object();

    private OpenWrtBridgeFactory() {
    }

    public static OpenWrtBridge getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    log.info("Creating new OpenWrtBridge instance");
                    instance = new DefaultOpenWrtBridge();
                    log.info("OpenWrtBridge instance created successfully");
                }
            }
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (lock) {
            if (instance != null) {
                log.info("Resetting OpenWrtBridge instance");
                instance.disconnect();
                instance = null;
                log.info("OpenWrtBridge instance reset successfully");
            }
        }
    }

    public static OpenWrtBridge createNewInstance() {
        synchronized (lock) {
            log.info("Creating new OpenWrtBridge instance");
            OpenWrtBridge newInstance = new DefaultOpenWrtBridge();
            log.info("New OpenWrtBridge instance created successfully");
            return newInstance;
        }
    }
}
