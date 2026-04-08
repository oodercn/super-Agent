(function() {
    'use strict';

    var selectedProfile = 'micro';
    var currentStep = 0;
    var installSteps = [];
    var requiredSkills = [];
    var installedSkillIds = new Set();
    var currentInstallId = null;
    var eventSource = null;

    async function initPage() {
        await loadInstallSteps();
        await loadRequiredSkills();
        renderSteps();
        renderSkills();
    }

    async function loadInstallSteps() {
        try {
            var response = await fetch('/api/v1/install-scene/steps?profile=' + selectedProfile);
            var result = await response.json();
            if (result.status === 'success' && result.data) {
                installSteps = result.data;
                addLog('加载了 ' + installSteps.length + ' 个安装步骤', 'info');
            }
        } catch (e) {
            console.error('Failed to load install steps:', e);
            addLog('无法加载安装步骤', 'error');
        }
    }

    async function loadRequiredSkills() {
        try {
            var response = await fetch('/api/v1/install-scene/skills?profile=' + selectedProfile);
            var result = await response.json();
            if (result.status === 'success' && result.data) {
                requiredSkills = result.data;
                addLog('需要安装 ' + requiredSkills.length + ' 个技能包', 'info');
                
                requiredSkills.forEach(function(skill) {
                    if (skill.currentSystem) {
                        installedSkillIds.add(skill.id);
                    }
                });
            }
        } catch (e) {
            console.error('Failed to load required skills:', e);
            addLog('无法加载技能列表', 'error');
        }
    }

    function renderSteps() {
        var container = document.getElementById('stepsContainer');
        if (!container || installSteps.length === 0) return;

        var html = '<div class="install-steps">';
        
        installSteps.forEach(function(step, index) {
            var stepNum = index + 1;
            html += '<div class="step-item" id="step-' + stepNum + '" data-step="' + stepNum + '">';
            html += '<div class="step-number">' + stepNum + '</div>';
            html += '<div class="step-content">';
            html += '<div class="step-title">' + step.name + '</div>';
            html += '<div class="step-desc">' + step.description + '</div>';
            if (step.required) {
                html += '<span class="step-badge required">必需</span>';
            } else if (step.recommended) {
                html += '<span class="step-badge recommended">推荐</span>';
            }
            html += '</div>';
            html += '</div>';
        });
        
        html += '</div>';
        container.innerHTML = html;
    }

    function renderSkills() {
        var container = document.getElementById('skillsContainer');
        if (!container || requiredSkills.length === 0) return;

        var html = '<div class="skills-list">';
        
        requiredSkills.forEach(function(skill, index) {
            var isInstalled = installedSkillIds.has(skill.id);
            html += '<div class="skill-item' + (isInstalled ? ' installed' : '') + '" id="skill-' + skill.id + '">';
            html += '<div class="skill-icon"><i class="ri-puzzle-line"></i></div>';
            html += '<div class="skill-info">';
            html += '<div class="skill-name">' + skill.name + '</div>';
            html += '<div class="skill-desc">' + skill.desc + '</div>';
            html += '<div class="skill-version">v' + skill.version + '</div>';
            html += '</div>';
            html += '<div class="skill-status">';
            if (isInstalled) {
                html += '<span class="status-badge installed"><i class="ri-checkbox-circle-line"></i> 已安装</span>';
            } else if (skill.required) {
                html += '<span class="status-badge required">必需</span>';
            } else {
                html += '<span class="status-badge optional">可选</span>';
            }
            html += '</div>';
            html += '</div>';
        });
        
        html += '</div>';
        container.innerHTML = html;
    }

    function startInstall() {
        if (eventSource) {
            eventSource.close();
        }

        addLog('开始安装流程...', 'info');
        updateStepUI(0);

        eventSource = new EventSource('/api/v1/install-scene/start?profile=' + selectedProfile);

        eventSource.addEventListener('start', function(e) {
            var data = JSON.parse(e.data);
            currentInstallId = data.installId;
            addLog('安装ID: ' + currentInstallId, 'info');
        });

        eventSource.addEventListener('step', function(e) {
            var data = JSON.parse(e.data);
            updateStepUI(data.step);
            addLog('步骤 ' + data.step + '/' + data.total + ': ' + data.name, 'info');
        });

        eventSource.addEventListener('skill', function(e) {
            var data = JSON.parse(e.data);
            updateSkillUI(data.skillId, 'installing');
            addLog('正在处理: ' + data.name + ' (' + data.index + '/' + data.total + ')', 'info');
        });

        eventSource.addEventListener('skillProgress', function(e) {
            var data = JSON.parse(e.data);
            if (data.status === 'installed') {
                installedSkillIds.add(data.skillId);
                updateSkillUI(data.skillId, 'installed');
                addLog('✓ ' + data.message, 'success');
            } else if (data.status === 'skipped') {
                updateSkillUI(data.skillId, 'skipped');
                addLog('○ ' + data.message, 'info');
            }
        });

        eventSource.addEventListener('stepComplete', function(e) {
            var data = JSON.parse(e.data);
            markStepComplete(data.step);
        });

        eventSource.addEventListener('complete', function(e) {
            var data = JSON.parse(e.data);
            addLog('✓ 安装流程完成！', 'success');
            showCompletionBanner(data);
            eventSource.close();
        });

        eventSource.addEventListener('error', function(e) {
            var data = JSON.parse(e.data);
            addLog('✗ 安装失败: ' + data.error, 'error');
            eventSource.close();
        });

        eventSource.onerror = function(e) {
            addLog('连接中断，请重试', 'error');
            eventSource.close();
        };
    }

    function updateStepUI(stepNum) {
        currentStep = stepNum;
        document.querySelectorAll('.step-item').forEach(function(item) {
            var num = parseInt(item.getAttribute('data-step'));
            item.classList.remove('active', 'completed');
            if (num < stepNum) {
                item.classList.add('completed');
            } else if (num === stepNum) {
                item.classList.add('active');
            }
        });
    }

    function markStepComplete(stepNum) {
        var item = document.getElementById('step-' + stepNum);
        if (item) {
            item.classList.add('completed');
        }
    }

    function updateSkillUI(skillId, status) {
        var item = document.getElementById('skill-' + skillId);
        if (!item) return;

        item.classList.remove('installing', 'installed', 'skipped');
        item.classList.add(status);

        var statusBadge = item.querySelector('.status-badge');
        if (statusBadge) {
            if (status === 'installed') {
                statusBadge.innerHTML = '<i class="ri-checkbox-circle-line"></i> 已安装';
                statusBadge.className = 'status-badge installed';
            } else if (status === 'skipped') {
                statusBadge.innerHTML = '<i class="ri-checkbox-circle-line"></i> 跳过';
                statusBadge.className = 'status-badge skipped';
            } else if (status === 'installing') {
                statusBadge.innerHTML = '<i class="ri-loader-4-line"></i> 安装中...';
                statusBadge.className = 'status-badge installing';
            }
        }
    }

    function showCompletionBanner(data) {
        var container = document.querySelector('.installer-main');
        if (!container) return;

        var banner = document.createElement('div');
        banner.className = 'completion-banner';
        banner.innerHTML = 
            '<h3><i class="ri-checkbox-circle-fill"></i> 安装完成</h3>' +
            '<p>已成功安装 ' + data.installedSkills.length + ' 个技能包</p>' +
            '<p>部署规模: ' + data.profile + '</p>' +
            '<button class="nx-btn nx-btn--primary" onclick="goToNextStep()">' +
                '<i class="ri-arrow-right-line"></i> 进入场景配置' +
            '</button>';
        container.appendChild(banner);
    }

    function addLog(message, type) {
        type = type || 'info';
        var logContainer = document.getElementById('install-log');
        if (!logContainer) return;

        var icons = {
            'info': 'ri-information-line',
            'success': 'ri-checkbox-circle-line',
            'error': 'ri-error-warning-line',
            'warning': 'ri-alert-line'
        };
        var time = new Date().toLocaleTimeString();
        var div = document.createElement('div');
        div.className = 'log-item ' + type;
        div.innerHTML = '<i class="' + icons[type] + '"></i><span class="log-time">' + time + '</span>' + message;
        logContainer.appendChild(div);
        logContainer.scrollTop = logContainer.scrollHeight;
    }

    function goToNextStep() {
        window.location.href = '/console/pages/scene-group-management.html';
    }

    window.startInstall = startInstall;
    window.goToNextStep = goToNextStep;

    window.selectProfile = function(profile) {
        selectedProfile = profile;
        document.querySelectorAll('.profile-card').forEach(function(card) {
            card.classList.remove('selected');
        });
        document.querySelector('.profile-card[data-profile="' + profile + '"]').classList.add('selected');
        
        initPage();
        addLog('已选择部署规模: ' + profile, 'info');
    };

    document.addEventListener('DOMContentLoaded', initPage);

})();
