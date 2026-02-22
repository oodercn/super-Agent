(function(global) {
    'use strict';

    var LlmFunctions = {
        history: [],
        functionCounter: 1,

        init: function() {
            this.bindEvents();
            this.loadHistory();
        },

        bindEvents: function() {
            var self = this;

            document.getElementById('add-function-btn').addEventListener('click', function() {
                self.addFunction();
            });

            document.getElementById('execute-btn').addEventListener('click', function() {
                self.executeFunctions();
            });

            document.getElementById('clear-btn').addEventListener('click', function() {
                self.clearAll();
            });

            document.getElementById('clear-history-btn').addEventListener('click', function() {
                self.clearHistory();
            });

            document.getElementById('functions-list').addEventListener('click', function(e) {
                var deleteBtn = e.target.closest('.function-delete');
                if (deleteBtn) {
                    var card = deleteBtn.closest('.function-card');
                    if (card) {
                        card.remove();
                    }
                }
            });

            document.getElementById('functions-list').addEventListener('input', function(e) {
                var card = e.target.closest('.function-card');
                if (card && e.target.classList.contains('function-name-input')) {
                    var nameDisplay = card.querySelector('.function-name');
                    if (nameDisplay) {
                        nameDisplay.textContent = e.target.value || 'unnamed';
                    }
                }
            });
        },

        addFunction: function() {
            this.functionCounter++;
            var id = this.functionCounter;
            
            var html = '\
                <div class="function-card" data-id="' + id + '">\
                    <div class="function-header">\
                        <span class="function-name">new_function_' + id + '</span>\
                        <button class="nx-btn nx-btn--ghost nx-btn--sm function-delete" title="删除">\
                            <i class="ri-close-line"></i>\
                        </button>\
                    </div>\
                    <div class="function-body">\
                        <div class="form-group">\
                            <label>函数名称</label>\
                            <input type="text" class="form-input function-name-input" value="new_function_' + id + '" placeholder="函数名称">\
                        </div>\
                        <div class="form-group">\
                            <label>描述</label>\
                            <input type="text" class="form-input function-desc-input" placeholder="函数描述">\
                        </div>\
                        <div class="form-group">\
                            <label>参数 (JSON Schema)</label>\
                            <textarea class="form-textarea function-params-input" rows="4" placeholder=\'{"type": "object", "properties": {...}}\'></textarea>\
                        </div>\
                    </div>\
                </div>\
            ';
            
            document.getElementById('functions-list').insertAdjacentHTML('beforeend', html);
        },

        getFunctions: function() {
            var functions = [];
            var cards = document.querySelectorAll('.function-card');
            
            cards.forEach(function(card) {
                var name = card.querySelector('.function-name-input').value.trim();
                var description = card.querySelector('.function-desc-input').value.trim();
                var paramsStr = card.querySelector('.function-params-input').value.trim();
                
                if (name) {
                    var func = {
                        name: name,
                        description: description || name
                    };
                    
                    if (paramsStr) {
                        try {
                            func.parameters = JSON.parse(paramsStr);
                        } catch (e) {
                            console.warn('Invalid JSON for function parameters:', name);
                        }
                    }
                    
                    functions.push(func);
                }
            });
            
            return functions;
        },

        executeFunctions: async function() {
            var self = this;
            var prompt = document.getElementById('prompt-input').value.trim();
            var functions = this.getFunctions();
            
            if (!prompt) {
                alert('请输入提示词');
                return;
            }
            
            if (functions.length === 0) {
                alert('请至少定义一个函数');
                return;
            }
            
            var statusEl = document.getElementById('result-status');
            statusEl.className = 'result-status loading';
            statusEl.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 执行中...';
            
            document.getElementById('result-placeholder').style.display = 'none';
            document.getElementById('result-content').style.display = 'block';
            document.getElementById('user-input-display').textContent = prompt;
            document.getElementById('function-call-block').style.display = 'none';
            document.getElementById('function-result-block').style.display = 'none';
            document.getElementById('ai-response').textContent = '正在处理...';
            
            try {
                var response = await fetch('/api/llm/chat/functions', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        prompt: prompt,
                        functions: functions
                    })
                });
                
                var data = await response.json();
                
                if (data.requestStatus === 200 && data.data) {
                    statusEl.className = 'result-status success';
                    statusEl.innerHTML = '<i class="ri-check-line"></i> 执行成功';
                    
                    var resultData = data.data;
                    
                    if (resultData.functionCalls && resultData.functionCalls.length > 0) {
                        document.getElementById('function-call-block').style.display = 'block';
                        var callHtml = resultData.functionCalls.map(function(call) {
                            return '\
                                <div class="function-call-item">\
                                    <div class="function-call-name">' + self.escapeHtml(call.name) + '</div>\
                                    <div class="function-call-args">' + self.escapeHtml(JSON.stringify(call.arguments, null, 2)) + '</div>\
                                </div>\
                            ';
                        }).join('');
                        document.getElementById('function-call-display').innerHTML = callHtml;
                    }
                    
                    if (resultData.functionResults) {
                        document.getElementById('function-result-block').style.display = 'block';
                        document.getElementById('function-result-display').textContent = 
                            JSON.stringify(resultData.functionResults, null, 2);
                    }
                    
                    document.getElementById('ai-response').textContent = resultData.response || '无响应';
                    
                    self.addToHistory(prompt, functions, resultData);
                } else {
                    statusEl.className = 'result-status error';
                    statusEl.innerHTML = '<i class="ri-error-warning-line"></i> 执行失败';
                    document.getElementById('ai-response').textContent = '错误: ' + (data.message || '未知错误');
                }
            } catch (error) {
                console.error('Function calling error:', error);
                statusEl.className = 'result-status error';
                statusEl.innerHTML = '<i class="ri-error-warning-line"></i> 执行失败';
                document.getElementById('ai-response').textContent = '错误: ' + error.message;
            }
        },

        clearAll: function() {
            document.getElementById('prompt-input').value = '';
            document.getElementById('result-placeholder').style.display = 'flex';
            document.getElementById('result-content').style.display = 'none';
            document.getElementById('result-status').innerHTML = '';
            document.getElementById('result-status').className = 'result-status';
        },

        addToHistory: function(prompt, functions, result) {
            var item = {
                prompt: prompt.substring(0, 100) + (prompt.length > 100 ? '...' : ''),
                fullPrompt: prompt,
                functions: functions.map(function(f) { return f.name; }),
                timestamp: Date.now()
            };
            
            this.history.unshift(item);
            if (this.history.length > 20) {
                this.history.pop();
            }
            
            this.saveHistory();
            this.renderHistory();
        },

        renderHistory: function() {
            var container = document.getElementById('history-list');
            var self = this;
            
            if (this.history.length === 0) {
                container.innerHTML = '<div class="history-empty">暂无历史记录</div>';
                return;
            }
            
            container.innerHTML = this.history.map(function(item, index) {
                var funcTags = item.functions.map(function(f) {
                    return '<span class="history-function-tag">' + self.escapeHtml(f) + '</span>';
                }).join('');
                
                return '\
                    <div class="history-item" data-index="' + index + '">\
                        <div class="history-prompt">' + self.escapeHtml(item.prompt) + '</div>\
                        <div class="history-meta">\
                            <div class="history-functions">' + funcTags + '</div>\
                            <span>' + self.formatTime(item.timestamp) + '</span>\
                        </div>\
                    </div>\
                ';
            }).join('');
            
            container.querySelectorAll('.history-item').forEach(function(el) {
                el.addEventListener('click', function() {
                    var index = parseInt(this.getAttribute('data-index'));
                    var item = self.history[index];
                    document.getElementById('prompt-input').value = item.fullPrompt;
                });
            });
        },

        loadHistory: function() {
            var saved = localStorage.getItem('llm-functions-history');
            if (saved) {
                this.history = JSON.parse(saved);
                this.renderHistory();
            }
        },

        saveHistory: function() {
            localStorage.setItem('llm-functions-history', JSON.stringify(this.history));
        },

        clearHistory: function() {
            if (confirm('确定要清空历史记录吗？')) {
                this.history = [];
                this.saveHistory();
                this.renderHistory();
            }
        },

        escapeHtml: function(text) {
            var div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        },

        formatTime: function(timestamp) {
            var date = new Date(timestamp);
            return date.getHours() + ':' + String(date.getMinutes()).padStart(2, '0');
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            LlmFunctions.init();
        });
    } else {
        LlmFunctions.init();
    }

    global.LlmFunctions = LlmFunctions;
})(window);
