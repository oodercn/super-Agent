# Skill 协议栈参考实现部署指南

## 1. 文档概述

本文档提供了 Ooder AI Bridge 协议 v0.6.0 中 Skill 协议栈参考实现的部署指南，包括部署前准备、部署方式、配置说明、验证步骤和维护指南等内容。

## 2. 部署前准备

### 2.1 系统要求

| 组件 | 最低配置 | 推荐配置 |
|------|----------|----------|
| CPU | 2 核 | 4 核或更多 |
| 内存 | 4 GB | 8 GB 或更多 |
| 磁盘 | 20 GB | 50 GB 或更多 |
| 网络 | 100 Mbps | 1 Gbps 或更快 |
| 操作系统 | Ubuntu 18.04+/CentOS 7+/Windows Server 2016+ | Ubuntu 20.04+/CentOS 8+/Windows Server 2019+ |

### 2.2 软件依赖

| 软件 | 版本 | 用途 | 安装指南 |
|------|------|------|----------|
| Python | 3.9+ | 运行时环境 | [Python 官方安装指南](https://www.python.org/downloads/) |
| PostgreSQL | 14+ | 关系型数据库 | [PostgreSQL 安装指南](https://www.postgresql.org/download/) |
| Redis | 6+ | 缓存和会话管理 | [Redis 安装指南](https://redis.io/download/) |
| Docker | 20.10+ | 容器化部署 | [Docker 安装指南](https://docs.docker.com/get-docker/) |
| Docker Compose | 1.29+ | 容器编排 | [Docker Compose 安装指南](https://docs.docker.com/compose/install/) |

### 2.3 网络要求

| 端口 | 用途 | 协议 |
|------|------|------|
| 8000 | Skill API 端口 | HTTP/HTTPS |
| 5432 | PostgreSQL 端口 | TCP |
| 6379 | Redis 端口 | TCP |
| 22 | SSH 访问端口 | TCP |

## 3. 部署方式

### 3.1 本地部署

#### 3.1.1 安装依赖

```bash
# 更新系统包
sudo apt update && sudo apt upgrade -y

# 安装 Python 3.9
sudo apt install python3.9 python3.9-venv python3.9-dev -y

# 安装 PostgreSQL
sudo apt install postgresql postgresql-contrib -y

# 安装 Redis
sudo apt install redis-server -y
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

#### 3.1.4 配置数据库

```bash
# 登录 PostgreSQL
sudo -u postgres psql

# 创建数据库和用户
CREATE DATABASE skill_db;
CREATE USER skill_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE skill_db TO skill_user;
ALTER USER skill_user CREATEDB;

# 退出 PostgreSQL\q
```

#### 3.1.5 配置环境变量

```bash
# 创建 .env 文件
cat > .env << EOF
APP_NAME=ooder-skill
APP_VERSION=0.6.0
APP_HOST=0.0.0.0
APP_PORT=8000

DATABASE_URL=postgresql://skill_user:your_secure_password@localhost:5432/skill_db

SECURITY_JWT_SECRET=your_jwt_secret_key_change_in_production
SECURITY_JWT_EXPIRY=3600

REDIS_URL=redis://localhost:6379/0
EOF
```

#### 3.1.6 启动服务

```bash
# 初始化数据库
python -m app.database.init

# 启动服务
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

### 3.2 Docker 部署

#### 3.2.1 安装 Docker 和 Docker Compose

```bash
# 安装 Docker
sudo apt install docker.io -y

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version
```

#### 3.2.2 配置 Docker Compose

```yaml
# docker-compose.yml
version: "3.8"
services:
  skill-service:
    build: .
    ports:
      - "8000:8000"
    environment:
      - APP_NAME=ooder-skill
      - APP_VERSION=0.6.0
      - DATABASE_URL=postgresql://skill_user:your_secure_password@db:5432/skill_db
      - SECURITY_JWT_SECRET=your_jwt_secret_key_change_in_production
      - REDIS_URL=redis://redis:6379/0
    depends_on:
      - db
      - redis
    volumes:
      - .:/app
    restart: always

  db:
    image: postgres:14
    environment:
      - POSTGRES_USER=skill_user
      - POSTGRES_PASSWORD=your_secure_password
      - POSTGRES_DB=skill_db
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    restart: always

  redis:
    image: redis:6
    volumes:
      - redis_data:/data
    restart: always

volumes:
  postgres_data:
  redis_data:
```

#### 3.2.3 初始化数据库脚本

```sql
-- init.sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

#### 3.2.4 构建和启动

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

### 3.3 云平台部署

#### 3.3.1 AWS 部署

1. **创建 EC2 实例**
   - 选择 Ubuntu 20.04 LTS 镜像
   - 实例类型：t3.medium 或更高
   - 配置安全组，开放 8000、22 端口

2. **安装依赖**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install python3.9 python3.9-venv python3.9-dev postgresql redis-server -y
   ```

3. **部署应用**
   - 上传项目代码到 EC2 实例
   - 按照本地部署步骤配置环境
   - 启动服务

4. **使用 RDS PostgreSQL**（可选）
   - 创建 RDS PostgreSQL 实例
   - 配置安全组，允许 EC2 实例访问
   - 更新数据库连接字符串

#### 3.3.2 阿里云部署

1. **创建 ECS 实例**
   - 选择 Ubuntu 20.04 LTS 镜像
   - 实例类型：ecs.c5.large 或更高
   - 配置安全组，开放 8000、22 端口

2. **安装依赖**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install python3.9 python3.9-venv python3.9-dev postgresql redis-server -y
   ```

3. **部署应用**
   - 上传项目代码到 ECS 实例
   - 按照本地部署步骤配置环境
   - 启动服务

4. **使用 RDS PostgreSQL**（可选）
   - 创建 RDS PostgreSQL 实例
   - 配置白名单，允许 ECS 实例访问
   - 更新数据库连接字符串

## 4. 配置说明

### 4.1 核心配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| APP_NAME | 应用名称 | ooder-skill |
| APP_VERSION | 应用版本 | 0.6.0 |
| APP_HOST | 应用监听地址 | 0.0.0.0 |
| APP_PORT | 应用监听端口 | 8000 |
| APP_DEBUG | 调试模式 | False |

### 4.2 数据库配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| DATABASE_URL | 数据库连接字符串 | postgresql://user:password@localhost:5432/skill_db |
| DATABASE_POOL_SIZE | 连接池大小 | 10 |
| DATABASE_MAX_OVERFLOW | 最大连接数 | 20 |

### 4.3 安全配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| SECURITY_JWT_SECRET | JWT 密钥 | your_jwt_secret_key |
| SECURITY_JWT_EXPIRY | JWT 过期时间（秒） | 3600 |
| SECURITY_ALGORITHM | JWT 算法 | HS256 |

### 4.4 Redis 配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| REDIS_URL | Redis 连接字符串 | redis://localhost:6379/0 |
| REDIS_MAX_CONNECTIONS | 最大连接数 | 50 |

### 4.5 AI Bridge 配置

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| AI_BRIDGE_PROTOCOL_VERSION | 协议版本 | 0.6.0 |
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
  "version": "0.6.0",
  "timestamp": "2026-01-18T00:00:00Z"
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

### 5.3 数据库连接验证

```bash
# 登录 PostgreSQL
sudo -u postgres psql

# 查看 Skill 表
\c skill_db
SELECT * FROM skills;

# 退出 PostgreSQL\q
```

### 5.4 日志验证

```bash
# 查看应用日志
cat app.log

# 或使用 Docker Compose 查看日志
docker-compose logs -f skill-service
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

### 6.2 定期备份

```bash
# 数据库备份脚本
cat > backup_db.sh << EOF
#!/bin/bash

BACKUP_DIR="/var/backups/ooder-skill"
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份数据库
pg_dump -h localhost -U skill_user -d skill_db -F c > $BACKUP_DIR/skill_db_backup_$DATE.dump

# 保留最近 7 天的备份
find $BACKUP_DIR -name "*.dump" -mtime +7 -delete
EOF

# 赋予执行权限
chmod +x backup_db.sh

# 配置定时任务
crontab -e
# 添加以下行，每天凌晨 2 点执行备份
0 2 * * * /path/to/backup_db.sh
```

### 6.3 性能监控

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

### 6.4 故障排查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 服务无法启动 | 端口被占用 | 检查端口占用情况，修改配置文件中的端口 |
| 数据库连接失败 | 连接字符串错误 | 检查数据库连接字符串，确保数据库服务运行 |
| API 响应错误 | 代码异常 | 查看应用日志，定位错误原因 |
| 性能下降 | 资源不足 | 增加服务器资源，优化代码和数据库查询 |

## 7. 升级指南

### 7.1 版本升级步骤

```bash
# 停止当前服务
systemctl stop ooder-skill

# 备份数据
./backup_db.sh

# 备份配置文件
cp .env .env.bak

# 获取最新代码
git pull origin main

# 安装新依赖
pip install -r requirements.txt

# 更新数据库结构
python -m app.database.migrate

# 恢复配置文件
cp .env.bak .env

# 启动服务
systemctl start ooder-skill
```

### 7.2 Docker 升级步骤

```bash
# 停止当前服务
docker-compose down

# 备份数据
docker-compose exec db pg_dump -U skill_user -d skill_db -F c > skill_db_backup_$(date +%Y%m%d_%H%M%S).dump

# 获取最新代码
git pull origin main

# 重新构建镜像
docker-compose build

# 启动服务
docker-compose up -d
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
- 限制数据库用户权限

### 8.3 应用安全

- 定期更新依赖包
- 使用强密码策略
- 实现最小权限原则
- 定期进行安全扫描

## 9. 常见问题

### 9.1 服务启动失败

**问题描述**：服务无法启动，提示端口被占用。

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

### 9.2 数据库连接失败

**问题描述**：无法连接到 PostgreSQL 数据库。

**解决方案**：
```bash
# 检查 PostgreSQL 服务是否运行
systemctl status postgresql

# 启动 PostgreSQL 服务
systemctl start postgresql

# 检查数据库连接字符串
vi .env
# 确保 DATABASE_URL 配置正确
```

### 9.3 API 认证失败

**问题描述**：API 请求返回 401 Unauthorized。

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
