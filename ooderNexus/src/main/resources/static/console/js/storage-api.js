/**
 * 存储管理 API 调用封装
 * 提供统一的 API 调用接口，处理存储管理的所有功能
 */

class StorageAPI {
    constructor() {
        this.apiClient = new ApiClient('/api/storage');
        this.cache = new Map();
    }

    /**
     * 获取存储空间信息
     */
    async getStorageSpace() {
        const cacheKey = 'storage-space';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.apiClient.get('/space');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 获取文件夹内容
     */
    async getFolderChildren(folderId) {
        if (!folderId || folderId === '') {
            // 调用根目录端点
            const data = await this.apiClient.get('/folder/children');
            return data;
        } else {
            // 使用POST请求而不是GET请求，避免路径参数的长度限制和特殊字符问题
            const data = await this.apiClient.post('/folder/children', { folderId: folderId });
            return data;
        }
    }

    /**
     * 创建文件夹
     */
    async createFolder(folderData) {
        const data = await this.apiClient.post('/folder', folderData);
        
        this.cache.delete('storage-space');
        return data;
    }

    /**
     * 删除文件夹
     */
    async deleteFolder(folderId) {
        const data = await this.apiClient.delete(`/folder/${encodeURIComponent(folderId)}`);
        
        this.cache.delete('storage-space');
        return data;
    }

    /**
     * 上传文件
     */
    async uploadFile(formData) {
        // 直接使用post方法上传FormData
        const data = await this.apiClient.post('/upload', formData);
        
        this.cache.delete('storage-space');
        return data;
    }

    /**
     * 下载文件
     */
    async downloadFile(fileId) {
        const url = `${this.apiClient.baseURL}/download/${encodeURIComponent(fileId)}`;
        
        try {
            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error('文件下载失败');
            }
            
            const blob = await response.blob();
            
            return {
                success: true,
                data: blob,
                fileName: this.getFileNameFromResponse(response)
            };
        } catch (error) {
            console.error('文件下载错误:', error);
            throw error;
        }
    }

    /**
     * 从响应中获取文件名
     */
    getFileNameFromResponse(response) {
        const contentDisposition = response.headers.get('Content-Disposition');
        if (contentDisposition) {
            const matches = /filename[^;=\n]*=((['"]).*?\2|([^;\n]*))/i.exec(contentDisposition);
            if (matches != null && matches[1]) {
                return matches[1].replace(/['"]/g, '');
            }
        }
        return 'file';
    }

    /**
     * 删除文件
     */
    async deleteFile(fileId) {
        const data = await this.apiClient.delete(`/file/${encodeURIComponent(fileId)}`);
        
        this.cache.delete('storage-space');
        return data;
    }

    /**
     * 更新文件信息
     */
    async updateFile(fileId, fileData) {
        const data = await this.apiClient.put(`/file/${encodeURIComponent(fileId)}`, fileData);
        
        return data;
    }

    /**
     * 获取文件版本列表
     */
    async getFileVersions(fileId) {
        const data = await this.apiClient.get(`/file/${encodeURIComponent(fileId)}/versions`);
        return data;
    }

    /**
     * 恢复文件版本
     */
    async restoreFileVersion(fileId, versionId) {
        const data = await this.apiClient.post(`/file/${encodeURIComponent(fileId)}/restore/${encodeURIComponent(versionId)}`);
        
        return data;
    }

    /**
     * 清理存储
     */
    async cleanupStorage() {
        const data = await this.apiClient.post('/cleanup');
        
        this.cache.delete('storage-space');
        return data;
    }

    /**
     * 获取分享的文件列表
     */
    async getSharedFiles() {
        const data = await this.apiClient.get('/shared');
        return data;
    }

    /**
     * 分享文件
     */
    async shareFile(shareData) {
        const data = await this.apiClient.post('/share', shareData);
        
        return data;
    }

    /**
     * 获取收到的文件列表
     */
    async getReceivedFiles() {
        const data = await this.apiClient.get('/received');
        return data;
    }

    /**
     * 取消分享文件
     */
    async unshareFile(fileId) {
        const data = await this.apiClient.delete(`/share/${encodeURIComponent(fileId)}`);
        
        return data;
    }

    /**
     * 清除缓存
     */
    clearCache() {
        this.cache.clear();
    }

    /**
     * 清除特定缓存
     */
    clearCacheKey(key) {
        this.cache.delete(key);
    }
}

// 导出 StorageAPI 类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = StorageAPI;
} else {
    window.StorageAPI = StorageAPI;
}

// 创建全局实例
window.storageAPI = new StorageAPI();
