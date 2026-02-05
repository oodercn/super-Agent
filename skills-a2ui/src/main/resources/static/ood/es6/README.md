# OOD ES6 模块兼容层

## 概述

本兼容层提供了在传统 `ood.Class` 系统和 ES6 模块系统之间的桥梁，支持新旧代码的和平共存与渐进式迁移。

## 目录结构

```
ood/es6/
├── index.js          # 主入口，导出所有 API
├── shim.js           # 全局对象初始化与桥接
├── registry.js       # 全局模块注册表
├── module-bridge.js  # 新旧模块桥接
└── README.md         # 本文档
```

## 核心功能

### 1. 全局模块注册表 (`registry.js`)

管理 ES6 模块到全局 `ood` 对象的注册。

```javascript
import { registerModule, getModule } from './es6/registry.js';

// 注册模块
registerModule('ood.MyModule', MyModuleClass);

// 获取模块
const MyModule = getModule('ood.MyModule');
```

### 2. 兼容层 (`shim.js`)

提供旧代码到新代码的桥接接口。

```javascript
import { getOod, getLegacyModule, getModuleAnywhere } from './es6/shim.js';

// 获取全局 ood 对象
const ood = getOod();

// 获取旧模块
const Cookies = getLegacyModule('ood.Cookies');

// 混合获取（优先 ES6，其次旧模块）
const Button = getModuleAnywhere('ood.UI.Button');
```

### 3. 模块桥接 (`module-bridge.js`)

处理新旧模块的相互引用和多继承。

```javascript
import { mixin, createCompatibleClass } from './es6/module-bridge.js';

// 使用 mixin 模拟多继承
class Button extends mixin(HTMLButton, absValue) {
  // Button 的实现
}

// 创建兼容旧系统的类
const MyButton = createCompatibleClass('ood.UI.Button', ['ood.UI.HTMLButton', 'ood.absValue'], {
  Static: {
    staticMethod() {}
  },
  Instance: {
    instanceMethod() {}
  }
});
```

## 使用方式

### 方式 1: 使用装饰器自动注册

```javascript
import { register } from './es6/shim.js';

@register('ood.Cookies')
export class Cookies {
  static get(name) {
    // 获取 cookie
  }

  static set(name, value) {
    // 设置 cookie
  }
}
```

### 方式 2: 手动注册

```javascript
import { registerClass } from './es6/shim.js';

export class Cookies {
  static get(name) {
    // 获取 cookie
  }
}

// 手动注册
registerClass('ood.Cookies', Cookies);
```

### 方式 3: 创建兼容旧系统的类

```javascript
import { createCompatibleClass } from './es6/module-bridge.js';

export const Button = createCompatibleClass('ood.UI.Button', ['ood.UI.HTMLButton', 'ood.absValue'], {
  Static: {
    staticMethod() {
      console.log('Static method');
    }
  },
  Instance: {
    instanceMethod() {
      console.log('Instance method');
    }
  }
});

// 使用
const button = new Button();
button.instanceMethod();
Button.staticMethod();
```

### 方式 4: 使用旧 ood.Class 系统

```javascript
import { createOodClass } from './es6/shim.js';

export const MyModule = createOodClass('ood.MyModule', 'ood.Module', {
  Static: {
    // 静态方法
  },
  Instance: {
    // 实例方法
  }
});
```

### 方式 5: 包装旧类

```javascript
import { wrapLegacyClass } from './es6/module-bridge.js';

// 包装已存在的旧类
const LegacyButton = window.ood.UI.Button;
const WrappedButton = wrapLegacyClass('ood.UI.Button', LegacyButton);
```

## 新旧代码互操作

### 在 ES6 模块中使用旧模块

```javascript
import { getLegacyModule } from './es6/shim.js';

// 获取旧模块
const Cookies = getLegacyModule('ood.Cookies');

// 直接使用
Cookies.set('name', 'value');
```

### 在旧代码中使用 ES6 模块

```javascript
// ES6 模块会被自动注册到全局 ood 对象
// 可以直接使用

// 例如：如果注册了 'ood.NewFeature'
const newFeature = new ood.NewFeature();
```

## API 参考

### registry.js

| 函数 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `registerModule(name, moduleClass, options)` | name, moduleClass, options | boolean | 注册模块 |
| `getModule(name)` | name | Function\|Object | 获取模块 |
| `getModuleName(moduleClass)` | moduleClass | string | 获取模块名 |
| `hasModule(name)` | name | boolean | 检查模块是否存在 |
| `unregisterModule(name, options)` | name, options | boolean | 移除模块 |
| `listModules(filter)` | filter | Array | 列出所有模块 |
| `clearModules(options)` | options | void | 清空所有模块 |
| `registerBatch(modules, options)` | modules, options | Object | 批量注册 |

### shim.js

| 函数 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `register(name)` | name | Function | 装饰器函数 |
| `registerClass(name, moduleClass, options)` | name, moduleClass, options | boolean | 手动注册类 |
| `getOod()` | - | Object | 获取全局 ood |
| `getLegacyModule(name)` | name | Function\|Object | 获取旧模块 |
| `getModuleAnywhere(name)` | name | Function\|Object | 混合获取 |
| `isModuleAvailable(name)` | name | boolean | 检查模块可用性 |
| `createOodClass(className, parentClass, classDefinition)` | className, parentClass, classDefinition | Function | 创建旧风格类 |
| `adaptES6Class(className, parentClassName, es6Class)` | className, parentClassName, es6Class | Function | 适配 ES6 类 |
| `waitForModule(name, timeout)` | name, timeout | Promise | 等待模块加载 |
| `getVersion()` | - | Object | 获取版本信息 |

### module-bridge.js

| 函数 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `mixin(...parents)` | parents | Function | Mixin 函数 |
| `createCompatibleClass(className, parentClassNames, definition)` | className, parentClassNames, definition | Function | 创建兼容类 |
| `wrapLegacyClass(className, oldClass)` | className, oldClass | Function | 包装旧类 |
| `wrapLegacyClasses(classMap)` | classMap | Object | 批量包装 |
| `createEventCompat(target)` | target | Object | 创建事件兼容接口 |
| `createDataBinderCompat(target)` | target | Object | 创建数据绑定兼容接口 |
| `isSubclass(childClass, parentClass)` | childClass, parentClass | boolean | 检查继承关系 |

## 迁移示例

### 示例 1: 迁移 Cookies.js

**旧代码:**
```javascript
ood.Class('ood.Cookies', null, {
    Static: {
        get: function(name) {
            // 实现
        },
        set: function(name, value) {
            // 实现
        }
    }
});
```

**新代码:**
```javascript
import { register } from './es6/shim.js';

@register('ood.Cookies')
export class Cookies {
    static get(name) {
        // 实现
    }

    static set(name, value) {
        // 实现
    }
}
```

### 示例 2: 迁移有继承的类

**旧代码:**
```javascript
ood.Class('ood.UI.Button', ['ood.UI.HTMLButton', 'ood.absValue'], {
    Instance: {
        onClick: function() {
            // 实现
        }
    }
});
```

**新代码:**
```javascript
import { createCompatibleClass } from './es6/module-bridge.js';

export const Button = createCompatibleClass('ood.UI.Button', ['ood.UI.HTMLButton', 'ood.absValue'], {
    Instance: {
        onClick() {
            // 实现
        }
    }
});
```

### 示例 3: 在新模块中使用旧模块

```javascript
import { getLegacyModule } from './es6/shim.js';

// 获取旧的 Cookies 模块
const Cookies = getLegacyModule('ood.Cookies');

export class NewFeature {
    saveSettings(settings) {
        Cookies.set('settings', JSON.stringify(settings));
    }
}
```

## 注意事项

1. **保持向后兼容**: 兼容层不会修改旧的 `ood.Class` 系统，所有旧代码继续正常工作。

2. **模块命名**: ES6 模块的名称必须与命名空间一致（如 `ood.Cookies`）。

3. **多继承**: ES6 不支持多继承，使用 `mixin` 函数模拟。

4. **事件系统**: 使用 `createEventCompat` 创建兼容的事件接口。

5. **数据绑定**: 使用 `createDataBinderCompat` 创建兼容的数据绑定接口。

6. **异步加载**: 使用 `waitForModule` 处理异步模块加载场景。

## 调试

启用调试模式：

```javascript
import { initialize } from './es6/index.js';

// 启用调试模式
await initialize({
    debug: true,
    autoWrapLegacy: true
});
```

## 版本信息

- 版本: 0.5.0
- 兼容: OOD 2.1+
- 最低浏览器: ES6 支持
