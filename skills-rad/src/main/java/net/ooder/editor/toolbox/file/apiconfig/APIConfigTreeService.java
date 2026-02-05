package net.ooder.editor.toolbox.file.apiconfig;

import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rad/apiconfig/")
public class APIConfigTreeService {

    @RequestMapping(method = RequestMethod.POST, value = "loadChildView")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> loadChildView(String projectName, String domainId, String sourceClassName, String xpath) {
        TreeListResultModel<List<OODFileTree>> resultModel = new TreeListResultModel<List<OODFileTree>>();
        ApiClassConfig customESDClassAPIBean = null;
        try {
            customESDClassAPIBean = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            List<MethodConfig> customMethods = customESDClassAPIBean.getAllViewMethods();
            List<MethodConfig> customViews = new ArrayList<>();
            for (MethodConfig childConfig : customMethods) {
                CustomViewBean view = childConfig.getView();
                if (view != null) {
                    childConfig.setDomainId(domainId);
                    customViews.add(childConfig);
                }
            }
            resultModel = TreePageUtil.getTreeList(customViews, OODFileTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return resultModel;
    }


}
