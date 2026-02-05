package net.ooder.editor.toolbox.widgets;

import net.ooder.esd.annotation.ui.ComponentType;

import java.util.HashMap;
import java.util.Map;

public class WidgetItem {

    String id;
    String key;
    String imageClass;
    String caption;
    boolean draggable;
    Map iniProp = new HashMap();


    public WidgetItem(ComponentType componentType) {
        this.id = componentType.name();
        this.key = componentType.getClassName();
        this.imageClass = componentType.getImageClass();
        this.caption = componentType.getName();
        this.draggable = true;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public Map getIniProp() {
        return iniProp;
    }

    public void setIniProp(Map iniProp) {
        this.iniProp = iniProp;
    }
}
