package net.ooder.editor.toolbox.file.service;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.tools.OODModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/rad/file/esdclass/")
@Controller
public class OODEsdClassService {

    @MethodChinaName(cname = "获取文件内容")
    @RequestMapping(value = {"openEsdClass"})
    @TreeEvent(eventEnum = TreeViewEventEnum.onDblclick,
            actions = @CustomAction(script = "SPA.openFile()", params = "{args[1]}")
    )
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    public @ResponseBody
    ResultModel<OODModule> openEsdClass(String path, String className, String projectName) {
        ResultModel<OODModule> result = new ResultModel<OODModule>();
        OODModule oodModule = new OODModule();
        String json = "{}";
        try {
            json = getClient().readFileAsString(path, projectName);
        } catch (Exception e) {
            e.printStackTrace();
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }

    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
