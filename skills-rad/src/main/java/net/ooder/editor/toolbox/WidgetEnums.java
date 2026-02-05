package net.ooder.editor.toolbox;

import net.ooder.editor.toolbox.widgets.WidgetConfig;
import net.ooder.esd.annotation.field.TabItem;

/**
 * 布局组件枚举类
 * 用于定义所有布局相关的组件
 */
public enum WidgetEnums implements TabItem {


    CONTAINER(WidgetConfig.CONTAINER, WidgetGalleryService.class, false, true, true),

    BUTTON_VIEWS(WidgetConfig.BUTTON_VIEWS, WidgetGalleryService.class, true, true, true),

    TOOLBAR(WidgetConfig.TOOLBAR, WidgetGalleryService.class, true, true, true),


    BASEFORM(WidgetConfig.BASEFORM, WidgetGalleryService.class, false, true, true),

    ADVFORM(WidgetConfig.ADVFORM, WidgetGalleryService.class, true, true, true),



    SVGPAPER(WidgetConfig.SVGPAPER, WidgetGalleryService.class, false, true, true),

    SVGBASE(WidgetConfig.SVGBASE, WidgetGalleryService.class, true, true, true),

    SVGCOMB(WidgetConfig.SVGCOMB, WidgetGalleryService.class, true, true, true),




    COMPONENTS(WidgetConfig.COMPONENTS, WidgetGalleryService.class, false, true, true),

    STYLE_COMPONENTS(WidgetConfig.STYLE_COMPONENTS, WidgetGalleryService.class, true, true, true),

    API_COMPONENTS(WidgetConfig.API_COMPONENTS, WidgetGalleryService.class, true, true, true),


    MBASE(WidgetConfig.MBASE, WidgetGalleryService.class, false, true, true),

    MLAYOUT(WidgetConfig.MLAYOUT, WidgetGalleryService.class, true, true, true),

    MNAV(WidgetConfig.MNAV, WidgetGalleryService.class, true, true, true),

    MFDBACK(WidgetConfig.MFDBACK, WidgetGalleryService.class, true, true, true);


    private final String name;

    private final Class bindClass;

    private final String imageClass;

    private final boolean initFold;

    private final boolean dynDestory;

    private final boolean dynLoad;

    private final String caption;

    private final String tips;

    WidgetConfig widgets;


    WidgetEnums(WidgetConfig widgets, Class bindClass, Boolean initFold, Boolean dynLoad, Boolean dynDestory) {
        this.name = widgets.getCaption();

        this.imageClass = widgets.getImageClass();
        this.bindClass = bindClass;
        this.initFold = initFold;
        this.dynLoad = dynLoad;
        this.dynDestory = dynDestory;
        this.widgets = widgets;
        this.tips = name;
        this.caption = name;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public String getTips() {
        return tips;
    }

    public WidgetConfig getWidgets() {
        return widgets;
    }

    public void setWidgets(WidgetConfig widgets) {
        this.widgets = widgets;
    }

    @Override
    public boolean isInitFold() {
        return initFold;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    @Override
    public boolean isDynLoad() {
        return dynLoad;
    }


    public Class[] getBindClass() {
        return new Class[]{bindClass};
    }


    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getImageClass() {
        return imageClass;
    }
}