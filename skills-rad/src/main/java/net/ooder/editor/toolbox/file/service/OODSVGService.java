package net.ooder.editor.toolbox.file.service;

import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.view.TreeViewAnnotation;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/rad/file/svg/")
@Controller
public class OODSVGService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD, autoRun = true, bindTabsEvent = CustomTabsEvent.TABEDITOR)
    @RequestMapping("SVG")
    @TreeViewAnnotation
    @ModuleAnnotation(cache = false)
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> getSVG(String projectName) {

        return null;

    }


    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
