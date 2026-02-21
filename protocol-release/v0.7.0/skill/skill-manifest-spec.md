# Skill Manifest Specification - v0.7.0

## 1. Overview

The Skill Manifest (`skill.yaml`) is the core metadata file that describes a skill's identity, capabilities, dependencies, and configuration requirements. This specification defines the schema and semantics of the skill manifest.

## 2. Schema Definition

### 2.1 Root Structure

```yaml
apiVersion: skill.ooder.net/v1    # API version (required)
kind: Skill                        # Resource kind (required)

metadata:                          # Metadata section (required)
  # ... see Metadata section

spec:                              # Specification section (required)
  # ... see Spec section
```

### 2.2 Metadata Section

```yaml
metadata:
  # Required fields
  id: string                       # Unique identifier (e.g., skill-org-feishu)
  name: string                     # Human-readable name
  version: string                  # Semantic version (e.g., 0.7.0)
  description: string              # Brief description (max 500 chars)
  
  # Optional fields
  author: string                   # Author or team name
  license: string                  # SPDX license identifier
  homepage: string                 # Project homepage URL
  repository: string               # Source repository URL
  documentation: string            # Documentation URL
  icon: string                     # Icon URL (SVG or PNG)
  keywords:                        # Search keywords
    - string
  maintainers:                     # Maintainer list
    - name: string
      email: string
      github: string
```

### 2.3 Spec Section

```yaml
spec:
  # Required fields
  type: string                     # Skill type
  runtime:                         # Runtime configuration
    # ... see Runtime section
  capabilities:                    # Capabilities provided
    # ... see Capabilities section
  
  # Optional fields
  scenes:                          # Scene definitions
    # ... see Scenes section
  dependencies:                    # Dependencies
    # ... see Dependencies section
  config:                          # Configuration requirements
    # ... see Config section
  endpoints:                       # API endpoints
    # ... see Endpoints section
  resources:                       # Resource requirements
    # ... see Resources section
  deployment:                      # Deployment options
    # ... see Deployment section
```

## 3. Skill Types

### 3.1 Type Values

| Type | Description | Singleton | Example |
|------|-------------|-----------|---------|
| `enterprise-skill` | Organization-level capability | Yes | skill-org-feishu |
| `tool-skill` | General-purpose utility | No | skill-pdf-converter |
| `integration-skill` | External system connector | No | skill-doc-confluence |
| `infrastructure-skill` | Core infrastructure | Yes | skill-vfs |

### 3.2 Type Characteristics

```yaml
# Enterprise Skill
spec:
  type: enterprise-skill
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: true              # Only one per organization
    requiresAuth: true           # Requires authentication

# Tool Skill
spec:
  type: tool-skill
  deployment:
    modes:
      - local-deployed
    singleton: false
    requiresAuth: false

# Integration Skill
spec:
  type: integration-skill
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: false
    requiresAuth: true

# Infrastructure Skill
spec:
  type: infrastructure-skill
  deployment:
    modes:
      - local-deployed
    singleton: true
    requiresAuth: true
```

## 4. Runtime Configuration

### 4.1 Java Runtime

```yaml
spec:
  runtime:
    language: java
    javaVersion: "11"            # Java version (8, 11, 17, 21)
    framework: spring-boot       # Framework (spring-boot, quarkus, micronaut)
    mainClass: net.ooder.skill.org.feishu.FeishuSkillApplication
    jvmOpts: "-Xms256m -Xmx512m" # JVM options (optional)
```

### 4.2 Python Runtime

```yaml
spec:
  runtime:
    language: python
    pythonVersion: "3.11"
    framework: fastapi           # Framework (fastapi, flask, django)
    entrypoint: main:app
    requirements: requirements.txt
```

### 4.3 Node.js Runtime

```yaml
spec:
  runtime:
    language: nodejs
    nodeVersion: "20"
    framework: express           # Framework (express, nestjs, fastify)
    entrypoint: dist/index.js
    packageManager: npm          # npm, yarn, pnpm
```

## 5. Capabilities

### 5.1 Capability Definition

```yaml
spec:
  capabilities:
    - id: string                 # Unique capability ID (required)
      name: string               # Human-readable name (required)
      description: string        # Description (required)
      category: string           # Category (optional)
      version: string            # Capability version (optional)
      parameters:                # Input parameters (optional)
        - name: string
          type: string           # string, number, boolean, object, array
          required: boolean
          default: any
          description: string
      returns:                   # Return type (optional)
        type: string
        description: string
      examples:                  # Usage examples (optional)
        - description: string
          input: object
          output: object
```

### 5.2 Capability Categories

| Category | Description | Examples |
|----------|-------------|----------|
| `data-access` | Data read/write operations | org-data-read, file-read |
| `authentication` | Authentication services | user-auth, sso-auth |
| `communication` | Messaging and notifications | send-message, send-email |
| `integration` | External system integration | sync-data, import-data |
| `processing` | Data processing | transform-data, analyze-data |
| `storage` | Storage operations | file-upload, file-download |

### 5.3 Capability Examples

```yaml
spec:
  capabilities:
    - id: org-data-read
      name: Organization Data Read
      description: Read organization structure and member data from Feishu
      category: data-access
      parameters:
        - name: orgId
          type: string
          required: false
          description: Organization ID (optional, uses default if not provided)
        - name: includeInactive
          type: boolean
          required: false
          default: false
          description: Whether to include inactive members
      returns:
        type: object
        description: Organization tree with members
      examples:
        - description: Get full organization tree
          input: {}
          output:
            orgId: "root"
            name: "Company"
            children: [...]

    - id: send-message
      name: Send Message
      description: Send message to user or group
      category: communication
      parameters:
        - name: target
          type: string
          required: true
          description: Target user ID or group ID
        - name: content
          type: string
          required: true
          description: Message content
        - name: type
          type: string
          required: false
          default: text
          description: Message type (text, markdown, card)
      returns:
        type: object
        description: Send result with message ID
```

## 6. Scenes

### 6.1 Scene Definition

```yaml
spec:
  scenes:
    - name: string               # Scene name (required)
      description: string        # Scene description (required)
      capabilities:              # Capabilities provided in this scene
        - string
      roles:                     # Member roles
        - roleId: string
          name: string
          required: boolean
          capabilities: [string]
      communicationProtocol: string  # http, websocket, grpc
      securityPolicy: string     # api-key, oauth2, jwt
      allowParallel: boolean     # Allow parallel execution (default: true)
```

### 6.2 Scene Examples

```yaml
spec:
  scenes:
    - name: auth
      description: Authentication scene with organization data support
      capabilities:
        - org-data-read
        - user-auth
      roles:
        - roleId: org-provider
          name: Organization Provider
          required: true
          capabilities: [org-data-read, user-auth]
        - roleId: org-consumer
          name: Organization Consumer
          required: false
          capabilities: [org-data-read]
      communicationProtocol: http
      securityPolicy: api-key
      allowParallel: true

    - name: messaging
      description: Unified messaging scene
      capabilities:
        - send-message
        - receive-message
      roles:
        - roleId: msg-provider
          name: Message Provider
          required: true
          capabilities: [send-message, receive-message]
        - roleId: msg-consumer
          name: Message Consumer
          required: false
          capabilities: [receive-message]
      communicationProtocol: http
      securityPolicy: api-key
      allowParallel: false       # Sequential message processing
```

## 7. Dependencies

### 7.1 Skill Dependencies

```yaml
spec:
  dependencies:
    skills:
      - id: string               # Skill ID (required)
        version: string          # Version constraint (required)
        optional: boolean        # Is optional (default: false)
        description: string      # Why this dependency is needed
```

### 7.2 Version Constraints

| Constraint | Description | Example |
|------------|-------------|---------|
| `1.0.0` | Exact version | `1.0.0` |
| `>=1.0.0` | Greater than or equal | `>=1.0.0` |
| `>1.0.0` | Greater than | `>1.0.0` |
| `<=1.0.0` | Less than or equal | `<=1.0.0` |
| `<1.0.0` | Less than | `<1.0.0` |
| `>=1.0.0 <2.0.0` | Range | `>=1.0.0 <2.0.0` |
| `~1.0.0` | Compatible (same minor) | `~1.0.0` means `>=1.0.0 <1.1.0` |
| `^1.0.0` | Compatible (same major) | `^1.0.0` means `>=1.0.0 <2.0.0` |

### 7.3 Library Dependencies

```yaml
spec:
  dependencies:
    libraries:
      - group: string            # Maven group ID
        artifact: string         # Maven artifact ID
        version: string          # Version
        scope: string            # compile, runtime, provided, test
```

### 7.4 Dependency Examples

```yaml
spec:
  dependencies:
    skills:
      - id: skill-vfs
        version: ">=1.0.0"
        optional: true
        description: For storing skill data and configuration
      - id: skill-msg
        version: "^1.0.0"
        optional: false
        description: For sending notifications
    
    libraries:
      - group: net.ooder
        artifact: ooder-common
        version: "2.0.0"
        scope: compile
      - group: org.springframework.boot
        artifact: spring-boot-starter-web
        version: "2.7.0"
        scope: compile
```

## 8. Configuration

### 8.1 Configuration Definition

```yaml
spec:
  config:
    required:                    # Required configuration items
      - name: string             # Configuration name (environment variable)
        type: string             # string, number, boolean, object, array
        description: string      # Description
        secret: boolean          # Is this a secret value?
        validation: string       # Validation regex or pattern
    optional:                    # Optional configuration items
      - name: string
        type: string
        description: string
        default: any             # Default value
        secret: boolean
```

### 8.2 Configuration Examples

```yaml
spec:
  config:
    required:
      - name: FEISHU_APP_ID
        type: string
        description: Feishu application ID
        secret: false
        validation: "^[a-zA-Z0-9]{16,32}$"
      - name: FEISHU_APP_SECRET
        type: string
        description: Feishu application secret
        secret: true
      - name: FEISHU_ENCRYPT_KEY
        type: string
        description: Encryption key for event callback
        secret: true
    
    optional:
      - name: FEISHU_API_BASE_URL
        type: string
        description: Feishu API base URL
        default: https://open.feishu.cn/open-apis
        secret: false
      - name: FEISHU_TIMEOUT
        type: number
        description: API request timeout in milliseconds
        default: 30000
        secret: false
      - name: FEISHU_CACHE_ENABLED
        type: boolean
        description: Enable response caching
        default: true
        secret: false
```

## 9. Endpoints

### 9.1 Endpoint Definition

```yaml
spec:
  endpoints:
    - path: string               # URL path (required)
      method: string             # HTTP method (required)
      description: string        # Description (required)
      capability: string         # Associated capability (required)
      authentication: boolean    # Requires authentication (default: true)
      rateLimit: number          # Rate limit per minute (optional)
      parameters:                # Path/query parameters (optional)
        - name: string
          in: path | query | body
          type: string
          required: boolean
          description: string
      response:                  # Response schema (optional)
        type: string
        schema: object
```

### 9.2 Endpoint Examples

```yaml
spec:
  endpoints:
    - path: /api/org/tree
      method: GET
      description: Get organization tree
      capability: org-data-read
      authentication: true
      rateLimit: 60
      parameters:
        - name: orgId
          in: query
          type: string
          required: false
          description: Organization ID (optional)
        - name: includeInactive
          in: query
          type: boolean
          required: false
          description: Include inactive members
      response:
        type: object
        schema:
          type: object
          properties:
            orgId:
              type: string
            name:
              type: string
            children:
              type: array

    - path: /api/org/person/{id}
      method: GET
      description: Get person by ID
      capability: org-data-read
      authentication: true
      parameters:
        - name: id
          in: path
          type: string
          required: true
          description: Person ID

    - path: /api/auth/verify
      method: POST
      description: Verify user authentication
      capability: user-auth
      authentication: false      # No auth required for login
      parameters:
        - name: credentials
          in: body
          type: object
          required: true
          description: User credentials
```

## 10. Resources

### 10.1 Resource Definition

```yaml
spec:
  resources:
    cpu: string                  # CPU requirement (e.g., "500m")
    memory: string               # Memory requirement (e.g., "512Mi")
    storage: string              # Storage requirement (e.g., "1Gi")
    network:                     # Network requirements (optional)
      ingress: boolean           # Requires ingress
      egress: boolean            # Requires egress
      ports:                     # Required ports
        - number
```

### 10.2 Resource Examples

```yaml
spec:
  resources:
    cpu: "500m"                  # 0.5 CPU cores
    memory: "512Mi"              # 512 MB
    storage: "1Gi"               # 1 GB
    network:
      ingress: true
      egress: true
      ports:
        - 8080
        - 8081
```

## 11. Deployment

### 11.1 Deployment Definition

```yaml
spec:
  deployment:
    modes:                       # Supported deployment modes
      - remote-hosted            # Hosted on SkillCenter
      - local-deployed           # Deployed locally
    singleton: boolean           # Only one instance allowed
    requiresAuth: boolean        # Requires authentication
    healthCheck:                 # Health check configuration
      path: string               # Health check endpoint
      interval: number           # Check interval in seconds
      timeout: number            # Timeout in seconds
    startup:                     # Startup configuration
      timeout: number            # Startup timeout in seconds
      order: number              # Startup order (lower = earlier)
```

### 11.2 Deployment Examples

```yaml
spec:
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: true
    requiresAuth: true
    healthCheck:
      path: /actuator/health
      interval: 30
      timeout: 10
    startup:
      timeout: 60
      order: 10                  # Start after infrastructure skills
```

## 12. Complete Example

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
  maintainers:
    - name: Ooder Team
      email: team@ooder.net
      github: ooder

spec:
  type: enterprise-skill
  
  runtime:
    language: java
    javaVersion: "11"
    framework: spring-boot
    mainClass: net.ooder.skill.org.feishu.FeishuSkillApplication
    jvmOpts: "-Xms256m -Xmx512m"
  
  capabilities:
    - id: org-data-read
      name: Organization Data Read
      description: Read organization structure and member data from Feishu
      category: data-access
    - id: user-auth
      name: User Authentication
      description: Authenticate users via Feishu OAuth
      category: authentication
  
  scenes:
    - name: auth
      description: Authentication scene with organization data support
      capabilities:
        - org-data-read
        - user-auth
      roles:
        - roleId: org-provider
          name: Organization Provider
          required: true
          capabilities: [org-data-read, user-auth]
      communicationProtocol: http
      securityPolicy: api-key
      allowParallel: true
  
  dependencies:
    skills:
      - id: skill-vfs
        version: ">=1.0.0"
        optional: true
        description: For storing skill data
    libraries:
      - group: net.ooder
        artifact: ooder-common
        version: "2.0.0"
        scope: compile
  
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
      authentication: true
    - path: /api/auth/verify
      method: POST
      description: Verify user authentication
      capability: user-auth
      authentication: false
  
  resources:
    cpu: "500m"
    memory: "512Mi"
    storage: "1Gi"
  
  deployment:
    modes:
      - remote-hosted
      - local-deployed
    singleton: true
    requiresAuth: true
    healthCheck:
      path: /actuator/health
      interval: 30
      timeout: 10
```

## 13. Validation

### 13.1 Required Fields Validation

The following fields are required and must be present:
- `apiVersion`
- `kind`
- `metadata.id`
- `metadata.name`
- `metadata.version`
- `metadata.description`
- `spec.type`
- `spec.runtime`
- `spec.capabilities`

### 13.2 Schema Validation

Use JSON Schema for validation:

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "required": ["apiVersion", "kind", "metadata", "spec"],
  "properties": {
    "apiVersion": {
      "type": "string",
      "pattern": "^skill\\.ooder\\.net/v[0-9]+$"
    },
    "kind": {
      "type": "string",
      "enum": ["Skill"]
    },
    "metadata": {
      "type": "object",
      "required": ["id", "name", "version", "description"],
      "properties": {
        "id": {
          "type": "string",
          "pattern": "^skill-[a-z0-9-]+$"
        },
        "version": {
          "type": "string",
          "pattern": "^[0-9]+\\.[0-9]+\\.[0-9]+$"
        }
      }
    }
  }
}
```
