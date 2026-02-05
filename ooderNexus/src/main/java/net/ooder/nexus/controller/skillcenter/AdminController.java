package net.ooder.nexus.controller.skillcenter;

import net.ooder.nexus.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final Map<String, Skill> skillMap;
    private final Map<String, Category> categoryMap;
    private final Map<String, Certificate> certificateMap;
    private final Map<String, Validation> validationMap;
    private final Map<String, Group> groupMap;
    private final Map<String, Hosting> hostingMap;
    private final Map<String, Backup> backupMap;

    public AdminController() {
        this.skillMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.certificateMap = new HashMap<>();
        this.validationMap = new HashMap<>();
        this.groupMap = new HashMap<>();
        this.hostingMap = new HashMap<>();
        this.backupMap = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        Category category1 = new Category();
        category1.setId("cat-001");
        category1.setName("文本处理");
        category1.setDescription("文本分析相关技能");
        categoryMap.put(category1.getId(), category1);

        Skill skill1 = new Skill();
        skill1.setId("skill-001");
        skill1.setName("文本分析");
        skill1.setDescription("分析文本内容");
        skill1.setCategory("text-processing");
        skill1.setAuthor("张三");
        skill1.setCreatedAt(LocalDateTime.now());
        skillMap.put(skill1.getId(), skill1);

        Backup backup1 = new Backup();
        backup1.setId("backup-001");
        backup1.setName("系统备份");
        backup1.setSize(1024000);
        backup1.setCreatedAt(LocalDateTime.now());
        backupMap.put(backup1.getId(), backup1);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSkills", skillMap.size());
        stats.put("totalUsers", 42);
        stats.put("todayExecutions", 156);
        stats.put("totalGroups", groupMap.size());
        stats.put("pendingSkills", getPendingSkills());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getAllSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(skillMap.values())));
    }

    @GetMapping("/skills/pending")
    public ResponseEntity<ApiResponse<List<Skill>>> getPendingSkills() {
        List<Skill> pendingSkills = new ArrayList<>();
        skillMap.values().forEach(skill -> {
            if (!skill.isApproved()) {
                pendingSkills.add(skill);
            }
        });
        return ResponseEntity.ok(ApiResponse.success(pendingSkills));
    }

    @PostMapping("/skills/{skillId}/approve")
    public ResponseEntity<ApiResponse<Boolean>> approveSkill(@PathVariable String skillId) {
        Skill skill = skillMap.get(skillId);
        if (skill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Skill not found"));
        }
        skill.setApproved(true);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @PostMapping("/skills/{skillId}/reject")
    public ResponseEntity<ApiResponse<Boolean>> rejectSkill(@PathVariable String skillId) {
        Skill skill = skillMap.get(skillId);
        if (skill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Skill not found"));
        }
        skillMap.remove(skillId);
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkill(@PathVariable String skillId) {
        boolean result = skillMap.remove(skillId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(categoryMap.values())));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<Category>> addCategory(@RequestBody Category category) {
        category.setId("cat-" + System.currentTimeMillis());
        categoryMap.put(category.getId(), category);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCategory(@PathVariable String categoryId) {
        boolean result = categoryMap.remove(categoryId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/market/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getMarketSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(skillMap.values())));
    }

    @DeleteMapping("/market/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> removeMarketSkill(@PathVariable String skillId) {
        boolean result = skillMap.remove(skillId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/market/enterprise")
    public ResponseEntity<ApiResponse<EnterpriseAPI>> importAPI(@RequestBody EnterpriseAPI api) {
        return ResponseEntity.ok(ApiResponse.success(api));
    }

    @DeleteMapping("/market/enterprise/{apiId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteAPI(@PathVariable String apiId) {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping("/certification/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getCertificationSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(skillMap.values())));
    }

    @PostMapping("/certification/certify")
    public ResponseEntity<ApiResponse<Certificate>> certifySkill(@RequestBody Map<String, Object> request) {
        String skillId = (String) request.get("skillId");
        String level = (String) request.get("level");
        String note = (String) request.get("note");

        Certificate certificate = new Certificate();
        certificate.setId("cert-" + System.currentTimeMillis());
        certificate.setSkillId(skillId);
        certificate.setLevel(level);
        certificate.setNote(note);
        certificate.setCertifiedAt(LocalDateTime.now());
        certificate.setExpiresAt(LocalDateTime.now().plusYears(1));
        certificateMap.put(certificate.getId(), certificate);

        Skill skill = skillMap.get(skillId);
        if (skill != null) {
            skill.setCertification(true);
        }

        return ResponseEntity.ok(ApiResponse.success(certificate));
    }

    @GetMapping("/certification/certificates")
    public ResponseEntity<ApiResponse<List<Certificate>>> getCertificates() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(certificateMap.values())));
    }

    @DeleteMapping("/certification/certificates/{certId}")
    public ResponseEntity<ApiResponse<Boolean>> revokeCertificate(@PathVariable String certId) {
        boolean result = certificateMap.remove(certId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/certification/validations")
    public ResponseEntity<ApiResponse<List<Validation>>> getValidations() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(validationMap.values())));
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<Group>>> getGroups() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(groupMap.values())));
    }

    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<Group>> createGroup(@RequestBody Group group) {
        group.setId("group-" + System.currentTimeMillis());
        group.setCreatedAt(LocalDateTime.now());
        groupMap.put(group.getId(), group);
        return ResponseEntity.ok(ApiResponse.success(group));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteGroup(@PathVariable String groupId) {
        boolean result = groupMap.remove(groupId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<Group>> getGroup(@PathVariable String groupId) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(group));
    }

    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<ApiResponse<Boolean>> addMember(@PathVariable String groupId, @RequestBody Map<String, Object> request) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Boolean>> removeMember(@PathVariable String groupId, @PathVariable String memberId) {
        Group group = groupMap.get(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Group not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping("/remote/skills")
    public ResponseEntity<ApiResponse<List<Hosting>>> getRemoteSkills() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(hostingMap.values())));
    }

    @PostMapping("/remote/skills/{skillId}/sync")
    public ResponseEntity<ApiResponse<Boolean>> syncSkill(@PathVariable String skillId) {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/remote/skills/{skillId}")
    public ResponseEntity<ApiResponse<Boolean>> removeRemoteSkill(@PathVariable String skillId) {
        boolean result = hostingMap.remove(skillId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/remote/hosting")
    public ResponseEntity<ApiResponse<List<Hosting>>> getHostingList() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(hostingMap.values())));
    }

    @PostMapping("/remote/hosting/{hostingId}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleHosting(@PathVariable String hostingId) {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/remote/hosting/{hostingId}")
    public ResponseEntity<ApiResponse<Boolean>> removeHosting(@PathVariable String hostingId) {
        boolean result = hostingMap.remove(hostingId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/remote/monitoring")
    public ResponseEntity<ApiResponse<List<Hosting>>> getMonitoringList() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(hostingMap.values())));
    }

    @PostMapping("/remote/monitoring/{monitoringId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkMonitoring(@PathVariable String monitoringId) {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/remote/monitoring/{monitoringId}")
    public ResponseEntity<ApiResponse<Boolean>> removeMonitoring(@PathVariable String monitoringId) {
        boolean result = hostingMap.remove(monitoringId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/storage/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStorageStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalSpace", 107374182400L);
        status.put("usedSpace", 536870912L);
        status.put("freeSpace", 536870912L);
        status.put("usagePercent", 50.0);
        status.put("skillFiles", 20971520L);
        status.put("backupFiles", 15728640L);
        status.put("logFiles", 10485760L);
        status.put("tempFiles", 5242880L);
        status.put("otherFiles", 16777216L);
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @GetMapping("/storage/backups")
    public ResponseEntity<ApiResponse<List<Backup>>> getBackups() {
        return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(backupMap.values())));
    }

    @PostMapping("/storage/backup")
    public ResponseEntity<ApiResponse<Backup>> createBackup() {
        Backup backup = new Backup();
        backup.setId("backup-" + System.currentTimeMillis());
        backup.setName("系统备份-" + LocalDateTime.now().toString());
        backup.setSize(1024000);
        backup.setCreatedAt(LocalDateTime.now());
        backup.setDescription("自动备份");
        backupMap.put(backup.getId(), backup);
        return ResponseEntity.ok(ApiResponse.success(backup));
    }

    @GetMapping("/storage/backups/{backupId}/download")
    public ResponseEntity<byte[]> downloadBackup(@PathVariable String backupId) {
        Backup backup = backupMap.get(backupId);
        if (backup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new byte[1024]);
    }

    @PostMapping("/storage/backups/{backupId}/restore")
    public ResponseEntity<ApiResponse<Boolean>> restoreFromBackup(@PathVariable String backupId) {
        Backup backup = backupMap.get(backupId);
        if (backup == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Backup not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @DeleteMapping("/storage/backups/{backupId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteBackup(@PathVariable String backupId) {
        boolean result = backupMap.remove(backupId) != null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/storage/clean")
    public ResponseEntity<ApiResponse<Map<String, Object>>> performClean(@RequestBody Map<String, Boolean> request) {
        Map<String, Object> result = new HashMap<>();
        result.put("freedSpace", 104857600L);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/storage/settings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("storagePath", "/var/lib/ooder/storage");
        settings.put("maxStorage", 100);
        settings.put("backupRetention", 30);
        settings.put("autoBackup", "daily");
        return ResponseEntity.ok(ApiResponse.success(settings));
    }

    @PutMapping("/storage/settings")
    public ResponseEntity<ApiResponse<Boolean>> saveSettings(@RequestBody Map<String, Object> settings) {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    public static class Skill {
        private String id;
        private String name;
        private String description;
        private String category;
        private String author;
        private LocalDateTime createdAt;
        private boolean approved;
        private boolean certification;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }

        public boolean isCertification() {
            return certification;
        }

        public void setCertification(boolean certification) {
            this.certification = certification;
        }
    }

    public static class Category {
        private String id;
        private String name;
        private String description;
        private int skillCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSkillCount() {
            return skillCount;
        }

        public void setSkillCount(int skillCount) {
            this.skillCount = skillCount;
        }
    }

    public static class Certificate {
        private String id;
        private String skillId;
        private String skillName;
        private String certificateId;
        private String level;
        private String note;
        private LocalDateTime certifiedAt;
        private LocalDateTime expiresAt;

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

        public String getCertificateId() {
            return certificateId;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public LocalDateTime getCertifiedAt() {
            return certifiedAt;
        }

        public void setCertifiedAt(LocalDateTime certifiedAt) {
            this.certifiedAt = certifiedAt;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
        }
    }

    public static class Validation {
        private String id;
        private String skillName;
        private String certificateId;
        private LocalDateTime validatedAt;
        private boolean valid;
        private String note;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getCertificateId() {
            return certificateId;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public LocalDateTime getValidatedAt() {
            return validatedAt;
        }

        public void setValidatedAt(LocalDateTime validatedAt) {
            this.validatedAt = validatedAt;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class Group {
        private String id;
        private String name;
        private String description;
        private String type;
        private LocalDateTime createdAt;
        private List<Member> members;

        public Group() {
            this.members = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public List<Member> getMembers() {
            return members;
        }

        public void setMembers(List<Member> members) {
            this.members = members;
        }
    }

    public static class Member {
        private String id;
        private String name;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static class Hosting {
        private String id;
        private String skillName;
        private String hostingUrl;
        private String status;
        private LocalDateTime createdAt;
        private String monitoringUrl;
        private boolean online;
        private long responseTime;
        private LocalDateTime lastChecked;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getHostingUrl() {
            return hostingUrl;
        }

        public void setHostingUrl(String hostingUrl) {
            this.hostingUrl = hostingUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getMonitoringUrl() {
            return monitoringUrl;
        }

        public void setMonitoringUrl(String monitoringUrl) {
            this.monitoringUrl = monitoringUrl;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(long responseTime) {
            this.responseTime = responseTime;
        }

        public LocalDateTime getLastChecked() {
            return lastChecked;
        }

        public void setLastChecked(LocalDateTime lastChecked) {
            this.lastChecked = lastChecked;
        }
    }

    public static class Backup {
        private String id;
        private String name;
        private long size;
        private LocalDateTime createdAt;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class EnterpriseAPI {
        private String id;
        private String name;
        private String description;
        private String url;
        private String doc;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
