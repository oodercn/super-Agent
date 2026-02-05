package net.ooder.editor.menu;

import net.ooder.annotation.*;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/rad/menu/tools/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "工具(T)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = EditMenuAction.class, userSpace = UserSpace.SYS)
public class ToolsMenuAction {

    @MethodChinaName(cname = "构建项目")
    @RequestMapping(value = {"build"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0, caption = "构建项目", imageClass = "ri-hammer-line", tips = "构建当前项目")
    @ResponseBody
    public ResultModel<Boolean> build() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "运行项目")
    @RequestMapping(value = {"run"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1, caption = "运行项目", imageClass = "ri-play-line", tips = "运行当前项目")
    @ResponseBody
    public ResultModel<Boolean> run() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split1"})
    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split1() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "调试")
    @RequestMapping(value = {"debug"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 3, caption = "调试", imageClass = "ri-bug-line", tips = "启动调试模式")
    @ResponseBody
    public ResultModel<Boolean> debug() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "测试")
    @RequestMapping(value = {"test"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 4, caption = "测试", imageClass = "ri-flask-line", tips = "运行测试")
    @ResponseBody
    public ResultModel<Boolean> test() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }
}
    