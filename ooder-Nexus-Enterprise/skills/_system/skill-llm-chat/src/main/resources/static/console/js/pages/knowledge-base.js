(function() {
    'use strict';
    
    var KnowledgeBase = {
        knowledgeBases: [],
        currentKb: null,
        documents: [],
        tags: [],
        docTags: [],
        
        init: function() {
            var params = new URLSearchParams(window.location.search);
            this.initialKbId = params.get('kbId');
            this.loadKnowledgeBases();
        },
        
        loadKnowledgeBases: function() {
            var self = this;
            
            ApiClient.get('/api/v1/knowledge-bases')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.knowledgeBases = result.data;
                        self.renderKbList();
                        if (self.initialKbId) {
                            self.selectKb(self.initialKbId);
                            self.initialKbId = null;
                        }
                    } else {
                        console.error('Failed to load knowledge bases:', result.message);
                        self.knowledgeBases = [];
                        self.renderKbList();
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load knowledge bases:', error);
                    self.knowledgeBases = [];
                    self.renderKbList();
                });
        },
        
        renderKbList: function() {
            var container = document.getElementById('kbList');
            if (!container) return;
            
            var searchInput = document.getElementById('kbSearch');
            var searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
            
            var filtered = this.knowledgeBases.filter(function(kb) {
                return kb.name.toLowerCase().includes(searchTerm) || 
                    (kb.description && kb.description.toLowerCase().includes(searchTerm));
            });
            
            if (filtered.length === 0) {
                container.innerHTML = '<div style="text-align: center; padding: 20px; color: var(--nx-text-secondary);">无匹配结果</div>';
                return;
            }
            
            var self = this;
            container.innerHTML = filtered.map(function(kb) {
                var layer = kb.layerConfig ? kb.layerConfig.layer : 'GENERAL';
                var layerIcon = layer === 'GENERAL' ? 'ri-global-line' : (layer === 'PROFESSIONAL' ? 'ri-briefcase-line' : 'ri-flashlight-line');
                var activeClass = self.currentKb && self.currentKb.kbId === kb.kbId ? 'active' : '';
                var docCount = kb.documentCount || 0;
                return '<div class="kb-item ' + activeClass + '" onclick="selectKb(\'' + kb.kbId + '\')">' +
                    '<div class="kb-item-name"><i class="' + layerIcon + '"></i> ' + kb.name + '</div>' +
                    '<div class="kb-item-desc">' + (kb.description || '无描述') + '</div>' +
                    '<div class="kb-item-meta">' +
                        '<span><i class="ri-file-text-line"></i> ' + docCount + ' 文档</span>' +
                        '<span><i class="ri-' + (kb.visibility === 'public' ? 'global' : (kb.visibility === 'team' ? 'team' : 'lock')) + '-line"></i> ' + kb.visibility + '</span>' +
                    '</div>' +
                '</div>';
            }).join('');
        },
        
        filterKbList: function() {
            this.renderKbList();
        },
        
        selectKb: function(kbId) {
            this.currentKb = this.knowledgeBases.find(function(kb) { return kb.kbId === kbId; });
            if (!this.currentKb) return;
            
            document.getElementById('emptyState').style.display = 'none';
            document.getElementById('kbDetail').style.display = 'flex';
            
            document.getElementById('kbName').textContent = this.currentKb.name;
            
            var docCount = this.currentKb.documentCount || 0;
            document.getElementById('indexDocCount').textContent = docCount + ' 文档已索引';
            
            var indexStatus = this.currentKb.indexStatus ? this.currentKb.indexStatus.status : 'pending';
            var progress = indexStatus === 'completed' ? 100 : (indexStatus === 'indexing' ? (this.currentKb.indexStatus.progress || 0) : 0);
            document.getElementById('indexProgressFill').style.width = progress + '%';
            document.getElementById('indexPercent').textContent = progress + '%';
            document.getElementById('indexStatusText').textContent = indexStatus === 'completed' ? '已完成' : (indexStatus === 'indexing' ? '索引中...' : '待索引');
            
            if (this.currentKb.embeddingModel) {
                document.getElementById('embeddingModel').value = this.currentKb.embeddingModel;
            }
            if (this.currentKb.chunkSize) {
                document.getElementById('chunkSize').value = this.currentKb.chunkSize;
            }
            if (this.currentKb.chunkOverlap) {
                document.getElementById('chunkOverlap').value = this.currentKb.chunkOverlap;
            }
            if (this.currentKb.visibility) {
                document.getElementById('visibility').value = this.currentKb.visibility;
            }
            
            this.renderKbList();
            this.loadDocuments();
        },
        
        loadDocuments: function() {
            var self = this;
            var container = document.getElementById('docList');
            
            if (!this.currentKb) return;
            
            container.innerHTML = '<div style="text-align: center; padding: 40px; color: var(--nx-text-secondary);">' +
                '<i class="ri-loader-4-line ri-spin" style="font-size: 32px;"></i>' +
                '<div style="margin-top: 12px;">加载文档列表...</div>' +
            '</div>';
            
            ApiClient.get('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.documents = result.data.list || result.data || [];
                        self.renderDocuments();
                    } else {
                        self.documents = [];
                        self.renderDocuments();
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load documents:', error);
                    self.documents = [];
                    self.renderDocuments();
                });
        },
        
        renderDocuments: function() {
            var container = document.getElementById('docList');
            
            if (this.documents.length === 0) {
                container.innerHTML = '<div style="text-align: center; padding: 40px; color: var(--nx-text-secondary);">' +
                    '<i class="ri-file-list-3-line" style="font-size: 48px; display: block; margin-bottom: 12px;"></i>' +
                    '<div>暂无文档</div>' +
                    '<div style="font-size: 12px; margin-top: 8px;">点击上方按钮添加文档</div>' +
                '</div>';
                return;
            }
            
            var self = this;
            container.innerHTML = '<table class="doc-table">' +
                '<thead><tr>' +
                    '<th>文档名称</th>' +
                    '<th>来源</th>' +
                    '<th>分块数</th>' +
                    '<th>状态</th>' +
                    '<th>更新时间</th>' +
                    '<th>操作</th>' +
                '</tr></thead>' +
                '<tbody>' + this.documents.map(function(doc) {
                    var sourceIcon = doc.source === 'upload' ? 'ri-upload-line' : 
                                     (doc.source === 'url' ? 'ri-link' : 'ri-text');
                    var sourceText = doc.source === 'upload' ? '上传' : 
                                     (doc.source === 'url' ? 'URL导入' : '文本');
                    var statusClass = doc.status === 'indexed' ? 'status-success' : 
                                      (doc.status === 'indexing' ? 'status-warning' : 
                                      (doc.status === 'failed' ? 'status-error' : 'status-pending'));
                    var statusText = doc.status === 'indexed' ? '已索引' : 
                                     (doc.status === 'indexing' ? '索引中' : 
                                     (doc.status === 'failed' ? '失败' : '待处理'));
                    var updateTime = doc.updateTime ? self.formatTime(doc.updateTime) : '-';
                    
                    return '<tr>' +
                        '<td><div class="doc-title"><i class="ri-file-text-line"></i> ' + (doc.title || '未命名') + '</div></td>' +
                        '<td><span class="doc-source"><i class="' + sourceIcon + '"></i> ' + sourceText + '</span></td>' +
                        '<td>' + (doc.chunkCount || 0) + '</td>' +
                        '<td><span class="doc-status ' + statusClass + '">' + statusText + '</span></td>' +
                        '<td>' + updateTime + '</td>' +
                        '<td class="doc-actions">' +
                            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewDocument(\'' + doc.docId + '\')" title="查看"><i class="ri-eye-line"></i></button>' +
                            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="reindexDocument(\'' + doc.docId + '\')" title="重新索引"><i class="ri-refresh-line"></i></button>' +
                            '<button class="nx-btn nx-btn--ghost nx-btn--sm nx-btn--danger" onclick="deleteDocument(\'' + doc.docId + '\')" title="删除"><i class="ri-delete-bin-line"></i></button>' +
                        '</td>' +
                    '</tr>';
                }).join('') + '</tbody></table>';
        },
        
        viewDocument: function(docId) {
            var doc = this.documents.find(function(d) { return d.docId === docId; });
            if (!doc) return;
            
            document.getElementById('viewDocTitle').textContent = doc.title || '未命名文档';
            document.getElementById('viewDocContent').textContent = doc.content || '无内容预览';
            document.getElementById('viewDocMeta').innerHTML = 
                '<div><strong>文档ID:</strong> ' + doc.docId + '</div>' +
                '<div><strong>来源:</strong> ' + (doc.source || '-') + '</div>' +
                '<div><strong>分块数:</strong> ' + (doc.chunkCount || 0) + '</div>' +
                '<div><strong>状态:</strong> ' + (doc.status || '-') + '</div>' +
                (doc.sourceUrl ? '<div><strong>来源URL:</strong> <a href="' + doc.sourceUrl + '" target="_blank">' + doc.sourceUrl + '</a></div>' : '');
            
            document.getElementById('viewDocModal').classList.add('open');
        },
        
        reindexDocument: function(docId) {
            var self = this;
            var doc = this.documents.find(function(d) { return d.docId === docId; });
            if (!doc) return;
            
            if (!confirm('确定要重新索引文档 "' + (doc.title || docId) + '" 吗？')) return;
            
            ApiClient.post('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents/' + docId + '/reindex')
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('重新索引已启动');
                        self.loadDocuments();
                    } else {
                        alert('操作失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Reindex document error:', error);
                    alert('操作失败: ' + error.message);
                });
        },
        
        deleteDocument: function(docId) {
            var self = this;
            var doc = this.documents.find(function(d) { return d.docId === docId; });
            if (!doc) return;
            
            if (!confirm('确定要删除文档 "' + (doc.title || docId) + '" 吗？此操作不可恢复。')) return;
            
            ApiClient.delete('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents/' + docId)
                .then(function(result) {
                    if (result.status === 'success') {
                        self.documents = self.documents.filter(function(d) { return d.docId !== docId; });
                        self.renderDocuments();
                        if (self.currentKb) {
                            self.currentKb.documentCount = Math.max(0, (self.currentKb.documentCount || 1) - 1);
                        }
                    } else {
                        alert('删除失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Delete document error:', error);
                    alert('删除失败: ' + error.message);
                });
        },
        
        switchTab: function(tab) {
            document.querySelectorAll('.kb-tab').forEach(function(el) { el.classList.remove('active'); });
            document.querySelector('.kb-tab[data-tab="' + tab + '"]').classList.add('active');
            
            document.getElementById('tab-overview').style.display = tab === 'overview' ? 'block' : 'none';
            document.getElementById('tab-documents').style.display = tab === 'documents' ? 'block' : 'none';
            document.getElementById('tab-layer').style.display = tab === 'layer' ? 'block' : 'none';
            document.getElementById('tab-settings').style.display = tab === 'settings' ? 'block' : 'none';
            
            if (tab === 'documents' && this.currentKb) {
                this.loadDocuments();
            }
        },
        
        showCreateKbModal: function() {
            this.tags = [];
            this.renderTags();
            document.getElementById('newKbName').value = '';
            document.getElementById('newKbDesc').value = '';
            document.getElementById('newKbLayer').value = 'GENERAL';
            document.getElementById('newKbVisibility').value = 'private';
            document.getElementById('createKbModal').classList.add('open');
        },
        
        closeModal: function(modalId) {
            document.getElementById(modalId).classList.remove('open');
        },
        
        handleTagInput: function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                var input = event.target;
                var value = input.value.trim();
                if (value && !this.tags.includes(value)) {
                    this.tags.push(value);
                    this.renderTags();
                }
                input.value = '';
            }
        },
        
        renderTags: function() {
            var container = document.getElementById('tagInput');
            if (!container) return;
            var input = '<input type="text" placeholder="输入标签后按回车" onkeydown="handleTagInput(event)">';
            container.innerHTML = this.tags.map(function(tag) { 
                return '<span class="tag-item">' + tag + ' <span class="tag-remove" onclick="removeTag(\'' + tag + '\')">&times;</span></span>';
            }).join('') + input;
        },
        
        removeTag: function(tag) {
            this.tags = this.tags.filter(function(t) { return t !== tag; });
            this.renderTags();
        },
        
        createKb: function() {
            var self = this;
            var name = document.getElementById('newKbName').value;
            var description = document.getElementById('newKbDesc').value;
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
                tags: this.tags.slice()
            };
            
            newKb.layerConfig = {
                layer: layer,
                priority: layer === 'GENERAL' ? 0 : (layer === 'PROFESSIONAL' ? 1 : 2),
                enabled: true
            };
            
            ApiClient.post('/api/v1/knowledge-bases', newKb)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.knowledgeBases.push(result.data);
                        self.renderKbList();
                        self.closeModal('createKbModal');
                        self.selectKb(result.data.kbId);
                    } else {
                        alert('创建失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Create KB error:', error);
                    alert('创建失败: ' + error.message);
                });
        },
        
        rebuildIndex: function() {
            if (!this.currentKb) return;
            var self = this;
            
            ApiClient.post('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/rebuild-index')
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('索引重建已启动');
                        if (self.currentKb.indexStatus) {
                            self.currentKb.indexStatus.status = 'indexing';
                            self.currentKb.indexStatus.progress = 0;
                        }
                        self.selectKb(self.currentKb.kbId);
                    } else {
                        alert('重建索引失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Rebuild index error:', error);
                    alert('重建索引失败: ' + error.message);
                });
        },
        
        showEditKbModal: function() {
            if (!this.currentKb) return;
            
            document.getElementById('editKbName').value = this.currentKb.name || '';
            document.getElementById('editKbDesc').value = this.currentKb.description || '';
            document.getElementById('editKbVisibility').value = this.currentKb.visibility || 'private';
            
            var layer = 'GENERAL';
            if (this.currentKb.layerConfig && this.currentKb.layerConfig.layer) {
                layer = this.currentKb.layerConfig.layer;
            }
            document.getElementById('editKbLayer').value = layer;
            
            this.tags = this.currentKb.tags ? this.currentKb.tags.slice() : [];
            this.renderEditTags();
            
            document.getElementById('editKbModal').classList.add('open');
        },
        
        renderEditTags: function() {
            var container = document.getElementById('editTagInput');
            if (!container) return;
            var input = '<input type="text" placeholder="输入标签后按回车" onkeydown="handleEditTagInput(event)">';
            container.innerHTML = this.tags.map(function(tag) { 
                return '<span class="tag-item">' + tag + ' <span class="tag-remove" onclick="removeEditTag(\'' + tag + '\')">&times;</span></span>';
            }).join('') + input;
        },
        
        handleEditTagInput: function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                var input = event.target;
                var value = input.value.trim();
                if (value && !this.tags.includes(value)) {
                    this.tags.push(value);
                    this.renderEditTags();
                }
                input.value = '';
            }
        },
        
        removeEditTag: function(tag) {
            this.tags = this.tags.filter(function(t) { return t !== tag; });
            this.renderEditTags();
        },
        
        updateKb: function() {
            if (!this.currentKb) return;
            var self = this;
            
            var name = document.getElementById('editKbName').value;
            var description = document.getElementById('editKbDesc').value;
            var visibility = document.getElementById('editKbVisibility').value;
            var layer = document.getElementById('editKbLayer').value;
            
            if (!name) {
                alert('请输入知识库名称');
                return;
            }
            
            var updateData = {
                name: name,
                description: description,
                visibility: visibility,
                tags: this.tags.slice(),
                embeddingModel: this.currentKb.embeddingModel,
                chunkSize: this.currentKb.chunkSize,
                chunkOverlap: this.currentKb.chunkOverlap,
                layerConfig: {
                    layer: layer,
                    priority: layer === 'GENERAL' ? 0 : (layer === 'PROFESSIONAL' ? 1 : 2),
                    enabled: true
                }
            };
            
            ApiClient.put('/api/v1/knowledge-bases/' + this.currentKb.kbId, updateData)
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('知识库已更新');
                        self.closeModal('editKbModal');
                        var idx = self.knowledgeBases.findIndex(function(kb) { return kb.kbId === self.currentKb.kbId; });
                        if (idx >= 0) {
                            self.knowledgeBases[idx] = result.data;
                            self.currentKb = result.data;
                        }
                        self.selectKb(self.currentKb.kbId);
                    } else {
                        alert('更新失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Update KB error:', error);
                    alert('更新失败: ' + error.message);
                });
        },
        
        deleteKb: function() {
            if (!this.currentKb) return;
            var self = this;
            
            if (confirm('确定要删除知识库 "' + this.currentKb.name + '" 吗？此操作不可恢复。')) {
                ApiClient.delete('/api/v1/knowledge-bases/' + this.currentKb.kbId)
                    .then(function(result) {
                        if (result.status === 'success') {
                            self.knowledgeBases = self.knowledgeBases.filter(function(kb) { return kb.kbId !== self.currentKb.kbId; });
                            self.currentKb = null;
                            document.getElementById('emptyState').style.display = 'block';
                            document.getElementById('kbDetail').style.display = 'none';
                            self.renderKbList();
                        } else {
                            alert('删除失败: ' + (result.message || '未知错误'));
                        }
                    })
                    .catch(function(error) {
                        console.error('Delete KB error:', error);
                        alert('删除失败: ' + error.message);
                    });
            }
        },
        
        saveSettings: function() {
            if (!this.currentKb) return;
            var self = this;
            
            var updateData = {
                name: this.currentKb.name,
                description: this.currentKb.description,
                visibility: document.getElementById('visibility').value,
                embeddingModel: document.getElementById('embeddingModel').value,
                chunkSize: parseInt(document.getElementById('chunkSize').value),
                chunkOverlap: parseInt(document.getElementById('chunkOverlap').value),
                layerConfig: this.currentKb.layerConfig
            };
            
            ApiClient.put('/api/v1/knowledge-bases/' + this.currentKb.kbId, updateData)
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('设置已保存');
                        var idx = self.knowledgeBases.findIndex(function(kb) { return kb.kbId === self.currentKb.kbId; });
                        if (idx >= 0) {
                            self.knowledgeBases[idx] = result.data;
                            self.currentKb = result.data;
                        }
                    } else {
                        alert('保存失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Save settings error:', error);
                    alert('保存失败: ' + error.message);
                });
        },
        
        showAddTextModal: function() {
            if (!this.currentKb) {
                alert('请先选择知识库');
                return;
            }
            this.docTags = [];
            document.getElementById('textDocTitle').value = '';
            document.getElementById('textDocContent').value = '';
            this.renderDocTags();
            document.getElementById('addTextModal').classList.add('open');
        },
        
        renderDocTags: function() {
            var container = document.getElementById('docTagInput');
            if (!container) return;
            var input = '<input type="text" placeholder="输入标签后按回车" onkeydown="handleDocTagInput(event)">';
            container.innerHTML = this.docTags.map(function(tag) { 
                return '<span class="tag-item">' + tag + ' <span class="tag-remove" onclick="removeDocTag(\'' + tag + '\')">&times;</span></span>';
            }).join('') + input;
        },
        
        handleDocTagInput: function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                var input = event.target;
                var value = input.value.trim();
                if (value && !this.docTags.includes(value)) {
                    this.docTags.push(value);
                    this.renderDocTags();
                }
                input.value = '';
            }
        },
        
        removeDocTag: function(tag) {
            this.docTags = this.docTags.filter(function(t) { return t !== tag; });
            this.renderDocTags();
        },
        
        addTextDocument: function() {
            var self = this;
            var title = document.getElementById('textDocTitle').value;
            var content = document.getElementById('textDocContent').value;
            
            if (!title) {
                alert('请输入文档标题');
                return;
            }
            if (!content) {
                alert('请输入文档内容');
                return;
            }
            
            var docData = {
                title: title,
                content: content,
                tags: this.docTags.slice()
            };
            
            ApiClient.post('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents/text', docData)
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('文档已添加，正在后台索引');
                        self.closeModal('addTextModal');
                        self.loadDocuments();
                        if (self.currentKb) {
                            self.currentKb.documentCount = (self.currentKb.documentCount || 0) + 1;
                        }
                    } else {
                        alert('添加失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Add text document error:', error);
                    alert('添加失败: ' + error.message);
                });
        },
        
        showUploadModal: function() {
            if (!this.currentKb) {
                alert('请先选择知识库');
                return;
            }
            this.docTags = [];
            document.getElementById('uploadFileInput').value = '';
            document.getElementById('uploadDocTitle').value = '';
            this.renderUploadTags();
            document.getElementById('uploadModal').classList.add('open');
        },
        
        renderUploadTags: function() {
            var container = document.getElementById('uploadTagInput');
            if (!container) return;
            var input = '<input type="text" placeholder="输入标签后按回车" onkeydown="handleUploadTagInput(event)">';
            container.innerHTML = this.docTags.map(function(tag) { 
                return '<span class="tag-item">' + tag + ' <span class="tag-remove" onclick="removeUploadTag(\'' + tag + '\')">&times;</span></span>';
            }).join('') + input;
        },
        
        handleUploadTagInput: function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                var input = event.target;
                var value = input.value.trim();
                if (value && !this.docTags.includes(value)) {
                    this.docTags.push(value);
                    this.renderUploadTags();
                }
                input.value = '';
            }
        },
        
        removeUploadTag: function(tag) {
            this.docTags = this.docTags.filter(function(t) { return t !== tag; });
            this.renderUploadTags();
        },
        
        uploadFile: function() {
            var self = this;
            var fileInput = document.getElementById('uploadFileInput');
            var title = document.getElementById('uploadDocTitle').value;
            
            if (!fileInput.files || fileInput.files.length === 0) {
                alert('请选择要上传的文件');
                return;
            }
            
            var file = fileInput.files[0];
            var formData = new FormData();
            formData.append('file', file);
            if (title) {
                formData.append('title', title);
            }
            formData.append('tags', this.docTags.join(','));
            
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents/upload');
            xhr.onload = function() {
                if (xhr.status === 200) {
                    var result = JSON.parse(xhr.responseText);
                    if (result.status === 'success') {
                        alert('文件已上传，正在后台处理');
                        self.closeModal('uploadModal');
                        self.loadDocuments();
                        if (self.currentKb) {
                            self.currentKb.documentCount = (self.currentKb.documentCount || 0) + 1;
                        }
                    } else {
                        alert('上传失败: ' + (result.message || '未知错误'));
                    }
                } else {
                    alert('上传失败: HTTP ' + xhr.status);
                }
            };
            xhr.onerror = function() {
                alert('上传失败: 网络错误');
            };
            xhr.send(formData);
        },
        
        showUrlImportModal: function() {
            if (!this.currentKb) {
                alert('请先选择知识库');
                return;
            }
            this.docTags = [];
            document.getElementById('urlInput').value = '';
            document.getElementById('urlDocTitle').value = '';
            this.renderUrlTags();
            document.getElementById('urlImportModal').classList.add('open');
        },
        
        renderUrlTags: function() {
            var container = document.getElementById('urlTagInput');
            if (!container) return;
            var input = '<input type="text" placeholder="输入标签后按回车" onkeydown="handleUrlTagInput(event)">';
            container.innerHTML = this.docTags.map(function(tag) { 
                return '<span class="tag-item">' + tag + ' <span class="tag-remove" onclick="removeUrlTag(\'' + tag + '\')">&times;</span></span>';
            }).join('') + input;
        },
        
        handleUrlTagInput: function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                var input = event.target;
                var value = input.value.trim();
                if (value && !this.docTags.includes(value)) {
                    this.docTags.push(value);
                    this.renderUrlTags();
                }
                input.value = '';
            }
        },
        
        removeUrlTag: function(tag) {
            this.docTags = this.docTags.filter(function(t) { return t !== tag; });
            this.renderUrlTags();
        },
        
        importFromUrl: function() {
            var self = this;
            var url = document.getElementById('urlInput').value;
            var title = document.getElementById('urlDocTitle').value;
            
            if (!url) {
                alert('请输入URL地址');
                return;
            }
            
            if (!url.startsWith('http://') && !url.startsWith('https://')) {
                alert('请输入有效的URL地址（以http://或https://开头）');
                return;
            }
            
            var docData = {
                url: url,
                title: title || null,
                tags: this.docTags.slice()
            };
            
            ApiClient.post('/api/v1/knowledge-bases/' + this.currentKb.kbId + '/documents/url', docData)
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('URL导入已启动，正在后台处理');
                        self.closeModal('urlImportModal');
                        self.loadDocuments();
                        if (self.currentKb) {
                            self.currentKb.documentCount = (self.currentKb.documentCount || 0) + 1;
                        }
                    } else {
                        alert('导入失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('URL import error:', error);
                    alert('导入失败: ' + error.message);
                });
        },
        
        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        }
    };
    
    window.loadKnowledgeBases = function() { KnowledgeBase.loadKnowledgeBases(); };
    window.filterKbList = function() { KnowledgeBase.filterKbList(); };
    window.selectKb = function(kbId) { KnowledgeBase.selectKb(kbId); };
    window.switchTab = function(tab) { KnowledgeBase.switchTab(tab); };
    window.showCreateKbModal = function() { KnowledgeBase.showCreateKbModal(); };
    window.closeModal = function(modalId) { KnowledgeBase.closeModal(modalId); };
    window.handleTagInput = function(event) { KnowledgeBase.handleTagInput(event); };
    window.removeTag = function(tag) { KnowledgeBase.removeTag(tag); };
    window.createKb = function() { KnowledgeBase.createKb(); };
    window.rebuildIndex = function() { KnowledgeBase.rebuildIndex(); };
    window.showEditKbModal = function() { KnowledgeBase.showEditKbModal(); };
    window.deleteKb = function() { KnowledgeBase.deleteKb(); };
    window.saveSettings = function() { KnowledgeBase.saveSettings(); };
    
    window.handleEditTagInput = function(event) { KnowledgeBase.handleEditTagInput(event); };
    window.removeEditTag = function(tag) { KnowledgeBase.removeEditTag(tag); };
    window.updateKb = function() { KnowledgeBase.updateKb(); };
    
    window.showAddTextModal = function() { KnowledgeBase.showAddTextModal(); };
    window.handleDocTagInput = function(event) { KnowledgeBase.handleDocTagInput(event); };
    window.removeDocTag = function(tag) { KnowledgeBase.removeDocTag(tag); };
    window.addTextDocument = function() { KnowledgeBase.addTextDocument(); };
    
    window.showUploadModal = function() { KnowledgeBase.showUploadModal(); };
    window.handleUploadTagInput = function(event) { KnowledgeBase.handleUploadTagInput(event); };
    window.removeUploadTag = function(tag) { KnowledgeBase.removeUploadTag(tag); };
    window.uploadFile = function() { KnowledgeBase.uploadFile(); };
    
    window.showUrlImportModal = function() { KnowledgeBase.showUrlImportModal(); };
    window.handleUrlTagInput = function(event) { KnowledgeBase.handleUrlTagInput(event); };
    window.removeUrlTag = function(tag) { KnowledgeBase.removeUrlTag(tag); };
    window.importFromUrl = function() { KnowledgeBase.importFromUrl(); };
    
    window.viewDocument = function(docId) { KnowledgeBase.viewDocument(docId); };
    window.reindexDocument = function(docId) { KnowledgeBase.reindexDocument(docId); };
    window.deleteDocument = function(docId) { KnowledgeBase.deleteDocument(docId); };
    
    document.addEventListener('DOMContentLoaded', KnowledgeBase.init.bind(KnowledgeBase));
})();
