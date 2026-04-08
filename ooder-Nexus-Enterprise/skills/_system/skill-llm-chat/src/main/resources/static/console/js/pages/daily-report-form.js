(function(global) {
    'use strict';

    var sceneGroupId = null;
    var reportId = null;
    var attachments = [];

    var DailyReportForm = {
        init: function() {
            var urlParams = new URLSearchParams(window.location.search);
            sceneGroupId = urlParams.get('sceneGroupId');
            reportId = urlParams.get('reportId');

            DailyReportForm.initDateInfo();
            DailyReportForm.loadDraft();
        },

        initDateInfo: function() {
            var now = new Date();
            var weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
            var dateStr = now.getFullYear() + '年' + (now.getMonth() + 1) + '月' + now.getDate() + '日 ' + weekDays[now.getDay()];
            document.getElementById('dateInfo').textContent = dateStr;
        },

        loadDraft: function() {
            var draft = localStorage.getItem('dailyReportDraft');
            if (draft) {
                try {
                    var data = JSON.parse(draft);
                    if (data.workItems) {
                        var container = document.getElementById('workItems');
                        container.innerHTML = '';
                        data.workItems.forEach(function(item, index) {
                            addWorkItem(item);
                        });
                    }
                    if (data.planItems) {
                        var container = document.getElementById('planItems');
                        container.innerHTML = '';
                        data.planItems.forEach(function(item, index) {
                            addPlanItem(item);
                        });
                    }
                    if (data.issues) {
                        document.getElementById('issuesInput').value = data.issues;
                    }
                } catch (e) {
                    console.error('Failed to load draft:', e);
                }
            }
        },

        saveDraft: function() {
            var data = {
                workItems: getWorkItems(),
                planItems: getPlanItems(),
                issues: document.getElementById('issuesInput').value,
                savedAt: new Date().toISOString()
            };
            localStorage.setItem('dailyReportDraft', JSON.stringify(data));
            alert('草稿已保存');
        },

        submitReport: function() {
            var workItems = getWorkItems();
            var planItems = getPlanItems();
            var issues = document.getElementById('issuesInput').value;

            if (workItems.length === 0) {
                alert('请填写今日工作内容');
                return;
            }

            var report = {
                sceneGroupId: sceneGroupId,
                userId: 'current-user',
                userName: '当前用户',
                workItems: workItems,
                planItems: planItems,
                issues: issues,
                attachments: attachments
            };

            ApiClient.post('/api/v1/skills/report-submit', report)
                .then(function(result) {
                    if (result.status === 'success') {
                        localStorage.removeItem('dailyReportDraft');
                        alert('日志提交成功！');
                        window.history.back();
                    } else {
                        alert('提交失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('提交失败: ' + error.message);
                });
        },

        summarizeEmails: function() {
            showAiLoading();
            ApiClient.post('/api/v1/skills/report-email-fetch', {
                userId: 'current-user',
                sceneGroupId: sceneGroupId
            })
                .then(function(result) {
                    hideAiLoading();
                    if (result.status === 'success' && result.data) {
                        var container = document.getElementById('workItems');
                        container.innerHTML = '';
                        if (result.data.workItems) {
                            result.data.workItems.forEach(function(item) {
                                DailyReportForm.addWorkItem(item);
                            });
                        }
                    } else {
                        alert('获取邮件数据失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    hideAiLoading();
                    alert('获取邮件数据失败: ' + error.message);
                });
        },

        summarizeGitCommits: function() {
            showAiLoading();
            ApiClient.post('/api/v1/skills/report-git-fetch', {
                userId: 'current-user',
                sceneGroupId: sceneGroupId
            })
                .then(function(result) {
                    hideAiLoading();
                    if (result.status === 'success' && result.data) {
                        var container = document.getElementById('workItems');
                        container.innerHTML = '';
                        if (result.data.workItems) {
                            result.data.workItems.forEach(function(item) {
                                DailyReportForm.addWorkItem(item);
                            });
                        }
                    } else {
                        alert('获取Git提交记录失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    hideAiLoading();
                    alert('获取Git提交记录失败: ' + error.message);
                });
        },

        smartGenerate: function() {
            showAiLoading();
            ApiClient.post('/api/v1/skills/report-ai-generate', {
                userId: 'current-user',
                sceneGroupId: sceneGroupId
            })
                .then(function(result) {
                    hideAiLoading();
                    if (result.status === 'success' && result.data) {
                        var workContainer = document.getElementById('workItems');
                        workContainer.innerHTML = '';
                        if (result.data.workItems) {
                            result.data.workItems.forEach(function(item) {
                                DailyReportForm.addWorkItem(item);
                            });
                        }

                        var planContainer = document.getElementById('planItems');
                        planContainer.innerHTML = '';
                        if (result.data.planItems) {
                            result.data.planItems.forEach(function(item) {
                                DailyReportForm.addPlanItem(item);
                            });
                        }
                    } else {
                        alert('AI生成失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    hideAiLoading();
                    alert('AI生成失败: ' + error.message);
                });
        },

        viewHistory: function() {
            document.getElementById('historyModal').classList.add('open');
            DailyReportForm.loadHistory();
        },

        closeHistoryModal: function() {
            document.getElementById('historyModal').classList.remove('open');
        },

        loadHistory: function() {
            var container = document.getElementById('historyList');
            container.innerHTML = '<div class="empty-state"><i class="ri-loader-4-line ri-spin"></i><span>加载中...</span></div>';

            ApiClient.get('/api/v1/skills/report-history?sceneGroupId=' + sceneGroupId)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        DailyReportForm.renderHistory(result.data);
                    } else {
                        container.innerHTML = '<div class="empty-state"><i class="ri-file-list-line"></i><span>暂无历史记录</span></div>';
                    }
                })
                .catch(function(error) {
                    container.innerHTML = '<div class="empty-state"><i class="ri-error-warning-line"></i><span>加载失败</span></div>';
                });
        },

        renderHistory: function(data) {
            var container = document.getElementById('historyList');
            if (!data || data.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-file-list-line"></i><span>暂无历史记录</span></div>';
                return;
            }

            var html = '';
            data.forEach(function(item) {
                var statusClass = item.status === 'submitted' ? 'submitted' : 'draft';
                var statusText = item.status === 'submitted' ? '已提交' : '草稿';
                html += '<div class="history-item">' +
                    '<div class="history-item-header">' +
                    '<span class="history-item-date">' + item.date + '</span>' +
                    '<span class="history-item-status ' + statusClass + '">' + statusText + '</span>' +
                    '</div>' +
                    '<div class="history-item-content">' + (item.summary || '无内容') + '</div>' +
                    '</div>';
            });
            container.innerHTML = html;
        },

        addWorkItem: function(text) {
            var container = document.getElementById('workItems');
            var count = container.querySelectorAll('.work-item').length + 1;
            var div = document.createElement('div');
            div.className = 'work-item';
            div.innerHTML = '<span class="work-item-number">' + count + '</span>' +
                '<textarea class="work-item-input" placeholder="描述您今天完成的工作..." rows="2">' + (text || '') + '</textarea>' +
                '<button type="button" class="work-item-remove" onclick="removeWorkItem(this)"><i class="ri-delete-bin-line"></i></button>';
            container.appendChild(div);
        },

        removeWorkItem: function(btn) {
            var container = document.getElementById('workItems');
            if (container.querySelectorAll('.work-item').length > 1) {
                btn.parentElement.remove();
                DailyReportForm.renumberWorkItems();
            }
        },

        renumberWorkItems: function() {
            var items = document.querySelectorAll('#workItems .work-item');
            items.forEach(function(item, index) {
                item.querySelector('.work-item-number').textContent = index + 1;
            });
        },

        addPlanItem: function(text) {
            var container = document.getElementById('planItems');
            var count = container.querySelectorAll('.plan-item').length + 1;
            var div = document.createElement('div');
            div.className = 'plan-item';
            div.innerHTML = '<span class="plan-item-number">' + count + '</span>' +
                '<input type="text" class="plan-item-input" placeholder="描述明天的计划..." value="' + (text || '') + '">' +
                '<button type="button" class="plan-item-remove" onclick="removePlanItem(this)"><i class="ri-delete-bin-line"></i></button>';
            container.appendChild(div);
        },

        removePlanItem: function(btn) {
            var container = document.getElementById('planItems');
            if (container.querySelectorAll('.plan-item').length > 1) {
                btn.parentElement.remove();
                DailyReportForm.renumberPlanItems();
            }
        },

        renumberPlanItems: function() {
            var items = document.querySelectorAll('#planItems .plan-item');
            items.forEach(function(item, index) {
                item.querySelector('.plan-item-number').textContent = index + 1;
            });
        },

        handleFileUpload: function(input) {
            var files = input.files;
            for (var i = 0; i < files.length; i++) {
                attachments.push({
                    name: files[i].name,
                    size: files[i].size,
                    type: files[i].type
                });
            }
            DailyReportForm.renderAttachments();
        },

        renderAttachments: function() {
            var container = document.getElementById('attachmentList');
            if (attachments.length === 0) {
                container.innerHTML = '';
                return;
            }

            var html = '<div class="attachment-items">';
            attachments.forEach(function(file, index) {
                html += '<div class="attachment-item">' +
                    '<i class="ri-file-line"></i>' +
                    '<span class="attachment-name">' + file.name + '</span>' +
                    '<span class="attachment-size">' + formatFileSize(file.size) + '</span>' +
                    '<button type="button" class="attachment-remove" onclick="removeAttachment(' + index + ')"><i class="ri-close-line"></i></button>' +
                    '</div>';
            });
            html += '</div>';
            container.innerHTML = html;
        },

        removeAttachment: function(index) {
            attachments.splice(index, 1);
            DailyReportForm.renderAttachments();
        }
    };

    function getWorkItems() {
        var items = [];
        var inputs = document.querySelectorAll('#workItems .work-item-input');
        inputs.forEach(function(input) {
            if (input.value.trim()) {
                items.push(input.value.trim());
            }
        });
        return items;
    }

    function getPlanItems() {
        var items = [];
        var inputs = document.querySelectorAll('#planItems .plan-item-input');
        inputs.forEach(function(input) {
            if (input.value.trim()) {
                items.push(input.value.trim());
            }
        });
        return items;
    }

    function showAiLoading() {
        document.getElementById('aiLoadingModal').classList.add('open');
    }

    function hideAiLoading() {
        document.getElementById('aiLoadingModal').classList.remove('open');
    }

    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + ' B';
        if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    }

    global.DailyReportForm = DailyReportForm;
    global.addWorkItem = DailyReportForm.addWorkItem;
    global.removeWorkItem = DailyReportForm.removeWorkItem;
    global.addPlanItem = DailyReportForm.addPlanItem;
    global.removePlanItem = DailyReportForm.removePlanItem;
    global.saveDraft = DailyReportForm.saveDraft;
    global.submitReport = DailyReportForm.submitReport;
    global.summarizeEmails = DailyReportForm.summarizeEmails;
    global.summarizeGitCommits = DailyReportForm.summarizeGitCommits;
    global.smartGenerate = DailyReportForm.smartGenerate;
    global.viewHistory = DailyReportForm.viewHistory;
    global.closeHistoryModal = DailyReportForm.closeHistoryModal;
    global.handleFileUpload = DailyReportForm.handleFileUpload;
    global.removeAttachment = DailyReportForm.removeAttachment;

    window.onPageInit = function() {
        DailyReportForm.init();
    };

})(typeof window !== 'undefined' ? window : this);
