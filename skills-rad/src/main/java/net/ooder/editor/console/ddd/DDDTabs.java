package net.ooder.editor.console.ddd;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.BarLocationType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.view.BlockViewAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@ButtonViewsAnnotation(barLocation = BarLocationType.bottom, barSize = "1.5em")
@RequestMapping("/rad/ddd/tabs/")
@Controller
public class DDDTabs {


    @MethodChinaName(cname = "DDD配置")
    @RequestMapping(method = RequestMethod.POST, value = "APIMethodInfo")
    @ModuleAnnotation()
    @BlockViewAnnotation
    @UIAnnotation(dock = Dock.fill)
    @APIEventAnnotation(bindTabsEvent = CustomTabsEvent.TABEDITOR)
    @ResponseBody
    public ResultModel<DDDAPIMethodInfo> getAPIMethodInfo() {
        ResultModel<DDDAPIMethodInfo> result = new ResultModel<>();
        DDDAPIMethodInfo editor = new DDDAPIMethodInfo();
        result.setData(editor);
        return result;
    }
}
