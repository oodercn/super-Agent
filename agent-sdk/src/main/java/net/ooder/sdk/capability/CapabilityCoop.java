package net.ooder.sdk.capability;

import net.ooder.sdk.capability.model.OrchestrationConfig;
import net.ooder.sdk.capability.model.OrchestrationStep;
import net.ooder.sdk.capability.model.StepCondition;
import net.ooder.sdk.capability.model.RetryPolicy;
import net.ooder.sdk.capability.model.OutputMapping;
import net.ooder.sdk.capability.model.ErrorHandling;
import net.ooder.sdk.capability.model.OrchestrationDef;
import net.ooder.sdk.capability.model.OrchestrationResult;
import net.ooder.sdk.capability.model.StepResult;
import net.ooder.sdk.capability.model.SceneGroupConfig;
import net.ooder.sdk.capability.model.SceneGroupDef;
import net.ooder.sdk.capability.model.ChainConfig;
import net.ooder.sdk.capability.model.ChainLink;
import net.ooder.sdk.capability.model.DataMapping;
import net.ooder.sdk.capability.model.ChainErrorHandling;
import net.ooder.sdk.capability.model.ChainDef;
import net.ooder.sdk.capability.model.ChainResult;
import net.ooder.sdk.capability.model.LinkResult;
import net.ooder.sdk.capability.model.OrchestrationType;
import net.ooder.sdk.capability.model.OrchestrationStatus;
import net.ooder.sdk.capability.model.SceneGroupStatus;
import net.ooder.sdk.capability.model.ChainStatus;
import net.ooder.sdk.capability.model.ErrorStrategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapabilityCoop {
    
    CompletableFuture<OrchestrationDef> createOrchestration(OrchestrationConfig config);
    
    CompletableFuture<Void> deleteOrchestration(String orchestrationId);
    
    CompletableFuture<OrchestrationDef> getOrchestration(String orchestrationId);
    
    CompletableFuture<List<OrchestrationDef>> listOrchestrations();
    
    CompletableFuture<OrchestrationResult> executeOrchestration(String orchestrationId, Map<String, Object> input);
    
    CompletableFuture<SceneGroupDef> createSceneGroup(SceneGroupConfig config);
    
    CompletableFuture<Void> deleteSceneGroup(String sceneGroupId);
    
    CompletableFuture<SceneGroupDef> getSceneGroup(String sceneGroupId);
    
    CompletableFuture<List<SceneGroupDef>> listSceneGroups();
    
    CompletableFuture<ChainDef> createChain(ChainConfig config);
    
    CompletableFuture<Void> deleteChain(String chainId);
    
    CompletableFuture<ChainResult> executeChain(String chainId, Map<String, Object> input);
}
