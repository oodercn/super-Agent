// 默认的代码是一个从 ood.Module 派生来的类
// 要确保键值对的值不能包含外部引用
ood.Class('svg.AddLock', 'ood.Module',{
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
                ood.create("ood.UI.SVGPaper")
                .setHost(host,"svgpaper1")
                .setLeft("0.75em")
                .setTop("-0.1875em")
                .setWidth("59.25em")
                .setHeight("52.25em")
                .setPanelBgClr("#FFFFFF")
                .setSandboxTheme("default")
                .setCustomStyle({
                    "KEY":{
                        "opacity":0
                    }
                })
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.connector")
                .setHost(host,"connector1")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,190,249.99249267578125L,391.75,253.25",
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
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"kaiji")
                .setClassName("start")
                .setSvgTag("FlowChart:Termination")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,99.30592999467848,229.99999998316764L,180.69632999467848,229.99999998316764L,180.69632999467848,229.99999998316764C,191.35872999467847,229.99999998316764,200.0022899946785,239.1241199831677,200.0022899946785,250.37923998316762C,200.0022899946785,261.63434998316757,191.3587399946785,270.7583999831677,180.69632999467848,270.7583999831677L,99.30592999467848,270.7583999831677L,99.30592999467848,270.7583999831677C,88.64352999467849,270.7583999831677,79.99999999467849,261.63434998316757,79.99999999467849,250.37923998316762C,79.99999999467849,239.1241199831677,88.64352999467849,230.00004998316763,99.30592999467848,230.00004998316763Z"
                    },
                    "TEXT":{
                        "text":"开启加网",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .onClick([
                    {
                        "desc":"动作 2",
                        "type":"control",
                        "target":"tishi",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#5198D3-#A1C8F6",
                                        "stroke":"#004A7F",
                                        "path":"M,130,70L,156.66660000000002,70L,156.66660000000002,70L,196.666675,70L,290,70L,290,120.51483157542538L,290,120.51483157542538L,290,141.87443746328915L,290,156.59684899834173L,196.666674,156.59684899834173L,130.25897452000004,199.04164589195057L,156.66667080000002,156.59684899834173L,130,156.59684899834173L,130,142.1640416096027L,130,120.51483157542538L,130,120.51483157542538Z"
                                    },
                                    "TEXT":{
                                        "text":"请将遥控器对准，\n发射器，按下开机键。",
                                        "font-size":"12px",
                                        "fill":"#fff",
                                        "font-weight":"bold"
                                    }
                                },
                                "visibility":"hidden"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    },
                    {
                        "desc":"动作 5",
                        "type":"other",
                        "target":"msg",
                        "args":[
                            "输入设备IEEE地址",
                            "输入门锁侧面序号",
                            "00137AAAAA00001"
                        ],
                        "method":"prompt",
                        "okFlag":"_prompt_ok",
                        "koFlag":"_prompt_cancel",
                        "onOK":3,
                        "onKO":4
                    },
                    {
                        "desc":"动作 3",
                        "type":"control",
                        "target":"minglingzhixing",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#B6E026:0-#ABDC28:50-#95B91A:100",
                                        "stroke":"#004A7F",
                                        "path":"M,395,222C,395,222,505,233.4374985088087,505,233.4374985088087C,505,233.4374985088087,505,271.56249558515054,505,271.56249558515054C,505,271.56249558515054,395,283,395,283C,395,283,395,222,395,222"
                                    },
                                    "TEXT":{
                                        "text":"命令到达网关\n",
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
                        "desc":"动作 4",
                        "type":"control",
                        "target":"lockzhixng",
                        "args":[
                            {
                                "attr":{
                                    "KEY":{
                                        "fill":"90-#B6E026:0-#ABDC28:50-#95B91A:100",
                                        "stroke":"#004A7F",
                                        "path":"M,385,449.28060000000005L,420.147,432L,469.85300000000007,432L,505,449.28060000000005L,505,491L,385,491L,385,473.71939999999995Z"
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
                        "method":"setProperties",
                        "timeout":2000
                    },
                    {
                        "desc":"动作 6",
                        "type":"control",
                        "target":"jihuotishi",
                        "args":[
                            {
                                "visibility":"visible"
                            },
                            { }
                        ],
                        "method":"setProperties"
                    }
                ])
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"lockzhixng")
                .setSvgTag("FlowChart:LoopLimit")
                .setAttr({
                    "KEY":{
                        "fill":"90-#D3D3D3:0-#707070:50-#BABABA:100",
                        "stroke":"#004A7F",
                        "path":"M,385,449.28060000000005L,420.147,432L,469.85300000000007,432L,505,449.28060000000005L,505,491L,385,491L,385,473.71939999999995Z"
                    },
                    "TEXT":{
                        "text":"等待门锁执行",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"minglingzhixing")
                .setSvgTag("FlowChart:ManualOperation")
                .setAttr({
                    "KEY":{
                        "fill":"90-#D3D3D3:0-#707070:50-#BABABA:100",
                        "stroke":"#004A7F",
                        "path":"M,395,222C,395,222,505,233.4374985088087,505,233.4374985088087C,505,233.4374985088087,505,271.56249558515054,505,271.56249558515054C,505,271.56249558515054,395,283,395,283C,395,283,395,222,395,222"
                    },
                    "TEXT":{
                        "text":"命令到达网关\n",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"tishi")
                .setSvgTag("Tips:Rect")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,125,72L,151.66660000000002,72L,151.66660000000002,72L,191.666675,72L,285,72L,285,122.51483157542538L,285,122.51483157542538L,285,143.87443746328915L,285,158.59684899834173L,191.666674,158.59684899834173L,125.25897452000004,201.04164589195057L,151.66667080000002,158.59684899834173L,125,158.59684899834173L,125,144.1640416096027L,125,122.51483157542538L,125,122.51483157542538Z"
                    },
                    "TEXT":{
                        "text":"点击开始键，\n开始加网。",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setVAlign("33.33%")
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"jihuotishi")
                .setVisibility("hidden")
                .setSvgTag("Tips:Ellipse")
                .setAttr({
                    "KEY":{
                        "fill":"90-#5198D3-#A1C8F6",
                        "stroke":"#004A7F",
                        "path":"M,475.86032429282534,402.3484390176392L,493.3808321594449,373.70176667089413L,493.3808321594449,373.70176667089413C,474.81926004055697,365.0603369662356,469.5227387856501,348.781730879683,481.1444063334374,336.0939721791377C,492.76585580662254,323.4062869310822,517.502574739964,318.46192235364583,538.2916444906731,324.6713047541202C,559.0807082667355,330.88065178858085,569.1191661519169,346.2114786351955,561.4823728226236,360.0894750832098C,553.8459827819779,373.9674660902989,531.0339424903921,381.8507110335758,508.78086922674544,378.3015765004025L,475.86032429282534,402.3484390176392Z"
                    },
                    "TEXT":{
                        "text":"请激活门锁！",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
                .setVAlign("33.33%")
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.connector")
                .setHost(host,"xiafazhong")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,441.75,280.25L,441.75,428.25",
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
            
            host.svgpaper1.append(
                ood.create("ood.svg.pathComb")
                .setHost(host,"pathcomb47")
                .setSvgTag("FlowChart:Display")
                .setAttr({
                    "KEY":{
                        "fill":"90-#D3D3D3:0-#707070:50-#BABABA:100",
                        "stroke":"#004A7F",
                        "path":"M,85,462.4231L,105,442L,185,442C,196.04558,442,205,451.14371,205,462.4231C,205,473.7023181,196.04557599999998,482.84615,185,482.84615L,105,482.84615L,85,462.4231Z"
                    },
                    "TEXT":{
                        "text":"等待手工检测",
                        "font-size":"12px",
                        "fill":"#fff",
                        "font-weight":"bold"
                    }
                })
            );
            
            host.svgpaper1.append(
                ood.create("ood.svg.connector")
                .setHost(host,"connector14")
                .setSvgTag("Connectors:Straight")
                .setAttr({
                    "KEY":{
                        "path":"M,385,461.5L,205,462.42308044433594",
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
                .setFromObj("lockzhixng")
                .setFromPoint("left")
                .setToObj("pathcomb47")
                .setToPoint("right")
            );
            
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host,"cssbox2")
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
        },
        events:{
            "onReady":null
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