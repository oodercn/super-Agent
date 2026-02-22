package net.ooder.nexus.dto.common;

import java.io.Serializable;
import java.util.List;

/**
 * Organization tree DTO
 * Used for displaying organization structure in tree format
 */
public class OrgTreeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Node ID
     */
    private String id;

    /**
     * Node name
     */
    private String name;

    /**
     * Node type: department, user, group
     */
    private String type;

    /**
     * Parent node ID
     */
    private String parentId;

    /**
     * Node icon
     */
    private String icon;

    /**
     * Whether node is expanded
     */
    private Boolean expanded;

    /**
     * Whether node is selected
     */
    private Boolean selected;

    /**
     * Whether node has children
     */
    private Boolean hasChildren;

    /**
     * Node order
     */
    private Integer order;

    /**
     * Additional properties
     */
    private OrgNodeData data;

    /**
     * Child nodes
     */
    private List<OrgTreeDTO> children;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Boolean getExpanded() { return expanded; }
    public void setExpanded(Boolean expanded) { this.expanded = expanded; }
    public Boolean getSelected() { return selected; }
    public void setSelected(Boolean selected) { this.selected = selected; }
    public Boolean getHasChildren() { return hasChildren; }
    public void setHasChildren(Boolean hasChildren) { this.hasChildren = hasChildren; }
    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
    public OrgNodeData getData() { return data; }
    public void setData(OrgNodeData data) { this.data = data; }
    public List<OrgTreeDTO> getChildren() { return children; }
    public void setChildren(List<OrgTreeDTO> children) { this.children = children; }

    /**
     * Organization node data
     */
    public static class OrgNodeData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String email;
        private String mobile;
        private String position;
        private String avatar;
        private Integer status;
        private String description;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
