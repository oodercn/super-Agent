---
name: Ooder组件开发
version: 2.0.0
description: 提供Ooder框架FormLayout和TreeGrid两个核心组件的开发指南和生成工具
author: Ooder Team
category: 前端开发
tags: [ooder, 组件开发, 前端框架]
trigger: 当用户需要开发Ooder框架的FormLayout或TreeGrid组件时，或需要使用自然语言快速生成这些组件的代码时
---

# Ooder框架核心组件开发技能指南

## 文档目录

- [1. 使用场景](#使用场景)
- [2. 核心组件](#核心组件)
  - [2.1 FormLayout - 表单布局组件](#1-formlayout---表单布局组件)
    - [2.1.1 概述](#11-概述)
    - [2.1.2 特性](#12-特性)
    - [2.1.3 生成工具](#13-生成工具)
    - [2.1.4 详细文档](#14-详细文档)
  - [2.2 TreeGrid - 树形表格组件](#2-treegrid---树形表格组件)
    - [2.2.1 概述](#21-概述)
    - [2.2.2 特性](#22-特性)
    - [2.2.3 生成工具](#23-生成工具)
    - [2.2.4 详细文档](#24-详细文档)
- [3. 工具使用](#工具使用)
  - [3.1 生成工具目录](#1-生成工具目录)
  - [3.2 运行环境](#2-运行环境)
  - [3.3 使用步骤](#3-使用步骤)
- [4. 注意事项](#注意事项)

## 使用场景

本技能适用于以下场景：

1. **表单开发**：使用FormLayout组件构建各类表单
2. **列表开发**：使用TreeGrid组件创建数据列表
3. **快速开发**：使用自然语言快速生成组件代码

## 核心组件

### 1. FormLayout - 表单布局组件

#### 1.1 概述
- **组件名称**：ood.UI.FormLayout
- **功能描述**：提供强大的表单布局能力，支持复杂表单设计
- **适用场景**：各类表单开发，包括登录表单、注册表单、信息录入表单等
- **推荐指数**：★★★★★

#### 1.2 特性
- 灵活的表单布局管理
- 支持表单验证
- 支持移动端适配（通过MFormLayout）
- 丰富的表单控件集成

#### 1.3 生成工具
- **脚本文件**：`generateForm.js`
- **功能**：基于RAD设计的表单组件生成工具，生成符合Ooder框架规范的表单组件代码
- **参考模板**：JDSEasy RAD Studio生成的Form.js
- **使用方法**：
  ```bash
  # 基本用法
  node generateForm.js "类名：TestForm，标题：测试表单" TestForm.js
  
  # 自定义别名
  node generateForm.js "类名：TestForm，标题：测试表单，别名：TestForm" TestForm.js
  ```

#### 1.4 详细文档
- **文档路径**：[FormLayout组件专题](./docs/FormLayout.md)
- **文档内容**：包含组件概述、核心功能、使用场景、最佳实践、常见问题等

### 2. TreeGrid - 树形表格组件

#### 2.1 概述
- **组件名称**：ood.UI.TreeGrid
- **功能描述**：提供树形表格数据展示能力，支持列配置和数据管理
- **适用场景**：树形数据表格、产品列表、文件管理等
- **推荐指数**：★★★★★

#### 2.2 特性
- 支持树形表格数据展示
- 支持自定义列配置
- 支持数据排序和筛选
- 响应式设计
- 支持动态列生成

#### 2.3 生成工具
- **脚本文件**：`generateList.js`
- **功能**：基于RAD设计的列表组件生成工具，生成符合Ooder框架规范的列表组件代码
- **参考模板**：JDSEasy RAD Studio生成的Grid.js
- **使用方法**：
  ```bash
  # 基本用法
  node generateList.js "类名：TestList，标题：测试列表" TestList.js
  
  # 自定义别名和服务
  node generateList.js "类名：TestList，标题：测试列表，别名：Search，服务：net.ooder.test.TestService" TestList.js
  
  # 自定义列
  node generateList.js "类名：StudentSchedule，标题：学生课表，别名：Schedule，服务：net.ooder.test.CourseService，包含哪些列：学生ID,姓名，科目" TestStudentSchedule.js
  ```

#### 2.4 详细文档
- **文档路径**：[TreeGrid组件专题](./docs/TreeGrid.md)
- **文档内容**：包含组件概述、核心功能、使用场景、最佳实践、常见问题等

## 工具使用

### 1. 生成工具目录
```
ooder-skills/tools/
├── generateForm.js         # 表单组件生成工具（基于RAD设计）
└── generateList.js         # 列表组件生成工具（基于RAD设计）
```

### 2. 运行环境
- agent-rad  

### 3. 使用步骤
1. 打开命令行工具
2. 进入项目根目录
3. 运行相应的生成脚本
4. 根据提示输入自然语言描述
5. 生成组件代码文件
6. 根据实际需求修改代码

## 注意事项

1. 确保Ooder框架版本与技能包版本兼容
2. 生成的代码需要根据实际需求进行调整
3. 定期更新技能包，保持与Ooder框架同步
4. 遵循组件开发规范，确保代码质量
