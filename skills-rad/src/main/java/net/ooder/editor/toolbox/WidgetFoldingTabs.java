package net.ooder.editor.toolbox;

import net.ooder.annotation.Pid;
import net.ooder.editor.toolbox.widgets.WidgetConfig;
import net.ooder.esd.annotation.NavFoldingTabsAnnotation;
import net.ooder.esd.annotation.TabItemAnnotation;
import net.ooder.esd.custom.properties.NavTabListItem;

@NavFoldingTabsAnnotation()
public class WidgetFoldingTabs extends NavTabListItem {


    @Pid
    WidgetConfig widgetConfig;
    @Pid
    ToolBox.ToolBoxEnum toolBox;

    @TabItemAnnotation(customItems = WidgetEnums.class)
    public WidgetFoldingTabs(WidgetEnums widgetEnums, ToolBox.ToolBoxEnum toolBox, String projectName) {
        this.name = toolBox.name() + "_" + widgetEnums.name();
        this.id = widgetEnums.name();
        this.toolBox = toolBox;
        this.bindClass = widgetEnums.getBindClass();
        this.name = widgetEnums.getName();
        this.widgetConfig = widgetEnums.getWidgets();
        this.imageClass = widgetEnums.getImageClass();
        this.caption = widgetEnums.getName();

    }

    public ToolBox.ToolBoxEnum getToolBox() {
        return toolBox;
    }

    public void setToolBox(ToolBox.ToolBoxEnum toolBox) {
        this.toolBox = toolBox;
    }

    public WidgetConfig getWidgetConfig() {
        return widgetConfig;
    }

    public void setWidgetConfig(WidgetConfig widgetConfig) {
        this.widgetConfig = widgetConfig;
    }

}
