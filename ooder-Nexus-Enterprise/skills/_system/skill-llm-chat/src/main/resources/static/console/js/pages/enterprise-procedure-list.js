(function() {
    'use strict';

    let currentPage = 1;
    let pageSize = 20;
    let uploadedFiles = [];
    let currentStep = 1;

    function $(selector) {
        return document.querySelector(selector);
    }

    function $$(selector) {
        return document.querySelectorAll(selector);
    }

    document.addEventListener('DOMContentLoaded', function() {
        loadCategories();
        loadSources();
        loadData();
        initUpload();
    });

    function loadCategories() {
        fetch('/api/v1/enterprise-procedures/categories')
            .then(res => {
                if (!res.ok) return [];
                return res.json();
            })
            .then(data => {
                const select = $('#categoryFilter');
                if (!data || !Array.isArray(data)) return;
                data.forEach(cat => {
                    const option = document.createElement('option');
                    option.value = cat.code;
                    option.textContent = cat.name;
                    select.appendChild(option);
                });
            })
            .catch(() => {});
    }

    function loadSources() {
        fetch('/api/v1/enterprise-procedures/sources')
            .then(res => {
                if (!res.ok) return [];
                return res.json();
            })
            .then(data => {
                const select = $('#sourceFilter');
                if (!data || !Array.isArray(data)) return;
                data.forEach(src => {
                    const option = document.createElement('option');
                    option.value = src.code;
                    option.textContent = src.name;
                    select.appendChild(option);
                });
            })
            .catch(() => {});
    }

    function loadData() {
        const params = new URLSearchParams({
            category: $('#categoryFilter').value,
            source: $('#sourceFilter').value,
            status: $('#statusFilter').value,
            minCompleteness: $('#completenessFilter').value,
            keyword: $('#keywordInput').value,
            page: currentPage,
            pageSize: pageSize
        });

        fetch('/api/v1/enterprise-procedures?' + params.toString())
            .then(res => {
                if (!res.ok) return { data: [], total: 0 };
                return res.json();
            })
            .then(response => {
                const data = response && response.data ? response.data : [];
                const total = response && response.total ? response.total : 0;
                renderTable(data);
                renderPagination(total);
            })
            .catch(() => {
                renderTable([]);
                renderPagination(0);
            });
    }

    function renderTable(data) {
        const tbody = $('#dataTableBody');
        tbody.innerHTML = '';

        if (!data || data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="empty-data">暂无数据</td></tr>';
            return;
        }

        data.forEach(item => {
            const completenessBar = renderCompletenessBar(item.completeness);
            const sourceLabel = getSourceLabel(item.source);
            const statusBadge = renderStatusBadge(item.status);

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><input type="checkbox" value="${item.procedureId}"></td>
                <td>
                    <a href="enterprise-procedure-detail.html?id=${item.procedureId}" class="link">
                        ${item.name}
                    </a>
                </td>
                <td>${item.category || '-'}</td>
                <td><span class="source-tag">${sourceLabel}</span></td>
                <td>${completenessBar}</td>
                <td>${formatTime(item.updateTime)}</td>
                <td>${statusBadge}</td>
                <td>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="editProcedure('${item.procedureId}')" title="编辑">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="duplicateProcedure('${item.procedureId}')" title="复制">
                        <i class="ri-file-copy-line"></i>
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm nx-btn--danger" onclick="deleteProcedure('${item.procedureId}')" title="删除">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }

    function renderCompletenessBar(score) {
        const color = score >= 80 ? 'green' : (score >= 60 ? 'yellow' : 'red');
        return `
            <div class="completeness-bar">
                <div class="bar-fill ${color}" style="width: ${score}%"></div>
                <span class="bar-text">${score}%</span>
            </div>
        `;
    }

    function renderStatusBadge(status) {
        const statusMap = {
            'DRAFT': { label: '草稿', class: 'badge-secondary' },
            'ACTIVE': { label: '激活', class: 'badge-success' },
            'DEPRECATED': { label: '废弃', class: 'badge-warning' }
        };
        const info = statusMap[status] || { label: status, class: 'badge-secondary' };
        return `<span class="badge ${info.class}">${info.label}</span>`;
    }

    function getSourceLabel(source) {
        const sourceMap = {
            'KNOWLEDGE_BASE': '知识库梳理',
            'INDUSTRY_BEST_PRACTICE': '行业最佳实践',
            'MANUAL': '手动创建'
        };
        return sourceMap[source] || source;
    }

    function renderPagination(total) {
        const totalPages = Math.ceil(total / pageSize);
        const pagination = $('#pagination');
        pagination.innerHTML = '';

        if (totalPages <= 1) return;

        let html = '';
        html += `<button class="page-btn" onclick="goToPage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>
            <i class="ri-arrow-left-s-line"></i>
        </button>`;

        for (let i = 1; i <= totalPages; i++) {
            if (i === 1 || i === totalPages || (i >= currentPage - 2 && i <= currentPage + 2)) {
                html += `<button class="page-btn ${i === currentPage ? 'active' : ''}" onclick="goToPage(${i})">${i}</button>`;
            } else if (i === currentPage - 3 || i === currentPage + 3) {
                html += '<span class="page-ellipsis">...</span>';
            }
        }

        html += `<button class="page-btn" onclick="goToPage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>
            <i class="ri-arrow-right-s-line"></i>
        </button>`;

        pagination.innerHTML = html;
    }

    window.goToPage = function(page) {
        currentPage = page;
        loadData();
    };

    window.search = function() {
        currentPage = 1;
        loadData();
    };

    function initUpload() {
        const uploadArea = $('#uploadArea');
        const fileInput = $('#fileInput');

        uploadArea.addEventListener('click', function() {
            fileInput.click();
        });

        uploadArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            uploadArea.classList.add('dragover');
        });

        uploadArea.addEventListener('dragleave', function() {
            uploadArea.classList.remove('dragover');
        });

        uploadArea.addEventListener('drop', function(e) {
            e.preventDefault();
            uploadArea.classList.remove('dragover');
            handleFiles(e.dataTransfer.files);
        });

        fileInput.addEventListener('change', function() {
            handleFiles(this.files);
        });
    }

    function handleFiles(files) {
        for (let file of files) {
            if (!uploadedFiles.find(f => f.name === file.name)) {
                uploadedFiles.push(file);
                renderFileList();
            }
        }
    }

    function renderFileList() {
        const fileList = $('#fileList');
        fileList.innerHTML = '';

        uploadedFiles.forEach((file, index) => {
            const div = document.createElement('div');
            div.className = 'file-item';
            div.innerHTML = `
                <i class="ri-file-text-line"></i>
                <span class="file-name">${file.name}</span>
                <span class="file-size">${formatFileSize(file.size)}</span>
                <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="removeFile(${index})">
                    <i class="ri-close-line"></i>
                </button>
            `;
            fileList.appendChild(div);
        });
    }

    window.removeFile = function(index) {
        uploadedFiles.splice(index, 1);
        renderFileList();
    };

    window.showLlmAssistModal = function() {
        uploadedFiles = [];
        currentStep = 1;
        updateStepUI();
        $('#llmAssistModal').style.display = 'flex';
    };

    window.showCreateModal = function() {
        window.location.href = 'enterprise-procedure-create.html';
    };

    window.closeModal = function(modalId) {
        document.getElementById(modalId).style.display = 'none';
    };

    window.nextStep = function() {
        if (currentStep === 1) {
            if (uploadedFiles.length === 0) {
                alert('请先上传文档');
                return;
            }
            startLlmProcess();
        } else if (currentStep === 2) {
        } else if (currentStep === 3) {
            saveProcedure();
        }
    };

    function updateStepUI() {
        $$('.wizard-steps .step').forEach(step => {
            step.classList.remove('active', 'completed');
        });
        
        const currentStepEl = document.querySelector(`.wizard-steps .step[data-step="${currentStep}"]`);
        if (currentStepEl) {
            currentStepEl.classList.add('active');
        }
        
        for (let i = 1; i < currentStep; i++) {
            const prevStepEl = document.querySelector(`.wizard-steps .step[data-step="${i}"]`);
            if (prevStepEl) {
                prevStepEl.classList.add('completed');
            }
        }

        $$('.step-content').forEach(content => {
            content.style.display = 'none';
        });
        
        const currentContent = document.getElementById(`step${currentStep}Content`);
        if (currentContent) {
            currentContent.style.display = 'block';
        }

        const nextStepBtn = $('#nextStepBtn');
        if (currentStep === 1) {
            nextStepBtn.textContent = '开始解析';
        } else if (currentStep === 2) {
            nextStepBtn.textContent = '处理中...';
            nextStepBtn.disabled = true;
        } else if (currentStep === 3) {
            nextStepBtn.textContent = '保存';
            nextStepBtn.disabled = false;
        }
    }

    function startLlmProcess() {
        currentStep = 2;
        updateStepUI();

        const formData = new FormData();
        uploadedFiles.forEach(file => {
            formData.append('files', file);
        });

        let progress = 0;
        const progressFill = $('#llmProgressFill');
        const progressText = $('#llmProgressText');
        
        const progressInterval = setInterval(() => {
            progress += Math.random() * 15;
            if (progress > 90) progress = 90;
            progressFill.style.width = progress + '%';
        }, 500);

        fetch('/api/v1/enterprise-procedures/llm-preview', {
            method: 'POST',
            body: formData
        })
        .then(res => res.json())
        .then(response => {
            clearInterval(progressInterval);
            progressFill.style.width = '100%';
            progressText.textContent = '解析完成';

            setTimeout(() => {
                currentStep = 3;
                updateStepUI();
                renderPreview(response);
            }, 500);
        })
        .catch(error => {
            clearInterval(progressInterval);
            progressText.textContent = '解析失败: ' + error.message;
            const nextStepBtn = $('#nextStepBtn');
            nextStepBtn.textContent = '重试';
            nextStepBtn.disabled = false;
        });
    }

    function renderPreview(preview) {
        const procedure = preview.procedure;
        let html = '';

        html += `<div class="preview-item">
            <label>流程名称</label>
            <input type="text" id="previewName" value="${procedure.name || ''}">
        </div>`;

        html += `<div class="preview-item">
            <label>分类</label>
            <input type="text" id="previewCategory" value="${procedure.category || ''}">
        </div>`;

        html += `<div class="preview-item">
            <label>描述</label>
            <textarea id="previewDescription">${procedure.description || ''}</textarea>
        </div>`;

        if (procedure.roles && procedure.roles.length > 0) {
            html += `<div class="preview-section">
                <h4>角色定义 (${procedure.roles.length})</h4>
                <ul class="preview-list">`;
            procedure.roles.forEach(role => {
                html += `<li>${role.name} (${role.minCount}-${role.maxCount}人) ${role.required ? '必需' : '可选'}</li>`;
            });
            html += `</ul></div>`;
        }

        if (procedure.steps && procedure.steps.length > 0) {
            html += `<div class="preview-section">
                <h4>流程步骤 (${procedure.steps.length})</h4>
                <ol class="preview-list">`;
            procedure.steps.forEach(step => {
                html += `<li>${step.name}</li>`;
            });
            html += `</ol></div>`;
        }

        html += `<div class="preview-item">
            <label>完善度</label>
            <div class="completeness-bar">
                <div class="bar-fill" style="width: ${preview.completeness || 0}%"></div>
                <span class="bar-text">${preview.completeness || 0}%</span>
            </div>
        </div>`;

        if (preview.suggestions && preview.suggestions.length > 0) {
            html += `<div class="preview-section suggestions">
                <h4>改进建议</h4>
                <ul class="suggestion-list">`;
            preview.suggestions.forEach(s => {
                html += `<li>${s}</li>`;
            });
            html += `</ul></div>`;
        }

        $('#previewContent').innerHTML = html;
    }

    function saveProcedure() {
        const formData = new FormData();
        uploadedFiles.forEach(file => {
            formData.append('files', file);
        });
        formData.append('organizationId', getCurrentOrgId());
        formData.append('author', getCurrentUserId());

        fetch('/api/v1/enterprise-procedures/llm-assist', {
            method: 'POST',
            body: formData
        })
        .then(res => res.json())
        .then(response => {
            closeModal('llmAssistModal');
            loadData();
            showSuccess('企业规范流程创建成功');
        })
        .catch(error => {
            showError('创建失败: ' + error.message);
        });
    }

    window.editProcedure = function(id) {
        window.location.href = `enterprise-procedure-detail.html?id=${id}`;
    };

    window.duplicateProcedure = function(id) {
        if (!confirm('确定要复制该企业规范流程吗？')) return;
        
        fetch(`/api/v1/enterprise-procedures/${id}/duplicate`, {
            method: 'POST'
        })
        .then(res => res.json())
        .then(response => {
            loadData();
            showSuccess('复制成功');
        })
        .catch(error => {
            showError('复制失败: ' + error.message);
        });
    };

    window.deleteProcedure = function(id) {
        if (!confirm('确定要删除该企业规范流程吗？此操作不可恢复。')) return;
        
        fetch(`/api/v1/enterprise-procedures/${id}`, {
            method: 'DELETE'
        })
        .then(res => {
            loadData();
            showSuccess('删除成功');
        })
        .catch(error => {
            showError('删除失败: ' + error.message);
        });
    };

    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + ' B';
        if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    }

    function formatTime(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleString('zh-CN');
    }

    function getCurrentOrgId() {
        return localStorage.getItem('currentOrgId') || 'default';
    }

    function getCurrentUserId() {
        return localStorage.getItem('userId') || 'system';
    }

    function showSuccess(message) {
        if (window.NX && NX.success) {
            NX.success(message);
        } else {
            alert(message);
        }
    }

    function showError(message) {
        if (window.NX && NX.error) {
            NX.error(message);
        } else {
            alert(message);
        }
    }
})();
