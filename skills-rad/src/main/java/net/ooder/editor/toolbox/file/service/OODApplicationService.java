package net.ooder.editor.toolbox.file.service;

import net.ooder.config.CApplication;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/rad/file/")
@Controller
public class OODApplicationService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREERELOAD)
    @RequestMapping("getApplications")
    @ResponseBody
    public TreeListResultModel<List<CApplication>> getApplications() {
        return null;
    }

}
