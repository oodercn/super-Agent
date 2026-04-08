/**
 * 我的技能页面脚本
 */

// 获取分类名称
function getCategoryName(category) {
    const categories = {
        'development': '开发工具',
        'utilities': '实用工具',
        'analysis': '数据分析',
        'automation': '自动化',
        'other': '其他'
    };
    return categories[category] || category;
}

// 渲染技能列表
async function renderSkillList() {
    const skillList = document.getElementById('skill-list');
    skillList.innerHTML = '<div class="loading">加载中...</div>';
    
    try {
        const data = await utils.api.get('/skills');
        if (data.success && data.data) {
            const skills = data.data.items || [];
            skillList.innerHTML = '';
            
            skills.forEach(skill => {
                const skillCard = document.createElement('div');
                skillCard.className = 'skill-card';
                skillCard.innerHTML = `
                    <div class="skill-card-header">
                        <h3>${skill.name}</h3>
                        <span class="skill-category">${getCategoryName(skill.category || 'other')}</span>
                    </div>
                    <div class="skill-card-body">
                        <p>${skill.description || ''}</p>
                        <div class="skill-meta">
                            <span class="skill-version">版本:1.0.0</span>
                            <span class="skill-created">创建于: ${skill.createdAt ? utils.date.format(skill.createdAt) : utils.date.format(new Date())}</span>
                            <span class="skill-executed">执行: 0次</span>
                        </div>
                    </div>
                    <div class="skill-card-footer">
                        <button class="btn-sm btn-primary execute-skill-btn" data-id="${skill.id}">
                            <i class="ri-play-line"></i> 执行
                        </button>
                        <button class="btn-sm btn-secondary update-skill-btn" data-id="${skill.id}">
                            <i class="ri-edit-line"></i> 更新
                        </button>
                        <button class="btn-sm btn-danger delete-skill-btn" data-id="${skill.id}">
                            <i class="ri-delete-line"></i> 删除
                        </button>
                    </div>
                `;
                skillList.appendChild(skillCard);
            });
            
            // 绑定事件
            bindSkillEvents();
        } else {
            skillList.innerHTML = '<div class="error">获取技能列表失败</div>';
        }
    } catch (error) {
        console.error('Error fetching skills:', error);
        skillList.innerHTML = '<div class="error">获取技能列表失败</div>';
    }
}

// 绑定技能事件
function bindSkillEvents() {
    // 执行技能按钮
    document.querySelectorAll('.execute-skill-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const skillId = this.getAttribute('data-id');
            executeSkill(skillId);
        });
    });
    
    // 更新技能按钮
    document.querySelectorAll('.update-skill-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const skillId = this.getAttribute('data-id');
            openUpdateModal(skillId);
        });
    });
    
    // 删除技能按钮
    document.querySelectorAll('.delete-skill-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const skillId = this.getAttribute('data-id');
            deleteSkill(skillId);
        });
    });
}

// 执行技能
function executeSkill(skillId) {
    utils.message.show(`执行技能: ${skillId}`, 'info');
    // 这里可以添加实际的执行逻辑
}

// 打开更新模态框
async function openUpdateModal(skillId) {
    try {
        const data = await utils.api.get(`/skills/${skillId}`);
        if (data.success && data.data) {
            const skill = data.data;
            document.getElementById('update-skill-id').value = skill.id;
            document.getElementById('update-skill-name').value = skill.name;
            document.getElementById('update-skill-description').value = skill.description || '';
            document.getElementById('update-skill-category').value = skill.category || 'other';
            document.getElementById('update-skill-version').value = '1.0.0';
            document.getElementById('update-skill-download-url').value = '';
            utils.modal.show('update-modal');
        } else {
            utils.message.show('获取技能详情失败', 'error');
        }
    } catch (error) {
        console.error('Error fetching skill details:', error);
        utils.message.show('获取技能详情失败', 'error');
    }
}

// 删除技能
async function deleteSkill(skillId) {
    if (confirm('确定要删除这个技能吗？')) {
        try {
            const data = await utils.api.delete(`/skills/${skillId}`);
            if (data.success) {
                renderSkillList();
                utils.message.show('技能删除成功！', 'success');
            } else {
                utils.message.show('技能删除失败', 'error');
            }
        } catch (error) {
            console.error('Error deleting skill:', error);
            utils.message.show('技能删除失败', 'error');
        }
    }
}

// 初始化页面
document.addEventListener('DOMContentLoaded', function() {
    // 渲染技能列表
    renderSkillList();
    
    // 发布技能按钮
    const publishSkillBtn = document.getElementById('publish-skill-btn');
    if (publishSkillBtn) {
        publishSkillBtn.addEventListener('click', function() {
            utils.modal.show('publish-modal');
        });
    }
    
    // 关闭发布模态框
    const closePublishModal = document.getElementById('close-publish-modal');
    if (closePublishModal) {
        closePublishModal.addEventListener('click', function() {
            utils.modal.hide('publish-modal');
        });
    }
    
    // 取消发布
    const cancelPublishBtn = document.getElementById('cancel-publish-btn');
    if (cancelPublishBtn) {
        cancelPublishBtn.addEventListener('click', function() {
            utils.modal.hide('publish-modal');
        });
    }
    
    // 提交发布
    const submitPublishBtn = document.getElementById('submit-publish-btn');
    if (submitPublishBtn) {
        submitPublishBtn.addEventListener('click', async function() {
            const form = document.getElementById('publish-skill-form');
            if (form.checkValidity()) {
                const newSkill = {
                    id: document.getElementById('skill-id').value,
                    name: document.getElementById('skill-name').value,
                    description: document.getElementById('skill-description').value,
                    category: document.getElementById('skill-category').value
                };
                
                try {
                    const data = await utils.api.post('/skills', newSkill);
                    if (data.success) {
                        renderSkillList();
                        utils.modal.hide('publish-modal');
                        form.reset();
                        utils.message.show('技能发布成功！', 'success');
                    } else {
                        utils.message.show('技能发布失败', 'error');
                    }
                } catch (error) {
                    console.error('Error publishing skill:', error);
                    utils.message.show('技能发布失败', 'error');
                }
            } else {
                form.reportValidity();
            }
        });
    }
    
    // 关闭更新模态框
    const closeUpdateModal = document.getElementById('close-update-modal');
    if (closeUpdateModal) {
        closeUpdateModal.addEventListener('click', function() {
            utils.modal.hide('update-modal');
        });
    }
    
    // 取消更新
    const cancelUpdateBtn = document.getElementById('cancel-update-btn');
    if (cancelUpdateBtn) {
        cancelUpdateBtn.addEventListener('click', function() {
            utils.modal.hide('update-modal');
        });
    }
    
    // 提交更新
    const submitUpdateBtn = document.getElementById('submit-update-btn');
    if (submitUpdateBtn) {
        submitUpdateBtn.addEventListener('click', async function() {
            const form = document.getElementById('update-skill-form');
            if (form.checkValidity()) {
                const skillId = document.getElementById('update-skill-id').value;
                const updatedSkill = {
                    name: document.getElementById('update-skill-name').value,
                    description: document.getElementById('update-skill-description').value,
                    category: document.getElementById('update-skill-category').value
                };
                
                try {
                    const data = await utils.api.put(`/skills/${skillId}`, updatedSkill);
                    if (data.success) {
                        renderSkillList();
                        utils.modal.hide('update-modal');
                        utils.message.show('技能更新成功！', 'success');
                    } else {
                        utils.message.show('技能更新失败', 'error');
                    }
                } catch (error) {
                    console.error('Error updating skill:', error);
                    utils.message.show('技能更新失败', 'error');
                }
            } else {
                form.reportValidity();
            }
        });
    }
    
    // 搜索技能
    const skillSearchInput = document.getElementById('skill-search-input');
    if (skillSearchInput) {
        skillSearchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const skillCards = document.querySelectorAll('.skill-card');
            skillCards.forEach(card => {
                const skillName = card.querySelector('h3').textContent.toLowerCase();
                const skillDescription = card.querySelector('p').textContent.toLowerCase();
                if (skillName.includes(searchTerm) || skillDescription.includes(searchTerm)) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    }
});
