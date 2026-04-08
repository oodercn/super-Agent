(function() {
    'use strict';
    
    var LlmKnowledgeConfigPage = {
        embeddingConfig: null,
        dictionaryTerms: [],
        synonymMappings: [],
        llmInterfaces: [],
        promptTemplates: [],
        
        init: function() {
            this.loadEmbeddingConfig();
            this.loadDictionaries();
            this.loadSynonyms();
            this.loadInterfaces();
            this.loadPromptTemplate();
        },
        
        loadEmbeddingConfig: function() {
            var self = this;
            ApiClient.get('/api/v1/embedding/config')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.embeddingConfig = result.data;
                        var modelSelect = document.getElementById('embeddingModel');
                        if (modelSelect) modelSelect.value = result.data.currentModel || 'text-embedding-ada-002';
                        var dimInput = document.getElementById('vectorDimension');
                        if (dimInput) dimInput.value = result.data.dimensions || 1536;
                        var chunkInput = document.getElementById('chunkSize');
                        if (chunkInput) chunkInput.value = result.data.defaultChunkSize || 500;
                        var overlapInput = document.getElementById('chunkOverlap');
                        if (overlapInput) overlapInput.value = result.data.defaultChunkOverlap || 50;
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load embedding config:', error);
                });
        },
        
        loadDictionaries: function() {
            var self = this;
            ApiClient.get('/api/v1/llm-knowledge-config/dictionaries')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.dictionaryTerms = result.data;
                        self.renderDictionaries();
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load dictionaries:', error);
                });
        },
        
        renderDictionaries: function() {
            var container = document.getElementById('dictTableBody');
            if (!container) return;
            
            if (!this.dictionaryTerms || this.dictionaryTerms.length === 0) {
                container.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--nx-text-secondary);">暂无术语</td></tr>';
                return;
            }
            
            var categoryNames = { tech: '技术术语', business: '业务术语', ai: 'AI术语', other: '其他' };
            
            container.innerHTML = this.dictionaryTerms.map(function(term) {
                return '<tr>' +
                    '<td><span class="dict-key">' + (term.term || '') + '</span></td>' +
                    '<td class="dict-desc">' + (term.fullName || term.description || '') + '</td>' +
                    '<td><span class="layer-badge ' + (term.category === 'ai' ? 'professional' : 'general') + '">' + (categoryNames[term.category] || term.category || '其他') + '</span></td>' +
                    '<td>' + (term.kbName || '-') + '</td>' +
                    '<td class="dict-actions">' +
                        '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="editDictTerm(\'' + term.termId + '\')"><i class="ri-edit-line"></i></button>' +
                        '<button class="nx-btn nx-btn--ghost nx-btn--sm nx-btn--danger" onclick="deleteDictTerm(\'' + term.termId + '\')"><i class="ri-delete-bin-line"></i></button>' +
                    '</td>' +
                '</tr>';
            }).join('');
        },
        
        loadSynonyms: function() {
            var self = this;
            ApiClient.get('/api/v1/llm-knowledge-config/synonyms')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.synonymMappings = result.data;
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load synonyms:', error);
                });
        },
        
        loadInterfaces: function() {
            var self = this;
            ApiClient.get('/api/v1/llm-knowledge-config/interfaces')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.llmInterfaces = result.data;
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load interfaces:', error);
                });
        },
        
        loadPromptTemplate: function() {
            var templateEl = document.getElementById('promptTemplate');
            if (!templateEl) return;
            
            ApiClient.get('/api/v1/llm-knowledge-config/prompt-templates/default')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data && result.data.content) {
                        templateEl.value = result.data.content;
                    }
                })
                .catch(function(error) {
                    console.warn('Failed to load prompt template:', error);
                });
        },
        
        testEmbedding: function() {
            var textEl = document.getElementById('testText');
            var modelEl = document.getElementById('embeddingModel');
            var text = textEl ? textEl.value : '';
            var modelId = modelEl ? modelEl.value : 'text-embedding-ada-002';
            
            if (!text) {
                alert('请输入测试文本');
                return;
            }
            
            var result = document.getElementById('testResult');
            if (!result) return;
            
            result.style.display = 'block';
            result.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 正在向量化...';
            
            ApiClient.post('/api/v1/embedding/test', { modelId: modelId, text: text })
                .then(function(response) {
                    if (response && response.status === 'success' && response.data) {
                        var data = response.data;
                        result.innerHTML = '<div style="margin-bottom: 8px;"><strong>向量维度:</strong> ' + (data.dimensions || 1536) + '</div>' +
                            '<div style="margin-bottom: 8px;"><strong>前10维:</strong> [' + (data.sampleVector || []).map(function(v) { return v.toFixed(4); }).join(', ') + ', ...]</div>' +
                            '<div><strong>文本长度:</strong> ' + (data.textLength || text.length) + ' 字符</div>';
                    } else {
                        result.innerHTML = '<div style="color: red;">测试失败: ' + (response.message || '未知错误') + '</div>';
                    }
                })
                .catch(function(error) {
                    result.innerHTML = '<div style="color: red;">测试失败: ' + (error.message || '网络错误') + '</div>';
                });
        },
        
        addDictionaryTerm: function(term) {
            var self = this;
            return ApiClient.post('/api/v1/llm-knowledge-config/dictionaries', term)
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        self.dictionaryTerms.push(result.data);
                        self.renderDictionaries();
                        return true;
                    }
                    alert('添加失败: ' + (result && result.message ? result.message : '未知错误'));
                    return false;
                })
                .catch(function(error) {
                    alert('添加失败: ' + (error.message || '网络错误'));
                    return false;
                });
        },
        
        deleteDictionaryTerm: function(termId) {
            var self = this;
            return ApiClient.delete('/api/v1/llm-knowledge-config/dictionaries/' + termId)
                .then(function(result) {
                    if (result && result.status === 'success') {
                        self.dictionaryTerms = self.dictionaryTerms.filter(function(t) { return t.termId !== termId; });
                        self.renderDictionaries();
                        return true;
                    }
                    return false;
                })
                .catch(function(error) {
                    console.error('Delete failed:', error);
                    return false;
                });
        },
        
        savePromptTemplate: function() {
            var templateEl = document.getElementById('promptTemplate');
            var content = templateEl ? templateEl.value : '';
            
            return ApiClient.post('/api/v1/llm-knowledge-config/prompt-templates', {
                name: '知识增强回答模板',
                description: '用于知识检索后的回答生成',
                content: content
            }).then(function(result) {
                if (result && result.status === 'success') {
                    alert('提示词模板已保存');
                    return true;
                }
                alert('保存失败: ' + (result && result.message ? result.message : '未知错误'));
                return false;
            }).catch(function(error) {
                alert('保存失败: ' + (error.message || '网络错误'));
                return false;
            });
        }
    };
    
    window.switchConfigTab = function(tab) {
        document.querySelectorAll('.config-tab').forEach(function(el) { el.classList.remove('active'); });
        var activeTab = document.querySelector('.config-tab[onclick="switchConfigTab(\'' + tab + '\')"]');
        if (activeTab) activeTab.classList.add('active');
        
        document.querySelectorAll('.tab-content').forEach(function(el) { el.classList.remove('active'); });
        var tabContent = document.getElementById('tab-' + tab);
        if (tabContent) tabContent.classList.add('active');
    };
    
    window.toggleInterface = function(header) {
        var body = header.nextElementSibling;
        if (body) body.classList.toggle('open');
    };
    
    window.testEmbedding = function() {
        LlmKnowledgeConfigPage.testEmbedding();
    };
    
    window.showAddDictModal = function() {
        var modal = document.getElementById('addDictModal');
        if (modal) modal.classList.add('open');
    };
    
    window.showAddSynonymModal = function() {
        var modalHtml = `
            <div class="modal-overlay" id="synonymModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
                <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 450px; width: 90%;">
                    <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="margin: 0; font-size: 16px;"><i class="ri-exchange-line"></i> 添加同义词映射</h3>
                        <button class="modal-close" onclick="closeSynonymModal()" style="background: none; border: none; font-size: 20px; cursor: pointer;"><i class="ri-close-line"></i></button>
                    </div>
                    <div class="modal-body" style="padding: 20px;">
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">原词 <span class="nx-text-error">*</span></label>
                            <input type="text" class="nx-input" id="synonymSource" placeholder="输入原词">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">同义词 <span class="nx-text-error">*</span></label>
                            <input type="text" class="nx-input" id="synonymTarget" placeholder="输入同义词（多个用逗号分隔）">
                        </div>
                        <div class="nx-form-group">
                            <label class="nx-form-label">说明</label>
                            <input type="text" class="nx-input" id="synonymDesc" placeholder="映射说明（可选）">
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: flex-end; gap: 8px;">
                        <button class="nx-btn nx-btn--secondary" onclick="closeSynonymModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="submitSynonym()">添加</button>
                    </div>
                </div>
            </div>
        `;
        
        var existing = document.getElementById('synonymModal');
        if (existing) existing.remove();
        document.body.insertAdjacentHTML('beforeend', modalHtml);
    };
    
    window.closeSynonymModal = function() {
        var modal = document.getElementById('synonymModal');
        if (modal) modal.remove();
    };
    
    window.submitSynonym = function() {
        var source = document.getElementById('synonymSource').value.trim();
        var target = document.getElementById('synonymTarget').value.trim();
        var desc = document.getElementById('synonymDesc').value.trim();
        
        if (!source || !target) {
            alert('请填写原词和同义词');
            return;
        }
        
        fetch('/api/v1/llm-knowledge-config/synonyms', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ source: source, targets: target.split(',').map(function(t) { return t.trim(); }), description: desc })
        })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    closeSynonymModal();
                    alert('同义词映射添加成功');
                    LlmKnowledgeConfigPage.loadSynonyms();
                } else {
                    alert('添加失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('Failed to add synonym:', error);
                alert('添加失败');
            });
    };
    
    window.showAddInterfaceModal = function() {
        var modalHtml = `
            <div class="modal-overlay" id="interfaceModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
                <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 550px; width: 90%;">
                    <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="margin: 0; font-size: 16px;"><i class="ri-code-box-line"></i> 添加接口定义</h3>
                        <button class="modal-close" onclick="closeInterfaceModal()" style="background: none; border: none; font-size: 20px; cursor: pointer;"><i class="ri-close-line"></i></button>
                    </div>
                    <div class="modal-body" style="padding: 20px;">
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">接口名称 <span class="nx-text-error">*</span></label>
                            <input type="text" class="nx-input" id="interfaceName" placeholder="例如: getUserInfo">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">接口描述</label>
                            <input type="text" class="nx-input" id="interfaceDesc" placeholder="接口用途说明">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">参数定义 (JSON)</label>
                            <textarea class="nx-input" id="interfaceParams" rows="4" placeholder='{"param1": {"type": "string", "desc": "参数说明"}}'></textarea>
                        </div>
                        <div class="nx-form-group">
                            <label class="nx-form-label">返回值定义 (JSON)</label>
                            <textarea class="nx-input" id="interfaceReturn" rows="3" placeholder='{"type": "object", "properties": {...}}'></textarea>
                        </div>
                    </div>
                    <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: flex-end; gap: 8px;">
                        <button class="nx-btn nx-btn--secondary" onclick="closeInterfaceModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="submitInterface()">添加</button>
                    </div>
                </div>
            </div>
        `;
        
        var existing = document.getElementById('interfaceModal');
        if (existing) existing.remove();
        document.body.insertAdjacentHTML('beforeend', modalHtml);
    };
    
    window.closeInterfaceModal = function() {
        var modal = document.getElementById('interfaceModal');
        if (modal) modal.remove();
    };
    
    window.submitInterface = function() {
        var name = document.getElementById('interfaceName').value.trim();
        var desc = document.getElementById('interfaceDesc').value.trim();
        var paramsStr = document.getElementById('interfaceParams').value.trim();
        var returnStr = document.getElementById('interfaceReturn').value.trim();
        
        if (!name) {
            alert('请输入接口名称');
            return;
        }
        
        var params = {}, returnDef = {};
        try {
            if (paramsStr) params = JSON.parse(paramsStr);
            if (returnStr) returnDef = JSON.parse(returnStr);
        } catch (e) {
            alert('JSON格式错误: ' + e.message);
            return;
        }
        
        fetch('/api/v1/llm-knowledge-config/interfaces', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: name, description: desc, parameters: params, returnType: returnDef })
        })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    closeInterfaceModal();
                    alert('接口定义添加成功');
                    LlmKnowledgeConfigPage.loadInterfaces();
                } else {
                    alert('添加失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('Failed to add interface:', error);
                alert('添加失败');
            });
    };
    
    window.closeModal = function(id) {
        var modal = document.getElementById(id);
        if (modal) modal.classList.remove('open');
    };
    
    window.addDictTerm = function() {
        var termEl = document.getElementById('dictTerm');
        var fullEl = document.getElementById('dictFull');
        var descEl = document.getElementById('dictDesc');
        var categoryEl = document.getElementById('dictCategory');
        
        var term = termEl ? termEl.value.trim() : '';
        var full = fullEl ? fullEl.value.trim() : '';
        
        if (!term || !full) {
            alert('请填写术语和全称');
            return;
        }
        
        LlmKnowledgeConfigPage.addDictionaryTerm({
            term: term,
            fullName: full,
            description: descEl ? descEl.value.trim() : '',
            category: categoryEl ? categoryEl.value : 'other'
        }).then(function(success) {
            if (success) {
                closeModal('addDictModal');
                if (termEl) termEl.value = '';
                if (fullEl) fullEl.value = '';
                if (descEl) descEl.value = '';
            }
        });
    };
    
    window.editDictTerm = function(termId) {
        console.log('Edit dict term:', termId);
        alert('编辑功能开发中...');
    };
    
    window.deleteDictTerm = function(termId) {
        if (confirm('确定要删除此术语吗？')) {
            LlmKnowledgeConfigPage.deleteDictionaryTerm(termId);
        }
    };
    
    window.savePromptTemplate = function() {
        LlmKnowledgeConfigPage.savePromptTemplate();
    };
    
    window.resetPromptTemplate = function() {
        if (confirm('确定要重置为默认模板吗？')) {
            ApiClient.get('/api/v1/llm-knowledge-config/prompt-templates/default')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        var templateEl = document.getElementById('promptTemplate');
                        if (templateEl) templateEl.value = result.data.content || '';
                    }
                })
                .catch(function(error) {
                    console.error('Reset failed:', error);
                });
        }
    };
    
    document.addEventListener('DOMContentLoaded', LlmKnowledgeConfigPage.init.bind(LlmKnowledgeConfigPage));
})();
