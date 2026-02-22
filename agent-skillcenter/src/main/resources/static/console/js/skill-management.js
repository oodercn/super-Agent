// 技能表格实例（使用 var 避免重复声明错误）
var skillTable = null;

// 页面加载时初始化
function initSkillManagement() {
    console.log('[SkillManagement] 初始化技能管理页面');

    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu('skill-management');
    }

    // 初始化表格
    initSkillTable();

    // 绑定表单提交事件
    initSkillForm();

    // 绑定搜索事件
    initSearchEvent();
}

// 初始化技能表格
function initSkillTable() {
    console.log('[SkillManagement] 开始初始化技能表格');

    if (!utils || !utils.DataTable) {
        console.error('[SkillManagement] DataTable 组件未加载, utils:', utils);
        return;
    }

    console.log('[SkillManagement] DataTable 组件已加载');

    const tableBody = document.getElementById('skillTableBody');
    if (!tableBody) {
        console.error('[SkillManagement] 找不到表格体元素 skillTableBody');
        return;
    }

    console.log('[SkillManagement] 找到表格体元素');
    console.log('[SkillManagement] 调用 utils.DataTable 参数:', {
        tableId: 'skillTableBody',
        apiUrl: '/api/admin/skills',
        columnsCount: 6
    });

    skillTable = utils.DataTable({
        tableId: 'skillTableBody',
        apiUrl: '/api/admin/skills',
        pageName: 'SkillManagement',
        columns: [
            { field: 'id', title: 'ID', width: '120px' },
            { field: 'name', title: '名称', width: '150px' },
            { field: 'category', title: '分类', width: '120px' },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="skill-status status-${value || 'unknown'}">${utils.status.getText(value) || value}</span>`
            },
            {
                field: 'createdAt',
                title: '创建时间',
                width: '180px',
                formatter: (value) => utils.date.format(value) || '-'
            },
            {
                field: 'actions',
                title: '操作',
                width: '250px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="btn btn-secondary" onclick="editSkill('${row.id}')">
                            <i class="ri-edit-line"></i> 编辑
                        </button>
                        <button class="btn btn-success" onclick="approveSkill('${row.id}')" ${row.status !== 'pending' ? 'disabled' : ''}>
                            <i class="ri-check-line"></i> 审核
                        </button>
                        <button class="btn btn-danger" onclick="deleteSkill('${row.id}')">
                            <i class="ri-delete-line"></i> 删除
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[SkillManagement] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[SkillManagement] 表格数据加载失败:', error);
        }
    });

    if (skillTable) {
        console.log('[SkillManagement] 技能表格初始化完成');
    } else {
        console.error('[SkillManagement] 技能表格初始化失败，返回null');
    }
}

// 初始化技能表单
function initSkillForm() {
    // 表单提交通过 onclick 处理，不需要绑定 submit 事件
    console.log('[SkillManagement] 表单事件初始化完成（使用 onclick 方式）');
}

// 查询技能（点击查询按钮时调用）
window.searchSkills = function() {
    console.log('[SkillManagement] 查询按钮被点击');
    console.log('[SkillManagement] skillTable 状态:', skillTable);
    
    const category = document.getElementById('filterCategory')?.value || '';
    const status = document.getElementById('filterStatus')?.value || '';
    const keyword = document.getElementById('filterKeyword')?.value?.trim() || '';

    console.log('[SkillManagement] 筛选值:', { category, status, keyword });

    if (skillTable && typeof skillTable.load === 'function') {
        // 构建查询参数
        const params = {};
        if (category) params.category = category;
        if (status) params.status = status;
        if (keyword) params.keyword = keyword;

        console.log('[SkillManagement] 查询参数:', params);
        skillTable.load(params);
    } else {
        console.error('[SkillManagement] skillTable 未初始化或 load 方法不可用');
        utils.msg.error('表格未初始化，请刷新页面重试');
    }
};

// 重置筛选条件
window.resetFilters = function() {
    console.log('[SkillManagement] 重置筛选条件');

    const categorySelect = document.getElementById('filterCategory');
    const statusSelect = document.getElementById('filterStatus');
    const keywordInput = document.getElementById('filterKeyword');

    if (categorySelect) categorySelect.value = '';
    if (statusSelect) statusSelect.value = '';
    if (keywordInput) keywordInput.value = '';

    // 重新加载全部数据
    if (skillTable) {
        skillTable.refresh();
    }
};

// 初始化搜索事件（保留回车键支持）
function initSearchEvent() {
    console.log('[SkillManagement] 开始初始化搜索事件');
    
    // 绑定查询按钮点击事件
    const btnSearch = document.getElementById('btnSearchSkills');
    console.log('[SkillManagement] 查询按钮元素:', btnSearch);
    if (btnSearch) {
        btnSearch.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[SkillManagement] 查询按钮点击事件触发');
            searchSkills();
        });
        console.log('[SkillManagement] 查询按钮事件已绑定');
    } else {
        console.error('[SkillManagement] 未找到查询按钮元素 #btnSearchSkills');
    }

    // 绑定重置按钮点击事件
    const btnReset = document.getElementById('btnResetFilters');
    console.log('[SkillManagement] 重置按钮元素:', btnReset);
    if (btnReset) {
        btnReset.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[SkillManagement] 重置按钮点击事件触发');
            resetFilters();
        });
        console.log('[SkillManagement] 重置按钮事件已绑定');
    } else {
        console.error('[SkillManagement] 未找到重置按钮元素 #btnResetFilters');
    }

    // 为关键词输入框添加回车键支持
    const keywordInput = document.getElementById('filterKeyword');
    if (keywordInput) {
        keywordInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                searchSkills();
            }
        });
    }
}

// 保存技能
window.saveSkill = async function() {
    console.log('[SkillManagement] 表单提交事件触发');

    const skillId = document.getElementById('skillId').value;
    const skillName = document.getElementById('skillName').value;
    const skillDescription = document.getElementById('skillDescription').value;
    const skillCategory = document.getElementById('skillCategory').value;
    const skillStatus = document.getElementById('skillStatus').value;

    console.log('[SkillManagement] 表单数据:', {
        skillId: skillId,
        name: skillName,
        description: skillDescription,
        category: skillCategory,
        status: skillStatus
    });

    // 前端校验
    if (!skillName || skillName.trim().length < 2) {
        utils.msg.error('技能名称至少2个字符');
        return;
    }
    if (!skillDescription || skillDescription.trim().length === 0) {
        utils.msg.error('技能描述不能为空');
        return;
    }

    const skillData = {
        name: skillName.trim(),
        description: skillDescription.trim(),
        category: skillCategory,
        status: skillStatus
    };

    // 显示保存中状态
    const submitBtn = document.querySelector('#skillModal .btn-primary');
    const originalText = submitBtn ? submitBtn.textContent : '保存';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 保存中...';
    }

    try {
        let result;
        if (skillId) {
            // 编辑现有技能
            console.log('[SkillManagement] 更新技能:', skillId);
            result = await utils.api.post(`/api/admin/skills/${skillId}/update`, skillData);
        } else {
            // 添加新技能
            console.log('[SkillManagement] 创建新技能');
            result = await utils.api.post('/admin/skills', skillData);
        }

        console.log('[SkillManagement] API响应:', result);

        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (result.success) {
            // 刷新表格数据
            if (skillTable) {
                skillTable.refresh();
            }
            utils.modal.close('skillModal');
            utils.msg.success('技能保存成功！');
        } else {
            utils.msg.error('技能保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('[SkillManagement] 保存技能错误:', error);
        utils.msg.error('技能保存失败: ' + (error && error.message ? error.message : '未知错误'));
    }
}

// 打开添加技能模态框
function openAddSkillModal() {
    console.log('[SkillManagement] 打开添加技能模态框');
    const modalTitle = document.getElementById('modalTitle');
    const skillId = document.getElementById('skillId');
    const skillName = document.getElementById('skillName');
    const skillDescription = document.getElementById('skillDescription');
    const skillCategory = document.getElementById('skillCategory');
    const skillStatus = document.getElementById('skillStatus');

    if (modalTitle) modalTitle.textContent = '添加技能';
    if (skillId) skillId.value = '';
    if (skillName) skillName.value = '';
    if (skillDescription) skillDescription.value = '';
    if (skillCategory) skillCategory.value = 'text-processing';
    if (skillStatus) skillStatus.value = 'pending';

    utils.modal.open('skillModal');
}

// 打开编辑技能模态框
async function editSkill(skillId) {
    console.log('[SkillManagement] 编辑技能:', skillId);
    try {
        const data = await utils.api.post('/admin/skills/get', { skillId: skillId });
        console.log('[SkillManagement] 获取技能详情响应:', data);

        if (data.success && data.data) {
            const skill = data.data;
            const modalTitle = document.getElementById('modalTitle');
            const skillIdInput = document.getElementById('skillId');
            const skillName = document.getElementById('skillName');
            const skillDescription = document.getElementById('skillDescription');
            const skillCategory = document.getElementById('skillCategory');
            const skillStatus = document.getElementById('skillStatus');

            if (modalTitle) modalTitle.textContent = '编辑技能';
            if (skillIdInput) skillIdInput.value = skill.id || '';
            if (skillName) skillName.value = skill.name || '';
            if (skillDescription) skillDescription.value = skill.description || '';
            if (skillCategory) skillCategory.value = skill.category || 'text-processing';
            if (skillStatus) skillStatus.value = skill.status || 'active';

            utils.modal.open('skillModal');
        } else {
            utils.msg.error('获取技能详情失败');
        }
    } catch (error) {
        console.error('[SkillManagement] 获取技能详情错误:', error);
        utils.msg.error('获取技能详情失败');
    }
}

// 关闭技能模态框
function closeSkillModal() {
    utils.modal.close('skillModal');
}

// 审核技能
async function approveSkill(skillId) {
    console.log('[SkillManagement] 审核技能:', skillId);
    try {
        const result = await utils.api.post(`/api/admin/skills/${skillId}/approve`);
        console.log('[SkillManagement] 审核技能响应:', result);

        if (result.success) {
            // 刷新表格数据
            if (skillTable) {
                skillTable.refresh();
            }
            utils.msg.success('技能审核成功！');
        } else {
            utils.msg.error('技能审核失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SkillManagement] 审核技能错误:', error);
        utils.msg.error('技能审核失败');
    }
}

// 删除技能
async function deleteSkill(skillId) {
    if (utils.msg.confirm('确定要删除这个技能吗？')) {
        console.log('[SkillManagement] 删除技能:', skillId);
        try {
            const result = await utils.api.post(`/api/admin/skills/${skillId}/delete`);
            console.log('[SkillManagement] 删除技能响应:', result);

            if (result.success) {
                // 刷新表格数据
                if (skillTable) {
                    skillTable.refresh();
                }
                utils.msg.success('技能删除成功！');
            } else {
                utils.msg.error('技能删除失败: ' + (result.message || '未知错误'));
            }
        } catch (error) {
            console.error('[SkillManagement] 删除技能错误:', error);
            utils.msg.error('技能删除失败');
        }
    }
}

// 页面加载时初始化
if (document.readyState === 'loading') {
    // DOM 还在加载中，等待 DOMContentLoaded 事件
    document.addEventListener('DOMContentLoaded', function () {
        console.log('[SkillManagement] DOM加载完成（通过事件）');
        initSkillManagement();
    });
} else {
    // DOM 已经加载完成，直接初始化
    console.log('[SkillManagement] DOM已加载，直接初始化');
    initSkillManagement();
}
