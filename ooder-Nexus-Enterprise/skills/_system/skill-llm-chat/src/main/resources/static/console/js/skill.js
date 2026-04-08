// 技能表格实例（使用 var 避免重复声明错误）
var skillTable = null;

// 初始化页面
function initSkillPage() {
    console.log('[Skill] 初始化技能页面');
    updateTimestamp();
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳

    // 初始化技能表格
    initSkillTable();
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 初始化技能表格
function initSkillTable() {
    if (!utils.DataTable) {
        console.error('[Skill] DataTable 组件未加载');
        return;
    }

    skillTable = utils.DataTable({
        tableId: 'skillTableBody',
        apiUrl: '/api/skills',
        pageName: 'Skill',
        columns: [
            { field: 'id', title: '技能ID', width: '150px' },
            { field: 'name', title: '技能名称', width: '150px' },
            { field: 'category', title: '分类', width: '120px' },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="skill-status">${value || '可用'}</span>`
            },
            {
                field: 'actions',
                title: '操作',
                width: '250px',
                align: 'center',
                formatter: (value, row) => `
                    <button class="btn btn-secondary" style="margin-right: 8px;" onclick="showSkillDetail('${row.id}')">详情</button>
                    <button class="btn btn-primary" style="margin-right: 8px;" onclick="executeSkill('${row.id}')">执行</button>
                    <button class="btn btn-danger" onclick="deleteSkill('${row.id}')">删除</button>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[Skill] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[Skill] 表格数据加载失败:', error);
        }
    });
}

// 显示技能详情
async function showSkillDetail(skillId) {
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

        alert(`技能详情:\nID: ${skill.id || '-'}\n名称: ${skill.name || '-'}\n分类: ${skill.category || '未分类'}\n描述: ${skill.description || '无描述'}`);
    } catch (error) {
        console.error('获取技能详情错误:', error);
        utils.message.error('获取技能详情失败: ' + error.message);
    }
}

// 执行技能
async function executeSkill(skillId) {
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

        // 解析 API 响应结构
        let executionResult = result;
        if (result.success && result.data) {
            executionResult = result.data;
        }

        if (executionResult.status === 'SUCCESS') {
            utils.message.success('执行成功!\n结果: ' + executionResult.output);
        } else {
            utils.message.error('执行失败!\n错误: ' + (executionResult.errorMessage || '未知错误'));
        }
    } catch (error) {
        console.error('执行技能错误:', error);
        utils.message.error('执行技能失败: ' + error.message);
    }
}

// 删除技能
async function deleteSkill(skillId) {
    if (!confirm('确定要删除技能 ' + skillId + ' 吗?')) {
        return;
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/skills/${skillId}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('删除技能失败');
        }
        const result = await response.json();
        if (result.success) {
            utils.message.success('技能 ' + skillId + ' 已删除');
            if (skillTable) skillTable.refresh();
        } else {
            utils.message.error('删除技能失败');
        }
    } catch (error) {
        console.error('删除技能错误:', error);
        utils.message.error('删除技能失败: ' + error.message);
    }
}

// 发布新技能
async function publishSkill() {
    const skillId = document.getElementById('skill-id').value;
    const skillName = document.getElementById('skill-name').value;
    const skillCategory = document.getElementById('skill-category').value;
    const skillDescription = document.getElementById('skill-description').value;

    if (!skillId || !skillName || !skillCategory) {
        utils.message.error('请填写所有必填字段');
        return;
    }

    // 显示保存中状态
    const submitBtn = document.querySelector('#skill-publish-tab button[onclick="publishSkill()"]');
    const originalText = submitBtn ? submitBtn.textContent : '发布技能';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 发布中...';
    }

    try {
        const skill = {
            id: skillId,
            name: skillName,
            category: skillCategory,
            description: skillDescription
        };

        const response = await fetch(`${utils.API_BASE_URL}/skills`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(skill)
        });

        if (!response.ok) {
            throw new Error('发布技能失败');
        }

        const result = await response.json();

        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (result.success) {
            utils.message.success('技能 ' + skillName + ' 已发布');
            // 清空表单
            document.getElementById('skill-id').value = '';
            document.getElementById('skill-name').value = '';
            document.getElementById('skill-category').value = 'text-processing';
            document.getElementById('skill-description').value = '';
            // 切换到技能列表标签页
            switchTab('skill-list');
            // 刷新表格
            if (skillTable) skillTable.refresh();
        } else {
            utils.message.error('发布技能失败');
        }
    } catch (error) {
        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('发布技能错误:', error);
        utils.message.error('发布技能失败: ' + error.message);
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
}

// 按分类查看技能
function viewSkillsByCategory(category) {
    utils.message.info('查看分类: ' + category + ' 的技能');
    // 这里可以添加按分类加载技能的逻辑
}

// 页面加载完成后初始化
window.onload = function() {
    initMenu('skill');
    init();
};
