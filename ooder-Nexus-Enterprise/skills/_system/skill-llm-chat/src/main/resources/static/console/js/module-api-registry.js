(function(global) {
    'use strict';

    var ModuleApiRegistry = {
        currentModule: null,
        modules: {},
        handlers: {},

        init: function() {
            this.registerDiscoveryApis();
            this.registerInstallApis();
            this.registerActivationApis();
            console.log('[ModuleApiRegistry] Initialized with modules:', Object.keys(this.modules));
        },

        registerDiscoveryApis: function() {
            var discovery = {
                id: 'discovery',
                name: '能力发现模块',
                apis: {}
            };

            discovery.apis.startScan = {
                name: 'startScan',
                description: '开始扫描能力',
                params: {
                    method: { type: 'string', description: '发现方式(LOCAL_FS/GITHUB/GITEE/AUTO)', required: true },
                    forceRefresh: { type: 'boolean', description: '是否强制刷新', required: false }
                },
                execute: function(params) {
                    if (typeof CapabilityDiscovery !== 'undefined') {
                        return CapabilityDiscovery.startScan(params.method, params.forceRefresh);
                    }
                    return { error: 'CapabilityDiscovery not available' };
                }
            };

            discovery.apis.selectCapability = {
                name: 'selectCapability',
                description: '选择一个能力',
                params: {
                    capabilityId: { type: 'string', description: '能力ID', required: true }
                },
                execute: function(params) {
                    if (typeof CapabilityDiscovery !== 'undefined') {
                        return CapabilityDiscovery.selectCapability(params.capabilityId);
                    }
                    return { error: 'CapabilityDiscovery not available' };
                }
            };

            discovery.apis.filterCapabilities = {
                name: 'filterCapabilities',
                description: '筛选能力',
                params: {
                    type: { type: 'string', description: '能力类型(SCENE/SKILL/COMMUNICATION/AI)', required: false },
                    keyword: { type: 'string', description: '关键词', required: false },
                    installed: { type: 'boolean', description: '是否已安装', required: false }
                },
                execute: function(params) {
                    if (typeof CapabilityDiscovery !== 'undefined') {
                        return CapabilityDiscovery.filterCapabilities(params);
                    }
                    return { error: 'CapabilityDiscovery not available' };
                }
            };

            discovery.apis.getCapabilityDetail = {
                name: 'getCapabilityDetail',
                description: '获取能力详情',
                params: {
                    capabilityId: { type: 'string', description: '能力ID', required: true }
                },
                execute: function(params) {
                    if (typeof CapabilityDiscovery !== 'undefined') {
                        return CapabilityDiscovery.getCapabilityDetail(params.capabilityId);
                    }
                    return { error: 'CapabilityDiscovery not available' };
                }
            };

            discovery.apis.getDriverConditions = {
                name: 'getDriverConditions',
                description: '获取驱动条件',
                params: {
                    capabilityId: { type: 'string', description: '能力ID', required: true }
                },
                execute: function(params) {
                    return fetch('/api/v1/discovery/capabilities/detail/' + params.capabilityId + '/driver-conditions')
                        .then(function(res) { return res.json(); });
                }
            };

            this.modules.discovery = discovery;
        },

        registerInstallApis: function() {
            var install = {
                id: 'install',
                name: '安装向导模块',
                apis: {}
            };

            install.apis.startInstall = {
                name: 'startInstall',
                description: '开始安装',
                params: {
                    capabilityId: { type: 'string', description: '能力ID', required: true },
                    config: { type: 'object', description: '安装配置', required: false }
                },
                execute: function(params) {
                    if (typeof InstallWizard !== 'undefined') {
                        return InstallWizard.startInstall(params.capabilityId, params.config);
                    }
                    return { error: 'InstallWizard not available' };
                }
            };

            install.apis.setConfig = {
                name: 'setConfig',
                description: '设置配置项',
                params: {
                    key: { type: 'string', description: '配置键', required: true },
                    value: { type: 'any', description: '配置值', required: true }
                },
                execute: function(params) {
                    if (typeof InstallWizard !== 'undefined') {
                        return InstallWizard.setConfig(params.key, params.value);
                    }
                    return { error: 'InstallWizard not available' };
                }
            };

            install.apis.nextStep = {
                name: 'nextStep',
                description: '下一步',
                params: {},
                execute: function(params) {
                    if (typeof InstallWizard !== 'undefined') {
                        return InstallWizard.nextStep();
                    }
                    return { error: 'InstallWizard not available' };
                }
            };

            install.apis.confirm = {
                name: 'confirm',
                description: '确认安装',
                params: {},
                execute: function(params) {
                    if (typeof InstallWizard !== 'undefined') {
                        return InstallWizard.confirm();
                    }
                    return { error: 'InstallWizard not available' };
                }
            };

            this.modules.install = install;
        },

        registerActivationApis: function() {
            var activation = {
                id: 'activation',
                name: '激活流程模块',
                apis: {}
            };

            activation.apis.executeStep = {
                name: 'executeStep',
                description: '执行激活步骤',
                params: {
                    stepId: { type: 'string', description: '步骤ID', required: true },
                    data: { type: 'object', description: '步骤数据', required: false }
                },
                execute: function(params) {
                    if (typeof ActivationFlow !== 'undefined') {
                        return ActivationFlow.executeStep(params.stepId, params.data);
                    }
                    return { error: 'ActivationFlow not available' };
                }
            };

            activation.apis.skipStep = {
                name: 'skipStep',
                description: '跳过步骤',
                params: {
                    stepId: { type: 'string', description: '步骤ID', required: true }
                },
                execute: function(params) {
                    if (typeof ActivationFlow !== 'undefined') {
                        return ActivationFlow.skipStep(params.stepId);
                    }
                    return { error: 'ActivationFlow not available' };
                }
            };

            activation.apis.selectDriverCondition = {
                name: 'selectDriverCondition',
                description: '选择驱动条件',
                params: {
                    conditionId: { type: 'string', description: '条件ID', required: true }
                },
                execute: function(params) {
                    if (typeof ActivationFlow !== 'undefined') {
                        return ActivationFlow.selectDriverCondition(params.conditionId);
                    }
                    return { error: 'ActivationFlow not available' };
                }
            };

            activation.apis.complete = {
                name: 'complete',
                description: '完成激活',
                params: {},
                execute: function(params) {
                    if (typeof ActivationFlow !== 'undefined') {
                        return ActivationFlow.complete();
                    }
                    return { error: 'ActivationFlow not available' };
                }
            };

            this.modules.activation = activation;
        },

        setCurrentModule: function(moduleId) {
            if (this.modules[moduleId]) {
                this.currentModule = moduleId;
                console.log('[ModuleApiRegistry] Current module set to:', moduleId);
                this.notifyModuleChange(moduleId);
            } else {
                console.warn('[ModuleApiRegistry] Unknown module:', moduleId);
            }
        },

        getCurrentModule: function() {
            return this.currentModule;
        },

        getModule: function(moduleId) {
            return this.modules[moduleId];
        },

        getAvailableApis: function(moduleId) {
            var module = this.modules[moduleId || this.currentModule];
            if (!module) return {};
            return module.apis;
        },

        getApiNames: function(moduleId) {
            var apis = this.getAvailableApis(moduleId);
            return Object.keys(apis);
        },

        getApiDefinition: function(moduleId, apiName) {
            var apis = this.getAvailableApis(moduleId);
            return apis[apiName];
        },

        executeApi: function(apiName, params) {
            if (!this.currentModule) {
                return Promise.reject(new Error('No current module set'));
            }

            var api = this.getApiDefinition(this.currentModule, apiName);
            if (!api) {
                return Promise.reject(new Error('API not found: ' + apiName + ' in module: ' + this.currentModule));
            }

            try {
                var result = api.execute(params || {});
                if (result && typeof result.then === 'function') {
                    return result;
                }
                return Promise.resolve(result);
            } catch (e) {
                return Promise.reject(e);
            }
        },

        validateApiCall: function(apiName, moduleId) {
            if (moduleId !== this.currentModule) {
                return { valid: false, error: 'Module mismatch. Current: ' + this.currentModule + ', Requested: ' + moduleId };
            }

            var api = this.getApiDefinition(moduleId, apiName);
            if (!api) {
                return { valid: false, error: 'API not found: ' + apiName };
            }

            return { valid: true };
        },

        validateParams: function(apiName, params) {
            var api = this.getApiDefinition(this.currentModule, apiName);
            if (!api) return { valid: false, error: 'API not found' };

            var missingParams = [];
            for (var paramName in api.params) {
                if (api.params[paramName].required && (!params || params[paramName] === undefined)) {
                    missingParams.push(paramName);
                }
            }

            if (missingParams.length > 0) {
                return { valid: false, error: 'Missing required params: ' + missingParams.join(', ') };
            }

            return { valid: true };
        },

        getModuleContext: function() {
            var module = this.modules[this.currentModule];
            if (!module) return null;

            return {
                moduleId: module.id,
                moduleName: module.name,
                availableApis: this.getApiNames(),
                apiDefinitions: this.getAvailableApis()
            };
        },

        onModuleChange: function(callback) {
            this.handlers.moduleChange = this.handlers.moduleChange || [];
            this.handlers.moduleChange.push(callback);
        },

        notifyModuleChange: function(moduleId) {
            var handlers = this.handlers.moduleChange || [];
            handlers.forEach(function(callback) {
                try {
                    callback(moduleId);
                } catch (e) {
                    console.error('[ModuleApiRegistry] Handler error:', e);
                }
            });
        },

        getApiSchema: function(moduleId) {
            var module = this.modules[moduleId || this.currentModule];
            if (!module) return null;

            var schema = {
                module: module.id,
                name: module.name,
                apis: []
            };

            for (var apiName in module.apis) {
                var api = module.apis[apiName];
                var apiSchema = {
                    name: api.name,
                    description: api.description,
                    parameters: []
                };

                for (var paramName in api.params) {
                    apiSchema.parameters.push({
                        name: paramName,
                        type: api.params[paramName].type,
                        description: api.params[paramName].description,
                        required: api.params[paramName].required
                    });
                }

                schema.apis.push(apiSchema);
            }

            return schema;
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            ModuleApiRegistry.init();
        });
    } else {
        ModuleApiRegistry.init();
    }

    global.ModuleApiRegistry = ModuleApiRegistry;
})(window);
