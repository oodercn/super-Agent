package net.ooder.skillcenter.controller;

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
 * 存储API的web测试用例
 */
@SpringBootTest
@AutoConfigureMockMvc
public class StorageControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // 初始化设置
    }

    /**
     * 测试获取存储状态
     */
    @Test
    public void testGetStorageStatus() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/storage/status")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.path").isString())
                .andExpect(jsonPath("$.data.exists").isBoolean());
    }

    /**
     * 测试获取存储统计信息
     */
    @Test
    public void testGetStorageStats() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/storage/stats")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.path").isString())
                .andExpect(jsonPath("$.data.totalSize").isNumber())
                .andExpect(jsonPath("$.data.totalSizeHuman").isString())
                .andExpect(jsonPath("$.data.totalFiles").isNumber())
                .andExpect(jsonPath("$.data.totalDirectories").isNumber());
    }

    /**
     * 测试备份存储
     */
    @Test
    public void testBackupStorage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/backup")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.success").isBoolean())
                .andExpect(jsonPath("$.data.message").isString());
    }

    /**
     * 测试获取备份列表
     */
    @Test
    public void testGetBackupList() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/storage/backups")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    /**
     * 测试恢复存储
     */
    @Test
    public void testRestoreStorage() throws Exception {
        // 注意：这里需要一个实际存在的备份文件名，否则会返回失败
        ResultActions resultActions = mockMvc.perform(post("/api/storage/restore/test-backup.zip")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.message").isString());
    }

    /**
     * 测试清理存储
     */
    @Test
    public void testCleanStorage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/storage/clean")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.message").isString());
    }

    /**
     * 测试删除备份
     */
    @Test
    public void testDeleteBackup() throws Exception {
        // 注意：这里需要一个实际存在的备份文件名，否则会返回失败
        ResultActions resultActions = mockMvc.perform(delete("/api/storage/backups/test-backup.zip")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.message").isString());
    }
}
