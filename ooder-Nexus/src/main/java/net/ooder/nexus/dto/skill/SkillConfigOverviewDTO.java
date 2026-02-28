package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.List;

public class SkillConfigOverviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalSkills;
    private Integer configuredSkills;
    private Integer activeSkills;
    private List<SkillSummaryDTO> skills;

    public Integer getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(Integer totalSkills) {
        this.totalSkills = totalSkills;
    }

    public Integer getConfiguredSkills() {
        return configuredSkills;
    }

    public void setConfiguredSkills(Integer configuredSkills) {
        this.configuredSkills = configuredSkills;
    }

    public Integer getActiveSkills() {
        return activeSkills;
    }

    public void setActiveSkills(Integer activeSkills) {
        this.activeSkills = activeSkills;
    }

    public List<SkillSummaryDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillSummaryDTO> skills) {
        this.skills = skills;
    }

    public static class SkillSummaryDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String skillId;
        private String name;
        private String status;
        private Boolean configured;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Boolean getConfigured() {
            return configured;
        }

        public void setConfigured(Boolean configured) {
            this.configured = configured;
        }
    }
}
