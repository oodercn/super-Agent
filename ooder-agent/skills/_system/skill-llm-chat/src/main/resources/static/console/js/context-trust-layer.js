(function(global) {
    'use strict';

    var ContextTrustLayer = {
        sessionId: null,
        userId: null,
        currentModule: null,
        pageState: {},
        lastUpdate: null,
        syncHandlers: [],
        pendingConfirm: null,

        init: function(options) {
            options = options || {};
            this.sessionId = options.sessionId || this.generateSessionId();
            this.userId = options.userId || this.getCurrentUserId();
            
            console.log('[ContextTrustLayer] Initialized with sessionId:', this.sessionId);
            
            this.setupEventListeners();
            this.startHeartbeat();
        },

        generateSessionId: function() {
            return 'sess-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
        },

        getCurrentUserId: function() {
            if (typeof getCurrentUserId === 'function') {
                return getCurrentUserId();
            }
            return 'user-' + Date.now();
        },

        setCurrentModule: function(module) {
            this.currentModule = module;
            this.updatePageState({ currentModule: module });
            
            if (typeof ModuleApiRegistry !== 'undefined') {
                ModuleApiRegistry.setCurrentModule(module);
            }
            
            console.log('[ContextTrustLayer] Current module set to:', module);
        },

        updatePageState: function(state) {
            var self = this;
            Object.keys(state).forEach(function(key) {
                self.pageState[key] = state[key];
            });
            this.lastUpdate = Date.now();
            
            this.pushToTrustLayer();
        },

        getPageState: function() {
            return Object.assign({}, this.pageState);
        },

        pushToTrustLayer: function() {
            var context = this.buildContext();
            
            fetch('/api/v1/llm/context/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(context)
            }).catch(function(err) {
                console.warn('[ContextTrustLayer] Failed to push context:', err);
            });
        },

        buildContext: function() {
            return {
                sessionId: this.sessionId,
                userId: this.userId,
                currentModule: this.currentModule,
                pageState: this.sanitizePageState(this.pageState),
                lastUpdate: this.lastUpdate,
                timestamp: Date.now()
            };
        },

        sanitizePageState: function(state) {
            var sanitized = Object.assign({}, state);
            var sensitiveKeys = ['password', 'token', 'secret', 'key', 'credential'];
            
            var self = this;
            Object.keys(sanitized).forEach(function(key) {
                if (sensitiveKeys.some(function(sk) { return key.toLowerCase().includes(sk); })) {
                    sanitized[key] = '***REDACTED***';
                }
            });
            
            return sanitized;
        },

        handleLlmResponse: function(response) {
            var self = this;
            
            if (response.syncContext) {
                this.syncToFrontend(response.syncData);
            }
            
            if (response.script) {
                return this.executeScript(response.script, response);
            }
            
            if (response.action) {
                return this.executeAction(response.action, response);
            }
            
            if (response.needConfirm) {
                this.showConfirmDialog(response.confirmMessage, response.pendingAction);
                return Promise.resolve({ needConfirm: true });
            }
            
            return Promise.resolve(response);
        },

        executeScript: function(script, response) {
            var self = this;
            var request = {
                script: script.code || script,
                scriptType: script.type || 'mvel',
                module: script.module || this.currentModule,
                requireConfirm: response.requireConfirm || false,
                syncContext: response.syncContext || false
            };

            return fetch('/api/v1/llm/execute-script', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(request)
            })
            .then(function(res) { return res.json(); })
            .then(function(result) {
                if (result.needConfirm) {
                    self.showConfirmDialog(result.confirmMessage, result.pendingScript);
                    return { needConfirm: true };
                }
                
                if (result.success) {
                    self.onScriptExecuted(result);
                    return result;
                } else {
                    self.onScriptError(result.error);
                    return { error: result.error };
                }
            });
        },

        executeAction: function(action, response) {
            var self = this;
            var request = {
                action: action.name || action,
                module: action.module || this.currentModule,
                params: action.params || {},
                requireConfirm: response.requireConfirm || false,
                syncContext: response.syncContext || false
            };

            return fetch('/api/llm/execute', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(request)
            })
            .then(function(res) { return res.json(); })
            .then(function(result) {
                if (result.needConfirm) {
                    self.showConfirmDialog(result.confirmMessage, result.pendingAction);
                    return { needConfirm: true };
                }
                
                if (result.success) {
                    self.onActionExecuted(result);
                    return result;
                } else {
                    self.onActionError(result.error);
                    return { error: result.error };
                }
            });
        },

        confirmAction: function(pendingAction) {
            var self = this;
            
            return fetch('/api/v1/llm/confirm', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(pendingAction)
            })
            .then(function(res) { return res.json(); })
            .then(function(result) {
                self.hideConfirmDialog();
                
                if (result.success) {
                    self.onActionExecuted(result);
                } else {
                    self.onActionError(result.error);
                }
                
                return result;
            });
        },

        cancelConfirm: function() {
            this.hideConfirmDialog();
            this.pendingConfirm = null;
        },

        showConfirmDialog: function(message, pendingAction) {
            this.pendingConfirm = pendingAction;
            
            var event = new CustomEvent('llm:confirmRequired', {
                detail: { message: message, pendingAction: pendingAction }
            });
            document.dispatchEvent(event);
            
            console.log('[ContextTrustLayer] Confirm required:', message);
        },

        hideConfirmDialog: function() {
            var event = new CustomEvent('llm:confirmResolved');
            document.dispatchEvent(event);
        },

        syncToFrontend: function(syncData) {
            var self = this;
            
            if (syncData && syncData.pageState) {
                Object.keys(syncData.pageState).forEach(function(key) {
                    self.pageState[key] = syncData.pageState[key];
                });
            }
            
            this.notifySync(syncData);
        },

        onSync: function(callback) {
            this.syncHandlers.push(callback);
        },

        notifySync: function(syncData) {
            this.syncHandlers.forEach(function(callback) {
                try {
                    callback(syncData);
                } catch (e) {
                    console.error('[ContextTrustLayer] Sync handler error:', e);
                }
            });
            
            var event = new CustomEvent('llm:contextSync', { detail: syncData });
            document.dispatchEvent(event);
        },

        onScriptExecuted: function(result) {
            console.log('[ContextTrustLayer] Script executed:', result);
            
            var event = new CustomEvent('llm:scriptExecuted', { detail: result });
            document.dispatchEvent(event);
        },

        onScriptError: function(error) {
            console.error('[ContextTrustLayer] Script error:', error);
            
            var event = new CustomEvent('llm:scriptError', { detail: { error: error } });
            document.dispatchEvent(event);
        },

        onActionExecuted: function(result) {
            console.log('[ContextTrustLayer] Action executed:', result);
            
            var event = new CustomEvent('llm:actionExecuted', { detail: result });
            document.dispatchEvent(event);
        },

        onActionError: function(error) {
            console.error('[ContextTrustLayer] Action error:', error);
            
            var event = new CustomEvent('llm:actionError', { detail: { error: error } });
            document.dispatchEvent(event);
        },

        setupEventListeners: function() {
            var self = this;
            
            document.addEventListener('llm:response', function(event) {
                self.handleLlmResponse(event.detail);
            });
            
            document.addEventListener('llm:confirmAccepted', function(event) {
                if (self.pendingConfirm) {
                    self.confirmAction(self.pendingConfirm);
                }
            });
            
            document.addEventListener('llm:confirmRejected', function(event) {
                self.cancelConfirm();
            });
        },

        startHeartbeat: function() {
            var self = this;
            
            setInterval(function() {
                self.pushToTrustLayer();
            }, 30000);
        },

        getAvailableApis: function() {
            if (typeof ModuleApiRegistry !== 'undefined') {
                return ModuleApiRegistry.getApiNames(this.currentModule);
            }
            return [];
        },

        generateScript: function(intent, context) {
            var request = {
                intent: intent,
                context: context || this.pageState
            };

            return fetch('/api/v1/llm/generate-script', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(request)
            })
            .then(function(res) { return res.json(); });
        },

        getContextForLlm: function() {
            return {
                sessionId: this.sessionId,
                userId: this.userId,
                currentModule: this.currentModule,
                pageState: this.sanitizePageState(this.pageState),
                availableApis: this.getAvailableApis(),
                timestamp: Date.now()
            };
        },

        destroy: function() {
            this.syncHandlers = [];
            this.pageState = {};
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            ContextTrustLayer.init();
        });
    } else {
        ContextTrustLayer.init();
    }

    global.ContextTrustLayer = ContextTrustLayer;
})(window);
