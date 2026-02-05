package net.ooder.editor.console.ddd.esdclass;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.BtnAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.PanelAnnotation;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.PanelType;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rad/file/esdclass/")
public class ESDClassTreeService {

    @RequestMapping(method = RequestMethod.POST, value = "loadChild")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> loadChild(String sourceClassName, String domainId, String currentClassName, String xpath) {
        TreeListResultModel<List<OODFileTree>> resultModel = new TreeListResultModel<List<OODFileTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(DDDModuleNavItem.values()), OODFileTree.class);
        return resultModel;
    }

    @MethodChinaName(cname = "聚合实体信息")
    @RequestMapping(method = RequestMethod.POST, value = "AggEntityMetaView")
    @TabsViewAnnotation
    @TreeEvent(eventEnum = TreeViewEventEnum.onClick, pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @PanelAnnotation(dock = Dock.fill)
    @ModuleAnnotation(panelType = PanelType.panel)
    @BtnAnnotation(closeBtn = false)
    @ResponseBody
    public ResultModel<DddEntityMetaView> getAggEntityMetaView(String projectName, String domainId, String sourceClassName, String sourceMethodName, String currentClassName, String xpath) {
        ResultModel<DddEntityMetaView> result = new ResultModel<DddEntityMetaView>();
        return result;
    }


}
