package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.Split;
import net.ooder.annotation.UserSpace;
import net.ooder.config.DSMResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.action.DYNAppendType;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/action/debug/serverconfig/"})
@MenuBarMenu(menuType = CustomMenuType.TOP, caption = "服务配置", index = 6, imageClass = "ri-server-line")
@Aggregation(type = AggregationType.MENU,userSpace = UserSpace.SYS)
public class ServerConfigAction {
    public ServerConfigAction() {

    }

    @RequestMapping(value = {"ESDServerList.dyn"}, method = {RequestMethod.POST})
    @DialogAnnotation( width = "600", height = "380")
    @ModuleAnnotation(caption = "本地配置")
    @DynLoadAnnotation(refClassName = "RAD.server.ESDServerList",append = DYNAppendType.append)
    @CustomAnnotation(index = 0, imageClass = "ri-computer-line")
    @ResponseBody
    public ResultModel<Boolean> getESDServerList(String projectName) {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(value = {"RemoteServerList.dyn"}, method = {RequestMethod.POST})
    @DialogAnnotation( width = "600", height = "380")
    @ModuleAnnotation(caption = "远程配置")
    @DynLoadAnnotation(refClassName = "RAD.server.RemoteServerList",append = DYNAppendType.append)
    @CustomAnnotation(index = 1, imageClass = "ri-cloud-line")
    @ResponseBody
    public DSMResultModel<Boolean> getRemoteServerList(String projectName) {
        DSMResultModel resultModel = new DSMResultModel();
        return resultModel;
    }

    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"ProxyHostList.dyn"}, method = {RequestMethod.POST})
    @DialogAnnotation( width = "600", height = "380")
    @ModuleAnnotation(caption = "代理配置")
    @DynLoadAnnotation(refClassName = "RAD.server.ProxyHostList",append = DYNAppendType.append)
    @CustomAnnotation(index = 3, imageClass = "ri-refresh-line")
    @ResponseBody
    public DSMResultModel<Boolean> getProxyHostList(String projectName) {
        DSMResultModel resultModel = new DSMResultModel();
        return resultModel;
    }

}
