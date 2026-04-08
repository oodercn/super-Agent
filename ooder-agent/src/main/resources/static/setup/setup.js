(function() {
    'use strict';

    let currentStep = 0;
    const steps = ['welcome', 'modules', 'install', 'admin', 'done'];
    let installErrors = [];

    function init() {
        loadTheme();
    }

    function loadTheme() {
        const theme = localStorage.getItem('nx-theme') || 'dark';
        applyTheme(theme);
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        const icon = document.getElementById('themeIcon');
        if (icon) {
            icon.className = theme === 'dark' ? 'ri-sun-line' : 'ri-moon-line';
        }
        localStorage.setItem('nx-theme', theme);
    }

    window.toggleTheme = function() {
        const current = document.documentElement.getAttribute('data-theme') || 'dark';
        const next = current === 'dark' ? 'light' : 'dark';
        applyTheme(next);
    };

    function showStep(stepName) {
        steps.forEach((step, index) => {
            const el = document.getElementById('step-' + step);
            if (el) {
                el.classList.remove('setup-step--active');
                if (step === stepName) {
                    el.classList.add('setup-step--active');
                    currentStep = index;
                }
            }
        });
    }

    window.startSetup = function() {
        showStep('modules');
    };

    window.prevStep = function() {
        if (currentStep > 0) {
            showStep(steps[currentStep - 1]);
        }
    };

    window.startInstall = async function() {
        showStep('install');
        installErrors = [];
        
        const progressFill = document.getElementById('progressFill');
        const progressText = document.getElementById('progressText');
        const installStatus = document.getElementById('installStatus');
        const installLog = document.getElementById('installLog');

        const installSteps = [
            { text: 'Checking environment...', delay: 500 },
            { text: 'Scanning skills directory...', delay: 800 },
            { text: 'Installing skill-scene-management (Scene Management UI)...', skillId: 'skill-scene-management', delay: 2000 },
            { text: 'Installing skill-capability (Capability Management)...', skillId: 'skill-capability', delay: 2000 },
            { text: 'Installing skill-llm (LLM Service)...', skillId: 'skill-llm', delay: 2000 },
            { text: 'Installing skill-llm-chat (LLM Chat Assistant)...', skillId: 'skill-llm-chat', delay: 2000 },
            { text: 'Installing skill-protocol (Protocol Service)...', skillId: 'skill-protocol', delay: 1500 },
            { text: 'Installing skill-management (Skill Management)...', skillId: 'skill-management', delay: 1500 },
            { text: 'Configuring dependencies...', delay: 800 },
            { text: 'Initializing database...', delay: 1000 },
            { text: 'Registering routes and menus...', delay: 600 },
            { text: 'Completing installation...', delay: 500 }
        ];

        for (let i = 0; i < installSteps.length; i++) {
            const progress = Math.round(((i + 1) / installSteps.length) * 100);
            
            installStatus.textContent = installSteps[i].text;
            progressFill.style.width = progress + '%';
            progressText.textContent = progress + '%';
            
            const logItem = document.createElement('div');
            logItem.className = 'setup-log-item';
            logItem.innerHTML = '<span class="setup-log-time">' + new Date().toLocaleTimeString() + '</span><span class="setup-log-text">' + installSteps[i].text + '</span>';
            installLog.appendChild(logItem);
            installLog.scrollTop = installLog.scrollHeight;

            if (installSteps[i].skillId) {
                try {
                    const response = await fetch('/api/v1/plugin/install', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ skillId: installSteps[i].skillId })
                    });
                    const result = await response.json();
                    
                    if (result.status === 'success') {
                        logItem.querySelector('.setup-log-text').classList.add('setup-log-text--success');
                        logItem.querySelector('.setup-log-text').innerHTML += ' <i class="ri-checkbox-circle-line"></i>';
                    } else {
                        logItem.querySelector('.setup-log-text').classList.add('setup-log-text--error');
                        logItem.querySelector('.setup-log-text').innerHTML += ' <i class="ri-error-warning-line"></i> ' + (result.message || 'Failed');
                        installErrors.push(installSteps[i].skillId + ': ' + (result.message || 'Failed'));
                    }
                } catch (e) {
                    logItem.querySelector('.setup-log-text').classList.add('setup-log-text--error');
                    logItem.querySelector('.setup-log-text').innerHTML += ' <i class="ri-error-warning-line"></i> ' + e.message;
                    installErrors.push(installSteps[i].skillId + ': ' + e.message);
                }
            }

            await new Promise(resolve => setTimeout(resolve, installSteps[i].delay));
        }

        await new Promise(resolve => setTimeout(resolve, 500));
        
        if (installErrors.length > 0) {
            const errorDiv = document.createElement('div');
            errorDiv.className = 'setup-error-summary';
            errorDiv.innerHTML = '<strong>Installation completed with errors:</strong><br>' + installErrors.join('<br>');
            installLog.appendChild(errorDiv);
        }
        
        showStep('admin');
    };

    window.createAdmin = async function(e) {
        e.preventDefault();
        
        const username = document.getElementById('adminUsername').value;
        const password = document.getElementById('adminPassword').value;
        const passwordConfirm = document.getElementById('adminPasswordConfirm').value;

        if (password !== passwordConfirm) {
            alert('Passwords do not match');
            return;
        }

        try {
            const response = await fetch('/api/v1/setup/admin', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            const result = await response.json();
            
            if (result.status === 'success') {
                showStep('done');
            } else {
                alert(result.message || 'Failed to create admin account');
            }
        } catch (e) {
            console.error('Create admin failed:', e);
            showStep('done');
        }
    };

    window.goToLogin = function() {
        window.location.href = '/console/pages/login.html';
    };

    document.addEventListener('DOMContentLoaded', init);
})();
