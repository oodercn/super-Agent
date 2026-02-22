(function(global) {
    'use strict';

    var SecurityCenter = {
        currentKeypair: null,

        init: function() {
            this.bindTabEvents();
            window.onPageInit = function() {
                console.log('安全中心页面初始化完成');
            };
        },

        bindTabEvents: function() {
            var tabs = document.querySelectorAll('.security-tab');
            tabs.forEach(function(tab) {
                tab.addEventListener('click', function() {
                    var tabId = this.getAttribute('data-tab');
                    SecurityCenter.switchTab(tabId);
                });
            });
        },

        switchTab: function(tabId) {
            var tabs = document.querySelectorAll('.security-tab');
            var panels = document.querySelectorAll('.security-panel');
            
            tabs.forEach(function(tab) {
                tab.classList.remove('active');
                if (tab.getAttribute('data-tab') === tabId) {
                    tab.classList.add('active');
                }
            });
            
            panels.forEach(function(panel) {
                panel.classList.remove('active');
                if (panel.id === tabId + '-panel') {
                    panel.classList.add('active');
                }
            });
        },

        generateKeyPair: function() {
            fetch('/api/security/keypair/generate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        SecurityCenter.currentKeypair = result.data;
                        document.getElementById('public-key-display').textContent = result.data.publicKey;
                        document.getElementById('private-key-display').textContent = result.data.privateKey;
                        document.getElementById('keypair-result').style.display = 'block';
                        SecurityCenter.showSuccess('密钥对生成成功');
                    } else {
                        SecurityCenter.showError(result.message || '密钥对生成失败');
                    }
                })
                .catch(function(error) {
                    console.error('生成密钥对错误:', error);
                    SecurityCenter.showError('密钥对生成失败');
                });
        },

        copyKey: function(type) {
            var key = type === 'public' ? 
                document.getElementById('public-key-display').textContent :
                document.getElementById('private-key-display').textContent;
            
            navigator.clipboard.writeText(key).then(function() {
                SecurityCenter.showSuccess('已复制到剪贴板');
            }).catch(function() {
                SecurityCenter.showError('复制失败');
            });
        },

        encryptData: function() {
            var data = document.getElementById('encrypt-data').value;
            var publicKey = document.getElementById('encrypt-public-key').value;
            
            if (!data || !publicKey) {
                SecurityCenter.showError('请填写数据和公钥');
                return;
            }
            
            fetch('/api/security/encrypt', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ data: data, publicKey: publicKey })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('encrypted-data').textContent = result.data;
                        document.getElementById('encrypt-result').style.display = 'block';
                        SecurityCenter.showSuccess('加密成功');
                    } else {
                        SecurityCenter.showError(result.message || '加密失败');
                    }
                })
                .catch(function(error) {
                    console.error('加密错误:', error);
                    SecurityCenter.showError('加密失败');
                });
        },

        decryptData: function() {
            var encryptedData = document.getElementById('decrypt-data').value;
            var privateKey = document.getElementById('decrypt-private-key').value;
            
            if (!encryptedData || !privateKey) {
                SecurityCenter.showError('请填写加密数据和私钥');
                return;
            }
            
            fetch('/api/security/decrypt', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ encryptedData: encryptedData, privateKey: privateKey })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('decrypted-data').textContent = result.data;
                        document.getElementById('decrypt-result').style.display = 'block';
                        SecurityCenter.showSuccess('解密成功');
                    } else {
                        SecurityCenter.showError(result.message || '解密失败');
                    }
                })
                .catch(function(error) {
                    console.error('解密错误:', error);
                    SecurityCenter.showError('解密失败');
                });
        },

        signData: function() {
            var data = document.getElementById('sign-data').value;
            var privateKey = document.getElementById('sign-private-key').value;
            
            if (!data || !privateKey) {
                SecurityCenter.showError('请填写数据和私钥');
                return;
            }
            
            fetch('/api/security/sign', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ data: data, privateKey: privateKey })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('signature-data').textContent = result.data;
                        document.getElementById('sign-result').style.display = 'block';
                        SecurityCenter.showSuccess('签名成功');
                    } else {
                        SecurityCenter.showError(result.message || '签名失败');
                    }
                })
                .catch(function(error) {
                    console.error('签名错误:', error);
                    SecurityCenter.showError('签名失败');
                });
        },

        verifySignature: function() {
            var data = document.getElementById('verify-data').value;
            var signature = document.getElementById('verify-signature').value;
            var publicKey = document.getElementById('verify-public-key').value;
            
            if (!data || !signature || !publicKey) {
                SecurityCenter.showError('请填写数据、签名和公钥');
                return;
            }
            
            fetch('/api/security/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ data: data, signature: signature, publicKey: publicKey })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        var valid = result.data;
                        var statusHtml = valid ? 
                            '<span class="status-badge success"><i class="ri-check-line"></i> 签名有效</span>' :
                            '<span class="status-badge error"><i class="ri-close-line"></i> 签名无效</span>';
                        document.getElementById('verify-status').innerHTML = statusHtml;
                        document.getElementById('verify-result').style.display = 'block';
                    } else {
                        SecurityCenter.showError(result.message || '验证失败');
                    }
                })
                .catch(function(error) {
                    console.error('验证错误:', error);
                    SecurityCenter.showError('验证失败');
                });
        },

        generateToken: function() {
            var subject = document.getElementById('token-subject').value;
            var expireMs = parseInt(document.getElementById('token-expire').value);
            
            if (!subject) {
                SecurityCenter.showError('请填写主题');
                return;
            }
            
            fetch('/api/security/token/generate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ subject: subject, expireMs: expireMs })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('generated-token').textContent = result.data;
                        document.getElementById('token-result').style.display = 'block';
                        SecurityCenter.showSuccess('Token 生成成功');
                    } else {
                        SecurityCenter.showError(result.message || 'Token 生成失败');
                    }
                })
                .catch(function(error) {
                    console.error('生成 Token 错误:', error);
                    SecurityCenter.showError('Token 生成失败');
                });
        },

        copyToken: function() {
            var token = document.getElementById('generated-token').textContent;
            navigator.clipboard.writeText(token).then(function() {
                SecurityCenter.showSuccess('已复制到剪贴板');
            }).catch(function() {
                SecurityCenter.showError('复制失败');
            });
        },

        validateToken: function() {
            var token = document.getElementById('validate-token').value;
            
            if (!token) {
                SecurityCenter.showError('请填写 Token');
                return;
            }
            
            fetch('/api/security/token/validate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ token: token })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        var data = result.data;
                        var html = '';
                        if (data.valid) {
                            html = '<span class="status-badge success"><i class="ri-check-line"></i> Token 有效</span>';
                            html += '<div style="margin-top: 12px;">';
                            html += '<p><strong>主题:</strong> ' + (data.subject || '-') + '</p>';
                            html += '<p><strong>签发时间:</strong> ' + (data.issuedAt ? new Date(data.issuedAt).toLocaleString() : '-') + '</p>';
                            html += '<p><strong>过期时间:</strong> ' + (data.expiresAt ? new Date(data.expiresAt).toLocaleString() : '-') + '</p>';
                            html += '</div>';
                        } else {
                            html = '<span class="status-badge error"><i class="ri-close-line"></i> Token 无效或已过期</span>';
                        }
                        document.getElementById('token-info').innerHTML = html;
                        document.getElementById('validate-result').style.display = 'block';
                    } else {
                        SecurityCenter.showError(result.message || '验证失败');
                    }
                })
                .catch(function(error) {
                    console.error('验证 Token 错误:', error);
                    SecurityCenter.showError('验证失败');
                });
        },

        generateSceneKey: function() {
            var sceneId = document.getElementById('scene-id').value;
            
            if (!sceneId) {
                SecurityCenter.showError('请填写场景 ID');
                return;
            }
            
            fetch('/api/security/scene/key/generate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sceneId: sceneId })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('scene-public-key').textContent = result.data.publicKey;
                        document.getElementById('scene-result').style.display = 'block';
                        SecurityCenter.showSuccess('场景密钥生成成功');
                    } else {
                        SecurityCenter.showError(result.message || '场景密钥生成失败');
                    }
                })
                .catch(function(error) {
                    console.error('生成场景密钥错误:', error);
                    SecurityCenter.showError('场景密钥生成失败');
                });
        },

        rotateSceneKey: function() {
            var sceneId = document.getElementById('scene-id').value;
            
            if (!sceneId) {
                SecurityCenter.showError('请填写场景 ID');
                return;
            }
            
            fetch('/api/security/scene/key/rotate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sceneId: sceneId })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        SecurityCenter.showSuccess('场景密钥轮换成功');
                    } else {
                        SecurityCenter.showError(result.message || '场景密钥轮换失败');
                    }
                })
                .catch(function(error) {
                    console.error('轮换场景密钥错误:', error);
                    SecurityCenter.showError('场景密钥轮换失败');
                });
        },

        encryptForPeer: function() {
            var sceneId = document.getElementById('peer-scene-id').value;
            var peerId = document.getElementById('peer-id').value;
            var data = document.getElementById('peer-data').value;
            
            if (!sceneId || !peerId || !data) {
                SecurityCenter.showError('请填写场景 ID、对等端 ID 和数据');
                return;
            }
            
            fetch('/api/security/peer/encrypt', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sceneId: sceneId, peerId: peerId, data: data })
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        document.getElementById('peer-encrypted-data').textContent = result.data;
                        document.getElementById('peer-encrypt-result').style.display = 'block';
                        SecurityCenter.showSuccess('加密成功');
                    } else {
                        SecurityCenter.showError(result.message || '加密失败');
                    }
                })
                .catch(function(error) {
                    console.error('对等端加密错误:', error);
                    SecurityCenter.showError('加密失败');
                });
        },

        checkHealth: function() {
            fetch('/api/security/health', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.requestStatus === 200) {
                        var healthy = result.data && result.data.healthy;
                        if (healthy) {
                            SecurityCenter.showSuccess('安全服务运行正常');
                        } else {
                            SecurityCenter.showError('安全服务异常');
                        }
                    } else {
                        SecurityCenter.showError('健康检查失败');
                    }
                })
                .catch(function(error) {
                    console.error('健康检查错误:', error);
                    SecurityCenter.showError('健康检查失败');
                });
        },

        showSuccess: function(message) {
            alert(message);
        },

        showError: function(message) {
            alert(message);
        }
    };

    SecurityCenter.init();

    global.generateKeyPair = SecurityCenter.generateKeyPair;
    global.copyKey = SecurityCenter.copyKey;
    global.encryptData = SecurityCenter.encryptData;
    global.decryptData = SecurityCenter.decryptData;
    global.signData = SecurityCenter.signData;
    global.verifySignature = SecurityCenter.verifySignature;
    global.generateToken = SecurityCenter.generateToken;
    global.copyToken = SecurityCenter.copyToken;
    global.validateToken = SecurityCenter.validateToken;
    global.generateSceneKey = SecurityCenter.generateSceneKey;
    global.rotateSceneKey = SecurityCenter.rotateSceneKey;
    global.encryptForPeer = SecurityCenter.encryptForPeer;
    global.checkHealth = SecurityCenter.checkHealth;
    global.SecurityCenter = SecurityCenter;

})(typeof window !== 'undefined' ? window : this);
