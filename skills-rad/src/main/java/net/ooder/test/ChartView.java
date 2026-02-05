package net.ooder.test;


import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.fchart.*;
import net.ooder.esd.annotation.field.FChartAnnotation;
import net.ooder.esd.bean.fchart.items.RawDataListItem;
import net.ooder.esd.tool.properties.fchart.RawData;

import java.util.UUID;

@ChartCustomAnnotation()
@ChartCosmeticsAnnotation(bgcolor = "FFFFFF,FFFFFF")
@ChartAnnotation(caption = "测试视图", xaxisname = "X轴名称", yaxisname = "Y轴")
@FChartAnnotation(chartType = FChartType.Column2D, customService = TestService.class, event = CustomFieldEvent.ONDATACLICK)
public class ChartView extends RawDataListItem {


    @RawDataItemAnnotation(showlabel = false)
    public ChartView(RawData rawData) {
        this.setId(UUID.randomUUID().toString());
        this.setLabel(rawData.getLabel());
        this.setValue(rawData.getValue());
        this.addTagVar("test", "test");
    }

}


