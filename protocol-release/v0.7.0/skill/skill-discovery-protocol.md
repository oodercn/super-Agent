# Skill Discovery Protocol - v0.7.0

## 1. Overview

The Skill Discovery Protocol defines how skills are discovered, registered, and made available to consumers. This protocol enables a decentralized, self-organizing network of skills that can be found and used without centralized configuration.

### 1.1 Design Goals

- **Zero Configuration**: Skills can be discovered without manual configuration
- **Decentralized**: No single point of failure in skill discovery
- **Real-time**: Changes in skill availability are propagated quickly
- **Secure**: Skills are authenticated before being used

### 1.2 Discovery Methods

| Method | Scope | Latency | Use Case |
|--------|-------|---------|----------|
| UDP Broadcast | Local Network | Low | LAN discovery |
| DHT (Kademlia) | Global | Medium | P2P discovery |
| SkillCenter API | Global | Low | Centralized catalog |
| mDNS/DNS-SD | Local Network | Low | Service discovery |

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
SKILL_REGISTER:agent-001;skill-org-feishu;0.7.0;enterprise-skill;192.168.1.100:8080;org-data-read,user-auth;auth;1707868800000;a1b2c3d4e5f6...
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
```

### 2.3 Registration Acknowledgment

```
SKILL_REGISTER_ACK:{agentId};{status};{message};{timestamp}
```

**Status Values:**

| Status | Description |
|--------|-------------|
| SUCCESS | Registration successful |
| DUPLICATE | Skill already registered |
| INVALID | Invalid registration data |
| UNAUTHORIZED | Authentication failed |

## 3. Skill Discovery

### 3.1 Discovery Request

A consumer can request skill discovery:

```
SKILL_DISCOVER:{requesterId};{capabilityFilter};{sceneFilter};{typeFilter};{timestamp}
```

**Example:**

```
SKILL_DISCOVER:agent-002;org-data-read;auth;enterprise-skill;1707868800000
```

### 3.2 Discovery Response

```
SKILL_DISCOVER_RESPONSE:{requesterId};{skills};{timestamp}
```

**Skills Format:**

```
skillId1|version1|endpoint1|capabilities1|scenes1;skillId2|version2|endpoint2|capabilities2|scenes2
```

**Example:**

```
SKILL_DISCOVER_RESPONSE:agent-002;skill-org-feishu|0.7.0|192.168.1.100:8080|org-data-read,user-auth|auth;skill-org-dingding|0.7.0|192.168.1.101:8080|org-data-read,user-auth|auth;1707868800000
```

### 3.3 Discovery Flow

```
┌─────────────────┐                              ┌─────────────────┐
│   Consumer      │                              │   MCPAgent      │
│   (EndAgent)    │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. SKILL_DISCOVER                             │
         │    (capability: org-data-read)                 │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  2. SKILL_DISCOVER_RESPONSE                    │
         │◀───────────────────────────────────────────────│
         │     (list of matching skills)                  │
         │                                                │
         │  3. JOIN_SCENE (selected skill)                │
         │───────────────────────────────────────────────▶│
         │                                                │
         │  4. JOIN_SCENE_RESPONSE                        │
         │◀───────────────────────────────────────────────│
         │     (connection info)                          │
         │                                                │
```

## 4. Scene-Based Discovery

### 4.1 Scene Request

Instead of discovering individual skills, consumers can request a scene:

```java
// SDK API
SceneJoinResult result = sdk.requestScene("auth", Arrays.asList("org-data-read", "user-auth"));

if (result.isJoined()) {
    String endpoint = result.getConnectionInfo().get("endpoint");
    String apiKey = result.getConnectionInfo().get("apiKey");
    // Use the skill
}
```

### 4.2 Scene Resolution Flow

```
┌─────────────────┐                              ┌─────────────────┐
│   Consumer      │                              │   MCPAgent      │
│                 │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. REQUEST_SCENE                              │
         │    (scene: auth, capabilities: [...])          │
         │───────────────────────────────────────────────▶│
         │                                                │
         │                                ┌───────────────┤
         │                                │ Scene exists? │
         │                                └───────────────┤
         │                                       │         │
         │                              Yes ◄────┘    No   │
         │                                       │         │
         │  2a. SCENE_JOIN_RESPONSE              │    2b. SCENE_NOT_FOUND
         │     (groupId, connectionInfo)         │         │
         │◀──────────────────────────────────────│         │
         │                                       │         │
         │                                       ▼         │
         │                              ┌───────────────┐  │
         │                              │ Query         │  │
         │                              │ SkillCenter   │  │
         │                              └───────────────┘  │
         │                                       │         │
         │                                       ▼         │
         │                              ┌───────────────┐  │
         │                              │ Install Skill │  │
         │                              │ (user choice) │  │
         │                              └───────────────┘  │
         │                                       │         │
         │                                       ▼         │
         │                              ┌───────────────┐  │
         │                              │ Create Scene  │  │
         │                              │ Group         │  │
         │                              └───────────────┘  │
         │                                       │         │
         │  3. SCENE_JOIN_RESPONSE              │         │
         │     (groupId, connectionInfo)         │         │
         │◀──────────────────────────────────────┘         │
         │                                                │
```

### 4.3 Connection Info Resolution

The connection info is dynamically resolved from the scene group:

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
    "version": "0.7.0",
    "capabilities": ["org-data-read", "user-auth"]
  }
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
    broadcastInterval: 5000      # ms
    timeout: 30000               # ms
```

### 5.2 Message Format

All UDP discovery messages follow this format:

```
{MESSAGE_TYPE}:{payload}
```

**Message Types:**

| Type | Direction | Description |
|------|-----------|-------------|
| SKILL_REGISTER | Skill → Network | Register skill availability |
| SKILL_REGISTER_ACK | Network → Skill | Acknowledge registration |
| SKILL_UNREGISTER | Skill → Network | Unregister skill |
| SKILL_DISCOVER | Consumer → Network | Request skill discovery |
| SKILL_DISCOVER_RESPONSE | Network → Consumer | Discovery results |
| SKILL_HEARTBEAT | Skill → Network | Keep-alive signal |
| SKILL_STATUS | Skill → Network | Status update |

### 5.3 Implementation

```java
public class UdpDiscoveryService {
    private static final String MULTICAST_GROUP = "224.0.0.1";
    private static final int PORT = 54321;
    
    public void broadcastRegistration(SkillRegistration registration) {
        String message = String.format("SKILL_REGISTER:%s;%s;%s;%s;%s;%s;%s;%d;%s",
            registration.getAgentId(),
            registration.getSkillId(),
            registration.getVersion(),
            registration.getSkillType(),
            registration.getEndpoint(),
            String.join(",", registration.getCapabilities()),
            String.join(",", registration.getScenes()),
            System.currentTimeMillis(),
            registration.getSignature()
        );
        
        sendMulticast(message);
    }
    
    public List<SkillInfo> discoverSkills(DiscoveryFilter filter) {
        String request = String.format("SKILL_DISCOVER:%s;%s;%s;%s;%d",
            getAgentId(),
            filter.getCapabilities(),
            filter.getScenes(),
            filter.getTypes(),
            System.currentTimeMillis()
        );
        
        sendMulticast(request);
        return waitForResponses(filter.getTimeout());
    }
}
```

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
│        │           │           │           │           │           │
│        └───────────┴───────────┴───────────┴───────────┘           │
│                              │                                      │
│                    Skill Registry (DHT)                             │
│                                                                     │
│     Key: SHA256(skillId + capability)                               │
│     Value: {endpoint, version, timestamp}                           │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Skill Key Generation

```java
public String generateSkillKey(String skillId, String capability) {
    String input = skillId + ":" + capability;
    return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
}
```

### 6.3 DHT Operations

```java
public interface DhtClient {
    // Store skill registration
    void put(String key, SkillRegistration value);
    
    // Find skill by key
    Optional<SkillRegistration> get(String key);
    
    // Find skills by capability
    List<SkillRegistration> findByCapability(String capability);
    
    // Remove skill registration
    void remove(String key);
}
```

### 6.4 Bootstrap Nodes

```yaml
dht:
  bootstrapNodes:
    - host: dht1.ooder.net
      port: 6881
    - host: dht2.ooder.net
      port: 6881
    - host: dht3.ooder.net
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
  "version": ">=0.7.0"
}
```

### 7.3 Search Response

```json
{
  "total": 2,
  "skills": [
    {
      "id": "skill-org-feishu",
      "name": "Feishu Organization Service",
      "version": "0.7.0",
      "type": "enterprise-skill",
      "capabilities": ["org-data-read", "user-auth"],
      "scenes": ["auth"],
      "endpoint": "https://skillcenter.ooder.net/skills/skill-org-feishu",
      "downloadUrl": "https://skillcenter.ooder.net/api/skills/skill-org-feishu/download"
    },
    {
      "id": "skill-org-dingding",
      "name": "Dingding Organization Service",
      "version": "0.7.0",
      "type": "enterprise-skill",
      "capabilities": ["org-data-read", "user-auth"],
      "scenes": ["auth"],
      "endpoint": "https://skillcenter.ooder.net/skills/skill-org-dingding",
      "downloadUrl": "https://skillcenter.ooder.net/api/skills/skill-org-dingding/download"
    }
  ]
}
```

## 8. Heartbeat and Health

### 8.1 Heartbeat Message

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

### 8.2 Health Check Configuration

```yaml
healthCheck:
  heartbeatInterval: 5000         # ms
  timeout: 30000                  # ms
  retryCount: 3
  unhealthyThreshold: 3           # Missed heartbeats before marking unhealthy
```

### 8.3 Health Check Flow

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│                 │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  HEARTBEAT (every 5s)                          │
         │───────────────────────────────────────────────▶│
         │                                                │
         │                        ┌───────────────────────┤
         │                        │ Update health status  │
         │                        └───────────────────────┤
         │                                                │
         │  (if missed 3 heartbeats)                      │
         │                                ┌───────────────┤
         │                                │ Mark UNHEALTHY│
         │                                └───────────────┤
         │                                                │
         │                                ┌───────────────┤
         │                                │ Notify group  │
         │                                │ members       │
         │                                └───────────────┤
         │                                                │
```

## 9. Skill Unregistration

### 9.1 Unregistration Message

```
SKILL_UNREGISTER:{agentId};{skillId};{reason};{timestamp};{signature}
```

**Reason Values:**

| Reason | Description |
|--------|-------------|
| SHUTDOWN | Normal shutdown |
| ERROR | Error-induced shutdown |
| MAINTENANCE | Temporary maintenance |
| UPGRADE | Upgrading to new version |

### 9.2 Unregistration Flow

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│                 │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. SKILL_UNREGISTER                           │
         │───────────────────────────────────────────────▶│
         │                                                │
         │                                ┌───────────────┤
         │                                │ Remove from   │
         │                                │ registry      │
         │                                └───────────────┤
         │                                                │
         │                                ┌───────────────┤
         │                                │ Notify scene  │
         │                                │ group members │
         │                                └───────────────┤
         │                                                │
         │  2. SKILL_UNREGISTER_ACK                       │
         │◀───────────────────────────────────────────────│
         │                                                │
```

## 10. Security

### 10.1 Message Signing

All discovery messages must be signed:

```java
public class DiscoveryMessageSigner {
    
    public String sign(String message, PrivateKey privateKey) {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }
    
    public boolean verify(String message, String signature, PublicKey publicKey) {
        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        return sig.verify(Base64.getDecoder().decode(signature));
    }
}
```

### 10.2 Certificate Chain

```
Root CA (ooder.net)
    │
    └── SkillCenter CA
            │
            ├── Skill Certificate (skill-org-feishu)
            └── Agent Certificate (agent-001)
```

### 10.3 Authentication Flow

```
┌─────────────────┐                              ┌─────────────────┐
│    Skill        │                              │   MCPAgent      │
│                 │                              │                 │
└─────────────────┘                              └─────────────────┘
         │                                                │
         │  1. SKILL_REGISTER (with certificate)          │
         │───────────────────────────────────────────────▶│
         │                                                │
         │                                ┌───────────────┤
         │                                │ Verify        │
         │                                │ certificate   │
         │                                └───────────────┤
         │                                                │
         │  2. CHALLENGE (nonce)                          │
         │◀───────────────────────────────────────────────│
         │                                                │
         │  3. CHALLENGE_RESPONSE (signed nonce)          │
         │───────────────────────────────────────────────▶│
         │                                                │
         │                                ┌───────────────┤
         │                                │ Verify        │
         │                                │ signature     │
         │                                └───────────────┤
         │                                                │
         │  4. SKILL_REGISTER_ACK                         │
         │◀───────────────────────────────────────────────│
         │                                                │
```

## 11. Caching and Refresh

### 11.1 Local Cache

Skills are cached locally for performance:

```yaml
cache:
  enabled: true
  ttl: 300000                    # 5 minutes
  maxSize: 1000
  refreshBeforeExpiry: 60000     # Refresh 1 minute before expiry
```

### 11.2 Cache Invalidation

Cache is invalidated when:
- TTL expires
- Skill unregisters
- Skill health status changes
- Manual refresh requested

### 11.3 Cache Structure

```json
{
  "skills": {
    "skill-org-feishu": {
      "endpoint": "192.168.1.100:8080",
      "version": "0.7.0",
      "capabilities": ["org-data-read", "user-auth"],
      "scenes": ["auth"],
      "status": "HEALTHY",
      "lastUpdated": 1707868800000,
      "expiresAt": 1707869100000
    }
  }
}
```

## 12. Error Handling

### 12.1 Error Codes

| Code | Description | Recovery Action |
|------|-------------|-----------------|
| DISC_001 | Discovery timeout | Retry with exponential backoff |
| DISC_002 | No skills found | Query SkillCenter |
| DISC_003 | Registration failed | Check credentials and retry |
| DISC_004 | Verification failed | Check certificate chain |
| DISC_005 | Network error | Use cached data if available |
| DISC_006 | Skill unavailable | Find alternative skill |

### 12.2 Fallback Strategy

```
1. Try UDP broadcast discovery
   ↓ (failed)
2. Try DHT discovery
   ↓ (failed)
3. Try SkillCenter API
   ↓ (failed)
4. Use cached data (if available and not expired)
   ↓ (failed)
5. Return error to user
```

## 13. Appendix

### 13.1 Message Examples

**Registration:**
```
SKILL_REGISTER:agent-001;skill-org-feishu;0.7.0;enterprise-skill;192.168.1.100:8080;org-data-read,user-auth;auth;1707868800000;MEUCIQDxxx...
```

**Discovery:**
```
SKILL_DISCOVER:agent-002;org-data-read;auth;enterprise-skill;1707868800000
```

**Heartbeat:**
```
SKILL_HEARTBEAT:agent-001;skill-org-feishu;HEALTHY;1707868805000;MEUCIQDxxx...
```

### 13.2 Configuration Reference

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
  
  healthCheck:
    heartbeatInterval: 5000
    timeout: 30000
    retryCount: 3
    unhealthyThreshold: 3
```
