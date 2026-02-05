package net.ooder.editor;

import net.ooder.config.ListResultModel;
import net.ooder.editor.console.MainStatusToolBar;
import net.ooder.editor.menu.ProjectTopMenu;
import net.ooder.editor.toolbox.ToolBox;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.annotation.view.ButtonViewsViewAnnotation;
import net.ooder.esd.annotation.view.LayoutViewAnnotation;
import net.ooder.esd.util.page.ButtonViewPageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@RequestMapping("/rad/")
@Controller
@MenuBarMenu(menuClasses = ProjectTopMenu.class, menuType = CustomMenuType.MENUBAR)
@ToolBarMenu(menuClasses = MainStatusToolBar.class, dock = Dock.bottom, autoIconColor = false, iconFontSize = "1em")
@LayoutAnnotation(transparent = false, layoutType = LayoutType.horizontal)
public class FramePanel {


    @RequestMapping(method = RequestMethod.POST, value = "ToolBox")
    @APIEventAnnotation()
    @ModuleAnnotation(caption = "ToolBox")
    @UIAnnotation(dock = Dock.fill)
    @LayoutItemAnnotation(pos = PosType.before, size = 230, max = 1000)
    @ButtonViewsViewAnnotation()
    @ResponseBody
    public ListResultModel<List<ToolBox>> getToolBox(String projectName) {
        ListResultModel<List<ToolBox>> result = new ListResultModel<List<ToolBox>>();
        result = ButtonViewPageUtil.getTabList(Arrays.asList(ToolBox.ToolBoxEnum.values()), ToolBox.class);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "MainTabs")
    @LayoutViewAnnotation
    @LayoutItemAnnotation(pos = PosType.main)
    @ModuleAnnotation
    @ResponseBody
    public ListResultModel<List<RADMainPanel>> getMainTabs(String projectName) {
        ListResultModel<List<RADMainPanel>> result = new ListResultModel<List<RADMainPanel>>();
        return result;
    }


}
