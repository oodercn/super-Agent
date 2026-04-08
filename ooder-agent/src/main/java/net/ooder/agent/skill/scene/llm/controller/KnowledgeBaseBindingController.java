package net.ooder.mvp.skill.scene.llm.controller;

import net.ooder.mvp.skill.scene.dto.knowledge.KbBindingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/knowledge-bases")
public class KnowledgeBaseBindingController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseBindingController.class);

    @GetMapping("/{kbId}/bindings")
    public KbBindingsResponseDTO getKnowledgeBaseBindings(@PathVariable String kbId) {
        log.debug("[KbBinding] Getting bindings for knowledge base: {}", kbId);
        
        KbBindingsResponseDTO response = new KbBindingsResponseDTO();
        response.setKbId(kbId);
        
        List<KbBindingDTO> bindings = new ArrayList<>();
        bindings.addAll(getSkillBindings(kbId));
        bindings.addAll(getSceneBindings(kbId));
        bindings.addAll(getPromptBindings(kbId));
        
        response.setBindings(bindings);
        response.setTotalBindings(bindings.size());
        
        return response;
    }
    
    @GetMapping("/{kbId}/bindings/skills")
    public KbBindingsResponseDTO getSkillBindingsForKb(@PathVariable String kbId) {
        KbBindingsResponseDTO response = new KbBindingsResponseDTO();
        response.setKbId(kbId);
        response.setBindings(getSkillBindings(kbId));
        return response;
    }
    
    @GetMapping("/{kbId}/bindings/scenes")
    public KbBindingsResponseDTO getSceneBindingsForKb(@PathVariable String kbId) {
        KbBindingsResponseDTO response = new KbBindingsResponseDTO();
        response.setKbId(kbId);
        response.setBindings(getSceneBindings(kbId));
        return response;
    }

    private List<KbBindingDTO> getSkillBindings(String kbId) {
        List<KbBindingDTO> bindings = new ArrayList<>();
        
        if ("kb-skill-prompts".equals(kbId) || "kb-global-menu".equals(kbId)) {
            KbBindingDTO binding = new KbBindingDTO();
            binding.setType("skill");
            binding.setId("skill-scene");
            binding.setName("场景技能");
            binding.setPurpose("kb-skill-prompts".equals(kbId) ? "提示语检索" : "菜单导航");
            binding.setLayer("SKILL");
            binding.setPriority(1);
            bindings.add(binding);
        }
        
        if ("kb-skill-docs".equals(kbId)) {
            KbBindingDTO binding = new KbBindingDTO();
            binding.setType("skill");
            binding.setId("skill-knowledge");
            binding.setName("知识库技能");
            binding.setPurpose("RAG增强");
            binding.setLayer("SKILL");
            binding.setPriority(2);
            bindings.add(binding);
        }
        
        return bindings;
    }

    private List<KbBindingDTO> getSceneBindings(String kbId) {
        List<KbBindingDTO> bindings = new ArrayList<>();
        
        if ("kb-scene-docs".equals(kbId) || "kb-skill-prompts".equals(kbId)) {
            KbBindingDTO binding = new KbBindingDTO();
            binding.setType("scene");
            binding.setId("scene-discovery");
            binding.setName("能力发现场景");
            binding.setPurpose("RAG增强");
            binding.setLayer("SCENE");
            binding.setPriority(2);
            bindings.add(binding);
        }
        
        if ("kb-platform-guide".equals(kbId)) {
            KbBindingDTO binding = new KbBindingDTO();
            binding.setType("scene");
            binding.setId("scene-activation");
            binding.setName("场景激活");
            binding.setPurpose("平台指南检索");
            binding.setLayer("GENERAL");
            binding.setPriority(1);
            bindings.add(binding);
        }
        
        return bindings;
    }

    private List<KbBindingDTO> getPromptBindings(String kbId) {
        List<KbBindingDTO> bindings = new ArrayList<>();
        
        if ("kb-skill-prompts".equals(kbId)) {
            KbBindingDTO binding = new KbBindingDTO();
            binding.setType("prompt");
            binding.setId("system-prompt");
            binding.setName("系统提示词");
            binding.setPurpose("SystemPrompt检索");
            binding.setLayer("SKILL");
            bindings.add(binding);
            
            KbBindingDTO roleBinding = new KbBindingDTO();
            roleBinding.setType("prompt");
            roleBinding.setId("role-discovery-assistant");
            roleBinding.setName("发现助手角色提示词");
            roleBinding.setPurpose("角色Prompt检索");
            roleBinding.setLayer("SKILL");
            bindings.add(roleBinding);
        }
        
        return bindings;
    }
    
    public static class KbBindingsResponseDTO {
        private String kbId;
        private List<KbBindingDTO> bindings;
        private Integer totalBindings;
        
        public String getKbId() { return kbId; }
        public void setKbId(String kbId) { this.kbId = kbId; }
        public List<KbBindingDTO> getBindings() { return bindings; }
        public void setBindings(List<KbBindingDTO> bindings) { this.bindings = bindings; }
        public Integer getTotalBindings() { return totalBindings; }
        public void setTotalBindings(Integer totalBindings) { this.totalBindings = totalBindings; }
    }
}
