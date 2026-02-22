# GitHub 发布操作指南

## 版本说明

当前发布版本: **v2.0.0-openwrt-preview** (OpenWrt 预览版)

---

## 步骤 1: 清理 Git 锁文件

在 Windows PowerShell 中执行：
```powershell
# 关闭所有使用 git 的程序
# 然后手动删除锁文件
cd E:\github\ooder-Nexus
Remove-Item -Force .git\index.lock
```

## 步骤 2: 添加文件到暂存区

```bash
cd E:/github/ooder-Nexus

# 添加修改的文件
git add README.md
git add RELEASE_COMMANDS.md
git add RELEASE_ANNOUNCEMENT.md

# 检查状态
git status
```

## 步骤 3: 提交代码

```bash
# 创建提交
git commit -m "release: v2.0.0-openwrt-preview - OpenWrt 预览版

主要更新：
- OpenWrt 路由器管理功能预览
- 支持路由器自动发现与连接
- 系统监控、网络配置、命令执行
- 默认关闭 Mock 模式，使用真实设备模式
- Windows 安装包可用

技术细节：
- 提供 Windows 启动脚本 (start.bat)
- 提供详细安装说明 (README-Preview.txt)
- 支持 OpenWrt 真实设备连接测试"
```

## 步骤 4: 推送到 GitHub

```bash
# 推送到 main 分支
git push origin main

# 创建标签
git tag -a v2.0.0-openwrt-preview -m "ooderNexus v2.0.0 OpenWrt 预览版 - Windows安装包"

# 推送标签
git push origin v2.0.0-openwrt-preview
```

## 步骤 5: 创建 GitHub Release

### 5.1 访问 GitHub Release 页面
打开浏览器访问：
```
https://github.com/oodercn/ooder-Nexus/releases/new
```

### 5.2 填写 Release 信息

**版本号**: `v2.0.0-openwrt-preview`

**标题**: `ooderNexus v2.0.0 OpenWrt 预览版 (Windows)`

**内容**:
```markdown
## 🎉 ooderNexus v2.0.0 OpenWrt 预览版

这是一个 OpenWrt 预览版本，专注于 OpenWrt 路由器设备管理功能。

### ✨ 主要新特性

#### OpenWrt 集成增强
- **自动角色检测**: 启动时自动检测 OpenWrt 系统，自动设置 Agent 角色为 `routeAgent`
- **深度系统集成**: 支持路由器系统状态监控、网络配置管理、IP 地址管理
- **一键安装脚本**: 提供 OpenWrt 专用安装脚本，自动完成环境配置

#### Windows 安装包
- **一键启动**: 提供 Windows 启动脚本，双击即可运行
- **真实设备模式**: 默认关闭 Mock 模式，连接真实 OpenWrt 设备
- **详细文档**: 包含完整的安装和使用说明

#### 网络管理功能
- **网络拓扑可视化**: 图形化展示 P2P 网络结构
- **链路管理**: 节点间链路创建、监控、断开
- **流量监控**: 实时网络流量统计和分析

### 📦 安装方式

#### Windows（推荐）
1. 下载 `ooder-nexus-2.0.0-openwrt-preview-windows.zip`
2. 解压到任意目录
3. 双击运行 `start.bat`
4. 访问 http://localhost:8081/console/index.html

#### OpenWrt 路由器
```bash
wget -O /tmp/install.sh https://github.com/oodercn/ooder-Nexus/releases/download/v2.0.0-openwrt-preview/install-openwrt.sh
chmod +x /tmp/install.sh
/tmp/install.sh
```

#### 通用平台
下载 `ooder-nexus-2.0.0-openwrt-preview.jar`，然后运行：
```bash
java -jar ooder-nexus-2.0.0-openwrt-preview.jar
```

### 📋 系统要求

- **操作系统**: Windows 10/11 / Linux / macOS / OpenWrt
- **Java**: 8 或更高版本
- **内存**: 最低 64MB，推荐 128MB+
- **存储**: 最低 100MB，推荐 256MB+

### ⚠️ 预览版声明

此版本为预览版，仅供测试和评估使用：
- 可能包含未完善的功能和已知问题
- 不建议在生产环境中使用
- 默认关闭 Mock 模式，需要真实 OpenWrt 设备进行测试

### 🔧 快速开始

1. 安装完成后访问 Web 控制台
2. 查看仪表盘了解系统状态
3. 在 OpenWrt 管理页面添加路由器设备
4. 开始使用 AI 技能和网络管理功能

### 📖 文档

详细文档请查看 [README.md](https://github.com/oodercn/ooder-Nexus/blob/main/README.md)

### 🙏 致谢

感谢所有贡献者和测试者的支持！
```

### 5.3 上传发布文件

点击 "Attach binaries by dropping them here or selecting them" 区域，上传以下文件：
- `release/windows/ooder-nexus-2.0.0-openwrt-preview-windows.zip`
- `release/windows/ooder-nexus-2.0.0-preview.jar`
- `release/windows/README-Preview.txt`

### 5.4 发布

- [x] 勾选 "This is a pre-release" （预览版）
- [ ] 不要勾选 "Set as the latest release"
- 点击 "Publish release"

## 步骤 6: 验证发布

1. 访问 `https://github.com/oodercn/ooder-Nexus/releases`
2. 确认 v2.0.0-openwrt-preview 版本已显示
3. 确认文件可以正常下载
4. 测试安装脚本是否可以正常执行

## 发布文件清单

| 文件 | 大小 | 说明 |
|------|------|------|
| ooder-nexus-2.0.0-openwrt-preview-windows.zip | 40.7 MB | Windows 安装包 |
| ooder-nexus-2.0.0-preview.jar | 44.7 MB | 可执行 JAR 包 |
| README-Preview.txt | 3.4 KB | 详细说明文档 |
| start.bat | 827 B | Windows 启动脚本 |

## 下载地址

- **GitHub Releases**: https://github.com/oodercn/ooder-Nexus/releases
- **Gitee 镜像**: https://gitee.com/ooderCN/ooder-nexus/releases

## 注意事项

1. 确保所有测试已通过
2. 确保 README 文档已更新
3. 确保版本号一致（代码、README、脚本）
4. 发布后验证下载链接可用
5. 预览版需要明确标注，避免用户误用于生产环境
