(function() {
    'use strict';

    var AppConfig = {
        brand: {
            name: 'Apex OS',
            shortName: 'Apex',
            tagline: 'Skill Scene Platform',
            version: '1.0.0'
        },
        
        api: {
            baseUrl: '/api/v1',
            endpoints: {
                auth: {
                    login: '/auth/login',
                    logout: '/auth/logout',
                    session: '/auth/session',
                    register: '/auth/register'
                },
                users: {
                    current: '/org/users/current',
                    list: '/org/users',
                    stats: '/org/users/current/stats'
                },
                capabilities: {
                    list: '/capabilities',
                    bindings: '/capabilities/bindings',
                    discoverable: '/capabilities/discoverable'
                },
                sceneGroups: {
                    list: '/scene-groups',
                    my: '/scene-groups/my/participated',
                    myLed: '/scene-groups/my/led',
                    myCreated: '/scene-groups/my/created'
                },
                llm: {
                    providers: '/llm/providers',
                    testProvider: '/llm/providers/{id}/test'
                },
                agent: {
                    list: '/agent/list',
                    detail: '/agent/{id}',
                    heartbeat: '/agent/{id}/heartbeat'
                },
                todos: {
                    list: '/my/todos',
                    accept: '/my/todos/{id}/accept',
                    reject: '/my/todos/{id}/reject'
                },
                keys: {
                    my: '/keys/my',
                    list: '/keys'
                }
            }
        },
        
        pages: {
            login: '/console/pages/login.html',
            dashboard: '/console/pages/role-admin.html',
            profile: '/console/pages/my-profile.html',
            capabilities: '/console/pages/capability-management.html',
            scenes: '/console/pages/scene-group-management.html',
            llmConfig: '/console/pages/llm-config.html'
        },
        
        roles: {
            admin: {
                name: '管理员',
                icon: 'ri-admin-line',
                workspace: '/console/pages/role-admin.html'
            },
            manager: {
                name: '管理者',
                icon: 'ri-user-settings-line',
                workspace: '/console/pages/role-admin.html'
            },
            leader: {
                name: '主导者',
                icon: 'ri-user-star-line',
                workspace: '/console/pages/role-leader.html'
            },
            collaborator: {
                name: '协作者',
                icon: 'ri-team-line',
                workspace: '/console/pages/role-collaborator.html'
            },
            installer: {
                name: '安装者',
                icon: 'ri-install-line',
                workspace: '/console/pages/role-installer.html'
            },
            employee: {
                name: '员工',
                icon: 'ri-user-line',
                workspace: '/console/pages/role-user.html'
            },
            developer: {
                name: '开发者',
                icon: 'ri-code-line',
                workspace: '/console/pages/role-developer.html'
            }
        },
        
        ui: {
            toast: {
                duration: 3000,
                position: 'bottom-right'
            },
            modal: {
                backdropClose: true,
                escapeClose: true
            },
            pagination: {
                pageSize: 20,
                pageSizeOptions: [10, 20, 50, 100]
            }
        },
        
        getApiUrl: function(endpoint, params) {
            var url = this.api.baseUrl + endpoint;
            if (params) {
                Object.keys(params).forEach(function(key) {
                    url = url.replace('{' + key + '}', params[key]);
                });
            }
            return url;
        },
        
        getPageTitle: function(pageName) {
            return pageName + ' - ' + this.brand.name;
        },
        
        getRoleInfo: function(roleType) {
            return this.roles[roleType] || this.roles.employee;
        }
    };

    window.AppConfig = AppConfig;
})();
