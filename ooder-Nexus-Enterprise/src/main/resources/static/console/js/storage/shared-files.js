/**
 * 我的分享页面 JavaScript
 * 文件路径: js/storage/shared-files.js
 * 对应HTML: pages/storage/shared-files.html
 */

let sharedFiles = [];

document.addEventListener('DOMContentLoaded', function() {
    initMenu();
    loadSharedFiles();
    bindEvents();
});

function bindEvents() {
    document.getElementById('search-input').addEventListener('input', debounce(filterFiles, 300));
    document.getElementById('status-filter').addEventListener('change', filterFiles);
}

async function loadSharedFiles() {
    const filesList = document.getElementById('files-list');
    filesList.innerHTML = `
        <div class="loading-state">
            <i class="ri-loader-4-line ri-spin"></i>
            <p>加载中...</p>
        </div>
    `;
    
    try {
        const response = await fetch('/api/storage/shared');
        const result = await response.json();
        
        if (result.success) {
            sharedFiles = result.data || [];
            renderFiles(sharedFiles);
        } else {
            filesList.innerHTML = `
                <div class="empty-state">
                    <i class="ri-error-warning-line"></i>
                    <p>加载失败: ${result.message || '未知错误'}</p>
                </div>
            `;
        }
    } catch (error) {
        console.error('加载分享文件列表失败:', error);
        filesList.innerHTML = `
            <div class="empty-state">
                <i class="ri-error-warning-line"></i>
                <p>加载失败，请稍后重试</p>
            </div>
        `;
    }
}

function renderFiles(files) {
    const filesList = document.getElementById('files-list');
    
    if (files.length === 0) {
        filesList.innerHTML = `
            <div class="empty-state">
                <i class="ri-share-circle-line"></i>
                <p>暂无分享的文件</p>
            </div>
        `;
        return;
    }
    
    let html = '<div class="files-grid">';
    files.forEach(file => {
        const statusClass = getStatusClass(file.status);
        const statusText = getStatusText(file.status);
        const fileIcon = getFileIcon(file.type);
        
        html += `
            <div class="file-card ${statusClass}">
                <div class="file-header">
                    <i class="${fileIcon} file-icon"></i>
                    <span class="file-status ${statusClass}">${statusText}</span>
                </div>
                <div class="file-info">
                    <h3 class="file-name" title="${file.title || file.name}">${file.title || file.name}</h3>
                    <p class="file-meta">
                        <span><i class="ri-calendar-line"></i> ${formatDate(file.createTime)}</span>
                        <span><i class="ri-time-line"></i> 过期: ${formatDate(file.expireTime) || '永久'}</span>
                    </p>
                    <p class="file-size">${formatFileSize(file.size)}</p>
                    ${file.password ? '<p class="file-password"><i class="ri-lock-line"></i> 密码保护</p>' : ''}
                </div>
                <div class="file-actions">
                    ${file.status === 'active' ? `
                        <button class="btn btn-ghost btn-sm" onclick="editShare('${file.id}', '${escapeHtml(file.title || file.name)}', '${file.expiryDays || 7}', '${file.password || ''}')">
                            <i class="ri-edit-line"></i> 编辑
                        </button>
                        <button class="btn btn-primary btn-sm" onclick="copyShareLink('${file.id}')">
                            <i class="ri-link"></i> 复制链接
                        </button>
                        <button class="btn btn-secondary btn-sm" onclick="cancelShare('${file.id}')">
                            <i class="ri-close-line"></i> 取消
                        </button>
                    ` : `
                        <button class="btn btn-secondary btn-sm" onclick="deleteShare('${file.id}')">
                            <i class="ri-delete-bin-line"></i> 删除
                        </button>
                    `}
                </div>
            </div>
        `;
    });
    html += '</div>';
    filesList.innerHTML = html;
}

function filterFiles() {
    const keyword = document.getElementById('search-input').value.toLowerCase();
    const status = document.getElementById('status-filter').value;
    
    let filtered = sharedFiles;
    
    if (keyword) {
        filtered = filtered.filter(f => 
            (f.name && f.name.toLowerCase().includes(keyword)) ||
            (f.title && f.title.toLowerCase().includes(keyword))
        );
    }
    
    if (status !== 'all') {
        filtered = filtered.filter(f => f.status === status);
    }
    
    renderFiles(filtered);
}

function getStatusClass(status) {
    const statusMap = {
        'active': 'status-active',
        'expired': 'status-expired',
        'cancelled': 'status-cancelled'
    };
    return statusMap[status] || 'status-active';
}

function getStatusText(status) {
    const statusMap = {
        'active': '分享中',
        'expired': '已过期',
        'cancelled': '已取消'
    };
    return statusMap[status] || status;
}

function getFileIcon(type) {
    const iconMap = {
        'image': 'ri-image-line',
        'video': 'ri-video-line',
        'audio': 'ri-music-line',
        'document': 'ri-file-text-line',
        'archive': 'ri-archive-line'
    };
    return iconMap[type] || 'ri-file-line';
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN');
}

function formatFileSize(bytes) {
    if (!bytes || bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

function showShareModal() {
    document.getElementById('edit-share-id').value = '';
    document.getElementById('share-modal-title').textContent = '新建分享';
    document.getElementById('file-input-group').style.display = 'block';
    document.getElementById('share-file').value = '';
    document.getElementById('share-title').value = '';
    document.getElementById('share-expiry').value = '7';
    document.getElementById('share-password').value = '';
    document.getElementById('share-modal').style.display = 'flex';
}

function editShare(shareId, title, expiryDays, password) {
    document.getElementById('edit-share-id').value = shareId;
    document.getElementById('share-modal-title').textContent = '编辑分享';
    document.getElementById('file-input-group').style.display = 'none';
    document.getElementById('share-title').value = title;
    document.getElementById('share-expiry').value = expiryDays;
    document.getElementById('share-password').value = password;
    document.getElementById('share-modal').style.display = 'flex';
}

function closeShareModal() {
    document.getElementById('share-modal').style.display = 'none';
    document.getElementById('edit-share-id').value = '';
    document.getElementById('share-file').value = '';
    document.getElementById('share-title').value = '';
    document.getElementById('share-password').value = '';
}

async function saveShare() {
    const editId = document.getElementById('edit-share-id').value;
    const title = document.getElementById('share-title').value;
    const expiry = document.getElementById('share-expiry').value;
    const password = document.getElementById('share-password').value;
    
    if (editId) {
        await updateShare(editId, title, expiry, password);
    } else {
        await createShare();
    }
}

async function updateShare(shareId, title, expiryDays, password) {
    try {
        const response = await fetch(`/api/storage/share/${shareId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                expiryDays: parseInt(expiryDays),
                password: password
            })
        });
        const result = await response.json();
        
        if (result.success) {
            showNotification('分享更新成功', 'success');
            closeShareModal();
            loadSharedFiles();
        } else {
            showNotification(result.message || '更新失败', 'error');
        }
    } catch (error) {
        console.error('更新分享失败:', error);
        showNotification('更新失败，请稍后重试', 'error');
    }
}

async function createShare() {
    const fileInput = document.getElementById('share-file');
    const title = document.getElementById('share-title').value;
    const expiry = document.getElementById('share-expiry').value;
    const password = document.getElementById('share-password').value;
    
    if (!fileInput.files[0]) {
        showNotification('请选择要分享的文件', 'error');
        return;
    }
    
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('title', title);
    formData.append('expiryDays', expiry);
    if (password) formData.append('password', password);
    
    try {
        const response = await fetch('/api/storage/share', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        
        if (result.success) {
            showNotification('分享创建成功', 'success');
            closeShareModal();
            loadSharedFiles();
        } else {
            showNotification(result.message || '创建失败', 'error');
        }
    } catch (error) {
        console.error('创建分享失败:', error);
        showNotification('创建失败，请稍后重试', 'error');
    }
}

async function copyShareLink(shareId) {
    const shareUrl = `${window.location.origin}/api/storage/share/${shareId}`;
    try {
        await navigator.clipboard.writeText(shareUrl);
        showNotification('分享链接已复制', 'success');
    } catch (error) {
        console.error('复制链接失败:', error);
        showNotification('复制失败，请手动复制', 'error');
    }
}

async function cancelShare(shareId) {
    if (!confirm('确定要取消此分享吗？')) return;
    
    try {
        const response = await fetch(`/api/storage/share/${shareId}/cancel`, {
            method: 'POST'
        });
        const result = await response.json();
        
        if (result.success) {
            showNotification('分享已取消', 'success');
            loadSharedFiles();
        } else {
            showNotification(result.message || '操作失败', 'error');
        }
    } catch (error) {
        console.error('取消分享失败:', error);
        showNotification('操作失败，请稍后重试', 'error');
    }
}

async function deleteShare(shareId) {
    if (!confirm('确定要删除此分享记录吗？')) return;
    
    try {
        const response = await fetch(`/api/storage/share/${shareId}`, {
            method: 'DELETE'
        });
        const result = await response.json();
        
        if (result.success) {
            showNotification('分享记录已删除', 'success');
            loadSharedFiles();
        } else {
            showNotification(result.message || '删除失败', 'error');
        }
    } catch (error) {
        console.error('删除分享失败:', error);
        showNotification('删除失败，请稍后重试', 'error');
    }
}

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <i class="ri-${type === 'success' ? 'check' : 'error-warning'}-line"></i>
        <span>${message}</span>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}
