package net.ooder.editor.toolbox.file.service;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.event.TreeEvent;
import net.ooder.esd.annotation.event.TreeViewEventEnum;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.tools.OODModule;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rad/file/custom/")
public class OODFileService {


    @MethodChinaName(cname = "获取文件内容")
    @RequestMapping(value = {"getFileContent"})
    @TreeEvent(eventEnum = TreeViewEventEnum.onDblclick,
            actions = @CustomAction( script = "SPA.openCls()")
    )
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREENODEEDITOR)
    public @ResponseBody
    ResultModel<OODModule> getFileContent(String path, String className, String projectName) {
        ResultModel<OODModule> result = new ResultModel<OODModule>();
        OODModule oodModule = new OODModule();
        String json = "{}";
        try {

            if (path != null && path.indexOf(VFSConstants.URLVERSION) > -1) {
                FileVersion version = CtVfsFactory.getCtVfsService().getFileVersionByPath(path);
                json = getClient().readFileAsString(path, projectName);
                ModuleComponent moduleComponent = JSONObject.parseObject(json, ModuleComponent.class);
                JDSActionContext.getActionContext().getContext().put("className", className + "V" + version.getIndex());
                moduleComponent.setClassName(className + "V" + version.getIndex());
                json = getClient().genJSON(moduleComponent, null, true).toString();
            } else {
                json = getClient().readFileAsString(path, projectName);
            }
            oodModule.setContent(json);
            result.setData(oodModule);
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
