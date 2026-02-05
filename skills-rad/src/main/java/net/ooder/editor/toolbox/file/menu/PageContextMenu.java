package net.ooder.editor.toolbox.file.menu;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping(value = "/rad/file/page/context/")
@Aggregation(type = AggregationType.MENU, rootClass = PageContextMenu.class)
@MenuBarMenu(menuType = CustomMenuType.CONTEXTMENU, caption = "页面右键菜单")
public class PageContextMenu extends FileContextMenu {


    @Split
    @CustomAnnotation(index = 0)
    @ResponseBody
    public ResultModel<Boolean> split0() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "copy")
    @CustomAnnotation(imageClass = "ri-file-copy-line", index = 1, caption = "复制") //
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW)
    public @ResponseBody
    ResultModel<Boolean> copy(String filePath, String packageName) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        result.addCtx("sfilePath", filePath);
        result.addCtx("packageName", packageName);
        return result;
    }


    @Split
    @CustomAnnotation(index = 10)
    @ResponseBody
    public ResultModel<Boolean> split10() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }


    @Disabled
    @RequestMapping(method = RequestMethod.POST, value = "buildAgg")
    @CustomAnnotation(index = 1, caption = "同步领域模型", imageClass = "ri-hammer-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = CustomCallBack.FREE,
            afterInvokeAction = @CustomAction(script = "SPA.reOpenCls()",params = {"args[1].data"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    @ResponseBody
    public ResultModel<OODFileTree> buildAgg(String currentClassName, String projectName) {
        ResultModel<OODFileTree> result = new ResultModel<>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
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
            result.setData(new OODFileTree(euModule,projectName,domainInst.getDomainId()));
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Split
    @CustomAnnotation(index = 15)
    @ResponseBody
    public ResultModel<Boolean> split15() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @Disabled
    @RequestMapping(method = RequestMethod.POST, value = "clearAgg")
    @CustomAnnotation(index = 16, caption = "重新生成代码", imageClass = "ri-loader-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = CustomCallBack.FREE,
            afterInvokeAction =@CustomAction(script = "SPA.reOpenCls()",params = {"args[1].data"}),
            onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
    )
    @ResponseBody
    public ResultModel<OODFileTree> clearAgg(String currentClassName, String projectName) {
        ResultModel<OODFileTree> result = new ResultModel<>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
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
            result.setData(new OODFileTree(euModule,projectName,domainInst.getDomainId()));
        } catch (JDSException e) {
            e.printStackTrace();
        }


        return result;
    }


    @Disabled
    @RequestMapping(value = {"reBuildService"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.CTX},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvokeAction = @CustomAction(script = "SPA.reOpenCls()",params = {"args[1].data"}),
            callback = CustomCallBack.FREE
    )
    @CustomAnnotation(index = 18, caption = "清空领域缓存", imageClass = "ri-loader-line")
    @ResponseBody
    public ResultModel<OODFileTree> reBuildService(String projectName, String currentClassName) {
        ResultModel result = new ResultModel();
        Set<String> esdClassNames = new HashSet<>();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            ModuleComponent moduleComponent = euModule.getComponent();
            MethodConfig methodConfig = moduleComponent.getMethodAPIBean();
            if (methodConfig == null) {
                methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
            }
            if (methodConfig != null) {
                esdClassNames.add(methodConfig.getSourceClassName());
                Set<Class> classes = methodConfig.getOtherClass();
                for (Class clazz : classes) {
                    if (clazz != null) {
                        esdClassNames.add(clazz.getName());
                    }
                    if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
                        esdClassNames.add(clazz.getSuperclass().getName());
                    }
                }
            } else if (moduleComponent.getProperties().getDsmProperties() != null) {
                String sourceClassName = moduleComponent.getProperties().getDsmProperties().getSourceClassName();
                if (sourceClassName != null) {
                    esdClassNames.add(sourceClassName);
                }
            }
            DSMFactory.getInstance().getAggregationManager().delAggEntity(domainInst.getDomainId(), esdClassNames, projectName, false);
            result.setData(new OODFileTree(euModule,projectName,domainInst.getDomainId()));
        } catch (Throwable e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }
}
