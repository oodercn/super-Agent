package net.ooder.test;


import net.ooder.annotation.Required;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComboInputType;

@FormAnnotation(col = 1, bottombarMenu = {CustomFormMenu.SEARCH})
public class SearchTestView {

    @CustomAnnotation(hidden = true, uid = true)
    String personId;
    @Required
    @CustomAnnotation(caption = "姓名", pid = true)
    String name;

    @CustomAnnotation(caption = "性別")
    SexEnum sex;

    @ComboInputAnnotation(inputType = ComboInputType.spin)
    @CustomAnnotation(caption = "年龄")
    Integer age;

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
