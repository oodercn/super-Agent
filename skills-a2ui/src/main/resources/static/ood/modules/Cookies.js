/**
 * OOD Cookies 模块 - ES6 版本
 * 
 * 功能：
 * - 设置 Cookie
 * - 获取 Cookie
 * - 删除 Cookie
 * - 清空所有 Cookie
 * 
 * 迁移说明：
 * - 从 ood.Class 转换为 ES6 class
 * - 使用装饰器自动注册到全局 ood 对象
 * - 保持所有原有 API 不变，确保向后兼容
 */

import { register } from '../es6/shim.js';

/**
 * Cookies 工具类
 */
@register('ood.Cookies')
export class Cookies {
  /**
   * 设置 Cookie
   * @param {string|Object} name - Cookie 名称或键值对对象
   * @param {*} value - Cookie 值
   * @param {number} days - 过期天数
   * @param {string} path - 路径
   * @param {string} domain - 域名
   * @param {boolean} isSecure - 是否使用 HTTPS
   * @returns {Cookies} 返回 this，支持链式调用
   */
  static set(name, value, days, path, domain, isSecure) {
    const ood = typeof window !== 'undefined' ? window.ood : {};

    // 支持批量设置
    if (ood.isHash && ood.isHash(name)) {
      for (const i in name) {
        this.set(i, name[i], days, path, domain, isSecure);
      }
      return this;
    }

    // 序列化非字符串值
    if (typeof value !== 'string') {
      value = ood.serialize ? ood.serialize(value) : String(value);
    }

    // 构建 Cookie 字符串
    const cookieString = [
      escape(String(name)),
      '=',
      escape(value),
      days ? `; expires=${new Date(Date.now() + days * 24 * 60 * 60 * 1000).toGMTString()}` : '',
      path ? `; path=${path}` : '',
      domain ? `; domain=${domain}` : '',
      isSecure ? '; secure' : ''
    ].join('');

    document.cookie = cookieString;

    return this;
  }

  /**
   * 获取 Cookie
   * @param {string} [name] - Cookie 名称，不提供则返回所有 Cookie
   * @returns {*|Object|null} Cookie 值或所有 Cookie 对象
   */
  static get(name) {
    const ood = typeof window !== 'undefined' ? window.ood : {};

    const ca = document.cookie.split('; ');
    const hash = {};

    // 反序列化函数
    const unserialize = (v) => {
      if (/^\s*\{[\s\S]*\}$/.test(v)) {
        return ood.unserialize ? ood.unserialize(v) : v;
      }
      if (/^\s*\[[\s\S]*\]$/.test(v)) {
        return ood.unserialize ? ood.unserialize(v) : v;
      }
      return v;
    };

    // 解析所有 Cookie
    for (let i = 0; i < ca.length; i++) {
      const parts = ca[i].split('=');
      const cookieName = parts[0];
      const cookieValue = parts[1] ? unescape(parts[1]) : '';
      hash[cookieName] = unserialize(cookieValue) || cookieValue;

      // 如果指定了 name，返回对应的值
      if (name && cookieName === escape(String(name))) {
        return hash[cookieName];
      }
    }

    // 返回所有 Cookie 或 null
    return name ? null : hash;
  }

  /**
   * 删除 Cookie
   * @param {string} name - Cookie 名称
   * @returns {Cookies} 返回 this，支持链式调用
   */
  static remove(name) {
    // 通过设置过期时间为过去来删除
    this.set(name, '', -1).set(name, '/', -1);
    return this;
  }

  /**
   * 清空所有 Cookie
   */
  static clear() {
    const ood = typeof window !== 'undefined' ? window.ood : {};
    
    if (ood.arr && ood.arr.each) {
      ood.arr.each(document.cookie.split(';'), (o) => {
        const cookieName = ood.str && ood.str.trim 
          ? ood.str.trim(o.split('=')[0]) 
          : o.split('=')[0].trim();
        Cookies.remove(cookieName);
      });
    } else {
      // 降级方案
      const cookies = document.cookie.split(';');
      cookies.forEach((cookie) => {
        const cookieName = cookie.split('=')[0].trim();
        Cookies.remove(cookieName);
      });
    }
  }
}

// 默认导出
export default Cookies;
