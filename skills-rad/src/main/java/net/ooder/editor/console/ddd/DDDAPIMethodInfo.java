package net.ooder.editor.console.ddd;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ResultModel;
import net.ooder.dsm.aggregation.api.method.parameter.MethodParamsMetaGroup;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.FormFieldAnnotation;
import net.ooder.esd.annotation.field.TabsFieldAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.Dock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/rad/ddd/")
@ToolBarMenu(menuClasses = DDDToolBar.class, dock = Dock.top, autoIconColor = false, iconFontSize = "1em")
public class DDDAPIMethodInfo {

    @MethodChinaName(cname = "API信息")
    public ResultModel<DDDMethodBaseFormView> methodView;

    public ResultModel<MethodParamsMetaGroup> apiMethodMetaView;


    @RequestMapping(method = RequestMethod.POST, value = "MethodView")
    @UIAnnotation(height = "120",top = "0em")
    @FormFieldAnnotation()
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ResultModel<DDDMethodBaseFormView> getMethodView() {
        return methodView;
    }

    public void setMethodView(ResultModel<DDDMethodBaseFormView> methodView) {
        this.methodView = methodView;
    }

    @MethodChinaName(cname = "详细信息")
    @RequestMapping(method = RequestMethod.POST, value = "ApiMethodMetaView")
    @TabsFieldAnnotation
    @UIAnnotation()
    @ResponseBody
    public ResultModel<MethodParamsMetaGroup> getApiMethodMetaView() {
        return apiMethodMetaView;
    }

    public void setApiMethodMetaView(ResultModel<MethodParamsMetaGroup> apiMethodMetaView) {
        this.apiMethodMetaView = apiMethodMetaView;
    }


}
