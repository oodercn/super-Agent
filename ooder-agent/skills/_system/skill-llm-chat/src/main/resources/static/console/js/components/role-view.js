/**
 * RoleView - 角色视图组件
 * 
 * 功能：
 * 1. 角色卡片展示
 * 2. 角色关系图
 * 3. 人员分配可视化
 * 4. 角色统计信息
 */

class RoleView {
    constructor(container, options = {}) {
        this.container = typeof container === 'string' 
            ? document.querySelector(container) 
            : container;
        
        this.options = {
            editable: true,
            showStatistics: true,
            showRelationships: true,
            onRoleClick: null,
            onRoleEdit: null,
            onRoleDelete: null,
            onAssignUser: null,
            ...options
        };
        
        this.roles = [];
        this.selectedRole = null;
        
        this.init();
    }
    
    init() {
        this.container.classList.add('role-view-container');
        this.render();
    }
    
    render() {
        let html = '';
        
        if (this.options.showStatistics && this.roles.length > 0) {
            html += this.renderStatistics();
        }
        
        html += '<div class="roles-grid-view">';
        
        if (this.roles.length === 0) {
            html += `
                <div class="roles-empty">
                    <div class="roles-empty-icon">
                        <i class="ri-team-line"></i>
                    </div>
                    <div class="roles-empty-title">暂无角色定义</div>
                    <div class="roles-empty-desc">请添加流程所需的角色</div>
                </div>
            `;
        } else {
            this.roles.forEach((role, index) => {
                html += this.renderRoleCard(role, index);
            });
        }
        
        html += '</div>';
        
        if (this.options.showRelationships && this.roles.length > 1) {
            html += this.renderRelationships();
        }
        
        this.container.innerHTML = html;
        this.bindEvents();
    }
    
    renderStatistics() {
        const totalRoles = this.roles.length;
        const requiredRoles = this.roles.filter(r => r.required).length;
        const totalMinCount = this.roles.reduce((sum, r) => sum + (r.minCount || 0), 0);
        const totalMaxCount = this.roles.reduce((sum, r) => sum + (r.maxCount || 0), 0);
        
        return `
            <div class="roles-statistics">
                <div class="stat-item">
                    <div class="stat-value">${totalRoles}</div>
                    <div class="stat-label">角色总数</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value required">${requiredRoles}</div>
                    <div class="stat-label">必需角色</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${totalMinCount} - ${totalMaxCount === 0 ? '∞' : totalMaxCount}</div>
                    <div class="stat-label">人数范围</div>
                </div>
            </div>
        `;
    }
    
    renderRoleCard(role, index) {
        const statusClass = this.getRoleStatusClass(role);
        const progressPercent = role.assignedUsers && role.maxCount > 0 
            ? Math.min(100, (role.assignedUsers.length / role.maxCount) * 100)
            : 0;
        
        return `
            <div class="role-card-view ${statusClass}" data-role-index="${index}">
                <div class="role-card-header">
                    <div class="role-icon ${role.required ? 'required' : ''}">
                        <i class="ri-user-line"></i>
                    </div>
                    <div class="role-title-section">
                        <h4 class="role-name">${role.name}</h4>
                        <span class="role-id-badge">${role.roleId}</span>
                    </div>
                    ${this.options.editable ? `
                        <div class="role-actions">
                            <button class="role-action-btn edit" onclick="this.closest('.role-view-container')._roleView.editRole(${index})" title="编辑">
                                <i class="ri-edit-line"></i>
                            </button>
                            <button class="role-action-btn assign" onclick="this.closest('.role-view-container')._roleView.assignUser(${index})" title="分配人员">
                                <i class="ri-user-add-line"></i>
                            </button>
                            <button class="role-action-btn delete" onclick="this.closest('.role-view-container')._roleView.deleteRole(${index})" title="删除">
                                <i class="ri-delete-bin-line"></i>
                            </button>
                        </div>
                    ` : ''}
                </div>
                
                <div class="role-card-body">
                    ${role.description ? `<p class="role-description">${role.description}</p>` : ''}
                    
                    <div class="role-count-section">
                        <div class="role-count-header">
                            <span class="count-label">人员配置</span>
                            <span class="count-value">
                                ${role.assignedUsers?.length || 0} / ${role.minCount}-${role.maxCount === 0 ? '∞' : role.maxCount} 人
                            </span>
                        </div>
                        <div class="role-count-bar">
                            <div class="role-count-fill" style="width: ${progressPercent}%"></div>
                        </div>
                        ${role.required ? '<span class="required-badge"><i class="ri-asterisk"></i> 必需</span>' : ''}
                    </div>
                    
                    ${role.assignedUsers && role.assignedUsers.length > 0 ? `
                        <div class="assigned-users">
                            <div class="assigned-users-header">
                                <span>已分配人员</span>
                                <span class="assigned-count">${role.assignedUsers.length}</span>
                            </div>
                            <div class="assigned-users-list">
                                ${role.assignedUsers.slice(0, 5).map(user => `
                                    <div class="user-avatar" title="${user.name}">
                                        ${user.avatar ? `<img src="${user.avatar}" alt="${user.name}">` : `<span>${user.name.charAt(0)}</span>`}
                                    </div>
                                `).join('')}
                                ${role.assignedUsers.length > 5 ? `
                                    <div class="user-avatar more" title="还有 ${role.assignedUsers.length - 5} 人">
                                        +${role.assignedUsers.length - 5}
                                    </div>
                                ` : ''}
                            </div>
                        </div>
                    ` : ''}
                    
                    ${role.skills && role.skills.length > 0 ? `
                        <div class="role-skills">
                            <span class="skills-label">技能要求:</span>
                            ${role.skills.map(skill => `
                                <span class="skill-tag">${skill}</span>
                            `).join('')}
                        </div>
                    ` : ''}
                </div>
                
                <div class="role-card-footer">
                    <div class="role-meta">
                        ${role.department ? `<span class="meta-item"><i class="ri-building-line"></i> ${role.department}</span>` : ''}
                        ${role.level ? `<span class="meta-item"><i class="ri-medal-line"></i> ${role.level}</span>` : ''}
                    </div>
                </div>
            </div>
        `;
    }
    
    renderRelationships() {
        return `
            <div class="role-relationships">
                <div class="relationships-header">
                    <h4><i class="ri-git-branch-line"></i> 角色关系图</h4>
                    <button class="toggle-relationships-btn" onclick="this.closest('.role-view-container')._roleView.toggleRelationships()">
                        <i class="ri-arrow-down-s-line"></i>
                    </button>
                </div>
                <div class="relationships-content" style="display: none;">
                    <div class="relationship-chart" id="relationshipChart"></div>
                </div>
            </div>
        `;
    }
    
    getRoleStatusClass(role) {
        if (!role.assignedUsers || role.assignedUsers.length === 0) {
            return role.required ? 'status-critical' : 'status-warning';
        }
        if (role.assignedUsers.length < role.minCount) {
            return 'status-warning';
        }
        if (role.maxCount > 0 && role.assignedUsers.length > role.maxCount) {
            return 'status-error';
        }
        return 'status-complete';
    }
    
    bindEvents() {
        this.container._roleView = this;
        
        this.container.querySelectorAll('.role-card-view').forEach(card => {
            card.addEventListener('click', (e) => {
                if (!e.target.closest('.role-action-btn')) {
                    const index = parseInt(card.dataset.roleIndex);
                    this.selectRole(index);
                }
            });
        });
    }
    
    selectRole(index) {
        this.container.querySelectorAll('.role-card-view').forEach(card => {
            card.classList.remove('selected');
        });
        
        const card = this.container.querySelector(`[data-role-index="${index}"]`);
        if (card) {
            card.classList.add('selected');
            this.selectedRole = this.roles[index];
            
            if (this.options.onRoleClick) {
                this.options.onRoleClick(this.roles[index], index);
            }
        }
    }
    
    editRole(index) {
        if (this.options.onRoleEdit) {
            this.options.onRoleEdit(this.roles[index], index);
        }
    }
    
    deleteRole(index) {
        if (this.options.onRoleDelete) {
            this.options.onRoleDelete(this.roles[index], index);
        }
    }
    
    assignUser(index) {
        if (this.options.onAssignUser) {
            this.options.onAssignUser(this.roles[index], index);
        }
    }
    
    toggleRelationships() {
        const content = this.container.querySelector('.relationships-content');
        const btn = this.container.querySelector('.toggle-relationships-btn i');
        
        if (content.style.display === 'none') {
            content.style.display = 'block';
            btn.className = 'ri-arrow-up-s-line';
            this.renderRelationshipChart();
        } else {
            content.style.display = 'none';
            btn.className = 'ri-arrow-down-s-line';
        }
    }
    
    renderRelationshipChart() {
        const chartContainer = this.container.querySelector('#relationshipChart');
        if (!chartContainer) return;
        
        const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        svg.setAttribute('width', '100%');
        svg.setAttribute('height', '200');
        
        const centerX = chartContainer.offsetWidth / 2;
        const centerY = 100;
        const radius = Math.min(centerX, centerY) - 50;
        
        this.roles.forEach((role, index) => {
            const angle = (2 * Math.PI * index) / this.roles.length - Math.PI / 2;
            const x = centerX + radius * Math.cos(angle);
            const y = centerY + radius * Math.sin(angle);
            
            const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
            circle.setAttribute('cx', x);
            circle.setAttribute('cy', y);
            circle.setAttribute('r', 20);
            circle.setAttribute('fill', role.required ? '#f59e0b' : '#3b82f6');
            circle.setAttribute('class', 'role-node');
            svg.appendChild(circle);
            
            const text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
            text.setAttribute('x', x);
            text.setAttribute('y', y + 35);
            text.setAttribute('text-anchor', 'middle');
            text.setAttribute('font-size', '12');
            text.setAttribute('fill', '#6b7280');
            text.textContent = role.name;
            svg.appendChild(text);
        });
        
        chartContainer.innerHTML = '';
        chartContainer.appendChild(svg);
    }
    
    loadRoles(roles) {
        this.roles = roles || [];
        this.render();
    }
    
    addRole(role) {
        this.roles.push(role);
        this.render();
    }
    
    updateRole(index, role) {
        if (index >= 0 && index < this.roles.length) {
            this.roles[index] = role;
            this.render();
        }
    }
    
    removeRole(index) {
        if (index >= 0 && index < this.roles.length) {
            this.roles.splice(index, 1);
            this.render();
        }
    }
    
    getRoles() {
        return this.roles;
    }
    
    getSelectedRole() {
        return this.selectedRole;
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = RoleView;
}

window.RoleView = RoleView;
