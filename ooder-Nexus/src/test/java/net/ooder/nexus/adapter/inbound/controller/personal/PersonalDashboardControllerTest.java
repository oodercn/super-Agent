package net.ooder.nexus.adapter.inbound.controller.personal;

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
 * 个人仪表盘控制器测试
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class PersonalDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetDashboardStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalSkills").exists())
                .andExpect(jsonPath("$.data.totalExecutions").exists())
                .andExpect(jsonPath("$.data.sharedSkills").exists())
                .andExpect(jsonPath("$.data.receivedSkills").exists())
                .andExpect(jsonPath("$.data.groupCount").exists())
                .andExpect(jsonPath("$.data.recentActivities").exists());
    }

    @Test
    public void testGetRecentActivities_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/dashboard/activities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetSkillStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/dashboard/skill-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalSkills").exists())
                .andExpect(jsonPath("$.data.activeSkills").exists())
                .andExpect(jsonPath("$.data.inactiveSkills").exists());
    }

    @Test
    public void testGetExecutionStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/dashboard/execution-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.totalExecutions").exists())
                .andExpect(jsonPath("$.data.successCount").exists())
                .andExpect(jsonPath("$.data.failureCount").exists());
    }

    @Test
    public void testGetSharingStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/personal/dashboard/sharing-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.sharedCount").exists())
                .andExpect(jsonPath("$.data.receivedCount").exists());
    }
}
