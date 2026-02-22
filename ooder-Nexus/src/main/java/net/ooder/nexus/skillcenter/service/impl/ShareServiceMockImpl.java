package net.ooder.nexus.skillcenter.service.impl;

import net.ooder.nexus.skillcenter.dto.ReceivedSkillDTO;
import net.ooder.nexus.skillcenter.dto.SkillShareDTO;
import net.ooder.nexus.skillcenter.service.ShareService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能分享服务模拟实现
 */
@Service
public class ShareServiceMockImpl implements ShareService {

    private final Map<String, SkillShareDTO> shareStore = new ConcurrentHashMap<>();

    @Override
    public boolean shareSkill(String skillId, String groupId, String message) {
        SkillShareDTO share = new SkillShareDTO();
        share.setId("share-" + UUID.randomUUID().toString().substring(0, 8));
        share.setSkillId(skillId);
        share.setSkillName(getSkillNameById(skillId));
        share.setGroupId(groupId);
        share.setGroupName(getGroupNameById(groupId));
        share.setMessage(message != null ? message : "分享了一个技能");
        share.setSharedAt(new Date());
        share.setStatus("shared");
        shareStore.put(share.getId(), share);
        return true;
    }

    @Override
    public List<SkillShareDTO> getSharedSkills() {
        return new ArrayList<>(shareStore.values());
    }

    @Override
    public List<ReceivedSkillDTO> getReceivedSkills() {
        List<ReceivedSkillDTO> received = new ArrayList<>();
        
        ReceivedSkillDTO skill1 = new ReceivedSkillDTO();
        skill1.setId("receive-001");
        skill1.setSkillId("text-analyzer");
        skill1.setSkillName("文本分析");
        skill1.setSharerId("user123");
        skill1.setSharerName("张三");
        skill1.setGroupId("group-001");
        skill1.setGroupName("开发团队");
        skill1.setReceivedAt(new Date());
        skill1.setMessage("分享一个文本分析工具");
        skill1.setStatus("received");
        received.add(skill1);
        
        ReceivedSkillDTO skill2 = new ReceivedSkillDTO();
        skill2.setId("receive-002");
        skill2.setSkillId("openwrt-network-control");
        skill2.setSkillName("OpenWrt网络控制");
        skill2.setSharerId("user456");
        skill2.setSharerName("李四");
        skill2.setGroupId("group-002");
        skill2.setGroupName("网络管理组");
        skill2.setReceivedAt(new Date());
        skill2.setMessage("这个网络控制插件很好用");
        skill2.setStatus("received");
        received.add(skill2);
        
        return received;
    }

    @Override
    public boolean unshareSkill(String shareId) {
        return shareStore.remove(shareId) != null;
    }

    private String getSkillNameById(String skillId) {
        Map<String, String> skillNames = new HashMap<>();
        skillNames.put("text-analyzer", "文本分析");
        skillNames.put("openwrt-network-control", "OpenWrt网络控制");
        skillNames.put("parental-control", "家长上网控制");
        skillNames.put("smart-bandwidth", "智能带宽管理");
        return skillNames.getOrDefault(skillId, "未知技能");
    }

    private String getGroupNameById(String groupId) {
        Map<String, String> groupNames = new HashMap<>();
        groupNames.put("group-001", "开发团队");
        groupNames.put("group-002", "网络管理组");
        groupNames.put("group-003", "家庭组");
        return groupNames.getOrDefault(groupId, "未知群组");
    }
}
