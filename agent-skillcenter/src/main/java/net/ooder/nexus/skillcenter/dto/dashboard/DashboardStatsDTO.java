package net.ooder.nexus.skillcenter.dto.dashboard;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class DashboardStatsDTO extends BaseDTO {

    private int totalSkills;
    private int totalMarketSkills;
    private SystemInfoDTO systemInfo;
    private AppInfoDTO appInfo;

    public DashboardStatsDTO() {}

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

    public SystemInfoDTO getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfoDTO systemInfo) {
        this.systemInfo = systemInfo;
    }

    public AppInfoDTO getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfoDTO appInfo) {
        this.appInfo = appInfo;
    }

    public static class SystemInfoDTO {
        private String javaVersion;
        private String osName;
        private String osVersion;
        private String osArch;

        public String getJavaVersion() {
            return javaVersion;
        }

        public void setJavaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getOsArch() {
            return osArch;
        }

        public void setOsArch(String osArch) {
            this.osArch = osArch;
        }
    }

    public static class AppInfoDTO {
        private String version;
        private String name;
        private String description;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
