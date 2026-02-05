package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.Split;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping(value = {"/action/agg/build/"})
@MenuBarMenu(menuType = CustomMenuType.TOP, caption = "领域应用", index = 2, imageClass = "ri-cube-line")
@Aggregation(type = AggregationType.MENU, userSpace = UserSpace.SYS)
public class AggTopBuildAction {
    public AggTopBuildAction() {

    }


    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(method = RequestMethod.POST, value = "buildAgg")
    @CustomAnnotation(index = 0, caption = "编译聚合", imageClass = "ri-add-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.SPA_CLASSNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = CustomCallBack.FREE,
            afterInvokeAction = @CustomAction(name = "reloadPage", method = "call", type = ActionTypeEnum.other, target = "callback", args = {"{SPA.reloadPage()}"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    @ResponseBody
    public ResultModel<Boolean> buildAgg(String currentClassName, String projectName) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            ModuleComponent moduleComponent = euModule.getComponent();
            MethodConfig methodConfig = null;
            CustomViewBean viewBean = null;
            try {
                methodConfig = moduleComponent.getMethodAPIBean();
                if (methodConfig == null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                }
                if (methodConfig != null) {
                    viewBean = methodConfig.getView();
                    viewBean.updateModule(euModule.getComponent());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (methodConfig == null) {
                viewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(moduleComponent, domainInst.getDomainId());
            }

            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euModule.getClassName(), projectName);
            aggRootBuild.build();
            if (methodConfig == null) {
                methodConfig = moduleComponent.getMethodAPIBean();
            }
            euModule.getComponent().getProperties().setDsmProperties(new DSMProperties(methodConfig, projectName));
            euModule.update(false);
        } catch (JDSException e) {
            e.printStackTrace();
        }


        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "clearAgg")
    @CustomAnnotation(index = 0, caption = "清空聚合", imageClass = "ri-refresh-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.SPA_CLASSNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = CustomCallBack.FREE,
            afterInvokeAction = @CustomAction(name = "reloadPage", method = "call", type = ActionTypeEnum.other, target = "callback", args = {"{SPA.reloadPage()}"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    @ResponseBody
    public ResultModel<Boolean> clearAgg(String currentClassName, String projectName) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            ModuleComponent moduleComponent = euModule.getComponent();
            moduleComponent.reSet();

            if (moduleComponent.getProperties().getDsmProperties() != null) {
                String soruceClass = moduleComponent.getProperties().getDsmProperties().getSourceClassName();
                if (soruceClass == null && moduleComponent.getModuleBean() != null) {
                    soruceClass = moduleComponent.getModuleBean().getSourceClassName();
                }
                DSMFactory.getInstance().getAggregationManager().deleteApiClassConfig(soruceClass);
            }
            DSMFactory.getInstance().getAggregationManager().deleteApiClassConfig(moduleComponent.getClassName());
            CustomViewBean viewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(moduleComponent, domainInst.getDomainId());
            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euModule.getClassName(), projectName);

            aggRootBuild.build();
            MethodConfig methodConfig = moduleComponent.getMethodAPIBean();
            if (methodConfig != null) {
                euModule.getComponent().getProperties().setDsmProperties(new DSMProperties(methodConfig, projectName));
                euModule.update(false);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


        return result;
    }


    @RequestMapping(value = {"reBuildEntity"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.SPA_CLASSNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = CustomCallBack.FREE,
            afterInvokeAction = @CustomAction(name = "reloadPage", method = "call", type = ActionTypeEnum.other, target = "callback", args = {"{SPA.reloadPage()}"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    @CustomAnnotation(index = 1, caption = "编译实体", imageClass = "ri-refresh-line")
    @ResponseBody
    public ResultModel<Boolean> reBuildEntity(String projectName, String currentClassName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            ModuleComponent moduleComponent = euModule.getComponent();
            MethodConfig methodConfig = null;
            try {
                methodConfig = moduleComponent.getMethodAPIBean();
                if (methodConfig == null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            EUModule module = ESDFacrory.getAdminESDClient().getModule(methodConfig.getUrl(), domainInst.getProjectVersionName());
            if (module != null) {
                if (euModule != null) {
                    Component component = euModule.getComponent().findComponentByFieldName(OODUtil.formatJavaName(methodConfig.getView().getName(), true));
                    if (component == null) {
                        component = euModule.getComponent().findComponentByFieldName(OODUtil.formatJavaName(methodConfig.getName(), true));
                    }
                    if (component != null && !(component instanceof ModuleComponent)) {
                        Component currComponent = module.getComponent().getCurrComponent().clone();
                        currComponent.setEvents(component.getEvents());
                        Component parent = component.getParent();
                        parent.removeChildren(component);
                        parent.addChildren(currComponent);
                        euModule.update(true);
                    }

                }
                List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                for (ModuleComponent childModule : moduleComponents) {
                    if (childModule.getEuModule() != null) {
                        ESDFacrory.getAdminESDClient().delModule(childModule.getEuModule());
                    }
                }
                ESDFacrory.getAdminESDClient().delModule(module);
            }
            JDSActionContext.getActionContext().getContext().put(CustomViewFactory.TopModuleKey, null);
            JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, null);
            CustomViewFactory.getInstance().buildView(methodConfig, domainInst.getProjectVersionName(), (Map<String, ?>) JDSActionContext.getActionContext().getContext(), false);
            CustomViewFactory.getInstance().reLoad();
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }

    @RequestMapping(value = {"reBuildService"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.CTX},
            beforeInvoke = CustomBeforInvoke.BUSY,
            callback = CustomCallBack.FREE
    )
    @CustomAnnotation(index = 3, caption = "重新编译", imageClass = "ri-refresh-line")
    @ResponseBody
    public ResultModel<Boolean> reBuildService(String projectName, String currentClassName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            ModuleComponent moduleComponent = euModule.getComponent();
            MethodConfig methodConfig = null;
            try {
                methodConfig = moduleComponent.getMethodAPIBean();
                if (methodConfig == null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Set<String> esdClassNames = new HashSet<>();
            esdClassNames.add(methodConfig.getSourceClassName());
            Set<Class> classes = methodConfig.getOtherClass();
            for (Class clazz : classes) {
                if (clazz != null) {
                    esdClassNames.add(clazz.getName());
                }
            }
            DSMFactory.getInstance().getAggregationManager().delAggEntity(domainInst.getDomainId(), esdClassNames, projectName, false);
            CustomViewFactory.getInstance().reLoad();
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }
}
