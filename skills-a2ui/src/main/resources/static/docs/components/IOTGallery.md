# IOTGallery 组件

## 概述

IOTGallery（物联网图库）是一个专门为物联网应用设计的图片库组件，基于 Gallery 组件扩展，增加了 IoT 设备状态显示、远程控制命令集成等功能。它支持设备状态可视化、批量操作、实时状态更新，适用于智能家居、工业监控、设备管理等 IoT 场景。

## 类名

- **完整类名**: `ood.UI.Gallery`
- **别名**: `ood.UI.IconList`（兼容性别名）
- **继承自**: `ood.UI.List`
- **文件位置**: `ood/UI/IOTGallery.js`

## 快速开始

```javascript
// 创建 IoT 设备图库
var iotGallery = ood.UI.Gallery({
    items: [
        {
            id: 'light1',
            caption: '客厅灯',
            imageClass: 'ri-lightbulb-line',
            iotStatus: 'on',
            tagCmds: 'toggle'
        },
        {
            id: 'thermostat1',
            caption: '温控器',
            imageClass: 'ri-thermometer-line',
            iotStatus: 'off',
            tagCmds: 'powerOn,temperatureUp,temperatureDown'
        },
        {
            id: 'camera1',
            caption: '安防摄像头',
            imageClass: 'ri-camera-line',
            iotStatus: 'alarm',
            tagCmds: 'record,stop,zoomIn,zoomOut'
        }
    ],
    columns: 3,
    width: '40em',
    height: '25em',
    responsive: true
}).appendTo('#iot-dashboard');

// 监听设备状态变化
iotGallery.on('onFlagClick', function(profile, item, e, src) {
    console.log('设备状态变化：', item.id, '->', item.iotStatus);
});
```

## API 参考

### iniProp 属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `items` | Array | 包含4个示例项目的数组 | IoT 设备项目列表 |
| `value` | String | `'a'` | 当前选中的设备 ID |
| `width` | String | `'32em'` | 组件宽度 |
| `height` | String | `'20em'` | 组件高度 |

### DataModel 属性

#### IoT 专用属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `iotStatus` | String | `null` | IoT 设备状态：`'on'`, `'off'`, `'none'`, `'alarm'` |
| `tagCmds` | String | `null` | 标签命令，设备支持的控制命令列表 |
| `tagCmdsAlign` | String | `null` | 标签命令对齐方式 |

#### 图片显示属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `bgimg` | String | `null` | 背景图片 URL |
| `autoImgSize` | Boolean | `false` | 是否自动调整图片大小 |
| `autoItemSize` | Boolean | `true` | 是否自动调整项目大小 |
| `iconOnly` | Boolean | `false` | 是否仅显示图标 |
| `iconFontSize` | String | `''` | 图标字体大小 |
| `itemMargin` | Number | `6` | 项目外边距 |
| `itemPadding` | Number | `2` | 项目内边距 |
| `itemWidth` | Number | `32` | 项目宽度（支持空间单位） |
| `itemHeight` | Number | `32` | 项目高度（支持空间单位） |
| `imgWidth` | Number | `16` | 图片宽度 |
| `imgHeight` | Number | `16` | 图片高度 |
| `width` | String | `'16rem'` | 组件宽度 |
| `height` | String | `'16rem'` | 组件高度 |
| `columns` | Number | `0` | 列数（0表示自动） |
| `rows` | Number | `0` | 行数（0表示自动） |

#### 通用属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `expression` | String | `''` | 值表达式，支持动态计算 |

### 实例方法

#### 数据管理
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `getStatus(id)` | `id`: String | String | 获取指定设备的当前状态 |
| `updateItemData(profile, item)` | `profile`: Object, `item`: Object | 无 | 更新设备数据并刷新显示 |
| `_afterInsertItems(profile)` | `profile`: Object | 无 | 插入项目后的处理逻辑（内部使用） |

### 静态方法

#### `_prepareData(profile)`
准备渲染数据，内部使用。

**参数**:
- `profile` (Object): 配置对象

**返回**: Object - 处理后的数据对象

#### `_prepareItem(profile, item)`
准备单个设备项目的数据，根据 `iotStatus` 设置样式。

**参数**:
- `profile` (Object): 配置对象
- `item` (Object): 设备数据

**返回**: 无

### 事件

IOTGallery 支持以下事件：

#### 交互事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onCmd` | 无 | 命令执行时触发 |
| `onFlagClick` | `profile`, `item`, `e`, `src` | 点击设备状态标志时触发 |

## 使用示例

### 示例 1：智能家居控制面板
```javascript
var smartHomePanel = ood.UI.Gallery({
    items: [
        {
            id: 'living_room_light',
            caption: '客厅灯',
            imageClass: 'ri-lightbulb-line',
            iotStatus: 'on',
            tagCmds: 'toggle,dim,bright',
            tagCmdsAlign: 'right'
        },
        {
            id: 'thermostat',
            caption: '空调',
            imageClass: 'ri-thermometer-line',
            iotStatus: 'off',
            tagCmds: 'powerOn,powerOff,tempUp,tempDown',
            tagCmdsAlign: 'bottom'
        },
        {
            id: 'security_camera',
            caption: '摄像头',
            imageClass: 'ri-camera-line',
            iotStatus: 'alarm',
            tagCmds: 'record,stop,zoom,rotate',
            tagCmdsAlign: 'left'
        },
        {
            id: 'smart_lock',
            caption: '智能锁',
            imageClass: 'ri-lock-line',
            iotStatus: 'none',
            tagCmds: 'lock,unlock,changeCode',
            tagCmdsAlign: 'top'
        }
    ],
    columns: 2,
    width: '50em',
    height: '30em',
    autoItemSize: false,
    itemWidth: '12em',
    itemHeight: '10em',
    itemPadding: '1em',
    itemMargin: '0.5em'
}).appendTo('#smart-home');

// 状态颜色映射
smartHomePanel.on('onFlagClick', function(profile, item, e, src) {
    var statusColors = {
        'on': 'var(--ood-status-on)',
        'off': 'var(--ood-status-off)',
        'alarm': 'var(--ood-status-alarm)',
        'none': 'var(--ood-status-neutral)'
    };
    ood(src).css('color', statusColors[item.iotStatus] || 'inherit');
});
```

### 示例 2：工业设备监控
```javascript
var industrialMonitor = ood.UI.Gallery({
    items: [
        {
            id: 'motor_1',
            caption: '电机 #1',
            imageClass: 'ri-cpu-line',
            iotStatus: 'on',
            bgimg: 'gradients/industrial-blue.jpg',
            tagCmds: 'start,stop,reset,faultReset'
        },
        {
            id: 'valve_2',
            caption: '阀门 #2',
            imageClass: 'ri-valve-line',
            iotStatus: 'off',
            bgimg: 'gradients/industrial-gray.jpg',
            tagCmds: 'open,close,lock,unlock'
        },
        {
            id: 'sensor_temp',
            caption: '温度传感器',
            imageClass: 'ri-temp-cold-line',
            iotStatus: 'alarm',
            bgimg: 'gradients/industrial-red.jpg',
            tagCmds: 'calibrate,reset,readValue'
        },
        {
            id: 'pump_main',
            caption: '主泵',
            imageClass: 'ri-oil-line',
            iotStatus: 'none',
            bgimg: 'gradients/industrial-green.jpg',
            tagCmds: 'start,stop,prime,flush'
        }
    ],
    columns: 4,
    width: '100%',
    height: '15em',
    responsiveBreakpoint: 'lg',
    iconFontSize: '2.5em'
}).appendTo('#industrial-monitor');

// 实时状态更新
setInterval(function() {
    // 模拟从 IoT 平台获取设备状态
    var statusUpdate = {
        'motor_1': Math.random() > 0.1 ? 'on' : 'alarm',
        'valve_2': Math.random() > 0.5 ? 'on' : 'off',
        'sensor_temp': Math.random() > 0.2 ? 'none' : 'alarm',
        'pump_main': Math.random() > 0.3 ? 'on' : 'off'
    };
    
    industrialMonitor.each(function(profile) {
        var items = profile.properties.items;
        ood.arr.each(items, function(item) {
            if (statusUpdate[item.id] && statusUpdate[item.id] !== item.iotStatus) {
                item.iotStatus = statusUpdate[item.id];
                industrialMonitor.updateItemData(profile, item);
            }
        });
    });
}, 5000); // 每5秒更新一次
```

### 示例 3：设备批量操作
```javascript
var batchControlPanel = ood.UI.Gallery({
    items: [
        {
            id: 'group_lights',
            caption: '所有灯光',
            imageClass: 'ri-lightbulb-flash-line',
            iotStatus: 'off',
            tagCmds: 'allOn,allOff,dimAll,brightAll'
        },
        {
            id: 'group_security',
            caption: '安防系统',
            imageClass: 'ri-shield-check-line',
            iotStatus: 'on',
            tagCmds: 'armSystem,disarmSystem,silenceAlarm,testSystem'
        },
        {
            id: 'group_climate',
            caption: '环境控制',
            imageClass: 'ri-windy-line',
            iotStatus: 'none',
            tagCmds: 'setAllTemp,humidityControl,airQuality,fansOn'
        }
    ],
    columns: 3,
    width: '45em',
    height: '12em',
    selMode: 'multiple',
    autoItemSize: true
}).appendTo('#batch-control');

// 批量操作处理
document.getElementById('batch-execute').addEventListener('click', function() {
    var selectedItems = batchControlPanel.getSelectedItems();
    if (selectedItems.length === 0) {
        alert('请选择要操作的设备组');
        return;
    }
    
    var command = document.getElementById('batch-command').value;
    if (!command) {
        alert('请选择操作命令');
        return;
    }
    
    // 执行批量操作
    selectedItems.forEach(function(item) {
        console.log('执行批量操作：', item.id, '->', command);
        // 这里可以调用 IoT 平台 API 执行实际命令
    });
});
```

## 注意事项

1. **IoT 状态映射**: `iotStatus` 属性支持四种标准状态：`'on'`（运行）、`'off'`（停止）、`'none'`（中性/待机）、`'alarm'`（警报）。每种状态对应不同的 CSS 变量颜色。

2. **标签命令格式**: `tagCmds` 使用逗号分隔的命令列表，如 `'start,stop,reset'`。命令应简洁明了，便于用户理解和操作。

3. **实时状态更新**: 对于动态 IoT 环境，建议使用轮询或 WebSocket 实时更新设备状态。更新时调用 `updateItemData` 方法刷新显示。

4. **批量操作支持**: 通过设置 `selMode: 'multiple'` 启用多选功能，支持对多个设备执行相同的控制命令。

5. **响应式设计**: 组件内置响应式布局，自动适应不同屏幕尺寸。可以通过 `responsiveBreakpoint` 配置断点行为。

6. **可访问性**: 确保为每个设备项目提供有意义的 `aria-label`，描述设备类型、当前状态和可用操作。

7. **性能优化**: 对于大量 IoT 设备，建议分页显示或使用虚拟滚动，避免一次性渲染过多项目影响性能。

8. **安全性**: IoT 控制命令应经过身份验证和授权检查，防止未授权操作。前端应使用 HTTPS 传输控制指令。

9. **状态持久化**: 设备状态变化可以保存到本地存储或服务器，以便页面刷新后恢复之前的控制状态。

10. **错误处理**: 实施完善的错误处理机制，包括网络中断、设备离线、命令执行失败等情况的用户反馈。