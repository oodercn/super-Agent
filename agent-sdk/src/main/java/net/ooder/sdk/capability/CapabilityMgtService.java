package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.CapabilityRegistration;
import net.ooder.sdk.capability.model.CapabilityInfo;
import net.ooder.sdk.capability.model.CapabilityUpdate;
import net.ooder.sdk.capability.model.CapabilityQuery;
import net.ooder.sdk.capability.model.VersionInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CapabilityMgtService {
    
    CompletableFuture<CapabilityInfo> register(CapabilityRegistration registration);
    
    CompletableFuture<Void> unregister(String capabilityId);
    
    CompletableFuture<CapabilityInfo> getCapability(String capabilityId);
    
    CompletableFuture<List<CapabilityInfo>> listCapabilities(CapabilityQuery query);
    
    CompletableFuture<Void> enableCapability(String capabilityId);
    
    CompletableFuture<Void> disableCapability(String capabilityId);
    
    CompletableFuture<CapabilityInfo> updateCapability(String capabilityId, CapabilityUpdate update);
    
    CompletableFuture<VersionInfo> getVersion(String capabilityId, String version);
    
    CompletableFuture<List<VersionInfo>> listVersions(String capabilityId);
    
    CompletableFuture<Void> rollbackVersion(String capabilityId, String version);
}
