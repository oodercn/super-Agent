(function(global) {
    'use strict';

    var SyncDashboard = {
        allTasks: [],
        allSkills: [],
        selectedSkills: new Set(),

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('数据同步页面初始化完成');
                self.loadData();
            };
        },

        loadData: async function() {
            var self = this;
            await Promise.all([
                self.loadStatistics(),
                self.loadTasks(),
                self.loadSkills(),
                self.loadHistory()
            ]);
        },

        loadStatistics: async function() {
            try {
                var response = await fetch('/api/sync/statistics', { method: 'POST' });
                var rs = await response.json();
                if (rs.requestStatus === 200 || rs.code === 200) {
                    var data = rs.data;
                    document.getElementById('stat-total').textContent = data.totalTasks || 0;
                    document.getElementById('stat-completed').textContent = data.completedTasks || 0;
                    document.getElementById('stat-running').textContent = data.runningTasks || 0;
                    document.getElementById('stat-skills').textContent = data.totalSyncedSkills || 0;
                    document.getElementById('stat-rate').textContent = (data.successRate || 0).toFixed(1) + '%';
                }
            } catch (error) {
                console.error('加载统计信息失败:', error);
            }
        },

        loadTasks: async function() {
            var self = this;
            try {
                var response = await fetch('/api/sync/tasks/list', { method: 'POST' });
                var rs = await response.json();
                if (rs.requestStatus === 200 || rs.code === 200) {
                    self.allTasks = rs.data;
                    self.filterTasks();
                }
            } catch (error) {
                console.error('加载同步任务失败:', error);
                document.getElementById('task-table-body').innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: var(--ns-danger);">加载失败</td></tr>';
            }
        },

        filterTasks: function() {
            var statusFilter = document.getElementById('status-filter').value;
            var filtered = statusFilter === 'all' ? this.allTasks : this.allTasks.filter(function(t) {
                return t.status === statusFilter;
            });
            this.renderTasks(filtered);
        },

        renderTasks: function(tasks) {
            var tbody = document.getElementById('task-table-body');
            tbody.innerHTML = '';
            if (tasks.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px;">暂无数据</td></tr>';
                return;
            }
            var self = this;
            tasks.forEach(function(task) {
                var row = document.createElement('tr');
                var typeText = { upload: '上传', download: '下载', bidirectional: '双向' }[task.type] || task.type;
                var statusClass = { pending: 'status-pending', running: 'status-running', completed: 'status-completed', failed: 'status-failed', cancelled: 'status-cancelled' }[task.status] || '';
                var statusText = { pending: '待执行', running: '进行中', completed: '已完成', failed: '失败', cancelled: '已取消' }[task.status] || task.status;
                var actionsHtml = '';
                if (task.status === 'pending') {
                    actionsHtml = '<button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editTask(\'' + task.id + '\', \'' + task.name + '\', \'' + task.type + '\')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--primary" onclick="executeTask(\'' + task.id + '\')">执行</button>';
                }
                if (task.status === 'running') {
                    actionsHtml += '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="cancelTask(\'' + task.id + '\')">取消</button>';
                }
                actionsHtml += '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteTask(\'' + task.id + '\')">删除</button>';
                row.innerHTML = '<td>' + task.name + '</td><td>' + typeText + '</td><td><span class="status-indicator ' + statusClass + '">' + statusText + '</span></td><td><div class="progress-bar"><div class="progress-fill" style="width: ' + (task.progress || 0) + '%"></div><span class="progress-text">' + (task.processedItems || 0) + '/' + (task.totalItems || 0) + '</span></div></td><td>' + new Date(task.createTime).toLocaleString() + '</td><td>' + actionsHtml + '</td>';
                tbody.appendChild(row);
            });
        },

        loadSkills: async function() {
            var self = this;
            try {
                var response = await fetch('/api/sync/skills/syncable', { method: 'POST' });
                var result = await response.json();
                if (result.code === 200) {
                    self.allSkills = result.data;
                    self.renderSkills(self.allSkills);
                }
            } catch (error) {
                console.error('加载技能列表失败:', error);
            }
        },

        renderSkills: function(skills) {
            var grid = document.getElementById('skill-grid');
            grid.innerHTML = '';
            if (skills.length === 0) {
                grid.innerHTML = '<div style="text-align: center; padding: 40px;">暂无技能</div>';
                return;
            }
            var self = this;
            skills.forEach(function(skill) {
                var card = document.createElement('div');
                card.className = 'skill-card' + (self.selectedSkills.has(skill.id) ? ' selected' : '');
                var actionsHtml = !skill.synced
                    ? '<button class="nx-btn nx-btn--sm nx-btn--primary" onclick="uploadSkill(\'' + skill.id + '\')">上传</button>'
                    : '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="downloadSkill(\'' + skill.id + '\')">下载</button>';
                card.innerHTML = '<div class="skill-checkbox"><input type="checkbox" ' + (self.selectedSkills.has(skill.id) ? 'checked' : '') + ' onchange="toggleSkill(\'' + skill.id + '\')"></div><div class="skill-icon"><i class="ri-apps-line"></i></div><div class="skill-info"><h4>' + skill.name + '</h4><p>' + skill.description + '</p><div class="skill-meta"><span class="badge">' + skill.category + '</span><span>v' + skill.version + '</span>' + (skill.synced ? '<span class="badge badge-success">已同步</span>' : '') + '</div></div><div class="skill-actions">' + actionsHtml + '</div>';
                grid.appendChild(card);
            });
        },

        filterSkills: function() {
            var searchTerm = document.getElementById('skill-search').value.toLowerCase();
            var filtered = this.allSkills.filter(function(s) {
                return s.name.toLowerCase().includes(searchTerm) || s.description.toLowerCase().includes(searchTerm);
            });
            this.renderSkills(filtered);
        },

        toggleSkill: function(skillId) {
            if (this.selectedSkills.has(skillId)) {
                this.selectedSkills.delete(skillId);
            } else {
                this.selectedSkills.add(skillId);
            }
            this.renderSkills(this.allSkills);
        },

        loadHistory: async function() {
            try {
                var response = await fetch('/api/sync/skills/synced', { method: 'POST' });
                var result = await response.json();
                if (result.code === 200) {
                    this.renderHistory(result.data);
                }
            } catch (error) {
                console.error('加载历史记录失败:', error);
            }
        },

        renderHistory: function(skills) {
            var tbody = document.getElementById('history-table-body');
            tbody.innerHTML = '';
            if (skills.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; padding: 40px;">暂无历史记录</td></tr>';
                return;
            }
            skills.forEach(function(skill) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + skill.name + '</td><td>' + (skill.syncDirection === 'upload' ? '上传' : '下载') + '</td><td>' + new Date(skill.syncTime).toLocaleString() + '</td><td><span class="status-indicator ' + (skill.syncStatus === 'success' ? 'status-completed' : 'status-failed') + '">' + (skill.syncStatus === 'success' ? '成功' : '失败') + '</span></td>';
                tbody.appendChild(row);
            });
        },

        switchTab: function(tabName) {
            document.querySelectorAll('.tab-item').forEach(function(item) {
                item.classList.remove('active');
            });
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('active');
            });
            document.querySelector('[data-tab="' + tabName + '"]').classList.add('active');
            document.getElementById('tab-' + tabName).classList.add('active');
        },

        showSyncModal: function() {
            document.getElementById('edit-task-id').value = '';
            document.getElementById('sync-modal-title').textContent = '新建同步任务';
            document.getElementById('sync-name').value = '';
            document.getElementById('sync-type').value = 'upload';
            document.getElementById('sync-modal').classList.add('active');
        },

        editTask: function(taskId, taskName, taskType) {
            document.getElementById('edit-task-id').value = taskId;
            document.getElementById('sync-modal-title').textContent = '编辑同步任务';
            document.getElementById('sync-name').value = taskName;
            document.getElementById('sync-type').value = taskType;
            document.getElementById('sync-modal').classList.add('active');
        },

        closeModal: function() {
            document.getElementById('sync-modal').classList.remove('active');
            document.getElementById('edit-task-id').value = '';
            document.getElementById('sync-name').value = '';
        },

        createSync: async function() {
            var self = this;
            var editId = document.getElementById('edit-task-id').value;
            var name = document.getElementById('sync-name').value;
            var type = document.getElementById('sync-type').value;
            if (!name) {
                alert('请输入任务名称');
                return;
            }

            if (editId) {
                try {
                    var response = await fetch('/api/sync/tasks/update', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                        body: JSON.stringify({ id: editId, name: name, type: type })
                    });
                    var result = await response.json();
                    if (result.code === 200) {
                        self.closeModal();
                        await self.loadData();
                        alert('同步任务更新成功');
                    } else {
                        alert(result.message || '更新失败');
                    }
                } catch (error) {
                    alert('更新失败: ' + error.message);
                }
            } else {
                try {
                    var response = await fetch('/api/sync/tasks/create', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                        body: JSON.stringify({ name: name, type: type, source: '本地', target: '云端', totalItems: self.selectedSkills.size || 1 })
                    });
                    var result = await response.json();
                    if (result.code === 200) {
                        self.closeModal();
                        document.getElementById('sync-name').value = '';
                        await self.loadData();
                        alert('同步任务创建成功');
                    } else {
                        alert(result.message || '创建失败');
                    }
                } catch (error) {
                    alert('创建失败: ' + error.message);
                }
            }
        },

        executeTask: async function(taskId) {
            var self = this;
            try {
                var response = await fetch('/api/sync/tasks/execute', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ id: taskId })
                });
                var result = await response.json();
                if (result.code === 200) {
                    await self.loadData();
                    alert('任务已开始执行');
                } else {
                    alert(result.message || '执行失败');
                }
            } catch (error) {
                alert('执行失败: ' + error.message);
            }
        },

        cancelTask: async function(taskId) {
            var self = this;
            try {
                var response = await fetch('/api/sync/tasks/cancel', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ id: taskId })
                });
                var result = await response.json();
                if (result.code === 200) {
                    await self.loadData();
                    alert('任务已取消');
                } else {
                    alert(result.message || '取消失败');
                }
            } catch (error) {
                alert('取消失败: ' + error.message);
            }
        },

        deleteTask: async function(taskId) {
            var self = this;
            if (!confirm('确定要删除此任务吗？')) return;
            try {
                var response = await fetch('/api/sync/tasks/delete', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ id: taskId })
                });
                var result = await response.json();
                if (result.code === 200) {
                    await self.loadData();
                    alert('删除成功');
                } else {
                    alert(result.message || '删除失败');
                }
            } catch (error) {
                alert('删除失败: ' + error.message);
            }
        },

        uploadSkill: async function(skillId) {
            var self = this;
            try {
                var response = await fetch('/api/sync/skills/upload', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ id: skillId, target: '云端' })
                });
                var result = await response.json();
                if (result.code === 200) {
                    await self.loadData();
                    alert('上传任务已创建');
                } else {
                    alert(result.message || '上传失败');
                }
            } catch (error) {
                alert('上传失败: ' + error.message);
            }
        },

        downloadSkill: async function(skillId) {
            var self = this;
            try {
                var response = await fetch('/api/sync/skills/download', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ id: skillId, source: '云端' })
                });
                var result = await response.json();
                if (result.code === 200) {
                    await self.loadData();
                    alert('下载任务已创建');
                } else {
                    alert(result.message || '下载失败');
                }
            } catch (error) {
                alert('下载失败: ' + error.message);
            }
        },

        batchSync: async function() {
            var self = this;
            if (self.selectedSkills.size === 0) {
                alert('请至少选择一个技能');
                return;
            }
            try {
                var response = await fetch('/api/sync/batch', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                    body: JSON.stringify({ skillIds: Array.from(self.selectedSkills), type: 'bidirectional' })
                });
                var result = await response.json();
                if (result.code === 200) {
                    self.selectedSkills.clear();
                    await self.loadData();
                    alert('批量同步任务已创建');
                } else {
                    alert(result.message || '批量同步失败');
                }
            } catch (error) {
                alert('批量同步失败: ' + error.message);
            }
        },

        refreshData: function() {
            this.loadData();
        }
    };

    SyncDashboard.init();

    global.loadData = function() { SyncDashboard.loadData(); };
    global.loadStatistics = function() { SyncDashboard.loadStatistics(); };
    global.loadTasks = function() { SyncDashboard.loadTasks(); };
    global.filterTasks = function() { SyncDashboard.filterTasks(); };
    global.loadSkills = function() { SyncDashboard.loadSkills(); };
    global.filterSkills = function() { SyncDashboard.filterSkills(); };
    global.toggleSkill = function(skillId) { SyncDashboard.toggleSkill(skillId); };
    global.loadHistory = function() { SyncDashboard.loadHistory(); };
    global.switchTab = function(tabName) { SyncDashboard.switchTab(tabName); };
    global.showSyncModal = function() { SyncDashboard.showSyncModal(); };
    global.editTask = function(taskId, taskName, taskType) { SyncDashboard.editTask(taskId, taskName, taskType); };
    global.closeModal = function() { SyncDashboard.closeModal(); };
    global.createSync = function() { SyncDashboard.createSync(); };
    global.executeTask = function(taskId) { SyncDashboard.executeTask(taskId); };
    global.cancelTask = function(taskId) { SyncDashboard.cancelTask(taskId); };
    global.deleteTask = function(taskId) { SyncDashboard.deleteTask(taskId); };
    global.uploadSkill = function(skillId) { SyncDashboard.uploadSkill(skillId); };
    global.downloadSkill = function(skillId) { SyncDashboard.downloadSkill(skillId); };
    global.batchSync = function() { SyncDashboard.batchSync(); };
    global.refreshData = function() { SyncDashboard.refreshData(); };

})(typeof window !== 'undefined' ? window : this);
