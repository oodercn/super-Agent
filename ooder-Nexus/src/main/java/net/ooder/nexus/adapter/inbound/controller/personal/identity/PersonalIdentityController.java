package net.ooder.nexus.adapter.inbound.controller.personal.identity;

import net.ooder.nexus.common.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人身份信息控制器
 *
 * <p>提供个人身份信息管理相关接口（获取、更新）</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/personal/identity")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalIdentityController {

    /**
     * 获取个人身份信息
     *
     * @return 身份信息
     */
    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getIdentity() {
        try {
            Map<String, Object> identity = new HashMap<>();
            identity.put("userId", "user-001");
            identity.put("username", "ooderUser");
            identity.put("nickname", "Ooder用户");
            identity.put("email", "user@ooder.net");
            identity.put("phone", "138****8888");
            identity.put("avatar", "/images/avatar/default.png");
            identity.put("bio", "ooder平台用户");
            identity.put("location", "中国");
            identity.put("website", "https://ooder.net");
            identity.put("createTime", System.currentTimeMillis() - 86400000L * 30);
            identity.put("lastLoginTime", System.currentTimeMillis());
            identity.put("status", "active");

            Map<String, Object> stats = new HashMap<>();
            stats.put("skillCount", 10);
            stats.put("sharedCount", 5);
            stats.put("receivedCount", 3);
            stats.put("executionCount", 100);
            identity.put("stats", stats);

            return ResultModel.success(identity);
        } catch (Exception e) {
            return ResultModel.error("Failed to get identity: " + e.getMessage(), 500);
        }
    }

    /**
     * 更新个人身份信息
     *
     * @param request 包含 nickname、email、phone、bio 等的请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateIdentity(@RequestBody Map<String, Object> request) {
        try {
            String nickname = (String) request.get("nickname");
            String email = (String) request.get("email");
            String phone = (String) request.get("phone");
            String bio = (String) request.get("bio");
            String location = (String) request.get("location");
            String website = (String) request.get("website");
            String avatar = (String) request.get("avatar");

            Map<String, Object> identity = new HashMap<>();
            identity.put("userId", "user-001");
            identity.put("username", "ooderUser");
            identity.put("nickname", nickname != null ? nickname : "Ooder用户");
            identity.put("email", email != null ? email : "user@ooder.net");
            identity.put("phone", phone != null ? phone : "138****8888");
            identity.put("avatar", avatar != null ? avatar : "/images/avatar/default.png");
            identity.put("bio", bio != null ? bio : "ooder平台用户");
            identity.put("location", location != null ? location : "中国");
            identity.put("website", website != null ? website : "https://ooder.net");
            identity.put("updateTime", System.currentTimeMillis());
            identity.put("status", "active");

            return ResultModel.success(identity);
        } catch (Exception e) {
            return ResultModel.error("Failed to update identity: " + e.getMessage(), 500);
        }
    }

    /**
     * 修改密码
     *
     * @param request 包含 oldPassword 和 newPassword 的请求
     * @return 修改结果
     */
    @PostMapping("/password")
    @ResponseBody
    public ResultModel<Boolean> changePassword(@RequestBody Map<String, String> request) {
        try {
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                return ResultModel.error("Old password and new password are required", 400);
            }

            return ResultModel.success(true);
        } catch (Exception e) {
            return ResultModel.error("Failed to change password: " + e.getMessage(), 500);
        }
    }

    /**
     * 上传头像
     *
     * @param request 包含 avatarData 的请求
     * @return 上传结果
     */
    @PostMapping("/avatar")
    @ResponseBody
    public ResultModel<Map<String, String>> uploadAvatar(@RequestBody Map<String, String> request) {
        try {
            String avatarData = request.get("avatarData");

            if (avatarData == null) {
                return ResultModel.error("Avatar data is required", 400);
            }

            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", "/images/avatar/user-001.png");
            result.put("thumbnailUrl", "/images/avatar/user-001-thumb.png");

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to upload avatar: " + e.getMessage(), 500);
        }
    }

    /**
     * 获取账户安全信息
     *
     * @return 安全信息
     */
    @PostMapping("/security")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSecurityInfo() {
        try {
            Map<String, Object> security = new HashMap<>();
            security.put("twoFactorEnabled", false);
            security.put("emailVerified", true);
            security.put("phoneVerified", false);
            security.put("lastPasswordChange", System.currentTimeMillis() - 86400000L * 7);
            security.put("loginNotifications", true);
            security.put("suspiciousActivityAlerts", true);

            Map<String, Object> devices = new HashMap<>();
            devices.put("currentDevice", "Chrome on Windows");
            devices.put("lastLoginIp", "192.168.1.1");
            devices.put("lastLoginLocation", "中国, 北京");
            security.put("recentActivity", devices);

            return ResultModel.success(security);
        } catch (Exception e) {
            return ResultModel.error("Failed to get security info: " + e.getMessage(), 500);
        }
    }
}
