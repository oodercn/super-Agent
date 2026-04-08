/**
 * LlmChatFloat 兼容性加载器
 * 自动检测是否支持ES6模块，选择合适的加载方式
 */
(function() {
    'use strict';

    window.__LLM_CHAT_FLOAT_READY__ = false;
    window.__LLM_CHAT_FLOAT_QUEUE__ = [];

    function checkES6ModuleSupport() {
        try {
            new Function('import("")');
            return true;
        } catch (e) {
            return false;
        }
    }

    function loadScript(src, isModule) {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = src;
            if (isModule) {
                script.type = 'module';
            }
            script.onload = resolve;
            script.onerror = reject;
            document.head.appendChild(script);
        });
    }

    function processQueue() {
        window.__LLM_CHAT_FLOAT_READY__ = true;
        const queue = window.__LLM_CHAT_FLOAT_QUEUE__;
        window.__LLM_CHAT_FLOAT_QUEUE__ = [];
        
        queue.forEach(item => {
            if (typeof item === 'function') {
                try {
                    item();
                } catch (e) {
                    console.error('[LlmChatFloat] Queue callback error:', e);
                }
            }
        });
    }

    window.__llmChatFloatOnReady__ = function(callback) {
        if (window.__LLM_CHAT_FLOAT_READY__) {
            try {
                callback();
            } catch (e) {
                console.error('[LlmChatFloat] Callback error:', e);
            }
        } else {
            window.__LLM_CHAT_FLOAT_QUEUE__.push(callback);
        }
    };

    async function init() {
        const supportsModule = checkES6ModuleSupport();
        const basePath = '/console/js/llm-chat-float/';

        if (supportsModule) {
            try {
                await loadScript(basePath + 'index.js', true);
                console.log('[LlmChatFloat] Loaded as ES6 module');
            } catch (e) {
                console.warn('[LlmChatFloat] Failed to load ES6 module, falling back to legacy');
                await loadScript('/console/js/llm-chat-float.js', false);
            }
        } else {
            await loadScript('/console/js/llm-chat-float.js', false);
            console.log('[LlmChatFloat] Loaded legacy version');
        }
        
        processQueue();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
