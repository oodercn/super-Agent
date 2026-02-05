package net.ooder.test;


import net.ooder.annotation.Enumstype;

public enum SexEnum implements Enumstype {
    men("男"),women("女");
    private final String type;
    private final String name;

    SexEnum (String name){
        this.type=name();
        this.name=name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
