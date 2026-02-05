package net.ooder.editor.console.java;

import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/")
public class JavaSrcFileService {


    @RequestMapping(method = RequestMethod.POST, value = "SourceJavaList")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadSourceJavaList(String projectName, String packageName, PackageCatItem catItem) {
        TreeListResultModel<List<JavaSourceTree>> resultModel = new TreeListResultModel<List<JavaSourceTree>>();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        try {
            List<DomainInst> domainInstList = DSMFactory.getInstance().getAllUserDomainInst(projectName);
            List<JavaSrcBean> allJavaSrcBeans = new ArrayList<>();
            for (DomainInst domainInst : domainInstList) {
                allJavaSrcBeans.addAll(domainInst.getAllJavaSrcBeans());
            }

            for (JavaSrcBean javaSrcBean : allJavaSrcBeans) {
                if (!javaSrcBeans.contains(javaSrcBean) && javaSrcBean.getPackageName().equals(packageName)) {
                    javaSrcBeans.add(javaSrcBean);
                }
            }

            resultModel = TreePageUtil.getTreeList(javaSrcBeans, JavaSourceTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }

    @RequestMapping(method = RequestMethod.POST, value = "SourcePackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getSourcePackageEditor(String packageName, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        JavaSourceEditor editor = null;
        try {
            List<DomainInst> domainInstList = DSMFactory.getInstance().getAllUserDomainInst(projectName);
            List<JavaSrcBean> allJavaSrcBeans = new ArrayList<>();
            for (DomainInst domainInst : domainInstList) {
                allJavaSrcBeans.addAll(domainInst.getAllJavaSrcBeans());
            }

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
