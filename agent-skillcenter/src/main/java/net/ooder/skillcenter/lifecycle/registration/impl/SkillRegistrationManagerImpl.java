package net.ooder.skillcenter.lifecycle.registration.impl;

import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import java.util.*;

public class SkillRegistrationManagerImpl implements SkillRegistrationManager {
    
    private static SkillRegistrationManagerImpl instance;
    private Map<String, SkillRegistrationInfo> registrations;
    
    public SkillRegistrationManagerImpl() {
        this.registrations = new HashMap<>();
        loadSampleData();
    }
    
    public static SkillRegistrationManager getInstance() {
        if (instance == null) {
            instance = new SkillRegistrationManagerImpl();
        }
        return instance;
    }
    
    @Override
    public RegistrationResult registerSkill(SkillRegistrationRequest request) {
        RegistrationResult result = new RegistrationResult();
        
        String skillId = request.getSkillId();
        if (skillId == null || skillId.isEmpty()) {
            skillId = "skill-" + System.currentTimeMillis();
            request.setSkillId(skillId);
        }
        
        SkillRegistrationInfo info = new SkillRegistrationInfo();
        info.setSkillId(skillId);
        info.setSkillName(request.getSkillName());
        info.setDescription(request.getDescription());
        info.setCategory(request.getCategory());
        info.setVersion(request.getVersion());
        info.setStatus("pending");
        info.setApplicant(request.getApplicant());
        info.setCreatedAt(System.currentTimeMillis());
        info.setUpdatedAt(System.currentTimeMillis());
        
        registrations.put(skillId, info);
        
        result.setSuccess(true);
        result.setSkillId(skillId);
        result.setStatus("pending");
        result.setTimestamp(System.currentTimeMillis());
        
        return result;
    }
    
    @Override
    public RegistrationResult updateSkill(String skillId, SkillUpdateRequest request) {
        RegistrationResult result = new RegistrationResult();
        
        SkillRegistrationInfo info = registrations.get(skillId);
        if (info == null) {
            result.setSuccess(false);
            result.setMessage("Skill not found: " + skillId);
            return result;
        }
        
        if (request.getSkillName() != null) {
            info.setSkillName(request.getSkillName());
        }
        if (request.getDescription() != null) {
            info.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            info.setCategory(request.getCategory());
        }
        if (request.getVersion() != null) {
            info.setVersion(request.getVersion());
        }
        
        info.setUpdatedAt(System.currentTimeMillis());
        
        result.setSuccess(true);
        result.setSkillId(skillId);
        result.setStatus(info.getStatus());
        result.setTimestamp(System.currentTimeMillis());
        
        return result;
    }
    
    @Override
    public boolean unregisterSkill(String skillId) {
        SkillRegistrationInfo info = registrations.remove(skillId);
        return info != null;
    }
    
    @Override
    public SkillRegistrationInfo getSkillRegistration(String skillId) {
        return registrations.get(skillId);
    }
    
    @Override
    public List<SkillRegistrationInfo> getAllRegistrations() {
        return new ArrayList<>(registrations.values());
    }
    
    @Override
    public List<SkillRegistrationInfo> getRegistrationsByStatus(String status) {
        List<SkillRegistrationInfo> result = new ArrayList<>();
        for (SkillRegistrationInfo info : registrations.values()) {
            if (status.equals(info.getStatus())) {
                result.add(info);
            }
        }
        return result;
    }
    
    @Override
    public boolean approveRegistration(String skillId, String reviewer, String comments) {
        SkillRegistrationInfo info = registrations.get(skillId);
        if (info == null) {
            return false;
        }
        
        info.setStatus("active");
        info.setReviewer(reviewer);
        info.setComments(comments);
        info.setUpdatedAt(System.currentTimeMillis());
        
        return true;
    }
    
    @Override
    public boolean rejectRegistration(String skillId, String reviewer, String comments) {
        SkillRegistrationInfo info = registrations.get(skillId);
        if (info == null) {
            return false;
        }
        
        info.setStatus("rejected");
        info.setReviewer(reviewer);
        info.setComments(comments);
        info.setUpdatedAt(System.currentTimeMillis());
        
        return true;
    }
    
    private void loadSampleData() {
        SkillRegistrationInfo skill1 = new SkillRegistrationInfo();
        skill1.setSkillId("skill-sample-001");
        skill1.setSkillName("Weather API");
        skill1.setDescription("Weather query skill");
        skill1.setCategory("utilities");
        skill1.setVersion("1.0.0");
        skill1.setStatus("active");
        skill1.setApplicant("admin");
        skill1.setCreatedAt(System.currentTimeMillis() - 86400000L);
        skill1.setUpdatedAt(System.currentTimeMillis() - 43200000L);
        registrations.put(skill1.getSkillId(), skill1);
        
        SkillRegistrationInfo skill2 = new SkillRegistrationInfo();
        skill2.setSkillId("skill-sample-002");
        skill2.setSkillName("Stock API");
        skill2.setDescription("Stock market data skill");
        skill2.setCategory("finance");
        skill2.setVersion("1.0.0");
        skill2.setStatus("pending");
        skill2.setApplicant("user123");
        skill2.setCreatedAt(System.currentTimeMillis() - 172800000L);
        skill2.setUpdatedAt(System.currentTimeMillis() - 86400000L);
        registrations.put(skill2.getSkillId(), skill2);
    }
}
