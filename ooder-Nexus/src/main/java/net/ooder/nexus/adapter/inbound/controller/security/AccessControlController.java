package net.ooder.nexus.adapter.inbound.controller.security;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.service.security.AccessControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 访问控制控制器
 * 提供 IP 白名单/黑名单、API 访问规则、角色权限管理接口
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since 0.7.3
 */
@RestController
@RequestMapping("/api/security/access")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AccessControlController {

    private static final Logger log = LoggerFactory.getLogger(AccessControlController.class);

    @Autowired
    private AccessControlService accessControlService;

    @GetMapping("/whitelist")
    public ResultModel<List<Map<String, Object>>> getWhitelist() {
        log.info("Get whitelist requested");
        try {
            List<Map<String, Object>> data = accessControlService.getWhitelist();
            return ResultModel.success("获取成功", data);
        } catch (Exception e) {
            log.error("Failed to get whitelist", e);
            return ResultModel.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/whitelist")
    public ResultModel<Map<String, Object>> addToWhitelist(@RequestBody Map<String, Object> ipData) {
        log.info("Add to whitelist requested: {}", ipData.get("ip"));
        try {
            Map<String, Object> data = accessControlService.addToWhitelist(ipData);
            return ResultModel.success("添加成功", data);
        } catch (Exception e) {
            log.error("Failed to add to whitelist", e);
            return ResultModel.error("添加失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/whitelist/{id}")
    public ResultModel<Boolean> removeFromWhitelist(@PathVariable String id) {
        log.info("Remove from whitelist requested: {}", id);
        try {
            boolean success = accessControlService.removeFromWhitelist(id);
            if (success) {
                return ResultModel.success("移除成功", true);
            } else {
                return ResultModel.error("记录不存在", 404);
            }
        } catch (Exception e) {
            log.error("Failed to remove from whitelist", e);
            return ResultModel.error("移除失败: " + e.getMessage());
        }
    }

    @GetMapping("/blacklist")
    public ResultModel<List<Map<String, Object>>> getBlacklist() {
        log.info("Get blacklist requested");
        try {
            List<Map<String, Object>> data = accessControlService.getBlacklist();
            return ResultModel.success("获取成功", data);
        } catch (Exception e) {
            log.error("Failed to get blacklist", e);
            return ResultModel.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/blacklist")
    public ResultModel<Map<String, Object>> addToBlacklist(@RequestBody Map<String, Object> ipData) {
        log.info("Add to blacklist requested: {}", ipData.get("ip"));
        try {
            Map<String, Object> data = accessControlService.addToBlacklist(ipData);
            return ResultModel.success("添加成功", data);
        } catch (Exception e) {
            log.error("Failed to add to blacklist", e);
            return ResultModel.error("添加失败: " + e.getMessage());
        }
    }

    @PostMapping("/blacklist/{id}/unban")
    public ResultModel<Boolean> unbanFromBlacklist(@PathVariable String id) {
        log.info("Unban from blacklist requested: {}", id);
        try {
            boolean success = accessControlService.unbanFromBlacklist(id);
            if (success) {
                return ResultModel.success("解封成功", true);
            } else {
                return ResultModel.error("记录不存在", 404);
            }
        } catch (Exception e) {
            log.error("Failed to unban from blacklist", e);
            return ResultModel.error("解封失败: " + e.getMessage());
        }
    }

    @GetMapping("/api-rules")
    public ResultModel<List<Map<String, Object>>> getApiRules() {
        log.info("Get API rules requested");
        try {
            List<Map<String, Object>> data = accessControlService.getApiRules();
            return ResultModel.success("获取成功", data);
        } catch (Exception e) {
            log.error("Failed to get API rules", e);
            return ResultModel.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/api-rules")
    public ResultModel<Map<String, Object>> saveApiRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Save API rule requested: {}", ruleData.get("path"));
        try {
            Map<String, Object> data = accessControlService.saveApiRule(ruleData);
            return ResultModel.success("保存成功", data);
        } catch (Exception e) {
            log.error("Failed to save API rule", e);
            return ResultModel.error("保存失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/api-rules/{id}")
    public ResultModel<Boolean> deleteApiRule(@PathVariable String id) {
        log.info("Delete API rule requested: {}", id);
        try {
            boolean success = accessControlService.deleteApiRule(id);
            if (success) {
                return ResultModel.success("删除成功", true);
            } else {
                return ResultModel.error("规则不存在", 404);
            }
        } catch (Exception e) {
            log.error("Failed to delete API rule", e);
            return ResultModel.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
    public ResultModel<List<Map<String, Object>>> getRoles() {
        log.info("Get roles requested");
        try {
            List<Map<String, Object>> data = accessControlService.getRoles();
            return ResultModel.success("获取成功", data);
        } catch (Exception e) {
            log.error("Failed to get roles", e);
            return ResultModel.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/roles")
    public ResultModel<Map<String, Object>> saveRole(@RequestBody Map<String, Object> roleData) {
        log.info("Save role requested: {}", roleData.get("name"));
        try {
            Map<String, Object> data = accessControlService.saveRole(roleData);
            return ResultModel.success("保存成功", data);
        } catch (Exception e) {
            log.error("Failed to save role", e);
            return ResultModel.error("保存失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/roles/{id}")
    public ResultModel<Boolean> deleteRole(@PathVariable String id) {
        log.info("Delete role requested: {}", id);
        try {
            boolean success = accessControlService.deleteRole(id);
            if (success) {
                return ResultModel.success("删除成功", true);
            } else {
                return ResultModel.error("无法删除系统角色或角色不存在", 400);
            }
        } catch (Exception e) {
            log.error("Failed to delete role", e);
            return ResultModel.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/rules")
    public ResultModel<Map<String, Object>> addRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Add rule requested: type={}", ruleData.get("type"));
        try {
            String type = (String) ruleData.get("type");
            Map<String, Object> data;
            
            if ("whitelist".equals(type)) {
                data = accessControlService.addToWhitelist(ruleData);
            } else if ("blacklist".equals(type)) {
                data = accessControlService.addToBlacklist(ruleData);
            } else {
                return ResultModel.error("无效的规则类型", 400);
            }
            
            return ResultModel.success("添加成功", data);
        } catch (Exception e) {
            log.error("Failed to add rule", e);
            return ResultModel.error("添加失败: " + e.getMessage());
        }
    }
}
