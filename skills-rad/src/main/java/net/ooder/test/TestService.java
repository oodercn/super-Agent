package net.ooder.test;

import net.ooder.annotation.Debug;
import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.dao.DAOFactory;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.annotation.view.FChartViewAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.fchart.RawData;
import net.ooder.esd.util.page.RawDataUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/demo/")
@Debug
public class TestService {

    @RequestMapping(value = "DynTestList")
    @DynLoadAnnotation(refClassName = "test.Search")
    @DialogAnnotation
    @ModuleAnnotation()
    @ResponseBody
    public ResultModel<EUModule> getTestList(EUModule module) {
        ResultModel resultModel = new ResultModel();
        module.getComponent().getCustomFunctions().put("test", "function(){}");
        resultModel.setData(module);
        return resultModel;
    }

    @RequestMapping(value = "Chart", method = RequestMethod.POST)
    @FChartViewAnnotation
    @ModuleAnnotation
    @ResponseBody
    @APIEventAnnotation(bindMenu = CustomMenuItem.DATAURL, autoRun = true)
    public ListResultModel<List<ChartView>> getChart() {
        ListResultModel<List<ChartView>> resultModel = new ListResultModel<>();
        List<RawData> rawDatas = new ArrayList<>();
        RawData rawData = new RawData();
        rawData.setLabel("星期一");
        rawData.setValue("3000");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期二");
        rawData.setValue("300");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期三");
        rawData.setValue("400");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期四");
        rawData.setValue("800");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期五");
        rawData.setValue("1000");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期六");
        rawData.setValue("1500");

        rawDatas.add(rawData);
        rawData = new RawData();
        rawData.setLabel("星期日");
        rawData.setValue("2500");
        rawDatas.add(rawData);
        resultModel = RawDataUtil.getDataList(rawDatas, ChartView.class);
        return resultModel;

    }


    @RequestMapping(value = "TestList")
    @APIEventAnnotation(bindMenu = {CustomMenuItem.RELOAD})
    @ResponseBody
    public ListResultModel<List<TestView>> loadTestList() {
        ListResultModel listResultModel = new ListResultModel();
        List<TestView> list = new ArrayList<>();
        TestView testView = new TestView();
        testView.setAge(18);
        testView.setName("wenzhznag");
        list.add(testView);
        listResultModel.setData(list);
        return listResultModel;
    }

    @RequestMapping(value = "Search")
    @GridViewAnnotation
    @ModuleAnnotation
    @ResponseBody
    public ListResultModel<List<TestView>> Search(@RequestBody TestView testView) {
        ListResultModel listResultModel = new ListResultModel();
        List<TestView> list = new ArrayList<>();
        testView = new TestView();
        testView.setAge(18);
        testView.setName("wenzhznag");
        list.add(testView);
        listResultModel.setData(list);
        return listResultModel;
    }


    @RequestMapping(value = "TestForm")
    @FormViewAnnotation()
    @ModuleAnnotation()
    @DialogAnnotation(width = "40em", height = "35em")
    @APIEventAnnotation(customResponseData = ResponsePathEnum.EXPRESSION,
            autoRun = true, bindMenu = {CustomMenuItem.ADD},
            bindFieldEvent = CustomFieldEvent.ONCHANGE)
    @ResponseBody
    public ResultModel<TestView> getTestForm(@RequestBody TestView testView, String projectName) {
        ResultModel resultModel = new ResultModel();
        testView.setName("1111222");
        testView.setAge(15);

        resultModel.setData(testView);
        return resultModel;
    }

    @RequestMapping(value = "SearchTestForm")
    @FormViewAnnotation()
    @ModuleAnnotation
    @ResponseBody
    public ResultModel<TestView> SerchTestForm(@RequestBody TestView testView) {
        ResultModel resultModel = new ResultModel();
        testView.setName("ddddddddddd");
        resultModel.setData(testView);
        return resultModel;
    }

    @RequestMapping(value = "SaveForm")
    @APIEventAnnotation(bindMenu = {CustomMenuItem.SAVE}, callback = {CustomCallBack.RELOADPARENT, CustomCallBack.CLOSE})
    @ResponseBody
    public ResultModel<Boolean> saveForm(@RequestBody TestView testView) {
        ResultModel resultModel = new ResultModel();
        MetadataFactory factory = MetadataFactory.getInstance("fdt");
        try {
            TableInfo tableInfo = factory.getInfoByClass(TestView.class);
            DAOFactory daoFactory = new DAOFactory(tableInfo);
            if (testView.getPersonId() == null || testView.getPersonId().equals("")) {
                testView.setPersonId(UUID.randomUUID().toString());
            }

            daoFactory.getDAO().update(testView);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return resultModel;
    }


}
