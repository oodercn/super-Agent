package net.ooder.editor.menu;

import net.ooder.annotation.*;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/rad/menu/resource/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "资源管理(E)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = ResourceMangerAction.class, userSpace = UserSpace.SYS)
public class ResourceMangerAction {


    @MethodChinaName(cname = "图标字体")
    @RequestMapping(value = {"csstool"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_CLASSNAME, RequestPathEnum.RAD_JSON}, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @CustomAnnotation(index = 0, caption = "图标字体")
    @ResponseBody
    public ResultModel<Boolean> csstool(String projectName, String currentClassName, String json) {
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

    @MethodChinaName(cname = "导入图标")
    @RequestMapping(value = {"localicon"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 2, caption = "导入图标", imageClass = "ri-file-code-line")
    @ResponseBody
    public ResultModel<Boolean> localicon() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "阿里站点")
    @RequestMapping(value = {"iconfont.cn"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 3, caption = "阿里站点", imageClass = "ri-video-line")
    @ResponseBody
    public ResultModel<Boolean> iconfont() {
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

    @MethodChinaName(cname = "图片库")
    @RequestMapping(value = {"imgtool"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 5, caption = "图片库", imageClass = "ri-image-line")
    @ResponseBody
    public ResultModel<Boolean> imgtool() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split3"})
    @Split
    @CustomAnnotation(index = 6)
    @ResponseBody
    public ResultModel<Boolean> split3() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "图片维护")
    @RequestMapping(value = {"imgmanager"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 7, caption = "图片维护", imageClass = "ri-brush-line")
    @ResponseBody
    public ResultModel<Boolean> imgmanager() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "导入图片")
    @RequestMapping(value = {"imgimport"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 8, caption = "导入图片", imageClass = "ri-file-code-line")
    @ResponseBody
    public ResultModel<Boolean> imgimport() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split4"})
    @Split
    @CustomAnnotation(index = 9)
    @ResponseBody
    public ResultModel<Boolean> split4() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "服务导入")
    @RequestMapping(value = {"serviceImport"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 10, caption = "服务导入")
    @ResponseBody
    public ResultModel<Boolean> serviceImport() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "服务维护")
    @RequestMapping(value = {"serviceManager"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 11, caption = "服务维护")
    @ResponseBody
    public ResultModel<Boolean> serviceManager() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split5"})
    @Split
    @CustomAnnotation(index = 12)
    @ResponseBody
    public ResultModel<Boolean> split5() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "数据库")
    @RequestMapping(value = {"mangager"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 13, caption = "数据库", imageClass = "ri-database-2-line")
    @ResponseBody
    public ResultModel<Boolean> mangager() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split6"})
    @Split
    @CustomAnnotation(index = 14)
    @ResponseBody
    public ResultModel<Boolean> split6() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "数据源")
    @RequestMapping(value = {"DBProviderList"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 15, caption = "数据源", imageClass = "ri-database-2-line")
    @ResponseBody
    public ResultModel<Boolean> DBProviderList() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "导入库表")
    @RequestMapping(value = {"importTable"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 16, caption = "导入库表", imageClass = "ri-upload-line")
    @ResponseBody
    public ResultModel<Boolean> importTable() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "数据库监控")
    @RequestMapping(value = {"SqlConsole"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 17, caption = "数据库监控", imageClass = "ri-bar-chart-line")
    @ResponseBody
    public ResultModel<Boolean> SqlConsole() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


}
