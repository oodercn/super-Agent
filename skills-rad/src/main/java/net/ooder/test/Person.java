package net.ooder.test;

import net.ooder.esd.annotation.FormAnnotation;

@FormAnnotation //表单视图转换
public class Person {
    String name;
    String account;
    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
