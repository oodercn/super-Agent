package net.ooder.editor;

import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceTree;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.action.CustomTreeAction;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.PanelType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.annotation.view.PanelViewAnnotation;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@RequestMapping("/rad/file/")
@Controller
@LayoutAnnotation(transparent = false, layoutType = LayoutType.vertical)
public class RADMainPanel {


    public RADMainPanel() {

    }

    @RequestMapping(method = RequestMethod.POST, value = "Designer")
    @LayoutItemAnnotation(pos = PosType.main)
    @ModuleAnnotation
    @PanelViewAnnotation()
    @ResponseBody
    public ModuleComponent getModuleComponent() {
        ModuleComponent moduleComponent = new ModuleComponent("RAD.OODDesigner");
        moduleComponent.setClassName("RAD.OODDesigner");
        return moduleComponent;
    }


    @RequestMapping(method = RequestMethod.POST, value = "JavaSoruceTree")
    @ModuleAnnotation(caption = "JAVA源码", panelType = PanelType.panel, imageClass = "ri-mouse-line")
    @NavTreeViewAnnotation
    @LayoutItemAnnotation(pos = PosType.after, max = 1000, size = 160)
    @PanelAnnotation(dock = Dock.fill)
    @BtnAnnotation(closeBtn = false)
    @TreeEvent(eventEnum = TreeViewEventEnum.onRender, customActions = CustomTreeAction.RELOAD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> getJavaSoruceTree() {
        String projectVersionName = null;
        try {
            projectVersionName = DSMFactory.getInstance().getDefaultProjectName();

        } catch (JDSException e) {
            e.printStackTrace();
        }
        TreeListResultModel<List<JavaSourceTree>> result = TreePageUtil.getTreeList(Arrays.asList(projectVersionName), JavaSourceTree.class);
        return result;
    }


}
