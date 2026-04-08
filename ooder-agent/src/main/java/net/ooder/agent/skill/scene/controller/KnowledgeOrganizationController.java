package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/knowledge-organizations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KnowledgeOrganizationController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeOrganizationController.class);
    
    private final Map<String, KnowledgeOrg> organizations = new ConcurrentHashMap<>();
    
    public KnowledgeOrganizationController() {
        initDefaultOrganizations();
    }
    
    private void initDefaultOrganizations() {
        KnowledgeOrg company = new KnowledgeOrg();
        company.setOrgId("org-company");
        company.setName("公司级");
        company.setType("company");
        company.setParentId(null);
        company.setSort(1);
        company.setDescription("全公司共享的通用业务知识");
        company.setIcon("ri-building-2-line");
        organizations.put(company.getOrgId(), company);
        
        KnowledgeOrg dept = new KnowledgeOrg();
        dept.setOrgId("org-department");
        dept.setName("部门级");
        dept.setType("department");
        dept.setParentId(null);
        dept.setSort(2);
        dept.setDescription("部门专属的业务知识");
        dept.setIcon("ri-team-line");
        organizations.put(dept.getOrgId(), dept);
        
        KnowledgeOrg special = new KnowledgeOrg();
        special.setOrgId("org-special");
        special.setName("专用业务");
        special.setType("special");
        special.setParentId(null);
        special.setSort(3);
        special.setDescription("特定业务领域的专业知识");
        special.setIcon("ri-focus-3-line");
        organizations.put(special.getOrgId(), special);
    }
    
    @GetMapping
    public ResultModel<List<KnowledgeOrg>> listOrganizations() {
        log.info("[listOrganizations] request start");
        List<KnowledgeOrg> result = organizations.values().stream()
            .sorted(Comparator.comparingInt(KnowledgeOrg::getSort))
            .collect(Collectors.toList());
        return ResultModel.success(result);
    }
    
    @GetMapping("/{orgId}")
    public ResultModel<KnowledgeOrg> getOrganization(@PathVariable String orgId) {
        log.info("[getOrganization] orgId: {}", orgId);
        KnowledgeOrg org = organizations.get(orgId);
        if (org == null) {
            return ResultModel.notFound("Organization not found: " + orgId);
        }
        return ResultModel.success(org);
    }
    
    @PostMapping
    public ResultModel<KnowledgeOrg> createOrganization(@RequestBody KnowledgeOrg request) {
        log.info("[createOrganization] name: {}, type: {}", request.getName(), request.getType());
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResultModel.error(400, "Name is required");
        }
        
        if (request.getOrgId() == null || request.getOrgId().trim().isEmpty()) {
            request.setOrgId("org-" + UUID.randomUUID().toString().substring(0, 8));
        }
        
        request.setCreatedAt(new Date());
        request.setUpdatedAt(new Date());
        
        organizations.put(request.getOrgId(), request);
        return ResultModel.success(request);
    }
    
    @PutMapping("/{orgId}")
    public ResultModel<KnowledgeOrg> updateOrganization(
            @PathVariable String orgId,
            @RequestBody KnowledgeOrg request) {
        log.info("[updateOrganization] orgId: {}", orgId);
        
        KnowledgeOrg existing = organizations.get(orgId);
        if (existing == null) {
            return ResultModel.notFound("Organization not found: " + orgId);
        }
        
        request.setOrgId(orgId);
        request.setCreatedAt(existing.getCreatedAt());
        request.setUpdatedAt(new Date());
        
        organizations.put(orgId, request);
        return ResultModel.success(request);
    }
    
    @DeleteMapping("/{orgId}")
    public ResultModel<Boolean> deleteOrganization(@PathVariable String orgId) {
        log.info("[deleteOrganization] orgId: {}", orgId);
        
        if (orgId.startsWith("org-company") || orgId.startsWith("org-department") || orgId.startsWith("org-special")) {
            return ResultModel.error(400, "Cannot delete default organization");
        }
        
        organizations.remove(orgId);
        return ResultModel.success(true);
    }
    
    @GetMapping("/{orgId}/knowledge-bases")
    public ResultModel<Map<String, Object>> getOrganizationKnowledgeBases(
            @PathVariable String orgId,
            @RequestParam(required = false, defaultValue = "false") boolean includeStats) {
        log.info("[getOrganizationKnowledgeBases] orgId: {}", orgId);
        
        KnowledgeOrg org = organizations.get(orgId);
        if (org == null) {
            return ResultModel.notFound("Organization not found: " + orgId);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("organization", org);
        result.put("knowledgeBases", org.getKnowledgeBases() != null ? org.getKnowledgeBases() : new ArrayList<>());
        
        if (includeStats) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("kbCount", org.getKnowledgeBases() != null ? org.getKnowledgeBases().size() : 0);
            stats.put("docCount", org.getTotalDocs() != null ? org.getTotalDocs() : 0);
            result.put("stats", stats);
        }
        
        return ResultModel.success(result);
    }
    
    @PostMapping("/{orgId}/knowledge-bases/{kbId}")
    public ResultModel<Boolean> bindKnowledgeBase(
            @PathVariable String orgId,
            @PathVariable String kbId) {
        log.info("[bindKnowledgeBase] orgId: {}, kbId: {}", orgId, kbId);
        
        KnowledgeOrg org = organizations.get(orgId);
        if (org == null) {
            return ResultModel.notFound("Organization not found: " + orgId);
        }
        
        if (org.getKnowledgeBases() == null) {
            org.setKnowledgeBases(new ArrayList<>());
        }
        
        if (!org.getKnowledgeBases().contains(kbId)) {
            org.getKnowledgeBases().add(kbId);
        }
        
        return ResultModel.success(true);
    }
    
    @DeleteMapping("/{orgId}/knowledge-bases/{kbId}")
    public ResultModel<Boolean> unbindKnowledgeBase(
            @PathVariable String orgId,
            @PathVariable String kbId) {
        log.info("[unbindKnowledgeBase] orgId: {}, kbId: {}", orgId, kbId);
        
        KnowledgeOrg org = organizations.get(orgId);
        if (org == null) {
            return ResultModel.notFound("Organization not found: " + orgId);
        }
        
        if (org.getKnowledgeBases() != null) {
            org.getKnowledgeBases().remove(kbId);
        }
        
        return ResultModel.success(true);
    }
    
    public static class KnowledgeOrg {
        private String orgId;
        private String name;
        private String type;
        private String parentId;
        private int sort;
        private String description;
        private String icon;
        private List<String> knowledgeBases;
        private Integer totalDocs;
        private Date createdAt;
        private Date updatedAt;
        
        public String getOrgId() { return orgId; }
        public void setOrgId(String orgId) { this.orgId = orgId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        
        public int getSort() { return sort; }
        public void setSort(int sort) { this.sort = sort; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        
        public List<String> getKnowledgeBases() { return knowledgeBases; }
        public void setKnowledgeBases(List<String> knowledgeBases) { this.knowledgeBases = knowledgeBases; }
        
        public Integer getTotalDocs() { return totalDocs; }
        public void setTotalDocs(Integer totalDocs) { this.totalDocs = totalDocs; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
        
        public Date getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    }
}
