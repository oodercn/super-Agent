package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.service.ShareService;
import net.ooder.skillcenter.dto.ReceivedSkillDTO;
import net.ooder.skillcenter.dto.SkillShareDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class ShareServiceSdkImpl implements ShareService {

    private final List<SkillShareDTO> sharedSkills = new ArrayList<>();
    private final List<ReceivedSkillDTO> receivedSkills = new ArrayList<>();

    @Override
    public boolean shareSkill(String skillId, String groupId, String message) {
        SkillShareDTO share = new SkillShareDTO();
        share.setSkillId(skillId);
        share.setGroupId(groupId);
        share.setMessage(message);
        share.setSharedAt(new Date());
        sharedSkills.add(share);
        return true;
    }

    @Override
    public List<SkillShareDTO> getSharedSkills() {
        return new ArrayList<>(sharedSkills);
    }

    @Override
    public List<ReceivedSkillDTO> getReceivedSkills() {
        return new ArrayList<>(receivedSkills);
    }

    @Override
    public boolean unshareSkill(String shareId) {
        sharedSkills.removeIf(s -> shareId.equals(s.getId()));
        return true;
    }
}
