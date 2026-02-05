package net.ooder.editor.console;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.dsm.view.config.tree.ViewConfigTree;
import net.ooder.dsm.website.DomainTempNavTree;
import net.ooder.editor.console.java.JavaSourceTree;
import net.ooder.editor.console.temp.JavaTempCat;
import net.ooder.editor.console.temp.JavaTempFoldingTabs;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.ToolBarEvent;
import net.ooder.esd.annotation.event.ToolBarEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.PanelType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.ButtonViewsViewAnnotation;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.util.page.ButtonViewPageUtil;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = {"/rad/file/status/"})
@Aggregation(type = AggregationType.MENU, rootClass = MainStatusToolBar.class, userSpace = UserSpace.SYS)
public class MainStatusToolBar {


    @ToolBarEvent(eventEnum = ToolBarEventEnum.onClick,
            pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @RequestMapping(method = RequestMethod.POST, value = "WebSiteTreeNav")
    @APIEventAnnotation(autoRun = true)
    @NavTreeViewAnnotation
    @ModuleAnnotation(imageClass = "ri-earth-line", dynLoad = true, panelType = PanelType.panel, caption = "模板站点")
    @CustomAnnotation(index = 2)
    @ResponseBody
    public TreeListResultModel<List<DomainTempNavTree>> getDSMTempTreeNav(String id) {
        TreeListResultModel<List<DomainTempNavTree>> resultModel = new TreeListResultModel<List<DomainTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList("sys"), DomainTempNavTree.class);
        return resultModel;

    }
//
//    @ToolBarEvent(eventEnum = ToolBarEventEnum.onClick,
//            pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
//    @MethodChinaName(cname = "模板管理")
//    @RequestMapping(method = RequestMethod.POST, value = "CodeTemps")
//    @APIEventAnnotation(autoRun = true)
//    @NavTreeViewAnnotation
//    @ModuleAnnotation(imageClass = "ri-stack-line", dynLoad = true, panelType = PanelType.panel, caption = "模板管理")
//    @CustomAnnotation(index = 3)
//    @ResponseBody
//    public TreeListResultModel<List<JavaTempNavTree>> getTempManager(String id) {
//        TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
//        resultModel = TreePageUtil.getTreeList(Arrays.asList(DSMType.values()), JavaTempNavTree.class, Arrays.asList(DSMType.REPOSITORY.getType()));
//        return resultModel;
//
//    }

    @ToolBarEvent(eventEnum = ToolBarEventEnum.onClick,
            pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @MethodChinaName(cname = "模板分类")
    @RequestMapping(method = RequestMethod.POST, value = "JavaTempCat")
    @APIEventAnnotation(autoRun = true)
    @ModuleAnnotation(imageClass = "ri-stack-line", dynLoad = true, panelType = PanelType.panel, caption = "模板分类")
    @UIAnnotation(dock = Dock.fill)
    @ButtonViewsViewAnnotation()
    @ResponseBody
    public ListResultModel<List<JavaTempFoldingTabs>> getJavaTempCat(String projectName) {
        ListResultModel<List<JavaTempFoldingTabs>> result = new ListResultModel<List<JavaTempFoldingTabs>>();
        result = ButtonViewPageUtil.getTabList(Arrays.asList(JavaTempCat.values()), JavaTempFoldingTabs.class);
        return result;
    }


    @ToolBarEvent(eventEnum = ToolBarEventEnum.onClick,
            pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @RequestMapping(method = RequestMethod.POST, value = "JavaSoruceTree")
    @ModuleAnnotation(caption = "JAVA源码", panelType = PanelType.panel, imageClass = "ri-mouse-line")
    @NavTreeViewAnnotation
    @PanelAnnotation(dock = Dock.fill)
    @BtnAnnotation(closeBtn = false)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> getJavaSoruceTree(String domainId) {
        String projectVersionName = null;
        try {
            projectVersionName = DSMFactory.getInstance().getDefaultProjectName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        TreeListResultModel<List<JavaSourceTree>> result = TreePageUtil.getTreeList(Arrays.asList(projectVersionName), JavaSourceTree.class);
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "DSMModuleView")
    @APIEventAnnotation(autoRun = true, customRequestData = RequestPathEnum.SPA_CLASSNAME)
    @NavTreeViewAnnotation
    @ToolBarEvent(eventEnum = ToolBarEventEnum.onClick,
            pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after")
    @ModuleAnnotation(caption = "领域模型", imageClass = "ri-css3-line", panelType = PanelType.panel)
    @PanelAnnotation(dock = Dock.fill)
    @BtnAnnotation(closeBtn = false)
    @ResponseBody
    public TreeListResultModel<List<ViewConfigTree>> getDSMModuleView(String currentClassName, String projectName) {
        TreeListResultModel<List<ViewConfigTree>> result = new TreeListResultModel<>();
        try {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            MethodConfig methodConfig = null;
            CustomViewBean viewBean = null;
            try {
                methodConfig = euModule.getComponent().getMethodAPIBean();
                if (methodConfig == null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                    viewBean = methodConfig.getView();
                    AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euModule.getClassName(), projectName);
                    aggRootBuild.build();
                    DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(viewBean.getDomainId());
                    if (methodConfig == null) {
                        AggViewRoot viewRoot = new AggViewRoot(domainInst, euModule.getClassName(), viewBean);
                        DSMFactory.getInstance().getAggregationManager().genAggViewJava(viewRoot, viewBean, euModule.getClassName(), getCurrChromeDriver());
                        methodConfig = euModule.getComponent().getMethodAPIBean();
                    }
                    euModule.getComponent().getProperties().setDsmProperties(new DSMProperties(methodConfig, projectName));
                    euModule.update(true);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String sourceClassName = methodConfig.getSourceClassName();

            if (methodConfig != null && methodConfig.getView() != null) {
                viewBean = euModule.getComponent().getMethodAPIBean().getView();
                result = TreePageUtil.getTreeList(Arrays.asList(viewBean), ViewConfigTree.class);
            } else {
                result = TreePageUtil.getTreeList(Arrays.asList(euModule), ViewConfigTree.class);
            }
            result.addCtx("sourceClassName", sourceClassName);
            result.addCtx("domainId", viewBean.getDomainId());
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }

}
