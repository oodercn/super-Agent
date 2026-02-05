package net.ooder.editor.console.java;

import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.dsm.enums.DSMType;

public enum PackageCatItem implements TreeItem {
    View("视图", DSMType.VIEW, false, true, true, CatPackageService.class),
    Aggregation("路由控制", DSMType.AGGREGATION, true, true, true, CatPackageService.class),
    Repository("接口实现", DSMType.REPOSITORY, true, true, true, CatPackageService.class),
    User("常规编码", DSMType.USERDOMAIN, true, true, true, CatPackageService.class);
    private final String name;
    private final DSMType dsmType;
    private final Class[] bindClass;
    private final String imageClass;
    private final boolean initFold;
    private final boolean dynDestory;
    private final boolean lazyLoad;

    PackageCatItem(String name, DSMType dsmType, Boolean initFold, Boolean lazyLoad, Boolean dynDestory, Class... bindClass) {
        this.name = name;
        this.dsmType = dsmType;
        this.imageClass = dsmType.getImageClass();
        this.bindClass = bindClass;
        this.initFold = initFold;
        this.lazyLoad = lazyLoad;
        this.dynDestory = dynDestory;

    }


    public DSMType getDsmType() {
        return dsmType;
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
        return bindClass;
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
