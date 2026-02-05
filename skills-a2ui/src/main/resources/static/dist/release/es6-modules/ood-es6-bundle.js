
// ============================================
// OOD ES6 兼容层 Bundle
// 构建时间: 2026-01-04T09:45:05.686Z
// ============================================

// 全局对象初始化
(function() {
  'use strict';
  
  // 保存现有 ood 对象
  const existingOod = typeof window !== 'undefined' ? window.ood : null;
  
  // 创建或获取全局 ood 对象
  window.ood = window.ood || {};
  
  // 合并现有属性
  if (existingOod) {
    Object.keys(existingOod).forEach(key => {
      if (!(key in window.ood)) {
        window.ood[key] = existingOod[key];
      }
    });
  }
  
  console.log('[ood-es6-bundle] ES6兼容层已加载');
})();

// ES6 模块导出

// ============================================
// 模块: index.js
// ============================================
/**
 * ES6 模块系统主入口
 * 
 * 这是 ES6 模块系统的统一入口点，负责：
 * 1. 初始化兼容层
 * 2. 导出核心 API
 * 3. 加载传统 ood 对象（如果存在）
 * 4. 提供统一的模块加载接口
 */

// 导入兼容层
import * as shim from './shim.js';
import * as registry from './registry.js';
import * as bridge from './module-bridge.js';

// 导出兼容层 API
export { shim, registry, bridge };

// 重新导出常用的 API（简化使用）
export {
  register,
  registerClass,
  getOod,
  getLegacyModule,
  getModuleAnywhere,
  isModuleAvailable,
  createOodClass,
  adaptES6Class,
  waitForModule,
  getVersion,
  ood,
  Class
} from './shim.js';

export {
  registerModule,
  getModule,
  getModuleName,
  hasModule,
  unregisterModule,
  listModules,
  clearModules,
  registerBatch,
  registryInstance
} from './registry.js';

export {
  mixin,
  createCompatibleClass,
  wrapLegacyClass,
  wrapLegacyClasses,
  createEventCompat,
  createDataBinderCompat,
  isSubclass
} from './module-bridge.js';

// 获取全局 ood 对象
const globalOod = shim.getOod();

/**
 * ES6 模块加载器
 * 支持动态加载 ES6 模块和传统脚本
 */
class ModuleLoader {
  constructor() {
    this.loadedModules = new Map();
    this.loadingPromises = new Map();
  }

  /**
   * 加载 ES6 模块
   * @param {string} path - 模块路径
   * @returns {Promise<*>} 模块导出
   */
  async loadES6Module(path) {
    if (this.loadedModules.has(path)) {
      return this.loadedModules.get(path);
    }

    if (this.loadingPromises.has(path)) {
      return this.loadingPromises.get(path);
    }

    const promise = import(path).then(module => {
      this.loadedModules.set(path, module);
      return module;
    });

    this.loadingPromises.set(path, promise);
    return promise;
  }

  /**
   * 加载传统脚本
   * @param {string} path - 脚本路径
   * @returns {Promise<void>}
   */
  async loadLegacyScript(path) {
    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = path;
      script.onload = () => resolve();
      script.onerror = () => reject(new Error(`Failed to load script: ${path}`));
      document.head.appendChild(script);
    });
  }

  /**
   * 混合加载模块
   * @param {string} path - 模块路径
   * @param {boolean} useES6 - 是否使用 ES6 模块（默认自动检测）
   * @returns {Promise<*>}
   */
  async load(path, useES6 = null) {
    // 自动检测：如果路径以 .js 或 .mjs 结尾，优先使用 ES6
    const shouldUseES6 = useES6 !== null 
      ? useES6 
      : (path.endsWith('.mjs') || path.endsWith('.js'));

    if (shouldUseES6) {
      return this.loadES6Module(path);
    } else {
      await this.loadLegacyScript(path);
      return null; // 传统脚本返回 null，通过全局对象访问
    }
  }
}

// 创建模块加载器实例
const moduleLoader = new ModuleLoader();

/**
 * 初始化 ES6 模块系统
 * @param {Object} options - 初始化选项
 * @returns {Promise<Object>} 初始化结果
 */
export async function initialize(options = {}) {
  const { 
    autoWrapLegacy = true, 
    legacyModuleNames = [],
    debug = false 
  } = options;

  if (debug) {
    console.log('[es6-index] Initializing ES6 module system...');
  }

  // 获取版本信息
  const version = shim.getVersion();
  if (debug) {
    console.log('[es6-index] Version:', version);
  }

  // 自动包装旧模块
  if (autoWrapLegacy) {
    const legacyClasses = {};
    
    // 如果提供了旧模块名列表，自动包装
    if (legacyModuleNames.length > 0) {
      legacyModuleNames.forEach(name => {
        const legacyClass = shim.getLegacyModule(name);
        if (legacyClass) {
          legacyClasses[name] = legacyClass;
        }
      });

      const result = bridge.wrapLegacyClasses(legacyClasses);
      if (debug) {
        console.log('[es6-index] Wrapped legacy classes:', result);
      }
    }
  }

  if (debug) {
    console.log('[es6-index] Initialization complete');
  }

  return {
    success: true,
    ood: globalOod,
    version
  };
}

/**
 * 获取模块加载器实例
 * @returns {ModuleLoader}
 */
export function getModuleLoader() {
  return moduleLoader;
}

// 默认导出全局 ood 对象
export default globalOod;

// 自动初始化（可选）
if (typeof window !== 'undefined' && window.ood && window.ood.autoInitialize !== false) {
  // 延迟初始化，确保页面加载完成
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
      console.log('[es6-index] ES6 module system ready');
    });
  } else {
    console.log('[es6-index] ES6 module system ready');
  }
}


// ============================================
// 模块: module-bridge.js
// ============================================
/**
 * 新旧模块桥接层
 * 
 * 功能：
 * 1. 处理 ES6 模块与传统 ood.Class 的相互引用
 * 2. 提供 Mixin 支持（模拟多继承）
 * 3. 处理事件系统的兼容性
 * 4. 处理数据绑定的兼容性
 */

import { getModule, registerModule } from './registry.js';
import { getOod, getLegacyModule, getModuleAnywhere } from './shim.js';

/**
 * Mixin 函数
 * 模拟多继承，将多个父类的属性和方法合并到目标类
 * 
 * @param {Function} target - 目标类
 * @param {...Function} parents - 父类列表
 * @returns {Function} 目标类
 * 
 * 使用示例：
 * ```javascript
 * class Button extends mixin(HTMLButton, absValue) {
 *   // Button 的实现
 * }
 * ```
 */
export function mixin(...parents) {
  return function(target) {
    parents.forEach(parent => {
      if (!parent) return;

      // 合并静态属性
      Object.getOwnPropertyNames(parent).forEach(key => {
        if (key === 'length' || key === 'name' || key === 'prototype') return;
        if (!target[key]) {
          Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(parent, key));
        }
      });

      // 合并原型属性
      Object.getOwnPropertyNames(parent.prototype).forEach(key => {
        if (key === 'constructor') return;
        if (!target.prototype[key]) {
          Object.defineProperty(
            target.prototype,
            key,
            Object.getOwnPropertyDescriptor(parent.prototype, key)
          );
        }
      });
    });

    return target;
  };
}

/**
 * 创建兼容旧 ood.Class 系统的 ES6 类
 * 
 * @param {string} className - 类名
 * @param {string|Array<string>} parentClassNames - 父类名（支持多继承）
 * @param {Object} definition - 类定义（包含 Static, Instance 等）
 * @returns {Function} ES6 类
 * 
 * 使用示例：
 * ```javascript
 * const Button = createCompatibleClass('ood.UI.Button', ['ood.UI.HTMLButton', 'ood.absValue'], {
 *   Static: {
 *     staticMethod() {}
 *   },
 *   Instance: {
 *     instanceMethod() {}
 *   }
 * });
 * ```
 */
export function createCompatibleClass(className, parentClassNames, definition = {}) {
  const { Static = {}, Instance = {}, Constructor, Initialize, Before, After } = definition;

  // 处理父类名（支持字符串或数组）
  const parentNames = typeof parentClassNames === 'string' 
    ? [parentClassNames] 
    : parentClassNames;

  // 获取父类
  const parents = parentNames.map(name => {
    const parent = getModuleAnywhere(name);
    if (!parent) {
      console.warn(`[module-bridge] Parent class "${name}" not found`);
    }
    return parent;
  }).filter(p => p);

  // 创建类定义
  const classDefinition = {};

  // 合并静态方法
  if (Static) {
    Object.keys(Static).forEach(key => {
      classDefinition[key] = Static[key];
    });
  }

  // 创建构造函数
  if (typeof Constructor === 'function') {
    classDefinition.constructor = Constructor;
  } else if (parents.length > 0) {
    // 使用第一个父类的构造函数
    classDefinition.constructor = parents[0];
  }

  // 创建类
  let targetClass;

  if (parents.length === 0) {
    // 无父类，直接创建
    targetClass = class {};
  } else if (parents.length === 1) {
    // 单继承
    targetClass = class extends parents[0] {
      constructor(...args) {
        super(...args);
        // 调用 initialize
        if (this.initialize && typeof this.initialize === 'function') {
          this.initialize.apply(this, args);
        }
      }
    };
  } else {
    // 多继承，使用 mixin
    const Base = parents[0];
    const MixinParents = parents.slice(1);
    
    targetClass = class extends Base {};
    targetClass = mixin(...MixinParents)(targetClass);
    
    // 修正 constructor
    const originalProto = targetClass.prototype;
    targetClass = class extends parents[0] {
      constructor(...args) {
        super(...args);
        // 调用 initialize
        if (this.initialize && typeof this.initialize === 'function') {
          this.initialize.apply(this, args);
        }
      }
    };
    // 恢复 prototype
    Object.setPrototypeOf(targetClass.prototype, originalProto);
  }

  // 合并实例方法
  if (Instance) {
    Object.keys(Instance).forEach(key => {
      targetClass.prototype[key] = Instance[key];
    });
  }

  // 执行 Before 钩子
  if (typeof Before === 'function') {
    const result = Before.call(targetClass);
    if (result === false) {
      console.warn(`[module-bridge] Class "${className}" initialization blocked by Before hook`);
      return null;
    }
  }

  // 执行 Initialize 钩子
  if (typeof Initialize === 'function') {
    Initialize.call(targetClass);
  }

  // 设置类名
  Object.defineProperty(targetClass, 'KEY', {
    value: className,
    writable: false,
    configurable: false
  });

  Object.defineProperty(targetClass.prototype, 'KEY', {
    value: className,
    writable: false,
    configurable: false
  });

  // 执行 After 钩子
  if (typeof After === 'function') {
    After.call(targetClass);
  }

  // 注册到全局
  registerModule(className, targetClass);

  return targetClass;
}

/**
 * 包装旧 ood 类为 ES6 模块
 * 
 * @param {string} className - 类名
 * @param {Function} oldClass - 旧的 ood 类
 * @returns {Function} 包装后的类
 */
export function wrapLegacyClass(className, oldClass) {
  if (!oldClass) {
    console.warn(`[module-bridge] Cannot wrap null class: ${className}`);
    return null;
  }

  // 设置类名
  Object.defineProperty(oldClass, 'KEY', {
    value: className,
    writable: false,
    configurable: false
  });

  if (oldClass.prototype) {
    Object.defineProperty(oldClass.prototype, 'KEY', {
      value: className,
      writable: false,
      configurable: false
    });
  }

  // 注册到 ES6 模块系统
  registerModule(className, oldClass);

  return oldClass;
}

/**
 * 事件系统兼容器
 * 处理新旧事件系统的互操作
 * 
 * @param {Object} target - 目标对象
 * @returns {Object} 事件系统接口
 */
export function createEventCompat(target) {
  const oodEvent = getModuleAnywhere('ood.Event');

  return {
    /**
     * 添加事件监听器
     */
    on(event, handler) {
      if (oodEvent) {
        // 使用旧的事件系统
        if (target.$evs) {
          target.$evs(event, handler, 'add');
        }
      } else {
        // 回退到 DOM 事件
        if (target.addEventListener) {
          target.addEventListener(event, handler);
        }
      }
    },

    /**
     * 移除事件监听器
     */
    un(event, handler) {
      if (oodEvent) {
        if (target.$evs) {
          target.$evs(event, handler, 'remove');
        }
      } else {
        if (target.removeEventListener) {
          target.removeEventListener(event, handler);
        }
      }
    },

    /**
     * 触发事件
     */
    fire(event, data) {
      if (oodEvent) {
        if (target.$evs) {
          target.$evs(event, data, 'fire');
        }
      } else {
        // 创建自定义事件
        const customEvent = new CustomEvent(event, { detail: data });
        if (target.dispatchEvent) {
          target.dispatchEvent(customEvent);
        }
      }
    }
  };
}

/**
 * 数据绑定兼容器
 * 处理新旧数据绑定系统的互操作
 * 
 * @param {Object} target - 目标对象
 * @returns {Object} 数据绑定接口
 */
export function createDataBinderCompat(target) {
  const DataBinder = getModuleAnywhere('ood.DataBinder');

  return {
    /**
     * 绑定数据到目标
     */
    bind(source, path) {
      if (DataBinder) {
        const binder = new DataBinder();
        binder.bind(target, source, path);
        return binder;
      } else {
        console.warn('[module-bridge] DataBinder not available, binding skipped');
        return null;
      }
    },

    /**
     * 解除绑定
     */
    unbind() {
      if (target.$binder) {
        target.$binder.destroy();
        target.$binder = null;
      }
    }
  };
}

/**
 * 批量包装旧类
 * 
 * @param {Object} classMap - 类名到旧类的映射
 * @returns {Object} 包装结果
 */
export function wrapLegacyClasses(classMap) {
  const result = {
    success: [],
    failed: []
  };

  Object.keys(classMap).forEach(className => {
    const oldClass = classMap[className];
    if (wrapLegacyClass(className, oldClass)) {
      result.success.push(className);
    } else {
      result.failed.push(className);
    }
  });

  console.log(`[module-bridge] Wrapped ${result.success.length} classes, ${result.failed.length} failed`);
  
  return result;
}

/**
 * 检查类的继承关系
 * 
 * @param {Function} childClass - 子类
 * @param {Function} parentClass - 父类
 * @returns {boolean} 是否存在继承关系
 */
export function isSubclass(childClass, parentClass) {
  if (!childClass || !parentClass) return false;

  // 检查原型链
  let current = childClass;
  while (current) {
    if (current === parentClass) {
      return true;
    }
    current = Object.getPrototypeOf(current);
  }

  // 检查原型
  current = childClass.prototype;
  while (current) {
    if (current === parentClass.prototype) {
      return true;
    }
    current = Object.getPrototypeOf(current);
  }

  return false;
}

// 默认导出
export default {
  mixin,
  createCompatibleClass,
  wrapLegacyClass,
  wrapLegacyClasses,
  createEventCompat,
  createDataBinderCompat,
  isSubclass
};


// ============================================
// 模块: registry.js
// ============================================
/**
 * 全局模块注册表
 * 
 * 功能：
 * 1. 管理 ES6 模块到全局 ood 对象的注册
 * 2. 提供模块查询和获取功能
 * 3. 处理模块名称冲突
 * 4. 支持双向查询（模块名 <-> 类对象）
 */

const registry = new Map();
const classToNameMap = new Map();

/**
 * 注册模块到全局 ood 对象
 * @param {string} name - 模块名称（如 'ood.Cookies'）
 * @param {Function|Object} moduleClass - 模块类或对象
 * @param {Object} options - 注册选项
 * @param {boolean} options.override - 是否覆盖已存在的模块（默认 false）
 * @param {boolean} options.silent - 是否静默处理冲突（默认 false）
 * @returns {boolean} 是否注册成功
 */
export function registerModule(name, moduleClass, options = {}) {
  const { override = false, silent = false } = options;

  // 参数校验
  if (typeof name !== 'string' || !name.trim()) {
    if (!silent) console.error('[registry] Invalid module name:', name);
    return false;
  }

  if (!moduleClass) {
    if (!silent) console.error('[registry] Module class is required for:', name);
    return false;
  }

  // 检查是否已存在
  if (registry.has(name) && !override) {
    const existing = registry.get(name);
    if (!silent) {
      console.warn(
        `[registry] Module "${name}" already exists. Use { override: true } to replace.`
      );
    }
    return false;
  }

  // 注册到 Map
  registry.set(name, moduleClass);
  classToNameMap.set(moduleClass, name);

  // 注册到全局 ood 对象（如果存在）
  if (typeof window !== 'undefined' && window.ood) {
    const parts = name.split('.');
    let current = window.ood;

    // 创建命名空间
    for (let i = 0; i < parts.length - 1; i++) {
      const part = parts[i];
      if (!current[part]) {
        current[part] = {};
      }
      current = current[part];
    }

    // 设置最终值
    const lastPart = parts[parts.length - 1];
    current[lastPart] = moduleClass;

    if (!silent) {
      console.log(`[registry] Module "${name}" registered to ood.${parts.join('.')}`);
    }
  } else if (!silent) {
    console.warn('[registry] Global ood object not found, module only stored in registry');
  }

  return true;
}

/**
 * 获取已注册的模块
 * @param {string} name - 模块名称
 * @returns {Function|Object|undefined} 模块类或对象
 */
export function getModule(name) {
  return registry.get(name);
}

/**
 * 根据类对象获取模块名称
 * @param {Function|Object} moduleClass - 模块类或对象
 * @returns {string|undefined} 模块名称
 */
export function getModuleName(moduleClass) {
  return classToNameMap.get(moduleClass);
}

/**
 * 检查模块是否已注册
 * @param {string} name - 模块名称
 * @returns {boolean} 是否已注册
 */
export function hasModule(name) {
  return registry.has(name);
}

/**
 * 移除已注册的模块
 * @param {string} name - 模块名称
 * @param {Object} options - 移除选项
 * @param {boolean} options.silent - 是否静默处理（默认 false）
 * @returns {boolean} 是否移除成功
 */
export function unregisterModule(name, options = {}) {
  const { silent = false } = options;

  if (!registry.has(name)) {
    if (!silent) console.warn(`[registry] Module "${name}" not found`);
    return false;
  }

  const moduleClass = registry.get(name);

  // 从全局 ood 对象中移除
  if (typeof window !== 'undefined' && window.ood) {
    const parts = name.split('.');
    let current = window.ood;

    for (let i = 0; i < parts.length - 1; i++) {
      current = current[parts[i]];
      if (!current) break;
    }

    if (current && parts[parts.length - 1] in current) {
      delete current[parts[parts.length - 1]];
    }
  }

  // 从 Map 中移除
  registry.delete(name);
  classToNameMap.delete(moduleClass);

  if (!silent) {
    console.log(`[registry] Module "${name}" unregistered`);
  }

  return true;
}

/**
 * 列出所有已注册的模块
 * @param {Object} filter - 过滤条件
 * @param {string} filter.prefix - 模块名称前缀（如 'ood.UI'）
 * @returns {Array<string>} 模块名称列表
 */
export function listModules(filter = {}) {
  const { prefix } = filter;

  let modules = Array.from(registry.keys());

  if (prefix) {
    modules = modules.filter(name => name.startsWith(prefix));
  }

  return modules.sort();
}

/**
 * 清空所有已注册的模块
 * @param {Object} options - 清空选项
 * @param {boolean} options.silent - 是否静默处理（默认 false）
 */
export function clearModules(options = {}) {
  const { silent = false } = options;

  registry.clear();
  classToNameMap.clear();

  if (!silent) {
    console.log('[registry] All modules cleared');
  }
}

/**
 * 批量注册模块
 * @param {Array<{name: string, module: Function|Object}>} modules - 模块列表
 * @param {Object} options - 注册选项
 * @returns {Object} 注册结果统计 { success: number, failed: number, errors: Array }
 */
export function registerBatch(modules, options = {}) {
  const result = {
    success: 0,
    failed: 0,
    errors: []
  };

  modules.forEach(({ name, module }) => {
    const success = registerModule(name, module, { silent: true, ...options });
    if (success) {
      result.success++;
    } else {
      result.failed++;
      result.errors.push({ name, reason: 'Registration failed' });
    }
  });

  console.log(`[registry] Batch registration complete: ${result.success} success, ${result.failed} failed`);
  
  if (result.failed > 0 && !options.silent) {
    console.warn('[registry] Failed modules:', result.errors);
  }

  return result;
}

// 导出注册表实例（用于调试）
export const registryInstance = registry;

// 默认导出
export default {
  registerModule,
  getModule,
  getModuleName,
  hasModule,
  unregisterModule,
  listModules,
  clearModules,
  registerBatch,
  registryInstance
};


// ============================================
// 模块: shim.js
// ============================================
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
  console.log('[shim] Existing ood object detected');
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
    console.log('[shim] Global ood object created');
  }

  // 合并现有 ood 对象的属性
  if (existingOod) {
    Object.keys(existingOod).forEach(key => {
      if (!(key in window.ood)) {
        window.ood[key] = existingOod[key];
      }
    });
    console.log('[shim] Merged existing ood object');
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


// ============================================
// 默认导出
// ============================================
export default window.ood;
export { window.ood as ood };
