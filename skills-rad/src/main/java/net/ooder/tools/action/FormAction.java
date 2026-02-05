package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.config.TreeListResultModel;
import net.ooder.dsm.admin.temp.JavaTempNavTree;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
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
@RequestMapping(value = {"/action/form/"})
@MethodChinaName(cname = "表单")
@MenuBarMenu(menuType = CustomMenuType.TOOLBAR, caption = "表单", index = 5)
@Aggregation(type = AggregationType.MENU,userSpace = UserSpace.SYS)
public class FormAction {

    @MethodChinaName(cname = "代码模板")
    @RequestMapping(method = RequestMethod.POST, value = "CodeTemps")
    @APIEventAnnotation(autoRun = true)
    @NavTreeViewAnnotation
    @CustomAnnotation(index = 2)
    @DialogAnnotation(caption = "代码模板", width = "900", height = "680")
    @ModuleAnnotation(imageClass = "ri-tools-line", dynLoad = true, caption = "代码模板")
    @ResponseBody
    public TreeListResultModel<List<JavaTempNavTree>> getTempManager(String id) {
        TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(DSMType.values()), JavaTempNavTree.class);
        return resultModel;

    }

    @RequestMapping(method = RequestMethod.POST, value = "ViewTemps")
    @APIEventAnnotation(autoRun = true)
    @NavTreeViewAnnotation
    @CustomAnnotation(index = 3)
    @DialogAnnotation(caption = "视图配置", width = "900", height = "680")
    @ModuleAnnotation(imageClass = "ri-settings-3-line", dynLoad = true, caption = "视图配置")
    @ResponseBody
    public TreeListResultModel<List<JavaTempNavTree>> getViewTemps(String id) {
        TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(DSMType.AGGREGATION), JavaTempNavTree.class);

        return resultModel;

    }

}
