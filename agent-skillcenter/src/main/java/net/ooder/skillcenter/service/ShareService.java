package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.ReceivedSkillDTO;
import net.ooder.skillcenter.dto.SkillShareDTO;

import java.util.List;

public interface ShareService {
    boolean shareSkill(String skillId, String groupId, String message);
    List<SkillShareDTO> getSharedSkills();
    List<ReceivedSkillDTO> getReceivedSkills();
    boolean unshareSkill(String shareId);
}
