package net.ooder.editor.console.java.esd;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceEditor;
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
public class JavaModuleSrcListService {


    @RequestMapping(method = RequestMethod.POST, value = "ModuleJava")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaEsdTree>> loadModuleJava(String projectName, String packageName, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaEsdTree>> resultModel = new TreeListResultModel<List<JavaEsdTree>>();
        try {
            DomainInst domainInst = null;
            if (domainId != null && !domainId.equals("")) {
                domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            } else {
                domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER);
            }
            List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            if (euModule.getComponent().getMethodAPIBean() != null) {
                MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
                UserSpace userSpace = domainInst.getUserSpace();
                switch (userSpace) {
                    case VIEW:
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(methodConfig.getView(), methodConfig.getEUClassName(), projectName);
                        List<JavaSrcBean> allJavaSrcBeans = aggRootBuild.getAllSrcBean();
                        if (allJavaSrcBeans.isEmpty()) {
                            aggRootBuild.build();
                            allJavaSrcBeans = aggRootBuild.getAllSrcBean();
                        }
                        for (JavaSrcBean javaSrcBean : allJavaSrcBeans) {
                            if (javaSrcBean != null && javaSrcBean.getPackageName().equals(packageName)) {
                                javaSrcBeans.add(javaSrcBean);                            }
                        }
                        break;
                    case USER:
                        Set<Class> clazzs = methodConfig.getOtherClass();
                        clazzs.add(methodConfig.getEsdClass().getCtClass());
                        for (Class clazz : clazzs) {
                            JavaSrcBean javaSrcBean = domainInst.getJavaSrcByClassName(clazz.getName());
                            if (javaSrcBean != null && javaSrcBean.getPackageName().equals(packageName)) {
                                javaSrcBeans.add(javaSrcBean);
                            }
                        }
                        break;
                }
            }
            resultModel = TreePageUtil.getTreeList(javaSrcBeans, JavaEsdTree.class);

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;

    }

    @RequestMapping(method = RequestMethod.POST, value = "PackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getPackageEditor(String currentClassName, String packageName, String domainId, String projectName) {
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
            if (euModule.getComponent().getMethodAPIBean() != null) {
                MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();

                UserSpace userSpace = domainInst.getUserSpace();
                switch (userSpace) {
                    case VIEW:
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(methodConfig.getView(), methodConfig.getEUClassName(), projectName);
                        List<JavaSrcBean> allJavaSrcBeans = aggRootBuild.getAllSrcBean();
                        if (allJavaSrcBeans.isEmpty()) {
                            aggRootBuild.build();
                            allJavaSrcBeans = aggRootBuild.getAllSrcBean();
                        }
                        for (JavaSrcBean javaSrcBean : allJavaSrcBeans) {
                            if (javaSrcBean != null && javaSrcBean.getPackageName().equals(packageName)) {
                                javaSrcBeans.add(javaSrcBean);
                            }
                        }
                        break;
                    case USER:
                        Set<Class> clazzs = methodConfig.getOtherClass();
                        clazzs.add(methodConfig.getEsdClass().getCtClass());
                        for (Class clazz : clazzs) {
                            JavaSrcBean javaSrcBean = domainInst.getJavaSrcByClassName(clazz.getName());
                            if (javaSrcBean != null && javaSrcBean.getPackageName().equals(packageName)) {
                                javaSrcBeans.add(javaSrcBean);
                            }
                        }

                        break;
                }

            }
            editor = new JavaSourceEditor(javaSrcBeans.get(0));
            result.setData(editor);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }
}
