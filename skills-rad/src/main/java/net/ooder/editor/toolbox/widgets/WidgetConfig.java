package net.ooder.editor.toolbox.widgets;

import net.ooder.esd.annotation.ui.ComponentType;

/**
 * 组件配置枚举类
 * 用于统一管理RAD组件配置
 */
public enum WidgetConfig {

    // 基础表单组件
    BASEFORM("ood.form", "常用组件", "ri-file-line",ComponentType.INPUT,  ComponentType.BUTTON,
            ComponentType.RICHEDITOR,
            ComponentType.RADIOBOX, ComponentType.CHECKBOX,ComponentType.LINK,ComponentType.MULTILINES,
            ComponentType.IMAGE, ComponentType.ICON, ComponentType.ELEMENT,
            ComponentType.LABEL
    ),

    // 高级表单组件
    ADVFORM("ood.advForm", "高级组件", "ri-edit-line",
            ComponentType.COMBOINPUT,  ComponentType.DATEPICKER,
            ComponentType.TIMEPICKER, ComponentType.TIMEPICKER,
            ComponentType.PROGRESSBAR, ComponentType.SLIDER,ComponentType.LIST,
            ComponentType.FOLDINGLIST
    ),
    // 布局组件
    BUTTON_VIEWS("ood.Tabs", "导航容器", "ri-layout-column-line",
            ComponentType.BUTTONVIEWS, ComponentType.TABS, ComponentType.BUTTONLAYOUT, ComponentType.STACKS, ComponentType.FOLDINGTABS, ComponentType.TREEBAR, ComponentType.MENUBAR),

    CONTAINER("ood.UI.absContainer", "面板容器", "ri-layout-grid-line",
            ComponentType.LAYOUT, ComponentType.DIALOG, ComponentType.PANEL, ComponentType.GROUP, ComponentType.BLOCK, ComponentType.DIV),

    TOOLBAR("ood.UI.Bar", "工具栏", "ri-tools-line",
            ComponentType.MENUBAR, ComponentType.POPMENU, ComponentType.TOOLBAR, ComponentType.STATUSBUTTONS, ComponentType.HTMLBUTTON),


    // 组件
    COMPONENTS("ood.components", "集成组件", "ri-window-line",
            ComponentType.GALLERY, ComponentType.TREEVIEW, ComponentType.TREEGRID,
            ComponentType.FILEUPLOAD, ComponentType.OPINION, ComponentType.FORMLAYOUT, ComponentType.TITLEBLOCK, ComponentType.CONTENTBLOCK),

    // API组件
    API_COMPONENTS("ood.api", "API组件", "ri-cloud-line", ComponentType.HIDDENINPUT,
            ComponentType.APICALLER, ComponentType.MESSAGESERVICE, ComponentType.MQTT),



    SVGPAPER("ood.svg", "绘图","ri-artboard-line", ComponentType.SVGPAPER,ComponentType.FCHART,ComponentType.ECHARTS),

    // SVG组件
    SVGBASE("ood.svgbase", "SVG基础组件", "ri-edit-2-line", ComponentType.SVGCIRCLE,
            ComponentType.SVGELLIPSE, ComponentType.SVGRECT, ComponentType.SVGIMAGE, ComponentType.SVGTEXT,ComponentType.SVGPATH),
    // SVG组件
    SVGCOMB("ood.svgcom", "SVG复合组件", "ri-layout-6-line", ComponentType.SVGRECTCOMB,
            ComponentType.SVGGROUP, ComponentType.SVGIMAGECOMB, ComponentType.SVGELLIPSECOMB, ComponentType.SVGCIRCLECOMB,ComponentType.SVGPATHCOMB,ComponentType.SVGPAPER),



    // 样式组件
    STYLE_COMPONENTS("ood.style", "样式动画", "ri-stack-line",
            ComponentType.TIMER, ComponentType.ANIMBINDER, ComponentType.CSSBOX),
    //以下是移动组件
    // 基础组件
    MBASE("ood.mobile.base", "基础组件", "ri-mobile-phone-line",
            ComponentType.MBUTTON, ComponentType.MINPUT, ComponentType.MLIST, ComponentType.MSWITCH),

    // 布局组件
    MLAYOUT("ood.mobile.layout", "布局组件", "ri-grid-line",
            ComponentType.MPANEL, ComponentType.MLAYOUT, ComponentType.MGRID),

    // 导航组件
    MNAV("ood.mobile.nav", "导航组件", "ri-menu-line",
            ComponentType.MNAVBAR, ComponentType.MTABBAR, ComponentType.MDRAWER),

    // 反馈组件
    MFDBACK("ood.mobile.feedback", "反馈组件", "ri-information-line",
            ComponentType.MTOAST, ComponentType.MMODAL);

    private final String id;
    private final String key;
    private final String caption;
    private final String imageClass;
    private final boolean group;

    private ComponentType[] componentTypes;


    WidgetConfig(String key, String caption, String imageClass, ComponentType... componentTypes) {
        this.id = name();
        this.key = key;
        this.caption = caption;
        this.imageClass = imageClass;
        this.group = componentTypes.length > 0;
        this.componentTypes = componentTypes;

    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public boolean isGroup() {
        return group;
    }

    public ComponentType[] getComponentTypes() {
        return componentTypes;
    }

    public void setComponentTypes(ComponentType[] componentTypes) {
        this.componentTypes = componentTypes;
    }

}