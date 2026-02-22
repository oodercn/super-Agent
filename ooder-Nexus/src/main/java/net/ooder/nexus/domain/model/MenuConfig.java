package net.ooder.nexus.domain.model;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单配置实体类
 * 对应 menu-config.json 的根结构
 */
public class MenuConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 批次号 */
    private Integer batch;

    /** 总批次数 */
    private Integer totalBatches;

    /** 描述 */
    private String description;

    /** 菜单列表 */
    private List<Menu> menu;

    public MenuConfig() {
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Integer getTotalBatches() {
        return totalBatches;
    }

    public void setTotalBatches(Integer totalBatches) {
        this.totalBatches = totalBatches;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "MenuConfig{" +
                "batch=" + batch +
                ", totalBatches=" + totalBatches +
                ", description='" + description + '\'' +
                ", menuSize=" + (menu != null ? menu.size() : 0) +
                '}';
    }
}
