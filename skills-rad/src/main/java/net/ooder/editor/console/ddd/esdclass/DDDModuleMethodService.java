package net.ooder.editor.console.ddd.esdclass;

import net.ooder.config.TreeListResultModel;
import net.ooder.dsm.view.config.base.BaseViewService;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/ddd/esdclass/")
public class DDDModuleMethodService extends BaseViewService {

    @RequestMapping(method = RequestMethod.POST, value = "loadChildMethod")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> loadChildMethod(String projectName, String domainId, String sourceClassName, String sourceMethodName, String currentClassName, String xpath) {
        TreeListResultModel<List<OODFileTree>> resultModel = new TreeListResultModel<List<OODFileTree>>();
        ApiClassConfig customESDClassAPIBean = this.getApiClassConfig(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        List<MethodConfig> customMethods = customESDClassAPIBean.getAllViewMethods();
        List<MethodConfig> methods = new ArrayList<>();
        for (MethodConfig childConfig : customMethods) {
            CustomViewBean view = childConfig.getView();
            if (view == null) {
                methods.add(childConfig);
            }
        }
        resultModel = TreePageUtil.getTreeList(methods, OODFileTree.class);
        return resultModel;
    }

}
