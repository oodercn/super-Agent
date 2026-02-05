package net.ooder.editor.console.java.page;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceEditor;
import net.ooder.editor.console.java.JavaSourceTree;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/module/")
public class JavaPagePackageService {

    @RequestMapping(method = RequestMethod.POST, value = "PagePackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadPagePackage(String projectName, PagePackageCatItem catItem, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaSourceTree>> resultModel = new TreeListResultModel<List<JavaSourceTree>>();
        try {
            List<JavaPackage> javaPackageList = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            ModuleComponent moduleComponent = euModule.getComponent();
            CustomViewBean viewBean = null;
            if (moduleComponent.getMethodAPIBean() != null && moduleComponent.getMethodAPIBean().getView() != null) {
                viewBean = moduleComponent.getMethodAPIBean().getView();
            } else {
                viewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(moduleComponent, domainInst.getDomainId());
                AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euModule.getClassName(), projectName);
            }

            List<JavaSrcBean> allJavaSrcBeans = viewBean.getAllJavaSrc();
            for (JavaSrcBean javaSrcBean : allJavaSrcBeans) {
                if (javaSrcBean.getDsmType() != null && javaSrcBean.getDsmType().equals(catItem.getDsmType()) && !javaPackageList.contains(javaSrcBean.getJavaPackage())) {
                    javaPackageList.add(javaSrcBean.getJavaPackage());
                }
            }
            resultModel = TreePageUtil.getTreeList(javaPackageList, JavaSourceTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;

    }

    @RequestMapping(method = RequestMethod.POST, value = "PagePackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getPagePackageEditor(String currentClassName, PagePackageCatItem catItem, String domainId, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        JavaSourceEditor editor = null;
        try {
            List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            ModuleComponent moduleComponent = euModule.getComponent();
            CustomViewBean viewBean = null;
            if (moduleComponent.getMethodAPIBean() != null && moduleComponent.getMethodAPIBean().getView() != null) {
                viewBean = moduleComponent.getMethodAPIBean().getView();
            } else {
                viewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(moduleComponent, domainInst.getDomainId());
                AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(viewBean, euModule.getClassName(), projectName);
                viewBean = aggRootBuild.getCustomViewBean();
            }

            List<JavaSrcBean> allJavaSrcBeans = viewBean.getAllJavaSrc();
            for (JavaSrcBean javaSrcBean : allJavaSrcBeans) {
                if (javaSrcBean.getDsmType() != null && javaSrcBean.getDsmType().equals(catItem.getDsmType())) {
                    javaSrcBeans.add(javaSrcBean);
                }
            }
            if (javaSrcBeans.size() > 0) {
                editor = new JavaSourceEditor(javaSrcBeans.get(0));
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        result.setData(editor);
        return result;
    }


}
