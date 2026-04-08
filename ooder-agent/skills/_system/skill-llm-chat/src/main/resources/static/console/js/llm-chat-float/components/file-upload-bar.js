export class FileUploadBar {

    constructor(inputAreaEl, options = {}) {
        this.container = inputAreaEl;
        this.options = {
            maxFileSize: 50 * 1024 * 1024,
            allowedTypes: ['image/*', 'application/pdf', 'text/*',
                          '.doc', '.docx', '.xls', '.xlsx', '.zip'],
            maxFiles: 5,
            ...options
        };
        this.pendingFiles = [];
        this.render();
        this.bindEvents();
    }

    render() {
        const barHtml = `
        <div class="file-upload-bar">
            <label class="file-upload-btn" title="上传附件">
                <i class="ri-attachment-2"></i>
                <input type="file" id="chatFileInput" multiple 
                       accept="${this.options.allowedTypes.join(',')}"
                       style="display:none" />
            </label>
            <div class="file-preview-list" id="filePreviewList"></div>
        </div>`;
        this.container.insertAdjacentHTML('afterend', barHtml);
    }

    bindEvents() {
        const input = document.getElementById('chatFileInput');
        if (!input) return;
        
        input.addEventListener('change', (e) => this.handleFiles(e.target.files));

        const parentEl = this.container.closest('.agent-chat-input-area') || this.container.parentElement;
        if (parentEl) {
            parentEl.addEventListener('paste', (e) => {
                const items = e.clipboardData?.items;
                if (!items) return;
                const files = Array.from(items)
                    .filter(i => i.kind === 'file')
                    .map(i => i.getAsFile())
                    .filter(f => f);
                if (files.length > 0) this.handleFiles(files);
            });

            parentEl.addEventListener('dragover', (e) => {
                e.preventDefault();
                parentEl.classList.add('drag-over');
            });
            parentEl.addEventListener('dragleave', () => parentEl.classList.remove('drag-over'));
            parentEl.addEventListener('drop', (e) => {
                e.preventDefault();
                parentEl.classList.remove('drag-over');
                if (e.dataTransfer?.files?.length) this.handleFiles(e.dataTransfer.files);
            });
        }
    }

    handleFiles(fileList) {
        Array.from(fileList).forEach(file => {
            if (this.pendingFiles.length >= this.options.maxFiles) {
                this.showToast(`最多上传 ${this.options.maxFiles} 个文件`);
                return;
            }
            if (file.size > this.options.maxFileSize) {
                this.showToast(`${file.name} 超过大小限制 (${this.formatSize(this.options.maxFileSize)})`);
                return;
            }
            this.pendingFiles.push(file);
            this.renderPreview(file);
        });
        this.emitChange();
    }

    renderPreview(file) {
        const list = document.getElementById('filePreviewList');
        if (!list) return;
        
        const isImage = file.type.startsWith('image/');
        const item = document.createElement('div');
        item.className = 'file-preview-item';
        item.dataset.fileName = file.name;
        item.innerHTML = `
            ${isImage ? `<img src="${URL.createObjectURL(file)}" class="file-thumb" alt="${file.name}" />` 
                        : `<i class="ri-file-${this.getFileIcon(file.type)}-line file-icon"></i>`}
            <span class="file-name" title="${file.name}">${file.name}</span>
            <span class="file-size">${this.formatSize(file.size)}</span>
            <button class="file-remove" title="移除"><i class="ri-close-line"></i></button>`;
        list.appendChild(item);

        item.querySelector('.file-remove').addEventListener('click', (e) => {
            e.stopPropagation();
            this.pendingFiles = this.pendingFiles.filter(f => f !== file);
            item.remove();
            this.emitChange();
        });
    }

    async uploadAll(refId) {
        const results = [];
        for (const file of this.pendingFiles) {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('scope', 'CHAT_ATTACHMENT');
            formData.append('refId', refId || '');

            try {
                const resp = await fetch('/api/v1/chat/files/upload', {
                    method: 'POST',
                    body: formData
                });
                if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
                const meta = await resp.json();
                results.push({
                    fileId: meta.fileId,
                    name: meta.originalName,
                    size: meta.size,
                    mimeType: meta.mimeType,
                    previewUrl: meta.previewUrl,
                    thumbnailUrl: meta.thumbnailUrl,
                    formattedSize: meta.formattedSize
                });
            } catch (e) {
                results.push({ error: file.name, message: e.message });
            }
        }
        this.clear();
        return results;
    }

    clear() {
        this.pendingFiles = [];
        const list = document.getElementById('filePreviewList');
        if (list) list.innerHTML = '';
        const input = document.getElementById('chatFileInput');
        if (input) input.value = '';
        this.emitChange();
    }

    getFileIcon(mime) {
        if (mime.includes('pdf')) return 'pdf';
        if (mime.includes('word') || mime.includes('document')) return 'word';
        if (mime.includes('sheet') || mime.includes('excel')) return 'excel';
        if (mime.includes('zip') || mime.includes('rar') || mime.includes('archive')) return 'zip';
        if (mime.includes('text')) return 'text';
        return 'unknown';
    }

    formatSize(bytes) {
        if (bytes < 1024) return bytes + 'B';
        if (bytes < 1048576) return (bytes / 1024).toFixed(1) + 'KB';
        return (bytes / 1048576).toFixed(1) + 'MB';
    }

    showToast(msg) {
        const toast = document.createElement('div');
        toast.className = 'file-upload-toast';
        toast.textContent = msg;
        toast.style.cssText = 'position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);background:rgba(0,0,0,.75);color:#fff;padding:10px 20px;border-radius:8px;z-index:99999;font-size:13px;';
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 2000);
    }

    emitChange() {
        this.container.dispatchEvent(new CustomEvent('fileUpload:change', {
            detail: { count: this.pendingFiles.length, files: this.pendingFiles }
        }));
    }

    hasPendingFiles() {
        return this.pendingFiles.length > 0;
    }

    destroy() {
        this.clear();
        const bar = this.container.parentElement?.querySelector('.file-upload-bar');
        if (bar) bar.remove();
    }
}
