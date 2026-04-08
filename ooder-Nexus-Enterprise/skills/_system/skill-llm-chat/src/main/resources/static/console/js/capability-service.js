var CapabilityService = {
    
    _cache: {
        all: null,
        byOwnership: {},
        byCategory: {},
        stats: null,
        lastUpdate: 0
    },
    
    CACHE_TTL: 30000,
    
    clearCache: function() {
        this._cache = {
            all: null,
            byOwnership: {},
            byCategory: {},
            stats: null,
            lastUpdate: 0
        };
    },
    
    isCacheValid: function() {
        return this._cache.all && 
               (Date.now() - this._cache.lastUpdate) < this.CACHE_TTL;
    },
    
    getAll: function(forceRefresh) {
        var self = this;
        
        if (this.isCacheValid() && !forceRefresh) {
            return Promise.resolve(this._cache.all);
        }
        
        return ApiClient.get('/api/v1/capabilities')
            .then(function(result) {
                var list = result.data || [];
                self._cache.all = list;
                self._cache.lastUpdate = Date.now();
                self._updateSubCaches(list);
                return list;
            });
    },
    
    _updateSubCaches: function(list) {
        var self = this;
        
        this._cache.byOwnership = { SIC: [], IC: [], PC: [] };
        this._cache.bySkillForm = { SCENE: [], STANDALONE: [] };
        this._cache.bySceneType = { AUTO: [], TRIGGER: [] };
        
        list.forEach(function(cap) {
            var ownership = self._normalizeOwnership(cap.ownership);
            if (self._cache.byOwnership[ownership]) {
                self._cache.byOwnership[ownership].push(cap);
            }
            
            var skillForm = cap.skillForm || self._normalizeSkillForm(cap);
            if (self._cache.bySkillForm[skillForm]) {
                self._cache.bySkillForm[skillForm].push(cap);
            }
            
            var sceneType = cap.sceneType;
            if (sceneType && self._cache.bySceneType[sceneType]) {
                self._cache.bySceneType[sceneType].push(cap);
            }
        });
    },
    
    _normalizeOwnership: function(ownership) {
        if (!ownership) return 'PC';
        var upper = ownership.toUpperCase();
        if (upper === 'SCENE_INTERNAL' || upper === 'SIC') return 'SIC';
        if (upper === 'INDEPENDENT' || upper === 'IC') return 'IC';
        if (upper === 'PLATFORM' || upper === 'PC') return 'PC';
        return 'PC';
    },
    
    _normalizeSkillForm: function(cap) {
        if (cap.skillForm) return cap.skillForm;
        if (cap.sceneCapability === true || cap.type === 'SCENE') return 'SCENE';
        return 'STANDALONE';
    },
    
    _normalizeSceneType: function(sceneType) {
        if (!sceneType) return null;
        var upper = sceneType.toUpperCase();
        if (['AUTO', 'TRIGGER'].indexOf(upper) >= 0) {
            return upper;
        }
        return null;
    },
    
    getByOwnership: function(ownership) {
        var self = this;
        var normalized = this._normalizeOwnership(ownership);
        
        if (this.isCacheValid() && this._cache.byOwnership[normalized]) {
            return Promise.resolve(this._cache.byOwnership[normalized]);
        }
        
        return this.getAll().then(function() {
            return self._cache.byOwnership[normalized] || [];
        });
    },
    
    getBySkillForm: function(skillForm) {
        var self = this;
        
        if (this.isCacheValid() && this._cache.bySkillForm[skillForm]) {
            return Promise.resolve(this._cache.bySkillForm[skillForm]);
        }
        
        return this.getAll().then(function() {
            return self._cache.bySkillForm[skillForm] || [];
        });
    },
    
    getBySceneType: function(sceneType) {
        var self = this;
        
        if (this.isCacheValid() && this._cache.bySceneType[sceneType]) {
            return Promise.resolve(this._cache.bySceneType[sceneType]);
        }
        
        return this.getAll().then(function() {
            return self._cache.bySceneType[sceneType] || [];
        });
    },
    
    getByCategory: function(category) {
        return this.getBySkillForm(category);
    },
    
    getStats: function(forceRefresh) {
        var self = this;
        
        if (this._cache.stats && !forceRefresh && this.isCacheValid()) {
            return Promise.resolve(this._cache.stats);
        }
        
        return ApiClient.get('/api/v1/capabilities/stats/overview')
            .then(function(result) {
                self._cache.stats = result.data;
                return result.data;
            });
    },
    
    getStatsByCategory: function() {
        return ApiClient.get('/api/v1/capabilities/stats/by-skill-form')
            .then(function(result) {
                return result.data || {};
            });
    },
    
    getStatsBySkillForm: function() {
        return ApiClient.get('/api/v1/capabilities/stats/by-skill-form')
            .then(function(result) {
                return result.data || {};
            });
    },
    
    getStatsBySceneType: function() {
        return ApiClient.get('/api/v1/capabilities/stats/by-scene-type')
            .then(function(result) {
                return result.data || {};
            });
    },
    
    getStatsByOwnership: function() {
        return ApiClient.get('/api/v1/capabilities/stats/by-ownership')
            .then(function(result) {
                return result.data || {};
            });
    },
    
    getById: function(capabilityId) {
        return ApiClient.get('/api/v1/capabilities/' + encodeURIComponent(capabilityId))
            .then(function(result) {
                return result.data;
            });
    },
    
    search: function(keyword, filters) {
        var params = {};
        if (keyword) params.keyword = keyword;
        if (filters) {
            if (filters.type) params.type = filters.type;
            if (filters.ownership) params.ownership = filters.ownership;
            if (filters.skillForm) params.skillForm = filters.skillForm;
            if (filters.sceneType) params.sceneType = filters.sceneType;
            if (filters.installed !== undefined) params.installed = filters.installed;
        }
        
        return ApiClient.get('/api/v1/capabilities', params)
            .then(function(result) {
                return result.data || [];
            });
    },
    
    searchByFilters: function(filters) {
        var params = {};
        if (filters.keyword) params.keyword = filters.keyword;
        if (filters.skillForm) params.skillForm = filters.skillForm;
        if (filters.sceneType) params.sceneType = filters.sceneType;
        if (filters.skillCategory) params.skillCategory = filters.skillCategory;
        if (filters.ownership) params.ownership = filters.ownership;
        
        return ApiClient.get('/api/v1/capabilities/search', params)
            .then(function(result) {
                return result.data || [];
            });
    },
    
    filter: function(list, filterType) {
        var self = this;
        
        if (!list) return [];
        
        switch (filterType) {
            case 'all':
                return list.filter(function(cap) {
                    var cat = self._normalizeCategory(cap.category);
                    return cat !== 'ASS';
                });
            case 'SIC':
                return list.filter(function(cap) {
                    return self._normalizeOwnership(cap.ownership) === 'SIC';
                });
            case 'IC':
                return list.filter(function(cap) {
                    return self._normalizeOwnership(cap.ownership) === 'IC';
                });
            case 'PC':
                return list.filter(function(cap) {
                    return self._normalizeOwnership(cap.ownership) === 'PC';
                });
            case 'ABS':
                return list.filter(function(cap) {
                    return self._normalizeCategory(cap.category) === 'ABS';
                });
            case 'TBS':
                return list.filter(function(cap) {
                    return self._normalizeCategory(cap.category) === 'TBS';
                });
            case 'new':
                return list.filter(function(cap) {
                    return !cap.installed && self._normalizeCategory(cap.category) !== 'ASS';
                });
            case 'installed':
                return list.filter(function(cap) {
                    return cap.installed && self._normalizeCategory(cap.category) !== 'ASS';
                });
            default:
                return list;
        }
    },
    
    calculateStats: function(list) {
        var self = this;
        var stats = {
            total: 0,
            SIC: 0, IC: 0, PC: 0,
            ABS: 0, ASS: 0, TBS: 0, OTHER: 0,
            new: 0, installed: 0
        };
        
        if (!list) return stats;
        
        list.forEach(function(cap) {
            var ownership = self._normalizeOwnership(cap.ownership);
            var category = self._normalizeCategory(cap.category);
            
            if (category !== 'ASS') {
                stats.total++;
                
                if (cap.installed) {
                    stats.installed++;
                } else {
                    stats.new++;
                }
            }
            
            stats[ownership]++;
            stats[category]++;
        });
        
        return stats;
    },
    
    getTypeInfo: function(type) {
        var typeConfig = {
            'ATOMIC': { name: '原子能力', icon: 'ri-flashlight-line', color: '#10b981' },
            'COMPOSITE': { name: '组合能力', icon: 'ri-stack-line', color: '#6366f1' },
            'SCENE': { name: '场景特性', icon: 'ri-artboard-line', color: '#8b5cf6' },
            'DRIVER': { name: '驱动能力', icon: 'ri-route-line', color: '#f59e0b' },
            'COLLABORATIVE': { name: '协作能力', icon: 'ri-team-line', color: '#ec4899' },
            'SERVICE': { name: '服务能力', icon: 'ri-server-line', color: '#3b82f6' },
            'AI': { name: 'AI能力', icon: 'ri-robot-line', color: '#14b8a6' },
            'TOOL': { name: '工具能力', icon: 'ri-tools-line', color: '#f97316' },
            'CONNECTOR': { name: '连接器', icon: 'ri-plug-line', color: '#64748b' },
            'DATA': { name: '数据能力', icon: 'ri-database-2-line', color: '#0891b2' },
            'MANAGEMENT': { name: '管理能力', icon: 'ri-settings-3-line', color: '#7c3aed' },
            'SKILL': { name: '技能包', icon: 'ri-puzzle-line', color: '#2563eb' },
            'CUSTOM': { name: '自定义', icon: 'ri-edit-line', color: '#6b7280' }
        };
        
        return typeConfig[type] || { name: type || '其他', icon: 'ri-question-line', color: '#9ca3af' };
    },
    
    getOwnershipInfo: function(ownership) {
        var normalized = this._normalizeOwnership(ownership);
        var config = {
            'SIC': { name: '场景内部能力', shortName: '场景能力', icon: 'ri-layout-grid-line', color: '#10b981', description: '依附于场景，不可独立使用' },
            'IC': { name: '独立能力', shortName: '独立能力', icon: 'ri-apps-line', color: '#6366f1', description: '可独立使用，支持多场景' },
            'PC': { name: '平台能力', shortName: '平台能力', icon: 'ri-building-line', color: '#3b82f6', description: '平台基础能力，全局可用' }
        };
        return config[normalized] || config['PC'];
    },
    
    getCategoryInfo: function(category) {
        var normalized = this._normalizeCategory(category);
        var config = {
            'ABS': { code: 'ABS', name: '自驱业务场景', icon: 'ri-layout-grid-line', color: '#10b981', description: '自动驱动且高业务语义' },
            'ASS': { code: 'ASS', name: '自驱系统场景', icon: 'ri-settings-4-line', color: '#6366f1', description: '自动驱动但业务语义不足（内部）' },
            'TBS': { code: 'TBS', name: '触发业务场景', icon: 'ri-hand-coin-line', color: '#f59e0b', description: '需要外部触发的高业务语义场景' },
            'NOT_SCENE_SKILL': { code: 'NOT_SCENE_SKILL', name: '非场景技能', icon: 'ri-tools-line', color: '#6b7280', description: '不满足场景技能基本标准' }
        };
        return config[normalized] || config['NOT_SCENE_SKILL'];
    }
};
