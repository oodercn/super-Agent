(function(global) {
    'use strict';

    var MyExecution = {
        init: function() {
            window.onPageInit = function() {
                MyExecution.loadExecutionHistory();
                MyExecution.loadSkillOptions();
            };
        },

        loadExecutionHistory: async function() {
            try {
                var response = await fetch('/api/skillcenter/personal/execution/history', { method: 'POST' });
                
                var contentType = response.headers.get('content-type');
                var rs;
                
                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MyExecution.renderExecutionHistory(rs.data);
                } else {
                    MyExecution.showError('加载执行历史失败');
                }
            } catch (error) {
                console.error('加载执行历史错误:', error);
                MyExecution.showError('加载执行历史失败');
            }
        },

        renderExecutionHistory: function(history) {
            var executionList = document.getElementById('execution-list');
            executionList.innerHTML = '';
            
            history.forEach(function(item) {
                var historyItem = document.createElement('div');
                historyItem.className = 'execution-item';
                historyItem.innerHTML = '\
                    <div class="execution-info">\
                        <div class="execution-skill">' + item.skillName + '</div>\
                        <div class="execution-status ' + (item.status === 'success' ? 'success' : 'failed') + '">' + (item.status === 'success' ? '成功' : '失败') + '</div>\
                        <div class="execution-time">' + MyExecution.formatTime(item.timestamp) + '</div>\
                    </div>\
                    <div class="execution-actions">\
                        <button class="btn btn-secondary" onclick="viewExecutionResult(\'' + item.id + '\')">\
                            <i class="ri-eye-line"></i> 查看结果\
                        </button>\
                    </div>\
                ';
                executionList.appendChild(historyItem);
            });
        },

        loadSkillOptions: async function() {
            try {
                var response = await fetch('/api/skillcenter/personal/skills/list', { method: 'POST' });
                
                var contentType = response.headers.get('content-type');
                var rs;
                
                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (rs.requestStatus === 200 || rs.status === 'success') {
                    var skillSelect = document.getElementById('skill-select');
                    skillSelect.innerHTML = '<option value="">请选择技能</option>';
                    rs.data.forEach(function(skill) {
                        var option = document.createElement('option');
                        option.value = skill.id;
                        option.textContent = skill.name;
                        skillSelect.appendChild(option);
                    });
                }
            } catch (error) {
                console.error('加载技能列表错误:', error);
            }
        },

        switchTab: function(tab) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) { btn.classList.remove('active'); });
            document.querySelectorAll('.tab-content').forEach(function(content) { content.style.display = 'none'; });
            
            event.target.classList.add('active');
            document.getElementById(tab + '-tab').style.display = 'block';
        },

        showExecuteModal: function() {
            document.getElementById('execute-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        executeSkill: async function() {
            var skillId = document.getElementById('skill-select').value;
            var parametersText = document.getElementById('skill-parameters').value;
            
            if (!skillId) {
                MyExecution.showError('请选择技能');
                return;
            }
            
            var parameters = {};
            if (parametersText) {
                try {
                    parameters = JSON.parse(parametersText);
                } catch (e) {
                    MyExecution.showError('参数格式错误，请输入有效的JSON');
                    return;
                }
            }
            
            try {
                var response = await fetch('/api/skillcenter/personal/execution/execute/' + skillId, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(parameters)
                });
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyExecution.closeModal('execute-modal');
                    MyExecution.showSuccess('技能执行成功');
                    MyExecution.loadExecutionHistory();
                } else {
                    MyExecution.showError('技能执行失败');
                }
            } catch (error) {
                console.error('执行技能错误:', error);
                MyExecution.showError('技能执行失败');
            }
        },

        viewExecutionResult: async function(executionId) {
            try {
                var response = await fetch('/api/skillcenter/personal/execution/result/' + executionId);
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyExecution.switchTab('result');
                    MyExecution.renderExecutionResult(result.data);
                } else {
                    MyExecution.showError('加载执行结果失败');
                }
            } catch (error) {
                console.error('加载执行结果错误:', error);
                MyExecution.showError('加载执行结果失败');
            }
        },

        renderExecutionResult: function(result) {
            var resultDiv = document.getElementById('execution-result');
            resultDiv.innerHTML = '\
                <div class="result-info">\
                    <h4>执行结果</h4>\
                    <p><strong>状态:</strong> ' + result.status + '</p>\
                    <p><strong>输出:</strong></p>\
                    <pre>' + JSON.stringify(result.output, null, 2) + '</pre>\
                    ' + (result.error ? '<p><strong>错误:</strong> ' + result.error + '</p>' : '') + '\
                </div>\
            ';
        },

        formatTime: function(timestamp) {
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) { MyExecution.showNotification(message, 'error'); },
        showSuccess: function(message) { MyExecution.showNotification(message, 'success'); },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MyExecution.init();

    global.switchTab = MyExecution.switchTab;
    global.showExecuteModal = MyExecution.showExecuteModal;
    global.closeModal = MyExecution.closeModal;
    global.executeSkill = MyExecution.executeSkill;
    global.viewExecutionResult = MyExecution.viewExecutionResult;
    global.MyExecution = MyExecution;

})(typeof window !== 'undefined' ? window : this);
