package net.ooder.editor.console.ddd.esdclass;

import net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavService;
import net.ooder.esd.annotation.TreeItem;

public enum DDDModuleNavItem implements TreeItem {
    ViewConfig("视图配置", "ri-settings-3-line", DDDModuleViewService.class, false, false, true),
    MethodConfig("视图路由", "ri-refresh-line", DDDModuleMethodService.class, true, true, true),
    CustomMethodConfig("领域事件", "ri-settings-3-line", AggEntityMethodNavService.class, true, true, true);
    private final String name;
    private final Class bindClass;
    private final String imageClass;
    private final boolean initFold;
    private final boolean dynDestory;
    private final boolean lazyLoad;

    DDDModuleNavItem(String name, String imageClass, Class bindClass, Boolean initFold, Boolean lazyLoad, Boolean dynDestory) {
        this.name = name;
        this.imageClass = imageClass;
        this.bindClass = bindClass;
        this.initFold = initFold;
        this.lazyLoad = lazyLoad;
        this.dynDestory = dynDestory;

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
    public boolean isLazyLoad() {
        return lazyLoad;
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
