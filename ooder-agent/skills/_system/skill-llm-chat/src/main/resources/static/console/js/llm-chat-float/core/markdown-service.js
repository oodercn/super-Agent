/**
 * Markdown 渲染服务
 * 支持代码高亮
 */
export class MarkdownService {
    constructor() {
        this.highlightJsLoaded = false;
    }

    async loadHighlightJs() {
        if (this.highlightJsLoaded || window.hljs) {
            this.highlightJsLoaded = true;
            return;
        }
        
        return new Promise((resolve) => {
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/github-dark.min.css';
            document.head.appendChild(link);
            
            const script = document.createElement('script');
            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js';
            script.onload = () => {
                this.highlightJsLoaded = true;
                resolve();
            };
            script.onerror = () => {
                console.warn('[MarkdownService] Failed to load highlight.js');
                resolve();
            };
            document.head.appendChild(script);
        });
    }

    render(text) {
        if (!text) return '';
        
        let html = this.escapeHtml(text);
        html = html.replace(/&#10;/g, '\n');
        
        html = this.renderCodeBlocks(html);
        html = this.renderInlineCode(html);
        html = this.renderHeaders(html);
        html = this.renderBold(html);
        html = this.renderItalic(html);
        html = this.renderLinks(html);
        html = this.renderLists(html);
        html = this.renderLineBreaks(html);
        
        return html;
    }

    renderCodeBlocks(html) {
        return html.replace(
            /```(\w*)\n([\s\S]*?)```/g,
            (match, lang, code) => {
                const language = lang || 'plaintext';
                const codeContent = code.trim();
                
                let highlighted = codeContent;
                if (window.hljs && language !== 'plaintext') {
                    try {
                        highlighted = hljs.highlight(codeContent, { language }).value;
                    } catch (e) {
                        highlighted = hljs.highlightAuto(codeContent).value;
                    }
                }
                
                return `<div class="md-code-block">
                    <div class="md-code-header">
                        <span class="md-code-lang">${language}</span>
                        <button class="md-code-copy" onclick="navigator.clipboard.writeText(decodeURIComponent('${encodeURIComponent(codeContent)}'))">
                            <i class="ri-file-copy-line"></i> 复制
                        </button>
                    </div>
                    <pre class="md-code-content"><code class="language-${language}">${highlighted}</code></pre>
                </div>`;
            }
        );
    }

    renderInlineCode(html) {
        return html.replace(
            /`([^`]+)`/g,
            '<code class="md-inline-code">$1</code>'
        );
    }

    renderHeaders(html) {
        return html
            .replace(/^### (.+)$/gm, '<h4 class="md-h4">$1</h4>')
            .replace(/^## (.+)$/gm, '<h3 class="md-h3">$1</h3>')
            .replace(/^# (.+)$/gm, '<h2 class="md-h2">$1</h2>');
    }

    renderBold(html) {
        return html.replace(
            /\*\*([^*]+)\*\*/g,
            '<strong class="md-bold">$1</strong>'
        );
    }

    renderItalic(html) {
        return html.replace(
            /\*([^*]+)\*/g,
            '<em class="md-italic">$1</em>'
        );
    }

    renderLinks(html) {
        return html.replace(
            /\[([^\]]+)\]\(([^)]+)\)/g,
            '<a class="md-link" href="$2" target="_blank" rel="noopener">$1</a>'
        );
    }

    renderLists(html) {
        const lines = html.split('\n');
        let inList = false;
        let result = [];
        
        for (const line of lines) {
            const listMatch = line.match(/^(\s*)[-*] (.+)$/);
            
            if (listMatch) {
                if (!inList) {
                    result.push('<ul class="md-list">');
                    inList = true;
                }
                result.push(`<li class="md-list-item">${listMatch[2]}</li>`);
            } else {
                if (inList) {
                    result.push('</ul>');
                    inList = false;
                }
                result.push(line);
            }
        }
        
        if (inList) {
            result.push('</ul>');
        }
        
        return result.join('\n');
    }

    renderLineBreaks(html) {
        return html.replace(/\n\n/g, '</p><p class="md-paragraph">').replace(/\n/g, '<br>');
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    addStyles() {
        if (document.getElementById('markdown-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'markdown-styles';
        style.textContent = `
            .md-code-block {
                margin: 12px 0;
                border-radius: 8px;
                overflow: hidden;
                background: #1e1e1e;
                border: 1px solid #333;
            }
            .md-code-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 8px 12px;
                background: #2d2d2d;
                border-bottom: 1px solid #333;
            }
            .md-code-lang {
                font-size: 12px;
                color: #888;
                text-transform: uppercase;
            }
            .md-code-copy {
                background: none;
                border: none;
                color: #888;
                font-size: 12px;
                cursor: pointer;
                display: flex;
                align-items: center;
                gap: 4px;
                padding: 4px 8px;
                border-radius: 4px;
                transition: all 0.2s;
            }
            .md-code-copy:hover {
                background: #444;
                color: #fff;
            }
            .md-code-content {
                margin: 0;
                padding: 12px;
                overflow-x: auto;
                font-family: 'Fira Code', 'Consolas', monospace;
                font-size: 13px;
                line-height: 1.5;
            }
            .md-code-content code {
                background: transparent;
                padding: 0;
            }
            .md-inline-code {
                background: #f1f5f9;
                padding: 2px 6px;
                border-radius: 4px;
                font-family: 'Fira Code', 'Consolas', monospace;
                font-size: 0.9em;
                color: #e91e63;
            }
            .md-h2, .md-h3, .md-h4 {
                margin: 16px 0 8px;
                font-weight: 600;
                color: #334155;
            }
            .md-h2 { font-size: 18px; }
            .md-h3 { font-size: 16px; }
            .md-h4 { font-size: 15px; }
            .md-bold { font-weight: 600; }
            .md-italic { font-style: italic; }
            .md-link {
                color: #6366f1;
                text-decoration: none;
            }
            .md-link:hover {
                text-decoration: underline;
            }
            .md-list {
                margin: 8px 0;
                padding-left: 20px;
            }
            .md-list-item {
                margin: 4px 0;
                color: #334155;
            }
            .md-paragraph {
                margin: 8px 0;
            }
        `;
        document.head.appendChild(style);
    }
}

export const markdownService = new MarkdownService();

export default MarkdownService;
