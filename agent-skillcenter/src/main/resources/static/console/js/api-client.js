/**
 * SkillCenter API 客户端
 * 统一封装所有 AJAX 请求，遵循 ooderNexus 规范
 *
 * 规范说明：
 * 1. 所有数据接口使用 POST 请求
 * 2. 使用 ResultModel 2.0 规范: {code, status, message, data, timestamp}
 * 3. 统一错误处理
 * 4. 统一请求头
 */

(function() {
  'use strict';

  /**
   * API 客户端配置
   */
  const config = {
    // 基础 URL
    baseURL: '',
    // 默认超时时间（毫秒）
    timeout: 30000,
    // 默认请求头
    headers: {
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest'
    }
  };

  /**
   * API 客户端
   */
  const ApiClient = {
    // 版本
    version: '2.0.0',

    /**
     * 配置客户端
     * @param {Object} options - 配置选项
     */
    setConfig(options) {
      Object.assign(config, options);
    },

    /**
     * 发送 POST 请求
     * @param {string} url - 请求地址
     * @param {Object} data - 请求数据
     * @param {Object} options - 请求选项
     * @returns {Promise} 响应结果
     */
    async post(url, data = {}, options = {}) {
      const fullUrl = this._buildUrl(url);
      const requestOptions = {
        method: 'POST',
        headers: { ...config.headers, ...options.headers },
        ...options
      };

      // 添加请求体
      if (data && Object.keys(data).length > 0) {
        requestOptions.body = JSON.stringify(data);
      }

      return this._request(fullUrl, requestOptions);
    },

    /**
     * 发送 GET 请求（仅用于特殊场景，如文件下载）
     * @param {string} url - 请求地址
     * @param {Object} params - URL 参数
     * @param {Object} options - 请求选项
     * @returns {Promise} 响应结果
     */
    async get(url, params = {}, options = {}) {
      const queryString = this._buildQueryString(params);
      const fullUrl = this._buildUrl(url) + (queryString ? '?' + queryString : '');
      const requestOptions = {
        method: 'GET',
        headers: { ...config.headers, ...options.headers },
        ...options
      };

      return this._request(fullUrl, requestOptions);
    },

    /**
     * 执行请求
     * @private
     */
    async _request(url, options) {
      // 创建 AbortController 用于超时控制
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), config.timeout);

      try {
        console.log(`[ApiClient] 请求: ${options.method} ${url}`);

        const response = await fetch(url, {
          ...options,
          signal: controller.signal
        });

        clearTimeout(timeoutId);

        // 检查响应状态
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }

        // 解析响应数据
        const result = await response.json();
        console.log(`[ApiClient] 响应:`, result);

        // 验证 ResultModel 格式
        if (!this._isValidResultModel(result)) {
          console.warn('[ApiClient] 响应格式不符合 ResultModel 规范');
          return result;
        }

        // 检查业务状态码
        if (result.code !== 200) {
          throw new Error(result.message || `业务错误: ${result.code}`);
        }

        return result;

      } catch (error) {
        clearTimeout(timeoutId);

        if (error.name === 'AbortError') {
          throw new Error('请求超时，请稍后重试');
        }

        console.error('[ApiClient] 请求失败:', error);
        throw error;
      }
    },

    /**
     * 构建完整 URL
     * @private
     */
    _buildUrl(url) {
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url;
      }
      if (url.startsWith('/')) {
        return url;
      }
      return `${config.baseURL}/${url}`;
    },

    /**
     * 构建查询字符串
     * @private
     */
    _buildQueryString(params) {
      if (!params || Object.keys(params).length === 0) {
        return '';
      }
      return Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');
    },

    /**
     * 验证 ResultModel 格式
     * @private
     */
    _isValidResultModel(result) {
      return result &&
             typeof result === 'object' &&
             'code' in result &&
             'status' in result &&
             'message' in result;
    },

    // ==================== 技能管理 API ====================

    /**
     * 获取技能列表
     * @param {Object} params - 查询参数
     * @returns {Promise} 技能列表
     */
    async getSkills(params = {}) {
      const result = await this.post('/api/skills/list', params);
      return result.data;
    },

    /**
     * 获取技能详情
     * @param {string} skillId - 技能ID
     * @returns {Promise} 技能详情
     */
    async getSkillById(skillId) {
      const result = await this.post('/api/skills/get', { skillId });
      return result.data;
    },

    /**
     * 添加技能
     * @param {Object} skill - 技能数据
     * @returns {Promise} 添加结果
     */
    async addSkill(skill) {
      const result = await this.post('/api/skills/add', skill);
      return result.data;
    },

    /**
     * 更新技能
     * @param {string} skillId - 技能ID
     * @param {Object} skill - 技能数据
     * @returns {Promise} 更新结果
     */
    async updateSkill(skillId, skill) {
      const result = await this.post('/api/skills/update', { skillId, ...skill });
      return result.data;
    },

    /**
     * 删除技能
     * @param {string} skillId - 技能ID
     * @returns {Promise} 删除结果
     */
    async deleteSkill(skillId) {
      const result = await this.post('/api/skills/delete', { skillId });
      return result.data;
    },

    /**
     * 审核技能
     * @param {string} skillId - 技能ID
     * @returns {Promise} 审核结果
     */
    async approveSkill(skillId) {
      const result = await this.post(`/api/skills/${skillId}/approve`);
      return result.data;
    },

    /**
     * 拒绝技能
     * @param {string} skillId - 技能ID
     * @returns {Promise} 拒绝结果
     */
    async rejectSkill(skillId) {
      const result = await this.post(`/api/skills/${skillId}/reject`);
      return result.data;
    },

    // ==================== 仪表盘 API ====================

    /**
     * 获取系统概览统计数据
     * @returns {Promise} 系统概览统计数据
     */
    async getDashboardStats() {
      const result = await this.post('/api/dashboard');
      return result.data;
    },

    /**
     * 获取技能执行统计数据
     * @returns {Promise} 技能执行统计数据
     */
    async getExecutionStats() {
      const result = await this.post('/api/dashboard/execution-stats');
      return result.data;
    },

    /**
     * 获取市场活跃度统计数据
     * @returns {Promise} 市场活跃度统计数据
     */
    async getMarketStats() {
      const result = await this.post('/api/dashboard/market-stats');
      return result.data;
    },

    /**
     * 获取系统资源使用统计数据
     * @returns {Promise} 系统资源使用统计数据
     */
    async getSystemStats() {
      const result = await this.post('/api/dashboard/system-stats');
      return result.data;
    },

    // ==================== 通用工具方法 ====================

    /**
     * 显示加载状态
     * @param {string} message - 加载消息
     */
    showLoading(message = '加载中...') {
      // 可以在这里实现全局加载状态显示
      console.log(`[ApiClient] ${message}`);
    },

    /**
     * 隐藏加载状态
     */
    hideLoading() {
      // 可以在这里实现全局加载状态隐藏
      console.log('[ApiClient] 加载完成');
    },

    /**
     * 显示错误消息
     * @param {string} message - 错误消息
     */
    showError(message) {
      console.error('[ApiClient] 错误:', message);
      // 可以在这里实现全局错误提示
      if (typeof alert !== 'undefined') {
        alert(message);
      }
    },

    /**
     * 显示成功消息
     * @param {string} message - 成功消息
     */
    showSuccess(message) {
      console.log('[ApiClient] 成功:', message);
      // 可以在这里实现全局成功提示
      if (typeof alert !== 'undefined') {
        alert(message);
      }
    }
  };

  // 暴露到全局
  window.ApiClient = ApiClient;

})();