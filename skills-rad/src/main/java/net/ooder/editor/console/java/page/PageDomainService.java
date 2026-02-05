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
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/page/")
public class PageDomainService {

    @RequestMapping(method = RequestMethod.POST, value = "loadPageRootPackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaPageTree>> loadPageRootPackage(String projectName, String domainId, String currentClassName) {
        TreeListResultModel<List<JavaPageTree>> resultModel = new TreeListResultModel<List<JavaPageTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(PagePackageCatItem.values()), JavaPageTree.class);
        return resultModel;
    }



    @RequestMapping(method = RequestMethod.POST, value = "PageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getSourceEditor(String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<JavaSourceEditor>();
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
