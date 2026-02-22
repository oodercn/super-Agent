(function(global) {
    'use strict';

    var RemoteHosting = {
        remoteSkillsData: [],
        hostingData: [],
        monitoringData: [],

        init: function() {
            window.onPageInit = function() {
                RemoteHosting.loadRemoteSkills();
                RemoteHosting.loadHostingList();
                RemoteHosting.loadMonitoringList();
            };
        },

        loadRemoteSkills: function() {
            var remoteSkills = document.getElementById('remote-skills');
            remoteSkills.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/remote/skills/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        RemoteHosting.renderRemoteSkills(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载远程技能列表错误:', error);
                    document.getElementById('remote-skills').innerHTML = '<div class="empty-state">加载失败</div>';
                    RemoteHosting.showError('加载远程技能列表失败');
                });
        },

        renderRemoteSkills: function(skills) {
            RemoteHosting.remoteSkillsData = skills;
            var skillList = document.getElementById('remote-skills');
            skillList.innerHTML = '';

            if (skills.length === 0) {
                skillList.innerHTML = '<div class="empty-state">暂无远程技能</div>';
                return;
            }

            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = 
                    '<div class="skill-info">' +
                        '<h3>' + skill.name + '</h3>' +
                        '<p>' + (skill.description || '暂无描述') + '</p>' +
                        '<span class="skill-category">' + RemoteHosting.getCategoryName(skill.category) + '</span>' +
                        '<span class="skill-author">作者: ' + skill.author + '</span>' +
                        '<span class="skill-url">' + skill.remoteUrl + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                        '<button class="btn btn-secondary" onclick="viewSkill(\'' + skill.id + '\')">' +
                            '<i class="ri-eye-line"></i> 查看' +
                        '</button>' +
                        '<button class="btn btn-primary" onclick="syncSkill(\'' + skill.id + '\')">' +
                            '<i class="ri-refresh-line"></i> 同步' +
                        '</button>' +
                        '<button class="btn btn-danger" onclick="removeSkill(\'' + skill.id + '\')">' +
                            '<i class="ri-delete-line"></i> 移除' +
                        '</button>' +
                    '</div>';
                skillList.appendChild(skillItem);
            });
        },

        loadHostingList: function() {
            var hostingList = document.getElementById('hosting-list');
            hostingList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/remote/hosting/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        RemoteHosting.renderHostingList(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载托管列表错误:', error);
                    document.getElementById('hosting-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    RemoteHosting.showError('加载托管列表失败');
                });
        },

        renderHostingList: function(hostingList) {
            RemoteHosting.hostingData = hostingList;
            var list = document.getElementById('hosting-list');
            list.innerHTML = '';

            if (hostingList.length === 0) {
                list.innerHTML = '<div class="empty-state">暂无托管</div>';
                return;
            }

            hostingList.forEach(function(item) {
                var hostingItem = document.createElement('div');
                hostingItem.className = 'hosting-item';
                hostingItem.innerHTML = 
                    '<div class="hosting-info">' +
                        '<h3>' + item.skillName + '</h3>' +
                        '<p><strong>托管地址:</strong> ' + item.hostingUrl + '</p>' +
                        '<p><strong>状态:</strong> <span class="hosting-status ' + (item.status === 'active' ? 'success' : 'warning') + '">' + (item.status === 'active' ? '活跃' : '停用') + '</span></p>' +
                        '<p><strong>创建时间:</strong> ' + RemoteHosting.formatTime(item.createdAt) + '</p>' +
                    '</div>' +
                    '<div class="hosting-actions">' +
                        '<button class="btn btn-secondary" onclick="viewHosting(\'' + item.id + '\')">' +
                            '<i class="ri-eye-line"></i> 查看' +
                        '</button>' +
                        '<button class="btn btn-primary" onclick="toggleHosting(\'' + item.id + '\')">' +
                            '<i class="ri-toggle-line"></i> ' + (item.status === 'active' ? '停用' : '启用') +
                        '</button>' +
                        '<button class="btn btn-danger" onclick="removeHosting(\'' + item.id + '\')">' +
                            '<i class="ri-delete-line"></i> 移除' +
                        '</button>' +
                    '</div>';
                list.appendChild(hostingItem);
            });
        },

        loadMonitoringList: function() {
            var monitoringList = document.getElementById('monitoring-list');
            monitoringList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/remote/monitoring/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        RemoteHosting.renderMonitoringList(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载监控列表错误:', error);
                    document.getElementById('monitoring-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    RemoteHosting.showError('加载监控列表失败');
                });
        },

        renderMonitoringList: function(monitoringList) {
            RemoteHosting.monitoringData = monitoringList;
            var list = document.getElementById('monitoring-list');
            list.innerHTML = '';

            if (monitoringList.length === 0) {
                list.innerHTML = '<div class="empty-state">暂无监控</div>';
                return;
            }

            monitoringList.forEach(function(item) {
                var monitoringItem = document.createElement('div');
                monitoringItem.className = 'monitoring-item';
                monitoringItem.innerHTML = 
                    '<div class="monitoring-info">' +
                        '<h3>' + item.skillName + '</h3>' +
                        '<p><strong>监控地址:</strong> ' + item.monitoringUrl + '</p>' +
                        '<p><strong>状态:</strong> <span class="monitoring-status ' + (item.status === 'online' ? 'success' : 'failed') + '">' + (item.status === 'online' ? '在线' : '离线') + '</span></p>' +
                        '<p><strong>最后检查:</strong> ' + RemoteHosting.formatTime(item.lastChecked) + '</p>' +
                        '<p><strong>响应时间:</strong> ' + item.responseTime + 'ms</p>' +
                    '</div>' +
                    '<div class="monitoring-actions">' +
                        '<button class="btn btn-secondary" onclick="viewMonitoring(\'' + item.id + '\')">' +
                            '<i class="ri-eye-line"></i> 查看' +
                        '</button>' +
                        '<button class="btn btn-primary" onclick="checkMonitoring(\'' + item.id + '\')">' +
                            '<i class="ri-refresh-line"></i> 检查' +
                        '</button>' +
                        '<button class="btn btn-danger" onclick="removeMonitoring(\'' + item.id + '\')">' +
                            '<i class="ri-delete-line"></i> 移除' +
                        '</button>' +
                    '</div>';
                list.appendChild(monitoringItem);
            });
        },

        getCategoryName: function(category) {
            var categoryMap = {
                'text-processing': '文本处理',
                'development': '开发工具',
                'deployment': '部署工具',
                'media': '媒体处理',
                'storage': '存储管理'
            };
            return categoryMap[category] || category;
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

        viewSkill: function(skillId) {
            var skill = RemoteHosting.remoteSkillsData.find(function(s) {
                return s.id === skillId;
            });
            if (skill) {
                document.getElementById('view-skill-name').textContent = skill.name || '-';
                document.getElementById('view-skill-category').textContent = RemoteHosting.getCategoryName(skill.category);
                document.getElementById('view-skill-status').textContent = skill.status || '-';
                document.getElementById('view-skill-lastSync').textContent = skill.lastSync || '-';
                document.getElementById('view-skill-modal').style.display = 'flex';
            }
        },

        closeViewSkillModal: function() {
            document.getElementById('view-skill-modal').style.display = 'none';
        },

        syncSkill: function(skillId) {
            fetch('/api/admin/remote/skills/' + skillId + '/sync', {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('技能同步成功');
                        RemoteHosting.loadRemoteSkills();
                    } else {
                        RemoteHosting.showError('技能同步失败');
                    }
                })
                .catch(function(error) {
                    console.error('同步技能错误:', error);
                    RemoteHosting.showError('技能同步失败');
                });
        },

        removeSkill: function(skillId) {
            if (!confirm('确定要移除此技能吗？')) {
                return;
            }

            fetch('/api/admin/remote/skills/' + skillId, {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('技能移除成功');
                        RemoteHosting.loadRemoteSkills();
                    } else {
                        RemoteHosting.showError('技能移除失败');
                    }
                })
                .catch(function(error) {
                    console.error('移除技能错误:', error);
                    RemoteHosting.showError('技能移除失败');
                });
        },

        viewHosting: function(hostingId) {
            var hosting = RemoteHosting.hostingData.find(function(h) {
                return h.id === hostingId;
            });
            if (hosting) {
                document.getElementById('view-hosting-name').textContent = hosting.name || '-';
                document.getElementById('view-hosting-provider').textContent = hosting.provider || '-';
                document.getElementById('view-hosting-status').textContent = hosting.status || '-';
                document.getElementById('view-hosting-region').textContent = hosting.region || '-';
                document.getElementById('view-hosting-modal').style.display = 'flex';
            }
        },

        closeViewHostingModal: function() {
            document.getElementById('view-hosting-modal').style.display = 'none';
        },

        toggleHosting: function(hostingId) {
            fetch('/api/admin/remote/hosting/' + hostingId + '/toggle', {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('托管状态切换成功');
                        RemoteHosting.loadHostingList();
                    } else {
                        RemoteHosting.showError('托管状态切换失败');
                    }
                })
                .catch(function(error) {
                    console.error('切换托管状态错误:', error);
                    RemoteHosting.showError('托管状态切换失败');
                });
        },

        removeHosting: function(hostingId) {
            if (!confirm('确定要移除此托管吗？')) {
                return;
            }

            fetch('/api/admin/remote/hosting/' + hostingId, {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('托管移除成功');
                        RemoteHosting.loadHostingList();
                    } else {
                        RemoteHosting.showError('托管移除失败');
                    }
                })
                .catch(function(error) {
                    console.error('移除托管错误:', error);
                    RemoteHosting.showError('托管移除失败');
                });
        },

        viewMonitoring: function(monitoringId) {
            var monitoring = RemoteHosting.monitoringData.find(function(m) {
                return m.id === monitoringId;
            });
            if (monitoring) {
                document.getElementById('view-monitoring-name').textContent = monitoring.name || '-';
                document.getElementById('view-monitoring-type').textContent = monitoring.type || '-';
                document.getElementById('view-monitoring-status').textContent = monitoring.status || '-';
                document.getElementById('view-monitoring-score').textContent = monitoring.score || '-';
                document.getElementById('view-monitoring-modal').style.display = 'flex';
            }
        },

        closeViewMonitoringModal: function() {
            document.getElementById('view-monitoring-modal').style.display = 'none';
        },

        checkMonitoring: function(monitoringId) {
            fetch('/api/admin/remote/monitoring/' + monitoringId + '/check', {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('监控检查成功');
                        RemoteHosting.loadMonitoringList();
                    } else {
                        RemoteHosting.showError('监控检查失败');
                    }
                })
                .catch(function(error) {
                    console.error('检查监控错误:', error);
                    RemoteHosting.showError('监控检查失败');
                });
        },

        removeMonitoring: function(monitoringId) {
            if (!confirm('确定要移除此监控吗？')) {
                return;
            }

            fetch('/api/admin/remote/monitoring/' + monitoringId, {
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
                    if (result && result.status === 'success') {
                        RemoteHosting.showSuccess('监控移除成功');
                        RemoteHosting.loadMonitoringList();
                    } else {
                        RemoteHosting.showError('监控移除失败');
                    }
                })
                .catch(function(error) {
                    console.error('移除监控错误:', error);
                    RemoteHosting.showError('监控移除失败');
                });
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '未知';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) {
            RemoteHosting.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            RemoteHosting.showNotification(message, 'success');
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

    RemoteHosting.init();

    global.switchTab = RemoteHosting.switchTab;
    global.viewSkill = RemoteHosting.viewSkill;
    global.closeViewSkillModal = RemoteHosting.closeViewSkillModal;
    global.syncSkill = RemoteHosting.syncSkill;
    global.removeSkill = RemoteHosting.removeSkill;
    global.viewHosting = RemoteHosting.viewHosting;
    global.closeViewHostingModal = RemoteHosting.closeViewHostingModal;
    global.toggleHosting = RemoteHosting.toggleHosting;
    global.removeHosting = RemoteHosting.removeHosting;
    global.viewMonitoring = RemoteHosting.viewMonitoring;
    global.closeViewMonitoringModal = RemoteHosting.closeViewMonitoringModal;
    global.checkMonitoring = RemoteHosting.checkMonitoring;
    global.removeMonitoring = RemoteHosting.removeMonitoring;

})(typeof window !== 'undefined' ? window : this);
