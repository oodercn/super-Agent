package net.ooder.editor.console.java.page;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
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
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.FileType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
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

import java.util.*;


@Controller
@RequestMapping(value = {"/rad/file/java/page/"})
@Aggregation(type = AggregationType.MENU, rootClass = JavaPageBar.class, userSpace = UserSpace.SYS)
@ToolBarMenu(hAlign = HAlignType.right)
public class JavaPageBar {


    @MethodChinaName(cname = "刷新")
    @RequestMapping(method = RequestMethod.POST, value = "reLoad")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 1, tips = "刷新工程文件")
    @APIEventAnnotation(
            afterInvoke = {CustomCallBack.PAGERELOAD}
    )
    public @ResponseBody
    TreeListResultModel<List<ConfigModuleTree>> reLoad(String id, String projectName, String parentId, FileType type, String xpath) {
        TreeListResultModel<List<ConfigModuleTree>> result = new TreeListResultModel<List<ConfigModuleTree>>();
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "buildAgg")
    @CustomAnnotation(index = 1, caption = "同步领域模型", imageClass = "ri-hammer-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = {CustomCallBack.FREE, CustomCallBack.RELOAD},
            afterInvokeAction = @CustomAction(script = "SPA.reOpenCls()", params = {"args[1].data"}),
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
            result.setData(new OODFileTree(euModule, projectName, domainInst.getDomainId()));
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "clearAgg")
    @CustomAnnotation(index = 16, caption = "重新生成代码", imageClass = "ri-loader-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = {CustomCallBack.FREE, CustomCallBack.RELOAD},
            afterInvokeAction = @CustomAction(script = "SPA.reOpenCls()", params = {"args[1].data"}),
            callback = {CustomCallBack.MESSAGE}
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
            Set<String> esdClassNames = new HashSet<>();

            MethodConfig methodConfig = moduleComponent.getMethodAPIBean();
            if (methodConfig == null) {
                String soruceClassName = aggRootBuild.getAggServiceRootBean().get(0).getClassName();
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(soruceClassName);
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.findEditorMethod();
                }
            }

            if (methodConfig != null) {
                euModule.getComponent().getProperties().setDsmProperties(new DSMProperties(methodConfig, projectName));
                euModule.update(false);
                esdClassNames.add(methodConfig.getSourceClassName());
                //   DSMFactory.getInstance().getAggregationManager().deleteApiClassConfig(esdClassNames);
            }


            result.setMessage("代码生成完毕！");
            result.setData(new OODFileTree(euModule, projectName, domainInst.getDomainId()));
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Disabled
    @RequestMapping(value = {"reBuildService"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.CTX},
            beforeInvoke = CustomBeforInvoke.BUSY,
            afterInvoke = {CustomCallBack.FREE, CustomCallBack.RELOAD},
            callback = CustomCallBack.MESSAGE
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
            result.setMessage("清空领域完成！");
            DSMFactory.getInstance().getAggregationManager().delAggEntity(domainInst.getDomainId(), esdClassNames, projectName, false);

            result.setData(new OODFileTree(euModule, projectName, domainInst.getDomainId()));
        } catch (Throwable e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }

}
