# ooderNexus v2.0.0 OpenWrt 预览版发布

**发布日期**: 2026年2月11日  
**版本号**: v2.0.0-openwrt-preview  
**版本类型**: OpenWrt 预览版 (Pre-release)  
**开源协议**: MIT  
**GitHub**: https://github.com/oodercn/ooder-Nexus  
**国内镜像**: https://gitee.com/ooderCN/ooder-nexus

---

## 🎉 重要发布声明

我们很高兴地宣布 **ooderNexus v2.0.0 OpenWrt 预览版** 现已发布！这是一个专为 OpenWrt 路由器优化的预览版本，让每一台路由器都能成为智能网络中枢。

> ⚠️ **预览版声明**: 此版本为预览版，仅供测试和评估使用，不建议在生产环境中使用。

### 什么是 ooderNexus？

ooderNexus 是一个基于 **Ooder Agent 架构**的 **P2P AI 能力分发枢纽**，采用 MIT 开源协议。它将去中心化的 P2P 网络技术与 AI 能力管理相结合，让用户能够在本地网络中构建私有的 AI 能力共享平台。

---

## 📥 下载地址

| 平台 | 下载链接 | 文件大小 |
|------|----------|----------|
| **Windows 安装包** | [ooder-nexus-2.0.0-openwrt-preview-windows.zip](https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/ooder-nexus-2.0.0-openwrt-preview-windows.zip) | 40.7 MB |
| **JAR 包** | [ooder-nexus-2.0.0-preview.jar](https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/ooder-nexus-2.0.0-preview.jar) | 44.7 MB |
| **OpenWrt 安装脚本** | [install-openwrt.sh](https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/install-openwrt.sh) | - |

**Gitee 镜像下载**（国内加速）:
- https://gitee.com/ooderCN/ooder-nexus/releases

---

## ✨ OpenWrt 预览版核心亮点

### 1. OpenWrt 深度集成 - 路由器即 AI 中枢

**专为 OpenWrt 优化**: 将 AI 能力直接部署到家庭/企业路由器，让路由器成为智能网络中枢。

- **自动角色检测**: 智能识别 OpenWrt 系统，自动配置为 `routeAgent` 角色
- **系统级集成**: CPU/内存/温度监控、网络配置管理、IP 地址分配
- **一键安装**: 提供 OpenWrt 专用安装脚本，5 分钟完成部署
- **24/7 运行**: 利用路由器长期在线特性，构建稳定的 AI 能力节点

**适用场景**:
- 🏠 家庭智能中枢 - 统一管理智能家居设备
- 🏢 企业边缘节点 - 分布式 AI 能力部署
- 🌐 网关代理 - 为内网设备提供 AI 服务

### 2. Windows 安装包 - 一键启动测试

**便捷的测试环境**: 提供 Windows 安装包，方便用户快速体验 OpenWrt 管理功能。

- **一键启动**: 双击 `start.bat` 即可运行，无需复杂配置
- **真实设备模式**: 默认关闭 Mock 模式，连接真实 OpenWrt 设备进行测试
- **详细文档**: 包含完整的安装和使用说明

### 3. VFS 虚拟文件系统 - 企业级存储架构

**存储技术升级**: 引入基于 MD5 哈希的虚拟文件系统，实现高效的文件管理。

- **哈希去重**: 相同文件只存储一份，节省存储空间
- **版本控制**: 文件修改自动创建新版本，支持历史回溯
- **原子操作**: 临时文件 + 重命名机制，防止数据损坏
- **P2P 同步**: 文件自动同步到网络节点，实现分布式存储

### 4. 协议仿真与调试 - 开发者的利器

**离线开发支持**: 无需真实网络环境，即可完成协议开发和测试。

- **MCP 仿真器**: 模拟 MCP 协议通信流程
- **Route 仿真器**: 模拟路由协议行为
- **场景化测试**: 自定义测试场景，验证协议正确性
- **可视化调试**: 图形化展示协议执行流程

### 5. 网络拓扑可视化 - 一目了然

**直观的网络管理**: 图形化展示 P2P 网络结构，让网络管理变得简单。

- **实时拓扑图**: 动态展示节点连接关系
- **链路状态**: 实时显示链路质量和流量
- **节点管理**: 一键添加/删除/配置节点
- **流量监控**: 实时统计网络流量

---

## 📦 安装方式

### Windows（推荐用于测试）

```powershell
# 1. 下载 Windows 安装包
# 访问: https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/ooder-nexus-2.0.0-openwrt-preview-windows.zip

# 2. 解压到任意目录

# 3. 双击运行 start.bat

# 4. 访问 http://localhost:8081/console/index.html
```

### OpenWrt 路由器（推荐用于部署）

```bash
# 一键安装
wget -O /tmp/install.sh https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/install-openwrt.sh
chmod +x /tmp/install.sh
/tmp/install.sh

# 安装完成后访问
# http://路由器IP:8091/console/index.html
```

**支持的设备**:
- x86_64 软路由 (J4125/N5105 等)
- ARM64 设备 (树莓派 4/5 等)
- ARMv7 设备
- MIPS 设备

### 通用平台

```bash
# 下载 JAR 包
wget https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/ooder-nexus-2.0.0-preview.jar

# 运行
java -jar ooder-nexus-2.0.0-preview.jar

# 访问 http://localhost:8081/console/index.html
```

---

## 🚀 快速开始

### 1. 部署第一个节点

在 OpenWrt 路由器或 PC 上安装 ooderNexus，服务启动后自动成为网络中的第一个节点。

### 2. 配置网络发现

进入「网络管理」→「P2P 配置」，启用 UDP 广播发现，其他节点将自动加入网络。

### 3. 添加 OpenWrt 设备（Windows 版本）

进入「OpenWrt 管理」页面，添加您的路由器设备：
- 输入路由器 IP 地址
- 输入 SSH 用户名和密码
- 点击连接，开始管理

### 4. 发布 AI 技能

进入「技能中心」→「发布技能」，将本地 AI 能力发布到网络，其他节点即可调用。

### 5. 监控网络状态

通过「网络拓扑」页面，实时查看网络结构、节点状态、流量统计。

---

## 📊 系统要求

| 项目 | 最低要求 | 推荐配置 |
|------|----------|----------|
| **操作系统** | Windows 10/11 / Linux / macOS / OpenWrt | Windows 11 / OpenWrt 23.05+ |
| **Java** | 8 | 11+ |
| **内存** | 64MB | 128MB+ |
| **存储** | 100MB | 256MB+ |
| **网络** | 局域网 | 公网可达 |

---

## 🆕 OpenWrt 预览版特性

| 特性 | 说明 |
|------|------|
| **OpenWrt 自动检测** | 启动时自动识别 OpenWrt 系统 |
| **自动角色配置** | 自动设置为 `routeAgent` 角色 |
| **系统级集成** | CPU/内存/温度监控、网络配置管理 |
| **一键安装脚本** | 专用 OpenWrt 安装脚本 |
| **Windows 安装包** | 一键启动，方便测试 |
| **P2P 网络** | 去中心化组网，节点自动发现 |
| **AI 技能管理** | 发布、分享、执行 AI 技能 |
| **VFS 文件系统** | 基于 MD5 哈希的虚拟文件系统 |
| **协议仿真** | MCP/Route 协议仿真与调试 |
| **网络拓扑可视化** | 图形化展示 P2P 网络结构 |
| **数据同步** | 自动同步到网络节点 |

---

## ⚠️ 预览版已知限制

1. **功能不完整**: 部分高级功能仍在开发中
2. **兼容性问题**: 仅测试了部分 OpenWrt 版本
3. **数据迁移**: 预览版数据可能不兼容正式版
4. **Mock 模式**: Windows 版本默认关闭 Mock，需要真实设备

---

## 🛡️ 安全与隐私

- **本地优先**: 所有数据本地存储，不上传云端
- **P2P 加密**: 节点间通信支持加密传输
- **访问控制**: 支持白名单/黑名单机制
- **权限管理**: 细粒度的技能执行权限控制

---

## 📖 文档与支持

- **完整文档**: https://github.com/oodercn/ooder-Nexus/blob/main/README.md
- **发布指南**: https://github.com/oodercn/ooder-Nexus/blob/main/RELEASE_COMMANDS.md
- **API 文档**: http://localhost:8081/swagger-ui.html (本地)
- **问题反馈**: https://github.com/oodercn/ooder-Nexus/issues
- **社区讨论**: https://github.com/oodercn/ooder-Nexus/discussions

---

## 🙏 致谢

感谢所有为 ooderNexus 做出贡献的开发者、测试者和用户！特别感谢：

- [Ooder Agent](https://github.com/oodercn/ooder-agent) - 底层 P2P 网络通信框架
- [Spring Boot](https://spring.io/projects/spring-boot) - 后端开发框架
- [OpenWrt](https://openwrt.org/) - 开源路由器操作系统

---

## 🔮 未来规划

### v2.1.0 (计划中)
- [ ] WebDAV 文件共享支持
- [ ] 容器化部署 (Docker)
- [ ] 多语言 SDK (Python/Go)
- [ ] 移动端 App

### v3.0.0 (规划中)
- [ ] 跨网络互联 (NAT 穿透)
- [ ] AI 模型仓库
- [ ] 分布式计算框架
- [ ] 企业级管理后台

---

## 📜 开源协议

本项目采用 **MIT 开源协议**，您可以自由使用、修改和分发。

```
Copyright (c) 2026 ooder Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 🤝 参与贡献

我们欢迎各种形式的贡献：

- 🐛 提交 Bug 报告
- 💡 提出功能建议
- 📝 完善文档
- 🔧 提交代码修复
- 🌍 翻译本地化

**贡献指南**: https://github.com/oodercn/ooder-Nexus/blob/main/CONTRIBUTING.md

---

## 📞 联系我们

- **GitHub**: https://github.com/oodercn/ooder-Nexus
- **国内镜像**: https://gitee.com/ooderCN/ooder-nexus
- **邮箱**: ooder@ooder.cn

---

<p align="center">
  <b>ooderNexus - 让 AI 能力无处不在</b><br>
  <i>构建去中心化的 AI 能力共享网络</i><br><br>
  Made with ❤️ by ooder Team
</p>

---

**立即体验 ooderNexus v2.0.0 OpenWrt 预览版，让您的路由器变身智能中枢！**

[⬇️ 下载 Windows 安装包](https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/ooder-nexus-2.0.0-openwrt-preview-windows.zip) | [📖 查看文档](https://github.com/oodercn/ooder-Nexus) | [💬 加入社区](https://github.com/oodercn/ooder-Nexus/discussions)
