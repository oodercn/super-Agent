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
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/storage`);
        const data = await response.json();
        
        if (!data.success || !data.data) {
            utils.msg.error('获取存储数据失败');
            return;
        }
        
        const allItems = [...(data.data.json || []), ...(data.data.vfs || [])];
        const storage = allItems.find(item => item.id === storageId);
        
        if (!storage) {
            utils.msg.error('存储项不存在');
            return;
        }
        
        const modalHtml = `
            <div class="modal-overlay" id="editStorageModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
                <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 500px; width: 90%;">
                    <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="margin: 0; font-size: 16px;"><i class="ri-edit-line"></i> 编辑存储</h3>
                        <button class="modal-close" onclick="closeEditStorageModal()" style="background: none; border: none; font-size: 20px; cursor: pointer;"><i class="ri-close-line"></i></button>
                    </div>
                    <div class="modal-body" style="padding: 20px;">
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">存储名称 <span class="nx-text-error">*</span></label>
                            <input type="text" class="nx-input" id="editStorageName" value="${storage.name || ''}" placeholder="请输入存储名称">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">存储类型</label>
                            <select class="nx-input" id="editStorageType" disabled>
                                <option value="json" ${storage.type === 'json' ? 'selected' : ''}>JSON存储</option>
                                <option value="vfs" ${storage.type === 'vfs' ? 'selected' : ''}>VFS存储</option>
                            </select>
                            <small style="color: var(--nx-text-secondary);">存储类型创建后不可修改</small>
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">存储路径</label>
                            <input type="text" class="nx-input" id="editStoragePath" value="${storage.path || ''}" placeholder="存储路径">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">描述</label>
                            <textarea class="nx-input" id="editStorageDesc" rows="3" placeholder="存储描述">${storage.description || ''}</textarea>
                        </div>
                        <div class="nx-form-group">
                            <label class="nx-form-label">配置 (JSON)</label>
                            <textarea class="nx-input" id="editStorageConfig" rows="4" placeholder='{"key": "value"}'>${storage.config ? JSON.stringify(storage.config, null, 2) : '{}'}</textarea>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: flex-end; gap: 8px;">
                        <button class="nx-btn nx-btn--secondary" onclick="closeEditStorageModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="submitEditStorage('${storageId}')">保存</button>
                    </div>
                </div>
            </div>
        `;
        
        const existingModal = document.getElementById('editStorageModal');
        if (existingModal) existingModal.remove();
        
        document.body.insertAdjacentHTML('beforeend', modalHtml);
    } catch (error) {
        console.error('[StorageManagement] 加载存储数据错误:', error);
        utils.msg.error('加载存储数据失败');
    }
};

window.closeEditStorageModal = function() {
    const modal = document.getElementById('editStorageModal');
    if (modal) modal.remove();
};

window.submitEditStorage = async function(storageId) {
    const name = document.getElementById('editStorageName').value.trim();
    const path = document.getElementById('editStoragePath').value.trim();
    const description = document.getElementById('editStorageDesc').value.trim();
    const configStr = document.getElementById('editStorageConfig').value.trim();
    
    if (!name) {
        utils.msg.error('请输入存储名称');
        return;
    }
    
    let config = {};
    try {
        config = configStr ? JSON.parse(configStr) : {};
    } catch (e) {
        utils.msg.error('配置JSON格式错误');
        return;
    }
    
    try {
        const response = await fetch(`${utils.API_BASE_URL}/admin/storage/${storageId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,
                path: path,
                description: description,
                config: config
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            closeEditStorageModal();
            utils.msg.success('存储更新成功');
            loadStorageData();
        } else {
            utils.msg.error('更新失败: ' + (data.message || '未知错误'));
        }
    } catch (error) {
        console.error('[StorageManagement] 更新存储错误:', error);
        utils.msg.error('更新失败');
    }
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
