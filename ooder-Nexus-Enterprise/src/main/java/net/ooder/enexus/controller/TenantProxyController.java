package net.ooder.enexus.controller;

import net.ooder.skill.tenant.context.TenantContext;
import net.ooder.skill.tenant.entity.Tenant;
import net.ooder.skill.tenant.entity.TenantMember;
import net.ooder.skill.tenant.model.*;
import net.ooder.skill.tenant.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantProxyController {

    private static final Logger log = LoggerFactory.getLogger(TenantProxyController.class);

    @Autowired
    private TenantService tenantService;

    @GetMapping
    public ResultModel<List<Tenant>> listTenants(@RequestParam(required = false) String userId) {
        log.info("[TenantProxyController] listTenants: userId={}", userId);
        try {
            String effectiveUserId = userId != null ? userId : TenantContext.getUserId();
            List<Tenant> tenants = tenantService.listTenants(effectiveUserId);
            return ResultModel.success(tenants);
        } catch (Exception e) {
            log.error("[TenantProxyController] listTenants failed", e);
            return ResultModel.error("获取租户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResultModel<Tenant> getTenant(@PathVariable String id) {
        log.info("[TenantProxyController] getTenant: id={}", id);
        try {
            Tenant tenant = tenantService.getTenant(id);
            if (tenant == null) {
                return ResultModel.error("租户不存在: " + id);
            }
            return ResultModel.success(tenant);
        } catch (Exception e) {
            log.error("[TenantProxyController] getTenant failed", e);
            return ResultModel.error("获取租户失败: " + e.getMessage());
        }
    }

    @PostMapping
    public ResultModel<Tenant> createTenant(@RequestBody TenantCreateRequest request) {
        log.info("[TenantProxyController] createTenant: name={}", request.getName());
        try {
            Tenant tenant = Tenant.builder()
                    .name(request.getName())
                    .code(request.getCode())
                    .description(request.getDescription())
                    .planType(request.getPlanType() != null ? request.getPlanType() : "FREE")
                    .maxUsers(request.getMaxUsers())
                    .maxStorageMB(request.getMaxStorageMB())
                    .domain(request.getDomain())
                    .build();
            Tenant created = tenantService.createTenant(tenant, TenantContext.getUserId());
            return ResultModel.success(created);
        } catch (Exception e) {
            log.error("[TenantProxyController] createTenant failed", e);
            return ResultModel.error("创建租户失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResultModel<Tenant> updateTenant(@PathVariable String id, @RequestBody TenantUpdateRequest request) {
        log.info("[TenantProxyController] updateTenant: id={}", id);
        try {
            Tenant updates = new Tenant();
            if (request.getName() != null) updates.setName(request.getName());
            if (request.getDescription() != null) updates.setDescription(request.getDescription());
            if (request.getPlanType() != null) updates.setPlanType(request.getPlanType());
            if (request.getMaxUsers() != null) updates.setMaxUsers(request.getMaxUsers());
            if (request.getMaxStorageMB() != null) updates.setMaxStorageMB(request.getMaxStorageMB());
            if (request.getDomain() != null) updates.setDomain(request.getDomain());
            
            Tenant updated = tenantService.updateTenant(id, updates);
            return ResultModel.success(updated);
        } catch (Exception e) {
            log.error("[TenantProxyController] updateTenant failed", e);
            return ResultModel.error("更新租户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResultModel<Void> deleteTenant(@PathVariable String id) {
        log.info("[TenantProxyController] deleteTenant: id={}", id);
        try {
            tenantService.deleteTenant(id);
            return ResultModel.success(null);
        } catch (Exception e) {
            log.error("[TenantProxyController] deleteTenant failed", e);
            return ResultModel.error("删除租户失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/members")
    public ResultModel<List<TenantMember>> getMembers(@PathVariable String id) {
        log.info("[TenantProxyController] getMembers: tenantId={}", id);
        try {
            List<TenantMember> members = tenantService.listMembers(id);
            return ResultModel.success(members);
        } catch (Exception e) {
            log.error("[TenantProxyController] getMembers failed", e);
            return ResultModel.error("获取成员列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/members")
    public ResultModel<Void> addMember(@PathVariable String id, @RequestBody Map<String, String> request) {
        log.info("[TenantProxyController] addMember: tenantId={}", id);
        try {
            String userId = request.get("userId");
            String role = request.getOrDefault("role", "MEMBER");
            tenantService.addMember(id, userId, role);
            return ResultModel.success(null);
        } catch (Exception e) {
            log.error("[TenantProxyController] addMember failed", e);
            return ResultModel.error("添加成员失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResultModel<Void> removeMember(@PathVariable String id, @PathVariable String userId) {
        log.info("[TenantProxyController] removeMember: tenantId={}, userId={}", id, userId);
        try {
            tenantService.removeMember(id, userId);
            return ResultModel.success(null);
        } catch (Exception e) {
            log.error("[TenantProxyController] removeMember failed", e);
            return ResultModel.error("移除成员失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/quota")
    public ResultModel<Map<String, Object>> getQuota(@PathVariable String id) {
        log.info("[TenantProxyController] getQuota: tenantId={}", id);
        try {
            Map<String, Object> quota = tenantService.getQuota(id);
            return ResultModel.success(quota);
        } catch (Exception e) {
            log.error("[TenantProxyController] getQuota failed", e);
            return ResultModel.error("获取配额失败: " + e.getMessage());
        }
    }
}
