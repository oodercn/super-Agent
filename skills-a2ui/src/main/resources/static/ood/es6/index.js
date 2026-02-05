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
    console.debug('[es6-index] Initializing ES6 module system...');
  }

  // 获取版本信息
  const version = shim.getVersion();
  if (debug) {
    console.debug('[es6-index] Version:', version);
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
        console.debug('[es6-index] Wrapped legacy classes:', result);
      }
    }
  }

  if (debug) {
    console.debug('[es6-index] Initialization complete');
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
      console.debug('[es6-index] ES6 module system ready');
    });
  } else {
    console.debug('[es6-index] ES6 module system ready');
  }
}
