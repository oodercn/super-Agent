# ooderNexus 调试工具使用说明

## 1. 概述

ooderNexus 提供了一系列内置调试工具，帮助开发者快速定位问题、优化性能。

## 2. 调试工具列表

### 2.1 P2P 通信报文查看器
实时查看 P2P 网络中的通信报文。

**访问方式：**
- Web 控制台 → 网络管理 → P2P 调试

**功能：**
- 实时显示发送/接收的报文
- 支持报文过滤和搜索
- 报文解析和格式化显示

### 2.2 技能执行调试器
调试技能执行流程，查看执行堆栈。

**访问方式：**
- Web 控制台 → 技能中心 → 调试工具

**功能：**
- 单步执行技能
- 查看执行堆栈
- 检查变量值

### 2.3 系统日志查看器
查看系统运行日志。

**访问方式：**
- Web 控制台 → 系统监控 → 日志
- 或直接查看 `logs/` 目录

**功能：**
- 实时日志流
- 日志级别过滤
- 日志导出

### 2.4 API 测试工具
使用 Swagger UI 测试 REST API。

**访问地址：**
```
http://localhost:8081/swagger-ui.html
```

## 3. 日志级别配置

在 `application.yml` 中配置：

```yaml
logging:
  level:
    root: INFO
    net.ooder.nexus: DEBUG
    net.ooder.sdk: DEBUG
```

## 4. 远程调试

启动时添加 JVM 参数：

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar ooder-nexus.jar
```

然后在 IDE 中配置远程调试连接 localhost:5005。

## 5. 性能分析

### 5.1 使用 Actuator

访问以下端点获取性能数据：

```
http://localhost:8081/actuator/metrics
http://localhost:8081/actuator/health
http://localhost:8081/actuator/info
```

### 5.2 使用 VisualVM

1. 下载 VisualVM
2. 连接到运行的 ooderNexus 进程
3. 查看 CPU、内存、线程使用情况

## 6. 常见问题排查

### 问题1：服务无法启动

**排查步骤：**
1. 检查端口是否被占用
2. 查看日志文件 `logs/nexus.log`
3. 检查配置文件格式

### 问题2：P2P 网络连接失败

**排查步骤：**
1. 检查防火墙设置
2. 确认 UDP 端口开放
3. 查看 P2P 调试页面的连接状态

### 问题3：技能执行失败

**排查步骤：**
1. 使用技能调试器单步执行
2. 查看执行日志
3. 检查参数是否正确

---

**更多调试技巧请参考开发指南。**
