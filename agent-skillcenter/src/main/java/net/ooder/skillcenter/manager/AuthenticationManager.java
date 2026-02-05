package net.ooder.skillcenter.manager;

import net.ooder.skillcenter.model.SkillAuthentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 认证管理器，负责技能认证的管理
 */
public class AuthenticationManager {
    // 单例实例映射
    private static Map<String, AuthenticationManager> instances = new HashMap<>();
    
    // 认证映射，key为认证ID，value为认证实例
    private Map<String, SkillAuthentication> authenticationMap;

    /**
     * 私有构造方法，初始化认证管理器
     */
    private AuthenticationManager() {
        this.authenticationMap = new HashMap<>();
        this.loadSampleData();
    }

    /**
     * 获取默认名称的认证管理器实例
     * @return 认证管理器实例
     */
    public static synchronized AuthenticationManager getInstance() {
        return getInstance("default");
    }

    /**
     * 获取指定名称的认证管理器实例
     * @param name 实例名称
     * @return 认证管理器实例
     */
    public static synchronized AuthenticationManager getInstance(String name) {
        if (!instances.containsKey(name)) {
            instances.put(name, new AuthenticationManager());
        }
        return instances.get(name);
    }

    /**
     * 创建新的技能认证
     * @param authentication 认证实例
     * @return 创建的认证实例
     */
    public synchronized SkillAuthentication createAuthentication(SkillAuthentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }

        // 生成认证ID
        if (authentication.getId() == null || authentication.getId().isEmpty()) {
            authentication.setId("auth-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // 设置申请时间
        if (authentication.getAppliedAt() == null) {
            authentication.setAppliedAt(LocalDateTime.now());
        }

        // 设置初始状态
        if (authentication.getStatus() == null || authentication.getStatus().isEmpty()) {
            authentication.setStatus("pending");
        }

        // 添加到认证映射
        authenticationMap.put(authentication.getId(), authentication);
        return authentication;
    }

    /**
     * 获取指定ID的认证
     * @param id 认证ID
     * @return 认证实例，不存在则返回null
     */
    public SkillAuthentication getAuthentication(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Authentication ID cannot be null or empty");
        }
        return authenticationMap.get(id);
    }

    /**
     * 获取所有认证
     * @return 认证列表
     */
    public List<SkillAuthentication> getAllAuthentications() {
        return new ArrayList<>(authenticationMap.values());
    }

    /**
     * 根据状态获取认证
     * @param status 认证状态
     * @return 认证列表
     */
    public List<SkillAuthentication> getAuthenticationsByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        List<SkillAuthentication> result = new ArrayList<>();
        for (SkillAuthentication auth : authenticationMap.values()) {
            if (status.equals(auth.getStatus())) {
                result.add(auth);
            }
        }
        return result;
    }

    /**
     * 根据技能ID获取认证
     * @param skillId 技能ID
     * @return 认证列表
     */
    public List<SkillAuthentication> getAuthenticationsBySkillId(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            throw new IllegalArgumentException("Skill ID cannot be null or empty");
        }

        List<SkillAuthentication> result = new ArrayList<>();
        for (SkillAuthentication auth : authenticationMap.values()) {
            if (skillId.equals(auth.getSkillId())) {
                result.add(auth);
            }
        }
        return result;
    }

    /**
     * 更新认证状态
     * @param id 认证ID
     * @param status 新状态
     * @param reviewer 审核人
     * @param comments 审核意见
     * @return 更新后的认证实例
     */
    public synchronized SkillAuthentication updateAuthenticationStatus(String id, String status, String reviewer, String comments) {
        SkillAuthentication auth = getAuthentication(id);
        if (auth == null) {
            throw new IllegalArgumentException("Authentication not found: " + id);
        }

        auth.setStatus(status);
        auth.setReviewer(reviewer);
        auth.setComments(comments);
        auth.setReviewedAt(LocalDateTime.now());

        return auth;
    }

    /**
     * 删除认证
     * @param id 认证ID
     * @return 删除是否成功
     */
    public synchronized boolean deleteAuthentication(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Authentication ID cannot be null or empty");
        }

        return authenticationMap.remove(id) != null;
    }

    /**
     * 加载示例数据
     */
    private void loadSampleData() {
        // 创建示例认证数据
        SkillAuthentication auth1 = new SkillAuthentication();
        auth1.setId("auth-001");
        auth1.setSkillId("weather-api-skill");
        auth1.setSkillName("Weather API");
        auth1.setApplicant("User123");
        auth1.setAppliedAt(LocalDateTime.now().minusDays(1));
        auth1.setStatus("pending");
        authenticationMap.put(auth1.getId(), auth1);

        SkillAuthentication auth2 = new SkillAuthentication();
        auth2.setId("auth-002");
        auth2.setSkillId("stock-api-skill");
        auth2.setSkillName("Stock API");
        auth2.setApplicant("User456");
        auth2.setAppliedAt(LocalDateTime.now().minusDays(2));
        auth2.setStatus("pending");
        authenticationMap.put(auth2.getId(), auth2);

        SkillAuthentication auth3 = new SkillAuthentication();
        auth3.setId("auth-003");
        auth3.setSkillId("text-to-uppercase-skill");
        auth3.setSkillName("Text to Uppercase");
        auth3.setApplicant("System");
        auth3.setAppliedAt(LocalDateTime.now().minusDays(3));
        auth3.setReviewedAt(LocalDateTime.now().minusDays(2));
        auth3.setStatus("approved");
        auth3.setReviewer("admin");
        auth3.setComments("技能功能正常，批准认证");
        auth3.setCertificateType("standard");
        auth3.setDescription("文本转大写技能认证");
        authenticationMap.put(auth3.getId(), auth3);

        System.out.println("Loaded " + authenticationMap.size() + " sample authentications");
    }
}
