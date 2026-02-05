package net.ooder.editor.console.java;

import net.ooder.annotation.UserSpace;
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

import java.util.Arrays;
import java.util.List;

/**
 * 源代码领域服务类
 * 负责处理Java源代码的领域逻辑和树节点加载
 */
@Controller
@RequestMapping("/rad/file/java/domain/")
public class SourceDomainService {

    /**
     * 加载领域包结构
     * @param projectName 项目名称
     * @return 树列表结果模型
     */
    @RequestMapping(method = RequestMethod.POST, value = "loadDomainPackage")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadPageRootPackage(String projectName) {
        TreeListResultModel<List<JavaSourceTree>> resultModel = new TreeListResultModel<List<JavaSourceTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(PackageCatItem.values()), JavaSourceTree.class);
        return resultModel;
    }


    /**
     * 获取源代码编辑器
     * @param projectName 项目名称
     * @return 源代码编辑器结果模型
     */
    @RequestMapping(method = RequestMethod.POST, value = "SourceEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getSourceEditor(String projectName) {
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
