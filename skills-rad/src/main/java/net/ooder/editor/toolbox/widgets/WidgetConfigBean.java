package net.ooder.editor.toolbox.widgets;

import net.ooder.esd.annotation.ui.ComponentType;

import java.util.ArrayList;
import java.util.List;

public class WidgetConfigBean {

    private final String id;
    private final String key;
    private final String caption;
    private final String imageClass;
    private final boolean group;

    private List<WidgetItem> sub=new ArrayList<>();


    public WidgetConfigBean(WidgetConfig widgetConfig) {
        this.id = widgetConfig.getId();
        this.key = widgetConfig.getId();
        this.imageClass = widgetConfig.getImageClass();
        this.caption = widgetConfig.getCaption();
        this.group = true;
        for(ComponentType componentType:widgetConfig.getComponentTypes()){
            WidgetItem widgetItem=new WidgetItem(componentType);
            sub.add(widgetItem);
        }
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
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

    public List<WidgetItem> getSub() {
        return sub;
    }

    public void setSub(List<WidgetItem> sub) {
        this.sub = sub;
    }
}
