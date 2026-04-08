// 技能认证管理 JavaScript

// 认证列表表格实例（使用 var 避免重复声明错误）
var authTable = null;
// 技能选择器实例
var authSkillSelector = null;

// 页面加载完成后初始化
function initSkillAuthPage() {
    console.log('[SkillAuth] 初始化技能认证页面');

    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu('skill-authentication');
    }

    // 初始化表格
    initAuthTable();

    // 初始化技能选择器
    initSkillSelector();

    // 绑定表单提交事件
    initForms();

    // 绑定搜索事件
    initSearchEvents();
}

// 页面加载时初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
        console.log('[SkillAuth] DOM加载完成（通过事件）');
        initSkillAuthPage();
    });
} else {
    console.log('[SkillAuth] DOM已加载，直接初始化');
    initSkillAuthPage();
}

// 初始化认证列表表格
function initAuthTable() {
    if (!utils.DataTable) {
        console.error('[SkillAuth] DataTable 组件未加载');
        return;
    }

    authTable = utils.DataTable({
        tableId: 'authTableBody',
        apiUrl: '/api/admin/authentication/requests',
        pageName: 'SkillAuth',
        columns: [
            { field: 'id', title: '认证ID', width: '120px' },
            { field: 'skillName', title: '技能名称', width: '150px' },
            { field: 'skillId', title: '技能ID', width: '150px' },
            { field: 'applicant', title: '申请人', width: '120px' },
            {
                field: 'appliedAt',
                title: '申请时间',
                width: '180px',
                formatter: (value) => value ? new Date(value).toLocaleString() : ''
            },
            {
                field: 'status',
                title: '状态',
                width: '100px',
                align: 'center',
                formatter: (value) => `<span class="skillcenter-auth-status skillcenter-status-${value}">${getStatusText(value)}</span>`
            },
            {
                field: 'actions',
                title: '操作',
                width: '200px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="skillcenter-action-buttons">
                        <button class="skillcenter-btn skillcenter-btn-secondary" onclick="viewAuthDetails('${row.id}')">
                            <i class="ri-eye-line"></i> 查看
                        </button>
                        <button class="skillcenter-btn skillcenter-btn-success" onclick="reviewAuth('${row.id}')" ${row.status !== 'pending' ? 'disabled' : ''}>
                            <i class="ri-check-line"></i> 审核
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[SkillAuth] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[SkillAuth] 表格数据加载失败:', error);
        }
    });
}

// 初始化技能选择器
function initSkillSelector() {
    if (utils && utils.SkillSelector) {
        authSkillSelector = utils.SkillSelector({
            selectId: 'skillSelect',
            emptyText: '请选择技能',
            pageName: 'SkillAuth',
            onLoad: (skills) => {
                console.log('[SkillAuth] 技能选择器加载完成，共', skills.length, '个技能');
            },
            onError: (error) => {
                console.error('[SkillAuth] 技能选择器加载失败:', error);
            }
        });
    } else {
        console.warn('[SkillAuth] SkillSelector 组件未加载，使用旧方式加载');
        populateSkillSelectFallback();
    }
}

// 填充技能选择下拉框（回退方式）
async function populateSkillSelectFallback() {
    const skillSelect = document.getElementById('skillSelect');
    if (!skillSelect) {
        console.error('[SkillAuth] 找不到 skillSelect 元素');
        return;
    }

    console.log('[SkillAuth] 使用回退方式加载技能列表');
    skillSelect.innerHTML = '<option value="">加载中...</option>';

    try {
        const apiUrl = `${utils.API_BASE_URL}/skills`;
        console.log('[SkillAuth] 请求API:', apiUrl);

        const response = await fetch(apiUrl);
        console.log('[SkillAuth] API响应状态:', response.status);

        if (!response.ok) {
            throw new Error(`HTTP错误: ${response.status}`);
        }

        const data = await response.json();
        console.log('[SkillAuth] API响应数据:', data);

        skillSelect.innerHTML = '<option value="">请选择技能</option>';

        if (data.success && data.data) {
            const skills = Array.isArray(data.data) ? data.data : (data.data.items || []);
            console.log('[SkillAuth] 解析到技能数量:', skills.length);

            if (skills.length === 0) {
                skillSelect.innerHTML = '<option value="">暂无技能</option>';
                return;
            }

            skills.forEach(skill => {
                const option = document.createElement('option');
                option.value = skill.id || skill.skillId || '';
                option.textContent = skill.name || skill.skillName || '未命名技能';
                skillSelect.appendChild(option);
            });
            console.log('[SkillAuth] 技能列表加载完成');
        } else {
            console.warn('[SkillAuth] API返回数据格式不正确:', data);
            skillSelect.innerHTML = '<option value="">加载失败</option>';
        }
    } catch (error) {
        console.error('[SkillAuth] 获取技能列表错误:', error);
        skillSelect.innerHTML = '<option value="">加载失败</option>';
    }
}

// 获取状态文本
function getStatusText(status) {
    switch (status) {
        case 'pending': return '待审核';
        case 'approved': return '已批准';
        case 'rejected': return '已拒绝';
        default: return status;
    }
}

// 初始化表单提交事件
function initForms() {
    // 表单提交通过 onclick 处理，不需要绑定 submit 事件
    console.log('[SkillAuth] 表单事件初始化完成（使用 onclick 方式）');
}

// 初始化搜索和筛选事件
function initSearchEvents() {
    console.log('[SkillAuth] 初始化搜索事件');

    // 绑定查询按钮点击事件
    const btnSearch = document.getElementById('btnSearchAuth');
    if (btnSearch) {
        btnSearch.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[SkillAuth] 查询按钮点击事件触发');
            searchAuth();
        });
    }

    // 绑定重置按钮点击事件
    const btnReset = document.getElementById('btnResetFilters');
    if (btnReset) {
        btnReset.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            console.log('[SkillAuth] 重置按钮点击事件触发');
            resetFilters();
        });
    }

    // 为关键词输入框添加回车键支持
    const skillSearch = document.getElementById('skillSearch');
    if (skillSearch) {
        skillSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchAuth();
            }
        });
    }
}

// 查询认证（点击查询按钮时调用）
window.searchAuth = function() {
    console.log('[SkillAuth] 查询按钮被点击');
    console.log('[SkillAuth] authTable 状态:', authTable);

    const status = document.getElementById('statusFilter')?.value || '';
    const keyword = document.getElementById('skillSearch')?.value?.trim() || '';

    console.log('[SkillAuth] 筛选值:', { status, keyword });

    if (authTable && typeof authTable.load === 'function') {
        // 构建查询参数
        const params = {};
        if (status) params.status = status;
        if (keyword) params.keyword = keyword;

        console.log('[SkillAuth] 查询参数:', params);
        authTable.load(params);
    } else {
        console.error('[SkillAuth] authTable 未初始化或 load 方法不可用');
        utils.msg.error('表格未初始化，请刷新页面重试');
    }
};

// 重置筛选条件
window.resetFilters = function() {
    console.log('[SkillAuth] 重置筛选条件');

    const statusSelect = document.getElementById('statusFilter');
    const keywordInput = document.getElementById('skillSearch');

    if (statusSelect) statusSelect.value = '';
    if (keywordInput) keywordInput.value = '';

    // 重新加载全部数据
    if (authTable) {
        authTable.refresh();
    }
};

// 提交签发认证表单
window.submitCertificateForm = async function() {
    const skillId = document.getElementById('skillSelect').value;
    const certificateType = document.getElementById('certificateType').value;
    const certificateDescription = document.getElementById('certificateDescription').value;

    if (!skillId) {
        utils.message.error('请选择技能');
        return;
    }

    const certData = {
        skillId: skillId,
        type: certificateType,
        description: certificateDescription
    };

    // 显示保存中状态
    const submitBtn = document.querySelector('#certificateModal .skillcenter-btn-primary');
    const originalText = submitBtn ? submitBtn.textContent : '签发';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 签发中...';
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/authentication/issue`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(certData)
        });

        const data = await response.json();

        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (data.success) {
            if (authTable) authTable.refresh();
            closeCertificateModal();
            utils.message.success('认证签发成功！');
        } else {
            utils.message.error('认证签发失败: ' + (data.message || '未知错误'));
        }
    } catch (error) {
        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('[SkillAuth] 签发认证错误:', error);
        utils.message.error('认证签发失败');
    }
}

// 提交审核表单
window.submitReviewForm = async function() {
    const authId = document.getElementById('authId').value;
    const reviewStatus = document.getElementById('reviewStatus').value;
    const reviewComments = document.getElementById('reviewComments').value;

    const reviewData = {
        status: reviewStatus,
        comments: reviewComments
    };

    // 显示保存中状态
    const submitBtn = document.querySelector('#reviewModal .skillcenter-btn-primary');
    const originalText = submitBtn ? submitBtn.textContent : '提交审核';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 提交中...';
    }

    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/authentication/requests/${authId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewData)
        });

        const data = await response.json();

        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (data.success) {
            if (authTable) authTable.refresh();
            closeReviewModal();
            utils.message.success('审核完成！');
        } else {
            utils.message.error('审核失败: ' + (data.message || '未知错误'));
        }
    } catch (error) {
        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('[SkillAuth] 审核错误:', error);
        utils.message.error('审核失败');
    }
}

// 打开签发认证模态框
function openIssueCertificateModal() {
    console.log('[SkillAuth] 打开签发认证模态框');
    const certificateForm = document.getElementById('certificateForm');
    if (certificateForm) certificateForm.reset();

    // 每次打开模态框时重新加载技能列表
    initSkillSelector();

    document.getElementById('certificateModal').style.display = 'flex';
}

// 关闭签发认证模态框
function closeCertificateModal() {
    document.getElementById('certificateModal').style.display = 'none';
}

// 打开审核认证模态框
function reviewAuth(authId) {
    // 调用API获取认证详情
    fetch(`${utils.API_BASE_URL}/admin/authentication/requests/${authId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.data) {
                const auth = data.data;
                document.getElementById('authId').value = auth.id;
                document.getElementById('skillDetails').innerHTML = `
                    <h4>技能详情</h4>
                    <p><strong>技能名称:</strong> ${auth.skillName}</p>
                    <p><strong>技能ID:</strong> ${auth.skillId}</p>
                    <p><strong>申请人:</strong> ${auth.applicant}</p>
                    <p><strong>申请时间:</strong> ${auth.appliedAt ? new Date(auth.appliedAt).toLocaleString() : ''}</p>
                `;
                document.getElementById('reviewModal').style.display = 'flex';
            } else {
                utils.message.error('获取认证详情失败');
            }
        })
        .catch(error => {
            console.error('[SkillAuth] 获取认证详情错误:', error);
            utils.message.error('获取认证详情失败');
        });
}

// 关闭审核认证模态框
function closeReviewModal() {
    document.getElementById('reviewModal').style.display = 'none';
}

// 查看认证详情
function viewAuthDetails(authId) {
    // 调用API获取认证详情
    fetch(`${utils.API_BASE_URL}/admin/authentication/requests/${authId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.data) {
                const auth = data.data;
                const details = `认证详情:

认证ID: ${auth.id}
技能名称: ${auth.skillName}
技能ID: ${auth.skillId}
申请人: ${auth.applicant}
申请时间: ${auth.appliedAt ? new Date(auth.appliedAt).toLocaleString() : ''}
状态: ${getStatusText(auth.status)}`;
                alert(details);
            } else {
                utils.message.error('获取认证详情失败');
            }
        })
        .catch(error => {
            console.error('[SkillAuth] 获取认证详情错误:', error);
            utils.message.error('获取认证详情失败');
        });
}
