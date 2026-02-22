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
public class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetStorageSpace_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/space")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRootFolderChildren_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/folder/children")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFolderChildren_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/folder/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"folderId\":\"./data/storage\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateFolder_Success() throws Exception {
        String folderJson = "{\"name\":\"test-folder\",\"parentId\":\"root\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/folder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(folderJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateFile_Success() throws Exception {
        String updateJson = "{\"name\":\"updated-name\",\"description\":\"updated description\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/storage/file/test-file-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFileVersions_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/file/test-file-id/versions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRestoreFileVersion_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/file/test-file-id/restore/version-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCleanupStorage_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/cleanup")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSharedFiles_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testShareFile_Success() throws Exception {
        String shareJson = "{\"fileId\":\"test-file-id\",\"sharedWith\":\"user123\",\"permission\":\"read\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetReceivedFiles_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/received")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
