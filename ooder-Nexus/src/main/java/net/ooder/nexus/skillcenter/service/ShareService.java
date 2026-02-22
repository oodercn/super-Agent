package net.ooder.nexus.skillcenter.service;

import net.ooder.nexus.skillcenter.dto.ReceivedSkillDTO;
import net.ooder.nexus.skillcenter.dto.SkillShareDTO;

import java.util.List;

/**
 * 技能分享服务接口
 */
public interface ShareService {
    /**
     * 分享技能到群组
     * @param skillId 技能ID
     * @param groupId 群组ID
     * @param message 分享消息
     * @return 是否成功
     */
    boolean shareSkill(String skillId, String groupId, String message);

    /**
     * 获取已分享的技能列表
     * @return 已分享技能列表
     */
    List<SkillShareDTO> getSharedSkills();

    /**
     * 获取接收到的技能列表
     * @return 接收到的技能列表
     */
    List<ReceivedSkillDTO> getReceivedSkills();

    /**
     * 取消分享
     * @param shareId 分享ID
     * @return 是否成功
     */
    boolean unshareSkill(String shareId);
}
