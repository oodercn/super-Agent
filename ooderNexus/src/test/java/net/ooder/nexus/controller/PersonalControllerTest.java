package net.ooder.nexus.controller;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class PersonalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPersonalDashboardStats_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/personal/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPersonalSkills_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/personal/skills")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetExecutionHistory_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/personal/execution/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSharedSkills_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/personal/sharing/shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetReceivedSkills_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/personal/sharing/received")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
