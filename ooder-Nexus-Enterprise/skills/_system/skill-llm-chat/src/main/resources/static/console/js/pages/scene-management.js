(function(global) {
    'use strict';

    var scenes = [];

    var SceneManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('场景管理页面初始化完成');
                SceneManagement.loadScenes();
                SceneManagement.initLlmAssistant();
            };
        },

        initLlmAssistant: function() {
            if (typeof LlmAssistant !== 'undefined') {
                LlmAssistant.init();
            }
        },

        loadScenes: async function() {
            try {
                var result = await ApiClient.post('/api/v1/scenes/list', { pageNum: 1, pageSize: 100 });
                
                if (result.status === 'success' && result.data) {
                    scenes = result.data.list || [];
                    SceneManagement.renderScenes(scenes);
                    SceneManagement.updateStatusOverview(scenes);
                } else {
                    console.error('加载场景数据失败:', result.message || '未知错误');
                    scenes = [];
                    SceneManagement.renderScenes(scenes);
                    SceneManagement.updateStatusOverview(scenes);
                }
            } catch (error) {
                console.error('加载场景数据失败:', error);
                scenes = [];
                SceneManagement.renderScenes(scenes);
                SceneManagement.updateStatusOverview(scenes);
            }
        },

        renderScenes: function(scenes) {
            var tbody = document.getElementById('sceneTableBody');
            tbody.innerHTML = '';
            
            if (!scenes || scenes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" class="nx-text-center nx-text-secondary">暂无数据</td></tr>';
                return;
            }
            
            scenes.forEach(function(scene) {
                var isActive = scene.active === true;
                var statusClass = isActive ? 'nx-badge--success' : 'nx-badge--secondary';
                var statusText = isActive ? '活跃' : '非活跃';
                
                var createdAt = scene.createTime 
                    ? new Date(scene.createTime).toISOString().split('T')[0] 
                    : '-';
                
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + scene.name + '</td>' +
                    '<td>' + SceneManagement.getTypeName(scene.type) + '</td>' +
                    '<td><span class="nx-badge ' + statusClass + '">' + statusText + '</span></td>' +
                    '<td>' + (scene.capabilities ? scene.capabilities.length : 0) + '</td>' +
                    '<td>' + createdAt + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="viewSceneDetail(\'' + scene.sceneId + '\')">详情</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteScene(\'' + scene.sceneId + '\')">删除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        getTypeName: function(type) {
            var typeMap = {
                'enterprise': '企业网络',
                'personal': '个人网络',
                'test': '测试网络',
                'development': '开发环境'
            };
            return typeMap[type] || type;
        },

        updateStatusOverview: function(scenes) {
            document.getElementById('totalScenes').textContent = scenes.length;
            var activeCount = 0;
            var types = {};
            scenes.forEach(function(s) {
                if (s.active === true) activeCount++;
                types[s.type] = true;
            });
            document.getElementById('activeScenes').textContent = activeCount;
            document.getElementById('sceneTypes').textContent = Object.keys(types).length;
        },

        createScene: function() {
            document.getElementById('modalTitle').textContent = '创建场景';
            document.getElementById('sceneForm').reset();
            document.getElementById('sceneModal').style.display = 'flex';
        },

        closeModal: function() {
            document.getElementById('sceneModal').style.display = 'none';
        },

        saveScene: async function() {
            var name = document.getElementById('sceneName').value;
            var type = document.getElementById('sceneType').value;
            var description = document.getElementById('sceneDescription').value;
            
            if (!name) {
                alert('请输入场景名称');
                return;
            }
            
            var sceneData = {
                name: name,
                type: type,
                description: description,
                version: '1.0.0'
            };
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/create', sceneData);
                
                if (result.status === 'success') {
                    SceneManagement.closeModal();
                    SceneManagement.loadScenes();
                    alert('场景创建成功');
                } else {
                    alert('创建失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('创建场景失败:', error);
                alert('创建失败: ' + error.message);
            }
        },

        viewSceneDetail: function(sceneId) {
            window.location.href = '/console/pages/scene-detail.html?id=' + sceneId;
        },

        deleteScene: async function(sceneId) {
            if (!confirm('确定要删除该场景吗？')) return;
            
            try {
                var result = await ApiClient.post('/api/v1/scenes/delete', { sceneId: sceneId });
                
                if (result.status === 'success') {
                    SceneManagement.loadScenes();
                    alert('场景已删除');
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            } catch (error) {
                console.error('删除场景失败:', error);
                alert('删除失败: ' + error.message);
            }
        },

        refreshScenes: function() {
            SceneManagement.loadScenes();
        }
    };

    SceneManagement.init();

    global.createScene = SceneManagement.createScene;
    global.closeModal = SceneManagement.closeModal;
    global.saveScene = SceneManagement.saveScene;
    global.viewSceneDetail = SceneManagement.viewSceneDetail;
    global.deleteScene = SceneManagement.deleteScene;
    global.refreshScenes = SceneManagement.refreshScenes;

})(typeof window !== 'undefined' ? window : this);
