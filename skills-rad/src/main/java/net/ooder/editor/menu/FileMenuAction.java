package net.ooder.editor.menu;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.EUFileType;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/rad/menu/file/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "文件(F)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = FileMenuAction.class, userSpace = UserSpace.SYS)
public class FileMenuAction {

    @MethodChinaName(cname = "保存")
    @RequestMapping(value = {"save"}, method = {RequestMethod.POST})
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "save", script = "SPA.save()", params = {"{args[0]}", "{args[1]}", "{args[2]}"})
    )
    @CustomAnnotation(index = 0, caption = "保存", imageClass = "ri-save-line")
    @ResponseBody
    public ResultModel<Boolean> saveContent(String projectName, String className, String content, String jscontent, String path, EUFileType fileType) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(DSMFactory.getInstance().getDefaultProjectName());
            if (path != null && path.endsWith(".cls")) {
                fileType = EUFileType.EUClass;
            }
            switch (fileType) {
                case EUClass:
                    if (path.endsWith(".js") && jscontent != null) {
                        this.getClient().saveFile(new StringBuffer(jscontent), path, version.getVersionName());
                    }
                    if (className != null && !className.equals("")) {
                        EUModule euModule = version.getModule(className);
                        if (euModule == null) {
                            euModule = version.createModule(path);
                        }
                        ModuleComponent omoduleComponent = euModule.getComponent();
                        ModuleComponent moduleComponent = JSONObject.parseObject(content, ModuleComponent.class);
                        if (omoduleComponent != null && omoduleComponent.getFormulas() != null) {
                            moduleComponent.setFormulas(omoduleComponent.getFormulas());
                        }
                        euModule.setComponent(moduleComponent);
                        Component currComponent = moduleComponent.getCurrComponent();
                        Component mainBoxComponent = moduleComponent.getMainBoxComponent();
                        moduleComponent.moveComponent(mainBoxComponent, moduleComponent.getChildren().toArray(new Component[]{}));
                        moduleComponent.setCurrComponent(currComponent);
                        euModule.update(true);
                    }
                    break;
                case EUFile:
                    this.getClient().saveFile(new StringBuffer(jscontent), path, projectName);
                    break;
            }
            result.setData(true);
        } catch (Exception e) {
            e.printStackTrace();
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }

    @MethodChinaName(cname = "保存所有")
    @RequestMapping(value = {"saveall"}, method = {RequestMethod.POST})
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "saveAll", script = "SPA.saveAll()", params = {"{args[0]}", "{args[1]}", "{args[2]}"})
    )
    @CustomAnnotation(index = 1, caption = "保存所有", imageClass = "ri-save-fill")
    @ResponseBody
    public ResultModel<Boolean> saveall(String projectName, String className, String content, String jscontent, String path, EUFileType fileType) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        this.saveContent(projectName, className, content, jscontent, path, fileType);
        return result;
    }

    @RequestMapping(value = {"split1"})
    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split1() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "关闭所有")
    @RequestMapping(value = {"closeall"}, method = {RequestMethod.POST})
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "closeall", script = "SPA.closeall()")
    )
    @CustomAnnotation(index = 3, caption = "关闭所有", imageClass = "ri-close-circle-line", tips = "关闭所有l")
    @ResponseBody
    public ResultModel<Boolean> closeall(String projectName) {
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


    @MethodChinaName(cname = "工程配置")
    @RequestMapping(value = {"setproject"}, method = {RequestMethod.POST})
    @MenuBarMenu(menuType = CustomMenuType.SUB)
    @CustomAnnotation(index = 5)
    @ResponseBody
    public ProjectMangerAction getProjectConfigAction() {
        return new ProjectMangerAction();
    }


    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }
}
