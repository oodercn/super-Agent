package net.ooder.nexus.skillcenter.dto.personal;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class PersonalDashboardStatsDTO extends BaseDTO {

    private int totalSkills;
    private int totalExecutions;
    private int successfulExecutions;
    private int failedExecutions;
    private List<ActivityDTO> recentActivities;

    public PersonalDashboardStatsDTO() {}

    public int getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(int totalSkills) {
        this.totalSkills = totalSkills;
    }

    public int getTotalExecutions() {
        return totalExecutions;
    }

    public void setTotalExecutions(int totalExecutions) {
        this.totalExecutions = totalExecutions;
    }

    public int getSuccessfulExecutions() {
        return successfulExecutions;
    }

    public void setSuccessfulExecutions(int successfulExecutions) {
        this.successfulExecutions = successfulExecutions;
    }

    public int getFailedExecutions() {
        return failedExecutions;
    }

    public void setFailedExecutions(int failedExecutions) {
        this.failedExecutions = failedExecutions;
    }

    public List<ActivityDTO> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<ActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public static class ActivityDTO {
        private String id;
        private String type;
        private String description;
        private String timestamp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
