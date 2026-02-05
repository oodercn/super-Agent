# Input 组件

## 概述

Input 是一个功能完整的输入框组件，支持单行文本、多行文本、密码输入、表单验证、掩码格式化和丰富的交互功能。它提供了现代化的输入体验，包括主题切换、响应式设计、可访问性增强和与 Excel 的集成。适用于表单输入、数据录入、搜索框等多种场景。

## 类名

- **完整类名**: `ood.UI.Input`
- **继承自**: `ood.UI.Widget`, `ood.absValue`
- **模板结构**: 多层嵌套的 DOM 结构，包含 FRAME、BORDER、BOX、WRAP、INPUT、LABEL、ERROR 等子节点

## 快速开始

```javascript
// 创建基本输入框
var usernameInput = ood.UI.Input({
    caption: '用户名',
    placeholder: '请输入用户名',
    width: '20em',
    required: true
}).appendTo('#form');

// 创建密码输入框
var passwordInput = ood.UI.Input({
    caption: '密码',
    texttype: 'password',
    placeholder: '请输入密码',
    maxlength: 20
}).appendTo('#form');

// 创建多行文本输入
var descriptionInput = ood.UI.Input({
    caption: '描述',
    multiLines: true,
    autoexpand: '5em',
    width: '30em',
    height: '8em'
}).appendTo('#form');

// 获取和设置值
usernameInput.setUIValue('john_doe');
var currentValue = usernameInput.getUIValue();
```

## API 参考

### iniProp 属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `width` | String | `'18em'` | 输入框宽度 |
| `labelSize` | String | `'6em'` | 标签尺寸 |
| `caption` | String | `"输入框"` | 组件标题 |

### DataModel 属性

#### 基础属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `value` | String | `''` | 输入框的值 |
| `width` | String | `'10em'` | 组件宽度（支持空间单位） |
| `height` | String | `'1.5em'` | 组件高度（支持空间单位） |
| `expression` | String | `''` | 值表达式，支持动态计算 |
| `selectable` | Boolean | `true` | 是否可选 |

#### 标签相关属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `labelSize` | String | `'4'` | 标签大小（支持空间单位） |
| `labelPos` | String | `'left'` | 标签位置：`'none'`, `'left'`, `'top'`, `'right'`, `'bottom'` |
| `labelGap` | String | `'4'` | 标签间距（支持空间单位） |
| `labelCaption` | String | `''` | 标签标题 |
| `labelHAlign` | String | `'right'` | 标签水平对齐：`''`, `'left'`, `'center'`, `'right'` |
| `labelVAlign` | String | `'top'` | 标签垂直对齐：`''`, `'top'`, `'middle'`, `'bottom'` |

#### 输入控制属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `placeholder` | String | `''` | 占位文本 |
| `texttype` | String | `'text'` | 文本类型：`'text'`, `'password'` |
| `maxlength` | Number | `-1` | 最大输入长度（-1 表示无限制） |
| `multiLines` | Boolean | `false` | 是否多行文本 |
| `autoexpand` | String | `''` | 自动扩展高度（支持空间单位） |
| `mask` | String | `''` | 输入掩码格式 |
| `selectOnFocus` | Boolean | `true` | 获得焦点时是否选中文本 |

#### 验证与提示属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `dynCheck` | Boolean | `false` | 是否启用动态检查 |
| `tipsErr` | String | `''` | 错误提示文本 |
| `tipsOK` | String | `''` | 成功提示文本 |
| `tipsBinder` | String | `''` | 提示信息绑定对象 |

#### 状态属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `disabled` | Boolean | `false` | 禁用状态 |
| `readonly` | Boolean | `false` | 只读状态 |
| `hiddenBorder` | Boolean | `false` | 是否隐藏边框 |

#### 显示属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `hAlign` | String | `''` | 水平对齐：`''`, `'left'`, `'center'`, `'right'` |
| `valueFormat` | Object | 包含多种格式验证规则 | 值格式配置 |

#### Excel 集成属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `excelCellId` | String | `''` | Excel 单元格 ID |
| `excelCellFormula` | String | `''` | Excel 公式 |

### 实例方法

#### 主题控制
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `setTheme(theme)` | `theme`: String | `this` | 设置主题：`'light'`, `'dark'`, `'high-contrast'` |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | `this` | 切换暗黑模式 |

#### 交互控制
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `activate(select)` | `select`: Boolean (可选) | `this` | 激活输入框（获得焦点），`select` 控制是否选中文本 |
| `_setTB(type)` | `type`: Number | 无 | 设置提示文本类型（内部使用） |

#### 布局调整
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `getAutoexpandHeight()` | 无 | Number | 获取自动扩展高度 |

### 事件

Input 组件支持丰富的事件系统：

#### 焦点事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `beforeFocus` | `profile` | 获得焦点前触发 |
| `onFocus` | `profile` | 获得焦点时触发 |
| `onBlur` | `profile` | 失去焦点时触发 |
| `onCancel` | `profile` | 取消输入时触发 |

#### 验证事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `beforeFormatCheck` | `profile`, `value` | 格式检查前触发 |
| `beforeFormatMark` | `profile`, `formatErr` | 格式标记前触发 |
| `beforeKeypress` | `profile`, `caret`, `keyboard`, `e`, `src` | 按键前触发 |

#### 标签事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onLabelClick` | `profile`, `e`, `src` | 点击标签时触发 |
| `onLabelDblClick` | `profile`, `e`, `src` | 双击标签时触发 |
| `onLabelActive` | `profile`, `e`, `src` | 标签激活时触发 |

#### Excel 集成事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onGetExcelCellValue` | `profile`, `excelCellId`, `dftValue` | 获取 Excel 单元格值时触发 |
| `beforeApplyExcelFormula` | `profile`, `excelCellFormula`, `value` | 应用 Excel 公式前触发 |
| `afterApplyExcelFormula` | `profile`, `excelCellFormula`, `value` | 应用 Excel 公式后触发 |

#### 自动扩展事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onAutoexpand` | `profile`, `height`, `offset` | 自动扩展高度变化时触发 |

## 使用示例

### 示例 1：用户注册表单
```javascript
var registerForm = ood.UI.Layout({
    items: [
        {
            component: 'Input',
            id: 'username',
            caption: '用户名',
            placeholder: '4-20位字母、数字或下划线',
            required: true,
            width: '25em',
            dynCheck: true,
            tipsOK: '用户名可用',
            tipsErr: '用户名已存在或格式错误'
        },
        {
            component: 'Input',
            id: 'email',
            caption: '邮箱',
            placeholder: 'example@domain.com',
            required: true,
            width: '25em',
            valueFormat: 'email'
        },
        {
            component: 'Input',
            id: 'password',
            caption: '密码',
            texttype: 'password',
            placeholder: '至少6位字符',
            required: true,
            width: '25em',
            maxlength: 30
        }
    ],
    theme: 'light',
    responsive: true
}).appendTo('#register-form');
```

### 示例 2：带掩码的输入框
```javascript
// 手机号输入（带掩码格式）
var phoneInput = ood.UI.Input({
    caption: '手机号',
    placeholder: '138-XXXX-XXXX',
    mask: '999-9999-9999',
    width: '15em',
    required: true
}).appendTo('#contact-form');

// 身份证号输入
var idCardInput = ood.UI.Input({
    caption: '身份证号',
    placeholder: 'XXXXXXXXXXXXXXXXXX',
    mask: '999999-99999999-999X',
    width: '20em',
    maxlength: 18
}).appendTo('#contact-form');
```

### 示例 3：多行文本输入
```javascript
var commentInput = ood.UI.Input({
    caption: '评论',
    multiLines: true,
    autoexpand: '10em',
    placeholder: '请输入您的评论...',
    width: '35em',
    height: '6em',
    maxlength: 500,
    borderType: 'round'
}).appendTo('#comment-section');

// 监听自动扩展
commentInput.on('onAutoexpand', function(profile, height, offset) {
    console.log('输入框高度扩展至：', height, 'px');
});
```

### 示例 4：Excel 集成
```javascript
var excelInput = ood.UI.Input({
    caption: '销售金额',
    excelCellId: 'B2',
    excelCellFormula: '=SUM(C2:C10)',
    width: '12em',
    readonly: true  // Excel 计算的值，用户不能直接修改
}).appendTo('#excel-integration');

// 获取 Excel 单元格值
excelInput.on('onGetExcelCellValue', function(profile, excelCellId, dftValue) {
    // 可以在这里从服务器获取 Excel 数据
    return fetch('/api/excel/cell/' + excelCellId)
        .then(response => response.text())
        .then(value => value || dftValue);
});
```

### 示例 5：主题切换
```javascript
var themedInput = ood.UI.Input({
    caption: '主题化输入框',
    placeholder: '尝试切换主题',
    width: '20em'
}).appendTo('#theme-demo');

// 添加主题切换按钮
document.getElementById('theme-dark').addEventListener('click', function() {
    themedInput.setTheme('dark');
});

document.getElementById('theme-hc').addEventListener('click', function() {
    themedInput.setTheme('high-contrast');
});

document.getElementById('theme-light').addEventListener('click', function() {
    themedInput.setTheme('light');
});

// 自动根据系统主题切换
if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    themedInput.setTheme('dark');
}
```

## 注意事项

1. **验证规则**: Input 组件内置多种验证格式，包括手机号、邮箱、身份证号、URL 等。可以通过 `valueFormat` 属性配置，或使用自定义正则表达式。

2. **掩码输入**: 掩码功能可以帮助用户按照特定格式输入（如日期、电话号）。掩码字符：`9` 表示数字，`A` 表示字母，`*` 表示任意字符。

3. **多行文本**: 启用 `multiLines: true` 后，输入框会转换为 `<textarea>` 元素。可以结合 `autoexpand` 实现自动高度调整。

4. **Excel 集成**: 支持与 Excel 的单元格双向绑定，可以显示 Excel 计算公式的结果。适用于需要与电子表格集成的业务场景。

5. **主题系统**: 支持亮色、暗黑、高对比度三种主题模式。主题变化会通过 CSS 变量应用到所有子元素。

6. **可访问性**: 组件自动为输入框添加 ARIA 属性，支持屏幕阅读器和键盘导航。确保为每个输入框提供有意义的 `aria-label` 或关联的 `<label>`。

7. **性能优化**: 对于大量输入框，建议合理使用 `dynCheck`（动态检查）以避免频繁的验证计算影响性能。

8. **移动端适配**: 组件已针对触摸设备优化，支持虚拟键盘的显示和隐藏事件。

9. **浏览器兼容性**: 支持主流浏览器，包括旧版 IE。复杂的 CSS 特性（如 CSS 变量）在不支持的浏览器中有降级方案。

10. **数据绑定**: 完全支持 A2UI 数据绑定系统，可以与 `DataBinder` 集成实现双向数据绑定和实时验证。