(function() {
    'use strict';
    
    var KnowledgeCenter = {
        knowledgeBases: [],
        organizations: [],
        currentView: 'overview',
        currentFilter: null,
        
        init: function() {
            this.loadOrganizations();
            this.loadKnowledgeBases();
        },
        
        loadOrganizations: function() {
            var self = this;
            ApiClient.get('/api/v1/knowledge-organizations')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.organizations = result.data;
                        self.renderOrgTree();
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load organizations:', error);
                });
        },
        
        renderOrgTree: function() {
            var container = document.getElementById('orgTree');
            if (!container || !this.organizations || this.organizations.length === 0) return;
            
            var html = this.organizations.map(function(org) {
                return '<div class="tree-item" data-view="' + org.orgId + '" onclick="switchView(\'' + org.orgId + '\')">' +
                    '<i class="' + (org.icon || 'ri-folder-line') + '"></i>' +
                    '<span class="tree-name">' + org.name + '</span>' +
                '</div>';
            }).join('');
            
            container.innerHTML = html;
        },
        
        loadKnowledgeBases: function() {
            var self = this;
            ApiClient.get('/api/v1/knowledge-bases')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.knowledgeBases = result.data;
                        self.renderStats();
                        self.renderTree();
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load knowledge bases:', error);
                });
        },
        
        renderStats: function() {
            var totalDocs = 0;
            var generalCount = 0;
            var professionalCount = 0;
            var sceneCount = 0;
            var embeddingModel = '-';
            
            var companyKbs = 0, companyDocs = 0;
            var deptKbs = 0, deptDocs = 0;
            var specialKbs = 0, specialDocs = 0;
            
            this.knowledgeBases.forEach(function(kb) {
                totalDocs += kb.documentCount || 0;
                var layer = kb.layerConfig ? kb.layerConfig.layer : 'GENERAL';
                
                if (layer === 'GENERAL') {
                    generalCount++;
                    companyKbs++;
                    companyDocs += kb.documentCount || 0;
                } else if (layer === 'PROFESSIONAL') {
                    professionalCount++;
                    deptKbs++;
                    deptDocs += kb.documentCount || 0;
                } else if (layer === 'SCENE') {
                    sceneCount++;
                    specialKbs++;
                    specialDocs += kb.documentCount || 0;
                }
                
                if (kb.embeddingModel && embeddingModel === '-') {
                    embeddingModel = kb.embeddingModel.split('/').pop().substring(0, 10);
                }
            });
            
            document.getElementById('statTotalKb').textContent = this.knowledgeBases.length;
            document.getElementById('statTotalDocs').textContent = totalDocs;
            document.getElementById('statEmbeddingModel').textContent = embeddingModel;
            document.getElementById('statBindings').textContent = '-';
            
            document.getElementById('totalCount').textContent = this.knowledgeBases.length;
            document.getElementById('generalCount').textContent = generalCount;
            document.getElementById('professionalCount').textContent = professionalCount;
            document.getElementById('sceneCount').textContent = sceneCount;
            
            document.getElementById('companyKbCount').textContent = companyKbs;
            document.getElementById('companyDocCount').textContent = companyDocs;
            document.getElementById('deptKbCount').textContent = deptKbs;
            document.getElementById('deptDocCount').textContent = deptDocs;
            document.getElementById('specialKbCount').textContent = specialKbs;
            document.getElementById('specialDocCount').textContent = specialDocs;
        },
        
        renderTree: function() {
            var self = this;
            var kbTree = document.getElementById('kbTree');
            
            if (this.knowledgeBases.length === 0) {
                kbTree.innerHTML = '<div class="tree-item" style="color: var(--nx-text-secondary); font-size: 13px;"><span class="tree-name">暂无知识库</span></div>';
                return;
            }
            
            kbTree.innerHTML = this.knowledgeBases.map(function(kb) {
                return '<div class="tree-item" data-id="' + kb.kbId + '" onclick="selectKb(\'' + kb.kbId + '\')">' +
                    '<i class="ri-book-line"></i>' +
                    '<span class="tree-name">' + kb.name + '</span>' +
                    '<span class="tree-count">' + (kb.documentCount || 0) + '</span>' +
                '</div>';
            }).join('');
        },
        
        switchView: function(view) {
            this.currentView = view;
            
            document.querySelectorAll('.tree-item').forEach(function(el) {
                el.classList.remove('active');
            });
            var activeItem = document.querySelector('.tree-item[data-view="' + view + '"]');
            if (activeItem) activeItem.classList.add('active');
            
            if (view === 'overview') {
                document.getElementById('view-overview').style.display = 'block';
                document.getElementById('view-list').style.display = 'none';
            } else {
                document.getElementById('view-overview').style.display = 'none';
                document.getElementById('view-list').style.display = 'block';
                this.renderKbList(view);
            }
        },
        
        renderKbList: function(filter) {
            var container = document.getElementById('kbGrid');
            var titleEl = document.getElementById('listTitle');
            
            var titleMap = {
                'company': '公司级知识库',
                'department': '部门级知识库',
                'special': '专用业务知识库',
                'layer-general': '通用知识层',
                'layer-professional': '专业模块层',
                'layer-scene': '场景知识层'
            };
            titleEl.textContent = titleMap[filter] || '知识库列表';
            
            var filtered = this.knowledgeBases.filter(function(kb) {
                var layer = kb.layerConfig ? kb.layerConfig.layer : 'GENERAL';
                if (filter === 'company' || filter === 'layer-general') return layer === 'GENERAL';
                if (filter === 'department' || filter === 'layer-professional') return layer === 'PROFESSIONAL';
                if (filter === 'special' || filter === 'layer-scene') return layer === 'SCENE';
                return true;
            });
            
            if (filtered.length === 0) {
                container.innerHTML = '<div class="empty-state">' +
                    '<i class="ri-book-line"></i>' +
                    '<div class="empty-title">暂无知识库</div>' +
                    '<div>点击"新建"创建第一个知识库</div>' +
                '</div>';
                return;
            }
            
            container.innerHTML = filtered.map(function(kb) {
                var layer = kb.layerConfig ? kb.layerConfig.layer : 'GENERAL';
                var layerClass = layer.toLowerCase();
                var layerName = layer === 'GENERAL' ? '通用层' : (layer === 'PROFESSIONAL' ? '专业层' : '场景层');
                
                return '<div class="kb-card" onclick="openKbDetail(\'' + kb.kbId + '\')">' +
                    '<div class="kb-card-header">' +
                        '<div class="kb-card-icon ' + layerClass + '"><i class="ri-book-line"></i></div>' +
                        '<div>' +
                            '<div class="kb-card-title">' + kb.name + '</div>' +
                            '<span class="layer-badge ' + layerClass + '">' + layerName + '</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="kb-card-desc">' + (kb.description || '暂无描述') + '</div>' +
                    '<div class="kb-card-meta">' +
                        '<span><i class="ri-file-text-line"></i> ' + (kb.documentCount || 0) + ' 文档</span>' +
                        '<span><i class="ri-' + (kb.visibility === 'public' ? 'global' : 'lock') + '-line"></i> ' + (kb.visibility || 'private') + '</span>' +
                    '</div>' +
                '</div>';
            }).join('');
        },
        
        selectKb: function(kbId) {
            window.location.href = 'knowledge-base.html?kbId=' + kbId;
        },
        
        createKb: function() {
            var self = this;
            var name = document.getElementById('newKbName').value.trim();
            var description = document.getElementById('newKbDesc').value.trim();
            var layer = document.getElementById('newKbLayer').value;
            var visibility = document.getElementById('newKbVisibility').value;
            
            if (!name) {
                alert('请输入知识库名称');
                return;
            }
            
            var newKb = {
                name: name,
                description: description,
                visibility: visibility,
                embeddingModel: 'text-embedding-ada-002',
                chunkSize: 500,
                chunkOverlap: 50,
                layerConfig: {
                    layer: layer,
                    priority: layer === 'GENERAL' ? 0 : (layer === 'PROFESSIONAL' ? 1 : 2),
                    enabled: true
                }
            };
            
            ApiClient.post('/api/v1/knowledge-bases', newKb)
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.knowledgeBases.push(result.data);
                        self.renderStats();
                        self.renderTree();
                        closeModal('createKbModal');
                        document.getElementById('newKbName').value = '';
                        document.getElementById('newKbDesc').value = '';
                    } else {
                        alert('创建失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('创建失败: ' + (error.message || '网络错误'));
                });
        }
    };
    
    window.toggleTreeSection = function(el) {
        var toggle = el.querySelector('.tree-toggle');
        var next = el.nextElementSibling;
        if (next && next.classList.contains('tree-children')) {
            next.classList.toggle('collapsed');
            toggle.classList.toggle('collapsed');
        }
    };
    
    window.switchView = function(view) {
        KnowledgeCenter.switchView(view);
    };
    
    window.selectKb = function(kbId) {
        KnowledgeCenter.selectKb(kbId);
    };
    
    window.openKbDetail = function(kbId) {
        window.location.href = 'knowledge-base.html?kbId=' + kbId;
    };
    
    window.showCreateKbModal = function() {
        document.getElementById('createKbModal').classList.add('open');
    };
    
    window.closeModal = function(id) {
        document.getElementById(id).classList.remove('open');
    };
    
    window.createKb = function() {
        KnowledgeCenter.createKb();
    };
    
    document.addEventListener('DOMContentLoaded', KnowledgeCenter.init.bind(KnowledgeCenter));
})();
