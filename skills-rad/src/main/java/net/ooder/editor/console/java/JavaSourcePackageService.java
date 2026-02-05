package net.ooder.editor.console.java;

import net.ooder.common.JDSException;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/file/java/package/")
public class JavaSourcePackageService {



    @RequestMapping(method = RequestMethod.POST, value = "loadDomainFile")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<JavaSourceTree>> loadDomainFile(String id, String filePath, String domainId, String projectName) {
        TreeListResultModel<List<JavaSourceTree>> result = new TreeListResultModel<>();
        List<Object> javaFileList = new ArrayList<>();
        File desFile = new File(filePath);
        DomainInst domainInst = null;
        try {
            domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            JavaPackage javaPackage = domainInst.getPackageByFile(desFile);
            if (javaPackage != null) {
                javaFileList.addAll(javaPackage.listFiles());
            }
            result = TreePageUtil.getTreeList(javaFileList, JavaSourceTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "PackageEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getAggJavaEditor(String id, String filePath, String domainId, String projectName) {
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



    @RequestMapping(method = RequestMethod.POST, value = "uploadFile")
    @APIEventAnnotation(bindFieldEvent = CustomFieldEvent.UPLOAD)
    @CustomAnnotation(caption = "上传文件")
    public @ResponseBody
    ResultModel<Boolean> addDocument(String domainId, String packageName, String projectName, @RequestParam("files") MultipartFile file) {
        ResultModel<Boolean> resultModel = new ResultModel<Boolean>();
        DomainInst domainInst = null;
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                if (fileName != null && fileName.endsWith(".java")) {
                    domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
                    JavaPackage javaPackage = domainInst.getPackageByName(packageName);
                    InputStream inputStream = file.getInputStream();
                    javaPackage.upload(file.getOriginalFilename(), inputStream);
                } else {
                    return new ErrorResultModel<>("只支持JAVA文件上传");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultModel;

    }

}
