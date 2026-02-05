package net.ooder.test;

import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value = "/demo/")
public class OtherButton {

    @RequestMapping(value = "CreateTable")
    @CustomAnnotation(caption = "创建 数据库表")
    @FieldAnnotation( expression = "this.readOnly!=null")
    @APIEventAnnotation()
    public ResultModel<Boolean> getTestForm(TestView testView) throws DAOException {
        MetadataFactory factory = MetadataFactory.getInstance("fdt");
        TableInfo tableInfo = factory.getInfoByClass(TestView.class);
        factory.createTableByInfo(tableInfo);

        return new ResultModel<>();
    }
}
