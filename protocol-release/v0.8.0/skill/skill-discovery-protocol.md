# Skill Discovery Protocol - v0.7.3

## 1. Overview

The Skill Discovery Protocol defines how skills are discovered, registered, and made available to consumers. This protocol enables a decentralized, self-organizing network of skills that can be found and used without centralized configuration.

v0.7.3 adds offline support, event bus integration, and enhanced discovery methods.

### 1.1 Design Goals

- **Zero Configuration**: Skills can be discovered without manual configuration
- **Decentralized**: No single point of failure in skill discovery
- **Real-time**: Changes in skill availability are propagated quickly
- **Secure**: Skills are authenticated before being used
- **v0.7.3 New**: Offline mode support
- **v0.7.3 New**: Event-driven discovery

### 1.2 v0.7.3 Upgrades

| Feature | Description |
|---------|-------------|
| DiscoveryProtocol | Multi-path discovery (UDP/DHT/SkillCenter/mDNS) |
| OfflineService | Offline skill discovery support |
| EventBus | Event-driven discovery notifications |
| Cache Enhancement | Offline skill cache |

### 1.3 Discovery Methods

| Method | Code | Scope | Latency | Use Case |
|--------|------|-------|---------|----------|
| UDP Broadcast | `UDP_BROADCAST` | Local Network | Low | LAN discovery |
| DHT (Kademlia) | `DHT_KADEMLIA` | Global | Medium | P2P discovery |
| SkillCenter API | `SKILL_CENTER` | Global | Low | Centralized catalog |
| mDNS/DNS-SD | `MDNS_DNS_SD` | Local Network | Low | Service discovery |
| GitHub | `GITHUB` | Global | Medium | GitHub repository discovery |
| Gitee | `GITEE` | Global | Medium | Gitee repository discovery |
| Git Repository | `GIT_REPOSITORY` | Global | Medium | Generic Git repository discovery |
| Local Filesystem | `LOCAL_FS` | Local | Very Low | Local development |
| Auto Detect | `AUTO` | - | - | Auto detect discovery method |

### 1.4 Discovery Method Enum

```java
public enum DiscoveryMethod {
    UDP_BROADCAST("udp_broadcast", "UDP Broadcast discovery"),
    DHT_KADEMLIA("dht_kademlia", "DHT/Kademlia discovery"),
    MDNS_DNS_SD("mdns_dns_sd", "mDNS/DNS-SD discovery"),
    SKILL_CENTER("skill_center", "SkillCenter API discovery"),
    LOCAL_FS("local_fs", "Local filesystem discovery"),
    GITHUB("github", "GitHub repository discovery"),
    GITEE("gitee", "Gitee repository discovery"),
    GIT_REPOSITORY("git_repository", "Git repository discovery (generic)"),
    AUTO("auto", "Auto detect discovery method");
}
```

## 2. Skill Registration

### 2.1 Registration Message

When a skill starts, it broadcasts a registration message:

```
SKILL_REGISTER:{agentId};{skillId};{version};{skillType};{endpoint};{capabilities};{scenes};{timestamp};{signature}
```

**Field Description:**

| Field | Type | Description |
|-------|------|-------------|
| agentId | string | Unique agent identifier |
| skillId | string | Skill identifier (e.g., skill-org-feishu) |
| version | string | Skill version (semantic versioning) |
| skillType | string | enterprise-skill, tool-skill, integration-skill |
| endpoint | string | Service endpoint (host:port) |
| capabilities | string | Comma-separated capability IDs |
| scenes | string | Comma-separated scene names |
| timestamp | long | Registration timestamp (epoch milliseconds) |
| signature | string | HMAC signature for verification |

**Example:**

```
SKILL_REGISTER:agent-001;skill-org-feishu;0.7.3;enterprise-skill;192.168.1.100:8080;org-data-read,user-auth;auth;1707868800000;a1b2c3d4e5f6...
```

### 2.2 Registration Flow

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│  (RouteAgent)   │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. SKILL_REGISTER                             │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  2. SKILL_REGISTER_ACK                         │
         │◀───────────────────────────────────────────────│
         │                                                │
         │  3. JOIN_SCENE (scene: auth)                   │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  4. JOIN_SCENE_RESPONSE                        │
         │◀───────────────────────────────────────────────│
         │     (sceneId, groupId, members)                │
         │                                                │
         │  5. HEARTBEAT (periodic)                       │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  6. SkillRegisteredEvent (v0.7.3)              │
         │◀───────────────────────────────────────────────│
         │                                                │
```

## 3. Skill Discovery

### 3.1 Discovery Request

A consumer can request skill discovery:

```
SKILL_DISCOVER:{requesterId};{capabilityFilter};{sceneFilter};{typeFilter};{timestamp}
```

### 3.2 Discovery Response

```
SKILL_DISCOVER_RESPONSE:{requesterId};{skills};{timestamp}
```

**Skills Format:**

```
skillId1|version1|endpoint1|capabilities1|scenes1;skillId2|version2|endpoint2|capabilities2|scenes2
```

### 3.3 v0.7.3 Discovery Protocol Interface

```java
public interface DiscoveryProtocol {
    
    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);
    
    CompletableFuture<List<PeerInfo>> discoverPeers();
    
    CompletableFuture<PeerInfo> discoverMcp();
    
    void addDiscoveryListener(DiscoveryListener listener);
    
    void removeDiscoveryListener(DiscoveryListener listener);
    
    void startBroadcast();
    
    void stopBroadcast();
    
    boolean isBroadcasting();
}
```

### 3.4 DiscoveryType Enum

```java
public enum DiscoveryType {
    LOCAL,
    LAN,
    WAN,
    ALL
}
```

### 3.5 DiscoveryRequest Model

```java
public class DiscoveryRequest {
    private String requestId;
    private DiscoveryType type;
    private int timeout;
    private String targetNetwork;
}
```

### 3.6 SkillPackageManager Interface

```java
public interface SkillPackageManager {
    
    CompletableFuture<SkillPackage> discover(String skillId, DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> discoverAll(DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId, DiscoveryMethod method);
    
    CompletableFuture<InstallResult> install(InstallRequest request);
    
    CompletableFuture<UninstallResult> uninstall(String skillId);
    
    CompletableFuture<UpdateResult> update(String skillId, String version);
    
    CompletableFuture<List<InstalledSkill>> listInstalled();
    
    CompletableFuture<InstalledSkill> getInstalled(String skillId);
    
    CompletableFuture<Boolean> isInstalled(String skillId);
    
    CompletableFuture<List<SkillPackage>> search(String query, DiscoveryMethod method);
    
    CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId, DiscoveryMethod method);
    
    String getSkillRootPath();
    
    void setSkillRootPath(String path);
}
```

## 4. Scene-Based Discovery

### 4.1 Scene Request

Instead of discovering individual skills, consumers can request a scene:

```java
SceneJoinResult result = sdk.requestScene("auth", Arrays.asList("org-data-read", "user-auth"));

if (result.isJoined()) {
    String endpoint = result.getConnectionInfo().get("endpoint");
    String apiKey = result.getConnectionInfo().get("apiKey");
}
```

### 4.2 Connection Info Resolution

```json
{
  "sceneId": "auth-001",
  "groupId": "group-auth-001",
  "connectionInfo": {
    "protocol": "http",
    "host": "192.168.1.100",
    "port": 8080,
    "basePath": "/api",
    "authType": "api-key",
    "authHeader": "X-API-Key",
    "apiKey": "sk-xxxxx"
  },
  "skillInfo": {
    "skillId": "skill-org-feishu",
    "version": "0.7.3",
    "capabilities": ["org-data-read", "user-auth"]
  },
  "offline": false
}
```

## 5. UDP Broadcast Discovery

### 5.1 Configuration

```yaml
discovery:
  udp:
    enabled: true
    multicastGroup: 224.0.0.1
    port: 54321
    broadcastInterval: 5000
    timeout: 30000
```

### 5.2 Message Types

| Type | Direction | Description |
|------|-----------|-------------|
| SKILL_REGISTER | Skill → Network | Register skill availability |
| SKILL_REGISTER_ACK | Network → Skill | Acknowledge registration |
| SKILL_UNREGISTER | Skill → Network | Unregister skill |
| SKILL_DISCOVER | Consumer → Network | Request skill discovery |
| SKILL_DISCOVER_RESPONSE | Network → Consumer | Discovery results |
| SKILL_HEARTBEAT | Skill → Network | Keep-alive signal |

## 6. DHT Discovery (Kademlia)

### 6.1 Overview

For wide-area network discovery, we use Kademlia DHT:

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Kademlia DHT                                │
│                                                                     │
│     ┌─────┐     ┌─────┐     ┌─────┐     ┌─────┐     ┌─────┐       │
│     │Node1│─────│Node2│─────│Node3│─────│Node4│─────│Node5│       │
│     └─────┘     └─────┘     └─────┘     └─────┘     └─────┘       │
│                              │                                      │
│                    Skill Registry (DHT)                             │
│                                                                     │
│     Key: SHA256(skillId + capability)                               │
│     Value: {endpoint, version, timestamp}                           │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Bootstrap Nodes

```yaml
dht:
  bootstrapNodes:
    - host: dht1.ooder.net
      port: 6881
    - host: dht2.ooder.net
      port: 6881
```

## 7. SkillCenter API Discovery

### 7.1 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/skills` | GET | List all skills |
| `/api/skills/{id}` | GET | Get skill details |
| `/api/skills/search` | POST | Search skills by criteria |
| `/api/skills/{id}/download` | GET | Download skill package |

### 7.2 Search Request

```json
POST /api/skills/search
{
  "capabilities": ["org-data-read", "user-auth"],
  "scenes": ["auth"],
  "types": ["enterprise-skill"],
  "keywords": ["feishu"],
  "version": ">=0.7.3"
}
```

## 8. GitHub/Gitee Repository Discovery (v0.7.3 New)

### 8.1 Overview

GitHub and Gitee repository discovery allows skills to be discovered and installed directly from Git repositories.

### 8.2 Configuration

```yaml
discovery:
  github:
    enabled: true
    defaultOwner: ooderCN
    defaultRepo: skills
    defaultBranch: main
    token: ${GITHUB_TOKEN}
    baseUrl: https://api.github.com
    timeout: 60000
    
  gitee:
    enabled: true
    defaultOwner: ooderCN
    defaultRepo: skills
    defaultBranch: main
    token: ${GITEE_TOKEN}
    baseUrl: https://gitee.com/api/v5
    timeout: 60000
```

### 8.3 Skill Index File

The repository must contain a `skill-index.yaml` file at the root:

```yaml
apiVersion: ooder.io/v1
kind: SkillIndex

metadata:
  name: ooder-skills
  version: 0.7.3
  updatedAt: 2026-02-20T00:00:00Z

skills:
  - id: skill-org-feishu
    name: Feishu Organization Service
    version: 0.7.3
    description: Feishu organization sync and user authentication
    path: skills/skill-org-feishu
    manifest: skill-manifest.yaml
    scenes:
      - auth
    capabilities:
      - org-data-read
      - user-auth
    downloadUrl: https://github.com/ooderCN/skills/releases/download/v0.7.3/skill-org-feishu-0.7.3.jar
```

## 9. Offline Discovery (v0.7.3 New)

### 9.1 Offline Cache

```yaml
discovery:
  offline:
    enabled: true
    cachePath: ./data/skill-cache
    maxAge: 86400000
    maxSkills: 100
```

### 9.2 Offline Discovery Flow

```
Network Disconnected
    │
    ▼
Enable Offline Mode
    │
    ├── Use cached skill registry
    ├── Return cached skills
    └── Publish OfflineModeEnabledEvent
    │
    ▼
Network Connected
    │
    ▼
Sync Skill Registry
    │
    ├── Fetch updated skills
    ├── Update local cache
    └── Publish SyncCompletedEvent
```

### 9.3 Offline Discovery Interface

```java
public interface OfflineDiscoveryService {
    
    List<SkillInfo> getCachedSkills();
    
    Optional<SkillInfo> getCachedSkill(String skillId);
    
    boolean isSkillCached(String skillId);
    
    CompletableFuture<SyncResult> syncNow();
}
```

## 10. Event-Driven Discovery (v0.7.3 New)

### 10.1 Discovery Events

| Event Type | Description |
|------------|-------------|
| SkillRegisteredEvent | Skill registered successfully |
| SkillUnregisteredEvent | Skill unregistered |
| SkillDiscoveredEvent | New skill discovered |
| SkillAvailableEvent | Skill becomes available |
| SkillUnavailableEvent | Skill becomes unavailable |
| DiscoveryCompletedEvent | Discovery process completed |

### 10.2 Event Subscription

```java
@PostConstruct
public void init() {
    eventBus.subscribe(SkillRegisteredEvent.class, this::onSkillRegistered);
    eventBus.subscribe(SkillDiscoveredEvent.class, this::onSkillDiscovered);
    eventBus.subscribe(SkillAvailableEvent.class, this::onSkillAvailable);
}
```

## 11. Heartbeat and Health

### 11.1 Heartbeat Message

```
SKILL_HEARTBEAT:{agentId};{skillId};{status};{timestamp};{signature}
```

**Status Values:**

| Status | Description |
|--------|-------------|
| HEALTHY | Skill is healthy and available |
| DEGRADED | Skill is running but degraded |
| UNHEALTHY | Skill is unhealthy |
| MAINTENANCE | Skill is under maintenance |
| OFFLINE | Skill is in offline mode (v0.7.3 new) |

### 11.2 Health Check Configuration

```yaml
healthCheck:
  heartbeatInterval: 5000
  timeout: 30000
  retryCount: 3
  unhealthyThreshold: 3
  offlineThreshold: 5
```

## 12. Security

### 12.1 Message Signing

All discovery messages must be signed using SHA256withECDSA.

### 12.2 Certificate Chain

```
Root CA (ooder.net)
    │
    └── SkillCenter CA
            │
            ├── Skill Certificate (skill-org-feishu)
            └── Agent Certificate (agent-001)
```

## 13. Caching and Refresh

### 13.1 Local Cache

```yaml
cache:
  enabled: true
  ttl: 300000
  maxSize: 1000
  refreshBeforeExpiry: 60000
```

### 13.2 Cache Invalidation

Cache is invalidated when:
- TTL expires
- Skill unregisters
- Skill health status changes
- Manual refresh requested
- Offline mode changes (v0.7.3 new)

## 14. Error Handling

| Code | Description | Recovery Action |
|------|-------------|-----------------|
| DISC_001 | Discovery timeout | Retry with exponential backoff |
| DISC_002 | No skills found | Query SkillCenter |
| DISC_003 | Registration failed | Check credentials and retry |
| DISC_004 | Verification failed | Check certificate chain |
| DISC_005 | Network error | Use cached data if available |
| DISC_006 | Skill unavailable | Find alternative skill |
| DISC_007 | Offline mode limit | Use cached skills |

## 15. Configuration Reference

```yaml
discovery:
  udp:
    enabled: true
    multicastGroup: 224.0.0.1
    port: 54321
    broadcastInterval: 5000
    timeout: 30000
  
  dht:
    enabled: true
    bootstrapNodes:
      - host: dht1.ooder.net
        port: 6881
    replicationFactor: 3
  
  skillCenter:
    enabled: true
    url: https://skillcenter.ooder.net
    timeout: 10000
    cache:
      enabled: true
      ttl: 300000
  
  mdns:
    enabled: true
    serviceName: _ooder-skill._tcp
  
  github:
    enabled: true
    defaultOwner: ooderCN
    defaultRepo: skills
    defaultBranch: main
    token: ${GITHUB_TOKEN}
    baseUrl: https://api.github.com
    timeout: 60000
  
  gitee:
    enabled: true
    defaultOwner: ooderCN
    defaultRepo: skills
    defaultBranch: main
    token: ${GITEE_TOKEN}
    baseUrl: https://gitee.com/api/v5
    timeout: 60000
  
  offline:
    enabled: true
    cachePath: ./data/skill-cache
    maxAge: 86400000
    maxSkills: 100
  
  healthCheck:
    heartbeatInterval: 5000
    timeout: 30000
    retryCount: 3
    unhealthyThreshold: 3
    offlineThreshold: 5
```

## 16. Version History

| Version | Date | Changes |
|---------|------|---------|
| v0.7.0 | 2026-02-11 | Initial version |
| v0.7.3 | 2026-02-20 | Added offline support, event bus, mDNS discovery, GitHub/Gitee repository discovery |
