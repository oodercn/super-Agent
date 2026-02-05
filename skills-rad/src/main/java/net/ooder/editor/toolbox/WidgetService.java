package net.ooder.editor.toolbox;

import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.NavFoldingTabsViewAnnotation;
import net.ooder.esd.util.page.TabPageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/rad/toolbox/")
@Controller
public class WidgetService {

    @APIEventAnnotation(bindTabsEvent = {CustomTabsEvent.TABCHILD, CustomTabsEvent.TABEDITOR})
    @RequestMapping("WidgetTab")
    @NavFoldingTabsViewAnnotation
    @ModuleAnnotation(dynLoad = true)
    @ResponseBody
    public TreeListResultModel<List<WidgetFoldingTabs>> getWidgetTab(ToolBox.ToolBoxEnum toolBox) {
        TreeListResultModel<List<WidgetFoldingTabs>> resultModel = new TreeListResultModel();
        if (toolBox != null) {
            resultModel = TabPageUtil.getTabList(Arrays.asList(toolBox.getWidgetEnums()), WidgetFoldingTabs.class);
        }
        return resultModel;
    }

}
