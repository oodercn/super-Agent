(function(global) {
    'use strict';

    var currentSkillId = null;
    var skillsData = [];

    var InstalledSkills = {
        init: function() {
            window.onPageInit = function() {
                InstalledSkills.loadStatistics();
                InstalledSkills.loadSkillList();
            };
        },

        loadStatistics: async function() {
            try {
                var response = await fetch('/api/skillcenter/installed/statistics', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({})
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    document.getElementById('stat-total').textContent = rs.data.totalSkills || 0;
                    document.getElementById('stat-running').textContent = rs.data.runningSkills || 0;
                    document.getElementById('stat-stopped').textContent = rs.data.stoppedSkills || 0;
                    document.getElementById('stat-error').textContent = rs.data.errorSkills || 0;
                    document.getElementById('stat-runs').textContent = rs.data.totalRuns || 0;
                }
            } catch (error) {
                console.error('Error loading statistics:', error);
            }
        },

        loadSkillList: async function() {
            try {
                var response = await fetch('/api/skillcenter/installed/list', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({})
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    skillsData = rs.data || [];
                    InstalledSkills.renderSkillList(skillsData);
                } else {
                    InstalledSkills.showError(rs.message);
                }
            } catch (error) {
                console.error('Error loading skills:', error);
                InstalledSkills.showError('加载技能列表失败');
            }
        },

        renderSkillList: function(skills) {
            var container = document.getElementById('skill-grid');
            
            if (!skills || skills.length === 0) {
                container.innerHTML = '\
                    <div class="empty-state" style="grid-column: 1 / -1;">\
                        <i class="ri-apps-line"></i>\
                        <p>暂无已安装的技能</p>\
                        <button class="nx-btn nx-btn--primary" onclick="showInstallModal()" style="margin-top: 16px;">\
                            <i class="ri-download-line"></i> 安装技能\
                        </button>\
                    </div>\
                ';
                return;
            }

            container.innerHTML = skills.map(function(skill) {
                return '\
                <div class="skill-card ' + skill.status.toLowerCase() + '" data-id="' + skill.id + '">\
                    <div class="skill-header">\
                        <div class="skill-icon">\
                            <i class="' + (skill.icon || 'ri-apps-line') + '"></i>\
                        </div>\
                        <div class="skill-title">\
                            <div class="skill-name">' + InstalledSkills.escapeHtml(skill.skillName) + '</div>\
                            <div class="skill-version">v' + skill.version + '</div>\
                        </div>\
                        <span class="skill-status status-' + skill.status.toLowerCase() + '">' + InstalledSkills.getStatusText(skill.status) + '</span>\
                    </div>\
                    <div class="skill-desc">' + InstalledSkills.escapeHtml(skill.description || '暂无描述') + '</div>\
                    <div class="skill-meta">\
                        <div class="skill-meta-item">\
                            <i class="ri-folder-line"></i>\
                            <span>' + (skill.category || '未分类') + '</span>\
                        </div>\
                        <div class="skill-meta-item">\
                            <i class="ri-play-circle-line"></i>\
                            <span>' + (skill.runCount || 0) + ' 次运行</span>\
                        </div>\
                        <div class="skill-meta-item">\
                            <i class="ri-download-line"></i>\
                            <span>' + (skill.source === 'SKILLCENTER' ? '技能中心' : skill.source === 'LOCAL' ? '本地' : '外部') + '</span>\
                        </div>\
                    </div>\
                    <div class="skill-actions">\
                        ' + (skill.canRun ? '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="showRunModal(\'' + skill.id + '\')"><i class="ri-play-line"></i> 运行</button>' : '') + '\
                        ' + (skill.running ? '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="stopSkill(\'' + skill.id + '\')"><i class="ri-stop-line"></i> 停止</button>' : '') + '\
                        <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="showEditModal(\'' + skill.id + '\')"><i class="ri-edit-line"></i> 编辑</button>\
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="showConfigModal(\'' + skill.id + '\')"><i class="ri-settings-3-line"></i> 配置</button>\
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="showLogsModal(\'' + skill.id + '\')"><i class="ri-file-list-line"></i> 日志</button>\
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="showProtocolModal(\'' + skill.id + '\')"><i class="ri-link"></i> 关联</button>\
                        <button class="nx-btn nx-btn--ghost nx-btn--sm nx-btn--danger" onclick="event.stopPropagation(); uninstallSkill(\'' + skill.id + '\')" title="卸载"><i class="ri-delete-bin-line"></i></button>\
                    </div>\
                </div>\
                ';
            }).join('');
        },

        getStatusText: function(status) {
            var statusMap = {
                'INSTALLED': '已安装',
                'RUNNING': '运行中',
                'STOPPED': '已停止',
                'ERROR': '错误',
                'UPDATING': '更新中'
            };
            return statusMap[status] || status;
        },

        escapeHtml: function(text) {
            if (!text) return '';
            var div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        },

        showEditModal: function(skillId) {
            currentSkillId = skillId;
            var skill = skillsData.find(function(s) { return s.id === skillId; });
            if (!skill) {
                alert('技能不存在');
                return;
            }

            document.getElementById('edit-skill-id').value = skill.id;
            document.getElementById('edit-skill-name').value = skill.skillName || '';
            document.getElementById('edit-version').value = skill.version || '';
            document.getElementById('edit-description').value = skill.description || '';
            document.getElementById('edit-category').value = skill.category || 'utilities';
            document.getElementById('edit-author').value = skill.author || '';
            document.getElementById('edit-icon').value = skill.icon || '';

            InstalledSkills.openModal('edit-modal');
        },

        saveEditSkill: async function() {
            var id = document.getElementById('edit-skill-id').value;
            var skillName = document.getElementById('edit-skill-name').value;
            var version = document.getElementById('edit-version').value;
            var description = document.getElementById('edit-description').value;
            var category = document.getElementById('edit-category').value;
            var author = document.getElementById('edit-author').value;
            var icon = document.getElementById('edit-icon').value;

            if (!skillName) {
                alert('请输入技能名称');
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/installed/edit', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        id: id,
                        skillName: skillName,
                        version: version,
                        description: description,
                        category: category,
                        author: author,
                        icon: icon
                    })
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    InstalledSkills.closeModal('edit-modal');
                    InstalledSkills.loadSkillList();
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error editing skill:', error);
                alert('编辑失败');
            }
        },

        showInstallModal: function() {
            document.getElementById('install-form').reset();
            document.getElementById('install-version').value = '1.0.0';
            InstalledSkills.openModal('install-modal');
        },

        installSkill: async function() {
            var skillId = document.getElementById('install-skill-id').value || 'skill-' + Date.now();
            var skillName = document.getElementById('install-skill-name').value;
            var version = document.getElementById('install-version').value;
            var description = document.getElementById('install-description').value;
            var category = document.getElementById('install-category').value;
            var author = document.getElementById('install-author').value;

            if (!skillName) {
                alert('请输入技能名称');
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/installed/install', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        skillId: skillId,
                        skillName: skillName,
                        version: version,
                        description: description,
                        category: category,
                        author: author
                    })
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    InstalledSkills.closeModal('install-modal');
                    InstalledSkills.loadStatistics();
                    InstalledSkills.loadSkillList();
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error installing skill:', error);
                alert('安装失败');
            }
        },

        showRunModal: function(skillId) {
            currentSkillId = skillId;
            var skill = skillsData.find(function(s) { return s.id === skillId; });
            if (skill) {
                document.getElementById('run-skill-info').innerHTML = '\
                    <div style="display: flex; align-items: center; gap: 12px;">\
                        <div class="skill-icon" style="width: 40px; height: 40px; font-size: 20px;">\
                            <i class="' + (skill.icon || 'ri-apps-line') + '"></i>\
                        </div>\
                        <div>\
                            <div style="font-weight: 600; color: var(--ns-dark);">' + InstalledSkills.escapeHtml(skill.skillName) + '</div>\
                            <div style="font-size: 12px; color: var(--ns-secondary);">v' + skill.version + '</div>\
                        </div>\
                    </div>\
                ';
            }
            document.getElementById('run-input').value = '';
            document.getElementById('run-debug').checked = false;
            InstalledSkills.openModal('run-modal');
        },

        executeSkill: async function() {
            var input = document.getElementById('run-input').value;
            var debug = document.getElementById('run-debug').checked;

            try {
                var url = debug ? '/api/skillcenter/installed/debug' : '/api/skillcenter/installed/run';
                var response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        id: currentSkillId,
                        input: input,
                        debug: debug
                    })
                });
                var rs = await response.json();
                
                InstalledSkills.closeModal('run-modal');
                InstalledSkills.showResultModal(rs.data);
                InstalledSkills.loadStatistics();
                InstalledSkills.loadSkillList();
            } catch (error) {
                console.error('Error executing skill:', error);
                alert('执行失败');
            }
        },

        showResultModal: function(result) {
            var statusDiv = document.getElementById('result-status');
            if (result.success) {
                statusDiv.innerHTML = '\
                    <div style="display: flex; align-items: center; gap: 8px; color: var(--ns-success);">\
                        <i class="ri-check-circle-line" style="font-size: 20px;"></i>\
                        <span>执行成功</span>\
                        <span style="margin-left: auto; color: var(--ns-secondary);">耗时: ' + result.executionTime + 'ms</span>\
                    </div>\
                ';
            } else {
                statusDiv.innerHTML = '\
                    <div style="display: flex; align-items: center; gap: 8px; color: var(--ns-danger);">\
                        <i class="ri-error-warning-line" style="font-size: 20px;"></i>\
                        <span>执行失败: ' + InstalledSkills.escapeHtml(result.error || '未知错误') + '</span>\
                    </div>\
                ';
            }

            document.getElementById('result-output').textContent = result.output || '(无输出)';
            
            var logsContainer = document.getElementById('result-logs');
            if (result.logs && result.logs.length > 0) {
                logsContainer.innerHTML = result.logs.map(function(log) {
                    return '<div class="log-entry">' + InstalledSkills.escapeHtml(log) + '</div>';
                }).join('');
            } else {
                logsContainer.innerHTML = '<div class="log-entry">无日志</div>';
            }

            InstalledSkills.openModal('result-modal');
        },

        stopSkill: async function(skillId) {
            if (!confirm('确定要停止该技能吗？')) return;

            try {
                var response = await fetch('/api/skillcenter/installed/stop', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId })
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    InstalledSkills.loadStatistics();
                    InstalledSkills.loadSkillList();
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error stopping skill:', error);
                alert('停止失败');
            }
        },

        showConfigModal: async function(skillId) {
            currentSkillId = skillId;
            var skill = skillsData.find(function(s) { return s.id === skillId; });
            
            try {
                var response = await fetch('/api/skillcenter/installed/config/get', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId })
                });
                var rs = await response.json();
                
                if (rs.requestStatus === 200) {
                    document.getElementById('config-skill-info').innerHTML = '\
                        <div style="font-weight: 600; color: var(--ns-dark);">' + InstalledSkills.escapeHtml(skill ? skill.skillName : '技能配置') + '</div>\
                    ';
                    document.getElementById('config-json').value = JSON.stringify(rs.data.config || {}, null, 2);
                    InstalledSkills.openModal('config-modal');
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error loading config:', error);
                alert('加载配置失败');
            }
        },

        saveConfig: async function() {
            var configJson = document.getElementById('config-json').value;
            var config;
            
            try {
                config = JSON.parse(configJson);
            } catch (e) {
                alert('JSON 格式错误');
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/installed/config/update', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        id: currentSkillId,
                        config: config
                    })
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    InstalledSkills.closeModal('config-modal');
                    InstalledSkills.loadSkillList();
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error saving config:', error);
                alert('保存失败');
            }
        },

        showLogsModal: async function(skillId) {
            currentSkillId = skillId;
            var skill = skillsData.find(function(s) { return s.id === skillId; });
            
            document.getElementById('logs-skill-info').innerHTML = '\
                <div style="font-weight: 600; color: var(--ns-dark);">' + InstalledSkills.escapeHtml(skill ? skill.skillName : '技能日志') + '</div>\
            ';

            try {
                var response = await fetch('/api/skillcenter/installed/logs', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId, limit: 100 })
                });
                var rs = await response.json();
                
                var container = document.getElementById('logs-container');
                if (rs.requestStatus === 200 && rs.data && rs.data.length > 0) {
                    container.innerHTML = rs.data.map(function(log) {
                        var className = 'log-entry';
                        if (log.includes('[ERROR]') || log.includes('error')) className += ' error';
                        if (log.includes('[SUCCESS]') || log.includes('success')) className += ' success';
                        return '<div class="' + className + '">' + InstalledSkills.escapeHtml(log) + '</div>';
                    }).join('');
                } else {
                    container.innerHTML = '<div class="log-entry">暂无日志</div>';
                }
                
                InstalledSkills.openModal('logs-modal');
            } catch (error) {
                console.error('Error loading logs:', error);
                alert('加载日志失败');
            }
        },

        showProtocolModal: async function(skillId) {
            currentSkillId = skillId;
            
            try {
                var response = await fetch('/api/skillcenter/installed/protocol/associations', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ skillId: skillId })
                });
                var rs = await response.json();
                
                if (rs.requestStatus === 200) {
                    var data = rs.data;
                    document.getElementById('protocol-content').innerHTML = '\
                        <div style="margin-bottom: 16px;">\
                            <div style="font-weight: 600; color: var(--ns-dark);">' + InstalledSkills.escapeHtml(data.skillName) + '</div>\
                            <div style="font-size: 12px; color: var(--ns-secondary);">ID: ' + data.skillId + '</div>\
                        </div>\
                        \
                        <div class="protocol-section">\
                            <div class="protocol-title">\
                                <i class="ri-route-line"></i> RouteAgent 关联\
                                <span class="protocol-status ' + (data.routeAgent && data.routeAgent.connected ? 'protocol-connected' : 'protocol-disconnected') + '">\
                                    ' + (data.routeAgent && data.routeAgent.connected ? '已连接' : '未连接') + '\
                                </span>\
                            </div>\
                            ' + (data.routeAgent && data.routeAgent.connected ? '\
                                <div style="font-size: 13px; color: var(--ns-secondary); margin-bottom: 8px;">\
                                    Agent ID: ' + InstalledSkills.escapeHtml(data.routeAgent.agentId) + '\
                                </div>\
                            ' : '') + '\
                            <div style="font-size: 12px; color: var(--ns-secondary); margin-bottom: 8px;">支持的命令:</div>\
                            <div class="command-list">\
                                ' + (data.routeAgent && data.routeAgent.commands ? data.routeAgent.commands.map(function(cmd) {
                                    return '<span class="command-tag">' + cmd + '</span>';
                                }).join('') : '') + '\
                            </div>\
                        </div>\
                        \
                        <div class="protocol-section">\
                            <div class="protocol-title">\
                                <i class="ri-computer-line"></i> EndAgent 关联\
                                <span class="protocol-status ' + (data.endAgent && data.endAgent.connected ? 'protocol-connected' : 'protocol-disconnected') + '">\
                                    ' + (data.endAgent && data.endAgent.connected ? '已连接' : '未连接') + '\
                                </span>\
                            </div>\
                            ' + (data.endAgent && data.endAgent.connected ? '\
                                <div style="font-size: 13px; color: var(--ns-secondary); margin-bottom: 8px;">\
                                    Agent ID: ' + InstalledSkills.escapeHtml(data.endAgent.agentId) + '\
                                </div>\
                            ' : '') + '\
                            <div style="font-size: 12px; color: var(--ns-secondary); margin-bottom: 8px;">支持的命令:</div>\
                            <div class="command-list">\
                                ' + (data.endAgent && data.endAgent.commands ? data.endAgent.commands.map(function(cmd) {
                                    return '<span class="command-tag">' + cmd + '</span>';
                                }).join('') : '') + '\
                            </div>\
                        </div>\
                        \
                        ' + (data.requiredCapabilities && data.requiredCapabilities.length > 0 ? '\
                            <div class="protocol-section">\
                                <div class="protocol-title">\
                                    <i class="ri-lightbulb-line"></i> 所需能力\
                                </div>\
                                <div class="command-list">\
                                    ' + data.requiredCapabilities.map(function(cap) {
                                        return '<span class="command-tag">' + cap + '</span>';
                                    }).join('') + '\
                                </div>\
                            </div>\
                        ' : '') + '\
                    ';
                    InstalledSkills.openModal('protocol-modal');
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error loading protocol associations:', error);
                alert('加载协议关联失败');
            }
        },

        uninstallSkill: async function(skillId) {
            if (!confirm('确定要卸载该技能吗？此操作不可恢复。')) return;

            try {
                var response = await fetch('/api/skillcenter/installed/uninstall', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId })
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    InstalledSkills.loadStatistics();
                    InstalledSkills.loadSkillList();
                } else {
                    alert(rs.message);
                }
            } catch (error) {
                console.error('Error uninstalling skill:', error);
                alert('卸载失败');
            }
        },

        refreshSkillList: function() {
            InstalledSkills.loadStatistics();
            InstalledSkills.loadSkillList();
        },

        openModal: function(modalId) {
            document.getElementById(modalId).classList.add('active');
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).classList.remove('active');
        },

        showError: function(message) {
            var container = document.getElementById('skill-grid');
            container.innerHTML = '\
                <div class="empty-state" style="grid-column: 1 / -1;">\
                    <i class="ri-error-warning-line"></i>\
                    <p>' + InstalledSkills.escapeHtml(message) + '</p>\
                    <button class="nx-btn nx-btn--secondary" onclick="refreshSkillList()" style="margin-top: 16px;">\
                        <i class="ri-refresh-line"></i> 重试\
                    </button>\
                </div>\
            ';
        }
    };

    InstalledSkills.init();

    global.showEditModal = InstalledSkills.showEditModal;
    global.saveEditSkill = InstalledSkills.saveEditSkill;
    global.showInstallModal = InstalledSkills.showInstallModal;
    global.installSkill = InstalledSkills.installSkill;
    global.showRunModal = InstalledSkills.showRunModal;
    global.executeSkill = InstalledSkills.executeSkill;
    global.stopSkill = InstalledSkills.stopSkill;
    global.showConfigModal = InstalledSkills.showConfigModal;
    global.saveConfig = InstalledSkills.saveConfig;
    global.showLogsModal = InstalledSkills.showLogsModal;
    global.showProtocolModal = InstalledSkills.showProtocolModal;
    global.uninstallSkill = InstalledSkills.uninstallSkill;
    global.refreshSkillList = InstalledSkills.refreshSkillList;
    global.openModal = InstalledSkills.openModal;
    global.closeModal = InstalledSkills.closeModal;
    global.InstalledSkills = InstalledSkills;

})(typeof window !== 'undefined' ? window : this);
