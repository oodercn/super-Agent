# Changelog

所有重要的变更都将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
并且本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [Unreleased]

### Added
- 计划添加更多OpenWrt设备支持
- 计划添加Docker部署支持
- 计划添加WebDAV文件共享功能
- **Ooder Agent SDK 0.7.3 协议支持**
  - DiscoveryProtocol: 多路节点发现协议（UDP/DHT/SkillCenter/mDNS）
  - LoginProtocol: 本地认证协议，支持离线认证
  - CollaborationProtocol: 场景组协作协议，支持任务分配
  - OfflineService: 离线服务，支持网络断开时继续运行
  - EventBus: 事件总线，统一事件管理
  - CloudHostingProtocol: 云托管协议，支持 Kubernetes 集群部署
  - DriverProxyPackage: 驱动代理包支持
  - ValidationToolchain: 验证工具链

### Changed
- 优化P2P网络发现性能
- 改进Web控制台用户体验
- SDK 版本从 0.6.6 升级到 0.7.3

---

## [2.0.0-openwrt-preview] - 2026-02-11

### 🎉 新增功能

#### OpenWrt 路由器管理（预览版）
- **路由器自动发现**: 自动扫描并发现局域网内的OpenWrt设备
- **SSH连接管理**: 支持通过SSH连接到OpenWrt路由器
- **系统状态监控**: 实时显示CPU、内存、温度、运行时间
- **网络配置管理**: 查看和配置网络接口、DHCP、防火墙
- **命令执行**: 通过Web界面执行Shell命令
- **文件管理**: 上传、下载、编辑路由器文件

#### Windows 安装包
- **一键启动**: 提供`start.bat`脚本，双击即可运行
- **真实设备模式**: 默认关闭Mock模式，连接真实OpenWrt设备
- **详细文档**: 包含完整的安装和使用说明（README-Preview.txt）

#### 技能管理增强
- **技能发布**: 支持将本地AI能力发布到网络
- **技能分享**: 支持分享给特定用户或群组
- **技能执行**: 远程执行网络中的技能
- **技能市场**: 浏览和安装网络中的技能

### 🔧 改进优化

#### 系统架构
- 优化OpenWrt自动检测逻辑，支持多种检测方式
- 改进Web控制台UI，增加OpenWrt管理页面
- 优化P2P网络连接稳定性
- 改进错误处理和日志记录

#### 性能优化
- 优化SSH连接池管理
- 减少内存占用
- 提高并发处理能力

### 🐛 问题修复

- 修复SSH连接超时问题
- 修复技能执行日志显示异常
- 修复网络拓扑图在某些情况下不更新的问题
- 修复文件传输进度显示不准确的问题

### ⚠️ 已知限制

1. **预览版声明**: 此版本为预览版，仅供测试和评估使用
2. **功能不完整**: 部分高级功能仍在开发中
3. **兼容性问题**: 仅测试了部分OpenWrt版本（21.02+）
4. **数据迁移**: 预览版数据可能不兼容正式版
5. **Mock模式**: Windows版本默认关闭Mock，需要真实OpenWrt设备

### 📦 发布文件

| 文件 | 大小 | 说明 |
|------|------|------|
| `ooder-nexus-2.0.0-openwrt-preview-windows.zip` | 40.7 MB | Windows安装包 |
| `ooder-nexus-2.0.0-preview.jar` | 44.7 MB | 可执行JAR包 |
| `README-Preview.txt` | 3.4 KB | 详细说明文档 |
| `start.bat` | 827 B | Windows启动脚本 |

### 🔗 下载地址

- **GitHub Releases**: https://github.com/oodercn/ooder-Nexus/releases/tag/v2.0.0-openwrt-preview
- **Gitee 镜像**: https://gitee.com/ooderCN/ooder-nexus/releases

---

## [2.0.0] - 2025-02-10

### 🎉 新增功能

#### OpenWrt 集成
- **自动角色检测**: 启动时自动检测OpenWrt系统，自动设置Agent角色为`routeAgent`
- **深度系统集成**: 支持路由器系统状态监控、网络配置管理、IP地址管理
- **一键安装脚本**: 提供OpenWrt专用安装脚本，自动完成环境配置

#### VFS 虚拟文件系统
- **哈希去重**: 相同文件只存储一份，节省存储空间
- **版本控制**: 文件修改自动创建新版本，支持历史回溯
- **原子操作**: 临时文件+重命名机制，防止数据损坏
- **P2P同步**: 文件自动同步到网络节点，实现分布式存储

#### 协议仿真与调试
- **MCP仿真器**: 模拟MCP协议通信流程
- **Route仿真器**: 模拟路由协议行为
- **场景化测试**: 自定义测试场景，验证协议正确性
- **可视化调试**: 图形化展示协议执行流程

#### 网络管理
- **网络拓扑可视化**: 图形化展示P2P网络结构
- **链路管理**: 节点间链路创建、监控、断开
- **流量监控**: 实时网络流量统计和分析

### 🔧 改进优化

- 优化P2P网络发现性能
- 改进Web控制台响应速度
- 优化数据存储结构
- 改进错误提示信息

### 🐛 问题修复

- 修复P2P连接在某些网络环境下不稳定的问题
- 修复技能执行超时处理不当的问题
- 修复文件同步冲突处理逻辑
- 修复日志文件过大导致的性能问题

---

## [1.0.0] - 2024-12-01

### 🎉 初始版本发布

#### 核心功能
- **P2P网络**: 去中心化组网，节点自动发现
- **AI技能管理**: 发布、分享、执行AI技能
- **存储管理**: 虚拟文件系统，支持文件版本控制
- **系统监控**: 实时监控系统状态和性能指标

#### 技术特性
- 基于Ooder Agent SDK 0.6.6
- Spring Boot 2.7.0 + Java 8
- 支持Windows/Linux/macOS/OpenWrt
- MIT开源协议

---

## 版本说明

### 版本号格式

本项目遵循[语义化版本](https://semver.org/lang/zh-CN/)规范：

- **MAJOR**: 不兼容的API修改
- **MINOR**: 向下兼容的功能新增
- **PATCH**: 向下兼容的问题修复
- **prerelease**: 预发布标识（如`-alpha`, `-beta`, `-preview`）

### 版本标签说明

| 标签 | 含义 |
|------|------|
| `[Unreleased]` | 未发布的功能，正在开发中 |
| `Added` | 新增功能 |
| `Changed` | 功能变更 |
| `Deprecated` | 废弃功能 |
| `Removed` | 移除功能 |
| `Fixed` | 问题修复 |
| `Security` | 安全修复 |

---

**维护者**: ooder Team  
**最后更新**: 2026-02-11
