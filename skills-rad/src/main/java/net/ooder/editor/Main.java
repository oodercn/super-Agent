package net.ooder.editor;

import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.view.LayoutViewAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/rad/")
@Controller
public class Main {

    @RequestMapping("Main")
    @LayoutViewAnnotation
    @ModuleAnnotation
    @ResponseBody
    public ResultModel<FramePanel> getMain() {
        ResultModel<FramePanel> mainPanelResultModel = new ResultModel<>();
        return mainPanelResultModel;
    }


}
