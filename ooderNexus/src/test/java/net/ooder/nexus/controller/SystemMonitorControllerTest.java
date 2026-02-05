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
public class SystemMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSystemHealth_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/system/monitor/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.timestamp").exists())
                .andExpect(jsonPath("$.data.uptime").exists())
                .andExpect(jsonPath("$.data.components").exists());
    }

    @Test
    public void testGetSystemMetrics_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/system/monitor/metrics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memory").exists())
                .andExpect(jsonPath("$.data.cpu").exists())
                .andExpect(jsonPath("$.data.disk").exists())
                .andExpect(jsonPath("$.data.network").exists())
                .andExpect(jsonPath("$.data.timestamp").exists());
    }

    @Test
    public void testGetMetricsHistory_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/system/monitor/metrics/history")
                        .param("hours", "24")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].memoryUsage").exists())
                .andExpect(jsonPath("$.data[0].cpuUsage").exists());
    }

    @Test
    public void testGetAlerts_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/system/monitor/alerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].level").exists())
                .andExpect(jsonPath("$.data[0].type").exists());
    }

    @Test
    public void testGetSystemInfo_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/system/monitor/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.systemInfo").exists())
                .andExpect(jsonPath("$.data.systemInfo.osName").exists())
                .andExpect(jsonPath("$.data.systemInfo.osVersion").exists())
                .andExpect(jsonPath("$.data.systemInfo.osArch").exists())
                .andExpect(jsonPath("$.data.systemInfo.javaVersion").exists());
    }
}
