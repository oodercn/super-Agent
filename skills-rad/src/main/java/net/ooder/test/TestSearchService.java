package net.ooder.test;

import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.custom.component.form.FullClassFormComponent;
import net.ooder.esd.custom.component.grid.FullGridComponent;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping(value = "/view/")
public class TestSearchService {

    @RequestMapping(value = "Search")
    @DynLoadAnnotation()
    @ModuleAnnotation(bindService = SearchTestView.class, caption = "流程展示")
    @ResponseBody
    public ListResultModel<List<Component>> Search(@RequestBody TestView testView) {
        ListResultModel<List<Component>> listResultModel = new ListResultModel<List<Component>>();
        Map<String, Object> valueMap = JDSActionContext.getActionContext().getContext();
        EUModule module = null;
        try {
            module = ESDFacrory.getAdminESDClient().buildDynCustomModule(FullClassFormComponent.class, valueMap, true);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        Map<String, String> test = new HashMap<>();
        test.put("fff", "ffffff");
        test.put("prjectName", "ddddd");
        module.addParams(test);
        List<Component> components = module.getTopComponents(true);
        listResultModel.setData(components);
        return listResultModel;
    }


    @RequestMapping(value = "SearchTest")
    @GridViewAnnotation
    @ModuleAnnotation(bindService = SearchTestView.class)
    @APIEventAnnotation(bindMenu = {CustomMenuItem.RELOAD}, autoRun = true, customRequestData = RequestPathEnum.CTX)
    @ResponseBody
    public ListResultModel<List<SearchTestView>> searchTest(@RequestBody TestView testView, FullGridComponent component) {
        ListResultModel<List<SearchTestView>> resultModel = new ListResultModel();
        List<SearchTestView> list = new ArrayList<>();
        SearchTestView searchTestView = new SearchTestView();
        searchTestView.setAge(18);
        searchTestView.setPersonId("test111");
        searchTestView.setName("wenzhznag");
        list.add(searchTestView);

        Map<String, String> test = new HashMap<>();
        test.put("fff", "ffffff");
        test.put("projectName", "ddddd");
        test.put("name", "dddddddddd");
        component.addParams(test);

        resultModel.setData(list);

        return resultModel;
    }


}
