package net.ooder.nexus.skillcenter.dto.skill;

import java.util.List;

public class DiscoveryScanResultDTO {
    private String source;
    private int skillCount;
    private List<SkillSearchResultDTO.SkillListItemDTO> skills;
    private long scanTime;

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public int getSkillCount() { return skillCount; }
    public void setSkillCount(int skillCount) { this.skillCount = skillCount; }
    public List<SkillSearchResultDTO.SkillListItemDTO> getSkills() { return skills; }
    public void setSkills(List<SkillSearchResultDTO.SkillListItemDTO> skills) { this.skills = skills; }
    public long getScanTime() { return scanTime; }
    public void setScanTime(long scanTime) { this.scanTime = scanTime; }
}
