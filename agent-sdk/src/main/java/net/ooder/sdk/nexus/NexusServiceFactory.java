package net.ooder.sdk.nexus;

public class NexusServiceFactory {
    
    public static NexusService create() {
        return new net.ooder.sdk.nexus.impl.NexusServiceImpl();
    }
}
