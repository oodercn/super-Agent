package net.ooder.editor.toolbox.file.menu;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.config.ResultModel;
import net.ooder.dsm.editor.agg.menu.UPLoadAgg;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/rad/file/domain/")
@Aggregation(type = AggregationType.MENU, rootClass = DomainContextMenu.class)
@MenuBarMenu(menuType = CustomMenuType.CONTEXTMENU, caption = "领域右键菜单")
public class DomainContextMenu extends PackageContextMenu {

    @RequestMapping(value = "ImportDomain")
    @APIEventAnnotation()
    @FormViewAnnotation
    @DialogAnnotation(width = "450", height = "380")
    @ModuleAnnotation
    @CustomAnnotation(index = 8, imageClass = "ood-icon-upload", tips = "导入工程")
    public @ResponseBody
    ResultModel<UPLoadAgg> importDomain() {
        ResultModel<UPLoadAgg> resultModel = new ResultModel<UPLoadAgg>();
        try {
            UPLoadAgg upLoadFile = new UPLoadAgg();
            resultModel.setData(upLoadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;

    }

}
