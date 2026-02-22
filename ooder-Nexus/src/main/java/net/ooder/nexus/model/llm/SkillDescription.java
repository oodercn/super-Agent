package net.ooder.nexus.model.llm;

import java.util.List;

public class SkillDescription {
    private String skillId;
    private String skillName;
    private String description;
    private String category;
    private List<String> tags;
    private List<SkillParameter> parameters;
    private List<SkillOutput> outputs;
    private SkillMetadata metadata;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<SkillParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<SkillParameter> parameters) {
        this.parameters = parameters;
    }

    public List<SkillOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<SkillOutput> outputs) {
        this.outputs = outputs;
    }

    public SkillMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SkillMetadata metadata) {
        this.metadata = metadata;
    }

    public static class SkillParameter {
        private String name;
        private String type;
        private String description;
        private boolean required;
        private Object defaultValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    public static class SkillOutput {
        private String name;
        private String type;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }

    public static class SkillMetadata {
        private String version;
        private String author;
        private long createTime;
        private long updateTime;
        private int downloadCount;
        private double rating;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }
    }
}
