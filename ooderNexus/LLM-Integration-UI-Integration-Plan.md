# ooderNexus LLM 集成 UI 页面整合方案

## 一、概述

本文档详细规划了 ooderNexus 项目中 LLM 集成相关的 UI 页面整合方案，采用本地 IDE bridge skills 完成的方式，并在结构设计上预留 SkillsCenter 的同步和管理接口。

## 二、UI 页面架构

### 2.1 整体结构

```
ooderNexus/
├── console/
│   ├── pages/
│   │   ├── llm-integration/
│   │   │   ├── skill-management.html
│   │   │   ├── skill-market.html
│   │   │   ├── skill-execution.html
│   │   │   ├── user-preferences.html
│   │   │   └── skill-debug.html
│   │   ├── skillcenter-sync/
│   │   │   ├── sync-dashboard.html
│   │   │   ├── skill-upload.html
│   │   │   ├── skill-download.html
│   │   │   ├── skill-categories.html
│   │   │   └── sync-status.html
│   │   └── storage/
│   │       ├── storage-management.html
│   │       ├── shared-skills.html
│   │       └── received-skills.html
```

### 2.2 页面导航

```
┌─────────────────────────────────────────────────────────┐
│                   ooderNexus 导航栏                  │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────────┐  │
│  │ LLM 集成    │──────▶│ SkillsCenter  │──────▶│ 存储管理     │  │
│  │              │      │ 同步         │      │              │  │
│  └──────────────┘      └──────────────┘      └──────────────┘  │
│         │                    │                    │            │
│         ▼                    ▼                    ▼            │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────────┐  │
│  │ 技能管理    │      │ 技能市场    │      │ 技能执行    │  │
│  └──────────────┘      └──────────────┘      └──────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## 三、LLM 集成页面设计

### 3.1 技能管理页面（skill-management.html）

#### 3.1.1 功能需求
- 展示已注册的技能列表
- 支持技能的启用/禁用
- 支持技能的编辑和删除
- 支持技能的版本管理
- 支持技能的调试和日志查看

#### 3.1.2 页面布局

```html
<div class="llm-integration-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>技能管理</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item active" data-page="skill-management">
                <i class="ri-apps-line"></i>
                <span>技能管理</span>
            </div>
            <div class="menu-item" data-page="skill-market">
                <i class="ri-store-2-line"></i>
                <span>技能市场</span>
            </div>
            <div class="menu-item" data-page="skill-execution">
                <i class="ri-play-circle-line"></i>
                <span>技能执行</span>
            </div>
            <div class="menu-item" data-page="user-preferences">
                <i class="ri-settings-3-line"></i>
                <span>用户偏好</span>
            </div>
            <div class="menu-item" data-page="skill-debug">
                <i class="ri-bug-line"></i>
                <span>技能调试</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能管理</h1>
            <div class="content-actions">
                <button class="btn btn-primary" onclick="showRegisterSkillModal()">
                    <i class="ri-add-line"></i>
                    注册技能
                </button>
                <button class="btn btn-secondary" onclick="refreshSkillList()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
            </div>
        </div>
        <div class="skill-list" id="skill-list">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

#### 3.1.3 功能实现

```javascript
class SkillManagement {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.filter = {
            category: '',
            keyword: '',
            status: 'all'
        };
    }

    async init() {
        await this.loadSkillList();
        this.bindEvents();
    }

    async loadSkillList() {
        const response = await this.llmAPI.getRegisteredSkills(this.filter);
        if (response.success) {
            this.skillList = response.data;
            this.renderSkillList();
        }
    }

    renderSkillList() {
        const skillList = document.getElementById('skill-list');
        let html = '';

        this.skillList.forEach(skill => {
            html += `<div class="skill-item" data-skill-id="${skill.skillId}">`;
            html += `<div class="skill-icon">`;
            html += `<i class="${skill.icon}"></i>`;
            html += `</div>`;
            html += `<div class="skill-info">`;
            html += `<h3 class="skill-name">${skill.skillName}</h3>`;
            html += `<p class="skill-description">${skill.description}</p>`;
            html += `<div class="skill-meta">`;
            html += `<span class="skill-version">版本：${skill.version}</span>`;
            html += `<span class="skill-status">状态：${this.getStatusText(skill.status)}</span>`;
            html += `<span class="skill-author">作者：${skill.author}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="skill-actions">`;
            html += `<button class="btn btn-sm btn-primary" onclick="executeSkill('${skill.skillId}')">`;
            html += `<i class="ri-play-line"></i>`;
            html += `执行</button>`;
            html += `<button class="btn btn-sm btn-secondary" onclick="editSkill('${skill.skillId}')">`;
            html += `<i class="ri-edit-line"></i>`;
            html += `编辑</button>`;
            html += `<button class="btn btn-sm btn-danger" onclick="deleteSkill('${skill.skillId}')">`;
            html += `<i class="ri-delete-bin-line"></i>`;
            html += `删除</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        skillList.innerHTML = html;
    }

    bindEvents() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });
    }

    async executeSkill(skillId) {
        const response = await this.llmAPI.executeSkill(skillId);
        if (response.success) {
            this.showSuccess('技能执行成功');
        } else {
            this.showError('技能执行失败');
        }
    }

    async editSkill(skillId) {
        const skill = this.skillList.find(s => s.skillId === skillId);
        if (skill) {
            this.showEditSkillModal(skill);
        }
    }

    async deleteSkill(skillId) {
        if (confirm('确定要删除该技能吗？')) {
            const response = await this.llmAPI.deleteSkill(skillId);
            if (response.success) {
                this.showSuccess('技能删除成功');
                await this.loadSkillList();
            } else {
                this.showError('技能删除失败');
            }
        }
    }

    getStatusText(status) {
        const statusMap = {
            'active': '启用',
            'disabled': '禁用',
            'error': '错误'
        };
        return statusMap[status] || status;
    }

    showSuccess(message) {
        this.showNotification(message, 'success');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    }
}
```

### 3.2 技能市场页面（skill-market.html）

#### 3.2.1 功能需求
- 展示可用的技能列表
- 支持技能的分类浏览
- 支持技能的搜索和过滤
- 支持技能的安装和卸载
- 支持技能的预览和详情查看

#### 3.2.2 页面布局

```html
<div class="llm-integration-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>技能市场</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="skill-management">
                <i class="ri-apps-line"></i>
                <span>技能管理</span>
            </div>
            <div class="menu-item active" data-page="skill-market">
                <i class="ri-store-2-line"></i>
                <span>技能市场</span>
            </div>
            <div class="menu-item" data-page="skill-execution">
                <i class="ri-play-circle-line"></i>
                <span>技能执行</span>
            </div>
            <div class="menu-item" data-page="user-preferences">
                <i class="ri-settings-3-line"></i>
                <span>用户偏好</span>
            </div>
            <div class="menu-item" data-page="skill-debug">
                <i class="ri-bug-line"></i>
                <span>技能调试</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能市场</h1>
            <div class="content-actions">
                <div class="search-box">
                    <input type="text" id="search-input" class="form-control" placeholder="搜索技能...">
                    <i class="ri-search-line search-icon"></i>
                </div>
                <div class="filter-box">
                    <select id="category-filter" class="form-control">
                        <option value="">所有分类</option>
                    </select>
                </div>
                <button class="btn btn-secondary" onclick="refreshSkillMarket()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
            </div>
        </div>
        <div class="skill-market-grid" id="skill-market-grid">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

#### 3.2.3 功能实现

```javascript
class SkillMarket {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.categories = [];
        this.filter = {
            category: '',
            keyword: ''
        };
    }

    async init() {
        await this.loadCategories();
        await this.loadSkillMarket();
        this.bindEvents();
    }

    async loadCategories() {
        const response = await this.llmAPI.getSkillCategories();
        if (response.success) {
            this.categories = response.data;
            this.renderCategories();
        }
    }

    renderCategories() {
        const categoryFilter = document.getElementById('category-filter');
        let html = '<option value="">所有分类</option>';
        this.categories.forEach(category => {
            html += `<option value="${category.id}">${category.name}</option>`;
        });
        categoryFilter.innerHTML = html;
    }

    async loadSkillMarket() {
        const response = await this.llmAPI.getSkillMarket(this.filter);
        if (response.success) {
            this.skillList = response.data;
            this.renderSkillMarket();
        }
    }

    renderSkillMarket() {
        const skillMarketGrid = document.getElementById('skill-market-grid');
        let html = '';

        this.skillList.forEach(skill => {
            html += `<div class="skill-card" data-skill-id="${skill.skillId}">`;
            html += `<div class="skill-card-icon">`;
            html += `<i class="${skill.icon}"></i>`;
            html += `</div>`;
            html += `<div class="skill-card-info">`;
            html += `<h3 class="skill-name">${skill.skillName}</h3>`;
            html += `<p class="skill-description">${skill.description}</p>`;
            html += `<div class="skill-card-meta">`;
            html += `<span class="skill-category">分类：${skill.category}</span>`;
            html += `<span class="skill-author">作者：${skill.author}</span>`;
            html += `<span class="skill-rating">评分：${skill.rating}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="skill-card-actions">`;
            html += `<button class="btn btn-primary" onclick="previewSkill('${skill.skillId}')">`;
            html += `<i class="ri-eye-line"></i>`;
            html += `预览</button>`;
            html += `<button class="btn btn-secondary" onclick="installSkill('${skill.skillId}')">`;
            html += `<i class="ri-download-line"></i>`;
            html += `安装</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        skillMarketGrid.innerHTML = html;
    }

    async installSkill(skillId) {
        const response = await this.llmAPI.installSkill(skillId);
        if (response.success) {
            this.showSuccess('技能安装成功');
        } else {
            this.showError('技能安装失败');
        }
    }

    async previewSkill(skillId) {
        const response = await this.llmAPI.getSkillDescription(skillId);
        if (response.success) {
            this.showSkillPreview(response.data);
        }
    }

    showSkillPreview(skill) {
        const modal = document.createElement('div');
        modal.className = 'modal skill-preview-modal';
        modal.innerHTML = `
            <div class="modal-content">
                <div class="modal-header">
                    <h3>${skill.skillName}</h3>
                    <button class="modal-close" onclick="this.closeModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="skill-preview-content">
                        <div class="skill-preview-icon">
                            <i class="${skill.icon}"></i>
                        </div>
                        <div class="skill-preview-info">
                            <h4>技能描述</h4>
                            <p>${skill.description}</p>
                            <h4>技能参数</h4>
                            <ul>
                                ${skill.parameters.map(param => `<li>${param.name}: ${param.description}</li>`).join('')}
                            </ul>
                            <h4>技能输出</h4>
                            <ul>
                                ${skill.outputs.map(output => `<li>${output.name}: ${output.description}</li>`).join('')}
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-primary" onclick="this.installSkill('${skill.skillId}')">安装技能</button>
                    <button class="btn btn-secondary" onclick="this.closeModal()">关闭</button>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    }

    closeModal() {
        document.querySelector('.skill-preview-modal').remove();
    }
}
```

### 3.3 技能执行页面（skill-execution.html）

#### 3.3.1 功能需求
- 展示技能执行历史
- 支持技能的执行和监控
- 支持执行结果的查看和下载
- 支持执行日志的查看和导出

#### 3.3.2 页面布局

```html
<div class="llm-integration-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>技能执行</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="skill-management">
                <i class="ri-apps-line"></i>
                <span>技能管理</span>
            </div>
            <div class="menu-item" data-page="skill-market">
                <i class="ri-store-2-line"></i>
                <span>技能市场</span>
            </div>
            <div class="menu-item active" data-page="skill-execution">
                <i class="ri-play-circle-line"></i>
                <span>技能执行</span>
            </div>
            <div class="menu-item" data-page="user-preferences">
                <i class="ri-settings-3-line"></i>
                <span>用户偏好</span>
            </div>
            <div class="menu-item" data-page="skill-debug">
                <i class="ri-bug-line"></i>
                <span>技能调试</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能执行历史</h1>
            <div class="content-actions">
                <button class="btn btn-secondary" onclick="refreshExecutionHistory()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
                <button class="btn btn-secondary" onclick="exportExecutionHistory()">
                    <i class="ri-download-line"></i>
                    导出
                </button>
            </div>
        </div>
        <div class="execution-history" id="execution-history">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

#### 3.3.3 功能实现

```javascript
class SkillExecution {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.executionHistory = [];
    }

    async init() {
        await this.loadExecutionHistory();
        this.bindEvents();
    }

    async loadExecutionHistory() {
        const response = await this.llmAPI.getExecutionHistory();
        if (response.success) {
            this.executionHistory = response.data;
            this.renderExecutionHistory();
        }
    }

    renderExecutionHistory() {
        const executionHistory = document.getElementById('execution-history');
        let html = '';

        this.executionHistory.forEach(execution => {
            html += `<div class="execution-item" data-execution-id="${execution.executionId}">`;
            html += `<div class="execution-header">`;
            html += `<div class="execution-skill">`;
            html += `<i class="${execution.skillIcon}"></i>`;
            html += `<span class="execution-skill-name">${execution.skillName}</span>`;
            html += `</div>`;
            html += `<div class="execution-info">`;
            html += `<span class="execution-time">${this.formatTime(execution.executeTime)}</span>`;
            html += `<span class="execution-status">${this.getStatusText(execution.status)}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="execution-result">`;
            html += `<h4>执行结果</h4>`;
            html += `<pre>${execution.result}</pre>`;
            html += `</div>`;
            html += `<div class="execution-actions">`;
            html += `<button class="btn btn-sm btn-secondary" onclick="viewExecutionLog('${execution.executionId}')">`;
            html += `<i class="ri-file-list-line"></i>`;
            html += `查看日志</button>`;
            html += `<button class="btn btn-sm btn-secondary" onclick="downloadExecutionResult('${execution.executionId}')">`;
            html += `<i class="ri-download-line"></i>`;
            html += `下载结果</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        executionHistory.innerHTML = html;
    }

    formatTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleString('zh-CN');
    }

    getStatusText(status) {
        const statusMap = {
            'running': '运行中',
            'completed': '已完成',
            'failed': '失败',
            'cancelled': '已取消'
        };
        return statusMap[status] || status;
    }
}
```

### 3.4 用户偏好页面（user-preferences.html）

#### 3.4.1 功能需求
- 展示用户偏好设置
- 支持技能的偏好配置
- 支持用户身份和 Token 管理
- 支持界面主题和语言设置

#### 3.4.2 页面布局

```html
<div class="llm-integration-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>用户偏好</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="skill-management">
                <i class="ri-apps-line"></i>
                <span>技能管理</span>
            </div>
            <div class="menu-item" data-page="skill-market">
                <i class="ri-store-2-line"></i>
                <span>技能市场</span>
            </div>
            <div class="menu-item" data-page="skill-execution">
                <i class="ri-play-circle-line"></i>
                <span>技能执行</span>
            </div>
            <div class="menu-item active" data-page="user-preferences">
                <i class="ri-settings-3-line"></i>
                <span>用户偏好</span>
            </div>
            <div class="menu-item" data-page="skill-debug">
                <i class="ri-bug-line"></i>
                <span>技能调试</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>用户偏好</h1>
            <div class="content-actions">
                <button class="btn btn-primary" onclick="savePreferences()">
                    <i class="ri-save-line"></i>
                    保存偏好
                </button>
                <button class="btn btn-secondary" onclick="resetPreferences()">
                    <i class="ri-refresh-line"></i>
                    重置默认
                </button>
            </div>
        </div>
        <div class="preferences-container">
            <div class="preference-section">
                <h3>用户身份</h3>
                <div class="form-group">
                    <label>用户 ID</label>
                    <input type="text" id="user-id" class="form-control" placeholder="输入用户 ID">
                </div>
                <div class="form-group">
                    <label>API Token</label>
                    <input type="password" id="api-token" class="form-control" placeholder="输入 API Token">
                </div>
            </div>
            <div class="preference-section">
                <h3>技能偏好</h3>
                <div class="form-group">
                    <label>默认技能分类</label>
                    <select id="default-category" class="form-control">
                        <option value="">无</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>技能排序</label>
                    <select id="skill-sort" class="form-control">
                        <option value="name">按名称</option>
                        <option value="date">按时间</option>
                        <option value="rating">按评分</option>
                    </select>
                </div>
            </div>
            <div class="preference-section">
                <h3>界面设置</h3>
                <div class="form-group">
                    <label>界面主题</label>
                    <select id="theme" class="form-control">
                        <option value="light">浅色</option>
                        <option value="dark">深色</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>语言</label>
                    <select id="language" class="form-control">
                        <option value="zh-CN">简体中文</option>
                        <option value="en-US">English</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
</div>
```

#### 3.4.3 功能实现

```javascript
class UserPreferences {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.preferences = {};
    }

    async init() {
        await this.loadPreferences();
        this.bindEvents();
    }

    async loadPreferences() {
        const response = await this.llmAPI.getUserPreferences();
        if (response.success) {
            this.preferences = response.data;
            this.renderPreferences();
        }
    }

    renderPreferences() {
        document.getElementById('user-id').value = this.preferences.userId || '';
        document.getElementById('api-token').value = this.preferences.apiToken || '';
        document.getElementById('default-category').value = this.preferences.defaultCategory || '';
        document.getElementById('skill-sort').value = this.preferences.skillSort || 'name';
        document.getElementById('theme').value = this.preferences.theme || 'light';
        document.getElementById('language').value = this.preferences.language || 'zh-CN';
    }

    async savePreferences() {
        const preferences = {
            userId: document.getElementById('user-id').value,
            apiToken: document.getElementById('api-token').value,
            defaultCategory: document.getElementById('default-category').value,
            skillSort: document.getElementById('skill-sort').value,
            theme: document.getElementById('theme').value,
            language: document.getElementById('language').value
        };

        const response = await this.llmAPI.updateUserPreferences(preferences);
        if (response.success) {
            this.showSuccess('偏好保存成功');
        } else {
            this.showError('偏好保存失败');
        }
    }

    async resetPreferences() {
        if (confirm('确定要重置为默认设置吗？')) {
            const response = await this.llmAPI.resetUserPreferences();
            if (response.success) {
                this.showSuccess('偏好重置成功');
                await this.loadPreferences();
            } else {
                this.showError('偏好重置失败');
            }
        }
    }
}
```

### 3.5 技能调试页面（skill-debug.html）

#### 3.5.1 功能需求
- 展示技能调试信息
- 支持技能的实时日志查看
- 支持技能的参数测试
- 支持技能的性能监控

#### 3.5.2 页面布局

```html
<div class="llm-integration-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>技能调试</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="skill-management">
                <i class="ri-apps-line"></i>
                <span>技能管理</span>
            </div>
            <div class="menu-item" data-page="skill-market">
                <i class="ri-store-2-line"></i>
                <span>技能市场</span>
            </div>
            <div class="menu-item" data-page="skill-execution">
                <i class="ri-play-circle-line"></i>
                <span>技能执行</span>
            </div>
            <div class="menu-item" data-page="user-preferences">
                <i class="ri-settings-3-line"></i>
                <span>用户偏好</span>
            </div>
            <div class="menu-item active" data-page="skill-debug">
                <i class="ri-bug-line"></i>
                <span>技能调试</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能调试</h1>
            <div class="content-actions">
                <button class="btn btn-secondary" onclick="clearLogs()">
                    <i class="ri-delete-bin-line"></i>
                    清除日志
                </button>
                <button class="btn btn-secondary" onclick="exportLogs()">
                    <i class="ri-download-line"></i>
                    导出日志
                </button>
            </div>
        </div>
        <div class="debug-container">
            <div class="debug-sidebar">
                <h3>技能列表</h3>
                <div class="debug-skill-list" id="debug-skill-list">
                    <div class="loading">加载中...</div>
                </div>
            </div>
            <div class="debug-main">
                <div class="debug-toolbar">
                    <div class="debug-tool">
                        <label>选择技能</label>
                        <select id="debug-skill-select" class="form-control">
                            <option value="">选择技能</option>
                        </select>
                    </div>
                    <div class="debug-tool">
                        <label>参数测试</label>
                        <input type="text" id="debug-params" class="form-control" placeholder="输入测试参数">
                    </div>
                    <button class="btn btn-primary" onclick="testSkill()">
                        <i class="ri-play-line"></i>
                        测试技能
                    </button>
                </div>
                </div>
                <div class="debug-log-container">
                    <h3>实时日志</h3>
                    <div class="debug-log" id="debug-log">
                        <div class="log-empty">暂无日志</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
```

#### 3.5.3 功能实现

```javascript
class SkillDebug {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.currentSkill = null;
        this.logs = [];
    }

    async init() {
        await this.loadSkillList();
        this.bindEvents();
    }

    async loadSkillList() {
        const response = await this.llmAPI.getRegisteredSkills();
        if (response.success) {
            this.skillList = response.data;
            this.renderSkillList();
        }
    }

    renderSkillList() {
        const debugSkillList = document.getElementById('debug-skill-list');
        let html = '';

        this.skillList.forEach(skill => {
            html += `<div class="debug-skill-item" data-skill-id="${skill.skillId}">`;
            html += `<i class="${skill.icon}"></i>`;
            html += `<span class="debug-skill-name">${skill.skillName}</span>`;
            html += `</div>`;
        });

        debugSkillList.innerHTML = html;
    }

    async testSkill() {
        const skillId = document.getElementById('debug-skill-select').value;
        const params = document.getElementById('debug-params').value;
        
        const response = await this.llmAPI.testSkill(skillId, params);
        if (response.success) {
            this.addLog('info', `技能测试成功：${response.data.result}`);
        } else {
            this.addLog('error', `技能测试失败：${response.message}`);
        }
    }

    addLog(level, message) {
        const logEntry = {
            timestamp: Date.now(),
            level: level,
            message: message
        };
        this.logs.unshift(logEntry);
        this.renderLogs();
    }

    renderLogs() {
        const debugLog = document.getElementById('debug-log');
        let html = '';

        this.logs.forEach(log => {
            const levelClass = `log-${log.level}`;
            const time = new Date(log.timestamp).toLocaleTimeString();
            html += `<div class="log-entry ${levelClass}">`;
            html += `<span class="log-time">[${time}]</span>`;
            html += `<span class="log-message">${log.message}</span>`;
            html += `</div>`;
        });

        debugLog.innerHTML = html;
    }

    async clearLogs() {
        if (confirm('确定要清除所有日志吗？')) {
            this.logs = [];
            this.renderLogs();
        }
    }

    async exportLogs() {
        const logText = this.logs.map(log => 
            `[${new Date(log.timestamp).toLocaleString()}] [${log.level.toUpperCase()}] ${log.message}`
        ).join('\n');
        
        const blob = new Blob([logText], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `debug-logs-${Date.now()}.txt`;
        a.click();
        URL.revokeObjectURL(url);
    }
}
```

## 四、SkillsCenter 同步页面设计

### 4.1 同步仪表盘页面（sync-dashboard.html）

#### 4.1.1 功能需求
- 展示同步状态概览
- 显示同步进度和统计
- 支持批量同步操作
- 支持同步任务的取消和管理

#### 4.1.2 页面布局

```html
<div class="skillcenter-sync-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>SkillsCenter 同步</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item active" data-page="sync-dashboard">
                <i class="ri-dashboard-line"></i>
                <span>同步仪表盘</span>
            </div>
            <div class="menu-item" data-page="skill-upload">
                <i class="ri-upload-line"></i>
                <span>技能上传</span>
            </div>
            <div class="menu-item" data-page="skill-download">
                <i class="ri-download-line"></i>
                <span>技能下载</span>
            </div>
            <div class="menu-item" data-page="skill-categories">
                <i class="ri-folder-line"></i>
                <span>技能分类</span>
            </div>
            <div class="menu-item" data-page="sync-status">
                <i class="ri-bar-chart-line"></i>
                <span>同步状态</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>SkillsCenter 同步仪表盘</h1>
            <div class="content-actions">
                <button class="btn btn-primary" onclick="showBatchSyncModal()">
                    <i class="ri-upload-cloud-line"></i>
                    批量同步
                </button>
                <button class="btn btn-secondary" onclick="refreshSyncStatus()">
                    <i class="ri-refresh-line"></i>
                    刷新状态
                </button>
            </div>
        </div>
        <div class="sync-dashboard">
            <div class="sync-stats">
                <div class="stat-card">
                    <div class="stat-icon ri-cloud-line"></div>
                    <div class="stat-info">
                        <h3>已同步技能</h3>
                        <p class="stat-value" id="synced-count">0</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon ri-time-line"></div>
                    <div class="stat-info">
                        <h3>最后同步时间</h3>
                        <p class="stat-value" id="last-sync-time">从未同步</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon ri-error-warning-line"></div>
                    <div class="stat-info">
                        <h3>同步失败</h3>
                        <p class="stat-value" id="sync-failed-count">0</p>
                    </div>
                </div>
            </div>
            <div class="sync-tasks">
                <h3>同步任务</h3>
                <div class="task-list" id="sync-task-list">
                    <div class="empty-state">暂无同步任务</div>
                </div>
            </div>
        </div>
    </div>
</div>
```

### 4.2 技能上传页面（skill-upload.html）

#### 4.2.1 功能需求
- 支持单个和批量技能上传
- 支持技能包的拖拽上传
- 显示上传进度和状态
- 支持上传任务的管理和取消

#### 4.2.2 页面布局

```html
<div class="skillcenter-sync-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>SkillsCenter 同步</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="sync-dashboard">
                <i class="ri-dashboard-line"></i>
                <span>同步仪表盘</span>
            </div>
            <div class="menu-item active" data-page="skill-upload">
                <i class="ri-upload-line"></i>
                <span>技能上传</span>
            </div>
            <div class="menu-item" data-page="skill-download">
                <i class="ri-download-line"></i>
                <span>技能下载</span>
            </div>
            <div class="menu-item" data-page="skill-categories">
                <i class="ri-folder-line"></i>
                <span>技能分类</span>
            </div>
            <div class="menu-item" data-page="sync-status">
                <i class="ri-bar-chart-line"></i>
                <span>同步状态</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能上传</h1>
            <div class="content-actions">
                <button class="btn btn-primary" onclick="showUploadModal()">
                    <i class="ri-add-line"></i>
                    添加技能
                </button>
                <button class="btn btn-secondary" onclick="clearUploadHistory()">
                    <i class="ri-delete-bin-line"></i>
                    清除历史
                </button>
            </div>
        </div>
        <div class="upload-area">
            <div class="upload-drop-zone" id="upload-drop-zone">
                <div class="upload-icon ri-upload-cloud-line"></div>
                <p class="upload-text">拖拽技能包到此处</p>
                <input type="file" id="file-input" class="file-input" multiple accept=".zip,.jar">
            </div>
            <div class="upload-progress" id="upload-progress">
                <div class="progress-bar">
                    <div class="progress-fill" style="width: 0%"></div>
                </div>
                <div class="progress-text">0%</div>
            </div>
        </div>
        <div class="upload-history">
            <h3>上传历史</h3>
            <div class="history-list" id="upload-history-list">
                <div class="empty-state">暂无上传历史</div>
            </div>
        </div>
    </div>
</div>
```

### 4.3 技能下载页面（skill-download.html）

#### 4.3.1 功能需求
- 展示 SkillsCenter 中的技能列表
- 支持技能的搜索和过滤
- 支持技能的下载和安装
- 显示下载进度和状态

#### 4.3.2 页面布局

```html
<div class="skillcenter-sync-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>SkillsCenter 同步</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="sync-dashboard">
                <i class="ri-dashboard-line"></i>
                <span>同步仪表盘</span>
            </div>
            <div class="menu-item" data-page="skill-upload">
                <i class="ri-upload-line"></i>
                <span>技能上传</span>
            </div>
            <div class="menu-item active" data-page="skill-download">
                <i class="ri-download-line"></i>
                <span>技能下载</span>
            </div>
            <div class="menu-item" data-page="skill-categories">
                <i class="ri-folder-line"></i>
                <span>技能分类</span>
            </div>
            <div class="menu-item" data-page="sync-status">
                <i class="ri-bar-chart-line"></i>
                <span>同步状态</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能下载</h1>
            <div class="content-actions">
                <div class="search-box">
                    <input type="text" id="search-input" class="form-control" placeholder="搜索技能...">
                    <i class="ri-search-line search-icon"></i>
                </div>
                <div class="filter-box">
                    <select id="category-filter" class="form-control">
                        <option value="">所有分类</option>
                    </select>
                </div>
                <button class="btn btn-secondary" onclick="refreshSkillList()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
            </div>
        </div>
        <div class="skill-list" id="skill-list">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

### 4.4 技能分类页面（skill-categories.html）

#### 4.4.1 功能需求
- 展示技能分类列表
- 支持分类的创建、编辑和删除
- 支持分类的排序和搜索
- 显示每个分类下的技能数量

#### 4.4.2 页面布局

```html
<div class="skillcenter-sync-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>SkillsCenter 同步</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="sync-dashboard">
                <i class="ri-dashboard-line"></i>
                <span>同步仪表盘</span>
            </div>
            <div class="menu-item" data-page="skill-upload">
                <i class="ri-upload-line"></i>
                <span>技能上传</span>
            </div>
            <div class="menu-item" data-page="skill-download">
                <i class="ri-download-line"></i>
                <span>技能下载</span>
            </div>
            <div class="menu-item active" data-page="skill-categories">
                <i class="ri-folder-line"></i>
                <span>技能分类</span>
            </div>
            <div class="menu-item" data-page="sync-status">
                <i class="ri-bar-chart-line"></i>
                <span>同步状态</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>技能分类</h1>
            <div class="content-actions">
                <button class="btn btn-primary" onclick="showCreateCategoryModal()">
                    <i class="ri-add-line"></i>
                    创建分类
                </button>
                <button class="btn btn-secondary" onclick="refreshCategories()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
            </div>
        </div>
        <div class="category-list" id="category-list">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

### 4.5 同步状态页面（sync-status.html）

#### 4.5.1 功能需求
- 展示同步任务列表
- 显示同步进度和状态
- 支持同步任务的取消和重试
- 显示同步日志和错误信息

#### 4.5.2 页面布局

```html
<div class="skillcenter-sync-container">
    <div class="sidebar">
        <div class="sidebar-header">
            <h2>SkillsCenter 同步</h2>
        </div>
        <div class="sidebar-menu">
            <div class="menu-item" data-page="sync-dashboard">
                <i class="ri-dashboard-line"></i>
                <span>同步仪表盘</span>
            </div>
            <div class="menu-item" data-page="skill-upload">
                <i class="ri-upload-line"></i>
                <span>技能上传</span>
            </div>
            <div class="menu-item" data-page="skill-download">
                <i class="ri-download-line"></i>
                <span>技能下载</span>
            </div>
            <div class="menu-item" data-page="skill-categories">
                <i class="ri-folder-line"></i>
                <span>技能分类</span>
            </div>
            <div class="menu-item active" data-page="sync-status">
                <i class="ri-bar-chart-line"></i>
                <span>同步状态</span>
            </div>
        </div>
    </div>
    <div class="main-content">
        <div class="content-header">
            <h1>同步状态</h1>
            <div class="content-actions">
                <button class="btn btn-secondary" onclick="refreshSyncStatus()">
                    <i class="ri-refresh-line"></i>
                    刷新
                </button>
                <button class="btn btn-secondary" onclick="cancelAllSync()">
                    <i class="ri-close-line"></i>
                    取消全部
                </button>
            </div>
        </div>
        <div class="sync-status-list" id="sync-status-list">
            <div class="loading">加载中...</div>
        </div>
    </div>
</div>
```

## 五、API 封装设计

### 5.1 LLM 集成 API 封装（llm-integration-api.js）

```javascript
class LLMIntegrationAPI {
    constructor() {
        this.baseURL = '/api/skillbridge';
        this.cache = new Map();
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            method: options.method || 'GET',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        if (config.method !== 'GET' && options.body) {
            config.body = JSON.stringify(options.body);
        }

        try {
            const response = await fetch(url, config);
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || '请求失败');
            }
            
            return data;
        } catch (error) {
            console.error('API 调用错误:', error);
            throw error;
        }
    }

    async registerSkill(skillData) {
        return await this.request('/register', {
            method: 'POST',
            body: skillData
        });
    }

    async discoverSkills(category, keyword) {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        if (keyword) params.append('keyword', keyword);
        
        return await this.request(`/discover?${params.toString()}`);
    }

    async executeSkill(skillId, params) {
        return await this.request(`/execute/${skillId}`, {
            method: 'POST',
            body: params
        });
    }

    async getUserPreferences(userId) {
        return await this.request(`/preferences?userId=${userId}`);
    }

    async updateUserPreferences(preferences) {
        return await this.request('/preferences', {
            method: 'PUT',
            body: preferences
        });
    }

    async analyzeContext(contextData) {
        return await this.request('/context', {
            method: 'POST',
            body: contextData
        });
    }

    async getSkillDescription(skillId) {
        return await this.request(`/skill/${skillId}/description`);
    }

    async getRegisteredSkills(filter) {
        const params = new URLSearchParams();
        if (filter.category) params.append('category', filter.category);
        if (filter.keyword) params.append('keyword', filter.keyword);
        if (filter.status) params.append('status', filter.status);
        
        return await this.request(`/registered?${params.toString()}`);
    }

    async deleteSkill(skillId) {
        return await this.request(`/skill/${skillId}`, {
            method: 'DELETE'
        });
    }

    async updateSkill(skillId, skillData) {
        return await this.request(`/skill/${skillId}`, {
            method: 'PUT',
            body: skillData
        });
    }

    async testSkill(skillId, params) {
        return await this.request(`/test/${skillId}`, {
            method: 'POST',
            body: params
        });
    }

    async getExecutionHistory() {
        return await this.request('/execution/history');
    }

    async getExecutionResult(executionId) {
        return await this.request(`/execution/result/${executionId}`);
    }

    async getExecutionLog(executionId) {
        return await this.request(`/execution/log/${executionId}`);
    }

    clearCache() {
        this.cache.clear();
    }

    clearCacheKey(key) {
        this.cache.delete(key);
    }
}
```

### 5.2 SkillsCenter 同步 API 封装（skillcenter-sync-api.js）

```javascript
class SkillCenterSyncAPI {
    constructor() {
        this.baseURL = '/api/skillcenter/sync';
        this.cache = new Map();
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            method: options.method || 'GET',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        if (config.method !== 'GET' && options.body) {
            config.body = JSON.stringify(options.body);
        }

        try {
            const response = await fetch(url, config);
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || '请求失败');
            }
            
            return data;
        } catch (error) {
            console.error('API 调用错误:', error);
            throw error;
        }
    }

    async uploadSkill(skillData) {
        const formData = new FormData();
        formData.append('file', skillData.file);
        formData.append('metadata', JSON.stringify(skillData.metadata));

        return await this.request('/upload', {
            method: 'POST',
            body: formData,
            headers: {}
        });
    }

    async downloadSkill(skillId) {
        return await this.request(`/download/${skillId}`);
    }

    async getSkillList(category, page, size) {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        if (page) params.append('page', page);
        if (size) params.append('size', size);
        
        return await this.request(`/list?${params.toString()}`);
    }

    async updateSkillVersion(skillId, versionData) {
        return await this.request(`/version/${skillId}`, {
            method: 'PUT',
            body: versionData
        });
    }

    async deleteSkill(skillId) {
        return await this.request(`/${skillId}`, {
            method: 'DELETE'
        });
    }

    async searchSkills(keyword, category) {
        const params = new URLSearchParams();
        if (keyword) params.append('keyword', keyword);
        if (category) params.append('category', category);
        
        return await this.request(`/search?${params.toString()}`);
    }

    async getCategories() {
        return await this.request('/categories');
    }

    async createCategory(categoryData) {
        return await this.request('/category', {
            method: 'POST',
            body: categoryData
        });
    }

    async updateCategory(categoryId, categoryData) {
        return await this.request(`/category/${categoryId}`, {
            method: 'PUT',
            body: categoryData
        });
    }

    async deleteCategory(categoryId) {
        return await this.request(`/category/${categoryId}`, {
            method: 'DELETE'
        });
    }

    async batchSyncSkills(skillList) {
        return await this.request('/batch', {
            method: 'POST',
            body: { skills: skillList }
        });
    }

    async getSyncStatus() {
        return await this.request('/status');
    }

    async cancelSync(batchId) {
        return await this.request('/cancel', {
            method: 'POST',
            body: { batchId }
        });
    }

    clearCache() {
        this.cache.clear();
    }

    clearCacheKey(key) {
        this.cache.delete(key);
    }
}
```

## 六、样式统一

### 6.1 CSS 样式规范

```css
/* LLM 集成页面样式 */
.llm-integration-container {
    display: flex;
    height: 100vh;
    background: #f5f5f5;
}

.llm-integration-container .sidebar {
    width: 250px;
    background: #ffffff;
    border-right: 1px solid #e0e0e0;
    display: flex;
    flex-direction: column;
}

.llm-integration-container .sidebar-header {
    padding: 20px;
    border-bottom: 1px solid #e0e0e0;
}

.llm-integration-container .sidebar-header h2 {
    margin: 0;
    font-size: 18px;
    color: #333;
}

.llm-integration-container .sidebar-menu {
    flex: 1;
    overflow-y: auto;
}

.llm-integration-container .menu-item {
    padding: 15px 20px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 10px;
    border-bottom: 1px solid #f0f0f0;
    transition: all 0.3s;
}

.llm-integration-container .menu-item:hover {
    background: #f5f5f5;
}

.llm-integration-container .menu-item.active {
    background: #e6f7ff;
    color: #ffffff;
}

.llm-integration-container .main-content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
}

.llm-integration-container .content-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.llm-integration-container .content-header h1 {
    margin: 0;
    font-size: 24px;
    color: #333;
}

.llm-integration-container .content-actions {
    display: flex;
    gap: 10px;
}

/* 技能卡片样式 */
.skill-card {
    background: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    transition: all 0.3s;
}

.skill-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}

.skill-card-icon {
    font-size: 48px;
    color: #e6f7ff;
    margin-bottom: 10px;
}

.skill-card-info {
    margin-bottom: 15px;
}

.skill-card-info h3 {
    margin: 0 0 10px;
    font-size: 18px;
    color: #333;
}

.skill-card-info p {
    margin: 0 0 10px;
    color: #666;
    font-size: 14px;
}

.skill-card-meta {
    display: flex;
    gap: 15px;
    font-size: 12px;
    color: #999;
}

.skill-card-actions {
    display: flex;
    gap: 10px;
}

/* 技能列表样式 */
.skill-item {
    background: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    gap: 15px;
}

.skill-item:hover {
    background: #f5f5f5;
}

.skill-icon {
    font-size: 32px;
    color: #e6f7ff;
}

.skill-info {
    flex: 1;
}

.skill-name {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 0;
}

.skill-description {
    font-size: 14px;
    color: #666;
    margin: 5px 0;
}

.skill-meta {
    display: flex;
    gap: 10px;
    font-size: 12px;
    color: #999;
}

.skill-actions {
    display: flex;
    gap: 8px;
}

/* 调试页面样式 */
.debug-container {
    display: flex;
    height: calc(100vh - 80px);
}

.debug-sidebar {
    width: 300px;
    border-right: 1px solid #e0e0e0;
    overflow-y: auto;
}

.debug-main {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.debug-toolbar {
    padding: 15px;
    border-bottom: 1px solid #e0e0e0;
    background: #f9f9f9;
}

.debug-tool {
    margin-bottom: 10px;
}

.debug-tool label {
    display: block;
    margin-bottom: 5px;
    font-size: 14px;
    color: #666;
}

.debug-log-container {
    flex: 1;
    overflow-y: auto;
}

.debug-log {
    background: #1e1e1e1;
    padding: 15px;
    font-family: 'Courier New', monospace;
    font-size: 12px;
    color: #d4d4d4;
    min-height: 300px;
}

.log-entry {
    padding: 5px 0;
    border-bottom: 1px solid #333;
}

.log-info {
    color: #2196f3;
}

.log-warning {
    color: #f59e0b;
}

.log-error {
    color: #f44336;
}

/* SkillsCenter 同步页面样式 */
.skillcenter-sync-container {
    display: flex;
    height: 100vh;
    background: #f5f5f5;
}

.sync-stats {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
}

.stat-card {
    background: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 15px;
}

.stat-icon {
    font-size: 36px;
    color: #e6f7ff;
}

.stat-info h3 {
    margin: 0;
    font-size: 14px;
    color: #666;
}

.stat-info p {
    margin: 5px 0 0;
    font-size: 24px;
    font-weight: 600;
    color: #333;
}

.sync-tasks {
    background: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 20px;
}

.task-list {
    min-height: 200px;
}

.task-item {
    padding: 15px;
    border-bottom: 1px solid #f0f0f0;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.task-item:hover {
    background: #f5f5f5;
}

/* 上传区域样式 */
.upload-area {
    background: #ffffff;
    border: 2px dashed #e0e0e0;
    border-radius: 8px;
    padding: 40px;
    text-align: center;
    margin-bottom: 30px;
}

.upload-drop-zone {
    min-height: 200px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px dashed #e0e0e0;
    border-radius: 8px;
    background: #fafafa;
}

.upload-icon {
    font-size: 64px;
    color: #e6f7ff;
    margin-bottom: 15px;
}

.upload-text {
    font-size: 16px;
    color: #666;
}

.file-input {
    display: none;
}

.upload-progress {
    margin-top: 20px;
}

.progress-bar {
    height: 20px;
    background: #e0e0e0;
    border-radius: 10px;
    overflow: hidden;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #e6f7ff 0%, #42a5f5 100%);
    transition: width 0.3s;
}

.progress-text {
    text-align: center;
    font-size: 14px;
    color: #666;
    margin-top: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .llm-integration-container {
        flex-direction: column;
    }
    
    .llm-integration-container .sidebar {
        width: 100%;
        height: auto;
        border-right: none;
        border-bottom: 1px solid #e0e0e0;
    }
    
    .llm-integration-container .main-content {
        height: auto;
    }
    
    .debug-container {
        flex-direction: column;
        height: auto;
    }
    
    .debug-sidebar {
        width: 100%;
        border-right: none;
        border-bottom: 1px solid #e0e0e0;
    }
    
    .skillcenter-sync-container {
        flex-direction: column;
    }
}
```

## 七、总结

本 UI 页面整合方案详细规划了 ooderNexus 项目中 LLM 集成相关的 UI 页面整合方案，采用本地 IDE bridge skills 完成的方式，并在结构设计上预留了 SkillsCenter 的同步和管理接口。

主要特点：
1. **LLM 集成页面**：技能管理、技能市场、技能执行、用户偏好、技能调试
2. **SkillsCenter 同步页面**：同步仪表盘、技能上传、技能下载、技能分类、同步状态
3. **统一的样式规范**：使用统一的 CSS 类名、图标库、按钮样式
4. **完整的 API 封装**：LLM 集成 API 和 SkillsCenter 同步 API
5. **响应式设计**：支持多种屏幕尺寸和设备类型
6. **用户体验优化**：友好的用户界面、实时反馈、错误处理

该方案为 ooderNexus 项目提供了完整的 LLM 集成 UI 页面整合方案，支持本地 IDE bridge skills 完成，并预留了 SkillsCenter 的同步和管理接口。
