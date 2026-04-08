(function(global) {
    'use strict';

    var currentSceneId = null;
    var sceneData = null;

    var SceneDetail = {
        init: function() {
            window.onPageInit = function() {
                console.log('场景详情页面初始化完成');
                currentSceneId = SceneDetail.getSceneIdFromUrl();
                SceneDetail.initLlmAssistant();
                if (currentSceneId) {
                    SceneDetail.loadSceneDetail();
                } else {
                    alert('未指定场景ID');
                }
            };
        },

        initLlmAssistant: function() {
            if (typeof LlmAssistant !== 'undefined') {
                LlmAssistant.init();
            }
        },

        getSceneIdFromUrl: function() {
            var params = new URLSearchParams(window.location.search);
            return params.get('id') || params.get('sceneId');
        },

        loadSceneDetail: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/get', { sceneId: currentSceneId });
                
                if (result.status === 'success' && result.data) {
                    sceneData = result.data;
                    SceneDetail.renderSceneDetail(sceneData);
                    SceneDetail.loadCapabilities();
                    SceneDetail.loadCollaborativeScenes();
                } else {
                    alert('加载场景详情失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('加载场景详情失败:', error);
                alert('加载场景详情失败: ' + error.message);
            }
        },

        renderSceneDetail: function(scene) {
            document.getElementById('sceneName').textContent = scene.name || scene.sceneId;
            document.getElementById('sceneId').textContent = scene.sceneId;
            
            var statusClass = scene.active ? 'status-active' : 'status-inactive';
            var statusText = scene.active ? '活跃' : '非活跃';
            
            document.getElementById('capCount').textContent = scene.capabilities ? scene.capabilities.length : 0;
            document.getElementById('collabCount').textContent = scene.collaborativeScenes ? scene.collaborativeScenes.length : 0;
            document.getElementById('snapshotCount').textContent = '0';
            document.getElementById('version').textContent = scene.version || '-';
            
            document.getElementById('info-id').textContent = scene.sceneId;
            document.getElementById('info-name').textContent = scene.name || '-';
            document.getElementById('info-type').textContent = SceneDetail.getTypeName(scene.type);
            document.getElementById('info-status').innerHTML = '<span class="status-badge ' + statusClass + '">' + statusText + '</span>';
            document.getElementById('info-version').textContent = scene.version || '-';
            document.getElementById('info-created').textContent = scene.createTime ? new Date(scene.createTime).toLocaleString() : '-';
            document.getElementById('info-updated').textContent = scene.updateTime ? new Date(scene.updateTime).toLocaleString() : '-';
            document.getElementById('info-description').textContent = scene.description || '暂无描述';
            
            document.getElementById('configBasic').textContent = JSON.stringify(scene.config || {}, null, 2);
            document.getElementById('configVfs').textContent = JSON.stringify(scene.vfsConfig || {}, null, 2);
            document.getElementById('configAuth').textContent = JSON.stringify(scene.authConfig || {}, null, 2);
        },

        loadCapabilities: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/capabilities/list', { sceneId: currentSceneId, pageNum: 1, pageSize: 100 });
                
                if (result.status === 'success' && result.data) {
                    SceneDetail.renderCapabilities(result.data.list || []);
                }
            } catch (error) {
                console.error('加载能力列表失败:', error);
            }
        },

        renderCapabilities: function(capabilities) {
            var container = document.getElementById('capabilitiesList');
            
            if (!capabilities || capabilities.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-flashlight-line"></i><p>暂无能力</p></div>';
                return;
            }
            
            var html = '';
            capabilities.forEach(function(cap) {
                html += '<div class="capability-item">' +
                    '<div class="item-info">' +
                    '<div class="item-name">' + (cap.name || cap.capId) + '</div>' +
                    '<div class="item-id">' + cap.capId + '</div>' +
                    '<div class="item-desc">' + (cap.description || '-') + '</div>' +
                    '</div>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeCapability(\'' + cap.capId + '\')">' +
                    '<i class="ri-delete-bin-line"></i></button>' +
                    '</div>';
            });
            container.innerHTML = html;
        },

        loadCollaborativeScenes: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/collaborative/list', { sceneId: currentSceneId, pageNum: 1, pageSize: 100 });
                
                if (result.status === 'success' && result.data) {
                    SceneDetail.renderCollaborativeScenes(result.data.list || []);
                }
            } catch (error) {
                console.error('加载协作场景失败:', error);
            }
        },

        renderCollaborativeScenes: function(collabs) {
            var container = document.getElementById('collaborativeList');
            
            if (!collabs || collabs.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-group-line"></i><p>暂无协作场景</p></div>';
                return;
            }
            
            var html = '';
            collabs.forEach(function(sceneId) {
                html += '<div class="collab-item">' +
                    '<div class="item-info">' +
                    '<div class="item-name">协作场景</div>' +
                    '<div class="item-id">' + sceneId + '</div>' +
                    '</div>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeCollaborativeScene(\'' + sceneId + '\')">' +
                    '<i class="ri-delete-bin-line"></i></button>' +
                    '</div>';
            });
            container.innerHTML = html;
        },

        getTypeName: function(type) {
            var typeMap = {
                'primary': '主场景',
                'collaborative': '协作场景',
                'enterprise': '企业网络',
                'personal': '个人网络',
                'test': '测试网络',
                'development': '开发环境'
            };
            return typeMap[type] || type || '-';
        },

        switchTab: function(tab, btn) {
            document.querySelectorAll('.nx-tabs__tab').forEach(function(t) {
                t.classList.remove('nx-tabs__tab--active');
            });
            btn.classList.add('nx-tabs__tab--active');
            
            document.getElementById('overview-panel').style.display = tab === 'overview' ? 'block' : 'none';
            document.getElementById('capabilities-panel').style.display = tab === 'capabilities' ? 'block' : 'none';
            document.getElementById('collaborative-panel').style.display = tab === 'collaborative' ? 'block' : 'none';
            document.getElementById('snapshots-panel').style.display = tab === 'snapshots' ? 'block' : 'none';
            document.getElementById('config-panel').style.display = tab === 'config' ? 'block' : 'none';
            document.getElementById('logs-panel').style.display = tab === 'logs' ? 'block' : 'none';
            
            if (tab === 'snapshots') {
                SceneDetail.loadSnapshots();
            }
            if (tab === 'logs') {
                SceneDetail.loadLogs();
            }
        },

        activateScene: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/activate', { sceneId: currentSceneId });
                
                if (result.status === 'success') {
                    alert('场景已激活');
                    SceneDetail.loadSceneDetail();
                } else {
                    alert('激活失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('激活场景失败:', error);
                alert('激活失败: ' + error.message);
            }
        },

        deactivateScene: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/deactivate', { sceneId: currentSceneId });
                
                if (result.status === 'success') {
                    alert('场景已停用');
                    SceneDetail.loadSceneDetail();
                } else {
                    alert('停用失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('停用场景失败:', error);
                alert('停用失败: ' + error.message);
            }
        },

        showAddCapabilityModal: function() {
            document.getElementById('addCapabilityModal').style.display = 'flex';
        },

        hideAddCapabilityModal: function() {
            document.getElementById('addCapabilityModal').style.display = 'none';
            document.getElementById('capId').value = '';
            document.getElementById('capName').value = '';
            document.getElementById('capDesc').value = '';
        },

        addCapability: async function() {
            var capId = document.getElementById('capId').value;
            var capName = document.getElementById('capName').value;
            var capDesc = document.getElementById('capDesc').value;
            
            if (!capId) {
                alert('请输入能力ID');
                return;
            }
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/capabilities/add', {
                    sceneId: currentSceneId,
                    capability: {
                        capId: capId,
                        name: capName,
                        description: capDesc
                    }
                });
                
                if (result.status === 'success') {
                    SceneDetail.hideAddCapabilityModal();
                    SceneDetail.loadCapabilities();
                    alert('能力添加成功');
                } else {
                    alert('添加失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('添加能力失败:', error);
                alert('添加失败: ' + error.message);
            }
        },

        removeCapability: async function(capId) {
            if (!confirm('确定要移除该能力吗？')) return;
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/capabilities/remove', { sceneId: currentSceneId, capId: capId });
                
                if (result.status === 'success') {
                    SceneDetail.loadCapabilities();
                    alert('能力已移除');
                } else {
                    alert('移除失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('移除能力失败:', error);
                alert('移除失败: ' + error.message);
            }
        },

        showAddCollabModal: function() {
            document.getElementById('addCollabModal').style.display = 'flex';
        },

        hideAddCollabModal: function() {
            document.getElementById('addCollabModal').style.display = 'none';
            document.getElementById('collabSceneId').value = '';
        },

        addCollaborativeScene: async function() {
            var collabSceneId = document.getElementById('collabSceneId').value;
            
            if (!collabSceneId) {
                alert('请输入协作场景ID');
                return;
            }
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/collaborative/add', {
                    sceneId: currentSceneId,
                    collaborativeSceneId: collabSceneId
                });
                
                if (result.status === 'success') {
                    SceneDetail.hideAddCollabModal();
                    SceneDetail.loadCollaborativeScenes();
                    alert('协作场景添加成功');
                } else {
                    alert('添加失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('添加协作场景失败:', error);
                alert('添加失败: ' + error.message);
            }
        },

        removeCollaborativeScene: async function(collabSceneId) {
            if (!confirm('确定要移除该协作场景吗？')) return;
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/collaborative/remove', {
                    sceneId: currentSceneId,
                    collaborativeSceneId: collabSceneId
                });
                
                if (result.status === 'success') {
                    SceneDetail.loadCollaborativeScenes();
                    alert('协作场景已移除');
                } else {
                    alert('移除失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('移除协作场景失败:', error);
                alert('移除失败: ' + error.message);
            }
        },

        createSnapshot: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/snapshot/create', { sceneId: currentSceneId });
                
                if (result.status === 'success') {
                    alert('快照创建成功');
                    SceneDetail.loadSnapshots();
                } else {
                    alert('创建失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('创建快照失败:', error);
                alert('创建失败: ' + error.message);
            }
        },

        loadSnapshots: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/snapshot/list', { sceneId: currentSceneId, pageNum: 1, pageSize: 100 });
                
                if (result.status === 'success' && result.data) {
                    SceneDetail.renderSnapshots(result.data.list || []);
                    document.getElementById('snapshotCount').textContent = result.data.total || 0;
                }
            } catch (error) {
                console.error('加载快照列表失败:', error);
            }
        },

        renderSnapshots: function(snapshots) {
            var container = document.getElementById('snapshotsList');
            
            if (!snapshots || snapshots.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-camera-line"></i><p>暂无快照</p></div>';
                return;
            }
            
            var html = '';
            snapshots.forEach(function(snap) {
                var createTime = snap.createTime ? new Date(snap.createTime).toLocaleString() : '-';
                html += '<div class="snapshot-item">' +
                    '<div class="item-info">' +
                    '<div class="item-name">' + (snap.snapshotId || '快照') + '</div>' +
                    '<div class="item-id">创建时间: ' + createTime + '</div>' +
                    '<div class="item-desc">状态: ' + (snap.status || 'valid') + '</div>' +
                    '</div>' +
                    '<div class="nx-flex nx-gap-2">' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="restoreSnapshot(\'' + snap.snapshotId + '\')">' +
                    '<i class="ri-refresh-line"></i> 恢复</button>' +
                    '</div>' +
                    '</div>';
            });
            container.innerHTML = html;
        },

        restoreSnapshot: async function(snapshotId) {
            if (!confirm('确定要恢复到该快照吗？当前配置将被覆盖。')) return;
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/snapshot/restore', { 
                    sceneId: currentSceneId,
                    snapshot: { snapshotId: snapshotId, sceneId: currentSceneId }
                });
                
                if (result.status === 'success') {
                    alert('快照恢复成功');
                    SceneDetail.loadSceneDetail();
                } else {
                    alert('恢复失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('恢复快照失败:', error);
                alert('恢复失败: ' + error.message);
            }
        },

        loadLogs: async function() {
            var level = document.getElementById('logLevel').value;
            var startTime = document.getElementById('logStartTime').value;
            var endTime = document.getElementById('logEndTime').value;
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/logs', { 
                    sceneId: currentSceneId, 
                    level: level,
                    startTime: startTime ? new Date(startTime).getTime() : null,
                    endTime: endTime ? new Date(endTime).getTime() : null,
                    pageNum: 1, 
                    pageSize: 100 
                });
                
                if (result.status === 'success' && result.data) {
                    SceneDetail.renderLogs(result.data.list || []);
                } else {
                    SceneDetail.renderLogs([]);
                }
            } catch (error) {
                console.error('加载日志失败:', error);
                SceneDetail.renderLogs([]);
            }
        },

        renderLogs: function(logs) {
            var tbody = document.getElementById('logsTableBody');
            
            if (!logs || logs.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" class="nx-text-center nx-text-secondary">暂无日志</td></tr>';
                return;
            }
            
            var html = '';
            logs.forEach(function(log) {
                var levelClass = log.level === 'ERROR' ? 'nx-badge--danger' : 
                                 log.level === 'WARN' ? 'nx-badge--warning' : 
                                 log.level === 'DEBUG' ? 'nx-badge--secondary' : 'nx-badge--info';
                var time = log.time ? new Date(log.time).toLocaleString() : '-';
                html += '<tr>' +
                    '<td>' + time + '</td>' +
                    '<td><span class="nx-badge ' + levelClass + '">' + (log.level || 'INFO') + '</span></td>' +
                    '<td>' + (log.source || '-') + '</td>' +
                    '<td>' + (log.message || '-') + '</td>' +
                    '</tr>';
            });
            tbody.innerHTML = html;
        },

        renderMockLogs: function() {
            SceneDetail.renderLogs([]);
        },

        refreshLogs: function() {
            SceneDetail.loadLogs();
        },

        goBack: function() {
            window.location.href = '/console/pages/scene-management.html';
        }
    };

    SceneDetail.init();

    global.switchTab = SceneDetail.switchTab;
    global.activateScene = SceneDetail.activateScene;
    global.deactivateScene = SceneDetail.deactivateScene;
    global.showAddCapabilityModal = SceneDetail.showAddCapabilityModal;
    global.hideAddCapabilityModal = SceneDetail.hideAddCapabilityModal;
    global.addCapability = SceneDetail.addCapability;
    global.removeCapability = SceneDetail.removeCapability;
    global.showAddCollabModal = SceneDetail.showAddCollabModal;
    global.hideAddCollabModal = SceneDetail.hideAddCollabModal;
    global.addCollaborativeScene = SceneDetail.addCollaborativeScene;
    global.removeCollaborativeScene = SceneDetail.removeCollaborativeScene;
    global.createSnapshot = SceneDetail.createSnapshot;
    global.restoreSnapshot = SceneDetail.restoreSnapshot;
    global.loadLogs = SceneDetail.loadLogs;
    global.refreshLogs = SceneDetail.refreshLogs;
    global.goBack = SceneDetail.goBack;

})(typeof window !== 'undefined' ? window : this);
