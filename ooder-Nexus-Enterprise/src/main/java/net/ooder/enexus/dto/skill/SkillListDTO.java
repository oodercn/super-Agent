package net.ooder.enexus.dto.skill;

import net.ooder.enexus.skill.registry.SkillRegistry;
import java.util.List;

public class SkillListDTO {
    private Integer total;
    private Integer systemCount;
    private Integer installedCount;
    private List<SkillRegistry.SkillInfo> skills;
    private List<SkillRegistry.SkillInfo> systemSkills;
    private List<SkillRegistry.SkillInfo> installedSkills;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(Integer systemCount) {
        this.systemCount = systemCount;
    }

    public Integer getInstalledCount() {
        return installedCount;
    }

    public void setInstalledCount(Integer installedCount) {
        this.installedCount = installedCount;
    }

    public List<SkillRegistry.SkillInfo> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillRegistry.SkillInfo> skills) {
        this.skills = skills;
    }

    public List<SkillRegistry.SkillInfo> getSystemSkills() {
        return systemSkills;
    }

    public void setSystemSkills(List<SkillRegistry.SkillInfo> systemSkills) {
        this.systemSkills = systemSkills;
    }

    public List<SkillRegistry.SkillInfo> getInstalledSkills() {
        return installedSkills;
    }

    public void setInstalledSkills(List<SkillRegistry.SkillInfo> installedSkills) {
        this.installedSkills = installedSkills;
    }
}
