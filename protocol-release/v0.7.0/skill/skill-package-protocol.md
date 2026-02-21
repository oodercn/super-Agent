# Skill Package Protocol - v0.7.0

## 1. Overview

The Skill Package Protocol defines the packaging format, distribution mechanism, and lifecycle management for Ooder skills. This protocol enables skills to be discovered, downloaded, installed, and managed in a standardized way.

### 1.1 Design Goals

- **Standardized Packaging**: Define a consistent format for skill packages
- **Easy Distribution**: Enable skill sharing through SkillCenter
- **Lifecycle Management**: Support install, update, and uninstall operations
- **Version Control**: Manage skill versions and dependencies
- **Security**: Ensure skill integrity and authenticity

### 1.2 Scope

This protocol covers:
- Skill package format and structure
- Skill manifest specification
- Package distribution and synchronization
- Installation and configuration management

## 2. Skill Package Structure

### 2.1 Package Format

A skill package is a directory structure containing all necessary files for a skill to run:

```
skill-{skill-id}/
├── skill.yaml              # Skill manifest (required)
├── skill.jar               # Compiled skill code (required for Java skills)
├── SKILLS.md               # Skill documentation (required)
├── README.md               # Brief introduction (required)
├── config/
│   ├── config.yaml         # Default configuration
│   └── config-template.yaml # Configuration template with placeholders
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

For distribution, a skill package can be archived as:

| Format | Extension | Use Case |
|--------|-----------|----------|
| ZIP | .skill | Standard distribution format |
| TAR.GZ | .skill.tar.gz | Unix-friendly format |
| Directory | - | Local development |

### 2.3 Naming Convention

```
skill-{category}-{name}-{version}.skill

Examples:
- skill-org-feishu-0.7.0.skill
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
  version: 0.7.0
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
  type: enterprise-skill          # enterprise-skill | tool-skill | integration-skill
  
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
    skills:                        # Dependencies on other skills
      - id: skill-vfs
        version: ">=1.0.0"
        optional: true
    libraries:                     # External library dependencies
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

### 3.3 Spec Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| type | string | Yes | Skill type: enterprise-skill, tool-skill, integration-skill |
| runtime | object | Yes | Runtime configuration |
| capabilities | []object | Yes | List of capabilities provided |
| scenes | []object | No | Scene definitions |
| dependencies | object | No | Skill and library dependencies |
| config | object | No | Configuration requirements |
| endpoints | []object | No | API endpoints |
| resources | object | No | Resource requirements |

## 4. Skill Types

### 4.1 Enterprise Skill

Enterprise skills provide organization-level capabilities that are typically unique within an organization:

```yaml
spec:
  type: enterprise-skill
  
  deployment:
    modes:
      - remote-hosted          # Hosted on SkillCenter
      - local-deployed         # Deployed locally
    singleton: true            # Only one instance per organization
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
    singleton: false           # Multiple instances allowed
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
```

Examples:
- skill-doc-confluence
- skill-doc-notion

## 5. Package Distribution

### 5.1 SkillCenter Storage

Skills are stored in SkillCenter with the following structure:

```
skillcenter/
├── index/
│   └── skills-index.json      # Skill catalog index
├── skills/
│   ├── skill-org-feishu/
│   │   ├── metadata.json      # Skill metadata
│   │   ├── versions/
│   │   │   ├── 0.7.0/
│   │   │   │   ├── skill.yaml
│   │   │   │   ├── skill-org-feishu-0.7.0.skill
│   │   │   │   └── checksum.sha256
│   │   │   └── 0.6.0/
│   │   └── stats.json         # Download statistics
│   └── skill-org-dingding/
└── config/
    └── skillcenter-config.yaml
```

### 5.2 Skills Index

```json
{
  "version": "1.0",
  "updated": "2026-02-14T00:00:00Z",
  "skills": [
    {
      "id": "skill-org-feishu",
      "name": "Feishu Organization Service",
      "latestVersion": "0.7.0",
      "type": "enterprise-skill",
      "capabilities": ["org-data-read", "user-auth"],
      "scenes": ["auth"],
      "keywords": ["feishu", "lark", "organization"]
    }
  ]
}
```

### 5.3 VFS Integration

Installed skills are stored in VFS:

```
/vfs/
├── skills/
│   ├── installed/
│   │   └── skill-org-feishu/
│   │       ├── skill.yaml
│   │       ├── skill.jar
│   │       └── config.yaml
│   ├── cache/
│   │   └── skill-org-feishu-0.7.0.skill
│   └── registry.json          # Local skill registry
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
7. If enterprise-skill with local-deployed:
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
```

### 6.3 Uninstall Operation

```
1. Stop skill process (if running)
2. Remove from scene groups
3. Unregister from local registry
4. Archive configuration (optional)
5. Remove skill files
```

## 7. Security

### 7.1 Package Signing

Skill packages should be signed with GPG:

```
skill-org-feishu-0.7.0.skill
skill-org-feishu-0.7.0.skill.sig
skill-org-feishu-0.7.0.skill.asc
```

### 7.2 Checksum Verification

```
checksum.sha256:
```
sha256sum skill-org-feishu-0.7.0.skill > checksum.sha256
```
```

### 7.3 Configuration Security

Sensitive configurations should:
- Use environment variables
- Support secret management integration
- Never be logged or exposed in API responses

## 8. Version Compatibility

### 8.1 Semantic Versioning

Skills follow semantic versioning (MAJOR.MINOR.PATCH):

- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes, backward compatible

### 8.2 Compatibility Matrix

| Skill Version | SDK Version | Protocol Version |
|---------------|-------------|------------------|
| 0.7.x | 0.7.0 | v0.7.0 |
| 0.6.x | 0.6.x | v0.6.5 |

## 9. Error Handling

### 9.1 Error Codes

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

## 10. Appendix

### 10.1 Example skill.yaml

See: [skill-manifest-spec.md](./skill-manifest-spec.md)

### 10.2 Example SKILLS.md

See: [../../docs/skills/SKILLS-README.md](../../docs/skills/SKILLS-README.md)

### 10.3 References

- [Skill Protocol](./skill-protocol.md)
- [Agent Protocol](../agent/agent-protocol.md)
- [P2P Protocol](../p2p/p2p-protocol.md)
