package net.ooder.nexus.dto.activity;

import java.io.Serializable;

public class ActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String icon;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
