package net.ooder.examples.endagent;

import net.ooder.examples.endagent.model.Skill;
import net.ooder.examples.endagent.service.SkillManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EndAgentApplicationTests {

    @Autowired
    private SkillManager skillManager;

    @Test
    void contextLoads() {
        // 验证应用上下文能够正常加载
    }

    @Test
    void testSkillManagerInitialization() {
        // 验证技能管理器初始化成功
        assertNotNull(skillManager);
        assertTrue(skillManager.getAllSkills().size() > 0, "SkillManager should have at least one skill initialized");
        System.out.println("Number of initialized skills: " + skillManager.getAllSkills().size());
    }

    @Test
    void testFileConverterSkill() {
        // 验证文件转换技能是否存在
        Skill skill = skillManager.getSkillById("file-converter-001");
        assertNotNull(skill, "File converter skill should exist");
        assertTrue(skill.getName().equals("File Converter"), "Skill name should match");
        assertTrue(skill.getCapabilities().size() > 0, "Skill should have at least one capability");
    }

    @Test
    void testTextProcessorSkill() {
        // 验证文本处理技能是否存在
        Skill skill = skillManager.getSkillById("text-processor-001");
        assertNotNull(skill, "Text processor skill should exist");
        assertTrue(skill.getName().equals("Text Processor"), "Skill name should match");
        assertTrue(skill.getCapabilities().size() > 0, "Skill should have at least one capability");
    }
}
