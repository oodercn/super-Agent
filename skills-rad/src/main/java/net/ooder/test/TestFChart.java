package net.ooder.test;

import net.ooder.config.ListResultModel;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.tool.properties.fchart.RawData;
import net.ooder.esd.util.page.RawDataUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@RequestMapping(value = "/demo/")
public class TestFChart {

    @RequestMapping(value = "getData")
    @APIEventAnnotation(bindMenu = CustomMenuItem.DATAURL, autoRun = true)
    @ResponseBody
    public ListResultModel<List<ChartView>> getData() {
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
}
