package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.CapabilitySpec;
import net.ooder.sdk.capability.model.SpecDefinition;
import net.ooder.sdk.capability.model.ValidationResult;
import net.ooder.sdk.capability.model.SpecQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CapabilitySpecService {
    
    CompletableFuture<CapabilitySpec> registerSpec(SpecDefinition definition);
    
    CompletableFuture<CapabilitySpec> getSpec(String specId);
    
    CompletableFuture<CapabilitySpec> getSpecByName(String name, String version);
    
    CompletableFuture<List<CapabilitySpec>> listSpecs(SpecQuery query);
    
    CompletableFuture<CapabilitySpec> updateSpec(String specId, SpecDefinition definition);
    
    CompletableFuture<Void> deleteSpec(String specId);
    
    CompletableFuture<ValidationResult> validateSpec(SpecDefinition definition);
    
    CompletableFuture<List<CapabilitySpec>> searchSpecs(String keyword);
}
