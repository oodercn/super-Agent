(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryBatch = {
    toggleBatchMode: function() {
        state.batchMode = !state.batchMode;
        state.selectedCapabilities = [];
        
        var batchBar = document.getElementById('batchBar');
        var resultsBody = document.getElementById('resultsBody');
        
        if (state.batchMode) {
            if (batchBar) batchBar.style.display = 'flex';
            if (resultsBody) resultsBody.classList.add('batch-mode');
        } else {
            if (batchBar) batchBar.style.display = 'none';
            if (resultsBody) resultsBody.classList.remove('batch-mode');
            resultsBody.querySelectorAll('.result-item').forEach(function(item) {
                item.classList.remove('selected');
            });
        }
        
        DiscoveryBatch.updateBatchCount();
    },

    toggleCapabilitySelection: function(skillId, event) {
        if (!state.batchMode) return;
        
        event.stopPropagation();
        
        var item = event.target.closest('.result-item');
        var index = state.selectedCapabilities.indexOf(skillId);
        
        if (index > -1) {
            state.selectedCapabilities.splice(index, 1);
            if (item) item.classList.remove('selected');
        } else {
            state.selectedCapabilities.push(skillId);
            if (item) item.classList.add('selected');
        }
        
        DiscoveryBatch.updateBatchCount();
    },

    selectAllCapabilities: function() {
        var items = document.querySelectorAll('.result-item');
        items.forEach(function(item) {
            var skillId = item.dataset.skillId;
            if (skillId && state.selectedCapabilities.indexOf(skillId) === -1) {
                state.selectedCapabilities.push(skillId);
                item.classList.add('selected');
            }
        });
        DiscoveryBatch.updateBatchCount();
    },

    deselectAllCapabilities: function() {
        state.selectedCapabilities = [];
        document.querySelectorAll('.result-item.selected').forEach(function(item) {
            item.classList.remove('selected');
        });
        DiscoveryBatch.updateBatchCount();
    },

    batchInstall: function() {
        if (state.selectedCapabilities.length === 0) {
            alert('请先选择要安装的能力');
            return;
        }
        
        var caps = state.selectedCapabilities.map(function(id) {
            return state.discoveredCapabilities.find(function(c) { return c.id === id; });
        }).filter(function(c) { return c && !c.installed; });
        
        if (caps.length === 0) {
            alert('所选能力已全部安装');
            return;
        }
        
        if (!confirm('确定要批量安装 ' + caps.length + ' 个能力吗？')) {
            return;
        }
        
        DiscoveryBatch.executeBatchInstall(caps);
    },

    updateBatchCount: function() {
        var countEl = document.getElementById('selectedCount');
        if (countEl) {
            countEl.textContent = state.selectedCapabilities.length;
        }
        
        var installBtn = document.getElementById('batchInstallBtn');
        if (installBtn) {
            installBtn.disabled = state.selectedCapabilities.length === 0;
        }
    },

    executeBatchInstall: function(caps) {
        var modal = document.getElementById('batchInstallModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'batchInstallModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = 
                '<div class="modal-content" style="max-width: 600px;">' +
                '<div class="modal-header">' +
                '<h3><i class="ri-download-cloud-line"></i> 批量安装</h3>' +
                '</div>' +
                '<div class="modal-body">' +
                '<div class="batch-progress">' +
                '<div class="batch-progress-bar" id="batchProgressBar"></div>' +
                '</div>' +
                '<div class="batch-status" id="batchStatus">准备安装...</div>' +
                '<div class="batch-list" id="batchList"></div>' +
                '</div>' +
                '<div class="modal-footer" style="display: none;" id="batchFooter">' +
                '<button class="nx-btn nx-btn--primary" onclick="closeBatchInstall()">完成</button>' +
                '</div>' +
                '</div>';
            document.body.appendChild(modal);
        }
        
        modal.classList.add('show');
        
        var progressBar = document.getElementById('batchProgressBar');
        var statusEl = document.getElementById('batchStatus');
        var listEl = document.getElementById('batchList');
        var footerEl = document.getElementById('batchFooter');
        
        listEl.innerHTML = caps.map(function(cap) {
            return '<div class="batch-item" data-id="' + cap.id + '">' +
                '<i class="ri-time-line batch-item-icon pending"></i>' +
                '<span class="batch-item-name">' + cap.name + '</span>' +
                '<span class="batch-item-status">等待中</span></div>';
        }).join('');
        
        var completed = 0;
        var total = caps.length;
        
        function installNext() {
            if (completed >= total) {
                progressBar.style.width = '100%';
                progressBar.style.background = 'var(--nx-success)';
                statusEl.textContent = '全部安装完成！成功: ' + total;
                footerEl.style.display = 'flex';
                return;
            }
            
            var cap = caps[completed];
            var progress = Math.round((completed / total) * 100);
            progressBar.style.width = progress + '%';
            statusEl.textContent = '正在安装: ' + cap.name + ' (' + (completed + 1) + '/' + total + ')';
            
            var itemEl = listEl.querySelector('[data-id="' + cap.id + '"]');
            if (itemEl) {
                itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-loader-4-line ri-spin';
                itemEl.querySelector('.batch-item-status').textContent = '安装中...';
            }
            
            ApiClient.post('/api/v1/discovery/install', {
                capabilityId: cap.id,
                name: cap.name,
                skillForm: cap.skillForm
            })
            .then(function(result) {
                if (itemEl) {
                    itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-checkbox-circle-line';
                    itemEl.querySelector('.batch-item-icon').style.color = 'var(--nx-success)';
                    itemEl.querySelector('.batch-item-status').textContent = '成功';
                }
            })
            .catch(function(error) {
                if (itemEl) {
                    itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-close-circle-line';
                    itemEl.querySelector('.batch-item-icon').style.color = 'var(--nx-danger)';
                    itemEl.querySelector('.batch-item-status').textContent = '失败';
                }
            })
            .finally(function() {
                completed++;
                setTimeout(installNext, 500);
            });
        }
        
        installNext();
    },

    closeBatchInstall: function() {
        var modal = document.getElementById('batchInstallModal');
        if (modal) {
            modal.classList.remove('show');
        }
        DiscoveryBatch.toggleBatchMode();
        DiscoveryCore.startScan();
    }
};

global.DiscoveryBatch = DiscoveryBatch;
global.toggleBatchMode = function() { DiscoveryBatch.toggleBatchMode(); };
global.toggleCapabilitySelection = function(skillId, event) { DiscoveryBatch.toggleCapabilitySelection(skillId, event); };
global.selectAllCapabilities = function() { DiscoveryBatch.selectAllCapabilities(); };
global.deselectAllCapabilities = function() { DiscoveryBatch.deselectAllCapabilities(); };
global.batchInstall = function() { DiscoveryBatch.batchInstall(); };
global.closeBatchInstall = function() { DiscoveryBatch.closeBatchInstall(); };

})(typeof window !== 'undefined' ? window : this);
