package net.ooder.nexus.dto.skill;

import java.io.Serializable;

/**
 * 技能操作请求DTO
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class SkillOperationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String operation;
    private String params;

    public SkillOperationDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
