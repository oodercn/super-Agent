/**
 * 前端技能管理器
 * 负责管理前端技能的注册、发现和执行
 */
OOD.Skills = {
    /**
     * 技能注册表
     */
    skillRegistry: {},
    
    /**
     * 注册技能
     * @param {Object} skill 技能对象
     */
    registerSkill: function(skill) {
        if (skill && skill.id) {
            this.skillRegistry[skill.id] = skill;
            console.log('[Skills] 注册技能:', skill.name, '(', skill.id, ')');
        }
    },
    
    /**
     * 获取技能
     * @param {String} skillId 技能ID
     * @returns {Object} 技能对象
     */
    getSkill: function(skillId) {
        return this.skillRegistry[skillId];
    },
    
    /**
     * 获取所有技能
     * @returns {Array} 技能列表
     */
    getAllSkills: function() {
        return Object.values(this.skillRegistry);
    },
    
    /**
     * 根据类型获取技能
     * @param {String} type 技能类型
     * @returns {Array} 技能列表
     */
    getSkillsByType: function(type) {
        return Object.values(this.skillRegistry).filter(skill => skill.type === type);
    },
    
    /**
     * 执行技能
     * @param {String} skillId 技能ID
     * @param {Object} context 执行上下文
     * @returns {Promise} 执行结果
     */
    executeSkill: function(skillId, context) {
        const skill = this.getSkill(skillId);
        if (!skill) {
            return Promise.reject(new Error('技能不存在: ' + skillId));
        }
        
        if (skill.validate && !skill.validate(context)) {
            return Promise.reject(new Error('技能参数验证失败: ' + skillId));
        }
        
        console.log('[Skills] 执行技能:', skill.name, '(', skillId, ')');
        
        try {
            const result = skill.execute(context);
            return Promise.resolve(result);
        } catch (error) {
            console.error('[Skills] 技能执行失败:', error);
            return Promise.reject(error);
        }
    },
    
    /**
     * 初始化技能系统
     */
    init: function() {
        console.log('[Skills] 初始化技能系统');
        
        // 注册内置技能
        this.registerBuiltinSkills();
        
        console.log('[Skills] 技能系统初始化完成，注册了', Object.keys(this.skillRegistry).length, '个技能');
    },
    
    /**
     * 注册内置技能
     */
    registerBuiltinSkills: function() {
        // 注册UI组件生成技能
        this.registerSkill({
            id: 'ui-component-generator',
            name: 'UI组件生成器',
            version: '1.0.0',
            description: '根据配置生成UI组件代码',
            type: 'frontend',
            tags: ['ui', 'component', 'generator'],
            
            validate: function(context) {
                return context && context.componentType;
            },
            
            execute: function(context) {
                const { componentType, props } = context;
                
                switch (componentType) {
                    case 'button':
                        return this.generateButton(props);
                    case 'input':
                        return this.generateInput(props);
                    case 'form':
                        return this.generateForm(props);
                    default:
                        throw new Error('不支持的组件类型: ' + componentType);
                }
            },
            
            generateButton: function(props) {
                const {
                    id = 'btn' + Math.floor(Math.random() * 1000),
                    text = '按钮',
                    type = 'primary',
                    size = 'medium',
                    onClick = 'console.log("按钮点击")'
                } = props || {};
                
                return {
                    html: `<button id="${id}" class="ood-button ood-button-${type} ood-button-${size}">${text}</button>`,
                    js: `
                        OOD.ready(function() {
                            OOD('#${id}').on('click', function() {
                                ${onClick};
                            });
                        });
                    `
                };
            },
            
            generateInput: function(props) {
                const {
                    id = 'input' + Math.floor(Math.random() * 1000),
                    label = '输入框',
                    placeholder = '请输入',
                    value = '',
                    onChange = 'console.log("输入变化", this.value)'
                } = props || {};
                
                return {
                    html: `
                        <div class="ood-form-item">
                            <label for="${id}">${label}</label>
                            <input id="${id}" type="text" class="ood-input" placeholder="${placeholder}" value="${value}">
                        </div>
                    `,
                    js: `
                        OOD.ready(function() {
                            OOD('#${id}').on('change', function() {
                                ${onChange};
                            });
                        });
                    `
                };
            },
            
            generateForm: function(props) {
                const {
                    id = 'form' + Math.floor(Math.random() * 1000),
                    title = '表单',
                    fields = [],
                    onSubmit = 'console.log("表单提交")'
                } = props || {};
                
                let fieldsHtml = '';
                fields.forEach((field, index) => {
                    const fieldId = field.id || `field${index}`;
                    fieldsHtml += `
                        <div class="ood-form-item">
                            <label for="${fieldId}">${field.label || '字段' + (index + 1)}</label>
                            <input id="${fieldId}" type="${field.type || 'text'}" class="ood-input" placeholder="${field.placeholder || ''}" value="${field.value || ''}">
                        </div>
                    `;
                });
                
                return {
                    html: `
                        <div class="ood-panel">
                            <div class="ood-panel-header">
                                <h3>${title}</h3>
                            </div>
                            <div class="ood-panel-body">
                                <form id="${id}" class="ood-form">
                                    ${fieldsHtml}
                                    <div class="ood-form-item">
                                        <button type="submit" class="ood-button ood-button-primary">提交</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    `,
                    js: `
                        OOD.ready(function() {
                            OOD('#${id}').on('submit', function(e) {
                                e.preventDefault();
                                ${onSubmit};
                            });
                        });
                    `
                };
            }
        });
        
        // 注册布局生成技能
        this.registerSkill({
            id: 'layout-generator',
            name: '布局生成器',
            version: '1.0.0',
            description: '生成页面布局代码',
            type: 'frontend',
            tags: ['layout', 'generator'],
            
            validate: function(context) {
                return context && context.layoutType;
            },
            
            execute: function(context) {
                const { layoutType, sections } = context;
                
                switch (layoutType) {
                    case 'grid':
                        return this.generateGridLayout(sections);
                    case 'flex':
                        return this.generateFlexLayout(sections);
                    case 'responsive':
                        return this.generateResponsiveLayout(sections);
                    default:
                        throw new Error('不支持的布局类型: ' + layoutType);
                }
            },
            
            generateGridLayout: function(sections) {
                const id = 'grid-layout-' + Math.floor(Math.random() * 1000);
                
                let gridHtml = '';
                sections.forEach((section, index) => {
                    gridHtml += `
                        <div class="ood-grid-item" style="grid-column: ${section.column || 1}; grid-row: ${section.row || 1};">
                            <h4>${section.title || '区块' + (index + 1)}</h4>
                            <p>${section.content || ''}</p>
                        </div>
                    `;
                });
                
                return {
                    html: `
                        <div id="${id}" class="ood-grid-layout">
                            ${gridHtml}
                        </div>
                    `,
                    css: `
                        .ood-grid-layout {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                            gap: 20px;
                            padding: 20px;
                        }
                        
                        .ood-grid-item {
                            background-color: #f5f5f5;
                            padding: 15px;
                            border-radius: 4px;
                        }
                    `
                };
            },
            
            generateFlexLayout: function(sections) {
                const id = 'flex-layout-' + Math.floor(Math.random() * 1000);
                
                let flexHtml = '';
                sections.forEach((section, index) => {
                    flexHtml += `
                        <div class="ood-flex-item" style="flex: ${section.flex || 1};">
                            <h4>${section.title || '区块' + (index + 1)}</h4>
                            <p>${section.content || ''}</p>
                        </div>
                    `;
                });
                
                return {
                    html: `
                        <div id="${id}" class="ood-flex-layout">
                            ${flexHtml}
                        </div>
                    `,
                    css: `
                        .ood-flex-layout {
                            display: flex;
                            gap: 20px;
                            padding: 20px;
                            flex-wrap: wrap;
                        }
                        
                        .ood-flex-item {
                            background-color: #f5f5f5;
                            padding: 15px;
                            border-radius: 4px;
                            min-width: 200px;
                        }
                    `
                };
            },
            
            generateResponsiveLayout: function(sections) {
                const id = 'responsive-layout-' + Math.floor(Math.random() * 1000);
                
                let responsiveHtml = '';
                sections.forEach((section, index) => {
                    responsiveHtml += `
                        <div class="ood-responsive-item">
                            <h4>${section.title || '区块' + (index + 1)}</h4>
                            <p>${section.content || ''}</p>
                        </div>
                    `;
                });
                
                return {
                    html: `
                        <div id="${id}" class="ood-responsive-layout">
                            ${responsiveHtml}
                        </div>
                    `,
                    css: `
                        .ood-responsive-layout {
                            display: flex;
                            flex-direction: column;
                            gap: 20px;
                            padding: 20px;
                        }
                        
                        .ood-responsive-item {
                            background-color: #f5f5f5;
                            padding: 15px;
                            border-radius: 4px;
                        }
                        
                        @media (min-width: 768px) {
                            .ood-responsive-layout {
                                flex-direction: row;
                                flex-wrap: wrap;
                            }
                            
                            .ood-responsive-item {
                                flex: 1 1 calc(50% - 10px);
                            }
                        }
                        
                        @media (min-width: 1200px) {
                            .ood-responsive-item {
                                flex: 1 1 calc(33.333% - 13.333px);
                            }
                        }
                    `
                };
            }
        });
        
        // 注册主题切换技能
        this.registerSkill({
            id: 'theme-switcher',
            name: '主题切换器',
            version: '1.0.0',
            description: '切换应用主题',
            type: 'frontend',
            tags: ['theme', 'ui'],
            
            validate: function(context) {
                return context && context.theme;
            },
            
            execute: function(context) {
                const { theme } = context;
                
                // 应用主题
                document.body.setAttribute('data-theme', theme);
                
                // 保存主题设置
                localStorage.setItem('ooder-theme', theme);
                
                console.log('[Skills] 主题切换为:', theme);
                
                return {
                    success: true,
                    theme: theme,
                    message: '主题切换成功'
                };
            }
        });
    }
};

// 初始化技能系统
if (typeof OOD !== 'undefined') {
    OOD.ready(function() {
        OOD.Skills.init();
    });
}
