package net.ooder.editor.console.temp;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.TreeListResultModel;
import net.ooder.dsm.admin.temp.JavaTempNavTree;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.PanelType;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = {"/rad/file/temp/"})
public class JavaTempCatService {


    @MethodChinaName(cname = "模板管理")
    @RequestMapping(method = RequestMethod.POST, value = "CodeTemps")
    @APIEventAnnotation(autoRun = true, bindTabsEvent = CustomTabsEvent.TABEDITOR)
    @NavTreeViewAnnotation
    @ModuleAnnotation(imageClass = "ri-stack-line", dynLoad = true, panelType = PanelType.panel, caption = "模板管理")
    @ResponseBody
    public TreeListResultModel<List<JavaTempNavTree>> getTempManager(DSMType dsmType) {
        TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(dsmType), JavaTempNavTree.class, Arrays.asList(dsmType.getType()));
        return resultModel;

    }

}
