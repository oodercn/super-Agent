// 存储管理 JavaScript

// 页面加载时初始化
function initStorageManagementPage() {
    console.log('[StorageManagement] 初始化存储管理页面');

    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu('storage-management');
    }

    // 初始化标签页
    initTabs();

    // 加载存储数据
    loadStorageData();
}

// 初始化标签页
function initTabs() {
    const tabButtons = document.querySelectorAll('.execution-tabs .tab-btn');
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const tabId = this.getAttribute('data-tab');
            switchTab(tabId);
        });
    });
}

// 切换标签页
function switchTab(tabId) {
    // 更新按钮状态
    document.querySelectorAll('.execution-tabs .tab-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute('data-tab') === tabId) {
            btn.classList.add('active');
        }
    });

    // 更新内容显示
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
        content.classList.remove('active');
    });

    const activeContent = document.getElementById(tabId + 'Tab');
    if (activeContent) {
        activeContent.style.display = 'block';
        activeContent.classList.add('active');
    }
}

// 加载存储数据
async function loadStorageData() {
    console.log('[StorageManagement] 加载存储数据');
    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/storage`);
        const data = await response.json();

        if (data.success && data.data) {
            renderStorageList(data.data);
        } else {
            console.warn('[StorageManagement] 加载存储数据失败:', data.message);
        }
    } catch (error) {
        console.error('[StorageManagement] 加载存储数据错误:', error);
    }
}

// 渲染存储列表
function renderStorageList(storageData) {
    const jsonList = document.getElementById('jsonStorageList');
    const vfsList = document.getElementById('vfsStorageList');
    const allList = document.getElementById('allStorageList');

    if (jsonList) {
        jsonList.innerHTML = storageData.json?.map(item => createStorageItemHTML(item)).join('') || '<div class="empty-state"><i class="ri-database-line" style="font-size: 48px; color: #ccc;"></i><p>暂无JSON存储项目</p></div>';
    }

    if (vfsList) {
        vfsList.innerHTML = storageData.vfs?.map(item => createStorageItemHTML(item)).join('') || '<div class="empty-state"><i class="ri-hard-drive-2-line" style="font-size: 48px; color: #ccc;"></i><p>暂无VFS存储项目</p></div>';
    }

    if (allList) {
        const allItems = [...(storageData.json || []), ...(storageData.vfs || [])];
        allList.innerHTML = allItems.map(item => createStorageItemHTML(item)).join('') || '<div class="empty-state"><i class="ri-database-2-line" style="font-size: 48px; color: #ccc;"></i><p>暂无存储项目</p></div>';
    }
}

// 创建存储项HTML
function createStorageItemHTML(item) {
    return `
        <div class="storage-item">
            <div class="storage-info">
                <h4>${item.name || '未命名'}</h4>
                <p>类型: ${item.type || '未知'} | 路径: ${item.path || '-'}</p>
            </div>
            <div class="storage-actions">
                <button class="btn btn-secondary" onclick="editStorage('${item.id}')">
                    <i class="ri-edit-line"></i> 编辑
                </button>
                <button class="btn btn-danger" onclick="deleteStorage('${item.id}')">
                    <i class="ri-delete-line"></i> 删除
                </button>
            </div>
        </div>
    `;
}

// 打开添加存储模态框
window.openAddStorageModal = function() {
    console.log('[StorageManagement] 打开添加存储模态框');
    const storageForm = document.getElementById('storageForm');
    if (storageForm) storageForm.reset();

    document.getElementById('storageModal').style.display = 'flex';
};

// 关闭存储模态框
window.closeStorageModal = function() {
    document.getElementById('storageModal').style.display = 'none';
};

// 保存存储设置
window.saveStorageSettings = async function() {
    console.log('[StorageManagement] 保存存储设置');

    const storageName = document.getElementById('storageName').value;
    const storageType = document.getElementById('storageType').value;
    const storagePath = document.getElementById('storagePath').value;

    if (!storageName || !storagePath) {
        utils.msg.error('请填写完整的存储信息');
        return;
    }

    const storageData = {
        name: storageName,
        type: storageType,
        path: storagePath
    };

    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/storage`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(storageData)
        });

        const data = await response.json();

        if (data.success) {
            utils.msg.success('存储添加成功！');
            closeStorageModal();
            loadStorageData();
        } else {
            utils.msg.error('添加失败: ' + (data.message || '未知错误'));
        }
    } catch (error) {
        console.error('[StorageManagement] 保存存储设置错误:', error);
        utils.msg.error('添加失败');
    }
};

// 编辑存储
window.editStorage = async function(storageId) {
    console.log('[StorageManagement] 编辑存储:', storageId);
    // TODO: 实现编辑功能
    utils.msg.info('编辑功能开发中...');
};

// 删除存储
window.deleteStorage = async function(storageId) {
    if (!utils.msg.confirm('确定要删除这个存储吗？')) {
        return;
    }

    console.log('[StorageManagement] 删除存储:', storageId);
    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/storage/${storageId}`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (data.success) {
            utils.msg.success('存储删除成功！');
            loadStorageData();
        } else {
            utils.msg.error('删除失败: ' + (data.message || '未知错误'));
        }
    } catch (error) {
        console.error('[StorageManagement] 删除存储错误:', error);
        utils.msg.error('删除失败');
    }
};

// 页面加载时初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
        console.log('[StorageManagement] DOM加载完成（通过事件）');
        initStorageManagementPage();
    });
} else {
    console.log('[StorageManagement] DOM已加载，直接初始化');
    initStorageManagementPage();
}
