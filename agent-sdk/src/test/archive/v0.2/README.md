# 测试用例存档 - 版本 0.2

## 存档信息

| 项目 | 值 |
|------|-----|
| 版本 | 0.2 |
| 存档日期 | 2026-02-16 |
| 测试用例总数 | 215 |
| 测试文件数 | 9 |
| 测试结果 | 全部通过 |

## 测试文件清单

| 文件 | 测试数 | 状态 | 覆盖模块 |
|------|--------|------|---------|
| IdentityInfoTest.java | 17 | ✅ 通过 | Core - Metadata |
| LocationInfoTest.java | 22 | ✅ 通过 | Core - Metadata |
| ResourceInfoTest.java | 31 | ✅ 通过 | Core - Metadata |
| ContextTest.java | 22 | ✅ 通过 | Core - Metadata |
| TimelineTest.java | 37 | ✅ 通过 | Core - Metadata |
| AgentMetadataTest.java | 33 | ✅ 通过 | Core - Metadata |
| SceneGroupInstanceTest.java | 28 | ✅ 通过 | Core - Metadata |
| ShortestPathCalculatorTest.java | 12 | ✅ 通过 | Service - Network |
| JsonStorageTest.java | 13 | ✅ 通过 | Service - Storage |

## 测试覆盖详情

### IdentityInfoTest (17个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateIdentity | 测试创建Identity对象 |
| testSnapshot | 测试快照功能 |
| testHasScene | 测试场景判断 |
| testAttributes | 测试属性存取 |
| testFactoryMethods | 测试工厂方法 |
| testDisplayName | 测试显示名称 |
| testOwnerId | 测试所有者ID |
| testOrganizationId | 测试组织ID |
| testSceneGroupId | 测试场景组ID |
| testRole | 测试角色 |
| testIsMcpAgent | 测试MCP代理判断 |
| testIsRouteAgent | 测试路由代理判断 |
| testIsEndAgent | 测试终端代理判断 |
| testHasSceneGroup | 测试场景组判断 |
| testHasSkill | 测试技能判断 |
| testGetIdentityKey | 测试身份键获取 |

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

### ResourceInfoTest (31个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateResource | 测试创建资源对象 |
| testDefaultStatus | 测试默认状态 |
| testCapabilities | 测试能力列表 |
| testSetCapabilities | 测试设置能力列表 |
| testDependencies | 测试依赖列表 |
| testTags | 测试标签 |
| testSetTags | 测试设置标签 |
| testProperties | 测试属性 |
| testGetPropertyAsString | 测试字符串属性获取 |
| testGetPropertyAsInt | 测试整数属性获取 |
| testGetPropertyAsLong | 测试长整数属性获取 |
| testGetPropertyAsBoolean | 测试布尔属性获取 |
| testGetPropertyWithDefault | 测试带默认值属性获取 |
| testMetrics | 测试指标 |
| testGetMetricAsDouble | 测试双精度指标获取 |
| testIsActive | 测试活跃状态判断 |
| testIsIdle | 测试空闲状态判断 |
| testIsError | 测试错误状态判断 |
| testIsBusy | 测试忙碌状态判断 |
| testMarkMethods | 测试状态标记方法 |
| testGetResourceKey | 测试资源键获取 |
| testSnapshot | 测试快照 |
| testFactoryOfTwo | 测试双参数工厂 |
| testFactoryOfThree | 测试三参数工厂 |
| testAction | 测试动作 |

### ContextTest (22个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateContextWithSnapshots | 测试使用快照创建上下文 |
| testCreateContextWithInfo | 测试使用信息对象创建上下文 |
| testCreateContextWithNullInfo | 测试空信息创建上下文 |
| testGetAgentId | 测试获取代理ID |
| testGetAgentIdNull | 测试空代理ID |
| testGetAddress | 测试获取地址 |
| testGetAddressNull | 测试空地址 |
| testGetStatus | 测试获取状态 |
| testGetStatusNull | 测试空状态 |
| testHasCapability | 测试能力判断 |
| testHasCapabilityNullResource | 测试空资源能力判断 |
| testIsValid | 测试有效性判断 |
| testIsValidNullIdentity | 测试空身份有效性 |
| testIsValidNullAgentId | 测试空代理ID有效性 |
| testGetSummary | 测试摘要获取 |
| testGetSummaryWithNulls | 测试空值摘要 |
| testEmptyFactory | 测试空工厂方法 |
| testTimestamp | 测试时间戳 |
| testGetIdentity | 测试获取身份 |
| testGetLocation | 测试获取位置 |
| testGetResource | 测试获取资源 |

### TimelineTest (37个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateTimeline | 测试创建时间线 |
| testCreateWithMaxHistory | 测试带最大历史创建 |
| testDefaultMaxHistory | 测试默认最大历史 |
| testRecordEvent | 测试记录事件 |
| testRecordEventWithContext | 测试带上下文记录事件 |
| testGetLatest | 测试获取最新事件 |
| testGetLatestEmpty | 测试空时间线最新事件 |
| testGetFirst | 测试获取首个事件 |
| testGetFirstEmpty | 测试空时间线首个事件 |
| testGetByEventType | 测试按类型获取事件 |
| testGetByTimeRange | 测试按时间范围获取事件 |
| testFindNearest | 测试查找最近事件 |
| testFindNearestEmpty | 测试空时间线查找最近 |
| testGetContextAt | 测试获取指定时间上下文 |
| testGetContextAtEmpty | 测试空时间线获取上下文 |
| testIsOnline | 测试在线状态判断 |
| testIsOnlineEmpty | 测试空时间线在线状态 |
| testGetAge | 测试获取年龄 |
| testGetUptime | 测试获取运行时间 |
| testGetIdleTime | 测试获取空闲时间 |
| testGetIdleTimeEmpty | 测试空时间线空闲时间 |
| testGetTimeSinceLastEvent | 测试获取距上次事件时间 |
| testGetEventCount | 测试获取事件计数 |
| testClear | 测试清空 |
| testGetAgeFormatted | 测试格式化年龄 |
| testGetIdleTimeFormatted | 测试格式化空闲时间 |
| testGetStatusSummary | 测试状态摘要 |
| testGetStatusSummaryOffline | 测试离线状态摘要 |
| testUpdateContext | 测试更新上下文 |
| testSetCurrentContext | 测试设置当前上下文 |
| testSetMaxHistory | 测试设置最大历史 |
| testSetOnlineTimeout | 测试设置在线超时 |
| testMaxHistoryLimit | 测试最大历史限制 |
| testFactoryCreate | 测试工厂创建 |
| testFactoryWithMaxHistory | 测试带最大历史工厂 |
| testGetCreateTime | 测试获取创建时间 |
| testIsExpired | 测试过期判断 |

### AgentMetadataTest (33个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateMetadata | 测试创建元数据 |
| testCreateMetadataWithAgentId | 测试带代理ID创建 |
| testCreateMetadataWithComponents | 测试带组件创建 |
| testCreateMetadataWithNullComponents | 测试空组件创建 |
| testGetAgentId | 测试获取代理ID |
| testGetAgentName | 测试获取代理名称 |
| testGetAgentType | 测试获取代理类型 |
| testGetAddress | 测试获取地址 |
| testGetHost | 测试获取主机 |
| testGetPort | 测试获取端口 |
| testGetStatus | 测试获取状态 |
| testHasCapability | 测试能力判断 |
| testIsOnline | 测试在线状态 |
| testGetUptime | 测试运行时间 |
| testRecordEvent | 测试记录事件 |
| testRecordEventWithDetail | 测试带详情记录事件 |
| testUpdateContext | 测试更新上下文 |
| testGetCurrentContext | 测试获取当前上下文 |
| testGetContextAt | 测试获取指定时间上下文 |
| testGetSceneId | 测试获取场景ID |
| testGetSceneGroupId | 测试获取场景组ID |
| testGetSkillId | 测试获取技能ID |
| testIsInScene | 测试场景内判断 |
| testIsInSceneGroup | 测试场景组内判断 |
| testHasSkill | 测试技能判断 |
| testGetSummary | 测试摘要获取 |
| testGetFullReport | 测试完整报告 |
| testStartInstance | 测试启动实例 |
| testCompleteInstanceSuccess | 测试成功完成实例 |
| testCompleteInstanceFailure | 测试失败完成实例 |
| testTerminateInstance | 测试终止实例 |
| testSetCurrentInstance | 测试设置当前实例 |
| testExtendedInfo | 测试扩展信息 |
| testBuilder | 测试构建器 |
| testFactoryOfOne | 测试单参数工厂 |
| testFactoryOfThree | 测试三参数工厂 |

### SceneGroupInstanceTest (28个测试)

| 测试方法 | 描述 |
|---------|------|
| testCreateInstance | 测试创建实例 |
| testCreateInstanceWithIds | 测试带ID创建实例 |
| testSetSceneGroupId | 测试设置场景组ID |
| testSetSceneId | 测试设置场景ID |
| testSetState | 测试设置状态 |
| testSetCreateTime | 测试设置创建时间 |
| testSetStartTime | 测试设置开始时间 |
| testSetEndTime | 测试设置结束时间 |
| testIsRunning | 测试运行状态判断 |
| testIsCompleted | 测试完成状态判断 |
| testIsSuccessful | 测试成功状态判断 |
| testStart | 测试启动 |
| testCompleteSuccess | 测试成功完成 |
| testCompleteFailure | 测试失败完成 |
| testTerminate | 测试终止 |
| testCreateRecoveryPoint | 测试创建恢复点 |
| testCreateRecoveryPointNotRunning | 测试非运行状态创建恢复点 |
| testGetLatestRecoveryPoint | 测试获取最新恢复点 |
| testGetLatestRecoveryPointEmpty | 测试空恢复点列表获取最新 |
| testGetDuration | 测试获取持续时间 |
| testGetDurationNotStarted | 测试未启动获取持续时间 |
| testGetDurationCompleted | 测试已完成获取持续时间 |
| testGetDurationFormatted | 测试格式化持续时间 |
| testSetResult | 测试设置结果 |
| testSetContext | 测试设置上下文 |
| testSetRecoveryPoints | 测试设置恢复点列表 |
| testInstanceResult | 测试实例结果 |
| testInstanceContext | 测试实例上下文 |
| testInstanceContextRemoveMember | 测试移除成员 |
| testInstanceContextRecordEvent | 测试记录事件 |
| testInstanceContextSnapshot | 测试上下文快照 |
| testRecoveryPoint | 测试恢复点 |
| testCompleteClearsContext | 测试完成清除上下文 |

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

## 运行命令

```bash
mvn test
```

## 测试结果

```
[INFO] Tests run: 215, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## 版本变更

### v0.2 (2026-02-16)
- 新增 ResourceInfoTest (31个测试)
- 新增 TimelineTest (37个测试)
- 新增 ContextTest (22个测试)
- 新增 AgentMetadataTest (33个测试)
- 新增 SceneGroupInstanceTest (28个测试)
- IdentityInfoTest 新增11个测试 (总计17个)

### v0.1 (2026-02-16)
- 初始版本
- IdentityInfoTest (6个测试)
- LocationInfoTest (22个测试)
- ShortestPathCalculatorTest (12个测试)
- JsonStorageTest (13个测试)
