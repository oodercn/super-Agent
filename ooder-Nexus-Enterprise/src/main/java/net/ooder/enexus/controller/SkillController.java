package net.ooder.enexus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.ooder.enexus.skill.registry.SkillRegistry;
import net.ooder.enexus.skill.registry.SkillRegistry.SkillInfo;
import net.ooder.enexus.dto.skill.SkillListDTO;
import net.ooder.enexus.dto.skill.SkillStatusDTO;
import net.ooder.enexus.model.ResultModel;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    
    @Autowired
    private SkillRegistry skillRegistry;
    
    @GetMapping
    public ResponseEntity<SkillListDTO> listSkills() {
        SkillListDTO result = new SkillListDTO();
        List<SkillInfo> allSkills = skillRegistry.getAllSkills();
        List<SkillInfo> systemSkills = skillRegistry.getSystemSkills();
        List<SkillInfo> installedSkills = skillRegistry.getInstalledSkills();
        
        result.setTotal(allSkills.size());
        result.setSystemCount(systemSkills.size());
        result.setInstalledCount(installedSkills.size());
        result.setSkills(allSkills);
        result.setSystemSkills(systemSkills);
        result.setInstalledSkills(installedSkills);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/system")
    public ResponseEntity<SkillListDTO> listSystemSkills() {
        SkillListDTO result = new SkillListDTO();
        List<SkillInfo> systemSkills = skillRegistry.getSystemSkills();
        result.setTotal(systemSkills.size());
        result.setSkills(systemSkills);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/installed")
    public ResponseEntity<SkillListDTO> listInstalledSkills() {
        SkillListDTO result = new SkillListDTO();
        List<SkillInfo> installedSkills = skillRegistry.getInstalledSkills();
        result.setTotal(installedSkills.size());
        result.setSkills(installedSkills);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{skillId}")
    public ResponseEntity<?> getSkill(@PathVariable String skillId) {
        SkillInfo skill = skillRegistry.getSkill(skillId);
        if (skill == null) {
            return ResponseEntity.status(404).body("Skill not found: " + skillId);
        }
        return ResponseEntity.ok(skill);
    }
    
    @GetMapping("/{skillId}/status")
    public ResponseEntity<SkillStatusDTO> getSkillStatus(@PathVariable String skillId) {
        SkillStatusDTO result = new SkillStatusDTO();
        SkillInfo skill = skillRegistry.getSkill(skillId);
        
        if (skill == null) {
            result.setInstalled(false);
            result.setExists(false);
        } else {
            result.setInstalled(skill.isInstalled());
            result.setExists(true);
            result.setSystem(skill.isSystem());
            result.setSource(skill.getSource());
        }
        
        return ResponseEntity.ok(result);
    }
}
