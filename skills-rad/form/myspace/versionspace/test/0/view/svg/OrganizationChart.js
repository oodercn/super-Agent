// 组织机构图示例 - 展示公司层级结构
ood.Class("svg.OrganizationChart", "ood.Module", {
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
            "caption": "组织机构图",
            "currComponentAlias": "OrganizationChart",
            "dock": "fill",
            "panelType": "block",
            "title": "组织机构图"
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
            
            // 创建 SVGPaper 组件
            var svgPaper = ood.create("ood.UI.SVGPaper")
                .setHost(host, "svgPaper")
                .setName("svgPaper")
                .setWidth("100%")
                .setHeight("100%")
                .setZIndex(1001)
                .setVisibility("visible")
                .setOverflow("visible")
                .setGraphicZIndex(2);
            append(svgPaper);
            
            // 标题
            svgPaper.append(ood.create("ood.svg.text")
                .setHost(svgPaper, "text_title")
                .setAttr({"KEY": {x: 700, y: 60, 'font-size': '24px', 'font-weight': 'bold', fill: '#2c3e50', text: '公司组织机构图'}}));
            
            // 顶层 - 董事会
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_board")
                .setAttr({"KEY": {x: 550, y: 100, width: 300, height: 60, rx: 5, ry: 5, fill: '#3498db', stroke: '#2980b9', 'stroke-width': 1.5}, "TEXT": {text: '董事会', fill: '#fff', 'font-size': '18px', 'font-weight': 'bold'}}));
            
            // 第二层 - CEO
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_ceo")
                .setAttr({"KEY": {x: 625, y: 200, width: 150, height: 50, rx: 5, ry: 5, fill: '#2ecc71', stroke: '#27ae60', 'stroke-width': 1.5}, "TEXT": {text: 'CEO', fill: '#fff', 'font-size': '16px', 'font-weight': 'bold'}}));
            
            // 连接董事会和CEO
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_board_to_ceo")
                .setAttr({"KEY": {path: "M,700,160L,700,200", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_board")
                .setFromPoint("bottom")
                .setToObj("rect_ceo")
                .setToPoint("top"));
            
            // 第三层 - 各部门总监
            // CTO
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_cto")
                .setAttr({"KEY": {x: 400, y: 300, width: 120, height: 45, rx: 5, ry: 5, fill: '#f39c12', stroke: '#e67e22', 'stroke-width': 1.5}, "TEXT": {text: 'CTO', fill: '#fff', 'font-size': '14px', 'font-weight': 'bold'}}));
            
            // CFO
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_cfo")
                .setAttr({"KEY": {x: 580, y: 300, width: 120, height: 45, rx: 5, ry: 5, fill: '#f39c12', stroke: '#e67e22', 'stroke-width': 1.5}, "TEXT": {text: 'CFO', fill: '#fff', 'font-size': '14px', 'font-weight': 'bold'}}));
            
            // COO
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_coo")
                .setAttr({"KEY": {x: 760, y: 300, width: 120, height: 45, rx: 5, ry: 5, fill: '#f39c12', stroke: '#e67e22', 'stroke-width': 1.5}, "TEXT": {text: 'COO', fill: '#fff', 'font-size': '14px', 'font-weight': 'bold'}}));
            
            // CMO
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_cmo")
                .setAttr({"KEY": {x: 940, y: 300, width: 120, height: 45, rx: 5, ry: 5, fill: '#f39c12', stroke: '#e67e22', 'stroke-width': 1.5}, "TEXT": {text: 'CMO', fill: '#fff', 'font-size': '14px', 'font-weight': 'bold'}}));
            
            // 连接CEO和各总监
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_ceo_to_cto")
                .setAttr({"KEY": {path: "M,700,250L,500,250L,500,300", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_ceo")
                .setFromPoint("bottom")
                .setToObj("rect_cto")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_ceo_to_cfo")
                .setAttr({"KEY": {path: "M,700,250L,640,250L,640,300", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_ceo")
                .setFromPoint("bottom")
                .setToObj("rect_cfo")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_ceo_to_coo")
                .setAttr({"KEY": {path: "M,700,250L,820,250L,820,300", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_ceo")
                .setFromPoint("bottom")
                .setToObj("rect_coo")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_ceo_to_cmo")
                .setAttr({"KEY": {path: "M,700,250L,1000,250L,1000,300", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_ceo")
                .setFromPoint("bottom")
                .setToObj("rect_cmo")
                .setToPoint("top"));
            
            // 第四层 - 各部门经理
            // 技术部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_tech_manager")
                .setAttr({"KEY": {x: 350, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '技术部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 研发部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_rnd_manager")
                .setAttr({"KEY": {x: 500, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '研发部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 财务部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_finance_manager")
                .setAttr({"KEY": {x: 630, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '财务部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 运营部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_operation_manager")
                .setAttr({"KEY": {x: 800, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '运营部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 市场部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_marketing_manager")
                .setAttr({"KEY": {x: 950, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '市场部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 销售部
            svgPaper.append(ood.create("ood.svg.rectComb")
                .setHost(svgPaper, "rect_sales_manager")
                .setAttr({"KEY": {x: 1100, y: 400, width: 100, height: 40, rx: 5, ry: 5, fill: '#9b59b6', stroke: '#8e44ad', 'stroke-width': 1.5}, "TEXT": {text: '销售部经理', fill: '#fff', 'font-size': '12px', 'font-weight': 'bold'}}));
            
            // 连接各总监和经理
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_cto_to_tech")
                .setAttr({"KEY": {path: "M,460,345L,420,345L,420,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_cto")
                .setFromPoint("bottom")
                .setToObj("rect_tech_manager")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_cto_to_rnd")
                .setAttr({"KEY": {path: "M,460,345L,550,345L,550,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_cto")
                .setFromPoint("bottom")
                .setToObj("rect_rnd_manager")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_cfo_to_finance")
                .setAttr({"KEY": {path: "M,640,345L,680,345L,680,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_cfo")
                .setFromPoint("bottom")
                .setToObj("rect_finance_manager")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_coo_to_operation")
                .setAttr({"KEY": {path: "M,820,345L,850,345L,850,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_coo")
                .setFromPoint("bottom")
                .setToObj("rect_operation_manager")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_cmo_to_marketing")
                .setAttr({"KEY": {path: "M,1000,345L,1000,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_cmo")
                .setFromPoint("bottom")
                .setToObj("rect_marketing_manager")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_cmo_to_sales")
                .setAttr({"KEY": {path: "M,1000,345L,1150,345L,1150,400", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_cmo")
                .setFromPoint("bottom")
                .setToObj("rect_sales_manager")
                .setToPoint("top"));
            
            // 第五层 - 员工代表
            // 技术部员工
            svgPaper.append(ood.create("ood.svg.circleComb")
                .setHost(svgPaper, "circle_tech_staff1")
                .setAttr({"KEY": {cx: 350, cy: 480, r: 25, fill: '#1abc9c', stroke: '#16a085', 'stroke-width': 1.5}, "TEXT": {text: '员工A', fill: '#fff', 'font-size': '10px', 'font-weight': 'bold'}}));
            
            svgPaper.append(ood.create("ood.svg.circleComb")
                .setHost(svgPaper, "circle_tech_staff2")
                .setAttr({"KEY": {cx: 450, cy: 480, r: 25, fill: '#1abc9c', stroke: '#16a085', 'stroke-width': 1.5}, "TEXT": {text: '员工B', fill: '#fff', 'font-size': '10px', 'font-weight': 'bold'}}));
            
            // 财务部员工
            svgPaper.append(ood.create("ood.svg.circleComb")
                .setHost(svgPaper, "circle_finance_staff1")
                .setAttr({"KEY": {cx: 630, cy: 480, r: 25, fill: '#1abc9c', stroke: '#16a085', 'stroke-width': 1.5}, "TEXT": {text: '员工C', fill: '#fff', 'font-size': '10px', 'font-weight': 'bold'}}));
            
            // 市场部员工
            svgPaper.append(ood.create("ood.svg.circleComb")
                .setHost(svgPaper, "circle_marketing_staff1")
                .setAttr({"KEY": {cx: 950, cy: 480, r: 25, fill: '#1abc9c', stroke: '#16a085', 'stroke-width': 1.5}, "TEXT": {text: '员工D', fill: '#fff', 'font-size': '10px', 'font-weight': 'bold'}}));
            
            // 连接各经理和员工
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_tech_manager_to_staff1")
                .setAttr({"KEY": {path: "M,420,440L,350,440L,350,455", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_tech_manager")
                .setFromPoint("bottom")
                .setToObj("circle_tech_staff1")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_tech_manager_to_staff2")
                .setAttr({"KEY": {path: "M,420,440L,450,440L,450,455", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_tech_manager")
                .setFromPoint("bottom")
                .setToObj("circle_tech_staff2")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_finance_manager_to_staff")
                .setAttr({"KEY": {path: "M,680,440L,630,440L,630,455", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_finance_manager")
                .setFromPoint("bottom")
                .setToObj("circle_finance_staff1")
                .setToPoint("top"));
            
            svgPaper.append(ood.create("ood.svg.connector")
                .setHost(svgPaper, "conn_marketing_manager_to_staff")
                .setAttr({"KEY": {path: "M,1000,440L,950,440L,950,455", fill: "none", stroke: '#6c757d', 'stroke-width': 1.5, 'arrow-end': 'classic-wide-long'}})
                .setFromObj("rect_marketing_manager")
                .setFromPoint("bottom")
                .setToObj("circle_marketing_staff1")
                .setToPoint("top"));
            
            return children;
        }
    }
});