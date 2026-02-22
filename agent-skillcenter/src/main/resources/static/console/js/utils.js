// 统一的工具函数文件

// API基础URL（使用条件声明避免重复）
if (typeof API_BASE_URL === 'undefined') {
    var API_BASE_URL = '/skillcenter/api';
}

// API调用工具函数（使用条件声明避免重复）
if (typeof api === 'undefined') {
    var api = {
        // GET请求
        get: async (endpoint, params = {}) => {
            try {
                // 构建查询字符串
                let queryString = '';
                const keys = Object.keys(params);
                if (keys.length > 0) {
                    queryString = '?' + keys.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`).join('&');
                }
                const url = `${API_BASE_URL}${endpoint}${queryString}`;
                
                const response = await fetch(url);
                const data = await response.json();
                return data;
            } catch (error) {
                console.error(`API GET Error (${endpoint}):`, error);
                throw error;
            }
        },
        
        // POST请求
        post: async (endpoint, data = {}) => {
            try {
                const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                return result;
            } catch (error) {
                console.error(`API POST Error (${endpoint}):`, error);
                throw error;
            }
        },
        
        // PUT请求
        put: async (endpoint, data = {}) => {
            try {
                const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                return result;
            } catch (error) {
                console.error(`API PUT Error (${endpoint}):`, error);
                throw error;
            }
        },
        
        // DELETE请求
        delete: async (endpoint) => {
            try {
                const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                const result = await response.json();
                return result;
            } catch (error) {
                console.error(`API DELETE Error (${endpoint}):`, error);
                throw error;
            }
        }
    };
}

// 模态框管理工具（使用条件声明避免重复）
if (typeof modal === 'undefined') {
    var modal = {
        // 打开模态框
        show: (modalId) => {
            console.log('[Utils.modal] 尝试打开模态框:', modalId);
            const modalElement = document.getElementById(modalId);
            console.log('[Utils.modal] 找到元素:', modalElement);
            if (modalElement) {
                modalElement.style.display = 'flex';
                console.log('[Utils.modal] 模态框已显示');
            } else {
                console.error('[Utils.modal] 未找到模态框元素:', modalId);
            }
        },
        
        // 关闭模态框
        hide: (modalId) => {
            console.log('[Utils.modal] 尝试关闭模态框:', modalId);
            const modalElement = document.getElementById(modalId);
            if (modalElement) {
                modalElement.style.display = 'none';
                console.log('[Utils.modal] 模态框已隐藏');
            } else {
                console.error('[Utils.modal] 未找到模态框元素:', modalId);
            }
        },
        
        // 兼容旧版本的别名
        open: function(modalId) { return this.show(modalId); },
        close: function(modalId) { return this.hide(modalId); },
        
        // 关闭所有模态框
        hideAll: () => {
            const modals = document.querySelectorAll('.modal');
            modals.forEach(modal => {
                modal.style.display = 'none';
            });
        }
    };
}

// 表单处理工具（使用条件声明避免重复）
if (typeof form === 'undefined') {
    var form = {
        // 获取表单数据
        getData: (formId) => {
            const formElement = document.getElementById(formId);
            if (!formElement) {
                return {};
            }
            
            const formData = {};
            const inputs = formElement.querySelectorAll('input, select, textarea');
            
            inputs.forEach(input => {
                if (input.name) {
                    formData[input.name] = input.value;
                } else if (input.id) {
                    formData[input.id] = input.value;
                }
            });
            
            return formData;
        },
        
        // 设置表单数据
        setData: (formId, data) => {
            const formElement = document.getElementById(formId);
            if (!formElement) {
                return;
            }
            
            Object.keys(data).forEach(key => {
                const input = formElement.querySelector(`[name="${key}"], [id="${key}"]`);
                if (input) {
                    input.value = data[key] || '';
                }
            });
        },
        
        // 重置表单
        reset: (formId) => {
            const formElement = document.getElementById(formId);
            if (formElement) {
                formElement.reset();
            }
        }
    };
}

// 表格工具（使用条件声明避免重复）
if (typeof table === 'undefined') {
    var table = {
        // 清空表格
        clear: (tableBodyId) => {
            const tableBody = document.getElementById(tableBodyId);
            if (tableBody) {
                tableBody.innerHTML = '';
            }
        },
        
        // 显示加载状态
        showLoading: (tableBodyId, message = '加载中...') => {
            const tableBody = document.getElementById(tableBodyId);
            if (tableBody) {
                const columns = tableBody.parentElement.querySelectorAll('th').length;
                tableBody.innerHTML = `<tr><td colspan="${columns}" style="text-align: center; padding: 20px;">${message}</td></tr>`;
            }
        },
        
        // 显示错误状态
        showError: (tableBodyId, message = '获取数据失败') => {
            const tableBody = document.getElementById(tableBodyId);
            if (tableBody) {
                const columns = tableBody.parentElement.querySelectorAll('th').length;
                tableBody.innerHTML = `<tr><td colspan="${columns}" style="text-align: center; padding: 20px; color: red;">${message}</td></tr>`;
            }
        }
    };
}

// 消息提示工具（使用条件声明避免重复）
if (typeof msg === 'undefined') {
    var msg = {
        // 显示成功消息
        success: (text) => {
            alert(text);
        },
        
        // 显示错误消息
        error: (text) => {
            alert(text);
        },
        
        // 显示确认消息
        confirm: (text) => {
            return confirm(text);
        },
        
        // 显示通用消息
        show: (text, type = 'info') => {
            alert(text);
        }
    };
}

// 日期工具（使用条件声明避免重复）
if (typeof date === 'undefined') {
    var date = {
        // 格式化日期
        format: (dateString) => {
            if (!dateString) {
                return '';
            }
            const date = new Date(dateString);
            return date.toLocaleString();
        }
    };
}

// 状态工具（使用条件声明避免重复）
if (typeof statusUtils === 'undefined') {
    var statusUtils = {
        // 获取状态文本
        getText: (statusValue) => {
            const statusMap = {
                'active': '活跃',
                'inactive': '停用',
                'pending': '待审核',
                'approved': '已通过',
                'rejected': '已拒绝',
                'success': '成功',
                'failed': '失败',
                'running': '运行中'
            };
            return statusMap[statusValue] || statusValue;
        },

        // 获取状态类名
        getClass: (statusValue) => {
            return `status-${statusValue}`;
        }
    };
}

// 搜索工具（使用条件声明避免重复）
if (typeof search === 'undefined') {
    var search = {
        // 表格搜索
        table: (tableBodyId, searchTerm) => {
            const rows = document.querySelectorAll(`#${tableBodyId} tr`);
            const term = searchTerm.toLowerCase();
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(term)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    };
}

// 图表工具（使用条件声明避免重复）
if (typeof chart === 'undefined') {
    var chart = {
        // 绘制简单的柱状图
        drawBarChart: (canvasId, data) => {
            const canvas = document.getElementById(canvasId);
            if (!canvas) {
                return;
            }
            
            const ctx = canvas.getContext('2d');
            
            // 设置画布尺寸
            canvas.width = canvas.offsetWidth;
            canvas.height = 300;
            
            // 绘制图表
            const chartWidth = canvas.width;
            const chartHeight = canvas.height - 40;
            const padding = 40;
            
            // 计算数据范围
            let maxValue = 0;
            data.datasets.forEach(dataset => {
                const datasetMax = Math.max(...dataset.data);
                if (datasetMax > maxValue) {
                    maxValue = datasetMax;
                }
            });
            maxValue = Math.ceil(maxValue * 1.1);
            
            // 绘制网格线
            ctx.strokeStyle = '#333';
            ctx.lineWidth = 1;
            
            // Y轴网格线
            for (let i = 0; i <= 5; i++) {
                const y = padding + (i * chartHeight / 5);
                ctx.beginPath();
                ctx.moveTo(padding, y);
                ctx.lineTo(chartWidth - padding, y);
                ctx.stroke();
                
                // Y轴标签
                ctx.fillStyle = '#b0b0b0';
                ctx.font = '12px Arial';
                ctx.textAlign = 'right';
                ctx.fillText(Math.round(maxValue * (1 - i / 5)), padding - 10, y + 4);
            }
            
            // X轴网格线和标签
            const barWidth = (chartWidth - padding * 2) / data.labels.length;
            data.labels.forEach((label, index) => {
                const x = padding + (index + 0.5) * barWidth;
                
                // X轴标签
                ctx.fillStyle = '#b0b0b0';
                ctx.font = '12px Arial';
                ctx.textAlign = 'center';
                ctx.fillText(label, x, canvas.height - 10);
            });
            
            // 绘制数据集
            data.datasets.forEach((dataset, datasetIndex) => {
                ctx.fillStyle = dataset.backgroundColor;
                ctx.strokeStyle = dataset.borderColor;
                ctx.lineWidth = dataset.borderWidth || 2;
                
                dataset.data.forEach((value, index) => {
                    const x = padding + index * barWidth + (datasetIndex * barWidth / data.datasets.length);
                    const barHeight = (value / maxValue) * chartHeight;
                    const y = padding + chartHeight - barHeight;
                    
                    // 绘制柱子
                    ctx.fillRect(x, y, barWidth / data.datasets.length - 4, barHeight);
                    ctx.strokeRect(x, y, barWidth / data.datasets.length - 4, barHeight);
                });
            });
            
            // 绘制图例
            const legendX = padding;
            const legendY = 10;
            const legendItemHeight = 20;
            
            data.datasets.forEach((dataset, index) => {
                const y = legendY + index * legendItemHeight;
                
                // 绘制颜色块
                ctx.fillStyle = dataset.backgroundColor;
                ctx.fillRect(legendX, y, 16, 16);
                ctx.strokeStyle = dataset.borderColor;
                ctx.strokeRect(legendX, y, 16, 16);
                
                // 绘制标签
                ctx.fillStyle = '#e0e0e0';
                ctx.font = '12px Arial';
                ctx.textAlign = 'left';
                ctx.fillText(dataset.label, legendX + 24, y + 12);
            });
        }
    };
}

// 技能列表缓存（使用条件声明避免重复）
if (typeof skillListCache === 'undefined') {
    var skillListCache = null;
    var skillListCacheTime = 0;
    var SKILL_CACHE_DURATION = 60000; // 缓存1分钟
}

// 加载技能列表（带缓存）
async function loadSkillOptions(selectElementId, options = {}) {
    const {
        includeEmpty = true,
        emptyText = '请选择技能',
        filter = null
    } = options;
    
    const select = document.getElementById(selectElementId);
    if (!select) {
        console.error(`[Utils] 未找到选择框元素: ${selectElementId}`);
        return;
    }
    
    // 显示加载中
    select.innerHTML = '<option value="">加载中...</option>';
    select.disabled = true;
    
    try {
        // 检查缓存
        const now = Date.now();
        let skills = skillListCache;
        if (!skills || (now - skillListCacheTime) > SKILL_CACHE_DURATION) {
            console.log('[Utils] 从服务器加载技能列表...');
            const response = await fetch(`${API_BASE_URL}/skills`);
            const result = await response.json();
            console.log('[Utils] 技能列表响应:', result);
            
            if (result.data) {
                console.log('[Utils] result.data 类型:', typeof result.data, Array.isArray(result.data));
                // 正确处理分页结构：data.data.content 或 data.data.items 或 data.data
                if (Array.isArray(result.data)) {
                    skills = result.data;
                } else if (result.data.content && Array.isArray(result.data.content)) {
                    skills = result.data.content;
                } else if (result.data.items && Array.isArray(result.data.items)) {
                    skills = result.data.items;
                } else {
                    skills = [];
                }
                console.log('[Utils] 解析后的技能数量:', skills.length);
                skillListCache = skills;
                skillListCacheTime = now;
            } else {
                console.warn('[Utils] 响应格式不正确:', result);
                skills = [];
            }
        } else {
            console.log('[Utils] 使用缓存的技能列表');
        }
        
        // 应用过滤器
        let filteredSkills = skills;
        if (typeof filter === 'function') {
            filteredSkills = skills.filter(filter);
        }
        
        // 清空并填充选项
        console.log('[Utils] 开始填充选项，技能数量:', filteredSkills.length);
        select.innerHTML = '';
        
        if (includeEmpty) {
            const emptyOption = document.createElement('option');
            emptyOption.value = '';
            emptyOption.textContent = emptyText;
            select.appendChild(emptyOption);
            console.log('[Utils] 添加空选项');
        }
        
        filteredSkills.forEach((skill, index) => {
            console.log(`[Utils] 添加选项 ${index}:`, skill.id, skill.name);
            const option = document.createElement('option');
            option.value = skill.id;
            option.textContent = skill.name;
            select.appendChild(option);
        });
        
        console.log('[Utils] 选项填充完成，select 子元素数量:', select.children.length);
        select.disabled = false;
        return filteredSkills;
    } catch (error) {
        console.error('[Utils] 加载技能列表错误:', error);
        select.innerHTML = '<option value="">加载失败</option>';
        select.disabled = false;
        return [];
    }
}

// 清除技能列表缓存
function clearSkillListCache() {
    skillListCache = null;
    skillListCacheTime = 0;
}

/**
 * 通用下拉列表加载方法
 * @param {string} selectElementId - 下拉框元素ID
 * @param {string} apiUrl - API接口地址（相对路径，如 '/skills'）
 * @param {Object} options - 配置选项
 * @param {string} options.emptyText - 空选项文本，默认为'请选择'
 * @param {string} options.valueField - 值字段名，默认为'id'
 * @param {string} options.textField - 文本字段名，默认为'name'
 * @param {boolean} options.includeEmpty - 是否包含空选项，默认为true
 * @param {Function} options.filter - 过滤函数
 * @param {Function} options.onError - 错误处理回调
 */
async function loadSelectOptions(selectElementId, apiUrl, options = {}) {
    const {
        emptyText = '请选择',
        valueField = 'id',
        textField = 'name',
        includeEmpty = true,
        filter = null,
        onError = null
    } = options;

    const select = document.getElementById(selectElementId);
    if (!select) {
        console.error(`[Utils] 未找到选择框元素: ${selectElementId}`);
        return [];
    }

    // 显示加载中
    select.innerHTML = '<option value="">加载中...</option>';
    select.disabled = true;

    try {
        const response = await fetch(`${API_BASE_URL}${apiUrl}`);
        const result = await response.json();
        console.log(`[Utils] ${apiUrl} 响应:`, result);

        let items = [];
        if (result.data) {
            // 正确处理分页结构：data.content 或 data.items 或 data（直接数组）
            if (Array.isArray(result.data)) {
                items = result.data;
            } else if (result.data.content && Array.isArray(result.data.content)) {
                items = result.data.content;
            } else if (result.data.items && Array.isArray(result.data.items)) {
                items = result.data.items;
            }
        }
        console.log(`[Utils] ${apiUrl} 解析后的数据数量:`, items.length);

        // 应用过滤器
        let filteredItems = items;
        if (typeof filter === 'function') {
            filteredItems = items.filter(filter);
        }

        // 清空并填充选项
        select.innerHTML = '';

        if (includeEmpty) {
            const emptyOption = document.createElement('option');
            emptyOption.value = '';
            emptyOption.textContent = emptyText;
            select.appendChild(emptyOption);
        }

        filteredItems.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueField] || '';
            option.textContent = item[textField] || '';
            select.appendChild(option);
        });

        select.disabled = false;
        return filteredItems;
    } catch (error) {
        console.error(`[Utils] 加载 ${apiUrl} 错误:`, error);
        select.innerHTML = '<option value="">加载失败</option>';
        select.disabled = false;
        if (typeof onError === 'function') {
            onError(error);
        }
        return [];
    }
}

/**
 * 带重试机制的初始化方法
 * @param {Function} initFunction - 初始化函数
 * @param {string} elementId - 要检查的元素ID
 * @param {Object} options - 配置选项
 * @param {number} options.maxRetries - 最大重试次数，默认10
 * @param {number} options.retryInterval - 重试间隔(ms)，默认200
 * @param {boolean} options.silent - 是否静默模式（不输出错误日志），默认false
 * @param {string} options.pageName - 页面名称，用于区分不同页面的初始化
 */
function initWithRetry(initFunction, elementId, options = {}) {
    const {
        maxRetries = 10,
        retryInterval = 200,
        silent = false,
        pageName = ''
    } = options;

    let retryCount = 0;
    const prefix = pageName ? `[${pageName}]` : '[Utils]';

    async function tryInit() {
        const element = document.getElementById(elementId);
        if (element) {
            if (!silent) {
                console.log(`${prefix} 元素 ${elementId} 已找到，执行初始化`);
            }
            await initFunction();
            return true;
        }

        retryCount++;
        if (retryCount < maxRetries) {
            if (!silent) {
                console.log(`${prefix} 元素 ${elementId} 未找到，第${retryCount}次重试...`);
            }
            setTimeout(() => tryInit(), retryInterval);
        } else {
            // 只在非静默模式下输出警告，且降低级别为 warn
            if (!silent) {
                console.warn(`${prefix} 元素 ${elementId} 不存在，跳过初始化（可能当前页面不需要此元素）`);
            }
        }
        return false;
    }

    // 立即尝试初始化
    tryInit().catch(err => {
        console.error(`${prefix} 初始化失败:`, err);
    });
}

/**
 * 技能选择器组件 - 统一封装技能选择列表的初始化
 * @param {Object} config - 配置对象
 * @param {string} config.selectId - 选择框元素ID（必填）
 * @param {string} config.apiUrl - API接口地址（可选，默认 '/skills'）
 * @param {string} config.emptyText - 空选项文本（可选，默认 '请选择技能'）
 * @param {string} config.pageName - 页面名称（可选，用于日志）
 * @param {boolean} config.autoInit - 是否自动初始化（可选，默认 true）
 * @param {Function} config.onLoad - 加载完成回调（可选）
 * @param {Function} config.onError - 错误回调（可选）
 * @param {Function} config.filter - 数据过滤函数（可选）
 * @returns {Object} 技能选择器实例
 */
function SkillSelector(config = {}) {
    const {
        selectId,
        apiUrl = '/skills',
        emptyText = '请选择技能',
        pageName = '',
        autoInit = true,
        onLoad = null,
        onError = null,
        filter = null
    } = config;

    if (!selectId) {
        console.error('[SkillSelector] selectId 是必填参数');
        return null;
    }

    const prefix = pageName ? `[${pageName}]` : '[SkillSelector]';
    let isInitialized = false;
    let cachedData = [];

    // 初始化方法
    async function init() {
        const select = document.getElementById(selectId);
        if (!select) {
            console.warn(`${prefix} 未找到选择框元素: ${selectId}`);
            return false;
        }

        if (isInitialized) {
            console.log(`${prefix} 技能选择器已初始化，跳过`);
            return true;
        }

        console.log(`${prefix} 开始初始化技能选择器: ${selectId}`);

        try {
            const result = await loadSelectOptions(selectId, apiUrl, {
                emptyText,
                filter,
                onError: (error) => {
                    console.error(`${prefix} 加载技能列表失败:`, error);
                    if (typeof onError === 'function') {
                        onError(error);
                    }
                }
            });

            cachedData = result;
            isInitialized = true;

            console.log(`${prefix} 技能选择器初始化完成，加载了 ${result.length} 个技能`);

            if (typeof onLoad === 'function') {
                onLoad(result);
            }

            return true;
        } catch (error) {
            console.error(`${prefix} 初始化技能选择器失败:`, error);
            if (typeof onError === 'function') {
                onError(error);
            }
            return false;
        }
    }

    // 刷新数据
    async function refresh() {
        isInitialized = false;
        return await init();
    }

    // 获取选中的值
    function getValue() {
        const select = document.getElementById(selectId);
        return select ? select.value : null;
    }

    // 获取选中的文本
    function getSelectedText() {
        const select = document.getElementById(selectId);
        if (select && select.selectedIndex >= 0) {
            return select.options[select.selectedIndex].textContent;
        }
        return null;
    }

    // 获取选中的完整数据
    function getSelectedData() {
        const value = getValue();
        if (!value) return null;
        return cachedData.find(item => item.id === value) || null;
    }

    // 设置选中的值
    function setValue(value) {
        const select = document.getElementById(selectId);
        if (select) {
            select.value = value;
        }
    }

    // 清空选择
    function clear() {
        setValue('');
    }

    // 禁用/启用
    function setDisabled(disabled) {
        const select = document.getElementById(selectId);
        if (select) {
            select.disabled = disabled;
        }
    }

    // 获取缓存的数据
    function getCachedData() {
        return [...cachedData];
    }

    // 自动初始化
    if (autoInit) {
        // 使用重试机制确保DOM已加载
        initWithRetry(init, selectId, { pageName, silent: false });
    }

    // 返回公共API
    return {
        init,
        refresh,
        getValue,
        getSelectedText,
        getSelectedData,
        setValue,
        clear,
        setDisabled,
        getCachedData,
        get selectId() { return selectId; },
        get isInitialized() { return isInitialized; }
    };
}

/**
 * 批量初始化技能选择器
 * @param {Array<Object>} configs - 配置对象数组
 * @returns {Object} 包含所有选择器实例的对象
 */
function initSkillSelectors(configs = []) {
    const selectors = {};

    configs.forEach(config => {
        if (config.selectId) {
            selectors[config.selectId] = SkillSelector(config);
        }
    });

    return selectors;
}

/**
 * 统一表格组件 - 封装列表表格的渲染和操作
 * @param {Object} config - 配置对象
 * @param {string} config.tableId - 表格元素ID（必填）
 * @param {string} config.apiUrl - API接口地址（必填）
 * @param {Array} config.columns - 列配置数组（必填）
 * @param {string} config.pageName - 页面名称（可选）
 * @param {boolean} config.autoLoad - 是否自动加载（可选，默认true）
 * @param {Function} config.onLoad - 加载完成回调（可选）
 * @param {Function} config.onError - 错误回调（可选）
 * @param {Object} config.pagination - 分页配置（可选）
 * @returns {Object} 表格组件实例
 */
function DataTable(config = {}) {
    console.log('[DataTable] 被调用，config:', config);
    
    const {
        tableId,
        apiUrl,
        columns = [],
        pageName = '',
        autoLoad = true,
        onLoad = null,
        onError = null,
        pagination = null
    } = config;

    console.log('[DataTable] 参数:', { tableId, apiUrl, columnsLength: columns.length });

    if (!tableId || !apiUrl || !columns.length) {
        console.error('[DataTable] tableId, apiUrl 和 columns 是必填参数:', { tableId, apiUrl, columnsLength: columns.length });
        return null;
    }

    const prefix = pageName ? `[${pageName}]` : '[DataTable]';
    let isLoading = false;
    let cachedData = [];
    let currentPage = 1;
    let pageSize = pagination?.pageSize || 10;

    // 获取表格体元素（直接通过ID获取tbody）
    function getTableBody() {
        const tbody = document.getElementById(tableId);
        if (!tbody) {
            console.error(`${prefix} 找不到表格体元素: ${tableId}`);
            return null;
        }
        return tbody;
    }

    // 获取表格元素（tbody的父元素）
    function getTable() {
        const tbody = getTableBody();
        if (!tbody) return null;
        return tbody.closest('table');
    }

    // 渲染表头
    function renderHeader() {
        const table = getTable();
        if (!table) {
            console.warn(`${prefix} 找不到表格元素，跳过渲染表头`);
            return;
        }

        let thead = table.querySelector('thead');
        if (!thead) {
            thead = document.createElement('thead');
            table.insertBefore(thead, table.firstChild);
        }

        const headerRow = document.createElement('tr');
        columns.forEach(col => {
            const th = document.createElement('th');
            th.textContent = col.title || col.field;
            if (col.width) th.style.width = col.width;
            if (col.align) th.style.textAlign = col.align;
            headerRow.appendChild(th);
        });

        thead.innerHTML = '';
        thead.appendChild(headerRow);
    }

    // 渲染数据行
    function renderRows(data) {
        const tbody = getTableBody();
        if (!tbody) return;

        tbody.innerHTML = '';

        if (!data || data.length === 0) {
            const emptyRow = document.createElement('tr');
            emptyRow.innerHTML = `<td colspan="${columns.length}" style="text-align: center; padding: 40px; color: #999;">
                <i class="ri-inbox-line" style="font-size: 48px; display: block; margin-bottom: 10px;"></i>
                暂无数据
            </td>`;
            tbody.appendChild(emptyRow);
            return;
        }

        data.forEach((row, index) => {
            const tr = document.createElement('tr');
            tr.style.cursor = 'pointer';
            tr.addEventListener('click', () => {
                // 移除其他行的选中状态
                tbody.querySelectorAll('tr').forEach(r => r.classList.remove('selected'));
                // 添加当前行的选中状态
                tr.classList.add('selected');
                // 触发选中事件
                if (config.onSelect) {
                    config.onSelect(row, index);
                }
            });

            columns.forEach(col => {
                const td = document.createElement('td');
                
                // 获取单元格值
                let value = row[col.field];
                
                // 如果有格式化函数，使用格式化函数
                if (typeof col.formatter === 'function') {
                    td.innerHTML = col.formatter(value, row, index);
                } else {
                    td.textContent = value !== undefined && value !== null ? value : '';
                }
                
                if (col.align) td.style.textAlign = col.align;
                if (col.className) td.className = col.className;
                
                tr.appendChild(td);
            });

            tbody.appendChild(tr);
        });
    }

    // 显示加载中
    function showLoading() {
        const tbody = getTableBody();
        if (!tbody) return;

        tbody.innerHTML = `<tr><td colspan="${columns.length}" style="text-align: center; padding: 40px;">
            <i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i>
            <p style="margin-top: 10px; color: #666;">加载中...</p>
        </td></tr>`;
    }

    // 显示错误
    function showError(message) {
        const tbody = getTableBody();
        if (!tbody) return;

        tbody.innerHTML = `<tr><td colspan="${columns.length}" style="text-align: center; padding: 40px; color: #ff4d4f;">
            <i class="ri-error-warning-line" style="font-size: 48px; display: block; margin-bottom: 10px;"></i>
            <p>${message}</p>
        </td></tr>`;
    }

    // 加载数据
    async function load(params = {}) {
        console.log(`${prefix} load() 被调用，isLoading=${isLoading}, params=`, params);
        
        if (isLoading) {
            console.log(`${prefix} 正在加载中，跳过本次请求`);
            return;
        }

        isLoading = true;
        showLoading();

        try {
            // 构建查询参数
            const queryParams = new URLSearchParams();
            if (params.keyword) queryParams.append('keyword', params.keyword);
            if (params.page) queryParams.append('page', params.page);
            if (params.pageSize) queryParams.append('pageSize', params.pageSize);
            // 支持额外的筛选参数
            if (params.category) queryParams.append('category', params.category);
            if (params.status) queryParams.append('status', params.status);
            
            const queryString = queryParams.toString();
            const url = `${API_BASE_URL}${apiUrl}${queryString ? '?' + queryString : ''}`;

            console.log(`${prefix} 加载数据: ${url}`);

            const response = await fetch(url);
            const result = await response.json();
            
            console.log(`${prefix} API响应:`, result);

            if (result.success && result.data) {
                // 支持多种数据结构
                let data = [];
                if (Array.isArray(result.data)) {
                    data = result.data;
                } else if (result.data.content && Array.isArray(result.data.content)) {
                    data = result.data.content;
                } else if (result.data.items && Array.isArray(result.data.items)) {
                    data = result.data.items;
                }

                cachedData = data;
                renderRows(data);

                console.log(`${prefix} 数据加载完成，共 ${data.length} 条记录`);

                if (typeof onLoad === 'function') {
                    onLoad(data);
                }

                return data;
            } else {
                showError('获取数据失败');
                if (typeof onError === 'function') {
                    onError(new Error('获取数据失败'));
                }
                return [];
            }
        } catch (error) {
            console.error(`${prefix} 加载数据失败:`, error);
            showError('加载数据失败，请稍后重试');
            if (typeof onError === 'function') {
                onError(error);
            }
            return [];
        } finally {
            isLoading = false;
        }
    }

    // 刷新数据
    function refresh() {
        // 重置加载状态，确保可以重新加载
        isLoading = false;
        return load();
    }

    // 搜索数据
    function search(keyword) {
        return load({ keyword });
    }

    // 获取缓存的数据
    function getCachedData() {
        return [...cachedData];
    }

    // 获取选中的行数据（需要在配置中设置 onSelect 回调）
    let selectedRow = null;
    function getSelectedRow() {
        return selectedRow;
    }

    // 初始化
    async function init() {
        console.log(`${prefix} 开始初始化表格`);
        try {
            renderHeader();
            if (autoLoad) {
                console.log(`${prefix} 自动加载数据`);
                await load();
            }
            console.log(`${prefix} 表格初始化完成`);
        } catch (error) {
            console.error(`${prefix} 表格初始化失败:`, error);
            throw error;
        }
    }

    // 自动初始化
    initWithRetry(init, tableId, { pageName, silent: false });

    // 返回公共API
    return {
        load,
        refresh,
        search,
        getCachedData,
        getSelectedRow,
        renderRows,
        get tableId() { return tableId; },
        get isLoading() { return isLoading; }
    };
}

/**
 * 批量初始化表格
 * @param {Array<Object>} configs - 配置对象数组
 * @returns {Object} 包含所有表格实例的对象
 */
function initDataTables(configs = []) {
    const tables = {};

    configs.forEach(config => {
        if (config.tableId) {
            tables[config.tableId] = DataTable(config);
        }
    });

    return tables;
}

// 导出工具函数
if (typeof window !== 'undefined') {
    window.utils = {
        api,
        modal,
        form,
        table,
        msg,
        date,
        status: statusUtils,
        search,
        chart,
        API_BASE_URL,
        loadSkillOptions,
        clearSkillListCache,
        loadSelectOptions,
        initWithRetry,
        SkillSelector,
        initSkillSelectors,
        DataTable,
        initDataTables
    };
}
