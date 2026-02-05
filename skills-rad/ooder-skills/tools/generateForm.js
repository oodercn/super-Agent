#!/usr/bin/env node

/**
 * Form组件生成工具
 * 用于将自然语言描述转换为Ooder Form组件代码
 * 参考了JDSEasy RAD Studio生成的Form.js结构
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
        console.error('用法: node generateForm.js <自然语言描述> <输出文件路径>');
        console.error('示例: node generateForm.js "类名：TestForm，标题：测试表单" TestForm.js');
        process.exit(1);
    }
    
    const description = args[0];
    const outputPath = path.resolve(args[1]);
    
    // 解析自然语言描述
    const parsed = parseDescription(description);
    
    // 生成代码
    const code = generateFormCode(parsed);
    
    // 保存到文件
    fs.writeFileSync(outputPath, code, 'utf8');
    console.log(`文件已生成: ${outputPath}`);
}

/**
 * 解析自然语言描述
 */
function parseDescription(description) {
    const result = {
        className: 'ooder.DefaultForm',
        caption: '表单示例',
        title: '表单示例',
        alias: 'TestForm'
    };
    
    // 分割描述
    const segments = description.split(/[,，]/);
    
    segments.forEach(segment => {
        segment = segment.trim();
        if (!segment) return;
        
        const match = segment.match(/^(.+?)[:：]\s*(.+)$/);
        if (!match) return;
        
        const type = match[1];
        const content = match[2];
        
        if (type === '类名') {
            result.className = `ooder.${content}`;
        } else if (type === '标题') {
            result.caption = content;
            result.title = content;
        } else if (type === '别名') {
            result.alias = content;
        }
    });
    
    return result;
}

/**
 * 生成Form组件代码
 */
function generateFormCode(parsed) {
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
            "caption": "${parsed.caption}",
            "currComponentAlias": "${parsed.alias}",
            "dock": "fill",
            "title": "${parsed.title}"
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
                .setHost(host, "SAVE")
                .setName("SAVE")
                .setDesc("保存")
                .setCurrClassName("${parsed.className.replace('ooder.', '')}")
                .setQueryURL("/${parsed.className.replace('ooder.', '').toLowerCase()}/save")
                .setQueryMethod("POST")
                .setRequestType("JSON")
                .setRequestDataSource([
                    {
                        "name": "${parsed.alias}",
                        "path": "",
                        "type": "FORM"
                    }
                ])
                .setResponseDataTarget([])
                .setResponseCallback([])
            );
            
            append(
                ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setDesc("重新加载")
                .setCurrClassName("${parsed.className.replace('ooder.', '')}")
                .setAutoRun(true)
                .setQueryURL("/${parsed.className.replace('ooder.', '').toLowerCase()}/load")
                .setQueryMethod("POST")
                .setRequestType("JSON")
                .setRequestDataSource([
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
                        "type": "FORM"
                    }
                ])
                .setResponseCallback([])
            );
            
            // 创建主对话框
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host, "${parsed.alias}")
                .setName("${parsed.alias}")
                .setLeft("18.333333333333332em")
                .setTop("8.333333333333334em")
                .setWidth("40em")
                .setHeight("35em")
                .setTabindex(2)
                .setVisibility("visible")
                .setCaption("${parsed.caption}")
            );
            
            // 创建表单布局
            host.${parsed.alias}.append(
                ood.create("ood.UI.FormLayout")
                .setHost(host, "${parsed.alias}Form")
                .setName("${parsed.alias}Form")
                .setDock("fill")
                .setWidth("39.333333333333336em")
                .setHeight("28.666666666666668em")
                .setVisibility("visible")
                .setFloatHandler(false)
                .setDefaultRowHeight(35)
                .setDefaultColWidth(150)
                .setLayoutData({
                    "rows": 3,
                    "cols": 2,
                    "rowSetting": {
                        "1": { "manualHeight": "35" },
                        "2": { "manualHeight": "35" },
                        "3": { "manualHeight": "35" }
                    },
                    "colSetting": {
                        "A": { "manualWidth": 150 },
                        "B": { "manualWidth": 297 }
                    },
                    "cells": {
                        "A1": { "value": "姓名", "style": { "textAlign": "center" } },
                        "A2": { "value": "性别", "style": { "textAlign": "center" } },
                        "A3": { "value": "年龄", "style": { "textAlign": "center" } }
                    }
                })
            );
            
            // 创建表单字段
            host.${parsed.alias}Form.append(
                ood.create("ood.UI.Input")
                .setHost(host, "name")
                .setName("name")
                .setDesc("姓名")
                .setRequired(true)
                .setLeft("0em")
                .setTop("0.4166666666666667em")
                .setWidth("24.666666666666668em")
                .setHeight("3em")
                .setBottom("0.4166666666666667em")
                .setTabindex(0)
                .setVisibility("visible")
                .setLabelSize("0em")
                .setLabelPos("none")
                .setLabelCaption("姓名"),
                "B1"
            );
            
            host.${parsed.alias}Form.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host, "sex")
                .setName("sex")
                .setDesc("性别")
                .setLeft("0em")
                .setTop("0.4166666666666667em")
                .setWidth("24.666666666666668em")
                .setHeight("3em")
                .setBottom("0.4166666666666667em")
                .setVisibility("visible")
                .setLabelSize("0em")
                .setLabelPos("none")
                .setLabelCaption("性别")
                .setType("input")
                .setCaption("性别"),
                "B2"
            );
            
            host.${parsed.alias}Form.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host, "age")
                .setName("age")
                .setDesc("年龄")
                .setLeft("0em")
                .setTop("0.4166666666666667em")
                .setWidth("24.666666666666668em")
                .setHeight("3em")
                .setBottom("0.4166666666666667em")
                .setTabindex(3)
                .setVisibility("visible")
                .setLabelSize("0em")
                .setLabelPos("none")
                .setLabelCaption("年龄")
                .setType("input")
                .setCaption("年龄"),
                "B3"
            );
            
            // 创建底部按钮栏
            host.${parsed.alias}.append(
                ood.create("ood.UI.Block")
                .setHost(host, "${parsed.alias}BottomBlock")
                .setName("${parsed.alias}BottomBlock")
                .setDock("bottom")
                .setHeight("3em")
                .setComboType("STATUSBUTTONS")
                .setBorderType("none")
                .setOverflow("hidden")
            );
            
            host.${parsed.alias}BottomBlock.append(
                ood.create("ood.UI.StatusButtons")
                .setHost(host, "${parsed.alias}Bottom")
                .setName("${parsed.alias}Bottom")
                .setItems([
                    {
                        "caption": "保存",
                        "id": "SAVE_button",
                        "imageClass": "ri-save-line",
                        "index": 0,
                        "itemType": "button",
                        "tabindex": 0,
                        "tagVar": {
                            "name": "SAVE",
                            "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                        },
                        "tips": "保存",
                        "title": "保存"
                    },
                    {
                        "caption": "取消",
                        "id": "CANCEL_button",
                        "imageClass": "ri-close-line",
                        "index": 1,
                        "itemType": "button",
                        "tabindex": 1,
                        "tagVar": {
                            "name": "CANCEL",
                            "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                        },
                        "tips": "取消",
                        "title": "取消"
                    }
                ])
                .setDock("fill")
                .setTabindex(0)
                .setPosition("static")
                .setBorderType("none")
            );
            
            // 创建上下文区块
            append(
                ood.create("ood.UI.Block")
                .setHost(host, "PAGECTX")
                .setName("PAGECTX")
                .setTabindex(4)
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