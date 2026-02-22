(function() {
    'use strict';

    var receivedFiles = [];

    function init() {
        console.log('接收分享页面初始化完成');
        loadReceivedFiles();
        bindEvents();
    }

    function bindEvents() {
        document.getElementById('search-input').addEventListener('input', debounce(filterFiles, 300));
        document.getElementById('status-filter').addEventListener('change', filterFiles);
    }

    async function loadReceivedFiles() {
        var filesList = document.getElementById('files-list');
        filesList.innerHTML = '<div class="loading-state"><i class="ri-loader-4-line ri-spin"></i><p>加载中...</p></div>';

        try {
            var response = await fetch('/api/storage/received/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            var result = await response.json();

            if (result.success) {
                receivedFiles = result.data && result.data.receivedFiles ? result.data.receivedFiles : [];
                renderFiles(receivedFiles);
            } else {
                filesList.innerHTML = '<div class="empty-state"><i class="ri-error-warning-line"></i><p>加载失败: ' + (result.message || '未知错误') + '</p></div>';
            }
        } catch (error) {
            console.error('加载接收文件列表失败:', error);
            filesList.innerHTML = '<div class="empty-state"><i class="ri-error-warning-line"></i><p>加载失败，请稍后重试</p></div>';
        }
    }

    function renderFiles(files) {
        var filesList = document.getElementById('files-list');

        if (files.length === 0) {
            filesList.innerHTML = '<div class="empty-state"><i class="ri-inbox-line"></i><p>暂无接收的文件</p></div>';
            return;
        }

        var html = '<div class="files-grid">';
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var statusClass = getStatusClass(file.status || 'pending');
            var statusText = getStatusText(file.status || 'pending');
            var fileIcon = getFileIcon(file.type);
            var fileName = file.fileName || file.name || '未知文件';
            var sender = file.sharedBy || file.sender || '未知';
            var sendTime = file.shareTime || file.sendTime;

            html += '<div class="file-card ' + statusClass + '">' +
                '<div class="file-header">' +
                    '<i class="' + fileIcon + ' file-icon"></i>' +
                    '<span class="file-status ' + statusClass + '">' + statusText + '</span>' +
                '</div>' +
                '<div class="file-info">' +
                    '<h3 class="file-name" title="' + fileName + '">' + fileName + '</h3>' +
                    '<p class="file-meta">' +
                        '<span><i class="ri-user-line"></i> ' + sender + '</span> ' +
                        '<span><i class="ri-calendar-line"></i> ' + formatDate(sendTime) + '</span>' +
                    '</p>' +
                    '<p class="file-size">' + formatFileSize(file.size) + '</p>' +
                '</div>' +
                '<div class="file-actions">' +
                    (file.status === 'pending' || !file.status ? 
                        '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="ReceivedFiles.acceptFile(\'' + file.id + '\')"><i class="ri-check-line"></i> 接收</button> ' +
                        '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="ReceivedFiles.rejectFile(\'' + file.id + '\')"><i class="ri-close-line"></i> 拒绝</button>' : '') +
                    (file.status === 'received' ? 
                        '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="ReceivedFiles.downloadFile(\'' + file.id + '\')"><i class="ri-download-line"></i> 下载</button>' : '') +
                '</div>' +
            '</div>';
        }
        html += '</div>';
        filesList.innerHTML = html;
    }

    function filterFiles() {
        var keyword = document.getElementById('search-input').value.toLowerCase();
        var status = document.getElementById('status-filter').value;

        var filtered = receivedFiles;

        if (keyword) {
            filtered = filtered.filter(function(f) {
                return (f.name && f.name.toLowerCase().indexOf(keyword) !== -1) ||
                       (f.sender && f.sender.toLowerCase().indexOf(keyword) !== -1);
            });
        }

        if (status !== 'all') {
            filtered = filtered.filter(function(f) { return f.status === status; });
        }

        renderFiles(filtered);
    }

    function getStatusClass(status) {
        var statusMap = {
            'pending': 'status-pending',
            'received': 'status-received',
            'expired': 'status-expired',
            'rejected': 'status-rejected'
        };
        return statusMap[status] || 'status-pending';
    }

    function getStatusText(status) {
        var statusMap = {
            'pending': '待接收',
            'received': '已接收',
            'expired': '已过期',
            'rejected': '已拒绝'
        };
        return statusMap[status] || status;
    }

    function getFileIcon(type) {
        var iconMap = {
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
        var date = new Date(dateStr);
        return date.toLocaleString('zh-CN');
    }

    function formatFileSize(bytes) {
        if (!bytes || bytes === 0) return '0 B';
        var k = 1024;
        var sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
        var i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    async function acceptFile(fileId) {
        try {
            var response = await fetch('/api/storage/received/' + fileId + '/accept', { method: 'POST' });
            var result = await response.json();

            if (result.success) {
                showNotification('文件接收成功', 'success');
                loadReceivedFiles();
            } else {
                showNotification(result.message || '接收失败', 'error');
            }
        } catch (error) {
            console.error('接收文件失败:', error);
            showNotification('接收失败，请稍后重试', 'error');
        }
    }

    async function rejectFile(fileId) {
        if (!confirm('确定要拒绝接收此文件吗？')) return;

        try {
            var response = await fetch('/api/storage/received/' + fileId + '/reject', { method: 'POST' });
            var result = await response.json();

            if (result.success) {
                showNotification('已拒绝接收文件', 'success');
                loadReceivedFiles();
            } else {
                showNotification(result.message || '操作失败', 'error');
            }
        } catch (error) {
            console.error('拒绝文件失败:', error);
            showNotification('操作失败，请稍后重试', 'error');
        }
    }

    async function downloadFile(fileId) {
        try {
            var response = await fetch('/api/storage/received/' + fileId + '/download');
            if (response.ok) {
                var blob = await response.blob();
                var url = URL.createObjectURL(blob);
                var a = document.createElement('a');
                a.href = url;
                a.download = 'file';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            } else {
                showNotification('下载失败', 'error');
            }
        } catch (error) {
            console.error('下载文件失败:', error);
            showNotification('下载失败，请稍后重试', 'error');
        }
    }

    function showNotification(message, type) {
        var notification = document.createElement('div');
        notification.className = 'notification ' + type;
        notification.innerHTML = '<i class="ri-' + (type === 'success' ? 'check' : 'error-warning') + '-line"></i><span>' + message + '</span>';
        document.body.appendChild(notification);

        setTimeout(function() { notification.classList.add('show'); }, 10);
        setTimeout(function() {
            notification.classList.remove('show');
            setTimeout(function() { document.body.removeChild(notification); }, 300);
        }, 3000);
    }

    function debounce(func, wait) {
        var timeout;
        return function() {
            var args = arguments;
            var later = function() {
                clearTimeout(timeout);
                func.apply(null, args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    window.ReceivedFiles = {
        init: init,
        acceptFile: acceptFile,
        rejectFile: rejectFile,
        downloadFile: downloadFile
    };

    window.onPageInit = init;
})();
