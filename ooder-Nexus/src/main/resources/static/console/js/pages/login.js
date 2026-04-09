(function() {
    'use strict';
    
    var Login = {
        rolePages: {
            'installer': '/console/pages/role-installer.html',
            'admin': '/console/pages/role-admin.html',
            'leader': '/console/pages/role-leader.html',
            'collaborator': '/console/pages/role-collaborator.html'
        },
        
        currentPlatform: 'dingding',
        qrcodeTimer: null,
        checkTimer: null,
        
        init: function() {
            var form = document.getElementById('loginForm');
            if (form) {
                form.addEventListener('submit', this.handleLogin.bind(this));
            }
        },
        
        handleLogin: function(e) {
            e.preventDefault();
            
            var username = document.getElementById('username').value;
            var password = document.getElementById('password').value;
            var role = document.getElementById('role').value;
            var loginBtn = document.getElementById('loginBtn');
            var loginError = document.getElementById('loginError');
            
            loginBtn.disabled = true;
            loginBtn.textContent = '登录中...';
            loginError.classList.remove('show');
            
            fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ username: username, password: password, role: role })
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    localStorage.setItem('token', result.data.token);
                    localStorage.setItem('user', JSON.stringify(result.data.user || result.data));
                    
                    var targetPage = Login.rolePages[role] || '/console/pages/role-installer.html';
                    window.location.href = targetPage;
                } else {
                    loginError.textContent = result.message || '登录失败，请检查用户名和密码';
                    loginError.classList.add('show');
                }
            })
            .catch(function(e) {
                console.error('Login error:', e);
                loginError.textContent = '登录请求失败，请稍后重试';
                loginError.classList.add('show');
            })
            .finally(function() {
                loginBtn.disabled = false;
                loginBtn.textContent = '登录';
            });
        },
        
        quickLogin: function(username, password, role) {
            document.getElementById('username').value = username;
            document.getElementById('password').value = password;
            document.getElementById('role').value = role;
            document.getElementById('loginForm').dispatchEvent(new Event('submit'));
        },
        
        switchLoginTab: function(tab) {
            var tabs = document.querySelectorAll('.login-tab');
            tabs.forEach(function(t) {
                t.classList.remove('active');
            });
            document.querySelector('[data-tab="' + tab + '"]').classList.add('active');
            
            var loginForm = document.getElementById('loginForm');
            var qrcodeLogin = document.getElementById('qrcodeLogin');
            
            if (tab === 'password') {
                loginForm.style.display = 'block';
                qrcodeLogin.style.display = 'none';
                Login.stopQrcodeCheck();
            } else if (tab === 'qrcode') {
                loginForm.style.display = 'none';
                qrcodeLogin.style.display = 'flex';
                Login.loadQrcode();
            }
        },
        
        switchPlatform: function(platform) {
            Login.currentPlatform = platform;
            
            var platforms = document.querySelectorAll('.qrcode-platform');
            platforms.forEach(function(p) {
                p.classList.remove('active');
            });
            document.querySelector('[data-platform="' + platform + '"]').classList.add('active');
            
            var platformNames = {
                'dingding': '钉钉',
                'weixin': '微信',
                'feishu': '飞书'
            };
            document.getElementById('platformName').textContent = platformNames[platform] || platform;
            
            Login.loadQrcode();
        },
        
        loadQrcode: function() {
            Login.stopQrcodeCheck();
            
            var container = document.getElementById('qrcodeContainer');
            container.innerHTML = '<div class="qrcode-placeholder"><i class="ri-qr-code-line"></i><p>正在加载二维码...</p></div>';
            
            fetch('/api/v1/auth/qrcode/' + Login.currentPlatform, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    Login.showQrcode(result.data);
                    Login.startQrcodeCheck(result.data.qrcodeId);
                } else {
                    container.innerHTML = '<div class="qrcode-placeholder"><i class="ri-error-warning-line"></i><p>' + (result.message || '获取二维码失败') + '</p></div>';
                }
            })
            .catch(function(e) {
                console.error('Load qrcode error:', e);
                container.innerHTML = '<div class="qrcode-placeholder"><i class="ri-error-warning-line"></i><p>获取二维码失败，请稍后重试</p></div>';
            });
        },
        
        showQrcode: function(data) {
            var container = document.getElementById('qrcodeContainer');
            var qrcodeHtml = '';
            
            if (data.qrcodeUrl) {
                qrcodeHtml = '<img src="' + data.qrcodeUrl + '" class="qrcode-image" alt="登录二维码">';
            } else if (data.qrcodeData) {
                qrcodeHtml = '<div class="qrcode-placeholder"><i class="ri-qr-code-line"></i><p>' + data.qrcodeData + '</p></div>';
            }
            
            if (data.expiresIn) {
                Login.qrcodeTimer = setTimeout(function() {
                    Login.showExpired();
                }, data.expiresIn * 1000);
            }
            
            container.innerHTML = qrcodeHtml;
        },
        
        showExpired: function() {
            Login.stopQrcodeCheck();
            
            var container = document.getElementById('qrcodeContainer');
            var expiredHtml = '<div class="qrcode-expired">' +
                '<i class="ri-refresh-line"></i>' +
                '<p>二维码已过期</p>' +
                '<button class="refresh-btn" onclick="Login.loadQrcode()">刷新二维码</button>' +
                '</div>';
            
            var qrcodeImage = container.querySelector('.qrcode-image');
            if (qrcodeImage) {
                container.innerHTML = expiredHtml + qrcodeImage.outerHTML;
            } else {
                container.innerHTML = expiredHtml;
            }
        },
        
        startQrcodeCheck: function(qrcodeId) {
            Login.checkTimer = setInterval(function() {
                fetch('/api/v1/mvp-auth/qrcode/check/' + qrcodeId, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                })
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        if (result.data.status === 'scanned') {
                            document.getElementById('qrcodeStatus').innerHTML = 
                                '<p class="qrcode-tip">扫码成功，请在手机上确认登录</p>';
                        } else if (result.data.status === 'confirmed') {
                            Login.stopQrcodeCheck();
                            localStorage.setItem('token', result.data.token);
                            localStorage.setItem('user', JSON.stringify(result.data.user));
                            
                            var role = result.data.role || 'admin';
                            var targetPage = Login.rolePages[role] || '/console/pages/role-admin.html';
                            window.location.href = targetPage;
                        } else if (result.data.status === 'expired') {
                            Login.showExpired();
                        }
                    }
                })
                .catch(function(e) {
                    console.error('Check qrcode error:', e);
                });
            }, 2000);
        },
        
        stopQrcodeCheck: function() {
            if (Login.qrcodeTimer) {
                clearTimeout(Login.qrcodeTimer);
                Login.qrcodeTimer = null;
            }
            if (Login.checkTimer) {
                clearInterval(Login.checkTimer);
                Login.checkTimer = null;
            }
        }
    };
    
    window.quickLogin = Login.quickLogin.bind(Login);
    window.switchLoginTab = Login.switchLoginTab.bind(Login);
    window.switchPlatform = Login.switchPlatform.bind(Login);
    window.Login = Login;
    
    document.addEventListener('DOMContentLoaded', Login.init.bind(Login));
})();
