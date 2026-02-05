package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.Split;
import net.ooder.annotation.UserSpace;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.DSMResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.action.DYNAppendType;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/action/resource/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "资源配置", index = 3)
@Aggregation(type = AggregationType.MENU,userSpace = UserSpace.SYS)
public class ResourcesAction {


    @RequestMapping(value = {"FontList.dyn"}, method = {RequestMethod.POST})
    @ModuleAnnotation(caption = "引入字体", imageClass = "ri-font-size-2")
    @DynLoadAnnotation(refClassName = "RAD.resource.FontList",append = DYNAppendType.append)
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @CustomAnnotation(index = 0)
    @ResponseBody
    @DialogAnnotation(width = "800", height = "550")
    public ResultModel<Boolean> getFontList(String projectManager) {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(value = {"ImageConfigList.dyn"}, method = {RequestMethod.POST})
    @DialogAnnotation(width = "800", height = "550")
    @ModuleAnnotation(caption = "导入图片", imageClass = "ri-image-line")
    @DynLoadAnnotation(refClassName = "RAD.resource.ImageConfigList",append = DYNAppendType.append)
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @CustomAnnotation(index = 3)
    @ResponseBody
    public DSMResultModel<Boolean> getImageConfigList(String projectManager) {
        DSMResultModel resultModel = new DSMResultModel();
        return resultModel;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome  = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }



}
