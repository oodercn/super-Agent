package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.common.model.ApiResponse;
import net.ooder.nexus.skillcenter.dto.ReceivedSkillDTO;
import net.ooder.nexus.skillcenter.dto.SkillShareDTO;
import net.ooder.nexus.skillcenter.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 技能分享REST API控制器
 */
@RestController
@RequestMapping("/api/skillcenter/share")
public class ShareController {

    private final ShareService shareService;

    @Autowired
    public ShareController(ShareService shareService) {
        this.shareService = shareService;
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
                        .body(ApiResponse.error("400", "Skill ID and Group ID are required"));
            }

            boolean result = shareService.shareSkill(request.getSkillId(), request.getGroupId(), request.getMessage());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("500", "Failed to share skill: " + e.getMessage()));
        }
    }

    /**
     * 获取已分享的技能
     * @return 已分享技能列表
     */
    @GetMapping("/shared")
    public ResponseEntity<ApiResponse<List<SkillShareDTO>>> getSharedSkills() {
        try {
            List<SkillShareDTO> sharedSkills = shareService.getSharedSkills();
            return ResponseEntity.ok(ApiResponse.success(sharedSkills));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("500", "Failed to get shared skills: " + e.getMessage()));
        }
    }

    /**
     * 获取收到的技能
     * @return 收到的技能列表
     */
    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<ReceivedSkillDTO>>> getReceivedSkills() {
        try {
            List<ReceivedSkillDTO> receivedSkills = shareService.getReceivedSkills();
            return ResponseEntity.ok(ApiResponse.success(receivedSkills));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("500", "Failed to get received skills: " + e.getMessage()));
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
                        .body(ApiResponse.error("400", "Share ID is required"));
            }

            boolean result = shareService.unshareSkill(shareId);
            if (!result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("404", "Share not found"));
            }

            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("500", "Failed to unshare skill: " + e.getMessage()));
        }
    }

    /**
     * 分享请求体
     */
    public static class ShareRequest {
        private String skillId;
        private String groupId;
        private String message;

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
}
