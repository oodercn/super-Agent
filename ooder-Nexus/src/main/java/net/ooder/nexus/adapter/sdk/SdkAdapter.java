package net.ooder.nexus.adapter.sdk;

import net.ooder.sdk.api.OoderSDK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(SdkAdapter.class);

    private final OoderSDK sdk;

    @Autowired
    public SdkAdapter(@Autowired(required = false) OoderSDK sdk) {
        this.sdk = sdk;
        if (sdk != null) {
            log.info("SdkAdapter initialized with OoderSDK");
        } else {
            log.info("SdkAdapter initialized without OoderSDK (standalone mode)");
        }
    }

    public void initialize() {
        if (sdk != null && !sdk.isInitialized()) {
            try {
                sdk.initialize();
                log.info("SDK initialized via adapter");
            } catch (Exception e) {
                log.error("Failed to initialize SDK", e);
            }
        }
    }

    public OoderSDK getSdk() {
        return sdk;
    }

    public boolean isAvailable() {
        return sdk != null && sdk.isRunning();
    }

    public String getVersion() {
        return "0.7.1";
    }

    public <T> T getService(Class<T> serviceClass) {
        if (sdk == null) {
            return null;
        }

        if (serviceClass == net.ooder.sdk.api.skill.SkillPackageManager.class) {
            return serviceClass.cast(sdk.getSkillPackageManager());
        } else if (serviceClass == net.ooder.sdk.api.scene.SceneGroupManager.class) {
            return serviceClass.cast(sdk.getSceneGroupManager());
        } else if (serviceClass == net.ooder.sdk.api.scene.CapabilityInvoker.class) {
            return serviceClass.cast(sdk.getCapabilityInvoker());
        }

        return null;
    }
}
