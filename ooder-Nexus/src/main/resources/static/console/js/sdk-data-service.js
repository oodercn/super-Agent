/**
 * SDK数据服务
 * 提供与后台 /api/sdk 接口的交互
 * 数据存储在后台 ./storage/sdk/ 目录下的JSON文件中
 * 
 * 使用方式:
 * - 读取数据: SdkDataService.getAll('devices')
 * - 保存数据: SdkDataService.save('devices', 'device-001', {...})
 * - 查询数据: SdkDataService.query('devices', {status: 'online'})
 * 
 * 注意: 此服务不缓存数据，每次调用都会直接访问后台API
 */

const SdkDataService = {
    baseUrl: '/api/sdk',

    /**
     * 获取完整URL
     */
    _getUrl(path) {
        return `${this.baseUrl}${path}`;
    },

    /**
     * 发送HTTP请求
     */
    async _request(method, url, data = null) {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            }
        };

        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(url, options);
            const result = await response.json();
            return result;
        } catch (error) {
            console.error('SDK数据服务请求失败:', error);
            return {
                code: 500,
                message: '请求失败: ' + error.message,
                data: null
            };
        }
    },

    /**
     * 保存数据
     * @param {string} collection - 集合名称
     * @param {string} id - 数据ID
     * @param {Object} data - 数据对象
     * @returns {Promise<Object>} 操作结果
     */
    async save(collection, id, data) {
        return await this._request('POST', this._getUrl(`/${collection}/${id}`), data);
    },

    /**
     * 批量保存数据
     * @param {string} collection - 集合名称
     * @param {Array} dataList - 数据列表
     * @returns {Promise<Object>} 操作结果
     */
    async saveBatch(collection, dataList) {
        return await this._request('POST', this._getUrl(`/${collection}/batch`), dataList);
    },

    /**
     * 根据ID获取数据
     * @param {string} collection - 集合名称
     * @param {string} id - 数据ID
     * @returns {Promise<Object>} 数据对象
     */
    async getById(collection, id) {
        return await this._request('GET', this._getUrl(`/${collection}/${id}`));
    },

    /**
     * 获取集合中的所有数据
     * @param {string} collection - 集合名称
     * @returns {Promise<Array>} 数据列表
     */
    async getAll(collection) {
        const result = await this._request('GET', this._getUrl(`/${collection}`));
        if (result.code === 200 && result.data) {
            return result.data;
        }
        return [];
    },

    /**
     * 根据条件查询数据
     * @param {string} collection - 集合名称
     * @param {Object} conditions - 查询条件
     * @returns {Promise<Array>} 符合条件的数据列表
     */
    async query(collection, conditions) {
        const result = await this._request('POST', this._getUrl(`/${collection}/query`), conditions);
        if (result.code === 200 && result.data) {
            return result.data;
        }
        return [];
    },

    /**
     * 更新数据
     * @param {string} collection - 集合名称
     * @param {string} id - 数据ID
     * @param {Object} data - 更新后的数据
     * @returns {Promise<Object>} 操作结果
     */
    async update(collection, id, data) {
        return await this._request('PUT', this._getUrl(`/${collection}/${id}`), data);
    },

    /**
     * 删除数据
     * @param {string} collection - 集合名称
     * @param {string} id - 数据ID
     * @returns {Promise<Object>} 操作结果
     */
    async delete(collection, id) {
        return await this._request('DELETE', this._getUrl(`/${collection}/${id}`));
    },

    /**
     * 删除集合中的所有数据
     * @param {string} collection - 集合名称
     * @returns {Promise<Object>} 操作结果
     */
    async deleteAll(collection) {
        return await this._request('DELETE', this._getUrl(`/${collection}`));
    },

    /**
     * 获取所有集合列表
     * @returns {Promise<Array>} 集合名称列表
     */
    async getCollections() {
        const result = await this._request('GET', this._getUrl('/collections'));
        if (result.code === 200 && result.data) {
            return result.data;
        }
        return [];
    },

    /**
     * 检查数据是否存在
     * @param {string} collection - 集合名称
     * @param {string} id - 数据ID
     * @returns {Promise<boolean>} 是否存在
     */
    async exists(collection, id) {
        const result = await this._request('GET', this._getUrl(`/${collection}/${id}/exists`));
        if (result.code === 200 && result.data !== undefined) {
            return result.data;
        }
        return false;
    },

    /**
     * 获取集合中的数据数量
     * @param {string} collection - 集合名称
     * @returns {Promise<number>} 数据数量
     */
    async count(collection) {
        const result = await this._request('GET', this._getUrl(`/${collection}/count`));
        if (result.code === 200 && result.data !== undefined) {
            return result.data;
        }
        return 0;
    },

    /**
     * 初始化集合数据（如果不存在则创建默认数据）
     * @param {string} collection - 集合名称
     * @param {Array} defaultData - 默认数据列表
     */
    async initCollection(collection, defaultData) {
        const count = await this.count(collection);
        if (count === 0 && defaultData && defaultData.length > 0) {
            console.log(`初始化集合 ${collection} 的默认数据...`);
            await this.saveBatch(collection, defaultData);
        }
    }
};

// 导出服务
if (typeof module !== 'undefined' && module.exports) {
    module.exports = SdkDataService;
}
