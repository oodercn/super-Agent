package net.ooder.nexus.dto.config;

import java.io.Serializable;

public class ConfigCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String icon;
    private Integer itemCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
}
