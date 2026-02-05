// 模板管理模块 - 基于ooder-core的Template功能

// 初始化模板系统
function initTemplateSystem() {
    console.log('初始化模板系统...');
    
    // 这里可以添加模板系统的初始化代码
    // 例如加载预定义模板、初始化模板缓存等
}

// 创建模板
function createTemplate(templateString, properties, events, domId) {
    try {
        // 检查ooder对象是否存在
        if (typeof ood === 'undefined') {
            console.error('ooder核心库未加载，无法创建模板');
            return null;
        }
        
        // 创建模板实例
        const template = new ood.Template(templateString, properties, events, domId);
        return template;
    } catch (error) {
        console.error('创建模板失败:', error);
        return null;
    }
}

// 渲染模板
function renderTemplate(template, container) {
    try {
        if (!template) {
            console.error('模板对象不存在，无法渲染');
            return false;
        }
        
        if (!container) {
            console.error('容器对象不存在，无法渲染模板');
            return false;
        }
        
        // 渲染模板
        template.render();
        
        // 将模板添加到容器
        container.appendChild(template.getRootNode());
        
        return true;
    } catch (error) {
        console.error('渲染模板失败:', error);
        return false;
    }
}

// 销毁模板
function destroyTemplate(template) {
    try {
        if (template) {
            template.destroy();
            return true;
        }
        return false;
    } catch (error) {
        console.error('销毁模板失败:', error);
        return false;
    }
}

// 预定义模板
const predefinedTemplates = {
    // 技能列表项模板
    skillListItem: `
        <div class="skill-item" tpl_evkey="skill">
            <div class="skill-info">
                <h4>{name}</h4>
                <p>{description}</p>
                <div class="skill-meta">
                    <span class="skill-category">{category}</span>
                    <span class="skill-version">v{version}</span>
                </div>
            </div>
            <div class="skill-actions">
                <button class="btn btn-primary" [event]="execute">执行</button>
                <button class="btn btn-secondary" [event]="details">详情</button>
            </div>
        </div>
    `,
    
    // 市场技能卡片模板
    marketSkillCard: `
        <div class="market-skill-card" tpl_evkey="marketSkill">
            <div class="card-header">
                <h3>{name}</h3>
                <div class="skill-rating">
                    <i class="ri-star-fill"></i>
                    <span>{rating}</span>
                </div>
            </div>
            <div class="card-body">
                <p class="skill-description">{description}</p>
                <div class="skill-meta">
                    <span class="skill-author">{author}</span>
                    <span class="skill-downloads">{downloads} 下载</span>
                </div>
            </div>
            <div class="card-footer">
                <button class="btn btn-primary" [event]="download">下载</button>
                <button class="btn btn-secondary" [event]="view">查看</button>
            </div>
        </div>
    `,
    
    // 执行历史项模板
    executionHistoryItem: `
        <div class="execution-item" tpl_evkey="execution">
            <div class="execution-info">
                <h4>{skillName}</h4>
                <div class="execution-meta">
                    <span class="execution-time">{timestamp}</span>
                    <span class="execution-status {statusClass}">{status}</span>
                </div>
            </div>
            <div class="execution-actions">
                <button class="btn btn-secondary" [event]="view">查看详情</button>
                {retryButton}
            </div>
        </div>
    `,
    
    // 存储备份项模板
    storageBackupItem: `
        <div class="backup-item" tpl_evkey="backup">
            <div class="backup-info">
                <h4>备份 {id}</h4>
                <div class="backup-meta">
                    <span class="backup-time">{timestamp}</span>
                    <span class="backup-size">{size}</span>
                </div>
            </div>
            <div class="backup-actions">
                <button class="btn btn-primary" [event]="restore">恢复</button>
                <button class="btn btn-danger" [event]="delete">删除</button>
            </div>
        </div>
    `
};

// 获取预定义模板
function getPredefinedTemplate(templateName) {
    return predefinedTemplates[templateName] || null;
}

// 使用模板渲染数据
function renderWithTemplate(templateName, data, container, events) {
    try {
        // 获取模板
        const templateString = getPredefinedTemplate(templateName);
        if (!templateString) {
            console.error('未找到预定义模板:', templateName);
            return false;
        }
        
        // 创建模板
        const template = createTemplate(templateString, data, events);
        if (!template) {
            return false;
        }
        
        // 渲染模板
        const result = renderTemplate(template, container);
        return result;
    } catch (error) {
        console.error('使用模板渲染数据失败:', error);
        return false;
    }
}

// 模板缓存
const templateCache = {};

// 缓存模板
function cacheTemplate(templateId, template) {
    templateCache[templateId] = template;
}

// 获取缓存的模板
function getCachedTemplate(templateId) {
    return templateCache[templateId] || null;
}

// 清除模板缓存
function clearTemplateCache() {
    Object.keys(templateCache).forEach(key => {
        const template = templateCache[key];
        if (template) {
            destroyTemplate(template);
        }
        delete templateCache[key];
    });
}

// 导出模板管理模块
export {
    initTemplateSystem,
    createTemplate,
    renderTemplate,
    destroyTemplate,
    getPredefinedTemplate,
    renderWithTemplate,
    cacheTemplate,
    getCachedTemplate,
    clearTemplateCache,
    predefinedTemplates
};