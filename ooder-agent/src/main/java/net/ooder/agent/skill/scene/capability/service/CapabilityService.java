package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityOwnership;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;
import net.ooder.mvp.skill.scene.capability.model.SceneType;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.capability.model.CapabilityStatus;

import java.util.List;

public interface CapabilityService {

    Capability register(Capability capability);

    void unregister(String capabilityId);

    Capability findById(String capabilityId);

    List<Capability> findAll();

    List<Capability> findByType(CapabilityType type);

    List<Capability> findBySceneType(String sceneType);

    List<Capability> search(String query);

    Capability update(Capability capability);

    void updateStatus(String capabilityId, String status);
    
    Capability addSceneType(String capabilityId, String sceneType, String approvedBy);
    
    Capability removeSceneType(String capabilityId, String sceneType, String approvedBy);
    
    List<Capability> findByOwnership(CapabilityOwnership ownership);
    
    List<Capability> findByOwnershipAndSceneType(CapabilityOwnership ownership, String sceneType);
    
    List<Capability> findBySkillForm(SkillForm form);
    
    List<Capability> findBySceneTypeNew(SceneType sceneType);
    
    List<Capability> findByCapabilityCategory(CapabilityCategory category);
    
    List<Capability> findByFilters(SkillForm form, SceneType sceneType, CapabilityCategory category, 
                                    CapabilityOwnership ownership, String keyword);
    
    void updateInstallStatus(String capabilityId, boolean installed);
    
    boolean isInstalled(String capabilityId);
    
    CapabilityStatus getCapabilityStatus(String capabilityId);
}
