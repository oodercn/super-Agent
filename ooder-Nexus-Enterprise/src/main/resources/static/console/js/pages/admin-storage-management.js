(function(global) {
    'use strict';

    var AdminStorage = {
        init: function() {
            window.onPageInit = function() {
                AdminStorage.loadStorageStatus();
                AdminStorage.loadBackupList();
                AdminStorage.loadRestoreList();
                AdminStorage.loadSettings();
            };
        },

        loadStorageStatus: function() {
            fetch('/api/admin/storage/status')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.renderStorageStatus(result.data);
                    } else {
                        AdminStorage.showError('加载存储状态失败');
                    }
                })
                .catch(function(error) {
                    console.error('加载存储状态错误:', error);
                    AdminStorage.showError('加载存储状态失败');
                });
        },

        renderStorageStatus: function(status) {
            var statusDiv = document.getElementById('storage-status');
            statusDiv.innerHTML = 
                '<div class="storage-overview">' +
                    '<div class="storage-card">' +
                        '<h4>总空间</h4>' +
                        '<p class="storage-value">' + AdminStorage.formatSize(status.totalSpace) + '</p>' +
                    '</div>' +
                    '<div class="storage-card">' +
                        '<h4>已使用</h4>' +
                        '<p class="storage-value">' + AdminStorage.formatSize(status.usedSpace) + '</p>' +
                    '</div>' +
                    '<div class="storage-card">' +
                        '<h4>可用空间</h4>' +
                        '<p class="storage-value">' + AdminStorage.formatSize(status.freeSpace) + '</p>' +
                    '</div>' +
                    '<div class="storage-card">' +
                        '<h4>使用率</h4>' +
                        '<p class="storage-value">' + status.usagePercent + '%</p>' +
                    '</div>' +
                '</div>' +
                '<div class="storage-details">' +
                    '<h4>存储详情</h4>' +
                    '<ul>' +
                        '<li><strong>技能文件:</strong> ' + AdminStorage.formatSize(status.skillFiles) + '</li>' +
                        '<li><strong>备份文件:</strong> ' + AdminStorage.formatSize(status.backupFiles) + '</li>' +
                        '<li><strong>日志文件:</strong> ' + AdminStorage.formatSize(status.logFiles) + '</li>' +
                        '<li><strong>临时文件:</strong> ' + AdminStorage.formatSize(status.tempFiles) + '</li>' +
                        '<li><strong>其他文件:</strong> ' + AdminStorage.formatSize(status.otherFiles) + '</li>' +
                    '</ul>' +
                '</div>';
        },

        loadBackupList: function() {
            fetch('/api/admin/storage/backups')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.renderBackupList(result.data);
                    } else {
                        AdminStorage.showError('加载备份列表失败');
                    }
                })
                .catch(function(error) {
                    console.error('加载备份列表错误:', error);
                    AdminStorage.showError('加载备份列表失败');
                });
        },

        renderBackupList: function(backups) {
            var backupList = document.getElementById('backup-list');
            backupList.innerHTML = '';
            
            backups.forEach(function(backup) {
                var backupItem = document.createElement('div');
                backupItem.className = 'backup-item';
                backupItem.innerHTML = 
                    '<div class="backup-info">' +
                        '<h3>' + backup.name + '</h3>' +
                        '<p><strong>大小:</strong> ' + AdminStorage.formatSize(backup.size) + '</p>' +
                        '<p><strong>创建时间:</strong> ' + AdminStorage.formatTime(backup.createdAt) + '</p>' +
                        '<p><strong>描述:</strong> ' + (backup.description || '无') + '</p>' +
                    '</div>' +
                    '<div class="backup-actions">' +
                        '<button class="btn btn-secondary" onclick="downloadBackup(\'' + backup.id + '\')">' +
                            '<i class="ri-download-line"></i> 下载' +
                        '</button>' +
                        '<button class="btn btn-primary" onclick="restoreFromBackup(\'' + backup.id + '\')">' +
                            '<i class="ri-refresh-line"></i> 恢复' +
                        '</button>' +
                        '<button class="btn btn-danger" onclick="deleteBackup(\'' + backup.id + '\')">' +
                            '<i class="ri-delete-line"></i> 删除' +
                        '</button>' +
                    '</div>';
                backupList.appendChild(backupItem);
            });
        },

        loadRestoreList: function() {
            fetch('/api/admin/storage/backups')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.renderRestoreList(result.data);
                    } else {
                        AdminStorage.showError('加载恢复列表失败');
                    }
                })
                .catch(function(error) {
                    console.error('加载恢复列表错误:', error);
                    AdminStorage.showError('加载恢复列表失败');
                });
        },

        renderRestoreList: function(backups) {
            var restoreList = document.getElementById('restore-list');
            restoreList.innerHTML = '';
            
            backups.forEach(function(backup) {
                var backupItem = document.createElement('div');
                backupItem.className = 'backup-item';
                backupItem.innerHTML = 
                    '<div class="backup-info">' +
                        '<h3>' + backup.name + '</h3>' +
                        '<p><strong>大小:</strong> ' + AdminStorage.formatSize(backup.size) + '</p>' +
                        '<p><strong>创建时间:</strong> ' + AdminStorage.formatTime(backup.createdAt) + '</p>' +
                    '</div>' +
                    '<div class="backup-actions">' +
                        '<button class="btn btn-primary" onclick="restoreFromBackup(\'' + backup.id + '\')">' +
                            '<i class="ri-refresh-line"></i> 恢复' +
                        '</button>' +
                    '</div>';
                restoreList.appendChild(backupItem);
            });
        },

        loadSettings: function() {
            fetch('/api/admin/storage/settings')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        var settings = result.data;
                        document.getElementById('storage-path').value = settings.storagePath || '';
                        document.getElementById('max-storage').value = settings.maxStorage || '';
                        document.getElementById('backup-retention').value = settings.backupRetention || '';
                        document.getElementById('auto-backup').value = settings.autoBackup || 'disabled';
                    }
                })
                .catch(function(error) {
                    console.error('加载设置错误:', error);
                });
        },

        switchTab: function(tab) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('active');
            });
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.style.display = 'none';
            });
            
            event.target.classList.add('active');
            document.getElementById(tab + '-tab').style.display = 'block';
        },

        createBackup: function() {
            fetch('/api/admin/storage/backup', {
                method: 'POST'
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.showSuccess('备份创建成功');
                        AdminStorage.loadBackupList();
                        AdminStorage.loadRestoreList();
                    } else {
                        AdminStorage.showError('备份创建失败');
                    }
                })
                .catch(function(error) {
                    console.error('创建备份错误:', error);
                    AdminStorage.showError('备份创建失败');
                });
        },

        downloadBackup: function(backupId) {
            fetch('/api/admin/storage/backups/' + backupId + '/download')
                .then(function(response) {
                    if (response.ok) {
                        return response.blob();
                    } else {
                        throw new Error('下载失败');
                    }
                })
                .then(function(blob) {
                    var url = window.URL.createObjectURL(blob);
                    var a = document.createElement('a');
                    a.href = url;
                    a.download = 'backup-' + backupId + '.zip';
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                })
                .catch(function(error) {
                    console.error('下载备份错误:', error);
                    AdminStorage.showError('下载备份失败');
                });
        },

        restoreFromBackup: function(backupId) {
            if (!confirm('确定要从此备份恢复吗？此操作将覆盖当前数据。')) {
                return;
            }
            
            fetch('/api/admin/storage/backups/' + backupId + '/restore', {
                method: 'POST'
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.showSuccess('数据恢复成功');
                        AdminStorage.loadStorageStatus();
                    } else {
                        AdminStorage.showError('数据恢复失败');
                    }
                })
                .catch(function(error) {
                    console.error('恢复数据错误:', error);
                    AdminStorage.showError('数据恢复失败');
                });
        },

        deleteBackup: function(backupId) {
            if (!confirm('确定要删除此备份吗？')) {
                return;
            }
            
            fetch('/api/admin/storage/backups/' + backupId, {
                method: 'DELETE'
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.showSuccess('备份删除成功');
                        AdminStorage.loadBackupList();
                        AdminStorage.loadRestoreList();
                    } else {
                        AdminStorage.showError('备份删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除备份错误:', error);
                    AdminStorage.showError('备份删除失败');
                });
        },

        performClean: function() {
            var cleanTemp = document.getElementById('clean-temp').checked;
            var cleanCache = document.getElementById('clean-cache').checked;
            var cleanLogs = document.getElementById('clean-logs').checked;
            var cleanOldBackups = document.getElementById('clean-old-backups').checked;
            
            if (!cleanTemp && !cleanCache && !cleanLogs && !cleanOldBackups) {
                AdminStorage.showError('请至少选择一个清理选项');
                return;
            }
            
            fetch('/api/admin/storage/clean', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    cleanTemp: cleanTemp,
                    cleanCache: cleanCache,
                    cleanLogs: cleanLogs,
                    cleanOldBackups: cleanOldBackups
                })
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.showSuccess('清理完成，释放空间: ' + AdminStorage.formatSize(result.data.freedSpace));
                        AdminStorage.loadStorageStatus();
                    } else {
                        AdminStorage.showError('清理失败');
                    }
                })
                .catch(function(error) {
                    console.error('清理错误:', error);
                    AdminStorage.showError('清理失败');
                });
        },

        saveSettings: function() {
            var storagePath = document.getElementById('storage-path').value;
            var maxStorage = document.getElementById('max-storage').value;
            var backupRetention = document.getElementById('backup-retention').value;
            var autoBackup = document.getElementById('auto-backup').value;
            
            fetch('/api/admin/storage/settings', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    storagePath: storagePath,
                    maxStorage: maxStorage,
                    backupRetention: backupRetention,
                    autoBackup: autoBackup
                })
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        AdminStorage.showSuccess('设置保存成功');
                    } else {
                        AdminStorage.showError('设置保存失败');
                    }
                })
                .catch(function(error) {
                    console.error('保存设置错误:', error);
                    AdminStorage.showError('设置保存失败');
                });
        },

        formatSize: function(bytes) {
            if (bytes === 0) return '0 B';
            var k = 1024;
            var sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
            var i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
        },

        formatTime: function(timestamp) {
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) {
            AdminStorage.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            AdminStorage.showNotification(message, 'success');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() {
                notification.remove();
            }, 3000);
        }
    };

    AdminStorage.init();

    global.switchTab = AdminStorage.switchTab;
    global.createBackup = AdminStorage.createBackup;
    global.downloadBackup = AdminStorage.downloadBackup;
    global.restoreFromBackup = AdminStorage.restoreFromBackup;
    global.deleteBackup = AdminStorage.deleteBackup;
    global.performClean = AdminStorage.performClean;
    global.saveSettings = AdminStorage.saveSettings;

})(typeof window !== 'undefined' ? window : this);
