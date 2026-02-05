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
      // Logging disabled for production
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
    // Logging disabled for production
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
    // Logging disabled for production
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

  console.debug(`[registry] Batch registration complete: ${result.success} success, ${result.failed} failed`);
  
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
