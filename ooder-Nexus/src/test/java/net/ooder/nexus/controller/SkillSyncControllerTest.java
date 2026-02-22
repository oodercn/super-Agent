package net.ooder.nexus.controller;

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
 * 技能同步控制器测试 (ResultModel 2.0 规范)
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class SkillSyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllTasks_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetTasksByStatus_Success() throws Exception {
        String requestJson = "{\"status\":\"PENDING\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/listByStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testCreateTask_Success() throws Exception {
        String taskJson = "{" +
                "\"name\":\"同步测试任务\"," +
                "\"type\":\"UPLOAD\"," +
                "\"source\":\"local\"," +
                "\"target\":\"remote\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("同步测试任务"));
    }

    @Test
    public void testExecuteTask_NotFound() throws Exception {
        String requestJson = "{\"id\":\"non-existent-id\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    public void testCancelTask_NotFound() throws Exception {
        String requestJson = "{\"id\":\"non-existent-id\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        String requestJson = "{\"id\":\"non-existent-id\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/tasks/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    public void testGetStatistics_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/statistics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testGetSyncableSkills_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/skills/syncable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetSyncedSkills_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/skills/synced")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testInitDefaultData_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sync/init-default")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }
}
