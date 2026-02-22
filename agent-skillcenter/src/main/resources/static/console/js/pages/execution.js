/**
 * 执行管理页面脚本
 */

// 初始化页面
function init() {
    updateTimestamp();
    loadExecutionHistory();
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 执行管理相关函数

// 加载执行历史
async function loadExecutionHistory() {
    try {
        // 由于后端API没有直接提供执行历史的接口，我们需要从其他接口获取数据
        // 这里我们暂时使用模拟数据
        const executions = [
            {
                executionId: 'exec-12345',
                skillId: 'text-uppercase',
                timestamp: new Date().toLocaleString('zh-CN'),
                status: 'SUCCESS',
                executionTime: '0.5s'
            },
            {
                executionId: 'exec-12344',
                skillId: 'code-generation',
                timestamp: new Date().toLocaleString('zh-CN'),
                status: 'SUCCESS',
                executionTime: '3.2s'
            },
            {
                executionId: 'exec-12343',
                skillId: 'local-deployment',
                timestamp: new Date().toLocaleString('zh-CN'),
                status: 'FAILED',
                executionTime: '1.8s'
            }
        ];
        renderExecutionHistory(executions);
    } catch (error) {
        console.error('加载执行历史错误:', error);
        alert('加载执行历史失败: ' + error.message);
    }
}

// 渲染执行历史
function renderExecutionHistory(executions) {
    const tbody = document.querySelector('table tbody');
    tbody.innerHTML = '';
    
    executions.forEach(execution => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${execution.executionId}</td>
            <td>${execution.skillId}</td>
            <td>${execution.timestamp}</td>
            <td>${execution.status === 'SUCCESS' ? '成功' : '失败'}</td>
            <td>${execution.executionTime}</td>
            <td>
                <button class="btn btn-secondary" onclick="showExecutionDetail('${execution.executionId}')">详情</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// 显示执行详情
async function showExecutionDetail(executionId) {
    try {
        const response = await fetch(`${utils.API_BASE_URL}/execution/result/${executionId}`);
        if (!response.ok) {
            throw new Error('获取执行详情失败');
        }
        const result = await response.json();
        alert(`执行详情:\n执行ID: ${executionId}\n状态: ${result.status === 'SUCCESS' ? '成功' : '失败'}\n结果: ${result.output || result.errorMessage}\n执行时间: ${result.executionTime || '未知'}`);
    } catch (error) {
        console.error('获取执行详情错误:', error);
        alert('获取执行详情失败: ' + error.message);
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
        alert(`执行状态 ${status}`);
    } catch (error) {
        console.error('获取执行状态错误:', error);
        alert('获取执行状态失败: ' + error.message);
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
        if (result) {
            alert('执行结果清理成功!');
            loadExecutionHistory();
        } else {
            alert('执行结果清理失败!');
        }
    } catch (error) {
        console.error('清理执行结果错误:', error);
        alert('清理执行结果失败: ' + error.message);
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
        loadExecutionHistory();
    } else if (tabId === 'execute-run') {
        loadSkillsForExecution();
    }
}

// 加载技能列表供执行选择
async function loadSkillsForExecution() {
    try {
        // 使用统一封装的 SkillSelector 组件
        if (utils && utils.SkillSelector) {
            utils.SkillSelector({
                selectId: 'skill-select',
                emptyText: '选择技能',
                pageName: 'ExecutionPage',
                onLoad: (skills) => {
                    console.log('[ExecutionPage] 技能选择器加载完成，共', skills.length, '个技能');
                },
                onError: (error) => {
                    console.error('[ExecutionPage] 技能选择器加载失败:', error);
                }
            });
        } else {
            // 回退到旧方式
            console.warn('[ExecutionPage] SkillSelector 组件未加载，使用旧方式加载');
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
        alert('加载技能列表失败: ' + error.message);
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
    }
}

// 页面加载完成后初始化
window.onload = function() {
    init();
};
