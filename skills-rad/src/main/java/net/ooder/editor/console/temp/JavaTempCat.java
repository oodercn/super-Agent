package net.ooder.editor.console.temp;

import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.dsm.enums.DSMType;

public enum JavaTempCat implements TabItem {

    REPOSITORY(DSMType.REPOSITORY, JavaTempCatService.class, false, true, true),
    AGGREGATION(DSMType.AGGREGATION, JavaTempCatService.class, false, true, true),
    VIEW(DSMType.VIEW, JavaTempCatService.class, false, true, true);

    private final String name;

    private final Class bindClass;

    private final String imageClass;

    private final boolean initFold;

    private final boolean dynDestory;

    private final boolean dynLoad;

    private final String caption;

    private final String tips;

    DSMType dsmType;


    JavaTempCat(DSMType dsmType, Class bindClass, Boolean initFold, Boolean dynLoad, Boolean dynDestory) {
        this.name = dsmType.name();
        this.imageClass = dsmType.getImageClass();
        this.bindClass = bindClass;
        this.initFold = initFold;
        this.dynLoad = dynLoad;
        this.dynDestory = dynDestory;
        this.dsmType = dsmType;
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

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
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
