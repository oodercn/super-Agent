package net.ooder.editor.console.java.esd;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceEditor;
import net.ooder.editor.console.java.SourcePackage;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/rad/file/java/module/")
public class JavaModulePackageService {
    @RequestMapping(method = RequestMethod.POST, value = "SourcePackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getSourcePackageEditor(String currentClassName, String domainId, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        JavaSourceEditor editor = null;
        try {
            DomainInst domainInst = null;
            if (domainId != null) {
                domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            } else {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            }
            List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
            if (methodConfig != null) {
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

                if (javaSrcBeans.size() > 0) {
                    editor = new JavaSourceEditor(javaSrcBeans.get(0));
                }

            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        result.setData(editor);
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "SourcePackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaEsdTree>> loadSourcePackage(String projectName, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaEsdTree>> resultModel = new TreeListResultModel<List<JavaEsdTree>>();
        try {
            DomainInst domainInst = null;
            if (domainId != null) {
                domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            } else {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            }
            List<SourcePackage> javaPackageList = new ArrayList<>();
            List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
            if (methodConfig != null) {
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
            resultModel = TreePageUtil.getTreeList(javaPackageList, JavaEsdTree.class);

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }


}
