package net.ooder.tools;

import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.test.TestView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping(value = "/eidtor/")
public class EditorService {
    @RequestMapping(value = "onChange")
    @APIEventAnnotation(customResponseData = ResponsePathEnum.EXPRESSION, customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<Component>> onChange(String fieldName, ModuleComponent moduleComponent) {
        ListResultModel<List<Component>> listResultModel = new ListResultModel();
        List<Component> components = new ArrayList<>();
        List<ComboInputComponent> comboInputComponents = null;
        try {
            comboInputComponents = moduleComponent.getRealModuleComponent().findComponents(ComponentType.COMBOINPUT, null);
            ( comboInputComponents.get(1).getProperties()).setDisabled(true);
            ( comboInputComponents.get(1).getProperties()).setValue(14);
            components.addAll(comboInputComponents);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        listResultModel.setData(components);
        return listResultModel;
    }

}
