(function(global) {
'use strict';

var state = DiscoveryState

var DiscoveryConfig = {
    openDiscoveryConfig: function() {
        var modal = document.getElementById('discoveryConfigModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'discoveryConfigModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = 
                '<div class="modal-content" style="max-width: 600px;">' +
                '<div class="modal-header">' +
                '<h3><i class="ri-settings-3-line"></i> 发现源配置</h3>' +
                '<button class="modal-close" onclick="closeDiscoveryConfig()"><i class="ri-close-line"></i></button>' +
                '</div>' +
                '<div class="modal-body">' +
                '<div class="config-tabs">' +
                '<div class="config-tab active" data-tab="gitee" onclick="switchConfigTab(\'gitee\')">Gitee</div>' +
                '<div class="config-tab" data-tab="github" onclick="switchConfigTab(\'github\')">GitHub</div>' +
                '</div>' +
                '<div class="config-tab-content active" id="configTabGitee">' +
                '<div class="config-form-vertical">' +
                '<div class="form-group">' +
                '<label class="form-label">仓库地址</label>' +
                '<input type="text" class="form-input" id="configGiteeRepo" placeholder="例如: owner/repo">' +
                '<div class="form-hint">Gitee 仓库地址，格式: owner/repo</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="form-label">访问令牌 (可选)</label>' +
                '<input type="password" class="form-input" id="configGiteeToken" placeholder="私有仓库需要提供访问令牌">' +
                '<div class="form-hint">私有仓库需要提供访问令牌</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="form-label">分支</label>' +
                '<input type="text" class="form-input" id="configGiteeBranch" value="master" placeholder="默认: master">' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="config-tab-content" id="configTabGithub" style="display: none;">' +
                '<div class="config-form-vertical">' +
                '<div class="form-group">' +
                '<label class="form-label">仓库地址</label>' +
                '<input type="text" class="form-input" id="configGithubRepo" placeholder="例如: owner/repo">' +
                '<div class="form-hint">GitHub 仓库地址，格式: owner/repo</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="form-label">访问令牌(可选)</label>' +
                '<input type="password" class="form-input" id="configGithubToken" placeholder="私有仓库需要提供访问令牌">' +
                '<div class="form-hint">私有仓库需要提供访问令牌</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="form-label">分支</label>' +
                '<input type="text" class="form-input" id="configGithubBranch" value="main" placeholder="默认: main">' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button class="nx-btn nx-btn--secondary" onclick="closeDiscoveryConfig()">取消</button>' +
                '<button class="nx-btn nx-btn--primary" onclick="saveDiscoveryConfig()">保存配置</button>' +
                '</div>' +
                '</div>';
            document.body.appendChild(modal);
            
            var style = document.createElement('style');
            style.textContent = 
                '.config-tabs { display: flex; gap: 4px; margin-bottom: 1px solid var(--nx-border); padding-bottom: 12px; }' +
                '.config-tab { padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 14px; color: var(--nx-text-secondary); transition: all 0.2s; }' +
                '.config-tab:hover { background: var(--nx-bg-hover); color: var(--nx-text-primary); }' +
                '.config-tab.active { background: var(--nx-primary); color: white; }' +
                '.config-tab-content { display: none; }' +
                '.config-tab-content.active { display: block; }' +
                '.config-form-vertical { display: flex; flex-direction: column; gap: 16px; }';
            document.head.appendChild(style);
            
            DiscoveryConfig.loadDiscoveryConfig();
        }
        
        modal.classList.add('show');
    },

    closeDiscoveryConfig: function() {
        var modal = document.getElementById('discoveryConfigModal');
        if (modal) {
            modal.classList.remove('show');
        }
    },

    switchConfigTab: function(tab) {
        var tabs = document.querySelectorAll('.config-tab');
        var contents = document.querySelectorAll('.config-tab-content');
        
        tabs.forEach(function(t) {
            t.classList.remove('active');
            if (t.dataset.tab === tab) {
                t.classList.add('active');
            }
        });
        
        contents.forEach(function(c) {
            c.classList.remove('active');
            if (c.id === 'configTab' + tab.charAt(0).toUpperCase() + tab.slice(1)) {
                c.classList.add('active');
            }
        });
    },

    loadDiscoveryConfig: function() {
        ApiClient.get('/api/v1/discovery/config')
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    if (result.data.gitee) {
                        var gitee = result.data.gitee;
                        var repoEl = document.getElementById('configGiteeRepo');
                        var tokenEl = document.getElementById('configGiteeToken');
                        var branchEl = document.getElementById('configGiteeBranch');
                        if (repoEl) repoEl.value = gitee.repo || '';
                        if (tokenEl) tokenEl.value = gitee.token || '';
                        if (branchEl) branchEl.value = gitee.branch || 'main';
                    }
                    if (result.data.github) {
                        var github = result.data.github;
                        var repoEl = document.getElementById('configGithubRepo');
                        var tokenEl = document.getElementById('configGithubToken');
                        var branchEl = document.getElementById('configGithubBranch');
                        if (repoEl) repoEl.value = github.repo || '';
                        if (tokenEl) tokenEl.value = github.token || '';
                        if (branchEl) branchEl.value = github.branch || 'main';
                    }
                }
            })
            .catch(function(error) {
                console.error('[loadDiscoveryConfig] Error:', error);
            });
    },

    saveDiscoveryConfig: function() {
        var giteeRepoEl = document.getElementById('configGiteeRepo');
        var giteeTokenEl = document.getElementById('configGiteeToken');
        var giteeBranchEl = document.getElementById('configGiteeBranch');
        var githubRepoEl = document.getElementById('configGithubRepo');
        var githubTokenEl = document.getElementById('configGithubToken');
        var githubBranchEl = document.getElementById('configGithubBranch');
        
        var config = {
            gitee: {
                repo: giteeRepoEl ? giteeRepoEl.value : '',
                token: giteeTokenEl ? giteeTokenEl.value : '',
                branch: giteeBranchEl ? giteeBranchEl.value : 'main'
            },
            github: {
                repo: githubRepoEl ? githubRepoEl.value : '',
                token: githubTokenEl ? githubTokenEl.value : '',
                branch: githubBranchEl ? githubBranchEl.value : 'main'
            }
        };
        
        ApiClient.put('/api/v1/discovery/config', config)
            .then(function(result) {
                if (result.status === 'success') {
                    DiscoveryUtils.addLog('success', '配置保存成功');
                    global.closeDiscoveryConfig();
                } else {
                    DiscoveryUtils.addLog('error', '配置保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', '配置保存失败: ' + error.message);
            });
    }
};

global.DiscoveryConfig = DiscoveryConfig;
global.openDiscoveryConfig = function() { DiscoveryConfig.openDiscoveryConfig(); };
global.closeDiscoveryConfig = function() { DiscoveryConfig.closeDiscoveryConfig(); };
global.saveDiscoveryConfig = function() { DiscoveryConfig.saveDiscoveryConfig(); };
global.switchConfigTab = function(tab) { DiscoveryConfig.switchConfigTab(tab); };

})(typeof window !== 'undefined' ? window : this);
