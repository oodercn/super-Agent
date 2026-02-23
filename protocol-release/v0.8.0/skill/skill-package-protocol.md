# Skill Package Protocol - v0.7.3

## 1. Overview

The Skill Package Protocol defines the packaging format, distribution mechanism, and lifecycle management for Ooder skills. This protocol enables skills to be discovered, downloaded, installed, and managed in a standardized way.

v0.7.3 adds offline support, event-driven lifecycle management, and enhanced version control.

### 1.1 Design Goals

- **Standardized Packaging**: Define a consistent format for skill packages
- **Easy Distribution**: Enable skill sharing through SkillCenter
- **Lifecycle Management**: Support install, update, and uninstall operations
- **Version Control**: Manage skill versions and dependencies
- **Security**: Ensure skill integrity and authenticity
- **v0.7.3 New**: Offline package management
- **v0.7.3 New**: Event-driven lifecycle

### 1.2 v0.7.3 Upgrades

| Feature | Description |
|---------|-------------|
| OfflineService | Offline package installation support |
| EventBus | Event-driven lifecycle management |
| Cache Enhancement | Offline package cache |
| Dependency Resolution | Enhanced offline dependency handling |

## 2. Skill Package Structure

### 2.1 Package Format

A skill package is a directory structure containing all necessary files:

```
skill-{skill-id}/
├── skill.yaml              # Skill manifest (required)
├── skill.jar               # Compiled skill code (required for Java skills)
├── SKILLS.md               # Skill documentation (required)
├── README.md               # Brief introduction (required)
├── config/
│   ├── config.yaml         # Default configuration
│   └── config-template.yaml # Configuration template
├── lib/                    # Dependencies (optional)
│   └── *.jar
├── static/                 # Static resources (optional)
│   ├── css/
│   ├── js/
│   └── images/
└── scenes/                 # Scene definitions (optional)
    └── {scene-name}.yaml
```

### 2.2 Package Archive

| Format | Extension | Use Case |
|--------|-----------|----------|
| ZIP | .skill | Standard distribution format |
| TAR.GZ | .skill.tar.gz | Unix-friendly format |
| Directory | - | Local development |

### 2.3 Naming Convention

```
skill-{category}-{name}-{version}.skill

Examples:
- skill-org-feishu-0.7.3.skill
- skill-msg-1.0.0.skill
- skill-vfs-2.1.0.skill
```

## 3. Skill Manifest (skill.yaml)

### 3.1 Manifest Structure

```yaml
apiVersion: skill.ooder.net/v1
kind: Skill

metadata:
  id: skill-org-feishu
  name: Feishu Organization Service
  version: 0.7.3
  description: Provides organization data integration with Feishu (Lark)
  author: Ooder Team
  license: Apache-2.0
  homepage: https://github.com/ooder/skill-org-feishu
  repository: https://github.com/ooder/skill-org-feishu.git
  keywords:
    - feishu
    - lark
    - organization
    - enterprise

spec:
  type: enterprise-skill
  
  runtime:
    language: java
    javaVersion: "11"
    framework: spring-boot
    mainClass: net.ooder.skill.org.feishu.FeishuSkillApplication
  
  capabilities:
    - id: org-data-read
      name: Organization Data Read
      description: Read organization structure and member data
      category: data-access
    - id: user-auth
      name: User Authentication
      description: Authenticate users via Feishu
      category: authentication
  
  scenes:
    - name: auth
      description: Authentication scene with organization data support
      capabilities:
        - org-data-read
        - user-auth
      roles:
        - roleId: org-provider
          required: true
          capabilities: [org-data-read, user-auth]
  
  dependencies:
    skills:
      - id: skill-vfs
        version: ">=1.0.0"
        optional: true
    libraries:
      - group: net.ooder
        artifact: ooder-common
        version: "2.0.0"
  
  config:
    required:
      - name: FEISHU_APP_ID
        type: string
        description: Feishu application ID
        secret: false
      - name: FEISHU_APP_SECRET
        type: string
        description: Feishu application secret
        secret: true
    optional:
      - name: FEISHU_API_BASE_URL
        type: string
        default: https://open.feishu.cn/open-apis
        description: Feishu API base URL
  
  endpoints:
    - path: /api/org/tree
      method: GET
      description: Get organization tree
      capability: org-data-read
    - path: /api/org/person/{id}
      method: GET
      description: Get person by ID
      capability: org-data-read
    - path: /api/auth/verify
      method: POST
      description: Verify user authentication
      capability: user-auth
  
  resources:
    cpu: "500m"
    memory: "512Mi"
    storage: "1Gi"
  
  offline:
    enabled: true
    cacheStrategy: "local"
    syncOnReconnect: true
```

### 3.2 Metadata Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| id | string | Yes | Unique skill identifier |
| name | string | Yes | Human-readable name |
| version | string | Yes | Semantic version |
| description | string | Yes | Brief description |
| author | string | No | Author or team name |
| license | string | No | License identifier |
| homepage | string | No | Project homepage URL |
| repository | string | No | Source repository URL |
| keywords | []string | No | Search keywords |

### 3.3 v0.7.3 Offline Configuration

```yaml
spec:
  offline:
    enabled: true
    cacheStrategy: "local"
    syncOnReconnect: true
    maxCacheSize: "100MB"
    cacheTTL: 86400000
    conflictResolution: "last-write"
```

## 4. Skill Types

### 4.1 Enterprise Skill

Enterprise skills provide organization-level capabilities:

```yaml
spec:
  type: enterprise-skill
  
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: true
  
  offline:
    enabled: true
    cacheStrategy: "distributed"
```

Examples:
- skill-org-feishu
- skill-org-dingding
- skill-msg

### 4.2 Tool Skill

Tool skills provide general-purpose utilities:

```yaml
spec:
  type: tool-skill
  
  deployment:
    modes:
      - local-deployed
    singleton: false
  
  offline:
    enabled: true
    cacheStrategy: "local"
```

Examples:
- skill-email-sender
- skill-pdf-converter

### 4.3 Integration Skill

Integration skills connect external systems:

```yaml
spec:
  type: integration-skill
  
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: false
  
  offline:
    enabled: false
```

Examples:
- skill-doc-confluence
- skill-doc-notion

## 5. Package Distribution

### 5.1 SkillCenter Storage

```
skillcenter/
├── index/
│   └── skills-index.json
├── skills/
│   ├── skill-org-feishu/
│   │   ├── metadata.json
│   │   ├── versions/
│   │   │   ├── 0.7.3/
│   │   │   │   ├── skill.yaml
│   │   │   │   ├── skill-org-feishu-0.7.3.skill
│   │   │   │   └── checksum.sha256
│   │   │   └── 0.7.0/
│   │   └── stats.json
│   └── skill-org-dingding/
└── config/
    └── skillcenter-config.yaml
```

### 5.2 Skills Index

```json
{
  "version": "1.0",
  "updated": "2026-02-20T00:00:00Z",
  "skills": [
    {
      "id": "skill-org-feishu",
      "name": "Feishu Organization Service",
      "latestVersion": "0.7.3",
      "type": "enterprise-skill",
      "capabilities": ["org-data-read", "user-auth"],
      "scenes": ["auth"],
      "keywords": ["feishu", "lark", "organization"],
      "offlineSupport": true
    }
  ]
}
```

### 5.3 VFS Integration

```
/vfs/
├── skills/
│   ├── installed/
│   │   └── skill-org-feishu/
│   │       ├── skill.yaml
│   │       ├── skill.jar
│   │       └── config.yaml
│   ├── cache/
│   │   └── skill-org-feishu-0.7.3.skill
│   ├── offline/
│   │   └── skill-org-feishu/
│   │       ├── skill.yaml
│   │       └── skill.jar
│   └── registry.json
├── config/
│   └── skills/
│       └── skill-org-feishu/
│           └── application.yml
└── logs/
    └── skills/
        └── skill-org-feishu/
            └── skill.log
```

## 6. Package Operations

### 6.1 Install Operation

```
1. Query SkillCenter for skill metadata
2. Download skill package to cache
3. Verify checksum and signature
4. Extract to installed directory
5. Merge configuration templates
6. Register skill in local registry
7. Publish SkillInstalledEvent (v0.7.3 new)
8. If enterprise-skill with local-deployed:
   a. Start skill process
   b. Register as RouteAgent
   c. Create scene group
```

### 6.2 Update Operation

```
1. Check for new version in SkillCenter
2. Download new version
3. Backup current configuration
4. Stop current skill (if running)
5. Replace skill files
6. Migrate configuration
7. Start new version
8. Update registry
9. Publish SkillUpdatedEvent (v0.7.3 new)
```

### 6.3 Uninstall Operation

```
1. Stop skill process (if running)
2. Remove from scene groups
3. Unregister from local registry
4. Archive configuration (optional)
5. Remove skill files
6. Publish SkillUninstalledEvent (v0.7.3 new)
```

### 6.4 v0.7.3 Offline Install

```
1. Check local cache for skill package
2. If cached:
   a. Verify checksum
   b. Extract to installed directory
   c. Mark as offline-installed
   d. Register in local registry
   e. Publish SkillInstalledEvent (offline: true)
3. If not cached:
   a. Return error or queue for later
   b. Publish SkillInstallQueuedEvent
```

## 7. Event-Driven Lifecycle (v0.7.3 New)

### 7.1 Lifecycle Events

| Event Type | Description |
|------------|-------------|
| SkillDownloadStartedEvent | Package download started |
| SkillDownloadCompletedEvent | Package download completed |
| SkillInstallStartedEvent | Installation started |
| SkillInstalledEvent | Installation completed |
| SkillUpdateStartedEvent | Update started |
| SkillUpdatedEvent | Update completed |
| SkillUninstallStartedEvent | Uninstall started |
| SkillUninstalledEvent | Uninstall completed |
| SkillCacheHitEvent | Cache hit during offline |
| SkillCacheMissEvent | Cache miss during offline |

### 7.2 Event Subscription

```java
@PostConstruct
public void init() {
    eventBus.subscribe(SkillInstalledEvent.class, this::onSkillInstalled);
    eventBus.subscribe(SkillUpdatedEvent.class, this::onSkillUpdated);
    eventBus.subscribe(SkillUninstalledEvent.class, this::onSkillUninstalled);
}

private void onSkillInstalled(SkillInstalledEvent event) {
    if (event.isOffline()) {
        log.info("Skill {} installed in offline mode", event.getSkillId());
        syncQueue.add(event);
    }
}
```

## 8. Security

### 8.1 Package Signing

Skill packages should be signed with GPG:

```
skill-org-feishu-0.7.3.skill
skill-org-feishu-0.7.3.skill.sig
skill-org-feishu-0.7.3.skill.asc
```

### 8.2 Checksum Verification

```
checksum.sha256:
sha256sum skill-org-feishu-0.7.3.skill > checksum.sha256
```

### 8.3 Configuration Security

Sensitive configurations should:
- Use environment variables
- Support secret management integration
- Never be logged or exposed in API responses

## 9. Version Compatibility

### 9.1 Semantic Versioning

Skills follow semantic versioning (MAJOR.MINOR.PATCH):

- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

### 9.2 Compatibility Matrix

| Skill Version | SDK Version | Protocol Version |
|---------------|-------------|------------------|
| 0.7.3 | 0.7.3 | v0.7.3 |
| 0.7.x | 0.7.2 | v0.7.0 |
| 0.6.x | 0.6.x | v0.6.5 |

### 9.3 v0.7.3 Dependency Resolution

```yaml
dependencies:
  skills:
    - id: skill-vfs
      version: ">=1.0.0"
      optional: true
      offline: true
```

## 10. Offline Package Management (v0.7.3 New)

### 10.1 Offline Cache Configuration

```yaml
package:
  offline:
    enabled: true
    cachePath: ./data/skill-cache
    maxSize: 1GB
    maxAge: 604800000
    autoCleanup: true
```

### 10.2 Offline Install Interface

```java
public interface OfflinePackageService {
    
    boolean isSkillCached(String skillId, String version);
    
    Optional<SkillPackage> getCachedPackage(String skillId, String version);
    
    CompletableFuture<InstallResult> installFromCache(String skillId, String version);
    
    CompletableFuture<SyncResult> syncCache();
}
```

### 10.3 Offline Install Flow

```
Install Request
    │
    ▼
Check Network
    │
    ├── Online ────────────────────────────────┐
    │                                           │
    │   Download from SkillCenter               │
    │   Verify and Install                      │
    │   Cache Package                           │
    │                                           │
    └── Offline ────────────────────────────────┤
                                                │
        Check Cache                             │
        │                                       │
        ├── Cached ────▶ Install from Cache    │
        │                                       │
        └── Not Cached                          │
                │                               │
                ▼                               │
            Queue for Later                     │
                                                │
                ▼                               │
            Network Reconnected ◀───────────────┘
                │
                ▼
            Auto Sync and Install
```

## 11. Driver Proxy Package Support (v0.7.3 New)

### 11.1 Overview

Driver Proxy Package enables lightweight skill integration by separating the interface definition from the implementation.

### 11.2 Interface Definition File

```yaml
apiVersion: skill.ooder.net/v1
kind: InterfaceDefinition

metadata:
  skillId: skill-org-feishu
  version: 0.7.3
  
spec:
  methods:
    - name: getOrgTree
      description: Get organization tree
      parameters:
        - name: rootId
          type: string
          required: false
      returnType: OrgTree
      
    - name: getPersonById
      description: Get person by ID
      parameters:
        - name: personId
          type: string
          required: true
      returnType: Person
      
  events:
    - name: onOrgChanged
      description: Organization structure changed
      payloadType: OrgChangeEvent
```

### 11.3 Install Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| **DRIVER_ONLY** | Install driver proxy only | Local implementation exists |
| **REMOTE_SKILL** | Install remote skill | Skill deployed on remote server |
| **FULL_INSTALL** | Full installation | Standard installation |

### 11.4 Driver Proxy Components

| Component | Description |
|-----------|-------------|
| **DriverLoader** | Load and cache driver proxy packages |
| **InterfaceParser** | Parse YAML/JSON interface definitions |
| **ProxyFactory** | Create dynamic proxies for remote calls |
| **FallbackHandler** | Handle offline fallback implementations |

## 12. Validation & Development Toolchain (v0.7.3 New)

### 12.1 Four-Level Validation

| Level | Description | Checks |
|-------|-------------|--------|
| **Level 1** | Basic | Directory structure, config files, dependencies |
| **Level 2** | Interface | Interface definition, parameter validation, return types |
| **Level 3** | Logic | Business logic, state management, error handling |
| **Level 4** | Integration | Multi-agent collaboration, network, security |

### 12.2 CLI Commands

```bash
java -jar agent-sdk-0.7.3.jar init --name my-scene --path ./my-scene
java -jar agent-sdk-0.7.3.jar generate --type driver --interface ./interface.yaml
java -jar agent-sdk-0.7.3.jar validate --scene ./my-scene --level 4
java -jar agent-sdk-0.7.3.jar test --scene ./my-scene --type unit
java -jar agent-sdk-0.7.3.jar package --scene ./my-scene --output ./skill.zip
```

### 12.3 Code Generation

| Type | Description |
|------|-------------|
| **Driver** | Generate driver proxy code |
| **SkillInterface** | Generate skill interface code |
| **Fallback** | Generate fallback implementation template |

## 13. Error Handling

| Code | Description |
|------|-------------|
| SKILL_001 | Skill not found |
| SKILL_002 | Version not found |
| SKILL_003 | Download failed |
| SKILL_004 | Checksum mismatch |
| SKILL_005 | Signature verification failed |
| SKILL_006 | Installation failed |
| SKILL_007 | Configuration invalid |
| SKILL_008 | Dependency not satisfied |
| SKILL_009 | Uninstall failed |
| SKILL_010 | Update failed |
| SKILL_011 | Offline cache miss (v0.7.3 new) |
| SKILL_012 | Offline dependency missing (v0.7.3 new) |

## 14. References

- [Skill Discovery Protocol](./skill-discovery-protocol.md)
- [Agent Protocol](../agent/agent-protocol.md)
- [P2P Protocol](../p2p/p2p-protocol.md)

## 15. Version History

| Version | Date | Changes |
|---------|------|---------|
| v0.7.0 | 2026-02-11 | Initial version |
| v0.7.3 | 2026-02-20 | Added offline support, event-driven lifecycle, enhanced dependency resolution |
