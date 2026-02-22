(function(global) {
    'use strict';

    var SkillAuthentication = {
        init: function() {
            window.onPageInit = function() {
                SkillAuthentication.loadSkills();
                SkillAuthentication.loadCertificates();
                SkillAuthentication.loadValidations();
                SkillAuthentication.loadSkillOptions();
            };
        },

        loadSkills: function() {
            var skillList = document.getElementById('skill-list');
            skillList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/certification/skills')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        SkillAuthentication.renderSkillList(result.data || []);
                    } else {
                        throw new Error('响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载技能列表错误:', error);
                    document.getElementById('skill-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillAuthentication.showError('加载技能列表失败');
                });
        },

        renderSkillList: function(skills) {
            var skillList = document.getElementById('skill-list');
            skillList.innerHTML = '';

            if (skills.length === 0) {
                skillList.innerHTML = '<div class="empty-state">暂无技能</div>';
                return;
            }

            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = 
                    '<div class="skill-info">' +
                        '<h3>' + skill.name + '</h3>' +
                        '<p>' + (skill.description || '暂无描述') + '</p>' +
                        '<span class="skill-category">' + SkillAuthentication.getCategoryName(skill.category) + '</span>' +
                        '<span class="skill-author">作者: ' + skill.author + '</span>' +
                        '<span class="skill-certification ' + (skill.certification ? 'success' : 'warning') + '">' + (skill.certification ? '已认证' : '未认证') + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                        '<button class="btn btn-primary" onclick="showCertifyModal()">' +
                            '<i class="ri-award-line"></i> 认证' +
                        '</button>' +
                    '</div>';
                skillList.appendChild(skillItem);
            });
        },

        loadCertificates: function() {
            var certificateList = document.getElementById('certificate-list');
            certificateList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/certification/certificates')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        SkillAuthentication.renderCertificateList(result.data || []);
                    } else {
                        throw new Error('响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载证书列表错误:', error);
                    document.getElementById('certificate-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillAuthentication.showError('加载证书列表失败');
                });
        },

        renderCertificateList: function(certificates) {
            var certificateList = document.getElementById('certificate-list');
            certificateList.innerHTML = '';

            if (certificates.length === 0) {
                certificateList.innerHTML = '<div class="empty-state">暂无证书</div>';
                return;
            }

            certificates.forEach(function(cert) {
                var certItem = document.createElement('div');
                certItem.className = 'certificate-item';
                certItem.innerHTML = 
                    '<div class="certificate-info">' +
                        '<h3>' + cert.skillName + '</h3>' +
                        '<p><strong>证书编号:</strong> ' + cert.certificateId + '</p>' +
                        '<p><strong>认证等级:</strong> ' + SkillAuthentication.getCertificationLevel(cert.level) + '</p>' +
                        '<p><strong>认证时间:</strong> ' + SkillAuthentication.formatTime(cert.certifiedAt) + '</p>' +
                        '<p><strong>有效期至:</strong> ' + SkillAuthentication.formatTime(cert.expiresAt) + '</p>' +
                    '</div>' +
                    '<div class="certificate-actions">' +
                        '<button class="btn btn-secondary" onclick="viewCertificate(\'' + cert.id + '\')">' +
                            '<i class="ri-eye-line"></i> 查看' +
                        '</button>' +
                        '<button class="btn btn-danger" onclick="revokeCertificate(\'' + cert.id + '\')">' +
                            '<i class="ri-close-line"></i> 撤销' +
                        '</button>' +
                    '</div>';
                certificateList.appendChild(certItem);
            });
        },

        loadValidations: function() {
            var validationList = document.getElementById('validation-list');
            validationList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/certification/validations')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        SkillAuthentication.renderValidationList(result.data || []);
                    } else {
                        throw new Error('响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载验证列表错误:', error);
                    document.getElementById('validation-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillAuthentication.showError('加载验证列表失败');
                });
        },

        renderValidationList: function(validations) {
            var validationList = document.getElementById('validation-list');
            validationList.innerHTML = '';

            if (validations.length === 0) {
                validationList.innerHTML = '<div class="empty-state">暂无验证记录</div>';
                return;
            }

            validations.forEach(function(validation) {
                var validationItem = document.createElement('div');
                validationItem.className = 'validation-item';
                var noteHtml = validation.note ? '<p><strong>说明:</strong> ' + validation.note + '</p>' : '';
                validationItem.innerHTML = 
                    '<div class="validation-info">' +
                        '<h3>' + validation.skillName + '</h3>' +
                        '<p><strong>证书编号:</strong> ' + validation.certificateId + '</p>' +
                        '<p><strong>验证时间:</strong> ' + SkillAuthentication.formatTime(validation.validatedAt) + '</p>' +
                        '<p><strong>验证结果:</strong> <span class="validation-status ' + (validation.valid ? 'success' : 'failed') + '">' + (validation.valid ? '有效' : '无效') + '</span></p>' +
                        noteHtml +
                    '</div>' +
                    '<div class="validation-actions">' +
                        '<button class="btn btn-secondary" onclick="viewValidation(\'' + validation.id + '\')">' +
                            '<i class="ri-eye-line"></i> 查看' +
                        '</button>' +
                    '</div>';
                validationList.appendChild(validationItem);
            });
        },

        loadSkillOptions: function() {
            fetch('/api/admin/skills')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        var skillSelect = document.getElementById('skill-select');
                        skillSelect.innerHTML = '<option value="">请选择技能</option>';
                        result.data.forEach(function(skill) {
                            var option = document.createElement('option');
                            option.value = skill.id;
                            option.textContent = skill.name;
                            skillSelect.appendChild(option);
                        });
                    }
                })
                .catch(function(error) {
                    console.error('加载技能列表错误:', error);
                });
        },

        getCategoryName: function(category) {
            var categoryMap = {
                'text-processing': '文本处理',
                'development': '开发工具',
                'deployment': '部署工具',
                'media': '媒体处理',
                'storage': '存储管理'
            };
            return categoryMap[category] || category;
        },

        getCertificationLevel: function(level) {
            var levelMap = {
                'basic': '基础',
                'intermediate': '中级',
                'advanced': '高级'
            };
            return levelMap[level] || level;
        },

        switchTab: function(tab) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('active');
            });
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.style.display = 'none';
            });

            event.target.classList.add('active');
            document.getElementById(tab + '-tab').style.display = 'block';
        },

        showCertifyModal: function() {
            document.getElementById('certify-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        certifySkill: function() {
            var skillId = document.getElementById('skill-select').value;
            var level = document.getElementById('certification-level').value;
            var note = document.getElementById('certification-note').value;

            if (!skillId) {
                SkillAuthentication.showError('请选择技能');
                return;
            }

            fetch('/api/admin/certification/certify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    skillId: skillId,
                    level: level,
                    note: note
                })
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        SkillAuthentication.closeModal('certify-modal');
                        SkillAuthentication.showSuccess('技能认证成功');
                        SkillAuthentication.loadSkills();
                        SkillAuthentication.loadCertificates();
                    } else {
                        SkillAuthentication.showError('技能认证失败');
                    }
                })
                .catch(function(error) {
                    console.error('认证技能错误:', error);
                    SkillAuthentication.showError('技能认证失败');
                });
        },

        viewCertificate: function(certId) {
            SkillAuthentication.showError('查看证书功能开发中...');
        },

        revokeCertificate: function(certId) {
            if (!confirm('确定要撤销此证书吗？')) {
                return;
            }

            fetch('/api/admin/certification/certificates/' + certId, {
                method: 'DELETE'
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        SkillAuthentication.showSuccess('证书撤销成功');
                        SkillAuthentication.loadCertificates();
                    } else {
                        SkillAuthentication.showError('证书撤销失败');
                    }
                })
                .catch(function(error) {
                    console.error('撤销证书错误:', error);
                    SkillAuthentication.showError('证书撤销失败');
                });
        },

        viewValidation: function(validationId) {
            SkillAuthentication.showError('查看验证功能开发中...');
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '未知';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) {
            SkillAuthentication.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            SkillAuthentication.showNotification(message, 'success');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() {
                notification.remove();
            }, 3000);
        }
    };

    SkillAuthentication.init();

    global.switchTab = SkillAuthentication.switchTab;
    global.showCertifyModal = SkillAuthentication.showCertifyModal;
    global.closeModal = SkillAuthentication.closeModal;
    global.certifySkill = SkillAuthentication.certifySkill;
    global.viewCertificate = SkillAuthentication.viewCertificate;
    global.revokeCertificate = SkillAuthentication.revokeCertificate;
    global.viewValidation = SkillAuthentication.viewValidation;

})(typeof window !== 'undefined' ? window : this);
