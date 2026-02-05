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
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/cat/")
public class CatPackageService {

    @RequestMapping(method = RequestMethod.POST, value = "CatPackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadCatPackage(String projectName, PackageCatItem catItem) {
        TreeListResultModel<List<JavaSourceTree>> resultModel = new TreeListResultModel<List<JavaSourceTree>>();
        try {
            List<JavaPackage> javaPackageList = new ArrayList<>();
            List<DomainInst> domainInstList = DSMFactory.getInstance().getAllUserDomainInst(projectName);
            List<JavaSrcBean> allJavaSrcBeans = new ArrayList<>();
            for (DomainInst domainInst : domainInstList) {
                allJavaSrcBeans.addAll(domainInst.getAllJavaSrcBeans());
            }
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


    @RequestMapping(method = RequestMethod.POST, value = "CatPackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getCatPackageEditor(String id, String filePath, String domainId, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        File desFile = new File(filePath);
        JavaSrcBean srcBean = null;
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            JavaPackage javaPackage = domainInst.getPackageByFile(desFile);
            if (javaPackage.listAllFile().size() > 0) {
                srcBean = javaPackage.listAllFile().iterator().next();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        JavaSourceEditor editor = new JavaSourceEditor(srcBean);
        result.setData(editor);
        return result;
    }


}
