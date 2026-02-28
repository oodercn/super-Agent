package net.ooder.nexus.dto.system;

import java.io.Serializable;
import java.util.List;

public class SkillServicesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<SkillServiceStatusDTO> skills;
    private Integer total;
    private Integer active;
    private Integer inactive;

    public List<SkillServiceStatusDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillServiceStatusDTO> skills) {
        this.skills = skills;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getInactive() {
        return inactive;
    }

    public void setInactive(Integer inactive) {
        this.inactive = inactive;
    }

    public static class SkillServiceStatusDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String name;
        private String status;
        private String lastActive;
        private Integer executions;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLastActive() {
            return lastActive;
        }

        public void setLastActive(String lastActive) {
            this.lastActive = lastActive;
        }

        public Integer getExecutions() {
            return executions;
        }

        public void setExecutions(Integer executions) {
            this.executions = executions;
        }
    }
}
