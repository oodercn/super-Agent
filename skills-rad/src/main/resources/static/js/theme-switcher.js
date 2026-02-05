/**
 * OOD CodeMirror 主题切换器
 * 支持暗色、亮色和高对比度模式的手动切换
 */

class OODThemeSwitcher {
    constructor() {
        this.themes = {
            'auto': '自动（跟随系统）',
            'light': '亮色模式',
            'dark': '暗色模式',
            'high-contrast-light': '高对比度（亮色）',
            'high-contrast-dark': '高对比度（暗色）'
        };
        
        this.currentTheme = this.getSavedTheme() || 'auto';
        this.init();
    }
    
    init() {
        this.applyTheme(this.currentTheme);
        this.createThemeSwitcher();
    }
    
    /**
     * 应用主题
     * @param {string} theme - 主题名称
     */
    applyTheme(theme) {
        const root = document.documentElement;
        
        // 移除所有主题类
        root.classList.remove('theme-light', 'theme-dark', 'theme-high-contrast-light', 'theme-high-contrast-dark');
        
        if (theme !== 'auto') {
            root.classList.add(`theme-${theme}`);
        }
        
        this.currentTheme = theme;
        this.saveTheme(theme);
        
        // 触发主题变更事件
        window.dispatchEvent(new CustomEvent('themeChanged', { 
            detail: { theme: theme } 
        }));
    }
    
    /**
     * 创建主题切换器UI
     */
    createThemeSwitcher() {
        const switcher = document.createElement('div');
        switcher.className = 'ood-theme-switcher';
        switcher.innerHTML = `
            <select id="ood-theme-select" class="ood-theme-select">
                ${Object.entries(this.themes).map(([key, label]) => 
                    `<option value="${key}" ${key === this.currentTheme ? 'selected' : ''}>${label}</option>`
                ).join('')}
            </select>
        `;
        
        // 添加样式
        if (!document.getElementById('ood-theme-switcher-styles')) {
            const styles = document.createElement('style');
            styles.id = 'ood-theme-switcher-styles';
            styles.textContent = `
                .ood-theme-switcher {
                    position: fixed;
                    top: 10px;
                    right: 10px;
                    z-index: 10000;
                    background: var(--ood-code-gutter-bg, #f6f8fa);
                    border: 1px solid var(--ood-border, #d0d7de);
                    border-radius: 6px;
                    padding: 8px;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                }
                
                .ood-theme-select {
                    background: var(--ood-code-bg, #ffffff);
                    color: var(--ood-code-text, #24292f);
                    border: 1px solid var(--ood-border, #d0d7de);
                    border-radius: 4px;
                    padding: 4px 8px;
                    font-size: 12px;
                    outline: none;
                }
                
                .ood-theme-select:focus {
                    border-color: var(--ood-code-cursor, #0969da);
                    box-shadow: 0 0 0 2px var(--ood-code-selection, rgba(9, 105, 218, 0.3));
                }
            `;
            document.head.appendChild(styles);
        }
        
        // 添加到页面
        document.body.appendChild(switcher);
        
        // 绑定事件
        const select = document.getElementById('ood-theme-select');
        select.addEventListener('change', (e) => {
            this.applyTheme(e.target.value);
        });
    }
    
    /**
     * 获取保存的主题
     */
    getSavedTheme() {
        try {
            return localStorage.getItem('ood-theme');
        } catch {
            return null;
        }
    }
    
    /**
     * 保存主题设置
     */
    saveTheme(theme) {
        try {
            localStorage.setItem('ood-theme', theme);
        } catch {
            // 忽略存储错误
        }
    }
    
    /**
     * 获取当前主题
     */
    getCurrentTheme() {
        return this.currentTheme;
    }
    
    /**
     * 切换到下一个主题
     */
    nextTheme() {
        const themes = Object.keys(this.themes);
        const currentIndex = themes.indexOf(this.currentTheme);
        const nextIndex = (currentIndex + 1) % themes.length;
        this.applyTheme(themes[nextIndex]);
    }
}

// 添加主题特定的CSS变量
const themeStyles = document.createElement('style');
themeStyles.textContent = `
    /* 亮色主题 */
    .theme-light {
        --ood-code-bg: #ffffff;
        --ood-code-text: #24292f;
        --ood-code-gutter-bg: #f6f8fa;
        --ood-border: #d0d7de;
        --ood-code-cursor: #24292f;
        --ood-code-cursor-secondary: #656d76;
        --ood-code-selection: #b6d7ff;
        --ood-code-selection-focused: #9cc7f7;
        --ood-code-keyword: #cf222e;
        --ood-code-atom: #0969da;
        --ood-code-number: #0550ae;
        --ood-code-def: #8250df;
        --ood-code-variable: #24292f;
        --ood-code-punctuation: #24292f;
        --ood-code-property: #116329;
        --ood-code-operator: #cf222e;
        --ood-code-variable-2: #0969da;
        --ood-code-variable-3: #0969da;
        --ood-code-type: #8250df;
        --ood-code-comment: #6e7781;
        --ood-code-string: #0a3069;
        --ood-code-string-2: #0a3069;
        --ood-code-meta: #8250df;
        --ood-code-qualifier: #8250df;
        --ood-code-builtin: #8250df;
        --ood-code-bracket: #656d76;
        --ood-code-tag: #116329;
        --ood-code-attribute: #0969da;
        --ood-code-hr: #8c959f;
        --ood-code-link: #0969da;
        --ood-code-activeline-bg: #f6f8fa;
        --ood-code-matchbracket: #26a148;
        --ood-code-nonmatchbracket: #d1242f;
        --ood-code-linenumber: #656d76;
        --ood-code-guttermarker: #24292f;
        --ood-code-guttermarker-subtle: #656d76;
    }
    
    /* 暗色主题 */
    .theme-dark {
        --ood-code-bg: #1e1e2e;
        --ood-code-text: #e0e0e0;
        --ood-code-gutter-bg: #252538;
        --ood-border: #3a3a4a;
        --ood-code-cursor: #ffffff;
        --ood-code-cursor-secondary: #c0c0c0;
        --ood-code-selection: #264f78;
        --ood-code-selection-focused: #3d5a7a;
        --ood-code-keyword: #c586c0;
        --ood-code-atom: #569cd6;
        --ood-code-number: #b5cea8;
        --ood-code-def: #dcdcaa;
        --ood-code-variable: #9cdcfe;
        --ood-code-punctuation: #d4d4d4;
        --ood-code-property: #9cdcfe;
        --ood-code-operator: #d4d4d4;
        --ood-code-variable-2: #4ec9b0;
        --ood-code-variable-3: #4ec9b0;
        --ood-code-type: #4ec9b0;
        --ood-code-comment: #6a9955;
        --ood-code-string: #ce9178;
        --ood-code-string-2: #ce9178;
        --ood-code-meta: #9cdcfe;
        --ood-code-qualifier: #d7ba7d;
        --ood-code-builtin: #d7ba7d;
        --ood-code-bracket: #808080;
        --ood-code-tag: #569cd6;
        --ood-code-attribute: #9cdcfe;
        --ood-code-hr: #808080;
        --ood-code-link: #569cd6;
        --ood-code-activeline-bg: #2a2a3e;
        --ood-code-matchbracket: #0b8043;
        --ood-code-nonmatchbracket: #cbc14a;
        --ood-code-linenumber: #999696;
        --ood-code-guttermarker: #ffffff;
        --ood-code-guttermarker-subtle: #999999;
    }
    
    /* 高对比度亮色主题 */
    .theme-high-contrast-light {
        --ood-code-bg: #ffffff;
        --ood-code-text: #000000;
        --ood-code-gutter-bg: #f0f0f0;
        --ood-border: #000000;
        --ood-code-cursor: #000000;
        --ood-code-cursor-secondary: #333333;
        --ood-code-selection: #0066cc;
        --ood-code-selection-focused: #0080ff;
        --ood-code-keyword: #cc0000;
        --ood-code-atom: #0066cc;
        --ood-code-number: #009900;
        --ood-code-def: #6600cc;
        --ood-code-variable: #000000;
        --ood-code-punctuation: #000000;
        --ood-code-property: #009900;
        --ood-code-operator: #cc0000;
        --ood-code-variable-2: #0066cc;
        --ood-code-variable-3: #0066cc;
        --ood-code-type: #6600cc;
        --ood-code-comment: #666666;
        --ood-code-string: #cc6600;
        --ood-code-string-2: #cc6600;
        --ood-code-meta: #6600cc;
        --ood-code-qualifier: #6600cc;
        --ood-code-builtin: #6600cc;
        --ood-code-bracket: #000000;
        --ood-code-tag: #009900;
        --ood-code-attribute: #0066cc;
        --ood-code-hr: #000000;
        --ood-code-link: #0066cc;
        --ood-code-activeline-bg: #e6e6e6;
        --ood-code-matchbracket: #00cc00;
        --ood-code-nonmatchbracket: #ff0000;
        --ood-code-linenumber: #000000;
        --ood-code-guttermarker: #000000;
        --ood-code-guttermarker-subtle: #333333;
    }
    
    /* 高对比度暗色主题 */
    .theme-high-contrast-dark {
        --ood-code-bg: #000000;
        --ood-code-text: #ffffff;
        --ood-code-gutter-bg: #1a1a1a;
        --ood-border: #ffffff;
        --ood-code-cursor: #ffff00;
        --ood-code-cursor-secondary: #ffffff;
        --ood-code-selection: #0066ff;
        --ood-code-selection-focused: #0080ff;
        --ood-code-keyword: #ff6b6b;
        --ood-code-atom: #4ecdc4;
        --ood-code-number: #95e1d3;
        --ood-code-def: #fce38a;
        --ood-code-variable: #ffffff;
        --ood-code-punctuation: #ffffff;
        --ood-code-property: #95e1d3;
        --ood-code-operator: #ff6b6b;
        --ood-code-variable-2: #4ecdc4;
        --ood-code-variable-3: #4ecdc4;
        --ood-code-type: #fce38a;
        --ood-code-comment: #a8a8a8;
        --ood-code-string: #95e1d3;
        --ood-code-string-2: #95e1d3;
        --ood-code-meta: #fce38a;
        --ood-code-qualifier: #fce38a;
        --ood-code-builtin: #fce38a;
        --ood-code-bracket: #ffffff;
        --ood-code-tag: #4ecdc4;
        --ood-code-attribute: #95e1d3;
        --ood-code-hr: #ffffff;
        --ood-code-link: #4ecdc4;
        --ood-code-activeline-bg: #333333;
        --ood-code-matchbracket: #00ff00;
        --ood-code-nonmatchbracket: #ff0000;
        --ood-code-linenumber: #ffffff;
        --ood-code-guttermarker: #ffff00;
        --ood-code-guttermarker-subtle: #ffffff;
    }
`;
document.head.appendChild(themeStyles);

// 自动初始化
let oodThemeSwitcher;
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        oodThemeSwitcher = new OODThemeSwitcher();
    });
} else {
    oodThemeSwitcher = new OODThemeSwitcher();
}

// 导出到全局
window.OODThemeSwitcher = OODThemeSwitcher;
window.oodThemeSwitcher = oodThemeSwitcher;