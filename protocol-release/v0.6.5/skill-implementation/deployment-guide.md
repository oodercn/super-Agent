# Skill 协议栈参考实现部署指南
## 1. 文档概述

本文档提供了 Ooder AI Bridge 协议 v0.6.3 中 Skill 协议栈参考实现的部署指南，包括部署前准备、部署方式、配置说明、验证步骤和维护指南等内容。
## 2. 部署前准备
### 2.1 系统要求

| 组件 | 最低配置 | 推荐配置 |
|------|----------|----------|
| CPU | 2 核 | 4 核或更多 |
| 内存 | 4 GB | 8 GB 或更多 |
| 磁盘 | 20 GB | 50 GB 或更多 |
| 网络 | 100 Mbps | 1 Gbps 或更多 |
| 操作系统 | Ubuntu 18.04+/CentOS 7+/Windows Server 2016+ | Ubuntu 20.04+/CentOS 8+/Windows Server 2019+ |

### 2.2 软件依赖

| 软件 | 版本 | 用途 | 安装指南 |
|------|------|------|----------|
| Python | 3.9+ | 运行时环境 | [Python 官方安装指南](https://www.python.org/downloads/) |

### 2.3 网络要求

| 端口 | 用途 | 协议 |
|------|------|------|
| 8000 | Skill API 端口 | HTTP/HTTPS |
| 22 | SSH 访问端口 | TCP |

## 3. 部署方式

### 3.1 本地部署

#### 3.1.1 安装依赖

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 Python 3.9
sudo apt install python3.9 python3.9-venv python3.9-dev -y
```

#### 3.1.2 创建虚拟环境

```bash
# 创建项目目录
mkdir -p ~/ooder-skill && cd ~/ooder-skill

# 创建虚拟环境
python3.9 -m venv venv

# 激活虚拟环境
source venv/bin/activate
```

#### 3.1.3 安装项目依赖

```bash
# 安装项目依赖
pip install -r requirements.txt
```

#### 3.1.4 配置环境变量

```bash
# 创建 .env 文件
cat > .env << EOF
APP_NAME=ooder-skill
APP_VERSION=0.6.3
APP_HOST=0.0.0.0
APP_PORT=8000

SECURITY_JWT_SECRET=your_jwt_secret_key_change_in_production
SECURITY_JWT_EXPIRY=3600
EOF
```

#### 3.1.5 启动服务

```bash
# 启动服务
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

### 3.3 云平台部署
#### 3.3.1 AWS 部署

1. **创建 EC2 实例**
   - 选择 Ubuntu 20.04 LTS 镜像
   - 实例类型：t3.medium 或更高
   - 配置安全组，开放 8000 和 22 端口

2. **安装依赖**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install python3.9 python3.9-venv python3.9-dev -y
   ```

3. **部署应用**
   - 上传项目代码到 EC2 实例
   - 按照本地部署步骤配置环境
   - 启动服务

#### 3.3.2 阿里云部署
1. **创建 ECS 实例**
   - 选择 Ubuntu 20.04 LTS 镜像
   - 实例类型：ecs.c5.large 或更高
   - 配置安全组，开放 8000 和 22 端口

2. **安装依赖**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install python3.9 python3.9-venv python3.9-dev -y
   ```

3. **部署应用**
   - 上传项目代码到 ECS 实例
   - 按照本地部署步骤配置环境
   - 启动服务

## 4. 配置说明

### 4.1 核心配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| APP_NAME | 应用名称 | ooder-skill |
| APP_VERSION | 应用版本 | 0.6.3 |
| APP_HOST | 应用监听地址 | 0.0.0.0 |
| APP_PORT | 应用监听端口 | 8000 |
| APP_DEBUG | 调试模式 | False |

### 4.2 安全配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| SECURITY_JWT_SECRET | JWT 密钥 | your_jwt_secret_key |
| SECURITY_JWT_EXPIRY | JWT 过期时间（秒） | 3600 |
| SECURITY_ALGORITHM | JWT 算法 | HS256 |

### 4.3 AI Bridge 配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| AI_BRIDGE_PROTOCOL_VERSION | 协议版本 | 0.6.3 |
| AI_BRIDGE_REGISTRY_URL | 注册中心地址 | http://localhost:8080/api/v1/skills/register |
| AI_BRIDGE_HEARTBEAT_INTERVAL | 心跳间隔（秒） | 30 |

## 5. 验证步骤

### 5.1 服务状态验证
```bash
# 检查服务是否运行
curl -X GET http://localhost:8000/health

# 预期响应
{
  "status": "healthy",
  "version": "0.6.3",
  "timestamp": "2026-01-23T00:00:00Z"
}
```

### 5.2 API 功能验证

```bash
# 创建 Skill
curl -X POST http://localhost:8000/api/v1/skills/ \
  -H "Content-Type: application/json" \
  -d '{"name": "weather-skill", "description": "Weather forecast skill", "version": "1.0.0", "endpoints": [{"url": "http://localhost:8001", "protocol": "http", "weight": 1}]}'

# 获取所有 Skill
curl -X GET http://localhost:8000/api/v1/skills/

# 获取特定 Skill
curl -X GET http://localhost:8000/api/v1/skills/{skill_id}
```

### 5.3 日志验证

```bash
# 查看应用日志
cat app.log
```

## 6. 维护指南

### 6.1 日志管理

```bash
# 日志旋转配置
cat > /etc/logrotate.d/ooder-skill << EOF
/var/log/ooder-skill/app.log {
    daily
    rotate 7
    compress
    delaycompress
    missingok
    notifempty
    create 644 root root
    postrotate
        systemctl restart ooder-skill
    endscript
}
EOF
```

### 6.2 性能监控

```bash
# 安装监控工具
sudo apt install prometheus-node-exporter grafana -y

# 配置 Prometheus
cat > /etc/prometheus/prometheus.yml << EOF
scrape_configs:
  - job_name: 'ooder-skill'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:8000']
EOF

# 启动监控服务
sudo systemctl start prometheus-node-exporter grafana
sudo systemctl enable prometheus-node-exporter grafana
```

### 6.3 故障排查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 服务无法启动 | 端口被占用 | 检查端口占用情况，修改配置文件中的端口 |
| API 响应错误 | 代码异常 | 查看应用日志，定位错误原因 |
| 性能下降 | 资源不足 | 增加服务器资源，优化代码 |

## 7. 升级指南

### 7.1 版本升级步骤

```bash
# 停止当前服务
systemctl stop ooder-skill

# 备份配置文件
cp .env .env.bak

# 获取最新代码
git pull origin main

# 安装新依赖
pip install -r requirements.txt

# 恢复配置文件
cp .env.bak .env

# 启动服务
systemctl start ooder-skill
```

## 8. 安全性建议
### 8.1 访问控制

- 使用防火墙限制访问 IP
- 关闭不必要的端口
- 配置 SSH 密钥认证，禁用密码登录
### 8.2 数据安全

- 使用加密传输（HTTPS）
- 敏感数据加密存储
- 定期备份数据
### 8.3 应用安全

- 定期更新依赖包
- 使用强密码策略
- 实现最小权限原则
- 定期进行安全扫描

## 9. 常见问题

### 9.1 服务启动失败

**问题描述**：服务无法启动，提示端口被占用？
**解决方案**：
```bash
# 检查端口占用情况
lsof -i :8000

# 停止占用端口的进程
kill -9 <PID>

# 修改应用端口
vi .env
# 将 APP_PORT=8000 改为其他端口
```

### 9.2 API 认证失败

**问题描述**：API 请求返回 401 Unauthorized？
**解决方案**：
```bash
# 检查 JWT 密钥是否正确
vi .env
# 确保 SECURITY_JWT_SECRET 配置正确

# 检查令牌是否过期
# 重新获取令牌或延长过期时间
```

---

**Ooder Technology Co., Ltd.**