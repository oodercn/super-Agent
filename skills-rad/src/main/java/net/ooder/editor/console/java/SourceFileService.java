package net.ooder.editor.console.java;

import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.JavaSrcBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
@RequestMapping("/rad/file/java/")
public class SourceFileService {


    @RequestMapping(method = RequestMethod.POST, value = "JavaSourceEditor")
    @ModuleAnnotation(caption = "JAVA树", imageClass = "bpmfont bpmgongzuoliuxitongpeizhi")
    @FormViewAnnotation()
    @APIEventAnnotation(autoRun = true, bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    @ResponseBody
    public ResultModel<JavaSourceEditor> getJavaSourceEditor(String filePath, String domainId, String javaTempId, String sourceClassName, String methodName, String projectName) {
        ResultModel<JavaSourceEditor> result = new ResultModel<>();
        File desFile = new File(filePath);
        JavaSrcBean srcBean = null;
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(desFile, domainInst, javaTempId);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        if (sourceClassName != null && !sourceClassName.equals("")) {
            srcBean.setSourceClassName(sourceClassName);
        }
        if (methodName != null && !methodName.equals("")) {
            srcBean.setMethodName(methodName);
        }

        JavaSourceEditor editor = new JavaSourceEditor(srcBean);
        result.setData(editor);
        return result;
    }


}
