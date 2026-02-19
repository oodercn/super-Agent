# SDK 7.3 与 Common 2.2 协作通知

## 一、SDK 7.3 变更概要

### 1.1 新增模块

| 模块 | 包路径 | 说明 |
|------|--------|------|
| 存储接口层 | `net.ooder.sdk.api.scene.store` | SceneStore/GroupStore/SkillStore/AgentStore/LinkStore |
| 服务模型层 | `net.ooder.sdk.api.skill` | SkillDefinition/Context/Request/Response/Callback/Service/Registry |
| 配置观察层 | `net.ooder.sdk.infra.observer` | ConfigObserver/ConfigChangeListener |
| 持久化实现 | `net.ooder.sdk.core.scene.impl` | PersistentSceneManagerImpl/PersistentSceneGroupManagerImpl |
| 双向存储 | `net.ooder.sdk.core.scene.store` | LocalSceneStore/DualSceneStore |

### 1.2 核心能力

- **场景持久化**: 本地文件 + VFS 双向存储
- **服务注册发现**: SkillRegistry 支持注册、发现、心跳
- **配置观察追踪**: ConfigObserver 支持读写追踪、验证

---

## 二、Common 2.2 需要移除的重复部分

### 2.1 需要移除的类

| 类名 | 包路径 | 替代方案 |
|------|--------|----------|
| `SceneGroupManager` | `net.ooder.northbound.skills` | 使用 `net.ooder.sdk.api.scene.SceneGroupManager` |
| `SceneGroupManagerImpl` | `net.ooder.northbound.skills.impl` | 使用 `net.ooder.sdk.core.scene.impl.PersistentSceneGroupManagerImpl` |
| `ConfigObserver` | `net.ooder.northbound.cluster.observer` | 使用 `net.ooder.sdk.infra.observer.ConfigObserver` |

### 2.2 需要适配的接口

```java
// 原接口
net.ooder.northbound.skills.SceneGroupManager
    String createScene(String sceneId, Map<String, Object> config);
    String createGroup(String sceneId, String groupId, Map<String, Object> config);
    String registerSkill(String sceneId, String groupId, SkillService skill);

// 新接口
net.ooder.sdk.api.scene.SceneGroupManager
    CompletableFuture<SceneGroup> create(String sceneId, SceneGroupConfig config);
    CompletableFuture<Void> join(String sceneGroupId, String agentId, MemberRole role);

net.ooder.sdk.api.skill.SkillRegistry
    CompletableFuture<String> register(SkillDefinition definition, SkillService service);
```

---

## 三、适配方案

### 3.1 创建适配器

```java
package net.ooder.northbound.adapter;

import net.ooder.sdk.api.scene.SceneGroupManager;
import net.ooder.sdk.api.scene.SceneGroup;
import net.ooder.sdk.api.scene.store.GroupStore;
import net.ooder.sdk.api.skill.SkillRegistry;
import net.ooder.sdk.api.skill.SkillDefinition;
import net.ooder.sdk.api.skill.SkillService;

public class NorthboundSceneGroupManagerAdapter {
    
    private final SceneGroupManager groupManager;
    private final GroupStore groupStore;
    private final SkillRegistry skillRegistry;
    
    public String createScene(String sceneId, Map<String, Object> config) {
        SceneDefinition definition = new SceneDefinition();
        definition.setSceneId(sceneId);
        definition.setConfig(config);
        
        sceneManager.create(definition).join();
        return sceneId;
    }
    
    public String createGroup(String sceneId, String groupId, Map<String, Object> config) {
        SceneGroupManager.SceneGroupConfig groupConfig = new SceneGroupManager.SceneGroupConfig();
        groupConfig.setSceneId(sceneId);
        groupConfig.setProperties(config);
        
        SceneGroup group = groupManager.create(sceneId, groupConfig).join();
        return group.getSceneGroupId();
    }
    
    public String registerSkill(String sceneId, String groupId, SkillService skill) {
        SkillDefinition definition = new SkillDefinition();
        definition.setSkillId(skill.getSkillId());
        definition.setSkillType(skill.getSkillType());
        definition.setSceneId(sceneId);
        definition.setGroupId(groupId);
        
        return skillRegistry.register(definition, skill).join();
    }
}
```

### 3.2 配置文件整合

```yaml
# sdk-config.yaml (统一配置)
scene:
  persistence:
    enabled: true
    storagePath: ./data/scenes

storage:
  dual:
    enabled: true
    local:
      path: ./config
      format: yaml
    remote:
      type: vfs
      endpoint: vfs://nexus/config
      syncInterval: 60000
    conflict:
      strategy: remote_wins

vfs:
  url: vfs://nexus
  syncEnabled: true
```

---

## 四、迁移步骤

### Phase 1: 适配层创建 (Day 1-2)

1. 创建 `NorthboundSceneGroupManagerAdapter`
2. 创建 `NorthboundConfigObserverAdapter`
3. 单元测试验证

### Phase 2: 调用方迁移 (Day 3-4)

1. 修改所有使用 `northbound.skills.SceneGroupManager` 的代码
2. 使用适配器替代直接调用
3. 集成测试验证

### Phase 3: 移除重复代码 (Day 5)

1. 删除 `northbound.skills.SceneGroupManager`
2. 删除 `northbound.skills.impl.SceneGroupManagerImpl`
3. 删除 `northbound.cluster.observer.ConfigObserver`
4. 回归测试

---

## 五、依赖更新

### 5.1 Maven 依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>7.3</version>
</dependency>
```

### 5.2 新增依赖类

```java
// 存储接口
import net.ooder.sdk.api.scene.store.SceneStore;
import net.ooder.sdk.api.scene.store.GroupStore;
import net.ooder.sdk.api.scene.store.SkillStore;
import net.ooder.sdk.api.scene.store.AgentStore;
import net.ooder.sdk.api.scene.store.LinkStore;

// 服务模型
import net.ooder.sdk.api.skill.SkillDefinition;
import net.ooder.sdk.api.skill.SkillContext;
import net.ooder.sdk.api.skill.SkillRequest;
import net.ooder.sdk.api.skill.SkillResponse;
import net.ooder.sdk.api.skill.SkillService;
import net.ooder.sdk.api.skill.SkillRegistry;

// 配置观察
import net.ooder.sdk.infra.observer.ConfigObserver;
import net.ooder.sdk.infra.observer.ConfigChangeListener;

// 持久化管理
import net.ooder.sdk.core.scene.impl.PersistentSceneManagerImpl;
import net.ooder.sdk.core.scene.impl.PersistentSceneGroupManagerImpl;
import net.ooder.sdk.core.scene.store.DualSceneStore;
import net.ooder.sdk.core.scene.store.LocalSceneStore;
```

---

## 六、验证清单

| 验收项 | 状态 | 说明 |
|--------|------|------|
| 场景创建持久化 | ⏳ | 创建场景后持久化到本地文件 |
| 组创建持久化 | ⏳ | 创建组后持久化到本地文件 |
| Skill注册持久化 | ⏳ | 注册Skill后持久化 |
| VFS双向同步 | ⏳ | 本地和VFS数据同步 |
| 配置读写追踪 | ⏳ | ConfigObserver记录读写操作 |
| 心跳超时检测 | ⏳ | SkillRegistry检测心跳超时 |

---

## 七、联系方式

- SDK 版本: `7.3`
- Common 版本: `2.2`
- 协作负责人: SDK Team
- 更新日期: 2026-02-19

---

**请 Common 团队确认以上变更，并在 5 个工作日内完成适配迁移。**
