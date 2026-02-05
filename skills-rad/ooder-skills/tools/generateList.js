#!/usr/bin/env node

/**
 * List组件生成工具
 * 用于将自然语言描述转换为Ooder List组件代码
 * 参考了JDSEasy RAD Studio生成的Grid.js结构
 */

const fs = require('fs');
const path = require('path');

/**
 * 主函数
 */
function main() {
    // 获取命令行参数
    const args = process.argv.slice(2);
    
    if (args.length < 2) {
        console.error('用法: node generateList.js <自然语言描述> <输出文件路径>');
        console.error('示例: node generateList.js "类名：TestList，标题：测试列表" TestList.js');
        process.exit(1);
    }
    
    const description = args[0];
    const outputPath = path.resolve(args[1]);
    
    // 解析自然语言描述
    const parsed = parseDescription(description);
    
    // 生成代码
    const code = generateListCode(parsed);
    
    // 保存到文件
    fs.writeFileSync(outputPath, code, 'utf8');
    console.log(`文件已生成: ${outputPath}`);
}

/**
 * 解析自然语言描述
 */
function parseDescription(description) {
    const result = {
        className: 'ooder.DefaultList',
        caption: '列表示例',
        alias: 'Search',
        service: 'net.ooder.test.TestService',
        scenario: 'default', // 默认场景
        columns: null // 用户指定的列
    };
    
    // 特殊处理列信息，因为它可能包含逗号
    const columnsKey = description.indexOf('包含哪些列：');
    const columnsKey2 = description.indexOf('包含哪些列:');
    const columnsKey3 = description.indexOf('列：');
    const columnsKey4 = description.indexOf('列:');
    
    // 找到列信息的位置
    const columnsStartIndex = Math.max(columnsKey, columnsKey2, columnsKey3, columnsKey4);
    
    if (columnsStartIndex !== -1) {
        // 确定列信息的前缀长度
        let prefixLength = 0;
        if (columnsKey === columnsStartIndex) prefixLength = 6;
        else if (columnsKey2 === columnsStartIndex) prefixLength = 6;
        else if (columnsKey3 === columnsStartIndex) prefixLength = 2;
        else if (columnsKey4 === columnsStartIndex) prefixLength = 2;
        
        // 提取列信息的内容
        let columnsContent = description.substring(columnsStartIndex + prefixLength);
        
        // 查找下一个字段的开始位置（即下一个键值对）
        const nextFieldPattern = /[,，]\s*(类名|标题|别名|服务|场景|包含哪些列|列)[:：]/;
        const nextFieldMatch = columnsContent.match(nextFieldPattern);
        
        if (nextFieldMatch) {
            // 只保留到下一个字段开始之前的内容
            columnsContent = columnsContent.substring(0, nextFieldMatch.index);
        }
        
        // 解析列名列表
        result.columns = columnsContent.split(/[,，、\s]+/).filter(col => col.length > 0);
        
        // 移除列信息部分，避免影响后续解析
        description = description.substring(0, columnsStartIndex) + description.substring(columnsStartIndex + prefixLength + columnsContent.length);
    }
    
    // 解析剩余的键值对
    const otherFields = description.split(/[,，]/);
    
    otherFields.forEach(field => {
        field = field.trim();
        if (!field) return;
        
        const match = field.match(/^(.+?)[:：]\s*(.+)$/);
        if (!match) return;
        
        const type = match[1];
        const content = match[2].trim();
        
        if (type === '类名') {
            result.className = `ooder.${content}`;
        } else if (type === '标题') {
            result.caption = content;
            // 根据标题自动识别场景
            if (content.includes('课表') || content.includes('课程')) {
                result.scenario = 'course';
            } else if (content.includes('学生')) {
                result.scenario = 'student';
            } else if (content.includes('用户')) {
                result.scenario = 'user';
            } else if (content.includes('订单')) {
                result.scenario = 'order';
            }
        } else if (type === '别名') {
            result.alias = content;
        } else if (type === '服务') {
            result.service = content;
        } else if (type === '场景') {
            result.scenario = content;
        }
    });
    
    return result;
}

/**
 * 列名到配置的映射
 */
const columnConfigMap = {
    "学生ID": {
        caption: "学生ID",
        hidden: true,
        id: "studentId",
        tips: "学生ID",
        title: "学生ID",
        type: "input",
        width: "8em"
    },
    "ID": {
        caption: "ID",
        hidden: true,
        id: "id",
        tips: "ID",
        title: "ID",
        type: "input",
        width: "8em"
    },
    "姓名": {
        caption: "姓名",
        hidden: false,
        id: "name",
        tips: "姓名",
        title: "姓名",
        type: "input",
        width: "12em"
    },
    "科目": {
        caption: "科目",
        hidden: false,
        id: "subject",
        tips: "科目",
        title: "科目",
        type: "input",
        width: "15em"
    },
    "课程名称": {
        caption: "课程名称",
        hidden: false,
        id: "courseName",
        tips: "课程名称",
        title: "课程名称",
        type: "input",
        width: "18em"
    },
    "上课时间": {
        caption: "上课时间",
        hidden: false,
        id: "courseTime",
        tips: "上课时间",
        title: "上课时间",
        type: "input",
        width: "15em"
    },
    "上课地点": {
        caption: "上课地点",
        hidden: false,
        id: "location",
        tips: "上课地点",
        title: "上课地点",
        type: "input",
        width: "12em"
    },
    "授课教师": {
        caption: "授课教师",
        hidden: false,
        id: "teacher",
        tips: "授课教师",
        title: "授课教师",
        type: "input",
        width: "12em"
    },
    "学分": {
        caption: "学分",
        hidden: false,
        id: "credit",
        tips: "学分",
        title: "学分",
        type: "spin",
        width: "8em"
    },
    "性别": {
        caption: "性别",
        hidden: false,
        id: "sex",
        tips: "性别",
        title: "性别",
        type: "input",
        width: "8em"
    },
    "年龄": {
        caption: "年龄",
        hidden: false,
        id: "age",
        tips: "年龄",
        title: "年龄",
        type: "spin",
        width: "8em"
    },
    "班级": {
        caption: "班级",
        hidden: false,
        id: "className",
        tips: "班级",
        title: "班级",
        type: "input",
        width: "15em"
    },
    "专业": {
        caption: "专业",
        hidden: false,
        id: "major",
        tips: "专业",
        title: "专业",
        type: "input",
        width: "15em"
    },
    "用户名": {
        caption: "用户名",
        hidden: false,
        id: "username",
        tips: "用户名",
        title: "用户名",
        type: "input",
        width: "12em"
    },
    "邮箱": {
        caption: "邮箱",
        hidden: false,
        id: "email",
        tips: "邮箱",
        title: "邮箱",
        type: "input",
        width: "18em"
    },
    "角色": {
        caption: "角色",
        hidden: false,
        id: "role",
        tips: "角色",
        title: "角色",
        type: "input",
        width: "12em"
    },
    "状态": {
        caption: "状态",
        hidden: false,
        id: "status",
        tips: "状态",
        title: "状态",
        type: "input",
        width: "10em"
    },
    "订单编号": {
        caption: "订单编号",
        hidden: false,
        id: "orderNo",
        tips: "订单编号",
        title: "订单编号",
        type: "input",
        width: "18em"
    },
    "客户名称": {
        caption: "客户名称",
        hidden: false,
        id: "customerName",
        tips: "客户名称",
        title: "客户名称",
        type: "input",
        width: "15em"
    },
    "订单金额": {
        caption: "订单金额",
        hidden: false,
        id: "amount",
        tips: "订单金额",
        title: "订单金额",
        type: "input",
        width: "12em"
    },
    "创建时间": {
        caption: "创建时间",
        hidden: false,
        id: "createTime",
        tips: "创建时间",
        title: "创建时间",
        type: "input",
        width: "15em"
    }
};

/**
 * 根据用户指定的列生成列配置
 */
function getColumnsByUserInput(columns) {
    // 生成用户指定的列
    const userColumns = columns.map(colName => {
        return columnConfigMap[colName] || {
            caption: colName,
            hidden: false,
            id: colName.toLowerCase().replace(/\s+/g, ''),
            tips: colName,
            title: colName,
            type: "input",
            width: "12em"
        };
    });
    
    // 转换为字符串，简化缩进处理
    const columnsString = userColumns.map(col => {
        const colStr = JSON.stringify(col, null, 4);
        return colStr.split('\n').map(line => `                ${line}`).join('\n');
    }).join(',\n');
    
    return `                .setColumns([
${columnsString}
                ])`;
}

/**
 * 根据场景获取列配置
 */
function getColumnsByScenario(scenario) {
    switch (scenario) {
        case 'course': // 学生课表场景
            return `                .setColumns([
                    {
                        "caption": "课程ID",
                        "hidden": true,
                        "id": "courseId",
                        "tips": "课程ID",
                        "title": "课程ID",
                        "type": "input",
                        "width": "8em"
                    },
                    {
                        "caption": "课程名称",
                        "hidden": false,
                        "id": "courseName",
                        "tips": "课程名称",
                        "title": "课程名称",
                        "type": "input",
                        "width": "18em"
                    },
                    {
                        "caption": "上课时间",
                        "hidden": false,
                        "id": "courseTime",
                        "tips": "上课时间",
                        "title": "上课时间",
                        "type": "input",
                        "width": "15em"
                    },
                    {
                        "caption": "上课地点",
                        "hidden": false,
                        "id": "location",
                        "tips": "上课地点",
                        "title": "上课地点",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "授课教师",
                        "hidden": false,
                        "id": "teacher",
                        "tips": "授课教师",
                        "title": "授课教师",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "学分",
                        "hidden": false,
                        "id": "credit",
                        "tips": "学分",
                        "title": "学分",
                        "type": "spin",
                        "width": "8em"
                    }
                ])`;
        
        case 'student': // 学生信息场景
            return `                .setColumns([
                    {
                        "caption": "学生ID",
                        "hidden": true,
                        "id": "studentId",
                        "tips": "学生ID",
                        "title": "学生ID",
                        "type": "input",
                        "width": "8em"
                    },
                    {
                        "caption": "姓名",
                        "hidden": false,
                        "id": "name",
                        "tips": "姓名",
                        "title": "姓名",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "性别",
                        "hidden": false,
                        "id": "sex",
                        "tips": "性别",
                        "title": "性别",
                        "type": "input",
                        "width": "8em"
                    },
                    {
                        "caption": "年龄",
                        "hidden": false,
                        "id": "age",
                        "tips": "年龄",
                        "title": "年龄",
                        "type": "spin",
                        "width": "8em"
                    },
                    {
                        "caption": "班级",
                        "hidden": false,
                        "id": "className",
                        "tips": "班级",
                        "title": "班级",
                        "type": "input",
                        "width": "15em"
                    },
                    {
                        "caption": "专业",
                        "flexSize": true,
                        "hidden": false,
                        "id": "major",
                        "tips": "专业",
                        "title": "专业",
                        "type": "input",
                        "width": "15em"
                    }
                ])`;
        
        case 'user': // 用户信息场景
            return `                .setColumns([
                    {
                        "caption": "用户ID",
                        "hidden": true,
                        "id": "userId",
                        "tips": "用户ID",
                        "title": "用户ID",
                        "type": "input",
                        "width": "8em"
                    },
                    {
                        "caption": "用户名",
                        "hidden": false,
                        "id": "username",
                        "tips": "用户名",
                        "title": "用户名",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "姓名",
                        "hidden": false,
                        "id": "name",
                        "tips": "姓名",
                        "title": "姓名",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "邮箱",
                        "hidden": false,
                        "id": "email",
                        "tips": "邮箱",
                        "title": "邮箱",
                        "type": "input",
                        "width": "18em"
                    },
                    {
                        "caption": "角色",
                        "hidden": false,
                        "id": "role",
                        "tips": "角色",
                        "title": "角色",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "状态",
                        "flexSize": true,
                        "hidden": false,
                        "id": "status",
                        "tips": "状态",
                        "title": "状态",
                        "type": "input",
                        "width": "10em"
                    }
                ])`;
        
        case 'order': // 订单信息场景
            return `                .setColumns([
                    {
                        "caption": "订单ID",
                        "hidden": true,
                        "id": "orderId",
                        "tips": "订单ID",
                        "title": "订单ID",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "订单编号",
                        "hidden": false,
                        "id": "orderNo",
                        "tips": "订单编号",
                        "title": "订单编号",
                        "type": "input",
                        "width": "18em"
                    },
                    {
                        "caption": "客户名称",
                        "hidden": false,
                        "id": "customerName",
                        "tips": "客户名称",
                        "title": "客户名称",
                        "type": "input",
                        "width": "15em"
                    },
                    {
                        "caption": "订单金额",
                        "hidden": false,
                        "id": "amount",
                        "tips": "订单金额",
                        "title": "订单金额",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "订单状态",
                        "hidden": false,
                        "id": "status",
                        "tips": "订单状态",
                        "title": "订单状态",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "创建时间",
                        "flexSize": true,
                        "hidden": false,
                        "id": "createTime",
                        "tips": "创建时间",
                        "title": "创建时间",
                        "type": "input",
                        "width": "15em"
                    }
                ])`;
        
        default: // 默认场景
            return `                .setColumns([
                    {
                        "caption": "ID",
                        "hidden": true,
                        "id": "id",
                        "tips": "ID",
                        "title": "ID",
                        "type": "input",
                        "width": "8em"
                    },
                    {
                        "caption": "名称",
                        "hidden": false,
                        "id": "name",
                        "tips": "名称",
                        "title": "名称",
                        "type": "input",
                        "width": "18em"
                    },
                    {
                        "caption": "类型",
                        "hidden": false,
                        "id": "type",
                        "tips": "类型",
                        "title": "类型",
                        "type": "input",
                        "width": "12em"
                    },
                    {
                        "caption": "创建时间",
                        "hidden": false,
                        "id": "createTime",
                        "tips": "创建时间",
                        "title": "创建时间",
                        "type": "input",
                        "width": "15em"
                    },
                    {
                        "caption": "状态",
                        "flexSize": true,
                        "hidden": false,
                        "id": "status",
                        "tips": "状态",
                        "title": "状态",
                        "type": "input",
                        "width": "10em"
                    }
                ])`;
    }
}

/**
 * 根据场景获取主键列
 */
function getUidColumnByScenario(scenario) {
    switch (scenario) {
        case 'course':
            return 'courseId';
        case 'student':
            return 'studentId';
        case 'user':
            return 'userId';
        case 'order':
            return 'orderId';
        default:
            return 'id';
    }
}

/**
 * 生成List组件代码
 */
function generateListCode(parsed) {
    // 优先使用用户指定的列配置，如果没有则使用场景默认配置
    let columnsCode;
    let uidColumn;
    
    if (parsed.columns && parsed.columns.length > 0) {
        // 使用用户指定的列
        columnsCode = getColumnsByUserInput(parsed.columns);
        
        // 自动选择主键列（优先选择包含ID的列）
        const idColumns = parsed.columns.filter(col => col.includes('ID') || col.includes('id'));
        if (idColumns.length > 0) {
            const idColName = idColumns[0];
            uidColumn = columnConfigMap[idColName] ? columnConfigMap[idColName].id : idColName.toLowerCase().replace(/\s+/g, '');
        } else {
            // 默认使用第一个列作为主键
            const firstColName = parsed.columns[0];
            uidColumn = columnConfigMap[firstColName] ? columnConfigMap[firstColName].id : firstColName.toLowerCase().replace(/\s+/g, '');
        }
    } else {
        // 使用场景默认配置
        columnsCode = getColumnsByScenario(parsed.scenario);
        uidColumn = getUidColumnByScenario(parsed.scenario);
    }
    
    return `ood.Class("${parsed.className}", "ood.Module", {
    Instance: {
        // 初始化方法
        initialize: function() {
            this.iniComponents();
        },

        // 组件依赖
        Dependencies: [],

        // 必需的组件
        Required: [],

        // 组件属性
        properties: {
            "autoDestroy": true,
            "bindClass": [],
            "bindClassName": "${parsed.service}",
            "bindService": "${parsed.service}",
            "currComponentAlias": "${parsed.alias}",
            "dock": "fill",
            "dsmProperties": {
                "domainId": "TEST_VIEW",
                "moduleViewType": "GRIDCONFIG",
                "projectName": "test",
                "realPath": "demo.${parsed.alias.toLowerCase()}",
                "sourceClassName": "${parsed.service}",
                "sourceMethodName": "${parsed.alias}"
            },
            "dynLoad": false,
            "panelType": "block",
            "src": "/demo/${parsed.alias}",
            "viewClass": "net.ooder.esd.custom.component.grid.FullGridComponent"
        },

        // 事件定义
        events: {},

        // 视图菜单栏
        ViewMenuBar: {},

        // 函数定义
        functions: {},

        // 初始化组件
        iniComponents: function() {
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);
            
            // 创建APICaller组件
            append(
                ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setDesc("${parsed.alias}")
                .setCurrClassName("demo.${parsed.alias}")
                .setAutoRun(true)
                .setQueryURL("/demo/${parsed.alias}")
                .setQueryMethod("POST")
                .setRequestType("JSON")
                .setRequestDataSource([
                    {
                        "name": "testView",
                        "path": "",
                        "type": "FORM"
                    },
                    {
                        "name": "PAGECTX",
                        "path": "",
                        "type": "FORM"
                    }
                ])
                .setResponseDataTarget([
                    {
                        "name": "${parsed.alias}",
                        "path": "data",
                        "type": "TREEGRID"
                    },
                    {
                        "name": "PAGECTX",
                        "path": "data",
                        "type": "FORM"
                    }
                ])
                .setResponseCallback([])
            );
            
            append(
                ood.create("ood.APICaller")
                .setHost(host, "DELETE")
                .setName("DELETE")
                .setDesc("删除")
                .setCurrClassName("demo.${parsed.alias}")
                .setQueryURL("/demo/${parsed.alias}/delete")
                .setQueryMethod("POST")
                .setRequestType("JSON")
                .setRequestDataSource([
                    {
                        "name": "testView",
                        "path": "",
                        "type": "FORM"
                    }
                ])
                .setResponseDataTarget([])
                .setResponseCallback([])
            );
            
            // 创建主区块
            append(
                ood.create("ood.UI.Block")
                .setHost(host, "${parsed.alias}Main")
                .setName("${parsed.alias}Main")
                .setDock("fill")
                .setTabindex(0)
                .setBorderType("none")
            );
            
            // 创建TreeGrid组件
            host.${parsed.alias}Main.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host, "${parsed.alias}")
                .setName("${parsed.alias}")
                .setDesc("${parsed.alias}")
                .setTabindex(0)
                .setSelMode("multibycheckbox")
                .setEditMode("inline")
                .setRowNumbered(true)
                .setRowHeight("3em")
                ${columnsCode}
                .setUidColumn("${uidColumn}")
                .setTagCmds([
                    {
                        "buttonType": "text",
                        "caption": "",
                        "id": "editbutton",
                        "imageClass": "fa-solid fa-edit",
                        "index": 0,
                        "pos": "row",
                        "tagCmdsAlign": "right",
                        "tips": "编辑"
                    }
                ])
                .setValue("")
            );
            
            // 创建菜单栏
            host.${parsed.alias}Main.append(
                ood.create("ood.UI.MenuBar")
                .setHost(host, "${parsed.alias}MenuBar")
                .setName("${parsed.alias}MenuBar")
                .setItems([
                    {
                        "bindClass": [],
                        "caption": "删除",
                        "expression": "true",
                        "id": "DELETE_button",
                        "imageClass": "ri-times-line",
                        "tabindex": 0,
                        "tagVar": {},
                        "tips": "删除",
                        "title": "删除",
                        "type": "button"
                    },
                    {
                        "bindClass": [],
                        "caption": "添加",
                        "expression": "true",
                        "id": "ADD_button",
                        "imageClass": "ri-plus-line",
                        "tabindex": 1,
                        "tagVar": {},
                        "tips": "添加",
                        "title": "添加",
                        "type": "button"
                    },
                    {
                        "bindClass": [],
                        "caption": "刷新",
                        "expression": "true",
                        "id": "RELOAD_button",
                        "imageClass": "ri-sync-alt-line",
                        "tabindex": 2,
                        "tagVar": {},
                        "tips": "刷新",
                        "title": "刷新",
                        "type": "button"
                    }
                ])
                .setAutoIconColor(true)
            );
            
            // 创建上下文区块
            append(
                ood.create("ood.UI.Block")
                .setHost(host, "PAGECTX")
                .setName("PAGECTX")
                .setTabindex(3)
                .setVisibility("hidden")
                .setBorderType("none")
            );
            
            // 添加隐藏字段
            host.PAGECTX.append(
                ood.create("ood.UI.HiddenInput")
                .setHost(host, "testView")
                .setName("testView")
                .setValue("")
            );
            
            host.PAGECTX.append(
                ood.create("ood.UI.HiddenInput")
                .setHost(host, "projectName")
                .setName("projectName")
                .setValue("test")
            );
            
            return children;
        },

        customAppend: function(parent, subId, left, top) { return false; }
    },
    Static: {
        "designViewConf": {
            "touchDevice": false
        },
        "viewStyles": {}
    }
});
`;
}

// 执行主函数
if (require.main === module) {
    main();
}