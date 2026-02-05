package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.manager.GroupManager;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 技能分享REST API控制器
 */
@RestController
@RequestMapping("/api/share")
public class ShareController {

    private final SkillManager skillManager;
    private final GroupManager groupManager;
    private final Map<String, SkillShare> shareMap;

    /**
     * 构造方法，初始化管理器
     */
    public ShareController() {
        this.skillManager = SkillManager.getInstance();
        this.groupManager = GroupManager.getInstance();
        this.shareMap = new HashMap<>();
    }

    /**
     * 分享技能
     * @param request 分享请求
     * @return 分享结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> shareSkill(@RequestBody ShareRequest request) {
        try {
            if (request.getSkillId() == null || request.getGroupId() == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "Skill ID and Group ID are required"));
            }

            // 验证技能是否存在
            if (skillManager.getSkill(request.getSkillId()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "Skill not found"));
            }

            // 验证群组是否存在
            if (groupManager.getGroup(request.getGroupId()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "Group not found"));
            }

            // 创建分享记录
            SkillShare share = new SkillShare();
            share.setId("share-" + UUID.randomUUID().toString().substring(0, 8));
            share.setSkillId(request.getSkillId());
            share.setGroupId(request.getGroupId());
            share.setMessage(request.getMessage() != null ? request.getMessage() : "分享了一个技能");
            share.setSharedAt(LocalDateTime.now());
            share.setStatus("shared");

            // 保存分享记录
            shareMap.put(share.getId(), share);

            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Failed to share skill: " + e.getMessage()));
        }
    }

    /**
     * 获取已分享的技能
     * @return 已分享技能列表
     */
    @GetMapping("/shared")
    public ResponseEntity<ApiResponse<List<SkillShare>>> getSharedSkills() {
        try {
            List<SkillShare> sharedSkills = new ArrayList<>(shareMap.values());
            return ResponseEntity.ok(ApiResponse.success(sharedSkills));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Failed to get shared skills: " + e.getMessage()));
        }
    }

    /**
     * 获取收到的技能
     * @return 收到的技能列表
     */
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<ReceivedSkill>>> getReceivedSkills() {
        try {
            // 模拟收到的技能数据
            List<ReceivedSkill> receivedSkills = new ArrayList<>();
            
            ReceivedSkill skill1 = new ReceivedSkill();
            skill1.setId("receive-001");
            skill1.setSkillId("text-analyzer");
            skill1.setSkillName("文本分析");
            skill1.setSharerId("user123");
            skill1.setSharerName("张三");
            skill1.setGroupId("group-001");
            skill1.setGroupName("Development Team");
            skill1.setReceivedAt(LocalDateTime.now().minusHours(2));
            skill1.setMessage("分享一个文本分析工具，挺好用的");
            skill1.setStatus("received");
            receivedSkills.add(skill1);

            ReceivedSkill skill2 = new ReceivedSkill();
            skill2.setId("receive-002");
            skill2.setSkillId("image-resizer");
            skill2.setSkillName("图片 resize");
            skill2.setSharerId("user456");
            skill2.setSharerName("李四");
            skill2.setGroupId("group-002");
            skill2.setGroupName("Design Team");
            skill2.setReceivedAt(LocalDateTime.now().minusDays(1));
            skill2.setMessage("市场团队的图片处理工具");
            skill2.setStatus("received");
            receivedSkills.add(skill2);

            return ResponseEntity.ok(ApiResponse.success(receivedSkills));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Failed to get received skills: " + e.getMessage()));
        }
    }

    /**
     * 取消分享
     * @param shareId 分享ID
     * @return 取消分享结果
     */
    @DeleteMapping("/{shareId}")
    public ResponseEntity<ApiResponse<Boolean>> unshareSkill(@PathVariable String shareId) {
        try {
            if (shareId == null || shareId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "Share ID is required"));
            }

            boolean result = shareMap.remove(shareId) != null;
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "Share not found"));
            }

            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Failed to unshare skill: " + e.getMessage()));
        }
    }

    /**
     * 分享请求体
     */
    static class ShareRequest {
        private String skillId;
        private String groupId;
        private String message;

        // Getters and setters
        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 技能分享类
     */
    public static class SkillShare {
        private String id;
        private String skillId;
        private String groupId;
        private String message;
        private LocalDateTime sharedAt;
        private String status;

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getSharedAt() {
            return sharedAt;
        }

        public void setSharedAt(LocalDateTime sharedAt) {
            this.sharedAt = sharedAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    /**
     * 收到的技能类
     */
    public static class ReceivedSkill {
        private String id;
        private String skillId;
        private String skillName;
        private String sharerId;
        private String sharerName;
        private String groupId;
        private String groupName;
        private LocalDateTime receivedAt;
        private String message;
        private String status;

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getSharerId() {
            return sharerId;
        }

        public void setSharerId(String sharerId) {
            this.sharerId = sharerId;
        }

        public String getSharerName() {
            return sharerName;
        }

        public void setSharerName(String sharerName) {
            this.sharerName = sharerName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public LocalDateTime getReceivedAt() {
            return receivedAt;
        }

        public void setReceivedAt(LocalDateTime receivedAt) {
            this.receivedAt = receivedAt;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
