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
public class ListDataExtractTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllListTasks_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testCreateListTask_Success() throws Exception {
        String taskJson = "{" +
                "\"name\":\"列表测试任务\"," +
                "\"sourceType\":\"API\"," +
                "\"apiUrl\":\"http://example.com/api/data\"," +
                "\"apiMethod\":\"GET\"," +
                "\"targetCollection\":\"list-test-collection\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("列表测试任务"));
    }

    @Test
    public void testGetListTaskById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateListTask_NotFound() throws Exception {
        String taskJson = "{\"name\":\"更新列表任务\",\"sourceType\":\"API\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/list-tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDeleteListTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list-tasks/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testGetSourceTypes_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/source-types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetListTaskStatuses_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetFileFormats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/file-formats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetListTaskStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testExecuteListTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/non-existent-id/execute")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testTestConnection_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/non-existent-id/test-connection")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.success").value(false));
    }

    @Test
    public void testPreviewData_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/non-existent-id/preview")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testEnableTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/non-existent-id/enable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDisableTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/non-existent-id/disable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testFullListTaskLifecycle() throws Exception {
        // 1. Create list task
        String taskJson = "{" +
                "\"name\":\"列表生命周期测试\"," +
                "\"sourceType\":\"FILE\"," +
                "\"filePath\":\"/data/test.json\"," +
                "\"fileFormat\":\"JSON\"," +
                "\"targetCollection\":\"lifecycle-list-test\"" +
                "}";

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract task ID
        String taskId = extractTaskId(response);

        // 2. Get task by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("列表生命周期测试"));

        // 3. Update task
        String updateJson = "{\"name\":\"已更新的列表任务\",\"sourceType\":\"FILE\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/list-tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Enable task
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/" + taskId + "/enable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 5. Disable task
        mockMvc.perform(MockMvcRequestBuilders.post("/api/list-tasks/" + taskId + "/disable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 6. Delete task
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list-tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    private String extractTaskId(String jsonResponse) {
        int idIndex = jsonResponse.indexOf("\"id\":\"");
        if (idIndex > 0) {
            int start = idIndex + 6;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end);
        }
        return null;
    }
}
