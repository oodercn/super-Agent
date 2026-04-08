(function(global) {
    'use strict';

    var sceneGroupId = null;
    var sceneGroup = null;
    var boundKnowledgeBases = [];
    var availableKnowledgeBases = [];
    var selectedKb = null;

    var LAYER_CONFIG = {
        'SCENE': { name: '场景层', desc: '当前场景独有', color: '#d97706', bgColor: '#fef3c7' },
        'PROFESSIONAL': { name: '专业层', desc: '特定领域', color: '#2563eb', bgColor: '#dbeafe' },
        'GENERAL': { name: '通用层', desc: '所有场景共享', color: '#059669', bgColor: '#d1fae5' }
    };

    var SceneKnowledge = {
        init: function() {
            var urlParams = new URLSearchParams(window.location.search);
            sceneGroupId = urlParams.get('sceneGroupId');

            if (!sceneGroupId) {
                document.getElementById('sceneGroupInfo').textContent = '未指定场景组';
                return;
            }

            SceneKnowledge.loadSceneGroup();
            SceneKnowledge.loadBoundKnowledgeBases();
            SceneKnowledge.loadAvailableKnowledgeBases();
        },

        loadSceneGroup: function() {
            ApiClient.get('/api/v1/scene-groups/' + sceneGroupId)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        sceneGroup = result.data;
                        document.getElementById('sceneGroupInfo').textContent = 
                            sceneGroup.name + ' (' + sceneGroupId + ')';
                    } else {
                        document.getElementById('sceneGroupInfo').textContent = '场景组加载失败';
                    }
                })
                .catch(function(error) {
                    document.getElementById('sceneGroupInfo').textContent = '场景组加载失败: ' + error.message;
                });
        },

        loadBoundKnowledgeBases: function() {
            ApiClient.get('/api/v1/scene-groups/' + sceneGroupId + '/knowledge')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        boundKnowledgeBases = result.data || [];
                        SceneKnowledge.renderBoundList();
                    }
                })
                .catch(function(error) {
                    document.getElementById('boundList').innerHTML = 
                        '<div class="empty-state"><i class="ri-error-warning-line"></i><span>加载失败</span></div>';
                });
        },

        loadAvailableKnowledgeBases: function() {
            ApiClient.get('/api/v1/knowledge-bases')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        availableKnowledgeBases = result.data;
                        SceneKnowledge.renderAvailableList();
                    }
                })
                .catch(function(error) {
                    document.getElementById('availableList').innerHTML = 
                        '<div class="empty-state"><i class="ri-error-warning-line"></i><span>加载失败</span></div>';
                });
        },

        renderBoundList: function() {
            var container = document.getElementById('boundList');
            document.getElementById('boundCount').textContent = boundKnowledgeBases.length;

            if (boundKnowledgeBases.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-book-2-line"></i><span>暂未绑定知识库</span></div>';
                return;
            }

            var html = '';
            boundKnowledgeBases.forEach(function(kb) {
                var layerConfig = LAYER_CONFIG[kb.layer] || LAYER_CONFIG['SCENE'];
                html += '<div class="knowledge-item">' +
                    '<div class="kb-icon bound"><i class="ri-book-2-line"></i></div>' +
                    '<div class="kb-info">' +
                    '<div class="kb-name">' + kb.name + '</div>' +
                    '<div class="kb-meta">' +
                    '<span class="kb-tag layer-' + kb.layer + '"><i class="ri-stack-line"></i> ' + layerConfig.name + '</span>' +
                    '<span class="kb-meta-item"><i class="ri-list-ordered"></i> TopK: ' + (kb.topK || 5) + '</span>' +
                    '<span class="kb-meta-item"><i class="ri-percent-line"></i> 阈值: ' + (kb.threshold || 0.7) + '</span>' +
                    '</div></div>' +
                    '<div class="kb-actions">' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="editBinding(\'' + kb.kbId + '\')">' +
                    '<i class="ri-edit-line"></i> 编辑</button>' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="unbindKnowledge(\'' + kb.kbId + '\')">' +
                    '<i class="ri-link-unlink"></i> 解绑</button>' +
                    '</div></div>';
            });
            container.innerHTML = html;
        },

        renderAvailableList: function() {
            var container = document.getElementById('availableList');
            var boundIds = boundKnowledgeBases.map(function(kb) { return kb.kbId; });
            var unbound = availableKnowledgeBases.filter(function(kb) { 
                return boundIds.indexOf(kb.kbId) === -1; 
            });

            if (unbound.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-checkbox-circle-line"></i><span>所有知识库已绑定</span></div>';
                return;
            }

            var html = '';
            unbound.forEach(function(kb) {
                html += '<div class="knowledge-item">' +
                    '<div class="kb-icon available"><i class="ri-database-2-line"></i></div>' +
                    '<div class="kb-info">' +
                    '<div class="kb-name">' + kb.name + '</div>' +
                    '<div class="kb-meta">' +
                    '<span class="kb-meta-item"><i class="ri-file-text-line"></i> ' + (kb.docCount || 0) + ' 文档</span>' +
                    '<span class="kb-meta-item"><i class="ri-calendar-line"></i> ' + SceneKnowledge.formatTime(kb.createTime) + '</span>' +
                    '</div></div>' +
                    '<div class="kb-actions">' +
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="openBindModal(\'' + kb.kbId + '\', \'' + kb.name + '\')">' +
                    '<i class="ri-link"></i> 绑定</button>' +
                    '</div></div>';
            });
            container.innerHTML = html;
        },

        openBindModal: function(kbId, kbName) {
            selectedKb = { kbId: kbId, name: kbName };
            document.getElementById('selectedKb').textContent = kbName + ' (' + kbId + ')';
            document.getElementById('layerSelect').value = 'SCENE';
            document.getElementById('topKInput').value = 5;
            document.getElementById('thresholdInput').value = 0.7;
            document.getElementById('bindModal').classList.add('open');
        },

        closeBindModal: function() {
            document.getElementById('bindModal').classList.remove('open');
            selectedKb = null;
        },

        confirmBind: function() {
            if (!selectedKb) return;

            var layer = document.getElementById('layerSelect').value;
            var topK = parseInt(document.getElementById('topKInput').value) || 5;
            var threshold = parseFloat(document.getElementById('thresholdInput').value) || 0.7;

            var binding = {
                kbId: selectedKb.kbId,
                name: selectedKb.name,
                layer: layer,
                topK: topK,
                threshold: threshold
            };

            ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/knowledge', binding)
                .then(function(result) {
                    if (result.status === 'success') {
                        SceneKnowledge.closeBindModal();
                        SceneKnowledge.loadBoundKnowledgeBases();
                        SceneKnowledge.loadAvailableKnowledgeBases();
                    } else {
                        alert('绑定失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('绑定失败: ' + error.message);
                });
        },

        editBinding: function(kbId) {
            var kb = boundKnowledgeBases.find(function(k) { return k.kbId === kbId; });
            if (!kb) return;

            selectedKb = { kbId: kb.kbId, name: kb.name };
            document.getElementById('selectedKb').textContent = kb.name + ' (' + kb.kbId + ')';
            document.getElementById('layerSelect').value = kb.layer || 'SCENE';
            document.getElementById('topKInput').value = kb.topK || 5;
            document.getElementById('thresholdInput').value = kb.threshold || 0.7;
            document.getElementById('bindModal').classList.add('open');
        },

        unbindKnowledge: function(kbId) {
            var kb = boundKnowledgeBases.find(function(k) { return k.kbId === kbId; });
            if (!kb) return;
            if (!confirm('确定要解绑此知识库吗？')) return;

            ApiClient.delete('/api/v1/scene-groups/' + sceneGroupId + '/knowledge/' + kbId + '?layer=' + (kb.layer || 'SCENE'))
                .then(function(result) {
                    if (result.status === 'success') {
                        SceneKnowledge.loadBoundKnowledgeBases();
                        SceneKnowledge.loadAvailableKnowledgeBases();
                    } else {
                        alert('解绑失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('解绑失败: ' + error.message);
                });
        },

        refreshAvailable: function() {
            SceneKnowledge.loadAvailableKnowledgeBases();
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleDateString('zh-CN');
        }
    };

    global.SceneKnowledge = SceneKnowledge;
    global.openBindModal = SceneKnowledge.openBindModal;
    global.closeBindModal = SceneKnowledge.closeBindModal;
    global.confirmBind = SceneKnowledge.confirmBind;
    global.editBinding = SceneKnowledge.editBinding;
    global.unbindKnowledge = SceneKnowledge.unbindKnowledge;
    global.refreshAvailable = SceneKnowledge.refreshAvailable;
    global.goBack = function() {
        window.history.back();
    };

    window.onPageInit = function() {
        SceneKnowledge.init();
    };

})(typeof window !== 'undefined' ? window : this);
