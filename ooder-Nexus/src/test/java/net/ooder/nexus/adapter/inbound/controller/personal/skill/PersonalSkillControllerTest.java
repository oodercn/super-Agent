package net.ooder.nexus.adapter.inbound.controller.personal.skill;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 个人技能控制器测试
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class PersonalSkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSkillList_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    public void testCreateSkill_Success() throws Exception {
        String skillJson = "{" +
                "\"name\":\"测试技能\"," +
                "\"description\":\"这是一个测试技能\"," +
                "\"version\":\"1.0.0\"," +
                "\"category\":\"test\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("测试技能"))
                .andExpect(jsonPath("$.data.skillId").exists());
    }

    @Test
    public void testUpdateSkill_Success() throws Exception {
        String skillJson = "{" +
                "\"skillId\":\"skill-001\"," +
                "\"name\":\"更新后的技能\"," +
                "\"description\":\"更新后的描述\"," +
                "\"version\":\"1.1.0\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testDeleteSkill_Success() throws Exception {
        String requestJson = "{\"skillId\":\"skill-001\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testGetSkillDetail_Success() throws Exception {
        String requestJson = "{\"skillId\":\"skill-001\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.skillId").exists())
                .andExpect(jsonPath("$.data.name").exists());
    }

    @Test
    public void testSearchSkills_Success() throws Exception {
        String requestJson = "{\"keyword\":\"测试\",\"category\":\"test\",\"page\":0,\"size\":10}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    public void testGetSkillCategories_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testPublishSkill_Success() throws Exception {
        String skillJson = "{" +
                "\"name\":\"发布测试技能\"," +
                "\"description\":\"测试发布功能\"," +
                "\"version\":\"1.0.0\"," +
                "\"category\":\"test\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.status").value("published"));
    }

    @Test
    public void testUnpublishSkill_Success() throws Exception {
        String requestJson = "{\"skillId\":\"skill-001\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/unpublish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.status").value("draft"));
    }

    @Test
    public void testFullSkillLifecycle() throws Exception {
        // 1. Create skill
        String skillJson = "{" +
                "\"name\":\"生命周期测试技能\"," +
                "\"description\":\"测试完整生命周期\"," +
                "\"version\":\"1.0.0\"," +
                "\"category\":\"lifecycle\"" +
                "}";

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract skill ID from response
        String skillId = extractSkillId(response);

        // 2. Update skill
        String updateJson = "{\"skillId\":\"" + skillId + "\",\"name\":\"已更新的技能\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 3. Get skill detail
        String detailJson = "{\"skillId\":\"" + skillId + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(detailJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 4. Delete skill
        String deleteJson = "{\"skillId\":\"" + skillId + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/skills/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    private String extractSkillId(String jsonResponse) {
        int idIndex = jsonResponse.indexOf("\"skillId\":\"");
        if (idIndex > 0) {
            int start = idIndex + 11;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end);
        }
        return null;
    }
}
