package net.ooder.editor.toolbox;

import net.ooder.config.ListResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.view.ButtonViewsViewAnnotation;
import net.ooder.esd.util.page.ButtonViewPageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rad/toolbox/")
public class ToolBoxService {


    @RequestMapping(method = RequestMethod.POST, value = "ToolBox")
    @APIEventAnnotation(autoRun = true, bindMenu = CustomMenuItem.INDEX)
    @ModuleAnnotation(caption = "ToolBox", dynLoad = true)
    @UIAnnotation(dock = Dock.fill)
    @ButtonViewsViewAnnotation()
    @ResponseBody
    public ListResultModel<List<ToolBox>> getToolBox(String projectName) {
        ListResultModel<List<ToolBox>> result = new ListResultModel<List<ToolBox>>();
        result = ButtonViewPageUtil.getTabList(Arrays.asList(ToolBox.ToolBoxEnum.values()), ToolBox.class);
        return result;
    }

}
