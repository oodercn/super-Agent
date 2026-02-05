package net.ooder.editor.console.ddd.esdclass;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ResultModel;
import net.ooder.dsm.view.config.base.BaseViewService;
import net.ooder.editor.console.ddd.DDDAPIMethodInfo;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rad/ddd/esdclass/")
public class DDDViewInfoService extends BaseViewService {




    @MethodChinaName(cname = "方法聚合")
    @RequestMapping(method = RequestMethod.POST, value = "DDDAPIViewInfo")
    @TabsViewAnnotation
    @TreeEvent(eventEnum = TreeViewEventEnum.onClick, pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<DDDAPIMethodInfo> getDDDAPIViewInfo(String projectName, String domainId, String sourceClassName, String sourceMethodName, String currentClassName, String xpath) {
        ResultModel<DDDAPIMethodInfo> result = new ResultModel<DDDAPIMethodInfo>();
        return result;
    }

}
