(function() {
    'use strict';
    
    var Login = {
        rolePages: {
            'installer': '/console/pages/role-installer.html',
            'admin': '/console/pages/role-admin.html',
            'leader': '/console/pages/role-leader.html',
            'collaborator': '/console/pages/role-collaborator.html'
        },
        
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
        }
    };
    
    window.quickLogin = Login.quickLogin.bind(Login);
    
    document.addEventListener('DOMContentLoaded', Login.init.bind(Login));
})();
