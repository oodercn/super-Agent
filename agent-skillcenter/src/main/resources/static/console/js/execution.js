// 执行历史表格实例
let executionTable = null;
// 技能选择器实例
let executionSkillSelector = null;

// 初始化页面
function init() {
    console.log('[Execution] 初始化执行页面');
    updateTimestamp();
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳

    // 初始化执行历史表格
    initExecutionTable();
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 初始化执行历史表格
function initExecutionTable() {
    if (!utils.DataTable) {
        console.error('[Execution] DataTable 组件未加载');
        return;
    }

    // 使用后端API获取执行历史数据
    executionTable = utils.DataTable({
        tableId: 'executionTableBody',
        apiUrl: `${utils.API_BASE_URL}/personal/executions`,
        pageName: 'Execution',
        columns: [
            { field: 'executionId', title: '执行ID', width: '150px' },
            { field: 'skillId', title: '技能ID', width: '150px' },
            {
                field: 'timestamp',
                title: '执行时间',
                width: '180px',
                formatter: (value) => value || new Date().toLocaleString('zh-CN')
            },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="execution-status status-${value?.toLowerCase()}">${value === 'SUCCESS' ? '成功' : '失败'}</span>`
            },
            { field: 'executionTime', title: '执行时长', width: '100px', align: 'center' },
            {
                field: 'actions',
                title: '操作',
                width: '120px',
                align: 'center',
                formatter: (value, row) => `
                    <button class="btn btn-secondary" onclick="showExecutionDetail('${row.executionId}')">详情</button>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[Execution] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[Execution] 表格数据加载失败:', error);
            utils.message.error('加载执行历史失败: ' + error.message);
        }
    });
}

// 显示执行详情
async function showExecutionDetail(executionId) {
    try {
        const response = await fetch(`${utils.API_BASE_URL}/personal/executions/${executionId}`);
        if (!response.ok) {
            throw new Error('获取执行详情失败');
        }
        const result = await response.json();
        if (result.success && result.data) {
            const execution = result.data;
            alert(`执行详情:\n执行ID: ${executionId}\n技能: ${execution.skillName || execution.skillId}\n状态: ${execution.status === 'SUCCESS' ? '成功' : '失败'}\n结果: ${execution.output || execution.errorMessage || '无'}\n执行时间: ${execution.executionTime || '未知'}`);
        } else {
            throw new Error(result.message || '获取执行详情失败');
        }
    } catch (error) {
        console.error('获取执行详情错误:', error);
        utils.message.error('获取执行详情失败: ' + error.message);
    }
}

// 获取执行状态
async function getExecutionStatus(executionId) {
    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/status/${executionId}`);
        if (!response.ok) {
            throw new Error('获取执行状态失败');
        }
        const status = await response.text();
        alert(`执行状态: ${status}`);
    } catch (error) {
        console.error('获取执行状态错误:', error);
        utils.message.error('获取执行状态失败: ' + error.message);
    }
}

// 清理执行结果
async function clearExecutionResult(executionId) {
    if (!confirm('确定要清理执行结果 ' + executionId + ' 吗?')) {
        return;
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/result/${executionId}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('清理执行结果失败');
        }
        const result = await response.json();
        if (result.success) {
            utils.message.success('执行结果清理成功!');
            if (executionTable) executionTable.refresh();
        } else {
            utils.message.error('执行结果清理失败!');
        }
    } catch (error) {
        console.error('清理执行结果错误:', error);
        utils.message.error('清理执行结果失败: ' + error.message);
    }
}

// 标签页切换函数
function switchTab(tabId) {
    // 隐藏所有标签页内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
    });

    // 显示选中的标签页内容
    document.getElementById(`${tabId}-tab`).classList.remove('hidden');

    // 更新标签页按钮状态
    document.querySelectorAll('.btn-secondary').forEach(btn => {
        btn.style.backgroundColor = '#1a1a1a';
        btn.style.color = 'var(--ooder-secondary)';
    });

    // 高亮当前标签页按钮
    event.target.style.backgroundColor = 'var(--ooder-primary)';
    event.target.style.color = 'white';

    // 根据标签页ID加载相应的数据
    if (tabId === 'execute-history') {
        if (executionTable) executionTable.refresh();
    } else if (tabId === 'execute-run') {
        loadSkillsForExecution();
    }
}

// 加载技能列表供执行选择
async function loadSkillsForExecution() {
    try {
        // 使用统一封装的 SkillSelector 组件
        if (utils && utils.SkillSelector) {
            executionSkillSelector = utils.SkillSelector({
                selectId: 'skill-select',
                emptyText: '选择技能',
                pageName: 'Execution',
                onLoad: (skills) => {
                    console.log('[Execution] 技能选择器加载完成，共', skills.length, '个技能');
                },
                onError: (error) => {
                    console.error('[Execution] 技能选择器加载失败:', error);
                }
            });
        } else {
            // 回退到旧方式
            console.warn('[Execution] SkillSelector 组件未加载，使用旧方式加载');
            const response = await fetch(`${utils.API_BASE_URL}/skills`);
            if (!response.ok) {
                throw new Error('加载技能列表失败');
            }
            const result = await response.json();

            let skills = [];
            if (result.success && result.data) {
                skills = result.data.items || result.data || [];
            } else if (Array.isArray(result)) {
                skills = result;
            }

            const skillSelect = document.getElementById('skill-select');
            skillSelect.innerHTML = '<option value="">选择技能</option>';

            skills.forEach(skill => {
                const option = document.createElement('option');
                option.value = skill.id;
                option.textContent = skill.name;
                skillSelect.appendChild(option);
            });
        }

        // 添加技能选择事件监听器
        const skillSelect = document.getElementById('skill-select');
        skillSelect.addEventListener('change', function() {
            const skillId = this.value;
            if (skillId) {
                loadSkillParameters(skillId);
            } else {
                document.getElementById('execution-parameters').innerHTML = '';
            }
        });
    } catch (error) {
        console.error('加载技能列表错误:', error);
        utils.message.error('加载技能列表失败: ' + error.message);
    }
}

// 加载技能参数
async function loadSkillParameters(skillId) {
    try {
        const response = await fetch(`${utils.API_BASE_URL}/skills/${skillId}`);
        if (!response.ok) {
            throw new Error('获取技能详情失败');
        }
        const result = await response.json();

        // 解析 API 响应结构
        let skill = null;
        if (result.success && result.data) {
            skill = result.data;
        } else {
            skill = result;
        }

        const parametersContainer = document.getElementById('execution-parameters');
        parametersContainer.innerHTML = `
            <h4>执行参数</h4>
            <div style="margin-top: 8px;">
                <p>技能: ${skill.name || '-'}</p>
                <p>分类: ${skill.category || '未分类'}</p>
                <p>描述: ${skill.description || '无描述'}</p>
            </div>
        `;
    } catch (error) {
        console.error('获取技能详情错误:', error);
        utils.message.error('获取技能详情失败: ' + error.message);
    }
}

// 从表单执行技能
async function executeSkillFromForm() {
    const skillId = document.getElementById('skill-select').value;
    if (!skillId) {
        utils.message.error('请选择技能');
        return;
    }

    // 显示执行中状态
    const executeBtn = document.querySelector('#execute-run-tab button[onclick="executeSkillFromForm()"]');
    const originalText = executeBtn ? executeBtn.textContent : '执行技能';
    if (executeBtn) {
        executeBtn.disabled = true;
        executeBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 执行中...';
    }

    try {
        const parameters = {};
        if (skillId === 'text-uppercase') {
            parameters.text = prompt('请输入要转换的文本:');
        } else if (skillId === 'code-generation') {
            parameters.language = prompt('请输入语言:');
            parameters.prompt = prompt('请输入代码生成提示:');
        } else if (skillId === 'local-deployment') {
            parameters.appName = prompt('请输入应用名称:');
            parameters.appPath = prompt('请输入应用路径:');
        }

        const response = await fetch(`${utils.API_BASE_URL}/skills/${skillId}/execute`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ parameters })
        });

        if (!response.ok) {
            throw new Error('执行技能失败');
        }

        const result = await response.json();

        // 恢复按钮状态
        if (executeBtn) {
            executeBtn.disabled = false;
            executeBtn.textContent = originalText;
        }

        if (result.status === 'SUCCESS') {
            utils.message.success('执行成功!\n结果: ' + result.output);
        } else {
            utils.message.error('执行失败!\n错误: ' + result.errorMessage);
        }
    } catch (error) {
        // 恢复按钮状态
        if (executeBtn) {
            executeBtn.disabled = false;
            executeBtn.textContent = originalText;
        }
        console.error('执行技能错误:', error);
        utils.message.error('执行技能失败: ' + error.message);
    }
}

// 获取执行状态
async function getStatus() {
    const executionId = document.getElementById('status-execution-id').value;
    if (!executionId) {
        utils.message.error('请输入执行ID');
        return;
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/status/${executionId}`);
        if (!response.ok) {
            throw new Error('获取执行状态失败');
        }
        const status = await response.text();
        const statusResult = document.getElementById('execution-status-result');
        statusResult.innerHTML = `
            <div style="padding: 12px; background-color: #1a1a1a; border-radius: var(--ooder-radius);">
                <p>执行ID: ${executionId}</p>
                <p>状态: ${status}</p>
            </div>
        `;
    } catch (error) {
        console.error('获取执行状态错误:', error);
        utils.message.error('获取执行状态失败: ' + error.message);
    }
}

// 获取执行结果
async function getResult() {
    const executionId = document.getElementById('result-execution-id').value;
    if (!executionId) {
        utils.message.error('请输入执行ID');
        return;
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/result/${executionId}`);
        if (!response.ok) {
            throw new Error('获取执行结果失败');
        }
        const result = await response.json();
        const resultContent = document.getElementById('execution-result-content');
        resultContent.innerHTML = `
            <div style="padding: 12px; background-color: #1a1a1a; border-radius: var(--ooder-radius);">
                <p>执行ID: ${executionId}</p>
                <p>状态: ${result.status === 'SUCCESS' ? '成功' : '失败'}</p>
                <p>结果: ${result.output || result.errorMessage}</p>
                <p>执行时间: ${result.executionTime || '未知'}</p>
            </div>
        `;
    } catch (error) {
        console.error('获取执行结果错误:', error);
        utils.message.error('获取执行结果失败: ' + error.message);
    }
}

// 清理执行结果
async function cleanResult() {
    const executionId = document.getElementById('result-execution-id').value;
    if (!executionId) {
        utils.message.error('请输入执行ID');
        return;
    }

    if (!confirm('确定要清理执行结果 ' + executionId + ' 吗?')) {
        return;
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/result/${executionId}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('清理执行结果失败');
        }
        const result = await response.json();
        if (result.success) {
            utils.message.success('执行结果清理成功!');
            document.getElementById('execution-result-content').innerHTML = '';
        } else {
            utils.message.error('执行结果清理失败!');
        }
    } catch (error) {
        console.error('清理执行结果错误:', error);
        utils.message.error('清理执行结果失败: ' + error.message);
    }
}

// 页面加载完成后初始化
window.onload = function() {
    initMenu('execution');
    init();
};
