export class KnowledgePanel {

    constructor(container, options = {}) {
        this.container = container;
        this.options = options;
        this.currentTab = 'search';
        this.render();
        this.bindEvents();
    }

    render() {
        this.container.innerHTML = `
        <div class="knowledge-panel">
            <div class="kp-header">
                <h4><i class="ri-book-open-line"></i> 知识中心</h4>
                <div class="kp-tabs">
                    <button class="kp-tab active" data-tab="search">检索</button>
                    <button class="kp-tab" data-tab="library">知识库</button>
                    <button class="kp-tab" data-tab="dict">字典</button>
                </div>
            </div>
            <div class="kp-body">
                <div class="kp-section kp-search active" id="kpSearch">
                    <input type="text" class="kp-input" id="kpSearchInput" placeholder="搜索知识..." />
                    <div class="kp-results" id="kpSearchResults"></div>
                </div>
                <div class="kp-section kp-library" id="kpLibrary">
                    <div class="kp-ingest-area" id="kpIngestArea">
                        <p>将业务数据拖拽至此，自动整理入库</p>
                        <button class="btn-sm kp-btn" id="kpIngestFromChat">从当前对话提取</button>
                    </div>
                    <div class="kp-doc-list" id="kpDocList"></div>
                </div>
                <div class="kp-section kp-dict" id="kpDict">
                    <div class="kp-dict-list" id="kpDictList"></div>
                </div>
            </div>
        </div>`;
    }

    bindEvents() {
        document.querySelectorAll('.kp-tab').forEach(tab => {
            tab.addEventListener('click', (e) => {
                document.querySelectorAll('.kp-tab').forEach(t => t.classList.remove('active'));
                document.querySelectorAll('.kp-section').forEach(s => s.classList.remove('active'));
                e.target.classList.add('active');
                const section = document.getElementById('kp' + this.capitalize(e.target.dataset.tab));
                if (section) section.classList.add('active');
            });
        });

        const searchInput = document.getElementById('kpSearchInput');
        let debounceTimer;
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => this.searchKnowledge(e.target.value), 300);
            });
        }

        const ingestBtn = document.getElementById('kpIngestFromChat');
        if (ingestBtn) {
            ingestBtn.addEventListener('click', () => this.emit('ingest-from-chat'));
        }
    }

    async searchKnowledge(query) {
        if (!query.trim()) return;
        const resultsEl = document.getElementById('kpSearchResults');
        if (!resultsEl) return;
        
        resultsEl.innerHTML = '<div class="kp-loading"><i class="ri-loader-4-line spin"></i> 检索中...</div>';
        
        try {
            const resp = await fetch(`/api/v1/rag/search?q=${encodeURIComponent(query)}&limit=5`);
            const results = await resp.json();
            this.renderSearchResults(results);
        } catch (e) {
            resultsEl.innerHTML = '<div class="kp-error">检索失败</div>';
        }
    }

    renderSearchResults(results) {
        const el = document.getElementById('kpSearchResults');
        if (!el || !Array.isArray(results)) return;
        
        if (results.length === 0) {
            el.innerHTML = '<div class="kp-empty">无匹配结果</div>';
            return;
        }

        el.innerHTML = results.map(doc => `
            <div class="kp-result-item" data-doc-id="${doc.docId}">
                <div class="kp-result-title">${this.escapeHtml(doc.title)}</div>
                <div class="kp-result-snippet">${this.escapeHtml((doc.content || '').substring(0, 120))}...</div>
                <div class="kp-result-actions">
                    <button class="kp-use-btn" data-doc-id="${doc.docId}">引用到对话</button>
                </div>
            </div>
        `).join('');

        el.querySelectorAll('.kp-use-btn').forEach(btn => {
            btn.addEventListener('click', () => this.emit('use-in-chat', { docId: btn.dataset.docId }));
        });
    }

    capitalize(str) { return str.charAt(0).toUpperCase() + str.slice(1); }

    emit(event, data) {
        this.container.dispatchEvent(new CustomEvent(`kp:${event}`, { detail: data }));
    }

    escapeHtml(str) {
        const div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    }

    destroy() {
        this.container.innerHTML = '';
    }
}
