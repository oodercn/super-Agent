package net.ooder.editor.toolbox.widgets;

import net.ooder.editor.toolbox.ToolBox;
import net.ooder.editor.toolbox.WidgetEnums;

import java.util.ArrayList;
import java.util.List;

public class WidgetCatBean {

    private final String id;
    private final String key;
    private final String caption;
    private final String imageClass;
    private final boolean group;
    private List<WidgetConfigBean> sub = new ArrayList<>();


    public WidgetCatBean(ToolBox.ToolBoxEnum toolBoxEnum) {
        this.id = toolBoxEnum.name();
        this.key = toolBoxEnum.getType();
        this.imageClass = toolBoxEnum.getImageClass();
        this.caption = toolBoxEnum.getName();
        this.group = true;
        for (WidgetEnums widgetEnums : toolBoxEnum.getWidgetEnums()) {
            WidgetConfigBean widgetItem = new WidgetConfigBean(widgetEnums.getWidgets());
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

    public List<WidgetConfigBean> getSub() {
        return sub;
    }

    public void setSub(List<WidgetConfigBean> sub) {
        this.sub = sub;
    }
}
