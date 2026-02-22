package net.ooder.nexus.model.llm;

import java.util.List;
import java.util.Map;

public class UserPreferences {
    private String userId;
    private Map<String, Object> preferences;
    private List<String> favoriteSkills;
    private List<String> recentSkills;
    private Map<String, SkillConfig> skillConfigs;
    private String apiToken;
    private String defaultCategory;
    private String skillSort;
    private String theme;
    private String language;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Object> preferences) {
        this.preferences = preferences;
    }

    public List<String> getFavoriteSkills() {
        return favoriteSkills;
    }

    public void setFavoriteSkills(List<String> favoriteSkills) {
        this.favoriteSkills = favoriteSkills;
    }

    public List<String> getRecentSkills() {
        return recentSkills;
    }

    public void setRecentSkills(List<String> recentSkills) {
        this.recentSkills = recentSkills;
    }

    public Map<String, SkillConfig> getSkillConfigs() {
        return skillConfigs;
    }

    public void setSkillConfigs(Map<String, SkillConfig> skillConfigs) {
        this.skillConfigs = skillConfigs;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getSkillSort() {
        return skillSort;
    }

    public void setSkillSort(String skillSort) {
        this.skillSort = skillSort;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static class SkillConfig {
        private String skillId;
        private Map<String, Object> config;
        private boolean autoExecute;
        private int timeout;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public Map<String, Object> getConfig() {
            return config;
        }

        public void setConfig(Map<String, Object> config) {
            this.config = config;
        }

        public boolean isAutoExecute() {
            return autoExecute;
        }

        public void setAutoExecute(boolean autoExecute) {
            this.autoExecute = autoExecute;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }
}
