# Agent SkillCenter

基于 Spring Boot 的技能中心管理系统，支持技能的创建、管理、市场分发和 P2P 网络共享。

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 8](https://img.shields.io/badge/Java-8-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-green.svg)]()

## 功能特性

- **技能管理**: 创建、编辑、删除、执行技能
- **技能市场**: 浏览、搜索、下载、评分技能
- **P2P 网络**: 局域网内技能共享与发现
- **用户管理**: 多用户支持与权限控制
- **群组管理**: 技能分组与共享
- **执行管理**: 技能执行历史与状态监控
- **存储管理**: 技能存储、备份与恢复
- **系统管理**: 系统配置、健康检查、日志管理

## 环境要求

- Java 8 或更高版本
- Maven 3.6+

## 快速开始

```bash
# 克隆项目
git clone https://gitee.com/ooderCN/agent-skillcenter.git
cd agent-skillcenter

# 构建项目
mvn clean package -DskipTests

# 运行项目
java -jar target/agent-skillcenter-2.0.jar
```

### 访问应用

- 控制台: http://localhost:8081/skillcenter/console/index.html
- 默认端口: 8081

## 项目结构

```
agent-skillcenter/
├── src/main/java/net/ooder/skillcenter/    # Java 源代码
│   ├── controller/                         # REST API 控制器
│   ├── service/                            # 业务逻辑层
│   ├── model/                              # 数据模型
│   ├── market/                             # 技能市场
│   ├── p2p/                                # P2P 网络通信
│   ├── storage/                            # 存储管理
│   └── sdk/                                # SDK 包装器
├── src/main/resources/                     # 配置文件
│   ├── static/console/                     # 前端控制台
│   └── application.yml                     # 应用配置
├── docs/                                   # 文档
├── LICENSE                                 # MIT 许可证
└── README.md                               # 本文件
```

## 配置说明

### 应用配置 (application.yml)

```yaml
server:
  port: 8081
  servlet:
    context-path: /skillcenter

skillcenter:
  sdk:
    mode: mock  # mock 或 real
    agent-id: ${AGENT_ID:SuperAgent-Default}
    agent-name: ${AGENT_NAME:SuperAgent}
```

### 数据存储

技能数据存储在项目目录下的 `skillcenter/storage/` 文件夹中：
- `skill_listings.json` - 技能列表
- `skill_ratings.json` - 技能评分
- `skill_reviews.json` - 技能评价
- `groups.json` - 群组信息
- `executions.json` - 执行记录

## API 文档

API 基础路径: `/skillcenter/api`

### 主要接口

| 接口 | 路径 | 说明 |
|------|------|------|
| 技能列表 | GET /api/skills | 获取所有技能 |
| 技能详情 | GET /api/skills/{id} | 获取技能详情 |
| 创建技能 | POST /api/skills | 创建新技能 |
| 更新技能 | PUT /api/skills/{id} | 更新技能 |
| 删除技能 | DELETE /api/skills/{id} | 删除技能 |
| 执行技能 | POST /api/skills/{id}/execute | 执行技能 |
| 市场技能 | GET /api/market/skills | 获取市场技能 |
| 搜索技能 | GET /api/market/skills/search | 搜索技能 |

完整 API 文档请参考 [API规范文档.md](API规范文档.md)

## 许可证

本项目采用 [MIT 许可证](LICENSE) 开源。

```
MIT License

Copyright (c) 2024 Ooder Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进本项目。

## 联系方式

- 项目主页: https://gitee.com/ooderCN/agent-skillcenter
- 问题反馈: https://gitee.com/ooderCN/agent-skillcenter/issues

---

**注意**: 这是一个示例版本，主要用于演示和学习目的。
