(function() {
    'use strict';

    var MyProfile = {
        currentUser: null,
        originalData: {},
        
        init: function() {
            this.loadUserProfile();
            this.loadUserStats();
            this.bindEvents();
        },
        
        bindEvents: function() {
            var self = this;
            document.getElementById('profileForm').addEventListener('submit', function(e) {
                e.preventDefault();
                self.saveProfile();
            });
        },
        
        loadUserProfile: function() {
            var self = this;
            fetch('/api/v1/org/users/current')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.currentUser = result.data;
                        self.originalData = {
                            name: self.currentUser.name || '',
                            title: self.currentUser.title || '',
                            email: self.currentUser.email || '',
                            phone: self.currentUser.phone || ''
                        };
                        self.renderProfile(self.currentUser);
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load user profile:', error);
                    self.showToast('加载用户信息失败', 'error');
                });
        },
        
        loadUserStats: function() {
            fetch('/api/v1/org/users/current/stats')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        document.getElementById('statScenes').textContent = result.data.scenesCount || 0;
                        document.getElementById('statCapabilities').textContent = result.data.capabilitiesCount || 0;
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load user stats:', error);
                });
        },
        
        renderProfile: function(user) {
            document.getElementById('profileAvatar').textContent = (user.name || 'U').charAt(0).toUpperCase();
            document.getElementById('profileName').textContent = user.name || '未知用户';
            
            var roleMap = {
                'admin': { name: '管理员', icon: 'ri-admin-line' },
                'manager': { name: '管理者', icon: 'ri-user-settings-line' },
                'leader': { name: '主导者', icon: 'ri-user-star-line' },
                'collaborator': { name: '协作者', icon: 'ri-team-line' },
                'installer': { name: '安装者', icon: 'ri-install-line' },
                'employee': { name: '员工', icon: 'ri-user-line' }
            };
            var roleInfo = roleMap[user.role] || { name: user.role || '用户', icon: 'ri-shield-user-line' };
            document.getElementById('profileRole').innerHTML = '<i class="' + roleInfo.icon + '"></i><span>' + roleInfo.name + '</span>';
            
            document.getElementById('infoUserId').textContent = user.userId || '-';
            document.getElementById('infoCreateTime').textContent = this.formatTime(user.createTime);
            document.getElementById('infoUpdateTime').textContent = this.formatTime(user.updateTime);
            
            document.getElementById('formName').value = user.name || '';
            document.getElementById('formTitle').value = user.title || '';
            document.getElementById('formEmail').value = user.email || '';
            document.getElementById('formPhone').value = user.phone || '';
            document.getElementById('formDepartment').value = user.departmentId || '-';
            document.getElementById('formRole').value = roleInfo.name;
        },
        
        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
        },
        
        saveProfile: function() {
            var self = this;
            if (!this.currentUser) return;
            
            var updatedData = {
                name: document.getElementById('formName').value,
                title: document.getElementById('formTitle').value,
                email: document.getElementById('formEmail').value,
                phone: document.getElementById('formPhone').value
            };
            
            fetch('/api/v1/org/users/' + this.currentUser.userId, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedData)
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.currentUser = result.data;
                        self.originalData = {
                            name: self.currentUser.name || '',
                            title: self.currentUser.title || '',
                            email: self.currentUser.email || '',
                            phone: self.currentUser.phone || ''
                        };
                        self.renderProfile(self.currentUser);
                        self.showToast('保存成功', 'success');
                    } else {
                        self.showToast(result.message || '保存失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('Failed to save profile:', error);
                    self.showToast('保存失败', 'error');
                });
        },
        
        resetForm: function() {
            if (!this.currentUser) return;
            document.getElementById('formName').value = this.originalData.name;
            document.getElementById('formTitle').value = this.originalData.title;
            document.getElementById('formEmail').value = this.originalData.email;
            document.getElementById('formPhone').value = this.originalData.phone;
        },
        
        showPasswordModal: function() {
            var self = this;
            var modalHtml = `
                <div class="modal-overlay" id="passwordModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
                    <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 400px; width: 100%; box-shadow: 0 20px 40px rgba(0,0,0,0.2);">
                        <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                            <h3 style="margin: 0; font-size: 16px;"><i class="ri-lock-password-line"></i> 修改密码</h3>
                            <button class="modal-close" onclick="closePasswordModal()" style="background: none; border: none; font-size: 20px; cursor: pointer; color: var(--nx-text-secondary);">
                                <i class="ri-close-line"></i>
                            </button>
                        </div>
                        <div class="modal-body" style="padding: 20px;">
                            <div class="nx-form-group nx-mb-3">
                                <label class="nx-form-label">当前密码</label>
                                <input type="password" class="nx-input" id="currentPassword" placeholder="请输入当前密码">
                            </div>
                            <div class="nx-form-group nx-mb-3">
                                <label class="nx-form-label">新密码</label>
                                <input type="password" class="nx-input" id="newPassword" placeholder="请输入新密码">
                            </div>
                            <div class="nx-form-group">
                                <label class="nx-form-label">确认新密码</label>
                                <input type="password" class="nx-input" id="confirmPassword" placeholder="请再次输入新密码">
                            </div>
                        </div>
                        <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: flex-end; gap: 8px;">
                            <button class="nx-btn nx-btn--secondary" onclick="closePasswordModal()">取消</button>
                            <button class="nx-btn nx-btn--primary" onclick="submitPasswordChange()">确认修改</button>
                        </div>
                    </div>
                </div>
            `;
            
            var existingModal = document.getElementById('passwordModal');
            if (existingModal) existingModal.remove();
            
            document.body.insertAdjacentHTML('beforeend', modalHtml);
        },
        
        closePasswordModal: function() {
            var modal = document.getElementById('passwordModal');
            if (modal) modal.remove();
        },
        
        submitPasswordChange: function() {
            var self = this;
            var currentPassword = document.getElementById('currentPassword').value;
            var newPassword = document.getElementById('newPassword').value;
            var confirmPassword = document.getElementById('confirmPassword').value;
            
            if (!currentPassword || !newPassword || !confirmPassword) {
                this.showToast('请填写所有密码字段', 'error');
                return;
            }
            
            if (newPassword !== confirmPassword) {
                this.showToast('两次输入的新密码不一致', 'error');
                return;
            }
            
            if (newPassword.length < 6) {
                this.showToast('新密码长度至少6位', 'error');
                return;
            }
            
            fetch('/api/v1/users/password', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    currentPassword: currentPassword,
                    newPassword: newPassword
                })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.closePasswordModal();
                        self.showToast('密码修改成功', 'success');
                    } else {
                        self.showToast(result.message || '密码修改失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('Failed to change password:', error);
                    self.showToast('密码修改失败', 'error');
                });
        },
        
        showDevicesModal: function() {
            var self = this;
            
            fetch('/api/v1/users/devices')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    var devices = [];
                    if (result.status === 'success' && result.data) {
                        devices = result.data;
                    }
                    self.renderDevicesModal(devices);
                })
                .catch(function(error) {
                    console.error('Failed to load devices:', error);
                    self.renderDevicesModal([]);
                });
        },
        
        renderDevicesModal: function(devices) {
            var self = this;
            var devicesHtml = devices.length > 0 ? devices.map(function(device) {
                return `
                    <div class="device-item" style="display: flex; align-items: center; justify-content: space-between; padding: 12px; border: 1px solid var(--nx-border-color); border-radius: 8px; margin-bottom: 8px;">
                        <div style="display: flex; align-items: center; gap: 12px;">
                            <i class="ri-${device.deviceType === 'mobile' ? 'smartphone' : 'computer'}-line" style="font-size: 24px; color: var(--nx-color-primary);"></i>
                            <div>
                                <div style="font-weight: 500;">${device.deviceName || '未知设备'}</div>
                                <div style="font-size: 12px; color: var(--nx-text-secondary);">
                                    ${device.ip || '-'} · ${device.lastLoginTime ? self.formatTime(device.lastLoginTime) : '-'}
                                </div>
                            </div>
                        </div>
                        <div style="display: flex; align-items: center; gap: 8px;">
                            ${device.isCurrent ? '<span class="nx-badge nx-badge--success">当前设备</span>' : ''}
                            <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="removeDevice('${device.deviceId}')" ${device.isCurrent ? 'disabled' : ''}>
                                <i class="ri-delete-bin-line"></i>
                            </button>
                        </div>
                    </div>
                `;
            }).join('') : '<p style="text-align: center; color: var(--nx-text-secondary); padding: 20px;">暂无登录设备记录</p>';
            
            var modalHtml = `
                <div class="modal-overlay" id="devicesModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
                    <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 500px; width: 100%; box-shadow: 0 20px 40px rgba(0,0,0,0.2);">
                        <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                            <h3 style="margin: 0; font-size: 16px;"><i class="ri-device-line"></i> 登录设备管理</h3>
                            <button class="modal-close" onclick="closeDevicesModal()" style="background: none; border: none; font-size: 20px; cursor: pointer; color: var(--nx-text-secondary);">
                                <i class="ri-close-line"></i>
                            </button>
                        </div>
                        <div class="modal-body" style="padding: 20px; max-height: 400px; overflow-y: auto;">
                            ${devicesHtml}
                        </div>
                        <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: space-between;">
                            <button class="nx-btn nx-btn--danger" onclick="removeAllDevices()">移除其他所有设备</button>
                            <button class="nx-btn nx-btn--secondary" onclick="closeDevicesModal()">关闭</button>
                        </div>
                    </div>
                </div>
            `;
            
            var existingModal = document.getElementById('devicesModal');
            if (existingModal) existingModal.remove();
            
            document.body.insertAdjacentHTML('beforeend', modalHtml);
        },
        
        closeDevicesModal: function() {
            var modal = document.getElementById('devicesModal');
            if (modal) modal.remove();
        },
        
        removeDevice: function(deviceId) {
            var self = this;
            if (!confirm('确定要移除此设备吗？')) return;
            
            fetch('/api/v1/users/devices/' + deviceId, {
                method: 'DELETE'
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.showToast('设备已移除', 'success');
                        self.showDevicesModal();
                    } else {
                        self.showToast(result.message || '移除失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('Failed to remove device:', error);
                    self.showToast('移除失败', 'error');
                });
        },
        
        removeAllDevices: function() {
            var self = this;
            if (!confirm('确定要移除其他所有登录设备吗？')) return;
            
            fetch('/api/v1/users/devices/others', {
                method: 'DELETE'
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.showToast('已移除其他所有设备', 'success');
                        self.showDevicesModal();
                    } else {
                        self.showToast(result.message || '移除失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('Failed to remove devices:', error);
                    self.showToast('移除失败', 'error');
                });
        },
        
        showToast: function(message, type) {
            var toast = document.getElementById('toast');
            toast.textContent = message;
            toast.className = 'profile-toast ' + type;
            toast.classList.add('show');
            
            setTimeout(function() {
                toast.classList.remove('show');
            }, 3000);
        }
    };

    window.MyProfile = MyProfile;
    window.resetForm = function() { MyProfile.resetForm(); };
    window.showPasswordModal = function() { MyProfile.showPasswordModal(); };
    window.showDevicesModal = function() { MyProfile.showDevicesModal(); };
    window.closePasswordModal = function() { MyProfile.closePasswordModal(); };
    window.submitPasswordChange = function() { MyProfile.submitPasswordChange(); };
    window.closeDevicesModal = function() { MyProfile.closeDevicesModal(); };
    window.removeDevice = function(deviceId) { MyProfile.removeDevice(deviceId); };
    window.removeAllDevices = function() { MyProfile.removeAllDevices(); };
    
    document.addEventListener('DOMContentLoaded', function() {
        MyProfile.init();
    });
})();
