// 市场技能表格实例
var marketSkillTable = null;

// 页面加载时初始化
function initMarketManagementPage() {
    console.log('[MarketManagement] 初始化市场管理页面');

    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu('market-management');
    }

    // 初始化表格
    initMarketSkillTable();

    // 绑定搜索事件
    initSearchEvent();
}

// 初始化市场技能表格
function initMarketSkillTable() {
    console.log('[MarketManagement] 开始初始化市场技能表格');

    if (!utils || !utils.DataTable) {
        console.error('[MarketManagement] DataTable 组件未加载, utils:', utils);
        return;
    }

    console.log('[MarketManagement] DataTable 组件已加载');

    const tableBody = document.getElementById('skillTableBody');
    if (!tableBody) {
        console.error('[MarketManagement] 找不到表格体元素 skillTableBody');
        return;
    }

    console.log('[MarketManagement] 找到表格体元素');

    marketSkillTable = utils.DataTable({
        tableId: 'skillTableBody',
        apiUrl: '/api/market/skills',
        pageName: 'MarketManagement',
        columns: [
            { field: 'id', title: '技能ID', width: '120px' },
            { field: 'name', title: '技能名称', width: '150px' },
            { field: 'category', title: '分类', width: '120px' },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="skill-status status-${value || 'unknown'}">${utils.status.getText(value) || value}</span>`
            },
            { field: 'downloads', title: '下载量', width: '100px', align: 'center' },
            {
                field: 'rating',
                title: '评分',
                width: '100px',
                align: 'center',
                formatter: (value) => getRatingStars(value || 0)
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
                width: '200px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="btn btn-secondary" onclick="editMarketSkill('${row.id}')">
                            <i class="ri-edit-line"></i> 编辑
                        </button>
                        <button class="btn btn-danger" onclick="removeMarketSkill('${row.id}')">
                            <i class="ri-delete-line"></i> 移除
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[MarketManagement] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[MarketManagement] 表格数据加载失败:', error);
        }
    });

    if (marketSkillTable) {
        console.log('[MarketManagement] 市场技能表格初始化完成');
    } else {
        console.error('[MarketManagement] 市场技能表格初始化失败，返回null');
    }
}

// 初始化搜索事件
function initSearchEvent() {
    console.log('[MarketManagement] 开始初始化搜索事件');

    // 绑定查询按钮点击事件
    const btnSearch = document.getElementById('btnSearchSkills');
    console.log('[MarketManagement] 查询按钮元素:', btnSearch);
    if (btnSearch) {
        btnSearch.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[MarketManagement] 查询按钮点击事件触发');
            searchMarketSkills();
        });
        console.log('[MarketManagement] 查询按钮事件已绑定');
    } else {
        console.error('[MarketManagement] 未找到查询按钮元素 #btnSearchSkills');
    }

    // 绑定重置按钮点击事件
    const btnReset = document.getElementById('btnResetFilters');
    console.log('[MarketManagement] 重置按钮元素:', btnReset);
    if (btnReset) {
        btnReset.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[MarketManagement] 重置按钮点击事件触发');
            resetFilters();
        });
        console.log('[MarketManagement] 重置按钮事件已绑定');
    } else {
        console.error('[MarketManagement] 未找到重置按钮元素 #btnResetFilters');
    }

    // 为关键词输入框添加回车键支持
    const keywordInput = document.getElementById('filterKeyword');
    if (keywordInput) {
        keywordInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                searchMarketSkills();
            }
        });
    }
}

// 查询市场技能（点击查询按钮时调用）
window.searchMarketSkills = function() {
    console.log('[MarketManagement] 查询按钮被点击');
    console.log('[MarketManagement] marketSkillTable 状态:', marketSkillTable);

    const category = document.getElementById('filterCategory')?.value || '';
    const status = document.getElementById('filterStatus')?.value || '';
    const keyword = document.getElementById('filterKeyword')?.value?.trim() || '';

    console.log('[MarketManagement] 筛选值:', { category, status, keyword });

    if (marketSkillTable && typeof marketSkillTable.load === 'function') {
        // 构建查询参数
        const params = {};
        if (category) params.category = category;
        if (status) params.status = status;
        if (keyword) params.keyword = keyword;

        console.log('[MarketManagement] 查询参数:', params);
        marketSkillTable.load(params);
    } else {
        console.error('[MarketManagement] marketSkillTable 未初始化或 load 方法不可用');
        utils.msg.error('表格未初始化，请刷新页面重试');
    }
};

// 重置筛选条件
window.resetFilters = function() {
    console.log('[MarketManagement] 重置筛选条件');

    const categorySelect = document.getElementById('filterCategory');
    const statusSelect = document.getElementById('filterStatus');
    const keywordInput = document.getElementById('filterKeyword');

    if (categorySelect) categorySelect.value = '';
    if (statusSelect) statusSelect.value = '';
    if (keywordInput) keywordInput.value = '';

    // 重新加载全部数据
    if (marketSkillTable) {
        marketSkillTable.refresh();
    }
};

// 获取评分星星
function getRatingStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';

    for (let i = 0; i < fullStars; i++) {
        stars += '★';
    }
    if (hasHalfStar) {
        stars += '½';
    }
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    for (let i = 0; i < emptyStars; i++) {
        stars += '☆';
    }

    return stars;
}

// 打开添加市场技能模态框
window.openAddMarketSkillModal = function() {
    document.getElementById('modalTitle').textContent = '添加市场技能';
    document.getElementById('skillId').value = '';
    document.getElementById('skillName').value = '';
    document.getElementById('skillDescription').value = '';
    document.getElementById('skillCategory').value = 'text-processing';
    document.getElementById('skillStatus').value = 'active';
    document.getElementById('skillVersion').value = '1.0.0';
    document.getElementById('skillAuthor').value = 'System';
    document.getElementById('skillModal').style.display = 'flex';
};

// 打开编辑市场技能模态框
window.editMarketSkill = function(skillId) {
    console.log('[MarketManagement] 编辑市场技能:', skillId);

    // 从缓存中获取技能数据
    if (marketSkillTable && marketSkillTable.getCachedData) {
        const cachedData = marketSkillTable.getCachedData();
        const skill = cachedData.find(s => s.id === skillId);

        if (skill) {
            document.getElementById('modalTitle').textContent = '编辑市场技能';
            document.getElementById('skillId').value = skill.id;
            document.getElementById('skillName').value = skill.name;
            document.getElementById('skillDescription').value = skill.description || '';
            document.getElementById('skillCategory').value = skill.category || 'text-processing';
            document.getElementById('skillStatus').value = skill.status || 'active';
            document.getElementById('skillVersion').value = skill.version || '1.0.0';
            document.getElementById('skillAuthor').value = skill.author || 'System';
            document.getElementById('skillModal').style.display = 'block';
            return;
        }
    }

    // 如果缓存中没有，调用API获取
    fetch(`${utils.API_BASE_URL}/market/skills/${skillId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.data) {
                const skill = data.data;
                document.getElementById('modalTitle').textContent = '编辑市场技能';
                document.getElementById('skillId').value = skill.id;
                document.getElementById('skillName').value = skill.name;
                document.getElementById('skillDescription').value = skill.description || '';
                document.getElementById('skillCategory').value = skill.category || 'text-processing';
                document.getElementById('skillStatus').value = skill.status || 'active';
                document.getElementById('skillVersion').value = skill.version || '1.0.0';
                document.getElementById('skillAuthor').value = skill.author || 'System';
                document.getElementById('skillModal').style.display = 'block';
            } else {
                utils.msg.error('获取技能详情失败');
            }
        })
        .catch(error => {
            console.error('[MarketManagement] 获取技能详情错误:', error);
            utils.msg.error('获取技能详情失败');
        });
};

// 关闭技能模态框
window.closeSkillModal = function() {
    document.getElementById('skillModal').style.display = 'none';
};

// 保存市场技能
window.saveMarketSkill = async function() {
    const skillId = document.getElementById('skillId').value;
    const skillName = document.getElementById('skillName').value;
    const skillDescription = document.getElementById('skillDescription').value;
    const skillCategory = document.getElementById('skillCategory').value;
    const skillStatus = document.getElementById('skillStatus').value;
    const skillVersion = document.getElementById('skillVersion').value;
    const skillAuthor = document.getElementById('skillAuthor').value;

    if (!skillName || !skillDescription) {
        utils.msg.error('请填写完整的技能信息');
        return;
    }

    const skillData = {
        name: skillName,
        description: skillDescription,
        category: skillCategory,
        status: skillStatus,
        version: skillVersion,
        author: skillAuthor
    };

    try {
        let url = '/market/skills';
        let method = 'POST';

        if (skillId) {
            // 编辑现有技能
            url = `/market/skills/${skillId}`;
            method = 'PUT';
        }

        console.log('[MarketManagement] 保存市场技能:', { url, method, skillData });

        const result = await utils.api.request(url, method, skillData);
        console.log('[MarketManagement] 保存市场技能响应:', result);

        if (result.success) {
            utils.msg.success('技能保存成功！');
            closeSkillModal();
            // 刷新表格
            if (marketSkillTable) {
                marketSkillTable.refresh();
            }
        } else {
            utils.msg.error('技能保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[MarketManagement] 保存市场技能错误:', error);
        utils.msg.error('技能保存失败');
    }
};

// 移除市场技能
window.removeMarketSkill = async function(skillId) {
    if (!utils.msg.confirm('确定要从市场中移除这个技能吗？')) {
        return;
    }

    console.log('[MarketManagement] 移除市场技能:', skillId);

    try {
        const result = await utils.api.post(`/api/market/skills/${skillId}/delete`);
        console.log('[MarketManagement] 移除市场技能响应:', result);

        if (result.success) {
            utils.msg.success('技能移除成功！');
            // 刷新表格
            if (marketSkillTable) {
                marketSkillTable.refresh();
            }
        } else {
            utils.msg.error('技能移除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[MarketManagement] 移除市场技能错误:', error);
        utils.msg.error('技能移除失败');
    }
};

// 页面加载时初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
        console.log('[MarketManagement] DOM加载完成（通过事件）');
        initMarketManagementPage();
    });
} else {
    console.log('[MarketManagement] DOM已加载，直接初始化');
    initMarketManagementPage();
}
