let storageAPI;
let currentFolderId = '';
let modalManager;
let buttonManager;

// 工具函数对象
const utils = {
    showError: function(message, selector) {
        console.error(message);
        const el = document.querySelector(selector);
        if (el) {
            el.innerHTML = `<div class="error-message"><i class="ri-error-warning-line"></i> ${message}</div>`;
        }
    },
    showSuccess: function(message, selector) {
        console.log(message);
        const el = document.querySelector(selector);
        if (el) {
            el.innerHTML = `<div class="success-message"><i class="ri-check-line"></i> ${message}</div>`;
        }
    },
    formatBytes: function(bytes) {
        if (bytes === 0) return '0 B';
        const k = 1024;
        const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    formatTime: function(timestamp) {
        return new Date(timestamp).toLocaleString('zh-CN');
    },
    escapeHtml: function(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },
    confirm: function(message) {
        return window.confirm(message);
    },
    downloadFile: function(blob, filename) {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    },
    parallel: async function(promises) {
        return Promise.all(promises);
    }
};

async function initStorageManagement() {
    try {
        await window.initMenu();
        
        storageAPI = new StorageAPI();
        modalManager = new ModalManager({
            onBeforeOpen: (id) => {
                if (id === 'upload-modal') {
                    modalManager.resetForm(id);
                } else if (id === 'share-modal') {
                    modalManager.resetForm(id);
                }
            }
        });
        
        buttonManager = new ButtonManager();
        
        // 注册按钮
        buttonManager.register('upload-btn');
        buttonManager.register('create-folder-btn');
        buttonManager.register('cleanup-btn');
        buttonManager.register('refresh-btn');
        buttonManager.register('share-btn');
        
        modalManager.register('upload-modal');
        modalManager.register('create-folder-modal');
        modalManager.register('share-modal');
        modalManager.register('rename-modal');
        
        await loadStorageSpace();
        await loadFileBrowser();
        await loadSharedFiles();
        await loadReceivedFiles();
        
    } catch (error) {
        console.error('初始化存储管理失败:', error);
        utils.showError('加载存储管理数据失败', '.storage-content');
    }
}

async function loadStorageSpace() {
    try {
        const response = await storageAPI.getStorageSpace();
        if ((response.code === 200 || response.message === "success") && response.data) {
            const data = response.data;
            document.getElementById('total-space').textContent = utils.formatBytes(data.totalSpace);
            document.getElementById('used-space').textContent = utils.formatBytes(data.usedSpace);
            document.getElementById('free-space').textContent = utils.formatBytes(data.freeSpace);
        }
    } catch (error) {
        console.error('加载存储空间失败:', error);
    }
}

async function loadFileBrowser(folderId = '') {
    try {
        currentFolderId = folderId;
        // 使用空字符串表示根目录，让服务器端处理
        const folderPath = folderId || '';
        console.log('加载文件夹:', folderPath);
        const response = await storageAPI.getFolderChildren(folderPath);
        
        if ((response.code === 200 || response.message === "success") && response.data) {
            const data = response.data;
            renderFileBrowser(data);
            updateFolderOptions(data.folder);
        }
    } catch (error) {
        console.error('加载文件浏览器失败:', error);
    }
}

function renderFileBrowser(data) {
    const browserContent = document.getElementById('file-browser-content');
    
    if (!data || !data.folder) {
        browserContent.innerHTML = '<div class="empty-state">文件夹不存在</div>';
        return;
    }
    
    let html = '<div class="breadcrumb">';
    html += '<span class="breadcrumb-item" onclick="loadFileBrowser(\'\')">根目录</span>';
    html += '</div>';
    
    html += '<div class="file-list">';
    
    if (data.children && data.children.length > 0) {
        html += '<h3>文件夹</h3>';
        html += '<div class="folder-list">';
        data.children.forEach(folder => {
            html += `<div class="folder-item">`;
            html += `<div class="file-info" onclick="loadFileBrowser('${folder.id}')">`;
            html += `<i class="ri-folder-line folder-icon"></i>`;
            html += `<span class="folder-name">${utils.escapeHtml(folder.name)}</span>`;
            html += `</div>`;
            html += `<div class="file-actions">`;
            html += `<button class="btn btn-sm btn-secondary" onclick="event.stopPropagation(); editFolder('${folder.id}', '${utils.escapeHtml(folder.name)}', '${folder.description || ''}')">编辑</button>`;
            html += `<button class="btn btn-sm btn-danger" onclick="event.stopPropagation(); deleteFolder('${folder.id}')">删除</button>`;
            html += `</div>`;
            html += '</div>';
        });
        html += '</div>';
    }
    
    if (data.files && data.files.length > 0) {
        html += '<h3>文件</h3>';
        html += '<div class="file-list">';
            data.files.forEach(file => {
                html += `<div class="file-item">`;
                html += `<div class="file-info" onclick="showFileDetails('${file.id}')">`;
                html += `<i class="ri-file-line file-icon"></i>`;
                html += `<span class="file-name">${utils.escapeHtml(file.name)}</span>`;
                html += `<span class="file-size">${utils.formatBytes(file.size)}</span>`;
                html += `</div>`;
                html += `<div class="file-actions">`;
                html += `<button class="btn btn-sm btn-ghost" onclick="showRenameModal('${file.id}', '${utils.escapeHtml(file.name)}')">重命名</button>`;
                html += `<button class="btn btn-sm btn-secondary" onclick="downloadFile('${file.id}')">下载</button>`;
                html += `<button class="btn btn-sm btn-primary" onclick="showShareModal('${file.id}')">分享</button>`;
                html += `<button class="btn btn-sm btn-danger" onclick="deleteFile('${file.id}')">删除</button>`;
                html += `</div>`;
                html += '</div>';
            });
            html += '</div>';
    }
    
    html += '</div>';
    
    browserContent.innerHTML = html;
}

function updateFolderOptions(folder) {
    const select = document.getElementById('upload-folder');
    const parentSelect = document.getElementById('parent-folder');
    
    let options = '<option value="">根目录</option>';
    
    if (folder) {
        options += `<option value="${folder.id}">${utils.escapeHtml(folder.name)}</option>`;
    }
    
    select.innerHTML = options;
    parentSelect.innerHTML = options;
}

async function loadSharedFiles() {
    try {
        const response = await storageAPI.getSharedFiles();
        
        if ((response.code === 200 || response.message === "success") && response.data) {
            const data = response.data;
            renderSharedFiles(data.sharedFiles);
        }
    } catch (error) {
        console.error('加载分享文件失败:', error);
    }
}

function renderSharedFiles(files) {
    const sharedFilesList = document.getElementById('shared-files-list');
    
    if (!files || files.length === 0) {
        sharedFilesList.innerHTML = '<div class="empty-state">暂无分享文件</div>';
        return;
    }
    
    let html = '';
    files.forEach(file => {
        html += `<div class="shared-file-item">`;
        html += `<div class="file-info">`;
        html += `<i class="ri-file-line file-icon"></i>`;
        html += `<span class="file-name">${utils.escapeHtml(file.fileName || file.name)}</span>`;
        if (file.size) {
            html += `<span class="file-size">${utils.formatBytes(file.size)}</span>`;
        }
        html += `<span class="file-target">分享给: ${utils.escapeHtml(file.target)}</span>`;
        html += `<span class="file-time">${utils.formatTime(file.shareTime)}</span>`;
        html += `</div>`;
        html += `<div class="file-actions">`;
        if (file.fileId) {
            html += `<button class="btn btn-sm btn-secondary" onclick="downloadFile('${file.fileId}')">下载</button>`;
        }
        if (file.id) {
            html += `<button class="btn btn-sm btn-danger" onclick="unshareFile('${file.id}')">取消分享</button>`;
        }
        html += `</div>`;
        html += `</div>`;
    });
    
    sharedFilesList.innerHTML = html;
}

async function loadReceivedFiles() {
    try {
        const response = await storageAPI.getReceivedFiles();
        
        if ((response.code === 200 || response.message === "success") && response.data) {
            const data = response.data;
            renderReceivedFiles(data.receivedFiles);
        }
    } catch (error) {
        console.error('加载收到的文件失败:', error);
    }
}

function renderReceivedFiles(files) {
    const receivedFilesList = document.getElementById('received-files-list');
    
    if (!receivedFilesList) {
        return;
    }
    
    if (!files || files.length === 0) {
        receivedFilesList.innerHTML = '<div class="empty-state">暂无收到的文件</div>';
        return;
    }
    
    let html = '';
    files.forEach(file => {
        html += `<div class="received-file-item">`;
        html += `<div class="file-info">`;
        html += `<i class="ri-file-line file-icon"></i>`;
        html += `<span class="file-name">${utils.escapeHtml(file.fileName || file.name)}</span>`;
        if (file.size) {
            html += `<span class="file-size">${utils.formatBytes(file.size)}</span>`;
        }
        html += `<span class="file-shared-by">分享者: ${utils.escapeHtml(file.sharedBy)}</span>`;
        html += `<span class="file-time">${utils.formatTime(file.shareTime)}</span>`;
        html += `</div>`;
        html += `<div class="file-actions">`;
        if (file.fileId) {
            html += `<button class="btn btn-sm btn-secondary" onclick="downloadFile('${file.fileId}')">下载</button>`;
        }
        if (file.shareUrl) {
            html += `<button class="btn btn-sm btn-primary" onclick="copyShareUrl('${file.shareUrl}')">复制链接</button>`;
        }
        html += `</div>`;
        html += `</div>`;
    });
    
    receivedFilesList.innerHTML = html;
}

function showShareModal(fileId) {
    if (fileId) {
        document.getElementById('share-file-id').value = fileId;
    }
    modalManager.open('share-modal');
}

async function shareFile() {
    const formData = modalManager.getFormData('share-modal');
    
    if (!formData.fileId) {
        utils.showError('请选择文件', '.storage-content');
        return;
    }
    
    if (!formData.target) {
        utils.showError('请输入分享目标', '.storage-content');
        return;
    }
    
    try {
        await buttonManager.executeWithLoading('share-btn', async () => {
            const response = await storageAPI.shareFile({
                fileId: formData.fileId,
                target: formData.target,
                targetType: formData.targetType || 'user',
                expireTime: formData.expireTime || ''
            });
            
            if (response.code === 200 || response.message === "success") {
                modalManager.close('share-modal');
                await loadSharedFiles();
                utils.showSuccess('分享成功', '.storage-content');
            } else {
                throw new Error(response.message || '分享失败');
            }
        });
    } catch (error) {
        console.error('分享文件失败:', error);
        utils.showError('分享文件失败', '.storage-content');
    }
}

function copyShareUrl(url) {
    navigator.clipboard.writeText(url).then(() => {
        utils.showSuccess('链接已复制到剪贴板', '.storage-content');
    }).catch(err => {
        console.error('复制链接失败:', err);
        utils.showError('复制链接失败', '.storage-content');
    });
}

function showUploadModal() {
    modalManager.open('upload-modal');
}

function showCreateFolderModal() {
    document.getElementById('edit-folder-id').value = '';
    document.getElementById('folder-modal-title').textContent = '新建文件夹';
    document.getElementById('folder-name').value = '';
    document.getElementById('folder-description').value = '';
    modalManager.open('create-folder-modal');
}

function editFolder(folderId, folderName, description) {
    document.getElementById('edit-folder-id').value = folderId;
    document.getElementById('folder-modal-title').textContent = '编辑文件夹';
    document.getElementById('folder-name').value = folderName;
    document.getElementById('folder-description').value = description;
    modalManager.open('create-folder-modal');
}

async function saveFolder() {
    const formData = modalManager.getFormData('create-folder-modal');
    const editId = document.getElementById('edit-folder-id').value;
    
    if (!formData.folderName || !formData.folderName.trim()) {
        utils.showError('请输入文件夹名称', '.storage-content');
        return;
    }
    
    try {
        await buttonManager.executeWithLoading('create-folder-btn', async () => {
            let response;
            if (editId) {
                response = await storageAPI.updateFolder({
                    id: editId,
                    name: formData.folderName.trim(),
                    description: formData.description || ''
                });
            } else {
                response = await storageAPI.createFolder({
                    parentId: formData.parentFolderId || '',
                    name: formData.folderName.trim(),
                    description: formData.description || ''
                });
            }
            
            if (response.code === 200 || response.message === "success") {
                modalManager.close('create-folder-modal');
                await loadFileBrowser(currentFolderId);
                utils.showSuccess(editId ? '文件夹更新成功' : '文件夹创建成功', '.storage-content');
            } else {
                throw new Error(response.message || '操作失败');
            }
        });
    } catch (error) {
        console.error('保存文件夹失败:', error);
        utils.showError('保存文件夹失败', '.storage-content');
    }
}

async function deleteFolder(folderId) {
    if (!utils.confirm('确定要删除此文件夹吗？文件夹内的所有文件也将被删除。')) {
        return;
    }
    
    try {
        const response = await storageAPI.deleteFolder(folderId);
        
        if (response.code === 200 || response.message === "success") {
            await loadFileBrowser(currentFolderId);
            utils.showSuccess('文件夹删除成功', '.storage-content');
        } else {
            throw new Error(response.message || '删除失败');
        }
    } catch (error) {
        console.error('删除文件夹失败:', error);
        utils.showError('删除文件夹失败', '.storage-content');
    }
}

function showRenameModal(fileId, fileName) {
    document.getElementById('rename-file-id').value = fileId;
    document.getElementById('rename-file-name').value = fileName;
    modalManager.open('rename-modal');
}

async function saveRename() {
    const fileId = document.getElementById('rename-file-id').value;
    const newName = document.getElementById('rename-file-name').value.trim();
    
    if (!newName) {
        utils.showError('请输入新名称', '.storage-content');
        return;
    }
    
    try {
        const response = await storageAPI.renameFile({
            id: fileId,
            newName: newName
        });
        
        if (response.code === 200 || response.message === "success") {
            modalManager.close('rename-modal');
            await loadFileBrowser(currentFolderId);
            utils.showSuccess('重命名成功', '.storage-content');
        } else {
            throw new Error(response.message || '重命名失败');
        }
    } catch (error) {
        console.error('重命名失败:', error);
        utils.showError('重命名失败', '.storage-content');
    }
}

function closeModal(modalId) {
    modalManager.close(modalId);
}

async function uploadFile() {
    const formData = modalManager.getFormData('upload-modal');
    
    if (!formData.file || formData.file.size === 0) {
        utils.showError('请选择文件', '.storage-content');
        return;
    }
    
    const uploadFormData = new FormData();
    uploadFormData.append('file', formData.file);
    uploadFormData.append('folderId', formData.folderId || '');
    uploadFormData.append('description', formData.description || '');
    
    try {
        await buttonManager.executeWithLoading('upload-btn', async () => {
            const response = await storageAPI.uploadFile(uploadFormData);
            
            if (response.code === 200 || response.message === "success") {
                modalManager.close('upload-modal');
                await loadFileBrowser(currentFolderId);
                await loadStorageSpace();
                utils.showSuccess('文件上传成功', '.storage-content');
            } else {
                throw new Error(response.message || '文件上传失败');
            }
        });
    } catch (error) {
        console.error('上传文件失败:', error);
        utils.showError('文件上传失败', '.storage-content');
    }
}

async function downloadFile(fileId) {
    try {
        const response = await storageAPI.downloadFile(fileId);
        
        if (response.code === 200 || response.message === "success") {
            utils.downloadFile(new Blob([response.data]), response.fileName || 'file');
        }
    } catch (error) {
        console.error('下载文件失败:', error);
        utils.showError('下载文件失败', '.storage-content');
    }
}

async function unshareFile(fileId) {
    try {
        const response = await storageAPI.unshareFile(fileId);
        
        if (response.code === 200 || response.message === "success") {
            await loadSharedFiles();
            utils.showSuccess('取消分享成功', '.storage-content');
        } else {
            throw new Error(response.message || '取消分享失败');
        }
    } catch (error) {
        console.error('取消分享失败:', error);
        utils.showError('取消分享失败', '.storage-content');
    }
}

async function cleanupStorage() {
    if (!utils.confirm('确定要清理缓存吗？')) {
        return;
    }
    
    try {
        await buttonManager.executeWithLoading('cleanup-btn', async () => {
            const response = await storageAPI.cleanupStorage();
            
            if (response.code === 200 || response.message === "success") {
                await loadStorageSpace();
                utils.showSuccess('缓存清理成功', '.storage-content');
            } else {
                throw new Error(response.message || '缓存清理失败');
            }
        });
    } catch (error) {
        console.error('清理缓存失败:', error);
        utils.showError('缓存清理失败', '.storage-content');
    }
}

async function refreshStorage() {
    try {
        await buttonManager.executeWithLoading('refresh-btn', async () => {
            await utils.parallel([
                loadStorageSpace(),
                loadFileBrowser(currentFolderId),
                loadSharedFiles(),
                loadReceivedFiles()
            ]);
            utils.showSuccess('刷新成功', '.storage-content');
        });
    } catch (error) {
        console.error('刷新失败:', error);
        utils.showError('刷新失败', '.storage-content');
    }
}

document.addEventListener('DOMContentLoaded', initStorageManagement);
