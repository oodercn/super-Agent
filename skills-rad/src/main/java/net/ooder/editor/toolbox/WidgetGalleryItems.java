package net.ooder.editor.toolbox;

import net.ooder.annotation.Pid;
import net.ooder.common.util.StringUtility;
import net.ooder.editor.toolbox.widgets.WidgetConfig;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.GalleryAnnotation;
import net.ooder.esd.annotation.GalleryItemAnnotation;
import net.ooder.esd.annotation.event.GalleryEvent;
import net.ooder.esd.annotation.event.GalleryEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.item.GalleryItem;

@GalleryAnnotation(columns = 2, autoIconColor = true, dock = Dock.fill, iconFontSize = "2em")
@GalleryEvent(eventEnum = GalleryEventEnum.onStartDrag,
        _return = false,
        actions = @CustomAction(script = "SPA._itemOnStartDrag()")
)
public class WidgetGalleryItems extends GalleryItem {

    @Pid
    WidgetConfig widgetConfig;
    @Pid
    String key;

    @GalleryItemAnnotation()
    public WidgetGalleryItems(ComponentType componentType, WidgetConfig widgetConfig) {
        this.imageClass = componentType.getImageClass();
        this.widgetConfig = widgetConfig;
        this.caption = "";
        this.key = componentType.getClassName();
        this.id = StringUtility.replace(key, ".", "_");
        this.comment = componentType.getName();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public WidgetConfig getWidgetConfig() {
        return widgetConfig;
    }

    public void setWidgetConfig(WidgetConfig widgetConfig) {
        this.widgetConfig = widgetConfig;
    }
}
