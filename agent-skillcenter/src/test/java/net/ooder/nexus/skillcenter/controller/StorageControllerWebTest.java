package net.ooder.nexus.skillcenter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 存储API的web测试用例 - 符合 ooderNexus 规范
 */
@SpringBootTest
@AutoConfigureMockMvc
public class StorageControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetStorageStatus() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/status")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.path").isString())
                .andExpect(jsonPath("$.data.exists").isBoolean());
    }

    @Test
    public void testGetStorageStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/stats")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.path").isString())
                .andExpect(jsonPath("$.data.totalSize").isNumber())
                .andExpect(jsonPath("$.data.totalSizeHuman").isString())
                .andExpect(jsonPath("$.data.totalFiles").isNumber())
                .andExpect(jsonPath("$.data.totalDirectories").isNumber());
    }

    @Test
    public void testBackupStorage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/backup")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.success").isBoolean())
                .andExpect(jsonPath("$.data.message").isString());
    }

    @Test
    public void testGetBackupList() throws Exception {
        String requestBody = "{\"pageNum\": 1, \"pageSize\": 10}";
        ResultActions resultActions = mockMvc.perform(post("/api/storage/backups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.items").isArray());
    }

    @Test
    public void testRestoreStorage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/restore/test-backup.zip")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").isBoolean());
    }

    @Test
    public void testCleanStorage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/clean")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").isBoolean());
    }

    @Test
    public void testDeleteBackup() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/backups/test-backup.zip/delete")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").isBoolean());
    }

    @Test
    public void testGetStorageSettings() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/settings")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.storagePath").isString());
    }

    @Test
    public void testCleanBackups() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/clean/backups")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").isBoolean());
    }
}
