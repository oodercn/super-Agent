package net.ooder.nexus.domain.model;

import java.io.Serializable;

/**
 * 菜单角标实体类
 */
public class MenuBadge implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角标文本 */
    private String text;

    /** 角标颜色 */
    private String color;

    public MenuBadge() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "MenuBadge{" +
                "text='" + text + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
