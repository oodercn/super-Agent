package net.ooder.editor.console.java.esd;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.console.java.JavaSourceEditor;
import net.ooder.editor.console.java.JavaSourceTree;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/esd/")
public class EsdDomainService {

    @RequestMapping(method = RequestMethod.POST, value = "loadPagePackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadPagePackage(String domainId, String projectName) {
        List<Object> javaFileList = new ArrayList<>();
        TreeListResultModel<List<JavaSourceTree>> result = new TreeListResultModel<>();
        DomainInst domainInst = null;
        try {
            domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            JavaPackage rootPackage = domainInst.getRootPackage();
            List<JavaPackage> childList = rootPackage.getChildPackages();
            for (JavaPackage child : childList) {
                if (!javaFileList.contains(child.getParent())) {
                    javaFileList.add(child);
                }
            }
            javaFileList.addAll(rootPackage.listFiles());
            result = TreePageUtil.getTreeList(javaFileList, JavaSourceTree.class);
        } catch (JDSException e) {
            result = new ErrorListResultModel<>();
            ((ErrorListResultModel<List<JavaSourceTree>>) result).setErrdes(e.getMessage());
            e.printStackTrace();
        }

        return result;

    }


    @RequestMapping(method = RequestMethod.POST, value = "PageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getPageEditor(String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        JavaSrcBean srcBean = null;
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstByCat(projectName, UserSpace.USER);
            JavaPackage rootPackage = domainInst.getRootPackage();
            List<JavaSrcBean> childList = rootPackage.listAllFile();
            if (childList.size() > 0) {
                srcBean = childList.get(0);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        JavaSourceEditor editor = new JavaSourceEditor(srcBean);
        result.setData(editor);
        return result;
    }

}
