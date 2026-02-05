package net.ooder.editor.console.java.page;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceEditor;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
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
@RequestMapping("/rad/file/java/page/")
public class JavaPageSrcListService {


    @RequestMapping(method = RequestMethod.POST, value = "PageJavaList")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaPageTree>> loadPageJavaList(String projectName, String packageName, PagePackageCatItem catItem, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaPageTree>> resultModel = new TreeListResultModel<List<JavaPageTree>>();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
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
                if (javaSrcBean.getPackageName().equals(packageName)) {
                    javaSrcBeans.add(javaSrcBean);
                }
            }

            resultModel = TreePageUtil.getTreeList(javaSrcBeans, JavaPageTree.class);
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
    public ResultModel<JavaSourceEditor> getPackageEditor(String currentClassName, String packageName, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        JavaSourceEditor editor = null;
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
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
                if (javaSrcBean.getPackageName().equals(packageName)) {
                    javaSrcBeans.add(javaSrcBean);
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
