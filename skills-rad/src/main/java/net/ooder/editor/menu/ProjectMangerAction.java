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
@RequestMapping(value = {"/rad/menu/project/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "资源管理(E)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = ProjectMangerAction.class, userSpace = UserSpace.SYS)
public class ProjectMangerAction {

    @MethodChinaName(cname = "新建工程")
    @RequestMapping(value = {"newProject"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0)
    @ResponseBody
    public ResultModel<Boolean> newProject() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "打开工程")
    @RequestMapping(value = {"openProject"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1)
    @ResponseBody
    public ResultModel<Boolean> openProject() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "皮肤样式")
    @RequestMapping(value = {"theme"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 2, caption = "皮肤样式", imageClass = "ri-palette-line", tips = "$RAD.builder.dftThemeTips")
    @ResponseBody
    public ResultModel<Boolean> theme() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "切换语言")
    @RequestMapping(value = {"ec"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 3, caption = "切换语言")
    @ResponseBody
    public ResultModel<Boolean> ec() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

}
