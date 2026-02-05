// 默认的代码是一个从 ood.Module 派生来的类
// 要确保键值对的值不能包含外部引用
ood.Class('svg.IRLear', 'ood.Module',{
    Instance:{
        // 依赖的类
        Dependencies:[],
        // 需要的模块
        Required:[],

        // 初始化属性
        properties : {},

        // 初始化函数
        initialize : function(){
        },

        // 初始化内部控件（通过界面编辑器生成的代码，大部分是界面控件）
        // *** 如果您不是非常熟悉XUI框架，请慎重手工改变本函数的代码 ***
        iniComponents : function(){
            // [[Code created by CrossUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.Timer")
                .setHost(host,"xui_timer2")
                .setAutoStart(false)
                .setInterval(15000)
                .onTime([
                    {
                        "desc":"动作 2",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            undefined,
                            "学习失败",
                            200,
                            5000
                        ],
                        "method":"message"
                    },
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"kjzixingtishi",
                        "args":[
                            {
                                "display":"",
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties",
                        "return":false
                    }
                ])
            );
            
            append(
                ood.create("ood.Timer")
                .setHost(host,"loadStatus")
                .setAutoStart(false)
                .setInterval(2000)
                .onTime([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"api_4",
                        "args":[ ],
                        "method":"invoke",
                        "onOK":0,
                        "onKO":1,
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail"
                    }
                ])
            );
            
            append(
                ood.create("ood.APICaller")
                .setHost(host,"api_1")
                .setName("api_1")
                .setQueryURL("/RAD/sendIRLearnCommand")
                .setQueryMethod("POST")
                .setRequestDataSource([
                    {
                        "type":"form",
                        "name":"view",
                        "path":""
                    }
                ])
                .setResponseDataTarget([ ])
                .setResponseCallback([ ])
                .setQueryArgs({
                    "code":1
                })
                .setProxyType("AJAX")
                .onData([
                    {
                        "desc":"动作 1",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            undefined,
                            "命令发送失败！"
                        ],
                        "method":"message",
                        "conditions":[
                            {
                                "left":"{args[1].requestStatus}",
                                "symbol":"=",
                                "right":"{-1}"
                            }
                        ],
                        "return":false,
                        "okFlag":"_confirm_yes",
                        "koFlag":"_confirm_no"
                    },
                    {
                        "desc":"动作 3",
                        "type":"control",
                        "target":"kjzixingtishi",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 7",
                        "type":"control",
                        "target":"api_4",
                        "args":[
                            "{page.api_4.setQueryData()}",
                            undefined,
                            undefined,
                            "{args[1].data.commandId}",
                            "commandId"
                        ],
                        "method":"setQueryData",
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"loadStatus",
                        "args":[ ],
                        "method":"start"
                    },
                    {
                        "desc":"动作 5",
                        "type":"control",
                        "target":"kaijizhixing",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#B6E026:0-#ABDC28:50-#95B91A:100",
                                        "stroke":"#004A7F",
                                        "path":"M,300,227.28060000000005L,335.147,210L,384.853,210L,420,227.28060000000005L,420,269L,300,269L,300,251.71939999999995Z"
                                    },
                                    "TEXT":{
                                        "text":"等待执行",
                                        "font-size":"12px",
                                        "fill":"#fff",
                                        "font-weight":"bold"
                                    }
                                }
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 6",
                        "type":"control",
                        "target":"kaijizhixing",
                        "args":[
                            "blinkAlertLoop"
                        ],
                        "method":"animate",
                        "onOK":1
                    }
                ])
                .onError([
                    {
                        "desc":"动作 1",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "网络错误！"
                        ],
                        "method":"alert",
                        "onOK":2
                    }
                ])
            );
            
            append(
                ood.create("ood.APICaller")
                .setHost(host,"api_4")
                .setName("api_4")
                .setQueryURL("/RAD/getCommandStatus")
                .setRequestDataSource([ ])
                .setResponseDataTarget([ ])
                .setResponseCallback([ ])
                .onData([
                    {
                        "desc":"动作 1",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "",
                            "学习成功！",
                            200,
                            5000
                        ],
                        "method":"message",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"=",
                                "right":"COMMANDSENDSUCCESS"
                            }
                        ],
                        "okFlag":"_confirm_yes",
                        "koFlag":"_confirm_no"
                    },
                    {
                        "desc":"动作 6",
                        "type":"control",
                        "target":"input17",
                        "args":[
                            "{page.input17.setUIValue()}",
                            undefined,
                            undefined,
                            "{args[1].data.modeId}"
                        ],
                        "method":"setUIValue",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"=",
                                "right":"COMMANDSENDSUCCESS"
                            }
                        ],
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 4",
                        "type":"control",
                        "target":"wancheng",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#B6E026:0-#ABDC28:50-#95B91A:100",
                                        "stroke":"#004A7F",
                                        "path":"M,521.1010683288612,209.99999997490806L,567.9012168823051,209.99999997490806L,567.9012168823051,209.99999997490806C,574.0321844649222,209.99999997490806,579.0023024644726,223.60129362063708,579.0023024644726,240.37926148361112C,579.0023024644726,257.1572144396201,574.0321902150042,270.75840373659497,567.9012168823051,270.75840373659497L,521.1010683288612,270.75840373659497L,521.1010683288612,270.75840373659497C,514.970100746244,270.75840373659497,509.9999999969401,257.1572144396201,509.9999999969401,240.37926148361112C,509.9999999969401,223.60129362063708,514.970100746244,210.00007450973249,521.1010683288612,210.00007450973249Z"
                                    },
                                    "TEXT":{
                                        "text":"完成",
                                        "font-size":"12px",
                                        "fill":"#fff",
                                        "font-weight":"bold"
                                    }
                                }
                            },
                            { }
                        ],
                        "method":"setProperties",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"=",
                                "right":"COMMANDSENDSUCCESS"
                            }
                        ]
                    },
                    {
                        "desc":"动作 2",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "学习失败！",
                            ""
                        ],
                        "method":"pop",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"!=",
                                "right":"COMMANDSENDING"
                            },
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"!=",
                                "right":"COMMANDSENDSUCCESS"
                            }
                        ]
                    },
                    {
                        "desc":"动作 7",
                        "type":"control",
                        "target":"kaijizhixing",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#F62B2B:0-#D20202:50-#E40A0A:100",
                                        "stroke":"#004A7F",
                                        "path":"M,300,227.28060000000005L,335.147,210L,384.853,210L,420,227.28060000000005L,420,269L,300,269L,300,251.71939999999995Z"
                                    },
                                    "TEXT":{
                                        "text":"学习失败！",
                                        "font-size":"12px",
                                        "fill":"#fff",
                                        "font-weight":"bold"
                                    }
                                }
                            },
                            { }
                        ],
                        "method":"setProperties",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"!=",
                                "right":"COMMANDSENDING"
                            },
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"!=",
                                "right":"COMMANDSENDSUCCESS"
                            }
                        ]
                    },
                    {
                        "desc":"动作 3",
                        "type":"control",
                        "target":"loadStatus",
                        "args":[ ],
                        "method":"suspend",
                        "conditions":[
                            {
                                "left":"{args[1].data.resultCode}",
                                "symbol":"!=",
                                "right":"COMMANDSENDING"
                            }
                        ],
                        "return":false
                    }
                ])
            );
            
            append(
                ood.create("ood.APICaller")
                .setHost(host,"sendIRControlCommand")
                .setName("sendIRControlCommand")
                .setQueryURL("/RAD/sendIRControlCommand")
                .setQueryMethod("POST")
                .setRequestDataSource([
                    {
                        "type":"form",
                        "name":"view",
                        "path":""
                    }
                ])
                .setResponseDataTarget([ ])
                .setResponseCallback([ ])
                .setProxyType("AJAX")
                .onData([
                    {
                        "desc":"动作 1",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "",
                            "发送成功！"
                        ],
                        "method":"message",
                        "conditions":[
                            {
                                "left":"requestStatus",
                                "symbol":"!=",
                                "right":"{-1}"
                            }
                        ],
                        "return":false
                    },
                    {
                        "desc":"动作 2",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "发送失败，请检查网关及网线及电源。",
                            "发送失败。"
                        ],
                        "method":"message",
                        "conditions":[
                            {
                                "left":"{args[1].requestStatus}",
                                "symbol":"=",
                                "right":"{-1}"
                            }
                        ]
                    }
                ])
            );
            
            append(
                ood.create("ood.UI.SVGPaper")
                .setHost(host,"view")
                .setDock("fill")
                .setLeft("0em")
                .setTop("-0.8333333333333334em")
                .setWidth("60.5em")
                .setHeight("48.083333333333336em")
                .setPanelBgClr("#FFFFFF")
                .setSandboxTheme("default")
            );
            
            host.view.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"irSn")
                .setDirtyMark(false)
                .setLeft("-1.6666666666666667em")
                .setTop("36.666666666666664em")
                .setWidth("21.6875em")
                .setLabelSize("8em")
                .setLabelCaption("选择红外设备：")
                .setItems([
                    {
                        "id":"086BD7FFFE8ACB8001",
                        "caption":"086BD7FFFE8ACB8001"
                    },
                    {
                        "id":"086BD7FFFE8ACB6501",
                        "caption":" 086BD7FFFE8ACB6501 "
                    }
                ])
                .onChange([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"zheli",
                        "args":[
                            {
                                "visibility":"hidden"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"kjtishi",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 3",
                        "type":"control",
                        "target":"kjtishi",
                        "args":[
                            "blinkAlert"
                        ],
                        "method":"animate",
                        "onOK":1
                    }
                ])
            );
            
            host.view.append(
                ood.create("ood.UI.Input")
                .setHost(host,"input17")
                .setReadonly(true)
                .setDirtyMark(false)
                .setLeft("22.5625em")
                .setTop("3.8125em")
                .setWidth("18em")
                .setLabelSize("8em")
                .setLabelCaption("学习结果：")
            );
            
            host.view.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"gwSn")
                .setDirtyMark(false)
                .setTips("网关会自动选择")
                .setLeft("27.5em")
                .setTop("36.666666666666664em")
                .setWidth("19.25em")
                .setLabelSize("8em")
                .setLabelCaption("网关选择")
                .setType("listbox")
                .setCaption("设备所属网关")
                .onFocus([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            "translateXAlert"
                        ],
                        "method":"animate",
                        "onOK":1
                    },
                    {
                        "desc":"动作 3",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            {
                                "visibility":"hidden"
                            },
                            { }
                        ],
                        "method":"setProperties",
                        "timeout":5000
                    }
                ])
                .onCommand([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    }
                ])
                .onClick([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"gwts",
                        "args":[
                            "blinkAlert"
                        ],
                        "method":"animate",
                        "onOK":1
                    }
                ])
            );
            
            host.view.append(
                ood.create("ood.svg.connector")
                .setHost(host,"connector1")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,190,239.9887237548828L,294,239.1999969482422",
                        "fill":"none",
                        "stroke":"#004A7F",
                        "stroke-width":2,
                        "arrow-start":"oval-midium-midium",
                        "arrow-end":"classic-wide-long"
                    },
                    "BG":{
                        "fill":"none",
                        "stroke":"#fff",
                        "stroke-width":4
                    }
                })
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"kjtishi")
                .setVisibility("hidden")
                .setSvgTag("Tips:Rect")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,120,70L,146.66660000000002,70L,146.66660000000002,70L,186.666675,70L,280,70L,280,120.51483157542538L,280,120.51483157542538L,280,141.87443746328915L,280,156.59684899834173L,186.666674,156.59684899834173L,120.25897452000004,199.04164589195057L,146.66667080000002,156.59684899834173L,120,156.59684899834173L,120,142.1640416096027L,120,120.51483157542538L,120,120.51483157542538Z"
                    },
                    "TEXT":{
                        "text":"点击开始键，\n开始学习空调开机。",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setShadow(false)
                .setVAlign("33.33%")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"kaiji")
                .setClassName("start")
                .setSvgTag("1")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,99.30592999467848,219.99999998316764L,180.69632999467848,219.99999998316764L,180.69632999467848,219.99999998316764C,191.35872999467847,219.99999998316764,200.0022899946785,229.1241199831677,200.0022899946785,240.37923998316762C,200.0022899946785,251.63434998316757,191.3587399946785,260.7583999831677,180.69632999467848,260.7583999831677L,99.30592999467848,260.7583999831677L,99.30592999467848,260.7583999831677C,88.64352999467849,260.7583999831677,79.99999999467849,251.63434998316757,79.99999999467849,240.37923998316762C,79.99999999467849,229.1241199831677,88.64352999467849,220.00004998316763,99.30592999467848,220.00004998316763Z"
                    },
                    "TEXT":{
                        "text":"开机学习",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setOffsetFlow("-8x")
                .onClick([
                    {
                        "desc":"动作 8",
                        "type":"control",
                        "target":"zheli",
                        "args":[ ],
                        "method":"animate",
                        "conditions":[
                            {
                                "left":"{page.irSn.getValue()}",
                                "symbol":"empty",
                                "right":""
                            }
                        ],
                        "return":false,
                        "okFlag":"_prompt_ok",
                        "koFlag":"_prompt_cancel",
                        "onOK":1
                    },
                    {
                        "desc":"动作 5",
                        "type":"none",
                        "target":"none",
                        "args":[ ],
                        "method":"none",
                        "conditions":[
                            {
                                "left":"{page.irSn.getValue()}",
                                "symbol":"empty",
                                "right":""
                            }
                        ],
                        "return":false
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"kjtishi",
                        "args":[
                            {
                                "visibility":"hidden"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 10",
                        "type":"control",
                        "target":"api_4",
                        "args":[
                            "{page.api_4.setQueryData()}",
                            undefined,
                            undefined,
                            "1",
                            "code"
                        ],
                        "method":"setQueryData",
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail",
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 11",
                        "type":"control",
                        "target":"api_1",
                        "args":[ ],
                        "method":"invoke",
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail",
                        "onOK":0,
                        "onKO":1
                    }
                ])
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"kaijizhixing")
                .setSvgTag("FlowChart:LoopLimit")
                .setAttr({
                    "KEY":{
                        "fill":"90-#D3D3D3:0-#707070:50-#BABABA:100",
                        "stroke":"#004A7F",
                        "path":"M,300,227.28060000000005L,335.147,210L,384.853,210L,420,227.28060000000005L,420,269L,300,269L,300,251.71939999999995Z"
                    },
                    "TEXT":{
                        "text":"等待执行",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setAnimDraw("2s ease-in-out")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"kjzixingtishi")
                .setVisibility("hidden")
                .setSvgTag("Tips:Rect")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,370,90L,396.6666,90L,396.6666,90L,436.666675,90L,530,90L,530,132.6856020194723L,530,132.6856020194723L,530,150.73470973273407L,530,163.1753134119487L,436.666674,163.1753134119487L,370.25897452000004,199.0416451702302L,396.6666708,163.1753134119487L,370,163.1753134119487L,370,150.97942850041466L,370,132.6856020194723L,370,132.6856020194723Z"
                    },
                    "TEXT":{
                        "text":"请将遥控器对准发射机\n按开机键进入学习！",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setVAlign("33.33%")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"wancheng")
                .setSvgTag("FlowChart:Termination")
                .setAttr({
                    "KEY":{
                        "fill":"90-#D3D3D3:0-#707070:50-#BABABA:100",
                        "stroke":"#004A7F",
                        "path":"M,615.9274575440478,209.99999997490806L,683.07481283076,209.99999997490806L,683.07481283076,209.99999997490806C,691.8713288941907,209.99999997490806,699.0022951292996,223.60129362063708,699.0022951292996,240.37926148361112C,699.0022951292996,257.1572144396201,691.8713371442243,270.75840373659497,683.07481283076,270.75840373659497L,615.9274575440478,270.75840373659497L,615.9274575440478,270.75840373659497C,607.1309414806173,270.75840373659497,599.9999999956098,257.1572144396201,599.9999999956098,240.37926148361112C,599.9999999956098,223.60129362063708,607.1309414806173,210.00007450973249,615.9274575440478,210.00007450973249Z"
                    },
                    "TEXT":{
                        "text":"完成",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
            );
            
            host.view.append(
                ood.create("ood.svg.connector")
                .setHost(host,"connector60")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,420,240L,600,240.3791961669922",
                        "fill":"none",
                        "stroke":"#004A7F",
                        "stroke-width":2,
                        "arrow-start":"oval-midium-midium",
                        "arrow-end":"classic-wide-long"
                    },
                    "BG":{
                        "fill":"none",
                        "stroke":"#fff",
                        "stroke-width":4
                    }
                })
                .setToObj("wancheng")
                .setToPoint("left")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"pathcomb36")
                .setClassName("start")
                .setSvgTag("1")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,99.30592999467848,329.99999998316764L,180.69632999467848,329.99999998316764L,180.69632999467848,329.99999998316764C,191.35872999467847,329.99999998316764,200.0022899946785,339.1241199831677,200.0022899946785,350.3792399831676C,200.0022899946785,361.63434998316757,191.3587399946785,370.7583999831677,180.69632999467848,370.7583999831677L,99.30592999467848,370.7583999831677L,99.30592999467848,370.7583999831677C,88.64352999467849,370.7583999831677,79.99999999467849,361.63434998316757,79.99999999467849,350.3792399831676C,79.99999999467849,339.1241199831677,88.64352999467849,330.0000499831676,99.30592999467848,330.0000499831676Z"
                    },
                    "TEXT":{
                        "text":"关机学习",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setOffsetFlow("-8x")
                .onClick([
                    {
                        "desc":"动作 8",
                        "type":"control",
                        "target":"zheli",
                        "args":[
                            "zoomAlert"
                        ],
                        "method":"animate",
                        "conditions":[
                            {
                                "left":"{page.irSn.getValue()}",
                                "symbol":"empty",
                                "right":""
                            }
                        ],
                        "return":false,
                        "okFlag":"_prompt_ok",
                        "koFlag":"_prompt_cancel",
                        "onOK":1
                    },
                    {
                        "desc":"动作 9",
                        "type":"other",
                        "target":"msg",
                        "args":[ ],
                        "method":"none",
                        "conditions":[
                            {
                                "left":"{page.irSn.getValue()}",
                                "symbol":"empty",
                                "right":""
                            }
                        ],
                        "return":false
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"kjtishi",
                        "args":[
                            {
                                "visibility":"hidden"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 10",
                        "type":"control",
                        "target":"api_4",
                        "args":[
                            "{page.api_4.setQueryData()}",
                            undefined,
                            undefined,
                            "0",
                            "code"
                        ],
                        "method":"setQueryData",
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail",
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 11",
                        "type":"control",
                        "target":"api_1",
                        "args":[ ],
                        "method":"invoke",
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail",
                        "onOK":0,
                        "onKO":1
                    }
                ])
            );
            
            host.view.append(
                ood.create("ood.svg.connector")
                .setHost(host,"connector13")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,200.00228881835938,350.3791961669922L,357,271",
                        "fill":"none",
                        "stroke":"#004A7F",
                        "stroke-width":2,
                        "arrow-start":"oval-midium-midium",
                        "arrow-end":"classic-wide-long"
                    },
                    "BG":{
                        "fill":"none",
                        "stroke":"#fff",
                        "stroke-width":4
                    }
                })
                .setFromObj("pathcomb36")
                .setFromPoint("right")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"gwts")
                .setVisibility("hidden")
                .setSvgTag("Tips:Scream")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,570.674715068582,332.4160704551089L,552.651877193058,333.2010218616347L,547.8299939151553,352.32605605349477L,531.885669599213,347.7023335333045L,508.76260289443246,362.3776232881299L,499.9589833514293,353.8132291154315L,425.80876769478164,390.4383781875121L,467.00829825081576,349.59354209243355L,432.78668761496,344.2835085560665L,443.49521986365835,336.3829984439667L,419.9999999858219,321.88257047555174L,436.8849935339822,318.3758448889641L,431.57573438446616,299.28881789835714L,449.27631192794684,301.2892354610869L,463.8386600403744,283.67559655776853L,476.73502615853795,290.5480413327464L,506.545534544313,280.00000005593563L,510.5431769901734,289.56251715186573L,546.1372342464631,289.42899834746345L,539.9669275745072,298.64555756101925L,570.043653868583,308.96895510139103L,555.6644412379,314.91336019415763L,570.674715068582,332.4160704551089Z"
                    },
                    "TEXT":{
                        "text":"网关已自动为您选择了\n，直接开始吧：）",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setVAlign("33.33%")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"zheli")
                .setSvgTag("Tips:CloudBalloon")
                .setAttr({
                    "KEY":{
                        "stroke":"#004A7F",
                        "fill":"90-#5198D3-#A1C8F6",
                        "path":"M,220.36500879814145,459.49176831239845C,220.36500879814145,458.967286959513,221.23905408695856,458.54246555042903,222.31812152493205,458.54246555042903C,223.39718896290594,458.54246555042903,224.27123425172283,458.967286959513,224.27123425172283,459.49176831239845C,224.27123425172283,460.0162326917773,223.39718896290594,460.44105410086127,222.31812152493205,460.44105410086127C,221.23905408695856,460.44105410086127,220.36500879814145,460.0162326917773,220.36500879814145,459.49176831239845ZM,224.36888988806248,452.67104390860027C,224.36888988806248,451.359812237209,226.55400115699229,450.2977431554516,229.2516756112646,450.2977431554516C,231.94934615931146,450.2977431554516,234.13445742824126,451.359812237209,234.13445742824126,452.67104390860027C,234.13445742824126,453.9822161727184,231.94934615931146,455.0443022279826,229.2516756112646,455.0443022279826C,226.55400115699229,455.0443022279826,224.36888988806248,453.982233146225,224.36888988806248,452.67104390860027ZM,233.64617924654362,442.3298671393168C,233.64617924654362,438.7046599491424,241.25036974364878,435.7682687735064,250.63826778207368,435.7682687735064C,260.0261384769202,435.7682687735064,267.6303328802507,438.7046599491424,267.6303328802507,442.3298671393168C,267.6303328802507,445.9550488692312,260.0261384769202,448.89141458460733,250.63826778207368,448.89141458460733C,241.25036974364878,448.89141458460733,233.64617924654362,445.9550488692312,233.64617924654362,442.3298671393168ZM,287.28669770878554,360.0048487318319C,287.0474034351617,360.0117691325555,286.81615549293554,360.0302055126085,286.5758743702153,360.05208519751363C,282.7313727106244,360.4021770515059,279.3381863417894,362.63277967089425,277.6144086064168,365.95652513916366L,276.7004865492879,368.47181709496283C,276.89765678113895,367.60014734385084,277.2004087781282,366.75478052205176,277.6144086064168,365.95652513916366C,274.54027757301577,363.0446144750929,270.2254258929052,361.7005902044692,265.9619605910309,362.33120158310805C,261.69849159309257,362.96180958264483,257.9958559592423,365.49083082952814,255.97233851906606,369.1567340204914C,250.25990893079074,365.94017704409475,243.13925307387376,366.13498902774757,237.63052466259398,369.6645184235907C,232.12179625131427,373.1940444403318,229.18565407248428,379.44335552351413,230.07801329205034,385.7482089048872L,230.66190639963378,388.310737326368C,230.39805178051762,387.47122648818925,230.20100351877943,386.6171787535683,230.07801329205034,385.7482089048872L,229.98915991272116,385.98438785419387C,225.20063914658914,386.4614832536935,221.2734602306664,389.85823421885493,220.2534019987944,394.404123130745C,219.23334376692264,398.95000866353325,221.35442251328755,403.6143993005104,225.50842518278984,405.96501410354233L,231.99469969744558,407.405700963571C,229.7298625405561,407.5761394851047,227.46871405557056,407.07427864766385,225.50842518278984,405.96501410354233C,222.30429618525602,409.2463856337869,221.56697945970376,414.10437853593123,223.68059215672417,418.1163558287086C,225.79420115767988,422.1283263632822,230.3081080622988,424.4384358716487,234.9649306748425,423.9145128777993L,237.79553544130283,423.22959595227076C,236.88477721498538,423.57811652387215,235.93889529217148,423.8049353608718,234.9649306748425,423.9145128777993C,237.60857373830794,428.36387098525904,241.9920761117495,431.609528787355,247.13779929019046,432.9010654679197C,252.28352246863136,434.19260552758624,257.74915107723433,433.4292934376099,262.3062871467709,430.7872817808643C,266.0240027218718,436.147388953093,272.62241951532286,438.96272150530683,279.2391466099881,438.0143089705838C,285.85587000858925,437.06589643586085,291.3140177835911,432.51798682102503,293.2525442313513,426.3471418634308L,293.92528702220045,422.8989474507024C,293.824554493072,424.0704043655852,293.6059692662194,425.222107050668,293.2525442313513,426.3471418634308C,297.80361505981205,429.0757294903626,303.5283377570582,429.2294955230436,308.2306330453878,426.7486433739708C,312.9329283337172,424.267791224898,315.86703398126525,419.5371161247393,315.91007834298233,414.3847493800936L,314.22427396947387,406.68637802233513L,307.58327482231067,401.4776444786311C,312.718434649748,404.4185343207322,315.9558947527227,408.89926401037894,315.91007834298233,414.3847493800936C,322.0160500451294,414.4432517715627,327.202633211444,409.6598593024148,329.52998559537076,404.1700723359613C,331.8573379792973,398.68027861130395,330.9769429190029,392.42160741581483,327.21979773280873,387.72030346950885C,328.77819508306936,384.2199830306264,328.68573804130904,380.23839062227637,326.96594096689864,376.8088995386522C,325.24613280429605,373.37940845502794,322.0738232220134,370.8304437486373,318.2583319690099,369.82985112212964C,317.40447464846494,365.28448259193453,313.8848681054796,361.62822335783926,309.2334010897175,360.4299836811714C,304.58193407395606,359.2317440045034,299.6289680269124,360.7092799709244,296.5147199145081,364.2324431387581L,294.6234180768851,367.14923660509726C,295.1066895372614,366.09445670026685,295.7378589998781,365.1112968605431,296.5147199145081,364.2324431387581C,294.3194094181249,361.4634988976452,290.8761524698483,359.90107651199605,287.2866903166577,360.00486224823953Z"
                    },
                    "TEXT":{
                        "fill":"#fff",
                        "font-size":"12px",
                        "font-weight":"bold",
                        "text":"从这里开始！"
                    }
                })
                .setVAlign("33.33%")
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"pathcomb42")
                .setSvgTag("FlowChart:Termination")
                .setAttr({
                    "KEY":{
                        "stroke":"#004A7F",
                        "fill":"90-#5198D3-#A1C8F6",
                        "path":"M,87.69713358961633,519.9999999831676L,162.30513134519356,519.9999999831676L,162.30513134519356,519.9999999831676C,172.07901518492224,519.9999999831676,180.00229243973604,529.1241199831677,180.00229243973604,540.3792399831676C,180.00229243973604,551.6343499831677,172.07902435160509,560.7583999831677,162.30513134519356,560.7583999831677L,87.69713358961633,560.7583999831677L,87.69713358961633,560.7583999831677C,77.92324974988742,560.7583999831677,69.99999999512193,551.6343499831677,69.99999999512193,540.3792399831676C,69.99999999512193,529.1241199831677,77.92324974988742,520.0000499831676,87.69713358961633,520.0000499831676Z"
                    },
                    "TEXT":{
                        "fill":"#fff",
                        "font-size":"12px",
                        "font-weight":"bold",
                        "text":"测试开机"
                    }
                })
                .onClick([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"sendIRControlCommand",
                        "args":[
                            "{page.sendIRControlCommand.setQueryData()}",
                            undefined,
                            undefined,
                            "1",
                            "code"
                        ],
                        "method":"setQueryData",
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"sendIRControlCommand",
                        "args":[ ],
                        "method":"invoke",
                        "onOK":0,
                        "onKO":1,
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail"
                    }
                ])
            );
            
            host.view.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"pathcomb43")
                .setSvgTag("FlowChart:Termination")
                .setAttr({
                    "KEY":{
                        "stroke":"#004A7F",
                        "fill":"90-#5198D3-#A1C8F6",
                        "path":"M,349.30592999467854,519.9999999831676L,430.69632999467854,519.9999999831676L,430.69632999467854,519.9999999831676C,441.35872999467847,519.9999999831676,450.00228999467845,529.1241199831677,450.00228999467845,540.3792399831676C,450.00228999467845,551.6343499831676,441.35873999467844,560.7583999831677,430.69632999467854,560.7583999831677L,349.30592999467854,560.7583999831677L,349.30592999467854,560.7583999831677C,338.6435299946785,560.7583999831677,329.99999999467855,551.6343499831676,329.99999999467855,540.3792399831676C,329.99999999467855,529.1241199831677,338.6435299946785,520.0000499831676,349.30592999467854,520.0000499831676Z"
                    },
                    "TEXT":{
                        "fill":"#fff",
                        "font-size":"12px",
                        "font-weight":"bold",
                        "text":"测试关机"
                    }
                })
                .onClick([
                    {
                        "desc":"动作 1",
                        "type":"control",
                        "target":"sendIRControlCommand",
                        "args":[
                            "{page.sendIRControlCommand.setQueryData()}",
                            undefined,
                            undefined,
                            "0",
                            "code"
                        ],
                        "method":"setQueryData",
                        "redirection":"other:callback:call"
                    },
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"sendIRControlCommand",
                        "args":[ ],
                        "method":"invoke",
                        "onOK":0,
                        "onKO":1,
                        "okFlag":"_DI_succeed",
                        "koFlag":"_DI_fail"
                    }
                ])
            );
            
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host,"sendCommand")
                .setClassName("start")
                .setNormalStatus({
                    "color":"#222222",
                    "border-radius":"6px",
                    "text-shadow":"0 -1px 0 #87C1DD",
                    "background-color":"#4BA3CC",
                    "cursor":"pointer",
                    "box-shadow":"none",
                    "border-top":"none",
                    "border-right":"none",
                    "border-bottom":"none",
                    "border-left":"none",
                    "background-image":"none"
                })
                .setActiveStatus({
                    "background-color":"#2D7A9E",
                    "background-image":"none"
                })
            );
            
            return children;
            // ]]Code created by CrossUI RAD Studio
        },

        // 可以自定义哪些界面控件将会被加到父容器中
        customAppend : function(parent, subId, left, top){
            // "return false" 表示默认情况下所有的第一层内部界面控件会被加入到父容器
            return false;
        }
        /*,
        // 属性影响本模块的部分
        propSetAction : function(prop){
        },
        // 本模块中所有ood dom节点的定制CSS style
        customStyle:{}
    },
    // 制定义模块的默认属性和事件声明
    Static:{
        $DataModel:{
        },
        $EventHandlers:{
        }
    */
    }
});