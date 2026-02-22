// 统一的应用脚本文件
// 整合所有页面的JavaScript功能

// ==================== 存储管理 ====================
// 存储数据
if (typeof appStorageItems === 'undefined') {
    window.appStorageItems = [
    {
        id: "json-001",
        name: "技能元数据存储",
        type: "json",
        path: "./skill_metadata.json",
        size: "2.5 MB",
        lastModified: "2026-01-31 10:00:00"
    },
    {
        id: "json-002",
        name: "用户配置存储",
        type: "json",
        path: "./user_config.json",
        size: "1.2 MB",
        lastModified: "2026-01-30 15:30:00"
    },
    {
        id: "json-003",
        name: "执行历史存储",
        type: "json",
        path: "./execution_history.json",
        size: "5.8 MB",
        lastModified: "2026-01-29 09:15:00"
    },
    {
        id: "vfs-001",
        name: "技能代码存储",
        type: "vfs",
        path: "./vfs/skills",
        size: "1.5 GB",
        lastModified: "2026-01-28 14:45:00"
    },
    {
        id: "vfs-002",
        name: "资源文件存储",
        type: "vfs",
        path: "./vfs/resources",
        size: "3.2 GB",
        lastModified: "2026-01-27 11:20:00"
    }
];
}

// 存储管理功能
function renderStorageList(storageType) {
    const storageList = document.getElementById('jsonStorageList');
    if (!storageList) return;
    
    storageList.innerHTML = '';
    
    const filteredItems = appStorageItems.filter(item => 
        storageType === 'all' || item.type === storageType
    );
    
    filteredItems.forEach(item => {
        const storageItem = document.createElement('div');
        storageItem.className = 'storage-item';
        storageItem.innerHTML = `
            <div class="info">
                <div class="name">${item.name}</div>
                <div class="details">
                    <span>类型: ${item.type.toUpperCase()}</span> | 
                    <span>路径: ${item.path}</span> | 
                    <span>修改时间: ${item.lastModified}</span>
                </div>
            </div>
            <div class="size">${item.size}</div>
            <div class="action-buttons">
                <button class="btn btn-secondary" onclick="viewStorageDetails('${item.id}')">
                    <i class="ri-eye-line"></i> 查看
                </button>
                <button class="btn btn-danger" onclick="deleteStorage('${item.id}')">
                    <i class="ri-delete-line"></i> 删除
                </button>
            </div>
        `;
        storageList.appendChild(storageItem);
    });
}

function switchStorageTab(tabType, event) {
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(button => button.classList.remove('active'));
    event.target.classList.add('active');
    
    renderStorageList(tabType);
}

function openAddStorageModal() {
    const storageForm = document.getElementById('storageForm');
    if (storageForm) {
        storageForm.reset();
        const storageModal = document.getElementById('storageModal');
        if (storageModal) {
            storageModal.style.display = 'block';
        }
    }
}

function closeStorageModal() {
    const storageModal = document.getElementById('storageModal');
    if (storageModal) {
        storageModal.style.display = 'none';
    }
}

function viewStorageDetails(storageId) {
    const storage = appStorageItems.find(s => s.id === storageId);
    if (storage) {
        alert(`存储详情:\n\n名称: ${storage.name}\n类型: ${storage.type}\n路径: ${storage.path}\n大小: ${storage.size}\n修改时间: ${storage.lastModified}`);
    }
}

function deleteStorage(storageId) {
    if (confirm('确定要删除这个存储吗？')) {
        const storageIndex = appStorageItems.findIndex(s => s.id === storageId);
        if (storageIndex !== -1) {
            appStorageItems.splice(storageIndex, 1);
            renderStorageList('json');
        }
    }
}

function initStorageForm() {
    const storageForm = document.getElementById('storageForm');
    if (storageForm) {
        storageForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const storageName = document.getElementById('storageName').value;
            const storageType = document.getElementById('storageType').value;
            const storagePath = document.getElementById('storagePath').value;
            
            const newStorage = {
                id: `${storageType}-${Date.now()}`,
                name: storageName,
                type: storageType,
                path: storagePath,
                size: "0 MB",
                lastModified: new Date().toLocaleString()
            };
            appStorageItems.push(newStorage);
            
            renderStorageList('json');
            closeStorageModal();
        });
    }
}

// ==================== 技能分享 ====================
function renderSharedSkills() {
    const sharedSkillsContainer = document.getElementById('shared-skills');
    if (!sharedSkillsContainer) return;
    
    sharedSkillsContainer.innerHTML = '<div class="loading">加载中...</div>';
    
    fetch(`${utils.API_BASE_URL}/share/shared`)
        .then(response => response.json())
        .then(data => {
            sharedSkillsContainer.innerHTML = '';
            
            if (data.success && data.data) {
                const sharedSkills = data.data;
                if (sharedSkills.length > 0) {
                    sharedSkills.forEach(skill => {
                        const skillCard = document.createElement('div');
                        skillCard.className = 'skill-sharing-card';
                        skillCard.innerHTML = `
                            <div class="skill-sharing-card-header">
                                <h4>技能分享</h4>
                                <span class="sharing-status">已分享</span>
                            </div>
                            <div class="skill-sharing-card-body">
                                <div class="sharing-info">
                                    <span class="sharing-group">分享到群组 ${skill.groupId}</span>
                                    <span class="sharing-time">分享时间: ${skill.sharedAt ? new Date(skill.sharedAt).toLocaleString() : new Date().toLocaleString()}</span>
                                </div>
                                <div class="sharing-message">
                                    <strong>分享消息:</strong>
                                    <p>${skill.message || '分享了一个技能'}</p>
                                </div>
                            </div>
                            <div class="skill-sharing-card-footer">
                                <button class="btn-sm btn-danger unshare-btn" data-id="${skill.id}">
                                    <i class="ri-close-circle-line"></i> 取消分享
                                </button>
                            </div>
                        `;
                        sharedSkillsContainer.appendChild(skillCard);
                    });
                } else {
                    sharedSkillsContainer.innerHTML = '<div class="empty-state"><i class="ri-share-line"></i><p>暂无已分享技能</p></div>';
                }
            } else {
                sharedSkillsContainer.innerHTML = '<div class="error">获取已分享技能失败</div>';
            }
            
            document.querySelectorAll('.unshare-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const shareId = this.getAttribute('data-id');
                    unshareSkill(shareId);
                });
            });
        })
        .catch(error => {
            console.error('Error fetching shared skills:', error);
            sharedSkillsContainer.innerHTML = '<div class="error">获取已分享技能失败</div>';
        });
}

function renderReceivedSkills() {
    const receivedSkillsContainer = document.getElementById('received-skills');
    if (!receivedSkillsContainer) return;
    
    receivedSkillsContainer.innerHTML = '<div class="loading">加载中...</div>';
    
    fetch(`${utils.API_BASE_URL}/share/received`)
        .then(response => response.json())
        .then(data => {
            receivedSkillsContainer.innerHTML = '';
            
            if (data.success && data.data) {
                const receivedSkills = data.data;
                if (receivedSkills.length > 0) {
                    receivedSkills.forEach(skill => {
                        const skillCard = document.createElement('div');
                        skillCard.className = 'skill-sharing-card';
                        skillCard.innerHTML = `
                            <div class="skill-sharing-card-header">
                                <h4>${skill.skillName}</h4>
                                <span class="sharing-status">已收到</span>
                            </div>
                            <div class="skill-sharing-card-body">
                                <div class="sharing-info">
                                    <span class="sharing-sharer">分享人: ${skill.sharerName}</span>
                                    <span class="sharing-group">来自群组: ${skill.groupName}</span>
                                    <span class="sharing-time">收到时间: ${skill.receivedAt ? new Date(skill.receivedAt).toLocaleString() : new Date().toLocaleString()}</span>
                                </div>
                                <div class="sharing-message">
                                    <strong>分享消息:</strong>
                                    <p>${skill.message || ''}</p>
                                </div>
                            </div>
                        `;
                        receivedSkillsContainer.appendChild(skillCard);
                    });
                } else {
                    receivedSkillsContainer.innerHTML = '<div class="empty-state"><i class="ri-share-line"></i><p>暂无收到的技能</p></div>';
                }
            } else {
                receivedSkillsContainer.innerHTML = '<div class="error">获取收到的技能失败</div>';
            }
        })
        .catch(error => {
            console.error('Error fetching received skills:', error);
            receivedSkillsContainer.innerHTML = '<div class="error">获取收到的技能失败</div>';
        });
}

function unshareSkill(shareId) {
    if (confirm('确定要取消分享这个技能吗？')) {
        fetch(`${utils.API_BASE_URL}/share/${shareId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                msg.success('取消分享成功');
                renderSharedSkills();
            } else {
                msg.error('取消分享失败');
            }
        })
        .catch(error => {
            console.error('Error unsharing skill:', error);
            msg.error('取消分享失败');
        });
    }
}

function openShareModal() {
    const shareModal = document.getElementById('share-modal');
    const shareForm = document.getElementById('share-form');
    
    if (shareModal) {
        shareModal.style.display = 'block';
        
        loadSkillList();
        loadGroupList();
    }
}

function closeShareModal() {
    const shareModal = document.getElementById('share-modal');
    if (shareModal) {
        shareModal.style.display = 'none';
    }
}

// 技能选择器实例存储 - 使用 window 对象避免重复声明
if (!window.skillSelectors) {
    window.skillSelectors = {};
}

// 初始化技能选择器 - 使用统一封装的组件
function initSkillSelector(selectId, options = {}) {
    if (utils && utils.SkillSelector) {
        window.skillSelectors[selectId] = utils.SkillSelector({
            selectId,
            ...options
        });
        return window.skillSelectors[selectId];
    }
    return null;
}

// 获取技能选择器实例
function getSkillSelector(selectId) {
    return window.skillSelectors[selectId] || null;
}

// 加载技能列表 - 兼容旧代码
function loadSkillList() {
    initSkillSelector('skill-select', {
        emptyText: '请选择技能',
        pageName: 'App'
    });
}

// 加载群组列表 - 兼容旧代码
function loadGroupList() {
    if (utils && utils.loadSelectOptions) {
        utils.loadSelectOptions('group-select', '/admin/groups', {
            emptyText: '请选择群组'
        });
    }
}

function submitShare() {
    console.log('[App] 提交分享...');
    
    const skillId = document.getElementById('share-skill-select').value;
    const groupId = document.getElementById('share-group-select').value;
    const message = document.getElementById('share-message').value;
    const submitBtn = document.getElementById('submit-share-btn');
    
    console.log('[App] 分享数据:', { skillId, groupId, message });
    console.log('[App] utils.API_BASE_URL:', utils?.API_BASE_URL);
    
    if (!skillId || !groupId) {
        console.log('[App] 验证失败: 请选择技能和群组');
        if (typeof msg !== 'undefined') {
            msg.error('请选择技能和群组');
        } else {
            alert('请选择技能和群组');
        }
        return;
    }
    
    submitBtn.disabled = true;
    
    const url = `${utils.API_BASE_URL}/share`;
    console.log('[App] 请求URL:', url);
    
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            skillId: skillId,
            groupId: groupId,
            message: message
        })
    })
    .then(response => {
        console.log('[App] 响应状态:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('[App] 响应数据:', data);
        submitBtn.disabled = false;
        if (data.success) {
            if (typeof msg !== 'undefined') {
                msg.success('分享成功');
            } else {
                alert('分享成功');
            }
            // 清空表单
            document.getElementById('share-skill-select').value = '';
            document.getElementById('share-group-select').value = '';
            document.getElementById('share-message').value = '';
            renderSharedSkills();
        } else {
            const errorMsg = data.message || '分享失败';
            console.error('[App] 分享失败:', errorMsg);
            if (typeof msg !== 'undefined') {
                msg.error(errorMsg);
            } else {
                alert(errorMsg);
            }
        }
    })
    .catch(error => {
        submitBtn.disabled = false;
        console.error('[App] 分享请求错误:', error);
        if (typeof msg !== 'undefined') {
            msg.error('分享失败: ' + error.message);
        } else {
            alert('分享失败: ' + error.message);
        }
    });
}

function initShareForm() {
    const shareForm = document.getElementById('share-form');
    if (shareForm) {
        shareForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const skillId = document.getElementById('skill-select').value;
            const groupId = document.getElementById('group-select').value;
            const submitBtn = document.getElementById('submit-share-btn');
            
            if (!skillId || !groupId) {
                msg.error('请选择技能和群组');
                return;
            }
            
            submitBtn.disabled = true;
            
            fetch(`${utils.API_BASE_URL}/share`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    skillId: skillId,
                    groupId: groupId
                })
            })
            .then(response => response.json())
            .then(data => {
                submitBtn.disabled = false;
                if (data.success) {
                    msg.success('分享成功');
                    closeShareModal();
                    renderSharedSkills();
                } else {
                    msg.error('分享失败');
                }
            })
            .catch(error => {
                submitBtn.disabled = false;
                console.error('Error sharing skill:', error);
                msg.error('分享失败');
            });
        });
    }
}

// ==================== 执行历史 ====================
function renderExecutionHistory() {
    const historyContainer = document.getElementById('execution-history');
    if (!historyContainer) return;
    
    historyContainer.innerHTML = '<div class="loading">加载中...</div>';
    
    fetch(`${utils.API_BASE_URL}/execution/history`)
        .then(response => response.json())
        .then(data => {
            historyContainer.innerHTML = '';
            
            if (data && Object.keys(data).length > 0) {
                Object.entries(data).forEach(([executionId, result]) => {
                    const historyItem = document.createElement('div');
                    historyItem.className = 'execution-history-item';
                    historyItem.innerHTML = `
                        <div class="history-item-header">
                            <h4>技能执行</h4>
                            <span class="execution-status ${result.status}">${result.status === 'SUCCESS' ? '成功' : '失败'}</span>
                        </div>
                        <div class="history-item-body">
                            <div class="history-item-meta">
                                <span class="execution-id">执行ID: ${executionId}</span>
                                <span class="execution-time">执行时间: ${new Date().toLocaleString()}</span>
                                <span class="execution-duration">耗时: 0.5s</span>
                            </div>
                        </div>
                        <div class="history-item-footer">
                            <button class="btn-sm btn-primary view-result-btn" data-id="${executionId}">
                                <i class="ri-file-text-line"></i> 查看结果
                            </button>
                        </div>
                    `;
                    historyContainer.appendChild(historyItem);
                });
            } else {
                historyContainer.innerHTML = '<div class="empty-state"><i class="ri-history-line"></i><p>暂无执行历史</p></div>';
            }
            
            document.querySelectorAll('.view-result-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const executionId = this.getAttribute('data-id');
                    viewExecutionResult(executionId);
                });
            });
        })
        .catch(error => {
            console.error('Error fetching execution history:', error);
            historyContainer.innerHTML = '<div class="error">获取执行历史失败</div>';
        });
}

function viewExecutionResult(executionId) {
    fetch(`${utils.API_BASE_URL}/execution/result/${executionId}`)
        .then(response => response.json())
        .then(result => {
            if (result) {
                const resultSkillName = document.getElementById('result-skill-name');
                const resultStatus = document.getElementById('result-status');
                const resultTime = document.getElementById('result-time');
                const resultOutput = document.getElementById('result-output');
                const resultModal = document.getElementById('result-modal');
                
                if (resultSkillName) {
                    resultSkillName.textContent = '技能执行';
                }
                if (resultStatus) {
                    resultStatus.textContent = result.status === 'SUCCESS' ? '成功' : '失败';
                }
                if (resultTime) {
                    resultTime.textContent = new Date().toLocaleString();
                }
                if (resultOutput) {
                    resultOutput.textContent = JSON.stringify(result, null, 2);
                }
                if (resultModal) {
                    resultModal.style.display = 'flex';
                }
            } else {
                alert('获取执行结果失败');
            }
        })
        .catch(error => {
            console.error('Error fetching execution result:', error);
            alert('获取执行结果失败');
        });
}

function executeSkill() {
    const skillSelect = document.getElementById('skill-select');
    const skillParams = document.getElementById('skill-params');
    const executionMode = document.querySelector('input[name="execution-mode"]:checked')?.value || 'sync';
    
    if (!skillSelect.value) {
        alert('请选择技能');
        return;
    }
    
    const skillId = skillSelect.value;
    const skillName = skillSelect.options[skillSelect.selectedIndex].text;
    const params = skillParams.value || '{}';
    
    let parameters = {};
    try {
        parameters = JSON.parse(params);
    } catch (e) {
        console.error('Invalid JSON parameters:', e);
    }
    
    const executeBtn = document.getElementById('submit-execute-btn');
    if (executeBtn) {
        executeBtn.disabled = true;
    }
    
    fetch(`${utils.API_BASE_URL}/execution/execute/${skillId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            params: parameters,
            mode: executionMode
        })
    })
    .then(response => response.json())
    .then(data => {
        if (executeBtn) {
            executeBtn.disabled = false;
        }
        if (data.success) {
            msg.success('技能执行成功');
            renderExecutionHistory();
        } else {
            msg.error('技能执行失败');
        }
    })
    .catch(error => {
        if (executeBtn) {
            executeBtn.disabled = false;
        }
        console.error('Error executing skill:', error);
        msg.error('技能执行失败');
    });
}

// ==================== 页面初始化 ====================
function initStorageManagementPage() {
    if (document.getElementById('jsonStorageList')) {
        renderStorageList('json');
    }
    
    initStorageForm();
}

function initMySharingPage() {
    console.log('[App] 初始化技能分享页面');

    if (document.getElementById('shared-skills')) {
        renderSharedSkills();
    }

    if (document.getElementById('received-skills')) {
        renderReceivedSkills();
    }

    // 使用统一封装的技能选择器组件初始化技能和群组选择器
    initSkillSelector('share-skill-select', {
        emptyText: '请选择技能',
        pageName: 'MySharing',
        onLoad: (skills) => {
            console.log('[MySharing] 技能选择器加载完成，共', skills.length, '个技能');
        }
    });

    // 初始化群组选择器
    if (utils && utils.SkillSelector) {
        utils.SkillSelector({
            selectId: 'share-group-select',
            apiUrl: '/api/admin/groups',
            emptyText: '请选择群组',
            pageName: 'MySharing'
        });
    }

    initShareForm();
    
    // 绑定分享技能按钮（页面顶部的按钮）
    const shareSkillBtn = document.getElementById('share-skill-btn');
    if (shareSkillBtn) {
        console.log('[App] 绑定分享技能按钮');
        shareSkillBtn.addEventListener('click', function() {
            console.log('[App] 点击分享技能按钮');
            // 切换到分享标签页
            const shareTab = document.getElementById('share-tab');
            const sharedTab = document.getElementById('shared-tab');
            const receivedTab = document.getElementById('received-tab');
            const tabBtns = document.querySelectorAll('.tab-btn');
            
            if (shareTab) shareTab.style.display = 'block';
            if (sharedTab) sharedTab.style.display = 'none';
            if (receivedTab) receivedTab.style.display = 'none';
            
            tabBtns.forEach(btn => {
                if (btn.getAttribute('data-tab') === 'share') {
                    btn.classList.add('active');
                } else {
                    btn.classList.remove('active');
                }
            });
        });
    }
    
    // 绑定关闭模态框按钮
    const closeShareModalBtn = document.getElementById('close-share-modal');
    if (closeShareModalBtn) {
        closeShareModalBtn.addEventListener('click', closeShareModal);
    }
    
    const closeShareBtn = document.getElementById('close-share-btn');
    if (closeShareBtn) {
        closeShareBtn.addEventListener('click', closeShareModal);
    }
    
    // 绑定提交分享按钮
    const submitShareBtn = document.getElementById('submit-share-btn');
    if (submitShareBtn) {
        submitShareBtn.addEventListener('click', submitShare);
    }
}

function initMyExecutionPage() {
    if (document.getElementById('execution-history')) {
        renderExecutionHistory();
    }

    // 使用统一封装的技能选择器组件
    initSkillSelector('skill-select', {
        emptyText: '请选择技能',
        pageName: 'MyExecution',
        onLoad: (skills) => {
            console.log('[MyExecution] 技能选择器加载完成，共', skills.length, '个技能');
        }
    });

    const executeBtn = document.getElementById('submit-execute-btn');
    if (executeBtn) {
        executeBtn.addEventListener('click', executeSkill);
    }

    const closeResultBtn = document.getElementById('close-result-btn');
    if (closeResultBtn) {
        closeResultBtn.addEventListener('click', function() {
            const resultModal = document.getElementById('result-modal');
            if (resultModal) {
                resultModal.style.display = 'none';
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    
    if (currentPath.includes('storage-management.html')) {
        initStorageManagementPage();
    } else if (currentPath.includes('my-sharing.html')) {
        initMySharingPage();
    } else if (currentPath.includes('my-execution.html')) {
        initMyExecutionPage();
    }
});
