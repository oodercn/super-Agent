package net.ooder.editor.console.java.esd;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.debug.bean.ConfigModuleTree;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.tool.component.ModuleComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = {"/rad/file/java/"})
@Aggregation(type = AggregationType.MENU, rootClass = JavaToolBar.class, userSpace = UserSpace.SYS)
public class JavaToolBar {


    @MethodChinaName(cname = "刷新工程文件")
    @RequestMapping(method = RequestMethod.POST, value = "reLoad")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 1, tips = "刷新工程文件")
    @APIEventAnnotation(
            afterInvoke = {CustomCallBack.PAGERELOAD}
    )
    public @ResponseBody
    TreeListResultModel<List<ConfigModuleTree>> reLoad() {
        TreeListResultModel<List<ConfigModuleTree>> result = new TreeListResultModel<List<ConfigModuleTree>>();
        return result;
    }


    @MethodChinaName(cname = "编译视图")
    @RequestMapping(value = {"buildView"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = {CustomCallBack.FREE, CustomCallBack.RELOAD},
            afterInvokeAction = @CustomAction(script = "SPA.reOpenCls()", params = {"args[1].data"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )


    @CustomAnnotation(index = 1, imageClass = "ri-refresh-line")
    @ResponseBody
    public ResultModel<OODFileTree> buildView(String currentClassName, String domainId, String projectName) {
        ResultModel<OODFileTree> resultModel = new ResultModel<>();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = null;
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            if (domainId == null) {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            } else {
                domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            }
            EUModule module = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);

            if (module != null) {
                List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                for (ModuleComponent moduleComponent : moduleComponents) {
                    if (moduleComponent.getEuModule() != null) {
                        ESDFacrory.getAdminESDClient().delModule(moduleComponent.getEuModule());
                    }
                }
                ESDFacrory.getAdminESDClient().delModule(module);
            }

            CustomViewFactory.getInstance().clear();

            MethodConfig methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(currentClassName, projectName);

            if (methodConfig != null) {
                CustomViewFactory.getInstance().buildView(methodConfig, projectName, (Map<String, ?>) JDSActionContext.getActionContext().getContext(), false);
            }
            resultModel.setData(new OODFileTree(module, projectName, domainInst.getDomainId()));
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    @MethodChinaName(cname = "清空领域配置")
    @RequestMapping(method = RequestMethod.POST, value = "reSet")
    @CustomAnnotation(imageClass = "ri-git-branch-line", index = 2)
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = {CustomCallBack.FREE},
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    public @ResponseBody
    TreeListResultModel<List<OODFileTree>> reSet(String currentClassName, String domainId, String projectName) {
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<List<OODFileTree>>();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            EUModule module = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            EUPackage euPackage = ESDFacrory.getAdminESDClient().getPackageByPath(projectName, module.getPackageName());
            if (euPackage != null) {
                result.setIds(Arrays.asList(euPackage.getId()));
            }

            DSMFactory.getInstance().getAggregationManager().clearModuleConfig(currentClassName, projectName);

        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
        }
        return result;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }

}
