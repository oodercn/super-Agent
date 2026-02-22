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
public class DeviceAssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllDevices_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testCreateDevice_Success() throws Exception {
        String deviceJson = "{" +
                "\"name\":\"测试设备\"," +
                "\"type\":\"light\"," +
                "\"ip\":\"192.168.1.200\"," +
                "\"mac\":\"AA:BB:CC:DD:EE:99\"," +
                "\"status\":\"online\"," +
                "\"description\":\"测试设备描述\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("测试设备"));
    }

    @Test
    public void testGetDeviceById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testUpdateDevice_NotFound() throws Exception {
        String deviceJson = "{\"name\":\"更新设备\",\"type\":\"light\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testDeleteDevice_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/devices/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testGetDevicesByType_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/type/light")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetDevicesByStatus_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/status/online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetDeviceStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCount").exists());
    }

    @Test
    public void testInitDefaultData_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/devices/init-default")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testFullDeviceLifecycle() throws Exception {
        // 1. Create device
        String deviceJson = "{" +
                "\"name\":\"生命周期测试设备\"," +
                "\"type\":\"sensor\"," +
                "\"ip\":\"192.168.1.201\"," +
                "\"mac\":\"AA:BB:CC:DD:EE:88\"," +
                "\"status\":\"online\"," +
                "\"description\":\"生命周期测试\"" +
                "}";

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract device ID
        String deviceId = extractDeviceId(response);

        // 2. Get device by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/" + deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("生命周期测试设备"));

        // 3. Update device
        String updateJson = "{\"name\":\"已更新的设备\",\"type\":\"sensor\",\"status\":\"offline\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/" + deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Delete device
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/devices/" + deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testDeviceTypes() throws Exception {
        // Create devices of different types
        String[] types = {"light", "socket", "lock", "sensor"};
        
        for (String type : types) {
            String deviceJson = "{" +
                    "\"name\":\"" + type + "设备\"," +
                    "\"type\":\"" + type + "\"," +
                    "\"ip\":\"192.168.1." + (100 + types.length) + "\"," +
                    "\"mac\":\"AA:BB:CC:DD:EE:" + type.substring(0, 2).toUpperCase() + "\"," +
                    "\"status\":\"online\"" +
                    "}";

            mockMvc.perform(MockMvcRequestBuilders.post("/api/devices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(deviceJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        // Verify each type
        for (String type : types) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/type/" + type)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }
    }

    private String extractDeviceId(String jsonResponse) {
        int idIndex = jsonResponse.indexOf("\"id\":\"");
        if (idIndex > 0) {
            int start = idIndex + 6;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end);
        }
        return null;
    }
}
