package net.ooder.editor.console.ddd.esdclass;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
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

import java.util.*;


@Controller
@RequestMapping(value = {"/rad/rad/esdclass/context/"})
@Aggregation(type = AggregationType.MENU, rootClass = EsdClassContextMenu.class)
@MenuBarMenu(menuType = CustomMenuType.CONTEXTMENU, caption = "菜单")
public class EsdClassContextMenu {


    @MethodChinaName(cname = "编译")
    @RequestMapping(method = RequestMethod.POST, value = "dsmBuild")
    @CustomAnnotation(imageClass = "ri-settings-3-line", index = 1)
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    public @ResponseBody
    TreeListResultModel<List<OODFileTree>> dsmBuild(String id, String projectName, String parentId, String packageName, String sourceClassName) {
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<List<OODFileTree>>();
        try {
            Map<String, ?> valueMap = JDSActionContext.getActionContext().getContext();
            result.setIds(Arrays.asList(new String[]{parentId}));
            if (sourceClassName != null) {
                ESDFacrory.getAdminESDClient().rebuildCustomModule(sourceClassName, projectName, valueMap);
            } else if (packageName != null) {
                ESDFacrory.getAdminESDClient().buildPackage(projectName, packageName, valueMap, this.getCurrChromeDriver());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = {"buildView"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @CustomAnnotation(index = 1, caption = "编译视图", imageClass = "ri-refresh-line")
    @ResponseBody
    public ResultModel<Boolean> buildView(String id, String projectName, String domainId, String parentId, String packageName, String sourceClassName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ApiClassConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            List<MethodConfig> allViewConfigs = esdClassConfig.getAllMethods();
            for (MethodConfig methodAPIBean : allViewConfigs) {
                if (methodAPIBean.isModule()) {
                    EUModule module = ESDFacrory.getAdminESDClient().getModule(methodAPIBean.getUrl(), projectName);
                    if (module != null) {
                        List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                        for (ModuleComponent moduleComponent : moduleComponents) {
                            if (moduleComponent.getEuModule() != null) {
                                ESDFacrory.getAdminESDClient().delModule(moduleComponent.getEuModule());
                            }
                        }
                        ESDFacrory.getAdminESDClient().delModule(module);
                    }
                    CustomViewFactory.getInstance().buildView(methodAPIBean, projectName, (Map<String, ?>) JDSActionContext.getActionContext().getContext(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            resultModel = new ErrorResultModel();

            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    @MethodChinaName(cname = "重置")
    @RequestMapping(method = RequestMethod.POST, value = "reSet")
    @CustomAnnotation(imageClass = "ri-close-line", index = 2)
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<OODFileTree>> reSet(String id, String projectName, String domainId, String sourceClassName) {
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<List<OODFileTree>>();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            List<MethodConfig> allViewConfigs = new ArrayList<>();
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            allViewConfigs.addAll(apiClassConfig.getAllMethods());
            List<MethodConfig> configs = apiClassConfig.getAllProxyMethods();
            Set<String> esdClassNames = new HashSet<>();
            esdClassNames.add(sourceClassName);
            for (MethodConfig methodConfig : configs) {
                if (methodConfig.getViewClassName() != null) {
                    esdClassNames.add(methodConfig.getViewClassName());
                }
            }
            DSMFactory.getInstance().getAggregationManager().delAggEntity(domainId, esdClassNames, projectName, true);
            for (MethodConfig methodAPIBean : allViewConfigs) {
                if (methodAPIBean.isModule()) {
                    EUModule module = ESDFacrory.getAdminESDClient().getModule(methodAPIBean.getUrl(), domainInst.getProjectVersionName());
                    if (module != null) {
                        List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                        for (ModuleComponent moduleComponent : moduleComponents) {
                            if (moduleComponent.getEuModule() != null) {
                                ESDFacrory.getAdminESDClient().delModule(moduleComponent.getEuModule());
                            }

                        }
                        ESDFacrory.getAdminESDClient().delModule(module);
                    }
                    CustomViewFactory.getInstance().buildView(methodAPIBean, domainInst.getProjectVersionName(), (Map<String, ?>) JDSActionContext.getActionContext().getContext(), false);
                }
            }
            EUPackage euPackage = ESDFacrory.getAdminESDClient().getPackageByPath(projectName, apiClassConfig.getUrl());
            if (euPackage != null) {
                result.setIds(Arrays.asList(euPackage.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
        }
        return result;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }

}
