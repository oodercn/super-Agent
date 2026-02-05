package net.ooder.editor.toolbox;

import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.editor.toolbox.widgets.WidgetConfig;
import net.ooder.esd.annotation.ContainerAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomGalleryEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.GalleryViewAnnotation;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.tool.properties.CS;
import net.ooder.esd.util.page.GalleryPageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/rad/toolbox/widget/")
@Controller
public class WidgetGalleryService {

    @APIEventAnnotation(bindGalleryEvent = CustomGalleryEvent.RELOAD, bindTabsEvent = CustomTabsEvent.TABEDITOR)
    @RequestMapping("Widget")
    @GalleryViewAnnotation
    @ContainerAnnotation(dragKey = "___iDesign")
    @ResponseBody
    public ListResultModel<List<WidgetGalleryItems>> getWidget(WidgetConfig widgetConfig) {
        ListResultModel<List<WidgetGalleryItems>> widgetItems = new ListResultModel<>();
        if (widgetConfig != null) {
            widgetItems = GalleryPageUtil.getGalleryList(Arrays.asList(widgetConfig.getComponentTypes()), WidgetGalleryItems.class, false);
        }
        return widgetItems;
    }

    @RequestMapping("loadCS")
    @APIEventAnnotation(bindFieldEvent = {CustomFieldEvent.LOADCS})
    public CS getCs() {
        CS cs = new CS();
        HashMap flagMap = new HashMap();
        flagMap.put("margin", "0.125em");
        cs.setCOMMENT(flagMap);
        HashMap iconMap = new HashMap();
        iconMap.put("background-color", "transparent");
        iconMap.put("border-top", "dashed 1px #ababab");
        iconMap.put("border-right", "dashed 1px #ababab");
        iconMap.put("border-bottom", "dashed 1px #ababab");
        iconMap.put("border-left", "dashed 1px #ababab");
        iconMap.put("margin", "1px 1px 1px 1px");
        cs.setITEMFRAME(iconMap);
        return cs;
    }


    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
