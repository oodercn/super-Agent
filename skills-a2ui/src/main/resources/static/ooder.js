
ood.Class('ooder.Index', 'ood.Module',{
 Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
    "autoDestroy":true,
    "bindClass":[],
    "currComponentAlias":"pageContainer",
    "dock":"fill",
   
    "panelType":"block"
},
        events:{},
        ViewMenuBar:{},
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);
            
            append(
                ood.create("ood.UI.Block")
                .setHost(host,"indexMain")
                .setName("indexMain")
                .setDock("fill")
                .setTabindex(3)
                .setBorderType("none")
            );
            
            host.indexMain.append(
                ood.create("ood.UI.Block")
                .setHost(host,"HomePageMain")
                .setName("HomePageMain")
                .setDock("fill")
                .setBorderType("none")
                .setCustomStyle({
                    "KEY":{
                        "text-align":"left"
                    }
                })
            );
            
            host.HomePageMain.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"pageContainer")
                .setName("pageContainer")
                .setItems([
                    {
                        "bindClass":[ ],
                        "cmd":false,
                        "flexSize":false,
                        "folded":false,
                        "hidden":false,
                        "id":"before",
                        "locked":true,
                        "min":10,
                        "overflow":"hidden",
                        "pos":"before",
                        "size":60,
                        "tabindex":0,
                        "transparent":true
                    },
                    {
                        "bindClass":[ ],
                        "bindClassName":"net.ooder.test.view.view.homepage.pagecontainer.FeaturesAPI",
                        "euClassName":"view.homepage.pagecontainer.TechStack",
                        "flexSize":false,
                        "id":"main",
                        "min":10,
                        "overflow":"auto",
                        "size":80,
                        "tabindex":1,
                        "transparent":true
                    },
                    {
                        "bindClass":[ ],
                        "bindClassName":"net.ooder.test.view.view.homepage.pagecontainer.FooterAPI",
                        "cmd":false,
                        "euClassName":"view.homepage.pagecontainer.Footer",
                        "flexSize":false,
                        "folded":false,
                        "hidden":false,
                        "id":"after",
                        "locked":true,
                        "min":10,
                        "overflow":"hidden",
                        "pos":"after",
                        "size":100,
                        "tabindex":2,
                        "transparent":true
                    }
                ])
                .setClassName("ood-layout")
                .setTabindex(0)
                .setVisibility("visible")
                .setType("vertical")
                .setOverflow("auto")
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"footer")
                .setName("footer")
                .setClassName("ood-footer")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setOverflow("hidden")
                .setCaption(""),
                "after"
            );
            
            host.footer.append(
                ood.create("ood.UI.Block")
                .setHost(host,"footerContent")
                .setName("footerContent")
                .setLeft("0em")
                .setTop("0em")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY":{
                        "scrollbar-width":"none",
                        "padding":"0 20px",
                        "margin":"0 auto",
                        "overflow":"hidden",
                        "display":"flex",
                        "gap":"20px",
                        "justify-content":"space-between",
                        "align-items":"center",
                        "-ms-overflow-style":"none",
                        "flex-wrap":"wrap"
                    }
                })
            );
            
            host.footerContent.append(
                ood.create("ood.UI.Label")
                .setHost(host,"footerCopyright")
                .setName("footerCopyright")
                .setTabindex(0)
                .setCaption("© 2025 OODER. 保留所有权利。")
                .setCustomStyle({
                    "CAPTION":{
                        "color":"#ffffff"
                    }
                })
            );
            
            host.footerContent.append(
                ood.create("ood.UI.Block")
                .setHost(host,"footerLinks")
                .setName("footerLinks")
                .setLeft("0em")
                .setTop("0em")
                .setPosition("relative")
                .setVisibility("visible")
                .setBorderType("none")
                .setCustomStyle({
                    "KEY":{
                        "padding":"0",
                        "margin":"0",
                        "display":"flex",
                        "gap":"20px",
                        "list-style":"none"
                    }
                })
            );
            
            host.footerLinks.append(
                ood.create("ood.UI.Label")
                .setHost(host,"footerPrivacy")
                .setName("footerPrivacy")
                .setLeft("0em")
                .setTop("0em")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setCaption("隐私政策")
                .setCustomStyle({
                    "CAPTION":{
                        "cursor":"pointer",
                        "color":"inherit",
                        "text-decoration":"inherit"
                    },
                    "KEY":{
                        "color":"#fff",
                        "display":"inline-block",
                        "text-decoration":"none",
                        "transition":"color 0.3s ease"
                    }
                })
            );
            
            host.footerLinks.append(
                ood.create("ood.UI.Label")
                .setHost(host,"footerTerms")
                .setName("footerTerms")
                .setLeft("8.83em")
                .setTop("0.08em")
                .setPosition("relative")
                .setVisibility("visible")
                .setCaption("服务条款")
                .setCustomStyle({
                    "CAPTION":{
                        "cursor":"pointer",
                        "color":"inherit",
                        "text-decoration":"inherit"
                    },
                    "KEY":{
                        "color":"#fff",
                        "display":"inline-block",
                        "text-decoration":"none",
                        "transition":"color 0.3s ease"
                    }
                })
            );
            
            host.footerLinks.append(
                ood.create("ood.UI.Label")
                .setHost(host,"footerAbout")
                .setName("footerAbout")
                .setLeft("73.83em")
                .setTop("0em")
                .setWidth("7.33em")
                .setHeight("0.75em")
                .setTabindex(2)
                .setPosition("relative")
                .setVisibility("visible")
                .setCaption("关于我们")
                .setCustomStyle({
                    "CAPTION":{
                        "cursor":"pointer",
                        "color":"inherit",
                        "text-decoration":"inherit"
                    },
                    "KEY":{
                        "color":"#fff",
                        "display":"inline-block",
                        "text-decoration":"none",
                        "transition":"color 0.3s ease"
                    }
                })
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"hero")
                .setName("hero")
                .setClassName("ood-hero")
                .setTop("0em")
                .setHeight("16.67em")
                .setTabindex(1)
                .setPosition("relative")
                .setVisibility("visible")
                .setOverflow("hidden")
                .setCaption(""),
                "main"
            );
            
            host.hero.append(
                ood.create("ood.UI.Button")
                .setHost(host,"heroBtn")
                .setName("heroBtn")
                .setLeft("91.25em")
                .setTop("64.58em")
                .setZIndex(1002)
                .setTabindex(0)
                .setVisibility("visible")
                .setCaption("立即开始")
                .setFontColor("#FFFFFF")
                .setCustomStyle({
                    "CAPTION":{
                        "color":"inherit",
                        "text-decoration":"none"
                    },
                    "KEY":{
                        "cursor":"pointer",
                        "border":"none",
                        "background-color":"#fff",
                        "border-radius":"50px",
                        "padding":"12px 30px",
                        "box-shadow":"0 4px 15px rgba(0,0,0,0.2)",
                        "transform":"translate(-50%, -50%)",
                        "font-weight":"600",
                        "display":"inline-block",
                        "text-decoration":"none",
                        "transition":"all 0.3s ease"
                    }
                })
            );
            
            host.hero.append(
                ood.create("ood.UI.SVGPaper")
                .setHost(host,"heroSvgPaper")
                .setName("heroSvgPaper")
                .setDock("width")
                .setLeft("-0.42em")
                .setTop("-0.42em")
                .setWidth("105.33em")
                .setHeight("16.17em")
                .setTabindex(1)
                .setPosition("relative")
                .setVisibility("visible")
            );
            
            host.heroSvgPaper.append(
                ood.create("ood.svg.text")
                .setHost(host,"heroTitleSvg")
                .setName("heroTitleSvg")
                .setShowEffects("Blur")
                .setActiveAnim("blinkAlert")
                .setTabindex(0)
                .setAttr({
                    "x":"50%",
                    "y":"40%",
                    "text":"Ooder企业级AI解决方案",
                    "fill":"#ffffff",
                    "font-family":"Arial, sans-serif",
                    "font-size":"48px",
                    "font-weight":"700"
                })
                .setOffsetFlow("none")
            );
            
            host.heroSvgPaper.append(
                ood.create("ood.svg.text")
                .setHost(host,"heroDescSvg")
                .setName("heroDescSvg")
                .setAttr({
                    "x":"50%",
                    "y":"60%",
                    "text":"快速构建企业级应用，无需复杂编码，可视化拖拽即可完成",
                    "fill":"#ffffff",
                    "font-family":"Arial, sans-serif",
                    "font-size":"20px",
                    "transform":[
                        "translate(0, 50)"
                    ]
                })
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"features")
                .setName("features")
                .setClassName("ood-features")
                .setLeft("0em")
                .setTop("0em")
                .setHeight("24.25em")
                .setTabindex(2)
                .setPosition("relative")
                .setVisibility("visible")
                .setOverflow("hidden")
                .setCaption(""),
                "main"
            );
            
            host.features.append(
                ood.create("ood.UI.Gallery")
                .setHost(host,"featuresContent")
                .setName("featuresContent")
                .setItems([
                    {
                        "bindClass":[ ],
                        "caption":"可视化设计",
                        "comment":"拖拽式界面设计，所见即所得，快速构建应用界面",
                        "id":"feature1",
                        "imageClass":"ri-paint-brush-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":0,
                        "title":"可视化设计"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"快速开发",
                        "comment":"低代码开发，大幅缩短开发周期，提高开发效率",
                        "id":"feature2",
                        "imageClass":"ri-rocket-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":1,
                        "title":"快速开发"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"丰富组件库",
                        "comment":"内置丰富的UI组件，满足各种业务需求",
                        "id":"feature3",
                        "imageClass":"ri-apps-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":2,
                        "title":"丰富组件库"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"多端适配",
                        "comment":"支持桌面、平板、移动设备，一次开发多端运行",
                        "id":"feature4",
                        "imageClass":"ri-device-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":3,
                        "title":"多端适配"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"API 集成",
                        "comment":"轻松集成各种API，实现数据交互和业务逻辑",
                        "id":"feature5",
                        "imageClass":"ri-eye-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":4,
                        "title":"API 集成"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"一键部署",
                        "comment":"开发完成后，一键部署到生产环境",
                        "id":"feature6",
                        "imageClass":"ri-upload-cloud-line",
                        "itemStyle":"font-size:48px; color:#667eea;",
                        "tabindex":5,
                        "title":"一键部署"
                    }
                ])
                .setLeft("15em")
                .setWidth("70em")
                .setHeight("22em")
                .setRight("15em")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setBorderType("none")
                .setIconFontSize("5em")
                .setItemMargin("0")
                .setColumns(3)
                .setCustomStyle({
                    "CAPTION":{
                        "color":"#FFFFFF"
                    },
                    "KEY":{
                        "color":"#FFFFFF"
                    }
                })
            );
            
            host.features.append(
                ood.create("ood.UI.Block")
                .setHost(host,"featuresTitle")
                .setName("featuresTitle")
                .setLeft("0em")
                .setTop("-2.25em")
                .setHeight("2.58em")
                .setPosition("relative")
                .setVisibility("visible")
                .setBorderType("none")
                .setOverflow("hidden")
                .setCustomStyle({
                    "KEY":{
                        "scrollbar-width":"none",
                        "padding":"0 20px",
                        "margin":"0 auto",
                        "overflow":"hidden",
                        "margin-bottom":"60px",
                        "-ms-overflow-style":"none",
                        "text-align":"center"
                    }
                })
            );
            
            host.featuresTitle.append(
                ood.create("ood.UI.Label")
                .setHost(host,"featuresTitleText")
                .setName("featuresTitleText")
                .setTabindex(0)
                .setCaption("核心功能")
                .setCustomStyle({
                    "CAPTION":{
                        "color":"#ffffff",
                        "font-size":"36px"
                    }
                })
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.MenuBar")
                .setHost(host,"navbar")
                .setName("navbar")
                .setItems([
                    {
                        "bindClass":[ ],
                        "caption":"OODER",
                        "id":"logo",
                        "itemStyle":"font-size:24px; font-weight:700; color:#ffffff;",
                        "tabindex":0,
                        "title":"OODER"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"功能",
                        "id":"features",
                        "tabindex":1,
                        "title":"功能"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"产品",
                        "id":"showcase",
                        "tabindex":2,
                        "title":"产品"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"技术",
                        "id":"tech",
                        "tabindex":3,
                        "title":"技术"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"联系我们",
                        "id":"contact",
                        "tabindex":4,
                        "title":"联系我们"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"进入设计器",
                        "id":"designer",
                        "itemStyle":"background-color:#667eea; color:#fff; padding:12px 30px; border-radius:50px; font-weight:600;",
                        "tabindex":5,
                        "title":"进入设计器"
                    }
                ])
                .setClassName("ood-navbar")
                .setHeight("4.08em")
                .setZIndex(1000)
                .setTabindex(3)
                .setPosition("relative")
                .setVisibility("visible")
                .setAutoIconColor(true)
                .setHandler(false)
                .setValue("designer"),
                "before"
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"productShowcase")
                .setName("productShowcase")
                .setClassName("ood-product-showcase")
                .setTop("-1.08em")
                .setHeight("15.17em")
                .setTabindex(4)
                .setPosition("relative")
                .setVisibility("visible")
                .setOverflow("hidden")
                .setCaption(""),
                "main"
            );
            
            host.productShowcase.append(
                ood.create("ood.UI.TitleBlock")
                .setHost(host,"showcaseContent")
                .setName("showcaseContent")
                .setItems([
                    {
                        "bindClass":[ ],
                        "caption":"showcase1",
                        "flagClass":"ri-layout-2-line",
                        "id":"showcase1",
                        "msgnum":"强大的设计器",
                        "tabindex":0,
                        "title":"OODER 设计器提供了直观的可视化界面，<br>让您可以轻松创建复杂的应用程序。支持多种视图模式，<br>包括设计视图、代码视图和预览视图，满足不同开发人员的需求。"
                    },
                    {
                        "bindClass":[ ],
                        "caption":"showcase2",
                        "flagClass":"ri-puzzle-line",
                        "id":"showcase2",
                        "msgnum":"丰富的组件库",
                        "tabindex":1,
                        "title":"设计器内置了丰富的组件库,<br>包括基础组件、布局组件、数据组件、<br>交互组件等，您可以根据需要拖拽使用。<br>同时，设计器还支持自定义组件，让您可以扩展设计器的功能。"
                    }
                ])
                .setLeft("15em")
                .setWidth("70em")
                .setHeight("11.58em")
                .setRight("15em")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setAutoItemColor(false)
                .setItemMargin("20")
                .setItemPadding("20")
                .setColumns(2)
                .setRows(1)
                .setCustomStyle({
                    "ITEM":{
                        "border-radius":"1px 0px 0px 0px",
                        "padding":"0px 0px 3px 0px",
                        "border-right":"dashed 1px #000000",
                        "margin":"18px 0px 0px -1px",
                        "overflow":"hidden"
                    },
                    "ITEMS":{
                        "overflow":"hidden"
                    },
                    "MSGNUM":{
                        "text-align":"center"
                    },
                    "TITLE":{
                        "overflow":"auto",
                        "text-decoration":"none",
                        "line-height":"1.5",
                        "text-align":"center"
                    }
                })
            );
            
            host.pageContainer.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"techStack")
                .setName("techStack")
                .setClassName("ood-tech-stack")
                .setTop("1.42em")
                .setHeight("15.33em")
                .setTabindex(5)
                .setPosition("relative")
                .setVisibility("visible")
                .setOverflow("hidden")
                .setCaption(""),
                "main"
            );
            
            host.techStack.append(
                ood.create("ood.UI.Block")
                .setHost(host,"techTitle")
                .setName("techTitle")
                .setDock("width")
                .setLeft("22.92em")
                .setTop("0.17em")
                .setWidth("39.67em")
                .setHeight("2.75em")
                .setTabindex(0)
                .setPosition("relative")
                .setVisibility("visible")
                .setBorderType("none")
                .setCustomStyle({
                    "KEY":{
                        "scrollbar-width":"none",
                        "padding":"0 20px",
                        "margin":"0 auto",
                        "overflow":"hidden",
                        "margin-bottom":"60px",
                        "-ms-overflow-style":"none",
                        "text-align":"center"
                    }
                })
            );
            
            host.techTitle.append(
                ood.create("ood.UI.Label")
                .setHost(host,"techTitleText")
                .setName("techTitleText")
                .setTabindex(0)
                .setCaption("技术栈")
                .setFontSize("4em")
                .setCustomStyle({
                    "CAPTION":{
                        "color":"#ffffff",
                        "font-size":"36px"
                    }
                })
            );
            
            host.techStack.append(
                ood.create("ood.UI.Gallery")
                .setHost(host,"techContent")
                .setName("techContent")
                .setItems([
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"JavaScript",
                        "id":"tech1",
                        "imageClass":"ri-javascript-line",
                        "itemStyle":"font-size:48px; color:#f7df1e;",
                        "tabindex":0
                    },
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"HTML5",
                        "id":"tech2",
                        "imageClass":"ri-html5-line",
                        "itemStyle":"font-size:48px; color:#e34f26;",
                        "tabindex":1
                    },
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"CSS3",
                        "id":"tech3",
                        "imageClass":"ri-css3-line",
                        "itemStyle":"font-size:48px; color:#1572b6;",
                        "tabindex":2
                    },
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"SVG",
                        "id":"tech4",
                        "imageClass":"ri-file-image-line",
                        "itemStyle":"font-size:48px; color:#ffb13b;",
                        "tabindex":3
                    },
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"响应式设计",
                        "id":"tech5",
                        "imageClass":"ri-mouse-line",
                        "itemStyle":"font-size:48px; color:#4caf50;",
                        "tabindex":4
                    },
                    {
                        "bindClass":[ ],
                        "caption":"",
                        "comment":"开源框架",
                        "id":"tech6",
                        "imageClass":"ri-git-branch-line",
                        "itemStyle":"font-size:48px; color:#f05032;",
                        "tabindex":5
                    }
                ])
                .setClassName("ood-css-nrh ood-tech-gallery")
                .setLeft("12.92em")
                .setTop("-5.08em")
                .setWidth("70em")
                .setHeight("9.08em")
                .setRight("15em")
                .setPosition("relative")
                .setVisibility("visible")
                .setAutoIconColor(false)
                .setIconFontSize("2em")
                .setColumns(6)
            );
            
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host,"globalCssBox")
                .setName("globalCssBox")
                .setClassName("ood-global-css")
                .setNormalStatus({
                    "ood-tech-stack":{
                        "background-color":"#1e1e1e;",
                        "scrollbar-width":"none;",
                        "padding":"80px 0;",
                        "overflow":"hidden;",
                        "color":"#ffffff;",
                        "-ms-overflow-style":"none;"
                    },
                    "ood-features":{
                        "background-color":"#1e1e1e;",
                        "scrollbar-width":"none;",
                        "padding":"80px 0;",
                        "overflow":"hidden;",
                        "color":"#ffffff;",
                        "-ms-overflow-style":"none;"
                    },
                    "ood-css-nrh":{
                        "border-radius":"6px",
                        "cursor":"pointer",
                        "border-top":"solid #3899C6  1px",
                        "box-shadow":"inset 0px 1px 0px #87C1DD",
                        "border-right":"solid #3899C6  1px",
                        "text-shadow":"0 -1px 0 #297192",
                        "$gradient":{
                            "orient":"T",
                            "stops":[
                                {
                                    "pos":"0%",
                                    "clr":"#4BA3CC"
                                },
                                {
                                    "pos":"50%",
                                    "clr":"#3289B2"
                                },
                                {
                                    "pos":"100%",
                                    "clr":"#3899C6 "
                                }
                            ],
                            "type":"linear"
                        },
                        "color":"#eeeeee",
                        "border-left":"solid #3899C6  1px",
                        "border-bottom":"solid #3899C6  1px"
                    },
                    "ood-footer":{
                        "background-color":"#0d0d0d;",
                        "scrollbar-width":"none;",
                        "padding":"40px 0;",
                        "overflow":"hidden;",
                        "color":"#fff;",
                        "-ms-overflow-style":"none;",
                        "text-align":"center;"
                    },
                    "ood-hero":{
                        "scrollbar-width":"none;",
                        "overflow":"hidden;",
                        "color":"#fff;",
                        "background":"linear-gradient(135deg, #1e1e1e 0%, #2d2d2d 100%);",
                        "-ms-overflow-style":"none;",
                        "text-align":"center;"
                    },
                    "ood-gallery":{
                        "padding":"0 20px;",
                        "ITEM":"width:350px; background-color:#2d2d2d; border-radius:12px; padding:40px 30px; text-align:center; transition:all 0.3s ease; box-shadow:0 4px 15px rgba(0,0,0,0.1);",
                        "margin":"0 auto;",
                        "ITEM:hover":"transform:translateY(-10px); box-shadow:0 10px 30px rgba(0,0,0,0.2);",
                        "ITEMS":"display:flex; flex-wrap:wrap; justify-content:center; gap:30px;",
                        "ICON":"font-size:60px; margin-bottom:20px; color:#667eea; display:block;",
                        "CAPTION":"font-size:24px; font-weight:700; color:#ffffff; margin-bottom:15px; line-height:1.3;",
                        "COMMENT":"font-size:16px; color:#a0a0a0; line-height:1.6;"
                    },
                    "ood-navbar":{
                        "background-color":"#1e1e1e;",
                        "box-shadow":"0 2px 10px rgba(0,0,0,0.3);",
                        "BORDER":"background-color:#1e1e1e; height:60px; line-height:60px;",
                        "color":"#ffffff;",
                        "height":"60px;"
                    },
                    "ood-product-showcase":{
                        "background-color":"#2d2d2d;",
                        "scrollbar-width":"none;",
                        "padding":"80px 0;",
                        "overflow":"hidden;",
                        "color":"#ffffff;",
                        "-ms-overflow-style":"none;"
                    },
                    "ood-layout":{
                        "background-color":"#121212;",
                        "MOVE":"display:none;"
                    },
                    "ood-contact":{
                        "scrollbar-width":"none;",
                        "padding":"80px 0;",
                        "overflow":"hidden;",
                        "color":"#fff;",
                        "background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%);",
                        "-ms-overflow-style":"none;",
                        "text-align":"center;"
                    },
                    "ood-tech-gallery":{
                        "padding":"0 20px;",
                        "ITEM":"width:150px; background-color:#2d2d2d; border-radius:12px; padding:30px 20px; text-align:center; transition:all 0.3s ease; box-shadow:0 4px 15px rgba(0,0,0,0.1);",
                        "margin":"0 auto;",
                        "ITEM:hover":"transform:translateY(-8px); box-shadow:0 8px 25px rgba(0,0,0,0.2);",
                        "ITEMS":"display:flex; flex-wrap:wrap; justify-content:center; gap:20px;",
                        "ICON":"font-size:50px; margin-bottom:15px; display:block;",
                        "CAPTION":"font-size:16px; font-weight:600; color:#ffffff; line-height:1.3;"
                    }
                })
                .setActiveStatus({
                    "ood-css-nrh":{
                        "background-color":"#4BA3CC",
                        "box-shadow":"inset 0px 1px 2px #297192",
                        "background-image":"none"
                    }
                })
            );
            
            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend :  function(parent, subId, left, top){ return false},
    addInteractiveEffects:function () {
            var host = this;
            
            // SVG文本动画效果
            function animateSvgText() {
                // 获取SVG文本元素
                var titleSvg = host.heroTitleSvg;
                var descSvg = host.heroDescSvg;
                var btn = host.heroBtn;
                
                if (titleSvg && titleSvg._elset) {
                    // 标题动画：淡入+上移
                    titleSvg.elemsAnimate({
                        opacity: 1,
                        transform: 'translate(0, 0)'
                    }, 1000, 'ease-out');
                    
                    // 延迟启动描述动画
                    setTimeout(function() {
                        if (descSvg && descSvg._elset) {
                            descSvg.elemsAnimate({
                                opacity: 1,
                                transform: 'translate(0, 0)'
                            }, 1000, 'ease-out');
                        }
                    }, 300);
                    
                    // 延迟启动按钮动画
                    setTimeout(function() {
                        if (btn) {
                            btn.animate({
                                opacity: 1,
                                transform: 'translate(-50%, -50%) scale(1)'
                            }, 800, 'ease-out');
                        }
                    }, 600);
                }
            }
            
            // 页面加载完成后启动动画
            setTimeout(animateSvgText, 500);
            
            // 悬停效果 - 使用OOD事件系统
            host.featuresContent.on('itemhover', function(profile, item, e, src) {
                ood(src).css({
                    'transform': 'translateY(-5px)',
                    'box-shadow': '0 10px 25px rgba(0, 0, 0, 0.1)'
                });
            });
            
            host.featuresContent.on('itemleave', function(profile, item, e, src) {
                ood(src).css({
                    'transform': 'translateY(0)',
                    'box-shadow': 'none'
                });
            });
            
            host.techContent.on('itemhover', function(profile, item, e, src) {
                ood(src).css({
                    'transform': 'translateY(-5px)',
                    'box-shadow': '0 10px 25px rgba(0, 0, 0, 0.1)'
                });
            });
            
            host.techContent.on('itemleave', function(profile, item, e, src) {
                ood(src).css({
                    'transform': 'translateY(0)',
                    'box-shadow': 'none'
                });
            });
        },
afterIniComponents:function () {
            // 添加交互效果
            this.addInteractiveEffects();
        } ,
    ViewMenuBar:{},

 } ,
      Static:{
    "designViewConf":{
        "touchDevice":false
    },
    "viewStyles":{
        
    }
}

});