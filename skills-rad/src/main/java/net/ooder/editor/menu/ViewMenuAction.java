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
@RequestMapping(value = {"/rad/menu/view/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "视图(V)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = EditMenuAction.class, userSpace = UserSpace.SYS)
public class ViewMenuAction {

    @MethodChinaName(cname = "资源管理器")
    @RequestMapping(value = {"explorer"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0, caption = "资源管理器", imageClass = "ri-folder-line", tips = "显示/隐藏资源管理器")
    @ResponseBody
    public ResultModel<Boolean> explorer() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "终端")
    @RequestMapping(value = {"terminal"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1, caption = "终端", imageClass = "ri-terminal-line", tips = "显示/隐藏终端")
    @ResponseBody
    public ResultModel<Boolean> terminal() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "问题面板")
    @RequestMapping(value = {"problems"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 2, caption = "问题面板", imageClass = "ri-error-warning-line", tips = "显示/隐藏问题面板")
    @ResponseBody
    public ResultModel<Boolean> problems() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split1"})
    @Split
    @CustomAnnotation(index = 3)
    @ResponseBody
    public ResultModel<Boolean> split1() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "放大")
    @RequestMapping(value = {"zoomIn"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 4, caption = "放大", imageClass = "ri-zoom-in-line", tips = "放大视图")
    @ResponseBody
    public ResultModel<Boolean> zoomIn() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "缩小")
    @RequestMapping(value = {"zoomOut"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 5, caption = "缩小", imageClass = "ri-zoom-out-line", tips = "缩小视图")
    @ResponseBody
    public ResultModel<Boolean> zoomOut() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "重置缩放")
    @RequestMapping(value = {"resetZoom"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 6, caption = "重置缩放", imageClass = "ri-zoom-in-line", tips = "重置视图缩放")
    @ResponseBody
    public ResultModel<Boolean> resetZoom() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }
}
    
