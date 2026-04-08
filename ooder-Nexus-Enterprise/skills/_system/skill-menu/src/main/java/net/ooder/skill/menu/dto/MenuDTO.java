package net.ooder.skill.menu.dto;

import lombok.Data;
import java.util.List;

@Data
public class MenuDTO {
    private Long id;
    private String menuId;
    private String name;
    private String title;
    private String url;
    private String icon;
    private String parentId;
    private Integer sort;
    private String category;
    private String requiredSkill;
    private Boolean visible;
    private Boolean enabled;
    private String description;
    private List<MenuDTO> children;
}
