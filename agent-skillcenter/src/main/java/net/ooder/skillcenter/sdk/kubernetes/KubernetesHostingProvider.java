package net.ooder.skillcenter.sdk.kubernetes;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.*;
import net.ooder.skillcenter.sdk.cloud.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KubernetesHostingProvider implements CloudHostingProvider {

    private static final Logger log = LoggerFactory.getLogger(KubernetesHostingProvider.class);
    private static final String PROVIDER_NAME = "kubernetes";
    
    private final KubernetesClient client;
    private final Map<String, CloudInstance> instances = new HashMap<>();
    
    public KubernetesHostingProvider() {
        io.fabric8.kubernetes.client.Config config = new io.fabric8.kubernetes.client.ConfigBuilder().build();
        this.client = new DefaultKubernetesClient(config);
        log.info("[KubernetesProvider] Initialized with config: {}", config.getMasterUrl());
    }
    
    public KubernetesHostingProvider(String kubeconfig) {
        io.fabric8.kubernetes.client.Config config = new io.fabric8.kubernetes.client.ConfigBuilder().withMasterUrl(kubeconfig).build();
        this.client = new DefaultKubernetesClient(config);
        log.info("[KubernetesProvider] Initialized with custom kubeconfig");
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public List<String> getSupportedRegions() {
        return Arrays.asList("default", "us-west-1", "us-east-1", "eu-west-1", "asia-east-1");
    }

    @Override
    public List<String> getSupportedInstanceTypes() {
        return Arrays.asList("deployment", "statefulset", "daemonset", "job", "cronjob");
    }

    @Override
    public CloudInstance createInstance(CloudHostingConfig config) throws CloudHostingException {
        log.info("[KubernetesProvider] Creating instance: {}", config.getInstanceName());
        
        try {
            String namespace = config.getRegion() != null ? config.getRegion() : "default";
            String instanceName = config.getInstanceName();
            
            Map<String, String> labels = new HashMap<>();
            labels.put("app", instanceName);
            labels.put("managed-by", "skillcenter");
            
            Map<String, String> annotations = new HashMap<>();
            annotations.put("skillcenter.io/created-at", String.valueOf(System.currentTimeMillis()));
            
            Map<String, Quantity> requests = new HashMap<>();
            requests.put("cpu", new Quantity(config.getResources().getCpuLimit() + ""));
            requests.put("memory", new Quantity(config.getResources().getMemoryLimit() + ""));
            
            ResourceRequirements resources = new ResourceRequirementsBuilder()
                .withRequests(requests)
                .withLimits(requests)
                .build();
            
            List<ContainerPort> ports = new ArrayList<>();
            if (config.getContainer() != null && config.getContainer().getPorts() != null) {
                for (Object p : config.getContainer().getPorts()) {
                    if (p instanceof Integer) {
                        ports.add(new ContainerPortBuilder().withContainerPort((Integer) p).build());
                    }
                }
            }
            
            Container container = new ContainerBuilder()
                .withName(instanceName)
                .withImage(config.getContainer() != null ? config.getContainer().getImage() : "nginx:latest")
                .withResources(resources)
                .withPorts(ports)
                .build();
            
            Deployment deployment = new DeploymentBuilder()
                .withNewMetadata()
                    .withName(instanceName)
                    .withNamespace(namespace)
                    .withLabels(labels)
                    .withAnnotations(annotations)
                .endMetadata()
                .withNewSpec()
                    .withReplicas(config.getScaling() != null ? config.getScaling().getMinReplicas() : 1)
                    .withNewSelector()
                        .withMatchLabels(labels)
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .withLabels(labels)
                        .endMetadata()
                        .withNewSpec()
                            .withContainers(container)
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
            
            client.apps().deployments().inNamespace(namespace).create(deployment);
            
            CloudInstance instance = new CloudInstance();
            instance.setId(instanceName + "-" + UUID.randomUUID().toString().substring(0, 8));
            instance.setName(instanceName);
            instance.setProvider(PROVIDER_NAME);
            instance.setProviderType("deployment");
            instance.setRegion(namespace);
            instance.setStatus("creating");
            instance.setReplicas(config.getScaling() != null ? config.getScaling().getMinReplicas() : 1);
            instance.setCreatedAt(System.currentTimeMillis());
            
            instances.put(instance.getId(), instance);
            
            log.info("[KubernetesProvider] Created deployment: {} in namespace: {}", instanceName, namespace);
            return instance;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to create instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "CreateFailed", e.getMessage());
        }
    }

    @Override
    public List<CloudInstance> listInstances() throws CloudHostingException {
        log.info("[KubernetesProvider] Listing all instances");
        
        try {
            List<CloudInstance> result = new ArrayList<>();
            
            for (Deployment deployment : client.apps().deployments().inAnyNamespace().list().getItems()) {
                if (deployment.getMetadata().getLabels() != null && 
                    "skillcenter".equals(deployment.getMetadata().getLabels().get("managed-by"))) {
                    
                    CloudInstance instance = convertToInstance(deployment);
                    result.add(instance);
                    instances.put(instance.getId(), instance);
                }
            }
            
            result.addAll(instances.values());
            return result;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to list instances: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "ListFailed", e.getMessage());
        }
    }

    @Override
    public CloudInstance getInstance(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Getting instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            Deployment deployment = client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .get();
            
            if (deployment != null) {
                return convertToInstance(deployment);
            }
            
            return instance;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to get instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "GetFailed", e.getMessage());
        }
    }

    @Override
    public boolean deleteInstance(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Deleting instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            Boolean result = client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .delete();
            
            boolean deleted = result != null && result;
            
            if (deleted) {
                instances.remove(instanceId);
                log.info("[KubernetesProvider] Deleted instance: {}", instanceId);
            }
            
            return deleted;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to delete instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "DeleteFailed", e.getMessage());
        }
    }

    @Override
    public boolean startInstance(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Starting instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .scale(1);
            
            instance.setStatus("running");
            instance.setReplicas(1);
            
            log.info("[KubernetesProvider] Started instance: {}", instanceId);
            return true;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to start instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "StartFailed", e.getMessage());
        }
    }

    @Override
    public boolean stopInstance(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Stopping instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .scale(0);
            
            instance.setStatus("stopped");
            instance.setReplicas(0);
            
            log.info("[KubernetesProvider] Stopped instance: {}", instanceId);
            return true;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to stop instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "StopFailed", e.getMessage());
        }
    }

    @Override
    public boolean restartInstance(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Restarting instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .edit(d -> new DeploymentBuilder(d)
                    .editMetadata()
                        .addToAnnotations("kubectl.kubernetes.io/restartedAt", timestamp)
                    .endMetadata()
                    .build());
            
            instance.setStatus("starting");
            
            log.info("[KubernetesProvider] Restarted instance: {}", instanceId);
            return true;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to restart instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "RestartFailed", e.getMessage());
        }
    }

    @Override
    public boolean scaleInstance(String instanceId, int replicas) throws CloudHostingException {
        log.info("[KubernetesProvider] Scaling instance: {} to {} replicas", instanceId, replicas);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .scale(replicas);
            
            instance.setReplicas(replicas);
            
            log.info("[KubernetesProvider] Scaled instance: {} to {} replicas", instanceId, replicas);
            return true;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to scale instance: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "ScaleFailed", e.getMessage());
        }
    }

    @Override
    public InstanceMetrics getMetrics(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Getting metrics for instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        InstanceMetrics metrics = new InstanceMetrics();
        metrics.setInstanceId(instanceId);
        metrics.setTimestamp(System.currentTimeMillis());
        metrics.setCpuUsage(Math.random() * 2);
        metrics.setMemoryUsage(Math.random() * 1024 * 1024 * 1024);
        metrics.setReplicas(instance.getReplicas());
        
        return metrics;
    }

    @Override
    public List<LogEntry> getLogs(String instanceId, LogQueryOptions options) throws CloudHostingException {
        log.info("[KubernetesProvider] Getting logs for instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            List<LogEntry> logs = new ArrayList<>();
            
            PodList pods = client.pods()
                .inNamespace(instance.getRegion())
                .withLabel("app", instance.getName())
                .list();
            
            int limit = options != null && options.getLimit() > 0 ? options.getLimit() : 50;
            
            for (Pod pod : pods.getItems()) {
                String podLogs = client.pods()
                    .inNamespace(instance.getRegion())
                    .withName(pod.getMetadata().getName())
                    .tailingLines(limit)
                    .getLog();
                
                if (podLogs != null) {
                    for (String line : podLogs.split("\n")) {
                        LogEntry entry = new LogEntry();
                        entry.setTimestamp(System.currentTimeMillis());
                        entry.setMessage(line);
                        entry.setLevel("INFO");
                        entry.setSource(pod.getMetadata().getName());
                        logs.add(entry);
                    }
                }
            }
            
            return logs;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to get logs: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "LogsFailed", e.getMessage());
        }
    }

    @Override
    public List<InstanceEvent> getEvents(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Getting events for instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            List<InstanceEvent> events = new ArrayList<>();
            
            EventList k8sEvents = client.v1().events()
                .inNamespace(instance.getRegion())
                .withField("involvedObject.name", instance.getName())
                .list();
            
            for (Event event : k8sEvents.getItems()) {
                InstanceEvent ie = new InstanceEvent();
                ie.setEventId(event.getMetadata().getUid());
                ie.setTimestamp(System.currentTimeMillis());
                ie.setType(event.getType());
                ie.setReason(event.getReason());
                ie.setMessage(event.getMessage());
                events.add(ie);
            }
            
            return events;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to get events: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "EventsFailed", e.getMessage());
        }
    }

    @Override
    public boolean updateResources(String instanceId, ResourceConfig resources) throws CloudHostingException {
        log.info("[KubernetesProvider] Updating resources for instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            Map<String, Quantity> newResources = new HashMap<>();
            newResources.put("cpu", new Quantity(resources.getCpuLimit() + ""));
            newResources.put("memory", new Quantity(resources.getMemoryLimit() + ""));
            
            client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .edit(d -> new DeploymentBuilder(d)
                    .editSpec()
                        .editTemplate()
                            .editSpec()
                                .editContainer(0)
                                    .withNewResources()
                                        .withRequests(newResources)
                                        .withLimits(newResources)
                                    .endResources()
                                .endContainer()
                            .endSpec()
                        .endTemplate()
                    .endSpec()
                    .build());
            
            log.info("[KubernetesProvider] Updated resources for instance: {}", instanceId);
            return true;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to update resources: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "UpdateFailed", e.getMessage());
        }
    }

    @Override
    public ServiceEndpoint getServiceEndpoint(String instanceId) throws CloudHostingException {
        log.info("[KubernetesProvider] Getting service endpoint for instance: {}", instanceId);
        
        CloudInstance instance = instances.get(instanceId);
        if (instance == null) {
            throw new CloudHostingException(PROVIDER_NAME, "InstanceNotFound", "Instance not found: " + instanceId);
        }
        
        try {
            Service service = client.services()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .get();
            
            ServiceEndpoint endpoint = new ServiceEndpoint();
            endpoint.setInstanceId(instanceId);
            
            if (service != null && service.getSpec() != null) {
                String type = service.getSpec().getType();
                endpoint.setType(type);
                
                if ("LoadBalancer".equals(type) && service.getStatus().getLoadBalancer() != null) {
                    List<LoadBalancerIngress> ingresses = service.getStatus().getLoadBalancer().getIngress();
                    if (!ingresses.isEmpty()) {
                        endpoint.setUrl("http://" + ingresses.get(0).getIp());
                    }
                } else if ("NodePort".equals(type)) {
                    List<ServicePort> ports = service.getSpec().getPorts();
                    if (ports != null && !ports.isEmpty()) {
                        Integer nodePort = ports.get(0).getNodePort();
                        endpoint.setUrl("http://<node-ip>:" + nodePort);
                    }
                } else {
                    endpoint.setUrl("http://" + instance.getName() + "." + instance.getRegion() + ".svc.cluster.local");
                }
            }
            
            return endpoint;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Failed to get service endpoint: {}", e.getMessage(), e);
            throw new CloudHostingException(PROVIDER_NAME, "EndpointFailed", e.getMessage());
        }
    }

    @Override
    public boolean healthCheck(String instanceId) {
        try {
            CloudInstance instance = instances.get(instanceId);
            if (instance == null) return false;
            
            Deployment deployment = client.apps().deployments()
                .inNamespace(instance.getRegion())
                .withName(instance.getName())
                .get();
            
            if (deployment == null) return false;
            
            DeploymentStatus status = deployment.getStatus();
            return status != null && 
                   status.getReadyReplicas() != null && 
                   status.getReadyReplicas() > 0;
            
        } catch (Exception e) {
            log.error("[KubernetesProvider] Health check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public CostEstimate estimateCost(CloudHostingConfig config) {
        log.info("[KubernetesProvider] Estimating cost");
        
        CostEstimate estimate = new CostEstimate();
        estimate.setCurrency("CNY");
        
        double cpuCost = config.getResources().getCpuLimit() * 0.05;
        double memoryCost = config.getResources().getMemoryLimit() / (1024.0 * 1024 * 1024) * 0.01;
        
        double hourlyCost = cpuCost + memoryCost;
        
        estimate.setHourlyCost(hourlyCost);
        estimate.setDailyCost(hourlyCost * 24);
        estimate.setMonthlyCost(hourlyCost * 24 * 30);
        
        CostBreakdown breakdown = new CostBreakdown();
        breakdown.setCompute(cpuCost);
        breakdown.setMemory(memoryCost);
        estimate.setBreakdown(breakdown);
        
        return estimate;
    }

    @Override
    public CostActual getActualCost(String instanceId, long startTime, long endTime) {
        log.info("[KubernetesProvider] Getting actual cost for instance: {}", instanceId);
        
        CostActual cost = new CostActual();
        cost.setInstanceId(instanceId);
        cost.setCurrency("CNY");
        cost.setStartTime(startTime);
        cost.setEndTime(endTime);
        
        long hours = (endTime - startTime) / (1000 * 60 * 60);
        if (hours <= 0) hours = 1;
        
        double hourlyCost = 0.10;
        cost.setTotalCost(hourlyCost * hours);
        
        return cost;
    }
    
    private CloudInstance convertToInstance(Deployment deployment) {
        CloudInstance instance = new CloudInstance();
        instance.setId(deployment.getMetadata().getName() + "-" + deployment.getMetadata().getUid().substring(0, 8));
        instance.setName(deployment.getMetadata().getName());
        instance.setProvider(PROVIDER_NAME);
        instance.setProviderType("deployment");
        instance.setRegion(deployment.getMetadata().getNamespace());
        
        DeploymentStatus status = deployment.getStatus();
        if (status != null) {
            int ready = status.getReadyReplicas() != null ? status.getReadyReplicas() : 0;
            int desired = status.getReplicas() != null ? status.getReplicas() : 0;
            
            if (ready == desired && ready > 0) {
                instance.setStatus("running");
            } else if (ready == 0 && desired == 0) {
                instance.setStatus("stopped");
            } else if (ready < desired) {
                instance.setStatus("starting");
            } else {
                instance.setStatus("unknown");
            }
            
            instance.setReplicas(ready);
        } else {
            instance.setStatus("creating");
            instance.setReplicas(0);
        }
        
        instance.setCreatedAt(System.currentTimeMillis());
        
        return instance;
    }
}
