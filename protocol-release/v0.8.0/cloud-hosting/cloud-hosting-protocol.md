# Cloud Hosting Protocol v0.7.3

**Version**: 0.7.3  
**Status**: Draft  
**Last Updated**: 2026-02-20

---

## 1. Overview

### 1.1 Purpose

The Cloud Hosting Protocol defines the interface and operations for hosting skills in cloud environments, including Kubernetes clusters and other cloud providers.

### 1.2 Scope

This protocol covers:
- Instance lifecycle management (create, start, stop, delete)
- Scaling operations
- Resource management
- Monitoring and logging
- Service discovery
- Cost estimation

### 1.3 Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    SkillCenter Application                       │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              HostingExtensionSdkAdapter                  │   │
│  │  - Auto-scaling policies                                 │   │
│  │  - Service registration                                  │   │
│  │  - Volume management                                     │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              CloudHostingProvider Interface              │   │
│  │  - Instance lifecycle                                    │   │
│  │  - Resource management                                   │   │
│  │  - Monitoring & logging                                  │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                   │
│         ┌────────────────────┼────────────────────┐            │
│         ▼                    ▼                    ▼            │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐      │
│  │ Kubernetes  │     │  Tencent    │     │   Custom    │      │
│  │  Provider   │     │   Cloud     │     │  Provider   │      │
│  └─────────────┘     └─────────────┘     └─────────────┘      │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. CloudHostingProvider Interface

### 2.1 Interface Definition

```java
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
```

---

## 3. Kubernetes Provider

### 3.1 Overview

The Kubernetes provider uses the Fabric8 Kubernetes client to manage skills as Deployments in Kubernetes clusters.

### 3.2 Supported Instance Types

| Type | Description |
|------|-------------|
| `deployment` | Stateless application deployment |
| `statefulset` | Stateful application deployment |
| `daemonset` | Node-level deployment |
| `job` | One-time job |
| `cronjob` | Scheduled job |

### 3.3 Supported Regions

| Region | Description |
|--------|-------------|
| `default` | Default namespace |
| `us-west-1` | US West region |
| `us-east-1` | US East region |
| `eu-west-1` | Europe West region |
| `asia-east-1` | Asia East region |

### 3.4 Configuration

```yaml
kubernetes:
  enabled: true
  kubeconfig: ${KUBECONFIG:}
  masterUrl: ${KUBERNETES_MASTER:}
  namespace: default
  
  defaults:
    imagePullPolicy: IfNotPresent
    restartPolicy: Always
    serviceAccount: default
    
  resources:
    requests:
      cpu: "250m"
      memory: "256Mi"
    limits:
      cpu: "500m"
      memory: "512Mi"
      
  scaling:
    minReplicas: 1
    maxReplicas: 10
    targetCPUUtilization: 70
    
  networking:
    serviceType: ClusterIP
    ingressEnabled: true
```

### 3.5 Instance Creation Flow

```
┌─────────────┐                              ┌─────────────────┐
│ SkillCenter │                              │   Kubernetes    │
│             │                              │    Cluster      │
└─────────────┘                              └─────────────────┘
       │                                              │
       │  1. createInstance(config)                  │
       │─────────────────────────────────────────────▶│
       │                                              │
       │  2. Create Deployment                        │
       │     - Set labels (app, managed-by)           │
       │     - Set resource limits                    │
       │     - Configure container ports              │
       │                                              │
       │  3. Return CloudInstance                     │
       │◀─────────────────────────────────────────────│
       │                                              │
       │  4. Create Service (optional)                │
       │─────────────────────────────────────────────▶│
       │                                              │
       │  5. Create Ingress (optional)                │
       │─────────────────────────────────────────────▶│
       │                                              │
```

### 3.6 Deployment Labels

```yaml
metadata:
  labels:
    app: {instance-name}
    managed-by: skillcenter
  annotations:
    skillcenter.io/created-at: {timestamp}
    skillcenter.io/skill-id: {skill-id}
    skillcenter.io/version: {version}
```

---

## 4. Data Models

### 4.1 CloudHostingConfig

```java
public class CloudHostingConfig {
    private String instanceName;
    private String region;
    private String instanceType;
    private ContainerConfig container;
    private ResourceConfig resources;
    private NetworkConfig network;
    private ScaleConfig scaling;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private Map<String, String> env;
}

public class ContainerConfig {
    private String image;
    private List<Integer> ports;
    private List<String> commands;
    private List<String> args;
    private Map<String, String> env;
    private List<VolumeMount> volumeMounts;
}

public class ResourceConfig {
    private double cpuLimit;
    private double cpuRequest;
    private long memoryLimit;
    private long memoryRequest;
    private long storageLimit;
}

public class NetworkConfig {
    private String serviceType;
    private List<Integer> ports;
    private boolean ingressEnabled;
    private String ingressHost;
    private String ingressPath;
}

public class ScaleConfig {
    private int minReplicas;
    private int maxReplicas;
    private int targetCPUUtilization;
    private int targetMemoryUtilization;
}
```

### 4.2 CloudInstance

```java
public class CloudInstance {
    private String id;
    private String name;
    private String provider;
    private String providerType;
    private String region;
    private String status;
    private int replicas;
    private long createdAt;
    private long updatedAt;
    private Map<String, String> labels;
    private Map<String, String> metadata;
}
```

### 4.3 InstanceMetrics

```java
public class InstanceMetrics {
    private String instanceId;
    private long timestamp;
    private double cpuUsage;
    private long memoryUsage;
    private int replicas;
    private int readyReplicas;
    private double networkIn;
    private double networkOut;
    private long diskUsage;
}
```

### 4.4 LogEntry

```java
public class LogEntry {
    private long timestamp;
    private String level;
    private String message;
    private String source;
    private Map<String, String> metadata;
}
```

### 4.5 LogQueryOptions

```java
public class LogQueryOptions {
    private int limit;
    private long startTime;
    private long endTime;
    private String level;
    private String pattern;
    private boolean follow;
}
```

### 4.6 InstanceEvent

```java
public class InstanceEvent {
    private String eventId;
    private long timestamp;
    private String type;
    private String reason;
    private String message;
    private Map<String, String> metadata;
}
```

### 4.7 ServiceEndpoint

```java
public class ServiceEndpoint {
    private String instanceId;
    private String type;
    private String url;
    private String host;
    private int port;
    private Map<String, String> metadata;
}
```

### 4.8 Cost Models

```java
public class CostEstimate {
    private String currency;
    private double hourlyCost;
    private double dailyCost;
    private double monthlyCost;
    private CostBreakdown breakdown;
}

public class CostBreakdown {
    private double compute;
    private double memory;
    private double storage;
    private double network;
}

public class CostActual {
    private String instanceId;
    private String currency;
    private long startTime;
    private long endTime;
    private double totalCost;
    private CostBreakdown breakdown;
}
```

---

## 5. HostingExtensionSdkAdapter Interface

### 5.1 Interface Definition

```java
public interface HostingExtensionSdkAdapter {
    
    HostingCompatibilityDTO checkCompatibility(String skillId);
    
    AutoScalePolicyDTO getAutoScalePolicy(String instanceId);
    
    AutoScalePolicyDTO createAutoScalePolicy(AutoScalePolicyDTO policy);
    
    AutoScalePolicyDTO updateAutoScalePolicy(String policyId, AutoScalePolicyDTO policy);
    
    boolean deleteAutoScalePolicy(String policyId);
    
    boolean enableAutoScalePolicy(String policyId);
    
    boolean disableAutoScalePolicy(String policyId);
    
    ServiceEndpointDTO registerService(String instanceId, ServiceEndpointDTO service);
    
    boolean unregisterService(String serviceId);
    
    ServiceEndpointDTO getService(String serviceId);
    
    List<ServiceEndpointDTO> getServicesByInstance(String instanceId);
    
    List<ServiceEndpointDTO> discoverService(String serviceName);
    
    VolumeDTO createVolume(VolumeDTO volume);
    
    VolumeDTO getVolume(String volumeId);
    
    boolean deleteVolume(String volumeId);
    
    boolean mountVolume(String volumeId, String instanceId, String mountPath, boolean readOnly);
    
    boolean unmountVolume(String volumeId, String instanceId);
    
    List<VolumeDTO> listVolumes();
    
    boolean isAvailable();
}
```

### 5.2 AutoScalePolicyDTO

```java
public class AutoScalePolicyDTO {
    private String policyId;
    private String instanceId;
    private String name;
    private boolean enabled;
    
    private int minReplicas;
    private int maxReplicas;
    
    private List<ScaleRule> scaleUpRules;
    private List<ScaleRule> scaleDownRules;
    
    private long cooldownPeriod;
    private long stabilizationWindow;
}

public class ScaleRule {
    private String metricType;
    private String metricName;
    private double threshold;
    private String comparison;
    private int stepSize;
    private long evaluationPeriod;
}
```

### 5.3 VolumeDTO

```java
public class VolumeDTO {
    private String volumeId;
    private String name;
    private String type;
    private String storageClass;
    private long size;
    private String accessMode;
    private String status;
    private String instanceId;
    private String mountPath;
    private boolean readOnly;
    private long createdAt;
}
```

---

## 6. REST API Endpoints

### 6.1 Instance Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/instances` | GET | List all instances |
| `/api/hosting/instances` | POST | Create instance |
| `/api/hosting/instances/{id}` | GET | Get instance |
| `/api/hosting/instances/{id}` | DELETE | Delete instance |
| `/api/hosting/instances/{id}/start` | POST | Start instance |
| `/api/hosting/instances/{id}/stop` | POST | Stop instance |
| `/api/hosting/instances/{id}/restart` | POST | Restart instance |
| `/api/hosting/instances/{id}/scale` | POST | Scale instance |
| `/api/hosting/instances/{id}/resources` | PUT | Update resources |

### 6.2 Monitoring

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/instances/{id}/metrics` | GET | Get metrics |
| `/api/hosting/instances/{id}/logs` | GET | Get logs |
| `/api/hosting/instances/{id}/events` | GET | Get events |
| `/api/hosting/instances/{id}/health` | GET | Health check |

### 6.3 Auto-scaling

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/autoscale` | GET | List auto-scale policies |
| `/api/hosting/autoscale` | POST | Create auto-scale policy |
| `/api/hosting/autoscale/{id}` | GET | Get auto-scale policy |
| `/api/hosting/autoscale/{id}` | PUT | Update auto-scale policy |
| `/api/hosting/autoscale/{id}` | DELETE | Delete auto-scale policy |
| `/api/hosting/autoscale/{id}/enable` | POST | Enable policy |
| `/api/hosting/autoscale/{id}/disable` | POST | Disable policy |

### 6.4 Service Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/services` | GET | List services |
| `/api/hosting/services` | POST | Register service |
| `/api/hosting/services/{id}` | GET | Get service |
| `/api/hosting/services/{id}` | DELETE | Unregister service |
| `/api/hosting/services/discover` | GET | Discover service |

### 6.5 Volume Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/volumes` | GET | List volumes |
| `/api/hosting/volumes` | POST | Create volume |
| `/api/hosting/volumes/{id}` | GET | Get volume |
| `/api/hosting/volumes/{id}` | DELETE | Delete volume |
| `/api/hosting/volumes/{id}/mount` | POST | Mount volume |
| `/api/hosting/volumes/{id}/unmount` | POST | Unmount volume |

### 6.6 Cost Management

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/hosting/cost/estimate` | POST | Estimate cost |
| `/api/hosting/instances/{id}/cost` | GET | Get actual cost |

---

## 7. Error Handling

### 7.1 CloudHostingException

```java
public class CloudHostingException extends Exception {
    private String provider;
    private String errorCode;
    private String errorMessage;
    
    public CloudHostingException(String provider, String errorCode, String message) {
        super(message);
        this.provider = provider;
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
```

### 7.2 Error Codes

| Code | Description |
|------|-------------|
| `CreateFailed` | Failed to create instance |
| `InstanceNotFound` | Instance not found |
| `StartFailed` | Failed to start instance |
| `StopFailed` | Failed to stop instance |
| `RestartFailed` | Failed to restart instance |
| `ScaleFailed` | Failed to scale instance |
| `UpdateFailed` | Failed to update instance |
| `DeleteFailed` | Failed to delete instance |
| `LogsFailed` | Failed to get logs |
| `EventsFailed` | Failed to get events |
| `EndpointFailed` | Failed to get endpoint |

---

## 8. Configuration Reference

```yaml
hosting:
  enabled: true
  defaultProvider: kubernetes
  
  kubernetes:
    enabled: true
    kubeconfig: ${KUBECONFIG:}
    masterUrl: ${KUBERNETES_MASTER:}
    namespace: default
    
  tencent:
    enabled: false
    secretId: ${TENCENT_SECRET_ID:}
    secretKey: ${TENCENT_SECRET_KEY:}
    region: ap-guangzhou
    
  defaults:
    resources:
      cpuLimit: 500m
      memoryLimit: 512Mi
    scaling:
      minReplicas: 1
      maxReplicas: 10
      
  monitoring:
    enabled: true
    metricsInterval: 60000
    logRetention: 7d
    
  cost:
    currency: CNY
    billingCycle: hourly
```

---

## 9. Version History

| Version | Date | Changes |
|---------|------|---------|
| v0.7.0 | 2026-02-11 | Initial version |
| v0.7.3 | 2026-02-20 | Added Kubernetes provider, auto-scaling, volume management |

---

**Document Status**: Draft  
**Next Step**: Review with team and finalize API specifications
