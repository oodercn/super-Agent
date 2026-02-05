package net.ooder.editor.console.temp;

import net.ooder.annotation.Pid;
import net.ooder.esd.annotation.BlockAnnotation;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.TabItemAnnotation;
import net.ooder.esd.annotation.ui.BarLocationType;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SideBarStatusType;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.enums.DSMType;

@BlockAnnotation(borderType = BorderType.none, dock = Dock.fill)
@ButtonViewsAnnotation(barLocation = BarLocationType.top, autoIconColor = false, barSize = "1.5em",
        sideBarStatus = SideBarStatusType.expand)
public class JavaTempFoldingTabs extends NavTabListItem {


    @Pid
    DSMType dsmType;

    @Pid
    JavaTempCat tempCat;

    @TabItemAnnotation(customItems = JavaTempCat.class)
    public JavaTempFoldingTabs(JavaTempCat tempCat) {
        this.dsmType = tempCat.getDsmType();
        this.caption = dsmType.getName() + "(" + dsmType.name() + ")";
        this.id = dsmType.getType();
        this.imageClass = dsmType.getImageClass();
    }

    public JavaTempCat getTempCat() {
        return tempCat;
    }

    public void setTempCat(JavaTempCat tempCat) {
        this.tempCat = tempCat;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }
}
