package net.ooder.tools.component;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.PopTreeViewAnnotation;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.ViewInst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = {"/test/component/"})
@MethodChinaName(cname = "工具")
@MenuBarMenu(menuType = CustomMenuType.COMPONENT, caption = "组件渲染", index = 0, imageClass = "ri-grid-line")
public class ComponentMenu {

    @RequestMapping(value = {"ESDClassTree"}, method = {RequestMethod.POST})
    @PopTreeViewAnnotation()
    @DialogAnnotation(width = "400")
    @ModuleAnnotation(caption = "绑定实体", dynLoad = true, imageClass = "ri-link-line")
    @APIEventAnnotation(isAllform = true, autoRun = true, customRequestData = {RequestPathEnum.SPA_CLASSNAME, RequestPathEnum.RAD_SELECTITEMS})
    @ResponseBody
    public TreeListResultModel<List<SPAClassTree>> getESDTreeView(String dsmId, String className, String[] selectItems) {
        TreeListResultModel<List<SPAClassTree>> result = new TreeListResultModel<List<SPAClassTree>>();
        try {
            if (dsmId != null && dsmId.equals("")) {
                dsmId = className;
            }
            ViewInst bean = DSMFactory.getInstance().getViewManager().getViewInstById(dsmId);
            List<SPAClassTree> treeViews = new ArrayList<SPAClassTree>();
            treeViews.add(new SPAClassTree(dsmId));
            result.setData(treeViews);

        } catch (JDSException e) {
            result = new ErrorListResultModel<>();
            ((ErrorListResultModel) result).setErrcode(e.getErrorCode());
            ((ErrorListResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }
}
