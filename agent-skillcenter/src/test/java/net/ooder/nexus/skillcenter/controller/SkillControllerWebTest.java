package net.ooder.nexus.skillcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.SkillResultDTO;
import net.ooder.skillcenter.service.SkillService;
import net.ooder.skillcenter.service.ExecutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 技能API的web测试用例 - 符合 ooderNexus 规范
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SkillControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @MockBean
    private ExecutionService executionService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<SkillDTO> mockSkills;
    private PageResult<SkillDTO> mockPageResult;

    @BeforeEach
    public void setUp() throws Exception {
        mockSkills = new ArrayList<>();
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId("text-to-uppercase-skill");
        skillDTO.setName("文本转大写技能");
        skillDTO.setCategory("text-processing");
        mockSkills.add(skillDTO);

        mockPageResult = PageResult.of(mockSkills, 1, 10, 1);

        when(skillService.getAllSkills(any(), any(), any(), anyInt(), anyInt())).thenReturn(mockPageResult);
        when(skillService.getSkillById(anyString())).thenReturn(skillDTO);
        when(skillService.deleteSkill(anyString())).thenReturn(true);
        when(skillService.approveSkill(anyString())).thenReturn(true);
        when(skillService.rejectSkill(anyString())).thenReturn(true);
        when(skillService.addSkill(any())).thenReturn(skillDTO);
        when(skillService.updateSkill(anyString(), any())).thenReturn(skillDTO);

        SkillResultDTO skillResultDTO = new SkillResultDTO();
        skillResultDTO.setExecutionId("exec-001");
        skillResultDTO.setStatus("SUCCESS");
        skillResultDTO.setOutput("HELLO WORLD");
        skillResultDTO.setExecutionTime(100L);
        when(executionService.executeSkill(anyString(), any())).thenReturn(skillResultDTO);
    }

    @Test
    public void testGetAllSkills() throws Exception {
        String requestBody = "{}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.items").isArray());
    }

    @Test
    public void testGetSkill() throws Exception {
        String requestBody = "{\"skillId\": \"text-to-uppercase-skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").value("text-to-uppercase-skill"));
    }

    @Test
    public void testGetNonExistentSkill() throws Exception {
        when(skillService.getSkillById("non-existent-skill")).thenReturn(null);

        String requestBody = "{\"skillId\": \"non-existent-skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void testExecuteSkill() throws Exception {
        String requestBody = "{\"parameters\": {\"text\": \"hello world\"}}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/text-to-uppercase-skill/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.status").value("SUCCESS"));
    }

    @Test
    public void testDeleteSkill() throws Exception {
        String requestBody = "{\"skillId\": \"text-to-uppercase-skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testAddSkill() throws Exception {
        String requestBody = "{\"id\": \"test-skill\", \"name\": \"Test Skill\", \"description\": \"A test skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testUpdateSkill() throws Exception {
        String requestBody = "{\"id\": \"text-to-uppercase-skill\", \"name\": \"Updated Skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/skills/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testApproveSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/skills/text-to-uppercase-skill/approve")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testRejectSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/skills/text-to-uppercase-skill/reject")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
