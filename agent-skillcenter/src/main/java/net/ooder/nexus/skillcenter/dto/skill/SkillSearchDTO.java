package net.ooder.nexus.skillcenter.dto.skill;

import java.util.List;

/**
 * 技能搜索请求DTO - 符合v0.7.0协议规范
 */
public class SkillSearchDTO {
    
    private List<String> capabilities;
    private List<String> scenes;
    private List<String> types;
    private List<String> keywords;
    private List<String> sources;
    private String version;
    private int pageNum = 1;
    private int pageSize = 20;

    public SkillSearchDTO() {}

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getScenes() {
        return scenes;
    }

    public void setScenes(List<String> scenes) {
        this.scenes = scenes;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
