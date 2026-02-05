/**
 * ES6 模块兼容层 - 全局对象初始化与桥接
 * 
 * 功能：
 * 1. 初始化全局 ood 对象
 * 2. 提供旧代码到新代码的桥接接口
 * 3. 支持 ES6 模块使用全局 ood 对象
 * 4. 处理新旧模块的相互引用
 */

import { registerModule, getModule, hasModule } from './registry.js';

// 保存对旧 ood 对象的引用（如果已存在）
let existingOod = null;
if (typeof window !== 'undefined' && window.ood) {
  existingOod = window.ood;
}

/**
 * 初始化全局 ood 对象
 * 如果不存在则创建，如果存在则合并
 */
function initializeGlobalOod() {
  if (typeof window === 'undefined') {
    console.warn('[shim] Window object not available, global initialization skipped');
    return null;
  }

  if (!window.ood) {
    window.ood = {};
  }

  // 合并现有 ood 对象的属性
  if (existingOod) {
    Object.keys(existingOod).forEach(key => {
      if (!(key in window.ood)) {
        window.ood[key] = existingOod[key];
      }
    });
  }

  return window.ood;
}

// 初始化
const globalOod = initializeGlobalOod();

/**
 * ES6 类注册装饰器
 * 将 ES6 类自动注册到全局 ood 对象
 * 
 * @param {string} name - 模块名称（如 'ood.Cookies'）
 * @returns {Function} 类装饰器
 * 
 * 使用示例：
 * ```javascript
 * @register('ood.Cookies')
 * export class Cookies {
 *   static get() {}
 * }
 * ```
 */
export function register(name) {
  return function(target) {
    registerModule(name, target);
    return target;
  };
}

/**
 * 手动注册模块（用于非装饰器场景）
 * @param {string} name - 模块名称
 * @param {Function|Object} moduleClass - 模块类或对象
 * @param {Object} options - 注册选项
 * @returns {boolean} 是否注册成功
 */
export function registerClass(name, moduleClass, options) {
  return registerModule(name, moduleClass, options);
}

/**
 * 获取全局 ood 对象
 * @returns {Object} 全局 ood 对象
 */
export function getOod() {
  if (typeof window !== 'undefined') {
    return window.ood;
  }
  console.warn('[shim] Window object not available');
  return {};
}

/**
 * 从全局 ood 获取旧模块
 * @param {string} name - 模块名称
 * @returns {Function|Object|undefined} 模块类或对象
 */
export function getLegacyModule(name) {
  if (typeof window === 'undefined' || !window.ood) {
    return undefined;
  }

  const parts = name.split('.');
  let current = window.ood;

  for (const part of parts) {
    if (current[part] === undefined) {
      return undefined;
    }
    current = current[part];
  }

  return current;
}

/**
 * 混合获取模块（优先从 ES6 注册表，其次从旧全局对象）
 * @param {string} name - 模块名称
 * @returns {Function|Object|undefined} 模块类或对象
 */
export function getModuleAnywhere(name) {
  // 先从 ES6 注册表获取
  const es6Module = getModule(name);
  if (es6Module !== undefined) {
    return es6Module;
  }

  // 再从旧全局对象获取
  return getLegacyModule(name);
}

/**
 * 检查模块是否可用
 * @param {string} name - 模块名称
 * @returns {boolean} 是否可用
 */
export function isModuleAvailable(name) {
  return hasModule(name) || (getLegacyModule(name) !== undefined);
}

/**
 * 创建 ood.Class 的 ES6 包装器
 * 用于在 ES6 模块中使用传统的 ood.Class 系统
 * 
 * @param {string} className - 类名
 * @param {string|Array<string>} parentClass - 父类名
 * @param {Object} classDefinition - 类定义
 * @returns {Function} 构造函数
 */
export function createOodClass(className, parentClass, classDefinition) {
  if (typeof window === 'undefined' || !window.ood || !window.ood.Class) {
    console.error('[shim] ood.Class is not available');
    return null;
  }

  // 调用旧的 ood.Class 创建类
  const ClassConstructor = window.ood.Class(className, parentClass, classDefinition);
  
  // 自动注册到 ES6 模块系统
  if (ClassConstructor) {
    registerModule(className, ClassConstructor);
  }

  return ClassConstructor;
}

/**
 * ES6 类到 ood.Class 的适配器
 * 将 ES6 类包装为兼容 ood.Class 的形式
 * 
 * @param {string} className - 类名
 * @param {string|Array<string>} parentClassName - 父类名
 * @param {Function} es6Class - ES6 类
 * @returns {Function} 包装后的类
 */
export function adaptES6Class(className, parentClassName, es6Class) {
  if (!es6Class) {
    console.error('[shim] ES6 class is required');
    return null;
  }

  // 获取父类
  const parentClass = parentClassName ? getModuleAnywhere(parentClassName) : null;

  // 如果父类存在，建立继承关系
  if (parentClass && typeof parentClass === 'function') {
    Object.setPrototypeOf(es6Class.prototype, parentClass.prototype);
    Object.setPrototypeOf(es6Class, parentClass);
  }

  // 设置类名
  Object.defineProperty(es6Class, 'KEY', {
    value: className,
    writable: false,
    configurable: false
  });

  Object.defineProperty(es6Class.prototype, 'KEY', {
    value: className,
    writable: false,
    configurable: false
  });

  // 注册到全局
  registerModule(className, es6Class);

  return es6Class;
}

/**
 * 等待模块就绪
 * 用于处理异步模块加载的场景
 * 
 * @param {string} name - 模块名称
 * @param {number} timeout - 超时时间（毫秒）
 * @returns {Promise<Function|Object>} 模块对象
 */
export function waitForModule(name, timeout = 5000) {
  return new Promise((resolve, reject) => {
    // 如果已经存在，立即返回
    if (isModuleAvailable(name)) {
      resolve(getModuleAnywhere(name));
      return;
    }

    // 等待模块加载
    const startTime = Date.now();
    const interval = setInterval(() => {
      if (isModuleAvailable(name)) {
        clearInterval(interval);
        resolve(getModuleAnywhere(name));
      } else if (Date.now() - startTime > timeout) {
        clearInterval(interval);
        reject(new Error(`Module "${name}" not available after ${timeout}ms`));
      }
    }, 50);
  });
}

/**
 * 获取版本信息
 * @returns {Object} 版本信息
 */
export function getVersion() {
  return {
    name: 'ood-es6-shim',
    version: '0.5.0',
    oodVersion: typeof window !== 'undefined' && window.ood ? window.ood.version || 'legacy' : 'unknown'
  };
}

// 默认导出全局 ood 对象
export default globalOod;

// 同时导出简化的 API
export const ood = globalOod;

// 导出旧代码中常用的全局函数（兼容性）
export const Class = typeof window !== 'undefined' && window.ood && window.ood.Class 
  ? window.ood.Class.bind(window.ood) 
  : null;
