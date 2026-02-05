# A2UI 组件库文档

本目录包含 A2UI 框架所有 UI 组件的详细使用文档。

## 组件分类

### 基础组件
- [Audio](Audio.md) - 音频播放器组件
- [Block](Block.md) - 通用块容器组件
- [ButtonLayout](ButtonLayout.md) - 按钮布局组件
- [ButtonViews](ButtonViews.md) - 按钮视图组件
- [CheckBox](CheckBox.md) - 复选框组件
- [ColorPicker](ColorPicker.md) - 颜色选择器组件
- [ComboInput](ComboInput.md) - 组合输入框组件
- [ContentBlock](ContentBlock.md) - 内容块组件
- [DatePicker](DatePicker.md) - 日期选择器组件
- [Dialog](Dialog.md) - 对话框组件
- [FileUpload](FileUpload.md) - 文件上传组件
- [Flash](Flash.md) - Flash 播放器组件
- [FoldingList](FoldingList.md) - 折叠列表组件
- [FoldingTabs](FoldingTabs.md) - 折叠标签页组件
- [FormLayout](FormLayout.md) - 表单布局组件
- [Gallery](Gallery.md) - 图片画廊组件
- [Group](Group.md) - 分组容器组件
- [HiddenInput](HiddenInput.md) - 隐藏输入框组件
- [Image](Image.md) - 图片显示组件
- [InfoBlock](InfoBlock.md) - 信息块组件
- [Input](Input.md) - 输入框组件
- [IOTGallery](IOTGallery.md) - IoT 图片画廊组件
- [Label](Label.md) - 标签组件
- [Layout](Layout.md) - 布局组件
- [List](List.md) - 列表组件
- [MDialog](MDialog.md) - 移动端对话框组件
- [MenuBar](MenuBar.md) - 菜单栏组件
- [MFormLayout](MFormLayout.md) - 移动端表单布局组件
- [MTabs](MTabs.md) - 移动端标签页组件
- [MTreeGrid](MTreeGrid.md) - 移动端树形表格组件
- [MTreeView](MTreeView.md) - 移动端树形视图组件
- [Opinion](Opinion.md) - 意见反馈组件
- [PageBar](PageBar.md) - 分页栏组件
- [Panel](Panel.md) - 面板组件
- [PopMenu](PopMenu.md) - 弹出菜单组件
- [ProgressBar](ProgressBar.md) - 进度条组件
- [RadioBox](RadioBox.md) - 单选框组件
- [Resizer](Resizer.md) - 尺寸调整组件
- [RichEditor](RichEditor.md) - 富文本编辑器组件
- [Slider](Slider.md) - 滑块组件
- [Stacks](Stacks.md) - 堆栈组件
- [StatusButtons](StatusButtons.md) - 状态按钮组件
- [SVGPaper](SVGPaper.md) - SVG 画布组件
- [Tabs](Tabs.md) - 标签页组件
- [Tensor](Tensor.md) - 张量显示组件
- [TimePicker](TimePicker.md) - 时间选择器组件
- [TitleBlock](TitleBlock.md) - 标题块组件
- [ToolBar](ToolBar.md) - 工具栏组件
- [TreeBar](TreeBar.md) - 树形导航栏组件
- [TreeGrid](TreeGrid.md) - 树形表格组件
- [TreeView](TreeView.md) - 树形视图组件
- [Video](Video.md) - 视频播放器组件

### 图表组件
- [ECharts](ECharts.md) - ECharts 图表组件（开源）
- [FusionChartsXT](FusionChartsXT.md) - FusionCharts 图表组件（商业许可）
- [SVGPaper](SVGPaper.md) - SVG 绘图组件

### 移动端组件
- [MDialog](MDialog.md) - 移动端对话框
- [MFormLayout](MFormLayout.md) - 移动端表单布局
- [MTabs](MTabs.md) - 移动端标签页
- [MTreeGrid](MTreeGrid.md) - 移动端树形表格
- [MTreeView](MTreeView.md) - 移动端树形视图

## 使用说明

每个组件文档包含以下部分：
1. **组件概述** - 组件功能和用途
2. **快速开始** - 基本使用方法
3. **API 参考** - 所有方法和属性
4. **示例代码** - 完整的使用示例
5. **注意事项** - 使用时的注意事项

## 快速开始

```html
<!-- 引入 ood.js 核心库 -->
<script type="text/javascript" src="ood/ood.js"></script>

<!-- 引入需要的 UI 组件 -->
<script type="text/javascript" src="ood/UI/Audio.js"></script>

<!-- 使用组件 -->
<div id="audio-player"></div>

<script>
// 创建音频播放器
var audio = ood.UI.Audio({
    src: 'audio.mp3',
    autoplay: false,
    controls: true
}).appendTo('#audio-player');
</script>
```

## 贡献文档

如果您发现文档有误或需要补充，欢迎提交 Pull Request 或 Issue。

## 许可证

组件文档与主框架一样，基于 MIT 许可证发布。