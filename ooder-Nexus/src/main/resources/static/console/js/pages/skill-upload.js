(function(global) {
    'use strict';

    var syncAPI = null;
    var uploadHistory = [];

    var SkillUpload = {
        init: function() {
            window.onPageInit = function() {
                console.log('技能上传页面初始化完成');
                syncAPI = new SkillCenterSyncAPI();
                SkillUpload.bindEvents();
            };
        },

        bindEvents: function() {
            var dropZone = document.getElementById('upload-drop-zone');
            var fileInput = document.getElementById('file-input');

            dropZone.addEventListener('dragover', function(e) {
                e.preventDefault();
                dropZone.classList.add('drag-over');
            });

            dropZone.addEventListener('dragleave', function(e) {
                e.preventDefault();
                dropZone.classList.remove('drag-over');
            });

            dropZone.addEventListener('drop', function(e) {
                e.preventDefault();
                dropZone.classList.remove('drag-over');
                SkillUpload.handleFiles(e.dataTransfer.files);
            });

            fileInput.addEventListener('change', function(e) {
                SkillUpload.handleFiles(e.target.files);
            });
        },

        handleFiles: function(files) {
            for (var i = 0; i < files.length; i++) {
                SkillUpload.uploadFile(files[i]);
            }
        },

        uploadFile: function(file) {
            var metadata = {
                name: file.name,
                size: file.size,
                type: file.type
            };
            SkillUpload.showUploadProgress(0);

            syncAPI.uploadSkill(file, metadata)
                .then(function(response) {
                    if (response.code === 200 && response.data.success) {
                        SkillUpload.showSuccess('技能 ' + file.name + ' 上传成功');
                        SkillUpload.addUploadHistory(response.data);
                    } else {
                        SkillUpload.showError('技能 ' + file.name + ' 上传失败：' + (response.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    SkillUpload.showError('技能 ' + file.name + ' 上传异常：' + error.message);
                })
                .finally(function() {
                    SkillUpload.hideUploadProgress();
                });
        },

        showUploadProgress: function(percent) {
            var progressDiv = document.getElementById('upload-progress');
            progressDiv.style.display = 'block';
            progressDiv.querySelector('.progress-fill').style.width = percent + '%';
            progressDiv.querySelector('.progress-text').textContent = percent + '%';
        },

        hideUploadProgress: function() {
            document.getElementById('upload-progress').style.display = 'none';
        },

        addUploadHistory: function(result) {
            uploadHistory.unshift(result);
            SkillUpload.renderUploadHistory();
        },

        renderUploadHistory: function() {
            var historyList = document.getElementById('upload-history-list');
            if (uploadHistory.length === 0) {
                historyList.innerHTML = '<div class="empty-state">暂无上传历史</div>';
                return;
            }

            var html = '';
            uploadHistory.forEach(function(item) {
                html += '<div class="history-item">' +
                    '<div class="history-info">' +
                    '<i class="ri-file-line"></i>' +
                    '<span class="history-name">' + item.skillName + '</span>' +
                    '<span class="history-version">' + item.version + '</span>' +
                    '</div>' +
                    '<div class="history-status ' + (item.success ? 'success' : 'error') + '">' +
                    (item.success ? '成功' : '失败') +
                    '</div>' +
                    '</div>';
            });
            historyList.innerHTML = html;
        },

        clearUploadHistory: function() {
            if (confirm('确定要清除上传历史吗？')) {
                uploadHistory = [];
                SkillUpload.renderUploadHistory();
                SkillUpload.showSuccess('上传历史已清除');
            }
        },

        showSuccess: function(message) {
            SkillUpload.showNotification(message, 'success');
        },

        showError: function(message) {
            SkillUpload.showNotification(message, 'error');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification ' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.classList.add('show'); }, 10);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    SkillUpload.init();

    global.clearUploadHistory = SkillUpload.clearUploadHistory;

})(typeof window !== 'undefined' ? window : this);
