package net.ooder.editor.console.java.page;

import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.dsm.enums.DSMType;

public enum PagePackageCatItem implements TreeItem {
    View("领域视图", DSMType.VIEW, false, true, true, JavaPagePackageService.class),
    Aggregation("领域模型", DSMType.AGGREGATION, true, true, true, JavaPagePackageService.class),
    Repository("仓储实现", DSMType.REPOSITORY, true, true, true, JavaPagePackageService.class);
    private final String name;
    private final DSMType dsmType;
    private final Class[] bindClass;
    private final String imageClass;
    private final boolean initFold;
    private final boolean dynDestory;
    private final boolean lazyLoad;

    PagePackageCatItem(String name, DSMType dsmType, Boolean initFold, Boolean lazyLoad, Boolean dynDestory, Class... bindClass) {
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
