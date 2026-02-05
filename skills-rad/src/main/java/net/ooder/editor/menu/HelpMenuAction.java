package net.ooder.editor.menu;


import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/rad/menu/help/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "帮助(H)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = EditMenuAction.class, userSpace = UserSpace.SYS)
public class HelpMenuAction {

    @MethodChinaName(cname = "文档")
    @RequestMapping(value = {"documentation"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0, caption = "文档", imageClass = "ri-book-line", tips = "查看帮助文档")
    @ResponseBody
    public ResultModel<Boolean> documentation() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "关于")
    @RequestMapping(value = {"about"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1, caption = "关于", imageClass = "ri-information-line", tips = "关于CodeBuddy CN")
    @ResponseBody
    public ResultModel<Boolean> about() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }
}
    