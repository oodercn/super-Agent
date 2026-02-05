package net.ooder.test;

import net.ooder.annotation.ColType;
import net.ooder.annotation.DBField;
import net.ooder.annotation.DBTable;
import net.ooder.annotation.Required;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.field.ComboListBoxAnnotation;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.menu.GridMenu;
import net.ooder.esd.annotation.ui.AlignType;
import net.ooder.esd.annotation.ui.CmdButtonType;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.tools.EditorService;

import java.util.ArrayList;
import java.util.List;

@GridRowCmd(menuClass = OtherButton.class)
@GridAnnotation(customMenu = {GridMenu.ADD, GridMenu.RELOAD, GridMenu.DELETE}, event = {CustomGridEvent.EDITOR})
@FormAnnotation(col = 1, customService = TestService.class, bottombarMenu = {CustomFormMenu.SAVE, CustomFormMenu.SEARCH})
@DBTable(tableName = "User3", configKey = "fdt", primaryKey = "personId")
public class TestView {
    @CustomAnnotation(hidden = true, uid = true)
    @DBField(dbFieldName = "PERSONID")
    String personId;


    @Required
    @CustomAnnotation(caption = "姓名")
    @FieldAnnotation(expression = "age+name")
    @DBField(dbFieldName = "NAME")
    String name;

    @ComboListBoxAnnotation
    @ComboInputAnnotation()
    @CustomListAnnotation(dynLoad = true)
    @FieldAnnotation(serviceClass = EditorService.class)
    @CustomAnnotation(caption = "性別")
    @DBField(dbFieldName = "SEX")
    SexEnum sex;




    @ComboListBoxAnnotation
    // @JSONField(serializeUsing = ItemSubDeserializer.class)
            List<CmdItem> tagCmds = new ArrayList<>();

    @ComboInputAnnotation()
    @CustomAnnotation(caption = "年龄")
    //@FieldAnnotation(serviceClass = EditorService.class)
    @DBField(dbFieldName = "AGE", dbType = ColType.INTEGER)
    Integer age;


    public List<CmdItem> getTagCmds() {
        tagCmds = new ArrayList<>();
        CmdItem item = new CmdItem("id", "测试", "");
        item.setButtonType(CmdButtonType.button);
        CmdItem item2 = new CmdItem("getTestFormbutton", "测试2", "");
        item.setLocation(AlignType.left);
        item.setTag("row");
        item2.setTag("row");

        CmdItem item3 = new CmdItem("id3", "测试3", "");
        item3.setTag("row");
        tagCmds.add(item);
        tagCmds.add(item2);
        tagCmds.add(item3);
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }


    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
