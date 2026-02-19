# 测试用例存档 - 版本 0.1

## 存档信息

| 项目 | 值 |
|------|-----|
| 版本 | 0.1 |
| 存档日期 | 2026-02-16 |
| 测试用例总数 | 53 |
| 测试文件数 | 4 |
| 测试结果 | 全部通过 |

## 测试文件清单

| 文件 | 测试数 | 状态 | 覆盖模块 |
|------|--------|------|---------|
| IdentityInfoTest.java | 6 | ✅ 通过 | Core - Metadata |
| LocationInfoTest.java | 22 | ✅ 通过 | Core - Metadata |
| ShortestPathCalculatorTest.java | 12 | ✅ 通过 | Service - Network |
| JsonStorageTest.java | 13 | ✅ 通过 | Service - Storage |

## 测试覆盖详情

### IdentityInfoTest (6个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateIdentity | 测试创建Identity对象 |
| testSnapshot | 测试快照功能 |
| testHasScene | 测试场景判断 |
| testAttributes | 测试属性存取 |
| testFactoryMethods | 测试工厂方法 |
| testDisplayName | 测试显示名称 |

### LocationInfoTest (22个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateLocation | 测试创建Location对象 |
| testGetAddress | 测试获取地址 |
| testGetAddressWithEndpoint | 测试端点地址 |
| testGetAddressHostOnly | 测试仅主机地址 |
| testGetAddressNull | 测试空地址 |
| testGetFullAddress | 测试完整地址 |
| testHasValidAddress | 测试有效地址判断 |
| testHasValidAddressNoPort | 测试无端口情况 |
| testHasValidAddressNoHost | 测试无主机情况 |
| testIsLocal | 测试本地判断 |
| testIsLocalLoopback | 测试回环地址 |
| testIsInSameRegion | 测试同区域判断 |
| testIsInSameRegionDifferent | 测试不同区域 |
| testIsInSameRegionNull | 测试空参数 |
| testIsInSameZone | 测试同区域判断 |
| testDistanceTo | 测试距离计算 |
| testDistanceToNull | 测试空参数距离 |
| testSnapshot | 测试快照 |
| testFactoryOf | 测试工厂方法 |
| testFactoryOfEndpoint | 测试端点工厂 |
| testFactoryLocal | 测试本地工厂 |
| testAttributes | 测试属性 |
| testGetLocationKey | 测试位置键 |
| testGetLocationKeyNull | 测试空位置键 |

### ShortestPathCalculatorTest (12个测试)

| 测试方法 | 描述 |
|---------|------|
| testCalculateDirectRoute | 测试直连路由 |
| testCalculateMultiHopRoute | 测试多跳路由 |
| testCalculateNoRoute | 测试无路由情况 |
| testCalculateSameSourceAndDestination | 测试相同源和目标 |
| testCalculateAlternativesSingle | 测试单条备选路由 |
| testCalculateAlternativesMultiple | 测试多条备选路由 |
| testCalculateAlternativesZeroMax | 测试零最大值 |
| testCalculateAlternativesNegativeMax | 测试负数最大值 |
| testRouteStatus | 测试路由状态 |
| testHopCount | 测试跳数计算 |

### JsonStorageTest (13个测试)

| 测试方法 | 描述 |
|---------|------|
| testSaveAndLoad | 测试保存和加载 |
| testLoadNonExistent | 测试加载不存在数据 |
| testExists | 测试存在判断 |
| testDelete | 测试删除 |
| testListKeys | 测试列出键 |
| testSaveList | 测试保存列表 |
| testLoadEmptyList | 测试加载空列表 |
| testSaveWithSpecialCharacters | 测试特殊字符 |
| testSaveNestedObject | 测试嵌套对象 |
| testSanitizeKey | 测试键名清理 |
| testSaveNullValue | 测试空值 |
| testSaveBooleanValues | 测试布尔值 |
| testSaveNumericValues | 测试数值 |

## 已知问题

### 实现缺陷

| 文件 | 方法 | 问题描述 |
|------|------|---------|
| ShortestPathCalculator.java | calculateAlternatives() | 传递空HashSet，无法找到路径 |
| ShortestPathCalculator.java | getNeighbors() | 永远返回空Map |

### 未测试模块

- ResourceInfo.java
- Context.java
- Timeline.java
- SceneGroupInstance.java
- AgentMetadata.java
- 所有Installer阶段
- 所有Discoverer
- 所有Agent实现类

## 运行命令

```bash
mvn test
```

## 测试结果

```
[INFO] Tests run: 53, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
