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
@RequestMapping(value = {"/rad/menu/server/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "资源管理(E)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = ServerMangerAction.class, userSpace = UserSpace.SYS)
public class ServerMangerAction {

    @MethodChinaName(cname = "服务器")
    @RequestMapping(value = {"server"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0, caption = "服务器", imageClass = "ri-server-line")
    @ResponseBody
    public ResultModel<Boolean> server() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split1"})
    @Split
    @CustomAnnotation(index = 1)
    @ResponseBody
    public ResultModel<Boolean> split1() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "远程配置")
    @RequestMapping(value = {"RemoteServerList"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 2, caption = "远程配置", imageClass = "ri-plug-line")
    @ResponseBody
    public ResultModel<Boolean> RemoteServerList() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "代理配置")
    @RequestMapping(value = {"ProxyHostList"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 3, caption = "代理配置", imageClass = "ri-server-line")
    @ResponseBody
    public ResultModel<Boolean> ProxyHostList() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split2"})
    @Split
    @CustomAnnotation(index = 4)
    @ResponseBody
    public ResultModel<Boolean> split2() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "启动服务")
    @RequestMapping(value = {"startServer"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 5, caption = "启动服务", imageClass = "ri-play-line", tips = "$(RAD.esdmenu.startSrver)")
    @ResponseBody
    public ResultModel<Boolean> startServer() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "关闭服务")
    @RequestMapping(value = {"stopServer"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 6, caption = "关闭服务", imageClass = "ri-stop-line", tips = "$(RAD.esdmenu.stopServer)")
    @ResponseBody
    public ResultModel<Boolean> stopServer() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split3"})
    @Split
    @CustomAnnotation(index = 7)
    @ResponseBody
    public ResultModel<Boolean> split3() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "服务配置")
    @RequestMapping(value = {"ESDServerList"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 8, caption = "服务配置", imageClass = "ri-cpu-line")
    @ResponseBody
    public ResultModel<Boolean> ESDServerList() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

}
