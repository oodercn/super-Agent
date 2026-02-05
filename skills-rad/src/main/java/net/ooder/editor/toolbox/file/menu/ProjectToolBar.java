package net.ooder.editor.toolbox.file.menu;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.config.ResultModel;
import net.ooder.editor.toolbox.file.UPLoadOODFile;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/rad/file/project/"})
@Aggregation(type = AggregationType.MENU, rootClass = ProjectToolBar.class, userSpace = UserSpace.SYS)
public class ProjectToolBar extends OODProjectMenu {

    @MethodChinaName(cname = "")
    @RequestMapping(value = "ImportAgg")
    @APIEventAnnotation()
    @FormViewAnnotation
    @DialogAnnotation(width = "450", height = "380")
    @ModuleAnnotation
    @CustomAnnotation(index = 2, imageClass = "ood-icon-upload", tips = "导入工程")
    public @ResponseBody
    ResultModel<UPLoadOODFile> importAgg() {
        ResultModel<UPLoadOODFile> resultModel = new ResultModel<UPLoadOODFile>();
        try {
            UPLoadOODFile upLoadFile = new UPLoadOODFile();
            resultModel.setData(upLoadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;

    }
}
