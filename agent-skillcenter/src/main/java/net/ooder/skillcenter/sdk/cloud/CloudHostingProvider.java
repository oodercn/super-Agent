package net.ooder.skillcenter.sdk.cloud;

import java.util.List;
import java.util.Map;

public interface CloudHostingProvider {

    String getProviderName();

    List<String> getSupportedRegions();

    List<String> getSupportedInstanceTypes();

    CloudInstance createInstance(CloudHostingConfig config) throws CloudHostingException;

    List<CloudInstance> listInstances() throws CloudHostingException;

    CloudInstance getInstance(String instanceId) throws CloudHostingException;

    boolean deleteInstance(String instanceId) throws CloudHostingException;

    boolean startInstance(String instanceId) throws CloudHostingException;

    boolean stopInstance(String instanceId) throws CloudHostingException;

    boolean restartInstance(String instanceId) throws CloudHostingException;

    boolean scaleInstance(String instanceId, int replicas) throws CloudHostingException;

    InstanceMetrics getMetrics(String instanceId) throws CloudHostingException;

    List<LogEntry> getLogs(String instanceId, LogQueryOptions options) throws CloudHostingException;

    List<InstanceEvent> getEvents(String instanceId) throws CloudHostingException;

    boolean updateResources(String instanceId, ResourceConfig resources) throws CloudHostingException;

    ServiceEndpoint getServiceEndpoint(String instanceId) throws CloudHostingException;

    boolean healthCheck(String instanceId);

    CostEstimate estimateCost(CloudHostingConfig config);

    CostActual getActualCost(String instanceId, long startTime, long endTime);
}
