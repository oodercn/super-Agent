package net.ooder.editor.console.temp;

import net.ooder.config.ListResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.view.ButtonViewsViewAnnotation;
import net.ooder.esd.util.page.ButtonViewPageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/rad/temp/")
public class TempCatService {


    @RequestMapping(method = RequestMethod.POST, value = "JavaTempCat")
    @APIEventAnnotation(autoRun = true, bindMenu = CustomMenuItem.INDEX)
    @ModuleAnnotation(caption = "JavaTempCat", dynLoad = true)
    @UIAnnotation(dock = Dock.fill)
    @ButtonViewsViewAnnotation()
    @ResponseBody
    public ListResultModel<List<JavaTempFoldingTabs>> getJavaTempCat(String projectName) {
        ListResultModel<List<JavaTempFoldingTabs>> result = new ListResultModel<List<JavaTempFoldingTabs>>();
        result = ButtonViewPageUtil.getTabList(Arrays.asList(JavaTempCat.values()), JavaTempFoldingTabs.class);
        return result;
    }

}
