package net.ooder.editor.menu;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.context.JDSActionContext;
import net.ooder.dsm.editor.menu.server.ServerManagerAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = {"/rad/menu/"})
@MethodChinaName(cname = "菜单")
@Aggregation(type = AggregationType.MENU, rootClass = ProjectTopMenu.class)
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, handler = true, caption = "RAD菜单", id = "ProjectTopMenu")
public class ProjectTopMenu {

    @RequestMapping(method = RequestMethod.POST, value = "file")
    @CustomAnnotation(index = 0)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "文件(F)")
    public FileMenuAction getFileMenuAction() {
        return new FileMenuAction();
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit")
    @CustomAnnotation(index = 1)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "编辑(E)")
    public EditMenuAction getEditMenuAction() {
        return new EditMenuAction();
    }

    @RequestMapping(method = RequestMethod.POST, value = "view")
    @CustomAnnotation(index = 2)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "视图(V)")
    public ViewMenuAction getViewMenuAction() {
        return new ViewMenuAction();
    }

    @RequestMapping(method = RequestMethod.POST, value = "tools")
    @CustomAnnotation(index = 3)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "工具(T)")
    public ToolsMenuAction getToolsMenuAction() {
        return new ToolsMenuAction();
    }

    @RequestMapping(method = RequestMethod.POST, value = "help")
    @CustomAnnotation(index = 4)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "帮助(H)")
    public HelpMenuAction getHelpMenuAction() {
        return new HelpMenuAction();
    }

    @RequestMapping(method = RequestMethod.POST, value = "resourceManager")
    @CustomAnnotation(index = 5)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "资源管理(E)")
    public ServerManagerAction getResourceManager() {
        return new ServerManagerAction();
    }


    @RequestMapping(method = RequestMethod.POST, value = "releasegroup")
    @CustomAnnotation(index = 6)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "服务器(S)")
    public ServerMangerAction getReleaseGroupAction() {
        return new ServerMangerAction();
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }


}