package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupFile;
import net.ooder.nexus.dto.group.FileListDTO;
import net.ooder.nexus.dto.group.FileOperationDTO;
import net.ooder.nexus.dto.group.FileShareDTO;
import net.ooder.nexus.dto.group.FileUploadDTO;
import net.ooder.nexus.service.group.impl.GroupFileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 群组文件服务测试
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
class GroupFileServiceTest {

    private GroupFileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new GroupFileServiceImpl();
    }

    @Test
    void testUploadFile() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-001");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("document.pdf");
        dto.setFilePath("/files/document.pdf");
        dto.setFileSize(2048);
        dto.setFileType("pdf");
        dto.setMimeType("application/pdf");
        dto.setDescription("Test document");

        GroupFile file = fileService.uploadFile(dto);

        assertNotNull(file);
        assertNotNull(file.getFileId());
        assertEquals("test-group-001", file.getGroupId());
        assertEquals("user-001", file.getUploaderId());
        assertEquals("document.pdf", file.getFileName());
        assertEquals(2048, file.getFileSize());
        assertEquals("pdf", file.getFileType());
        assertEquals("active", file.getStatus());
        assertEquals(0, file.getDownloadCount());
    }

    @Test
    void testGetFileList() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-002");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("file1.pdf");
        dto.setFilePath("/files/file1.pdf");
        dto.setFileSize(1024);
        dto.setFileType("pdf");
        fileService.uploadFile(dto);

        dto.setFileName("file2.docx");
        dto.setFilePath("/files/file2.docx");
        dto.setFileType("docx");
        fileService.uploadFile(dto);

        FileListDTO listDTO = new FileListDTO();
        listDTO.setGroupId("test-group-002");

        List<GroupFile> files = fileService.getFileList(listDTO);

        assertNotNull(files);
        assertEquals(2, files.size());
    }

    @Test
    void testGetFileListByType() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-003");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("report.pdf");
        dto.setFilePath("/files/report.pdf");
        dto.setFileSize(1024);
        dto.setFileType("pdf");
        fileService.uploadFile(dto);

        dto.setFileName("image.png");
        dto.setFilePath("/files/image.png");
        dto.setFileType("image");
        fileService.uploadFile(dto);

        FileListDTO listDTO = new FileListDTO();
        listDTO.setGroupId("test-group-003");
        listDTO.setFileType("pdf");

        List<GroupFile> files = fileService.getFileList(listDTO);

        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals("pdf", files.get(0).getFileType());
    }

    @Test
    void testGetFile() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-004");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("test.txt");
        dto.setFilePath("/files/test.txt");
        dto.setFileSize(512);
        GroupFile uploadedFile = fileService.uploadFile(dto);

        GroupFile file = fileService.getFile(uploadedFile.getFileId());

        assertNotNull(file);
        assertEquals(uploadedFile.getFileId(), file.getFileId());
        assertEquals("test.txt", file.getFileName());
    }

    @Test
    void testDownloadFile() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-005");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("download-test.pdf");
        dto.setFilePath("/files/download-test.pdf");
        dto.setFileSize(4096);
        GroupFile uploadedFile = fileService.uploadFile(dto);

        FileOperationDTO opDTO = new FileOperationDTO();
        opDTO.setFileId(uploadedFile.getFileId());
        opDTO.setGroupId("test-group-005");
        opDTO.setOperatorId("user-002");

        String filePath = fileService.downloadFile(opDTO);

        assertNotNull(filePath);
        assertEquals("/files/download-test.pdf", filePath);

        GroupFile file = fileService.getFile(uploadedFile.getFileId());
        assertEquals(1, file.getDownloadCount());
    }

    @Test
    void testDeleteFile() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-006");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("delete-test.pdf");
        dto.setFilePath("/files/delete-test.pdf");
        dto.setFileSize(1024);
        GroupFile uploadedFile = fileService.uploadFile(dto);

        FileOperationDTO opDTO = new FileOperationDTO();
        opDTO.setFileId(uploadedFile.getFileId());
        opDTO.setGroupId("test-group-006");
        opDTO.setOperatorId("user-001");

        boolean result = fileService.deleteFile(opDTO);

        assertTrue(result);

        GroupFile file = fileService.getFile(uploadedFile.getFileId());
        assertEquals("deleted", file.getStatus());
    }

    @Test
    void testDeleteFileByOtherUser() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-007");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("other-user-file.pdf");
        dto.setFilePath("/files/other-user-file.pdf");
        dto.setFileSize(1024);
        GroupFile uploadedFile = fileService.uploadFile(dto);

        FileOperationDTO opDTO = new FileOperationDTO();
        opDTO.setFileId(uploadedFile.getFileId());
        opDTO.setGroupId("test-group-007");
        opDTO.setOperatorId("user-002");

        boolean result = fileService.deleteFile(opDTO);

        assertFalse(result);
    }

    @Test
    void testShareFile() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-008");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("share-test.pdf");
        dto.setFilePath("/files/share-test.pdf");
        dto.setFileSize(2048);
        GroupFile uploadedFile = fileService.uploadFile(dto);

        FileShareDTO shareDTO = new FileShareDTO();
        shareDTO.setFileId(uploadedFile.getFileId());
        shareDTO.setSourceGroupId("test-group-008");
        shareDTO.setTargetGroupId("test-group-009");
        shareDTO.setSharedBy("user-001");

        GroupFile sharedFile = fileService.shareFile(shareDTO);

        assertNotNull(sharedFile);
        assertNotNull(sharedFile.getFileId());
        assertNotEquals(uploadedFile.getFileId(), sharedFile.getFileId());
        assertEquals("test-group-009", sharedFile.getGroupId());
        assertEquals("share-test.pdf", sharedFile.getFileName());
        assertEquals("test-group-008", sharedFile.getSharedFrom());
    }

    @Test
    void testGetStorageStats() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-010");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("stats-file1.pdf");
        dto.setFilePath("/files/stats-file1.pdf");
        dto.setFileSize(1024);
        fileService.uploadFile(dto);

        dto.setFileName("stats-file2.pdf");
        dto.setFilePath("/files/stats-file2.pdf");
        dto.setFileSize(2048);
        fileService.uploadFile(dto);

        GroupFileService.StorageStats stats = fileService.getStorageStats("test-group-010");

        assertNotNull(stats);
        assertEquals(2, stats.getFileCount());
        assertEquals(3072, stats.getTotalSize());
        assertEquals(3072, stats.getUsedQuota());
        assertTrue(stats.getMaxQuota() > 0);
    }

    @Test
    void testSearchFiles() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-011");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("project-report.pdf");
        dto.setFilePath("/files/project-report.pdf");
        dto.setFileSize(1024);
        dto.setDescription("Project report document");
        fileService.uploadFile(dto);

        dto.setFileName("meeting-notes.docx");
        dto.setFilePath("/files/meeting-notes.docx");
        dto.setDescription("Meeting notes");
        fileService.uploadFile(dto);

        List<GroupFile> results = fileService.searchFiles("test-group-011", "project");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("project-report.pdf", results.get(0).getFileName());
    }

    @Test
    void testSearchFilesByDescription() {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setGroupId("test-group-012");
        dto.setUploaderId("user-001");
        dto.setUploaderName("Test User");
        dto.setFileName("doc1.pdf");
        dto.setFilePath("/files/doc1.pdf");
        dto.setFileSize(1024);
        dto.setDescription("Important financial report");
        fileService.uploadFile(dto);

        dto.setFileName("doc2.pdf");
        dto.setFilePath("/files/doc2.pdf");
        dto.setDescription("Technical documentation");
        fileService.uploadFile(dto);

        List<GroupFile> results = fileService.searchFiles("test-group-012", "financial");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("doc1.pdf", results.get(0).getFileName());
    }
}
