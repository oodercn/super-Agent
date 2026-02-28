package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class PersonalStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer skillCount;
    private Integer executionCount;
    private Integer sharedCount;
    private Integer groupCount;

    public Integer getSkillCount() {
        return skillCount;
    }

    public void setSkillCount(Integer skillCount) {
        this.skillCount = skillCount;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public Integer getSharedCount() {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }
}
