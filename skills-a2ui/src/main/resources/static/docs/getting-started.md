# 快速开始指南

本指南将帮助您快速开始使用 A2UI (ood.js) 框架。

## 🚀 安装

### 方法1：直接引用（传统方式）
下载项目文件并直接引用：

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>My A2UI App</title>
    
    <!-- 引用核心样式 -->
    <link rel="stylesheet" type="text/css" href="path/to/css/default.css"/>
    <link rel="stylesheet" type="text/css" href="path/to/appearance/light/theme.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="path/to/css/remixicon/remixicon.css"/>
    
    <!-- 引用核心框架 -->
    <script type="text/javascript" src="path/to/ood/ood.js"></script>
    
    <!-- 引用需要的UI组件 -->
    <script type="text/javascript" src="path/to/ood/UI/Input.js"></script>
    <script type="text/javascript" src="path/to/ood/UI/ButtonLayout.js"></script>
    <script type="text/javascript" src="path/to/ood/UI/Layout.js"></script>
</head>
<body>
    <div id="app"></div>
    
    <script type="text/javascript">
        // 初始化应用
        ood.launch('app.ooder.Index', function() {
            ood('loading').remove();
            SPA = this;
            SPA.curProjectName = "myapp";
            this.initData();
            
            // 在这里创建您的UI
        }, 'en', 'light');
    </script>
</body>
</html>
```

### 方法2：使用构建版本
使用构建后的版本，位于 `runtime/` 或 `dist/` 目录：

```html
<!-- 使用传统构建版本 -->
<script type="text/javascript" src="path/to/runtime/ood/js/ood.js"></script>
<script type="text/javascript" src="path/to/runtime/ood/js/ood-all.js"></script>

<!-- 或使用现代构建版本 -->
<script type="text/javascript" src="path/to/dist/ood.js"></script>
```

### 方法3：通过 npm（未来支持）
```bash
npm install a2ui-ood
```

```javascript
import 'a2ui-ood/dist/ood.css';
import ood from 'a2ui-ood';
```

## 📦 基本使用

### 1. 创建组件

#### 按钮组件
```javascript
var button = new ood.UI.ButtonLayout({
    text: '点击我',
    type: 'primary',
    icon: 'ri-check-line',
    onClick: function() {
        alert('按钮被点击！');
    }
});
```

#### 输入框组件
```javascript
var input = new ood.UI.Input({
    placeholder: '请输入内容',
    label: '用户名:',
    required: true,
    onInputChange: function(profile, event, value) {
        console.log('输入值:', value);
    }
});
```

#### 面板组件
```javascript
var panel = new ood.UI.Panel({
    title: '我的面板',
    width: '800px',
    height: '600px'
});
```

### 2. 布局管理

#### 简单布局
```javascript
var layout = new ood.UI.Layout({
    container: '#app',
    style: {
        width: '100%',
        height: '100vh',
        padding: '20px'
    }
});
```

#### 添加组件到布局
```javascript
layout.add(panel);
panel.add(input);
panel.add(button);
```

### 3. 渲染组件
```javascript
layout.render();
```

## 🎨 主题系统

### 切换主题
```javascript
// 动态切换主题
function switchTheme(themeName) {
    document.getElementById('theme-style').href = 'appearance/' + themeName + '/theme.css';
}

// 使用示例
switchTheme('dark');  // 切换到深色主题
switchTheme('light'); // 切换到浅色主题
```

### 自定义主题
您可以创建自定义主题：

1. 在 `appearance/` 目录下创建新文件夹，如 `custom/`
2. 创建 `theme.css` 文件
3. 基于现有主题文件进行修改

## 🌐 国际化

### 设置语言
```javascript
// 设置中文
ood.Locale.setLanguage('cn');

// 设置英文
ood.Locale.setLanguage('en');
```

### 获取翻译
```javascript
var translation = ood.Locale.get('button.save');
// 返回当前语言下的翻译文本
```

## 🔧 事件处理

### 组件事件
```javascript
var button = new ood.UI.ButtonLayout({
    text: '保存',
    onClick: function(profile, event) {
        // 事件处理逻辑
        console.log('按钮点击事件', profile, event);
    }
});
```

### 自定义事件
```javascript
// 触发事件
ood.trigger('customEvent', { data: 'example' });

// 监听事件
ood.on('customEvent', function(data) {
    console.log('收到自定义事件:', data);
});
```

## 📁 项目结构

### 核心文件
```
ood/
├── ood.js              # 框架核心
├── UI.js               # UI组件基类
├── UI/                 # 所有UI组件
│   ├── Input.js       # 输入框组件
│   ├── ButtonLayout.js # 按钮组件
│   └── ...
└── mobile/            # 移动端组件
```

### 样式文件
```
css/
├── default.css        # 默认样式
├── mobile.css         # 移动端样式
└── remixicon/         # 图标字体
```

### 主题文件
```
appearance/
├── dark/theme.css     # 深色主题
├── light/theme.css    # 浅色主题
├── high-contrast/     # 高对比度主题
└── purple/            # 紫色主题
```

## 🚨 常见问题

### 1. 组件不显示
- 确保已调用 `render()` 方法
- 检查容器元素是否存在
- 验证组件是否正确添加到布局中

### 2. 样式问题
- 确保正确引用了CSS文件
- 检查主题文件路径
- 验证CSS类名是否正确

### 3. 事件不触发
- 确保事件绑定在组件创建之后
- 检查事件名称是否正确
- 验证事件处理函数是否被正确调用

## 📚 下一步

### 学习更多
- 查看 [组件文档](./components.md) 了解所有可用组件
- 阅读 [API 参考](./api-reference.md) 了解详细API
- 查看 [示例代码](../examples/) 学习实际用法

### 开始开发
1. 选择一个示例作为起点
2. 修改代码实现您的需求
3. 参考文档解决遇到的问题

### 获取帮助
- 在 [GitHub Issues](https://github.com/your-repo/a2ui/issues) 提问
- 查看 [常见问题](./faq.md)
- 参考 [故障排除指南](./troubleshooting.md)

---

恭喜！您已经完成了快速开始指南。现在可以开始使用 A2UI 构建您的应用了！