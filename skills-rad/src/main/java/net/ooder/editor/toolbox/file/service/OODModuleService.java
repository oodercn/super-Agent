package net.ooder.editor.toolbox.file.service;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.console.java.SourcePackage;
import net.ooder.editor.console.java.esd.JavaEsdTree;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.util.page.TreePageUtil;
import net.ooder.tools.OODModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/rad/file/")
public class OODModuleService {

    @MethodChinaName(cname = "获取文件内容")
    @RequestMapping(value = {"getFileContent"})
    @TreeEvent(eventEnum = TreeViewEventEnum.onDblclick,
            actions = @CustomAction(name = "openCls", script = "SPA.openCls()", params = "{args[1]}")
    )
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEDBCLICK)
    public @ResponseBody
    ResultModel<OODModule> openCls(String path, String className, String projectName) {
        ResultModel<OODModule> result = new ResultModel<OODModule>();
        OODModule oodModule = new OODModule();
        try {
            if (path == null && className != null) {
                path = className;
            }
            EUModule module = getClient().getModule(path, projectName, true);
            if (module != null) {
                String json = getClient().genJSON(module, null, true).toString();
                oodModule.setContent(json);
            }

            result.setData(oodModule);
        } catch (Exception e) {
            e.printStackTrace();
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


    @NavTreeViewAnnotation
    @RequestMapping(method = RequestMethod.POST, value = "JavaEsdTree")
    @TreeEvent(eventEnum = TreeViewEventEnum.onDblclick, pageAction = CustomLoadClassAction.show2, targetFrame = "mainTabs", childName = "after", _return = true)
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEDBCLICK)
    @ResponseBody
    public TreeListResultModel<List<JavaEsdTree>> loadJavaEsdTree(String projectName, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaEsdTree>> resultModel = new TreeListResultModel<List<JavaEsdTree>>();
        try {
            DomainInst domainInst = null;
            if (domainId != null && !domainId.equals("")) {
                domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            } else {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            }
            List<SourcePackage> javaPackageList = new ArrayList<>();
            List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            if (euModule != null) {
                if (euModule.getComponent().getMethodAPIBean() != null) {
                    MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();

                    UserSpace userSpace = domainInst.getUserSpace();
                    switch (userSpace) {
                        case VIEW:
                            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(methodConfig.getView(), methodConfig.getEUClassName(), projectName);
                            javaSrcBeans = aggRootBuild.getAllSrcBean();
                            if (javaSrcBeans.isEmpty()) {
                                aggRootBuild.build();
                                javaSrcBeans = aggRootBuild.getAllSrcBean();
                            }
                            break;
                        case USER:
                            Set<Class> clazzs = methodConfig.getOtherClass();
                            clazzs.add(methodConfig.getEsdClass().getCtClass());
                            for (Class clazz : clazzs) {
                                if (domainInst.getJavaSrcByClassName(clazz.getName()) != null) {
                                    javaSrcBeans.add(domainInst.getJavaSrcByClassName(clazz.getName()));
                                }
                            }
                            break;
                    }

                    for (JavaSrcBean javaSrcBean : javaSrcBeans) {
                        SourcePackage sourcePackage = new SourcePackage(javaSrcBean.getJavaPackage(), currentClassName, domainId);
                        if (!javaPackageList.contains(sourcePackage)) {
                            javaPackageList.add(sourcePackage);
                        }
                    }
                }
            }

            resultModel = TreePageUtil.getTreeList(javaPackageList, JavaEsdTree.class);

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }


    @JSONField(serialize = false)
    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
