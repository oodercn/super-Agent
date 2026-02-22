package net.ooder.nexus.skillcenter.dto.admin;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class AdminDashboardStatsDTO extends BaseDTO {

    private int totalSkills;
    private int totalMarketSkills;
    private int totalUsers;
    private int activeUsers;
    private int totalExecutions;
    private int successfulExecutions;
    private int failedExecutions;
    private double successRate;
    private int sharedSkills;
    private SystemInfoDTO systemInfo;
    private List<ActivityDTO> recentActivities;

    public AdminDashboardStatsDTO() {}

    public int getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(int totalSkills) {
        this.totalSkills = totalSkills;
    }

    public int getTotalMarketSkills() {
        return totalMarketSkills;
    }

    public void setTotalMarketSkills(int totalMarketSkills) {
        this.totalMarketSkills = totalMarketSkills;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
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

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public int getSharedSkills() {
        return sharedSkills;
    }

    public void setSharedSkills(int sharedSkills) {
        this.sharedSkills = sharedSkills;
    }

    public SystemInfoDTO getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfoDTO systemInfo) {
        this.systemInfo = systemInfo;
    }

    public List<ActivityDTO> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<ActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public static class SystemInfoDTO {
        private double cpuUsage;
        private double memoryUsage;
        private String totalMemory;
        private String usedMemory;
        private String freeMemory;
        private double systemLoad;
        private int availableProcessors;

        public double getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public double getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(double memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public String getTotalMemory() {
            return totalMemory;
        }

        public void setTotalMemory(String totalMemory) {
            this.totalMemory = totalMemory;
        }

        public String getUsedMemory() {
            return usedMemory;
        }

        public void setUsedMemory(String usedMemory) {
            this.usedMemory = usedMemory;
        }

        public String getFreeMemory() {
            return freeMemory;
        }

        public void setFreeMemory(String freeMemory) {
            this.freeMemory = freeMemory;
        }

        public double getSystemLoad() {
            return systemLoad;
        }

        public void setSystemLoad(double systemLoad) {
            this.systemLoad = systemLoad;
        }

        public int getAvailableProcessors() {
            return availableProcessors;
        }

        public void setAvailableProcessors(int availableProcessors) {
            this.availableProcessors = availableProcessors;
        }
    }

    public static class ActivityDTO {
        private String id;
        private String type;
        private String skillName;
        private String userId;
        private String timestamp;
        private String status;

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

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
