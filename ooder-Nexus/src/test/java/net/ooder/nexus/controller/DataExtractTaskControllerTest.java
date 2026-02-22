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

@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class DataExtractTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllTasks_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testCreateTask_Success() throws Exception {
        String taskJson = "{" +
                "\"name\":\"测试任务\"," +
                "\"type\":\"API\"," +
                "\"description\":\"测试数据抽取任务\"," +
                "\"sourceType\":\"API\"," +
                "\"targetCollection\":\"test-collection\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("测试任务"));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        String taskJson = "{\"name\":\"更新任务\",\"type\":\"API\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testGetTaskTypes_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetTaskStatuses_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetTaskStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testExecuteTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/non-existent-id/execute")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testFullTaskLifecycle() throws Exception {
        // 1. Create task
        String taskJson = "{" +
                "\"name\":\"生命周期测试任务\"," +
                "\"type\":\"DATABASE\"," +
                "\"description\":\"测试完整生命周期\"," +
                "\"sourceType\":\"DATABASE\"," +
                "\"targetCollection\":\"lifecycle-test\"" +
                "}";

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract task ID from response
        String taskId = extractTaskId(response);

        // 2. Get task by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("生命周期测试任务"));

        // 3. Update task
        String updateJson = "{\"name\":\"已更新的任务\",\"type\":\"DATABASE\",\"description\":\"已更新\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Delete task
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    private String extractTaskId(String jsonResponse) {
        // Simple extraction, in real tests use JsonPath or ObjectMapper
        int idIndex = jsonResponse.indexOf("\"id\":\"");
        if (idIndex > 0) {
            int start = idIndex + 6;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end);
        }
        return null;
    }
}
