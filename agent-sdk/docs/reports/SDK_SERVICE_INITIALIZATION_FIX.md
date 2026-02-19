# SDK 服务初始化问题解决报告

**文档版本**: 1.0  
**解决日期**: 2026-02-18  
**SDK版本**: 0.7.2  

---

## 1. 问题现象

agent-skillcenter 项目使用 SDK 0.7.2 时，发现以下服务返回 null：

```
[AgentSDKWrapper] - SkillPackageManager: null
[AgentSDKWrapper] - SceneManager: null
[AgentSDKWrapper] - SceneGroupManager: available
```

---

## 2. 问题原因

在 `OoderSDK.Builder.build()` 方法中，只有 `sceneGroupManager` 设置了默认实现，而 `skillPackageManager` 和 `sceneManager` 没有默认值：

**修复前代码**:
```java
public OoderSDK build() {
    if (configuration == null) {
        configuration = new SDKConfiguration();
    }
    if (agentFactory == null) {
        agentFactory = new net.ooder.sdk.core.agent.factory.AgentFactoryImpl();
    }
    if (sceneGroupManager == null) {
        sceneGroupManager = new net.ooder.sdk.core.scene.impl.SceneGroupManagerImpl();
    }
    return new OoderSDK(this);  // skillPackageManager 和 sceneManager 为 null
}
```

---

## 3. 解决方案

在 `OoderSDK.Builder.build()` 方法中为所有核心服务添加默认实现：

**修复后代码**:
```java
public OoderSDK build() {
    if (configuration == null) {
        configuration = new SDKConfiguration();
    }
    if (agentFactory == null) {
        agentFactory = new net.ooder.sdk.core.agent.factory.AgentFactoryImpl();
    }
    if (skillPackageManager == null) {
        skillPackageManager = new net.ooder.sdk.core.skill.impl.SkillPackageManagerImpl();
    }
    if (sceneManager == null) {
        sceneManager = new net.ooder.sdk.core.scene.impl.SceneManagerImpl();
    }
    if (sceneGroupManager == null) {
        sceneGroupManager = new net.ooder.sdk.core.scene.impl.SceneGroupManagerImpl();
    }
    if (capabilityInvoker == null) {
        capabilityInvoker = new net.ooder.sdk.core.scene.impl.CapabilityInvokerImpl();
    }
    return new OoderSDK(this);
}
```

---

## 4. 服务可用性状态

| 服务 | 修复前 | 修复后 | 实现类 |
|------|--------|--------|--------|
| SkillPackageManager | null | ✅ 可用 | SkillPackageManagerImpl |
| SceneManager | null | ✅ 可用 | SceneManagerImpl |
| SceneGroupManager | ✅ 可用 | ✅ 可用 | SceneGroupManagerImpl |
| CapabilityInvoker | null | ✅ 可用 | CapabilityInvokerImpl |

---

## 5. 使用示例

### 5.1 简单初始化

```java
OoderSDK sdk = OoderSDK.builder()
    .configuration(new SDKConfiguration())
    .build();

sdk.initialize();
sdk.start();

// 现在所有服务都可用
SkillPackageManager skillManager = sdk.getSkillPackageManager();  // 非null
SceneManager sceneManager = sdk.getSceneManager();                // 非null
SceneGroupManager groupManager = sdk.getSceneGroupManager();      // 非null
```

### 5.2 完整配置示例

```java
SDKConfiguration config = new SDKConfiguration();
config.setNodeId("node-001");
config.setNodeName("SkillCenter-Node");
config.setLogLevel("INFO");

OoderSDK sdk = OoderSDK.builder()
    .configuration(config)
    .build();

try {
    sdk.initialize();
    sdk.start();
    
    // 使用 SkillPackageManager
    SkillPackageManager skillManager = sdk.getSkillPackageManager();
    skillManager.discover("skill-001", DiscoveryMethod.LOCAL_FS)
        .thenCompose(pkg -> {
            InstallRequest request = new InstallRequest();
            request.setSkillId("skill-001");
            return skillManager.install(request);
        })
        .thenAccept(result -> {
            System.out.println("Skill installed: " + result.isSuccess());
        });
    
    // 使用 SceneManager
    SceneManager sceneManager = sdk.getSceneManager();
    SceneDefinition definition = new SceneDefinition();
    definition.setSceneId("scene-001");
    definition.setSceneName("Test Scene");
    sceneManager.create(definition)
        .thenAccept(scene -> {
            System.out.println("Scene created: " + scene.getSceneId());
        });
    
    // 使用 SceneGroupManager
    SceneGroupManager groupManager = sdk.getSceneGroupManager();
    SceneGroupConfig groupConfig = new SceneGroupConfig();
    groupConfig.setMaxMembers(10);
    groupManager.create("scene-001", groupConfig)
        .thenAccept(group -> {
            System.out.println("Group created: " + group.getGroupId());
        });
    
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## 6. 接口说明

### 6.1 SkillPackageManager 接口

```java
public interface SkillPackageManager {
    CompletableFuture<SkillPackage> discover(String skillId, DiscoveryMethod method);
    CompletableFuture<InstallResult> install(InstallRequest request);
    CompletableFuture<UninstallResult> uninstall(String skillId);
    CompletableFuture<UpdateResult> update(String skillId, String version);
    CompletableFuture<List<InstalledSkill>> listInstalled();
    CompletableFuture<InstalledSkill> getInstalled(String skillId);
    void addObserver(SkillPackageObserver observer);
    void removeObserver(SkillPackageObserver observer);
}
```

### 6.2 SceneManager 接口

```java
public interface SceneManager {
    CompletableFuture<SceneDefinition> create(SceneDefinition definition);
    CompletableFuture<Void> delete(String sceneId);
    CompletableFuture<SceneDefinition> get(String sceneId);
    CompletableFuture<List<SceneDefinition>> list(int page, int size);
    CompletableFuture<Boolean> activate(String sceneId);
    CompletableFuture<Boolean> deactivate(String sceneId);
    CompletableFuture<SceneState> getState(String sceneId);
    CompletableFuture<Boolean> addCapability(String sceneId, Capability capability);
    CompletableFuture<Boolean> removeCapability(String sceneId, String capId);
    CompletableFuture<SceneSnapshot> createSnapshot(String sceneId);
    CompletableFuture<Boolean> restoreSnapshot(String sceneId, String snapshotId);
}
```

### 6.3 SceneGroupManager 接口

```java
public interface SceneGroupManager {
    CompletableFuture<SceneGroup> create(String sceneId, SceneGroupConfig config);
    CompletableFuture<Boolean> destroy(String groupId);
    CompletableFuture<SceneGroup> get(String groupId);
    CompletableFuture<List<SceneGroup>> list(int page, int size);
    CompletableFuture<Boolean> join(String groupId, String agentId, String role);
    CompletableFuture<Boolean> leave(String groupId, String agentId);
    CompletableFuture<List<SceneMember>> listMembers(String groupId);
    CompletableFuture<SceneMember> getPrimaryMember(String groupId);
    CompletableFuture<Boolean> handleFailover(String groupId, String failedMemberId);
}
```

---

## 7. 外部服务依赖

当前 SDK 0.7.2 的核心服务**不依赖外部服务**，所有功能均为本地实现：

| 服务 | 外部依赖 | 说明 |
|------|----------|------|
| SkillPackageManager | 无 | 本地技能包管理 |
| SceneManager | 无 | 本地场景管理 |
| SceneGroupManager | 无 | 本地场景组管理 |
| CapabilityInvoker | 无 | 本地能力调用 |

如需网络协作功能（如 P2P 技能共享、跨节点场景组），需要配置南向协议：

```java
// 网络协作配置示例
SDKConfiguration config = new SDKConfiguration();
config.setDiscoveryEnabled(true);
config.setCollaborationEnabled(true);
```

---

## 8. 版本更新

此修复已包含在 SDK 0.7.2 最新版本中，已部署到本地 Maven 仓库。

---

**修复者**: ooder SDK Team  
**日期**: 2026-02-18
