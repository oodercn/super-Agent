(function(global) {
    'use strict';

    var ActivationProgress = {
        container: null,
        installId: null,
        skillId: null,
        eventSource: null,
        pollInterval: null,
        
        init: function(containerId, installId, skillId) {
            this.container = document.getElementById(containerId);
            this.installId = installId;
            this.skillId = skillId;
            
            if (!this.container) {
                console.error('Container not found:', containerId);
                return;
            }
            
            this.render();
            this.startPolling();
        },
        
        render: function() {
            var html = '<div class="activation-progress">' +
                '<div class="progress-header">' +
                    '<h4><i class="ri-loader-4-line ri-spin"></i> 激活进度</h4>' +
                    '<span class="progress-status" id="activation-status">初始化...</span>' +
                '</div>' +
                '<div class="progress-bar-container">' +
                    '<div class="progress-bar" id="activation-progress-bar" style="width: 0%"></div>' +
                '</div>' +
                '<div class="progress-percentage" id="activation-percentage">0%</div>' +
                '<div class="progress-steps" id="activation-steps"></div>' +
                '<div class="progress-message" id="activation-message"></div>' +
            '</div>';
            
            this.container.innerHTML = html;
        },
        
        startPolling: function() {
            var self = this;
            
            if (typeof EventSource !== 'undefined') {
                var sseUrl = '/api/v1/activations/' + this.installId + '/stream';
                this.eventSource = new EventSource(sseUrl);
                
                this.eventSource.addEventListener('process', function(e) {
                    var data = JSON.parse(e.data);
                    self.updateProgress(data);
                });
                
                this.eventSource.addEventListener('update', function(e) {
                    var data = JSON.parse(e.data);
                    self.updateProgress(data);
                });
                
                this.eventSource.addEventListener('complete', function(e) {
                    var data = JSON.parse(e.data);
                    self.updateProgress(data);
                    self.stopPolling();
                    self.onComplete(data);
                });
                
                this.eventSource.onerror = function() {
                    console.log('SSE connection error, falling back to polling');
                    self.eventSource.close();
                    self.startFallbackPolling();
                };
            } else {
                this.startFallbackPolling();
            }
        },
        
        startFallbackPolling: function() {
            var self = this;
            this.pollInterval = setInterval(function() {
                self.fetchProgress();
            }, 1000);
        },
        
        fetchProgress: function() {
            var self = this;
            
            ApiClient.get('/api/v1/activations/' + this.installId + '/process')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.updateProgress(result.data);
                        
                        if (result.data.status === 'COMPLETED' || 
                            result.data.status === 'FAILED' ||
                            result.data.status === 'CANCELLED') {
                            self.stopPolling();
                            self.onComplete(result.data);
                        }
                    }
                })
                .catch(function(error) {
                    console.error('Failed to fetch progress:', error);
                });
        },
        
        stopPolling: function() {
            if (this.eventSource) {
                this.eventSource.close();
                this.eventSource = null;
            }
            if (this.pollInterval) {
                clearInterval(this.pollInterval);
                this.pollInterval = null;
            }
        },
        
        updateProgress: function(process) {
            if (!process) return;
            
            var statusEl = document.getElementById('activation-status');
            var barEl = document.getElementById('activation-progress-bar');
            var percentageEl = document.getElementById('activation-percentage');
            var stepsEl = document.getElementById('activation-steps');
            var messageEl = document.getElementById('activation-message');
            
            var status = process.status || 'PENDING';
            var steps = process.steps || [];
            var currentStep = process.currentStep || 0;
            var totalSteps = process.totalSteps || steps.length;
            
            var percentage = totalSteps > 0 ? Math.round((currentStep / totalSteps) * 100) : 0;
            
            var statusText = this.getStatusText(status);
            if (statusEl) statusEl.textContent = statusText;
            if (barEl) barEl.style.width = percentage + '%';
            if (percentageEl) percentageEl.textContent = percentage + '%';
            
            if (stepsEl) {
                var stepsHtml = '<div class="steps-list">';
                steps.forEach(function(step, index) {
                    var stepStatus = step.status || 'PENDING';
                    var stepClass = 'step-item step-' + stepStatus.toLowerCase();
                    var icon = this.getStepIcon(stepStatus);
                    
                    stepsHtml += '<div class="' + stepClass + '">' +
                        '<span class="step-icon">' + icon + '</span>' +
                        '<span class="step-name">' + (step.name || step.stepId) + '</span>' +
                        '<span class="step-status">' + this.getStepStatusText(stepStatus) + '</span>' +
                    '</div>';
                }.bind(this));
                stepsHtml += '</div>';
                stepsEl.innerHTML = stepsHtml;
            }
            
            if (messageEl) {
                var currentStepObj = steps.find(function(s) { 
                    return s.status === 'IN_PROGRESS'; 
                });
                messageEl.textContent = currentStepObj ? 
                    (currentStepObj.description || currentStepObj.name || '处理中...') : 
                    this.getStatusMessage(status);
            }
            
            this.updateContainerClass(status);
        },
        
        getStatusText: function(status) {
            var map = {
                'PENDING': '等待中',
                'IN_PROGRESS': '进行中',
                'COMPLETED': '已完成',
                'FAILED': '失败',
                'CANCELLED': '已取消'
            };
            return map[status] || status;
        },
        
        getStepIcon: function(status) {
            var icons = {
                'PENDING': '<i class="ri-time-line"></i>',
                'IN_PROGRESS': '<i class="ri-loader-4-line ri-spin"></i>',
                'COMPLETED': '<i class="ri-check-line"></i>',
                'FAILED': '<i class="ri-close-line"></i>',
                'SKIPPED': '<i class="ri-skip-forward-line"></i>'
            };
            return icons[status] || '<i class="ri-question-line"></i>';
        },
        
        getStepStatusText: function(status) {
            var map = {
                'PENDING': '待执行',
                'IN_PROGRESS': '执行中',
                'COMPLETED': '已完成',
                'FAILED': '失败',
                'SKIPPED': '已跳过'
            };
            return map[status] || status;
        },
        
        getStatusMessage: function(status) {
            var messages = {
                'PENDING': '等待开始激活...',
                'IN_PROGRESS': '正在执行激活步骤...',
                'COMPLETED': '激活完成！',
                'FAILED': '激活失败，请查看详情',
                'CANCELLED': '激活已取消'
            };
            return messages[status] || '';
        },
        
        updateContainerClass: function(status) {
            if (!this.container) return;
            
            this.container.classList.remove('status-pending', 'status-progress', 'status-completed', 'status-failed');
            
            if (status === 'PENDING') {
                this.container.classList.add('status-pending');
            } else if (status === 'IN_PROGRESS') {
                this.container.classList.add('status-progress');
            } else if (status === 'COMPLETED') {
                this.container.classList.add('status-completed');
            } else if (status === 'FAILED') {
                this.container.classList.add('status-failed');
            }
        },
        
        onComplete: function(process) {
            var status = process.status;
            
            if (status === 'COMPLETED') {
                this.showSuccess(process);
            } else if (status === 'FAILED') {
                this.showError(process);
            }
            
            if (this.onCompleteCallback) {
                this.onCompleteCallback(process);
            }
        },
        
        showSuccess: function(process) {
            var header = this.container.querySelector('.progress-header h4');
            if (header) {
                header.innerHTML = '<i class="ri-check-line"></i> 激活成功';
            }
        },
        
        showError: function(process) {
            var header = this.container.querySelector('.progress-header h4');
            if (header) {
                header.innerHTML = '<i class="ri-error-warning-line"></i> 激活失败';
            }
        },
        
        destroy: function() {
            this.stopPolling();
            if (this.container) {
                this.container.innerHTML = '';
            }
        }
    };

    global.ActivationProgress = ActivationProgress;

})(typeof window !== 'undefined' ? window : this);
