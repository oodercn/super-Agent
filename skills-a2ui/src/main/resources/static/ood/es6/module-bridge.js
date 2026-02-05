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

  console.debug(`[module-bridge] Wrapped ${result.success.length} classes, ${result.failed.length} failed`);
  
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
