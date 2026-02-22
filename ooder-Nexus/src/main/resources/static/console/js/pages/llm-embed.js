(function(global) {
    'use strict';

    var LlmEmbed = {
        history: [],
        currentResult: null,
        isBatchMode: false,

        init: function() {
            this.bindEvents();
            this.loadHistory();
        },

        bindEvents: function() {
            var self = this;

            document.getElementById('input-text').addEventListener('input', function() {
                document.getElementById('input-char-count').textContent = this.value.length;
            });

            document.getElementById('batch-text').addEventListener('input', function() {
                var lines = this.value.split('\n').filter(function(l) { return l.trim(); });
                document.getElementById('input-char-count').textContent = lines.length + ' 条';
            });

            document.getElementById('embed-btn').addEventListener('click', function() {
                self.generateEmbedding();
            });

            document.getElementById('batch-btn').addEventListener('click', function() {
                self.toggleBatchMode();
            });

            document.getElementById('copy-btn').addEventListener('click', function() {
                self.copyResult();
            });

            document.getElementById('download-btn').addEventListener('click', function() {
                self.downloadResult();
            });

            document.getElementById('clear-history-btn').addEventListener('click', function() {
                self.clearHistory();
            });
        },

        toggleBatchMode: function() {
            this.isBatchMode = !this.isBatchMode;
            var batchInput = document.getElementById('batch-input');
            var singleInput = document.getElementById('input-text');
            var batchBtn = document.getElementById('batch-btn');

            if (this.isBatchMode) {
                batchInput.style.display = 'block';
                singleInput.style.display = 'none';
                batchBtn.classList.add('nx-btn--primary');
                batchBtn.classList.remove('nx-btn--secondary');
            } else {
                batchInput.style.display = 'none';
                singleInput.style.display = 'block';
                batchBtn.classList.add('nx-btn--secondary');
                batchBtn.classList.remove('nx-btn--primary');
            }
        },

        generateEmbedding: async function() {
            var self = this;
            var texts = [];

            if (this.isBatchMode) {
                var batchText = document.getElementById('batch-text').value;
                texts = batchText.split('\n').filter(function(l) { return l.trim(); });
            } else {
                var text = document.getElementById('input-text').value.trim();
                if (!text) {
                    alert('请输入文本');
                    return;
                }
                texts = [text];
            }

            try {
                var response;
                if (texts.length === 1) {
                    response = await fetch('/api/llm/embed', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ text: texts[0] })
                    });
                } else {
                    response = await fetch('/api/llm/embed/batch', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ texts: texts })
                    });
                }

                var data = await response.json();

                if (data.requestStatus === 200 && data.data) {
                    if (texts.length === 1) {
                        self.currentResult = data.data.embedding;
                        self.displayResult(data.data.embedding, data.data.dimensions);
                        self.addToHistory(texts[0], data.data.embedding);
                    } else {
                        self.currentResult = data.data.embeddings;
                        self.displayBatchResult(data.data.embeddings, texts);
                        texts.forEach(function(text, i) {
                            self.addToHistory(text, data.data.embeddings[i]);
                        });
                    }
                } else {
                    alert('生成嵌入失败: ' + data.message);
                }
            } catch (error) {
                console.error('Embed error:', error);
                alert('生成嵌入失败: ' + error.message);
            }
        },

        displayResult: function(embedding, dimensions) {
            document.getElementById('result-placeholder').style.display = 'none';
            document.getElementById('result-content').style.display = 'block';

            document.getElementById('dimension-count').textContent = dimensions || embedding.length;
            document.getElementById('total-dimensions').textContent = embedding.length;

            var preview = embedding.slice(0, 20);
            var previewHtml = preview.map(function(v) {
                return '<span class="vector-value">' + v.toFixed(4) + '</span>';
            }).join('');
            document.getElementById('vector-preview').innerHTML = previewHtml;
            document.getElementById('vector-end').textContent = Math.min(19, embedding.length - 1);

            var min = Math.min.apply(null, embedding);
            var max = Math.max.apply(null, embedding);
            var avg = embedding.reduce(function(a, b) { return a + b; }, 0) / embedding.length;

            document.getElementById('min-value').textContent = min.toFixed(4);
            document.getElementById('max-value').textContent = max.toFixed(4);
            document.getElementById('avg-value').textContent = avg.toFixed(4);

            document.getElementById('copy-btn').disabled = false;
            document.getElementById('download-btn').disabled = false;
        },

        displayBatchResult: function(embeddings, texts) {
            document.getElementById('result-placeholder').style.display = 'none';
            document.getElementById('result-content').style.display = 'block';

            document.getElementById('dimension-count').textContent = embeddings.length + ' 个向量';
            document.getElementById('total-dimensions').textContent = embeddings[0] ? embeddings[0].length : 0;

            var previewHtml = '<div class="batch-summary">';
            previewHtml += '<p>成功生成 ' + embeddings.length + ' 个向量嵌入</p>';
            previewHtml += '<p>每个向量维度: ' + (embeddings[0] ? embeddings[0].length : 0) + '</p>';
            previewHtml += '</div>';
            document.getElementById('vector-preview').innerHTML = previewHtml;

            document.getElementById('min-value').textContent = '-';
            document.getElementById('max-value').textContent = '-';
            document.getElementById('avg-value').textContent = '-';

            document.getElementById('copy-btn').disabled = false;
            document.getElementById('download-btn').disabled = false;
        },

        copyResult: function() {
            if (!this.currentResult) return;

            var text = JSON.stringify(this.currentResult, null, 2);
            navigator.clipboard.writeText(text).then(function() {
                alert('已复制到剪贴板');
            });
        },

        downloadResult: function() {
            if (!this.currentResult) return;

            var text = JSON.stringify(this.currentResult, null, 2);
            var blob = new Blob([text], { type: 'application/json' });
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'embedding_' + Date.now() + '.json';
            a.click();
            URL.revokeObjectURL(url);
        },

        addToHistory: function(text, embedding) {
            var item = {
                text: text.substring(0, 50) + (text.length > 50 ? '...' : ''),
                fullText: text,
                dimensions: embedding ? embedding.length : 0,
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
                return '\
                    <div class="history-item" data-index="' + index + '">\
                        <span class="history-text">' + self.escapeHtml(item.text) + '</span>\
                        <span class="history-meta">\
                            <span>' + item.dimensions + '维</span>\
                            <span>' + self.formatTime(item.timestamp) + '</span>\
                        </span>\
                    </div>\
                ';
            }).join('');

            container.querySelectorAll('.history-item').forEach(function(el) {
                el.addEventListener('click', function() {
                    var index = parseInt(this.getAttribute('data-index'));
                    var item = self.history[index];
                    document.getElementById('input-text').value = item.fullText;
                    document.getElementById('input-char-count').textContent = item.fullText.length;
                });
            });
        },

        loadHistory: function() {
            var saved = localStorage.getItem('llm-embed-history');
            if (saved) {
                this.history = JSON.parse(saved);
                this.renderHistory();
            }
        },

        saveHistory: function() {
            localStorage.setItem('llm-embed-history', JSON.stringify(this.history));
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
            LlmEmbed.init();
        });
    } else {
        LlmEmbed.init();
    }

    global.LlmEmbed = LlmEmbed;
})(window);
