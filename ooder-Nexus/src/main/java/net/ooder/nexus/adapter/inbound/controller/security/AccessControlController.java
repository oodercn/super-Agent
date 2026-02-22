package net.ooder.nexus.adapter.inbound.controller.security;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.security.AccessControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<Map<String, Object>> data = accessControlService.getWhitelist();
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to get whitelist", e);
            result.setRequestStatus(500);
            result.setMessage("获取失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/whitelist")
    public ResultModel<Map<String, Object>> addToWhitelist(@RequestBody Map<String, Object> ipData) {
        log.info("Add to whitelist requested: {}", ipData.get("ip"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = accessControlService.addToWhitelist(ipData);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to add to whitelist", e);
            result.setRequestStatus(500);
            result.setMessage("添加失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @DeleteMapping("/whitelist/{id}")
    public ResultModel<Boolean> removeFromWhitelist(@PathVariable String id) {
        log.info("Remove from whitelist requested: {}", id);
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        
        try {
            boolean success = accessControlService.removeFromWhitelist(id);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage(success ? "移除成功" : "记录不存在");
            result.setSuccess(success);
        } catch (Exception e) {
            log.error("Failed to remove from whitelist", e);
            result.setRequestStatus(500);
            result.setMessage("移除失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @GetMapping("/blacklist")
    public ResultModel<List<Map<String, Object>>> getBlacklist() {
        log.info("Get blacklist requested");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<Map<String, Object>> data = accessControlService.getBlacklist();
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to get blacklist", e);
            result.setRequestStatus(500);
            result.setMessage("获取失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/blacklist")
    public ResultModel<Map<String, Object>> addToBlacklist(@RequestBody Map<String, Object> ipData) {
        log.info("Add to blacklist requested: {}", ipData.get("ip"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = accessControlService.addToBlacklist(ipData);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to add to blacklist", e);
            result.setRequestStatus(500);
            result.setMessage("添加失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/blacklist/{id}/unban")
    public ResultModel<Boolean> unbanFromBlacklist(@PathVariable String id) {
        log.info("Unban from blacklist requested: {}", id);
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        
        try {
            boolean success = accessControlService.unbanFromBlacklist(id);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage(success ? "解封成功" : "记录不存在");
            result.setSuccess(success);
        } catch (Exception e) {
            log.error("Failed to unban from blacklist", e);
            result.setRequestStatus(500);
            result.setMessage("解封失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @GetMapping("/api-rules")
    public ResultModel<List<Map<String, Object>>> getApiRules() {
        log.info("Get API rules requested");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<Map<String, Object>> data = accessControlService.getApiRules();
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to get API rules", e);
            result.setRequestStatus(500);
            result.setMessage("获取失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/api-rules")
    public ResultModel<Map<String, Object>> saveApiRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Save API rule requested: {}", ruleData.get("path"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = accessControlService.saveApiRule(ruleData);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("保存成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to save API rule", e);
            result.setRequestStatus(500);
            result.setMessage("保存失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @DeleteMapping("/api-rules/{id}")
    public ResultModel<Boolean> deleteApiRule(@PathVariable String id) {
        log.info("Delete API rule requested: {}", id);
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        
        try {
            boolean success = accessControlService.deleteApiRule(id);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage(success ? "删除成功" : "规则不存在");
            result.setSuccess(success);
        } catch (Exception e) {
            log.error("Failed to delete API rule", e);
            result.setRequestStatus(500);
            result.setMessage("删除失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @GetMapping("/roles")
    public ResultModel<List<Map<String, Object>>> getRoles() {
        log.info("Get roles requested");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<Map<String, Object>> data = accessControlService.getRoles();
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to get roles", e);
            result.setRequestStatus(500);
            result.setMessage("获取失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/roles")
    public ResultModel<Map<String, Object>> saveRole(@RequestBody Map<String, Object> roleData) {
        log.info("Save role requested: {}", roleData.get("name"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = accessControlService.saveRole(roleData);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("保存成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to save role", e);
            result.setRequestStatus(500);
            result.setMessage("保存失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @DeleteMapping("/roles/{id}")
    public ResultModel<Boolean> deleteRole(@PathVariable String id) {
        log.info("Delete role requested: {}", id);
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        
        try {
            boolean success = accessControlService.deleteRole(id);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage(success ? "删除成功" : "无法删除系统角色或角色不存在");
            result.setSuccess(success);
        } catch (Exception e) {
            log.error("Failed to delete role", e);
            result.setRequestStatus(500);
            result.setMessage("删除失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }

    @PostMapping("/rules")
    public ResultModel<Map<String, Object>> addRule(@RequestBody Map<String, Object> ruleData) {
        log.info("Add rule requested: type={}", ruleData.get("type"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            String type = (String) ruleData.get("type");
            Map<String, Object> data;
            
            if ("whitelist".equals(type)) {
                data = accessControlService.addToWhitelist(ruleData);
            } else if ("blacklist".equals(type)) {
                data = accessControlService.addToBlacklist(ruleData);
            } else {
                result.setRequestStatus(400);
                result.setMessage("无效的规则类型");
                result.setSuccess(false);
                return result;
            }
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed to add rule", e);
            result.setRequestStatus(500);
            result.setMessage("添加失败: " + e.getMessage());
            result.setSuccess(false);
        }
        
        return result;
    }
}
