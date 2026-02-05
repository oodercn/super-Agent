ood.Class('OODER.HomePage', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function(child) {
                children.push(child.get(0))
            };
            
            ood.merge(properties, host.properties);
            
            // 创建页面布局容器 - 使用Layout组件作为根容器
            append(ood.crbefore',
                        pos: 'before',
                        "size"eate("ood.UI.Layout")
                            .setHost(host, "pageContainer")
                            .setName("pageContainer")
                            .setPosition("relative")
                            .setLeft("0")
                            .setTop("0")
                            .setWidth("100%")
                            .setHeight("100%")
                            .setVisibility("visible")
                            .setType("vertical")
                            .setItems([
                                {
                                    id: ': 60,
                        "overflow": "hidden",
                        "transparent": true,
                        "locked": true, // 锁定top栏
                        "cmd": false // 隐藏handler
                    },
                    {
                        id: 'main',
                        pos: 'main',
                        "overflow": "auto",
                        "transparent": true
                    },
                    {
                        id: 'after',
                        pos: 'after',
                        "size": 100,
                        "overflow": "hidden",
                        "transparent": true
                    }
                ])
                .setClassName("ood-layout")
            );

            // 创建统一的CSS样式管理组件
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host, "globalCssBox")
                .setClassName("ood-global-css")
                // 页面布局样式
                .setNormalStatus({
                    "ood-layout": {
                        "background-color":"#121212;",
                        "MOVE": "display:none;"
                    },
                    // 导航栏样式
                    "ood-navbar": {
                        "box-shadow":"0 2px 10px rgba(0,0,0,0.3);",
                        "color":"#ffffff;",
                        "height":"60px;",
                        "background-color":"#1e1e1e;",
                        "BORDER": "background-color:#1e1e1e; height:60px; line-height:60px;"
                    },
                    // 英雄区域样式
                    "ood-hero": {
                        "color":"#fff;",
                        "text-align":"center;",
                        "background":"linear-gradient(135deg, #1e1e1e 0%, #2d2d2d 100%);",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // 功能介绍样式
                    "ood-features": {
                        "padding":"80px 0;",
                        "color":"#ffffff;",
                        "background-color":"#1e1e1e;",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // 产品特点样式
                    "ood-product-showcase": {
                        "padding":"80px 0;",
                        "color":"#ffffff;",
                        "background-color":"#2d2d2d;",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // 技术栈样式
                    "ood-tech-stack": {
                        "padding":"80px 0;",
                        "color":"#ffffff;",
                        "background-color":"#1e1e1e;",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // 联系我们样式
                    "ood-contact": {
                        "padding":"80px 0;",
                        "color":"#fff;",
                        "text-align":"center;",
                        "background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%);",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // 页脚样式
                    "ood-footer": {
                        "padding":"40px 0;",
                        "color":"#fff;",
                        "text-align":"center;",
                        "background-color":"#0d0d0d;",
                        "overflow":"hidden;",
                        "scrollbar-width":"none;",
                        "-ms-overflow-style":"none;"
                    },
                    // Gallery组件通用样式
                    "ood-gallery": {
                        "margin":"0 auto;",
                        "padding":"0 20px;",
                        "ITEMS": "display:flex; flex-wrap:wrap; justify-content:center; gap:30px;",
                        "ITEM": "width:350px; background-color:#2d2d2d; border-radius:12px; padding:40px 30px; text-align:center; transition:all 0.3s ease; box-shadow:0 4px 15px rgba(0,0,0,0.1);",
                        "ITEM:hover": "transform:translateY(-10px); box-shadow:0 10px 30px rgba(0,0,0,0.2);",
                        "ICON": "font-size:60px; margin-bottom:20px; color:#667eea; display:block;",
                        "CAPTION": "font-size:24px; font-weight:700; color:#ffffff; margin-bottom:15px; line-height:1.3;",
                        "COMMENT": "font-size:16px; color:#a0a0a0; line-height:1.6;"
                    },
                    // 技术栈Gallery样式
                    "ood-tech-gallery": {
                        "margin":"0 auto;",
                        "padding":"0 20px;",
                        "ITEMS": "display:flex; flex-wrap:wrap; justify-content:center; gap:20px;",
                        "ITEM": "width:150px; background-color:#2d2d2d; border-radius:12px; padding:30px 20px; text-align:center; transition:all 0.3s ease; box-shadow:0 4px 15px rgba(0,0,0,0.1);",
                        "ITEM:hover": "transform:translateY(-8px); box-shadow:0 8px 25px rgba(0,0,0,0.2);",
                        "ICON": "font-size:50px; margin-bottom:15px; display:block;",
                        "CAPTION": "font-size:16px; font-weight:600; color:#ffffff; line-height:1.3;"
                    },
                    // item:hover集成样式
                    "ood-css-nrh": {
                        "color":"#eeeeee",
                        "border-radius":"6px",
                        "box-shadow":"inset 0px 1px 0px #87C1DD",
                        "text-shadow":"0 -1px 0 #297192",
                        "$gradient":{
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
                            "type":"linear",
                            "orient":"T"
                        },
                        "cursor":"pointer",
                        "border-top":"solid #3899C6  1px",
                        "border-right":"solid #3899C6  1px",
                        "border-bottom":"solid #3899C6  1px",
                        "border-left":"solid #3899C6  1px"
                    }
                })
                .setActiveStatus({
                    "ood-css-nrh": {
                        "box-shadow":"inset 0px 1px 2px #297192",
                        "background-image":"none",
                        "background-color":"#4BA3CC"
                    }
                })
            );

            // 创建导航栏 - 使用MenuBar组件
            host.pageContainer.append(ood.create("ood.UI.MenuBar")
                .setHost(host, "navbar")
                .setName("navbar")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("100%")
                .setZIndex(1000)
                .setBorder("flat")
                .setVisibility("visible")
                .setHAlign("left") // menbar居左排列
                .setItems([
                    {id: 'logo', caption: 'OODER', itemStyle: 'font-size:24px; font-weight:700; color:#ffffff;'},
                    {id: 'features', caption: '功能'},
                    {id: 'showcase', caption: '产品'},
                    {id: 'tech', caption: '技术'},
                    {id: 'contact', caption: '联系我们'},
                    {id: 'designer', caption: '进入设计器', itemStyle: 'background-color:#667eea; color:#fff; padding:12px 30px; border-radius:50px; font-weight:600;'}
                ])
                .setClassName("ood-navbar")
            , "before");
            
            // 创建英雄区域 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "hero")
                .setName("hero")
                .setPosition("relative")
                .setWidth("100%")
                .setHeight("600px")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-hero")
            , "main");
            
            // 创建SVG Paper画板
            host.hero.append(ood.create("ood.UI.SVGPaper")
                .setHost(host, "heroSvgPaper")
                .setName("heroSvgPaper")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("100%")
                .setHeight("100%")
                .setVisibility("visible")
            );
            
            // 创建SVG标题文本
            host.heroSvgPaper.append(ood.create("ood.svg.text")
                .setHost(host, "heroTitleSvg")
                .setName("heroTitleSvg")
                .setAttr({
                    "KEY": {
                        x: "50%",
                        y: "40%",
                        text: "低代码开发平台",
                        "font-family": "Arial, sans-serif",
                        "font-size": "48px",
                        "font-weight": "700",
                        "fill": "#ffffff",
                        "text-anchor": "middle",
                        "opacity": "0",
                        "transform": "translate(0, 50)"
                    }
                })
            );
            
            // 创建SVG描述文本
            host.heroSvgPaper.append(ood.create("ood.svg.text")
                .setHost(host, "heroDescSvg")
                .setName("heroDescSvg")
                .setAttr({
                    "KEY": {
                        x: "50%",
                        y: "55%",
                        text: "快速构建企业级应用，无需复杂编码，可视化拖拽即可完成",
                        "font-family": "Arial, sans-serif",
                        "font-size": "20px",
                        "fill": "#ffffff",
                        "text-anchor": "middle",
                        "opacity": "0",
                        "transform": "translate(0, 50)"
                    }
                })
            );
            
            // 创建英雄区域按钮
            host.hero.append(ood.create("ood.UI.Button")
                .setHost(host, "heroBtn")
                .setName("heroBtn")
                .setPosition("absolute")
                .setLeft("50%")
                .setTop("70%")
                .setWidth("auto")
                .setHeight("auto")
                .setCaption("立即开始")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "inline-block",
                        "padding": "12px 30px",
                        "background-color": "#fff",
                        "color": "#667eea",
                        "text-decoration": "none",
                        "border-radius": "50px",
                        "font-weight": "600",
                        "transition": "all 0.3s ease",
                        "box-shadow": "0 4px 15px rgba(0,0,0,0.2)",
                        "cursor": "pointer",
                        "border": "none",
                        "transform": "translate(-50%, -50%)",
                        "opacity": "0"
                    },
                    "CAPTION": {
                        "color": "inherit",
                        "text-decoration": "none"
                    }
                })
            );
            
            // 创建功能介绍区域 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "features")
                .setName("features")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("100%")
                .setHeight("auto")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-features")
            , "main");
            
            // 创建功能介绍标题容器
            host.features.append(ood.create("ood.UI.Block")
                .setHost(host, "featuresTitle")
                .setName("featuresTitle")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "margin": "0 auto",
                        "padding": "0 20px",
                        "text-align": "center",
                        "margin-bottom": "60px",
                        "overflow": "hidden",
                        "scrollbar-width": "none",
                        "-ms-overflow-style": "none"
                    }
                })
            );
            
            // 创建功能介绍标题文本
            host.featuresTitle.append(ood.create("ood.UI.Label")
                .setHost(host, "featuresTitleText")
                .setName("featuresTitleText")
                .setCaption("核心功能")
                .setCustomStyle({
                    "CAPTION": {
                        "font-size": "36px",
                        "color": "#ffffff"
                    }
                })
            );
            
            // 创建功能介绍内容 - 使用Gallery组件
            host.features.append(ood.create("ood.UI.Gallery")
                .setHost(host, "featuresContent")
                .setName("featuresContent")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setColumns(3)
                .setVisibility("visible")
                .setItems([
                    {id: 'feature1', caption: '可视化设计', comment: '拖拽式界面设计，所见即所得，快速构建应用界面', imageClass: 'ri-paint-brush-line', itemStyle: 'font-size:48px; color:#667eea;'},
                    {id: 'feature2', caption: '快速开发', comment: '低代码开发，大幅缩短开发周期，提高开发效率', imageClass: 'ri-rocket-line', itemStyle: 'font-size:48px; color:#667eea;'},
                    {id: 'feature3', caption: '丰富组件库', comment: '内置丰富的UI组件，满足各种业务需求', imageClass: 'ri-apps-line', itemStyle: 'font-size:48px; color:#667eea;'},
                    {id: 'feature4', caption: '多端适配', comment: '支持桌面、平板、移动设备，一次开发多端运行', imageClass: 'ri-device-line', itemStyle: 'font-size:48px; color:#667eea;'},
                    {id: 'feature5', caption: 'API 集成', comment: '轻松集成各种API，实现数据交互和业务逻辑', imageClass: 'ri-link', itemStyle: 'font-size:48px; color:#667eea;'},
                    {id: 'feature6', caption: '一键部署', comment: '开发完成后，一键部署到生产环境', imageClass: 'ri-upload-cloud-line', itemStyle: 'font-size:48px; color:#667eea;'}
                ])
                .setClassName("ood-css-nrh ood-gallery")
            );
            
            // 创建产品特点区域 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "productShowcase")
                .setName("productShowcase")
                .setPosition("relative")
                .setWidth("100%")
                .setHeight("auto")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-product-showcase")
            );
            
            // 创建产品特点内容 - 使用TitleBlock组件
            host.productShowcase.append(ood.create("ood.UI.TitleBlock")
                .setHost(host, "showcaseContent")
                .setName("showcaseContent")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setColumns(2)
                .setVisibility("visible")
                .setItems([
                    {id: 'showcase1', title: '强大的设计器', msgnum: '1', flagClass: 'ri-layout-2-line', caption: 'OODER 设计器提供了直观的可视化界面，让您可以轻松创建复杂的应用程序。支持多种视图模式，包括设计视图、代码视图和预览视图，满足不同开发人员的需求。'},
                    {id: 'showcase2', title: '丰富的组件库', msgnum: '2', flagClass: 'ri-puzzle-line', caption: '设计器内置了丰富的组件库，包括基础组件、布局组件、数据组件、交互组件等，您可以根据需要拖拽使用。同时，设计器还支持自定义组件，让您可以扩展设计器的功能。'}
                ])
                .setCustomStyle({
                    "KEY": {
                        "margin": "0 auto",
                        "padding": "0 20px"
                    },
                    "ITEMS": {
                        "display": "flex",
                        "flex-wrap": "wrap",
                        "justify-content": "center",
                        "gap": "30px"
                    },
                    "ITEM": {
                        "width": "550px",
                        "background-color": "#1e1e1e",
                        "border-radius": "12px",
                        "padding": "40px",
                        "transition": "all 0.3s ease",
                        "box-shadow": "0 4px 15px rgba(0,0,0,0.1)",
                        "position": "relative"
                    },
                    "FLAG": {
                        "font-size": "80px",
                        "color": "#667eea",
                        "opacity": "0.1",
                        "position": "absolute",
                        "left": "30px",
                        "top": "30px",
                        "z-index": "0"
                    },
                    "TITLE": {
                        "font-size": "28px",
                        "font-weight": "700",
                        "color": "#ffffff",
                        "margin-bottom": "20px",
                        "position": "relative",
                        "z-index": "1"
                    },
                    "CAPTION": {
                        "font-size": "16px",
                        "color": "#a0a0a0",
                        "line-height": "1.7",
                        "margin-bottom": "25px",
                        "position": "relative",
                        "z-index": "1"
                    },
                    "MSGNUM": {
                        "display": "none"
                    }
                })
            );
            
            // 创建技术栈区域 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "techStack")
                .setName("techStack")
                .setPosition("relative")
                .setWidth("100%")
                .setHeight("auto")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-tech-stack")
            );
            
            // 创建技术栈标题容器
            host.techStack.append(ood.create("ood.UI.Block")
                .setHost(host, "techTitle")
                .setName("techTitle")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "margin": "0 auto",
                        "padding": "0 20px",
                        "text-align": "center",
                        "margin-bottom": "60px",
                        "overflow": "hidden",
                        "scrollbar-width": "none",
                        "-ms-overflow-style": "none"
                    }
                })
            );
            
            // 创建技术栈标题文本
            host.techTitle.append(ood.create("ood.UI.Label")
                .setHost(host, "techTitleText")
                .setName("techTitleText")
                .setCaption("技术栈")
                .setCustomStyle({
                    "CAPTION": {
                        "font-size": "36px",
                        "color": "#ffffff"
                    }
                })
            );
            
            // 创建技术栈内容 - 使用Gallery组件
            host.techStack.append(ood.create("ood.UI.Gallery")
                .setHost(host, "techContent")
                .setName("techContent")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setColumns(6)
                .setVisibility("visible")
                .setItems([
                    {id: 'tech1', caption: 'JavaScript', imageClass: 'ri-javascript-line', itemStyle: 'font-size:48px; color:#f7df1e;'},
                    {id: 'tech2', caption: 'HTML5', imageClass: 'ri-html5-line', itemStyle: 'font-size:48px; color:#e34f26;'},
                    {id: 'tech3', caption: 'CSS3', imageClass: 'ri-css3-line', itemStyle: 'font-size:48px; color:#1572b6;'},
                    {id: 'tech4', caption: 'SVG', imageClass: 'ri-file-image-line', itemStyle: 'font-size:48px; color:#ffb13b;'},
                    {id: 'tech5', caption: '响应式设计', imageClass: 'ri-layout-responsive-line', itemStyle: 'font-size:48px; color:#4caf50;'},
                    {id: 'tech6', caption: '开源框架', imageClass: 'ri-git-branch-line', itemStyle: 'font-size:48px; color:#f05032;'}
                ])
                .setClassName("ood-css-nrh ood-tech-gallery")
            );
            
            // 创建联系我们区域 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "contact")
                .setName("contact")
                .setPosition("relative")
                .setWidth("100%")
                .setHeight("auto")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-contact")
            );
            
            // 创建联系我们内容容器
            host.contact.append(ood.create("ood.UI.Block")
                .setHost(host, "contactContent")
                .setName("contactContent")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "margin": "0 auto",
                        "padding": "0 20px",
                        "text-align": "center",
                        "overflow": "hidden",
                        "scrollbar-width": "none",
                        "-ms-overflow-style": "none"
                    }
                })
            );
            
            // 创建联系我们标题
            host.contactContent.append(ood.create("ood.UI.Label")
                .setHost(host, "contactTitle")
                .setName("contactTitle")
                .setCaption("联系我们")
                .setCustomStyle({
                    "CAPTION": {
                        "font-size": "36px",
                        "margin-bottom": "20px",
                        "color": "#ffffff"
                    }
                })
            );
            
            // 创建联系我们描述
            host.contactContent.append(ood.create("ood.UI.Label")
                .setHost(host, "contactDesc")
                .setName("contactDesc")
                .setCaption("如果您有任何问题或建议，欢迎随时联系我们")
                .setCustomStyle({
                    "CAPTION": {
                        "font-size": "18px",
                        "margin-bottom": "40px",
                        "opacity": "0.9",
                        "color": "#ffffff"
                    }
                })
            );
            
            // 创建联系我们按钮
            host.contactContent.append(ood.create("ood.UI.Button")
                .setHost(host, "contactBtn")
                .setName("contactBtn")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("auto")
                .setHeight("auto")
                .setCaption("发送邮件")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "inline-block",
                        "padding": "12px 30px",
                        "background-color": "#fff",
                        "color": "#667eea",
                        "text-decoration": "none",
                        "border-radius": "50px",
                        "font-weight": "600",
                        "transition": "all 0.3s ease",
                        "box-shadow": "0 4px 15px rgba(0,0,0,0.2)",
                        "cursor": "pointer",
                        "border": "none"
                    },
                    "CAPTION": {
                        "color": "inherit",
                        "text-decoration": "none"
                    }
                })
            );
            
            // 创建页脚 - 使用Panel组件
            host.pageContainer.append(ood.create("ood.UI.Panel")
                .setHost(host, "footer")
                .setName("footer")
                .setPosition("relative")
                .setWidth("100%")
                .setHeight("auto")
                .setVisibility("visible")
                .setCaption("")
                .setClassName("ood-footer")
            , "after");
            
            // 创建页脚内容容器
            host.footer.append(ood.create("ood.UI.Block")
                .setHost(host, "footerContent")
                .setName("footerContent")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("1200px")
                .setHeight("auto")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "margin": "0 auto",
                        "padding": "0 20px",
                        "display": "flex",
                        "justify-content": "space-between",
                        "align-items": "center",
                        "flex-wrap": "wrap",
                        "gap": "20px",
                        "overflow": "hidden",
                        "scrollbar-width": "none",
                        "-ms-overflow-style": "none"
                    }
                })
            );
            
            // 创建版权信息
            host.footerContent.append(ood.create("ood.UI.Label")
                .setHost(host, "footerCopyright")
                .setName("footerCopyright")
                .setCaption("© 2025 OODER. 保留所有权利。")
                .setCustomStyle({
                    "CAPTION": {
                        "color": "#ffffff"
                    }
                })
            );
            
            // 创建导航链接容器
            host.footerContent.append(ood.create("ood.UI.Block")
                .setHost(host, "footerLinks")
                .setName("footerLinks")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("auto")
                .setHeight("auto")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "flex",
                        "list-style": "none",
                        "gap": "20px",
                        "margin": "0",
                        "padding": "0"
                    }
                })
            );
            
            // 创建隐私政策链接
            host.footerLinks.append(ood.create("ood.UI.Label")
                .setHost(host, "footerPrivacy")
                .setName("footerPrivacy")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("auto")
                .setHeight("auto")
                .setCaption("隐私政策")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "inline-block",
                        "color": "#fff",
                        "text-decoration": "none",
                        "transition": "color 0.3s ease"
                    },
                    "CAPTION": {
                        "color": "inherit",
                        "text-decoration": "inherit",
                        "cursor": "pointer"
                    }
                })
            );
            
            // 创建服务条款链接
            host.footerLinks.append(ood.create("ood.UI.Label")
                .setHost(host, "footerTerms")
                .setName("footerTerms")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("auto")
                .setHeight("auto")
                .setCaption("服务条款")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "inline-block",
                        "color": "#fff",
                        "text-decoration": "none",
                        "transition": "color 0.3s ease"
                    },
                    "CAPTION": {
                        "color": "inherit",
                        "text-decoration": "inherit",
                        "cursor": "pointer"
                    }
                })
            );
            
            // 创建关于我们链接
            host.footerLinks.append(ood.create("ood.UI.Label")
                .setHost(host, "footerAbout")
                .setName("footerAbout")
                .setPosition("relative")
                .setLeft("0")
                .setTop("0")
                .setWidth("auto")
                .setHeight("auto")
                .setCaption("关于我们")
                .setVisibility("visible")
                .setCustomStyle({
                    "KEY": {
                        "display": "inline-block",
                        "color": "#fff",
                        "text-decoration": "none",
                        "transition": "color 0.3s ease"
                    },
                    "CAPTION": {
                        "color": "inherit",
                        "text-decoration": "inherit",
                        "cursor": "pointer"
                    }
                })
            );
            
            return children;
            // ]]Code created by JDSEasy RAD Studio
        },
        
        afterIniComponents: function () {
            // 添加交互效果
            this.addInteractiveEffects();
        },
        
        addInteractiveEffects: function () {
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
        }
    }
});