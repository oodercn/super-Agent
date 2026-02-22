(function(global) {
    'use strict';

    var llmProviders = [
        { id: 'openai-1', name: 'OpenAI GPT-4', type: 'openai', status: 'active', version: 'v4.0', createdAt: '2024-01-01' },
        { id: 'anthropic-1', name: 'Anthropic Claude', type: 'anthropic', status: 'active', version: 'v3.5', createdAt: '2024-01-02' }
    ];

    var llmRequests = [
        { id: 'req-1', provider: 'openai-1', type: 'completion', status: 'success', createdAt: '2024-01-10 10:00', responseTime: '2.5s' },
        { id: 'req-2', provider: 'anthropic-1', type: 'chat', status: 'success', createdAt: '2024-01-10 10:05', responseTime: '1.8s' }
    ];

    var LlmManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('LLM交互管理页面初始化完成');
                LlmManagement.loadLlmProviders();
                LlmManagement.loadLlmRequests();
            };
        },

        loadLlmProviders: function() {
            var tbody = document.getElementById('llmProvidersTableBody');
            tbody.innerHTML = '';
            llmProviders.forEach(function(provider) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + provider.id + '</td>' +
                    '<td>' + provider.name + '</td>' +
                    '<td>' + provider.type + '</td>' +
                    '<td><span class="' + (provider.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (provider.status === 'active' ? '活跃' : '禁用') + '</span></td>' +
                    '<td>' + provider.version + '</td>' +
                    '<td>' + provider.createdAt + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editLlmProvider(\'' + provider.id + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteLlmProvider(\'' + provider.id + '\')">删除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        loadLlmRequests: function() {
            var tbody = document.getElementById('llmRequestsTableBody');
            tbody.innerHTML = '';
            llmRequests.forEach(function(request) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + request.id + '</td>' +
                    '<td>' + request.provider + '</td>' +
                    '<td>' + request.type + '</td>' +
                    '<td><span class="' + (request.status === 'success' ? 'nx-text-success' : 'nx-text-danger') + '">' + (request.status === 'success' ? '成功' : '失败') + '</span></td>' +
                    '<td>' + request.createdAt + '</td>' +
                    '<td>' + request.responseTime + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="viewLlmRequest(\'' + request.id + '\')">查看</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        addLlmProvider: function() {
            alert('添加LLM提供者功能开发中...');
        },

        editLlmProvider: function(providerId) {
            alert('编辑LLM提供者: ' + providerId);
        },

        deleteLlmProvider: function(providerId) {
            if (confirm('确定要删除此LLM提供者吗？')) {
                alert('删除LLM提供者: ' + providerId);
            }
        },

        viewLlmRequest: function(requestId) {
            alert('查看LLM请求: ' + requestId);
        },

        refreshLlmRequests: function() {
            LlmManagement.loadLlmRequests();
        }
    };

    LlmManagement.init();

    global.addLlmProvider = LlmManagement.addLlmProvider;
    global.editLlmProvider = LlmManagement.editLlmProvider;
    global.deleteLlmProvider = LlmManagement.deleteLlmProvider;
    global.viewLlmRequest = LlmManagement.viewLlmRequest;
    global.refreshLlmRequests = LlmManagement.refreshLlmRequests;

})(typeof window !== 'undefined' ? window : this);
