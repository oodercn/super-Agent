package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupMessage;
import net.ooder.nexus.dto.group.MessageListDTO;
import net.ooder.nexus.dto.group.MessageReadDTO;
import net.ooder.nexus.dto.group.MessageSendDTO;
import net.ooder.nexus.service.group.impl.GroupMessageServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 群组消息服务测试
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
class GroupMessageServiceTest {

    private GroupMessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new GroupMessageServiceImpl();
    }

    @Test
    void testSendMessage() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-001");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setMsgType("text");
        dto.setContent("Hello, World!");

        GroupMessage message = messageService.sendMessage(dto);

        assertNotNull(message);
        assertNotNull(message.getMessageId());
        assertEquals("test-group-001", message.getGroupId());
        assertEquals("user-001", message.getSenderId());
        assertEquals("Test User", message.getSenderName());
        assertEquals("text", message.getMsgType());
        assertEquals("Hello, World!", message.getContent());
        assertEquals("sent", message.getStatus());
    }

    @Test
    void testGetMessageList() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-002");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Test message 1");
        messageService.sendMessage(dto);

        dto.setContent("Test message 2");
        messageService.sendMessage(dto);

        MessageListDTO listDTO = new MessageListDTO();
        listDTO.setGroupId("test-group-002");

        List<GroupMessage> messages = messageService.getMessageList(listDTO);

        assertNotNull(messages);
        assertEquals(2, messages.size());
    }

    @Test
    void testGetMessageHistory() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-003");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("History message 1");
        messageService.sendMessage(dto);

        dto.setContent("History message 2");
        messageService.sendMessage(dto);

        List<GroupMessage> history = messageService.getMessageHistory("test-group-003", null, 10);

        assertNotNull(history);
        assertEquals(2, history.size());
    }

    @Test
    void testMarkAsRead() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-004");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Message to read");
        GroupMessage message = messageService.sendMessage(dto);

        MessageReadDTO readDTO = new MessageReadDTO();
        readDTO.setGroupId("test-group-004");
        readDTO.setUserId("user-002");
        readDTO.setLastReadMessageId(message.getMessageId());

        boolean result = messageService.markAsRead(readDTO);

        assertTrue(result);
    }

    @Test
    void testGetUnreadCount() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-005");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Unread message 1");
        messageService.sendMessage(dto);

        dto.setContent("Unread message 2");
        messageService.sendMessage(dto);

        int unreadCount = messageService.getUnreadCount("test-group-005", "user-002");

        assertEquals(2, unreadCount);
    }

    @Test
    void testGetAllUnreadCounts() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-006");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Message for unread count");
        messageService.sendMessage(dto);

        Map<String, Integer> counts = messageService.getAllUnreadCounts("user-002");

        assertNotNull(counts);
        assertTrue(counts.containsKey("test-group-006"));
        assertEquals(1, counts.get("test-group-006"));
    }

    @Test
    void testRecallMessage() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-007");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Message to recall");
        GroupMessage message = messageService.sendMessage(dto);

        boolean result = messageService.recallMessage(message.getMessageId(), "user-001");

        assertTrue(result);

        MessageListDTO listDTO = new MessageListDTO();
        listDTO.setGroupId("test-group-007");
        List<GroupMessage> messages = messageService.getMessageList(listDTO);
        
        GroupMessage recalledMessage = messages.stream()
                .filter(m -> m.getMessageId().equals(message.getMessageId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(recalledMessage);
        assertEquals("recalled", recalledMessage.getStatus());
    }

    @Test
    void testRecallMessageByOtherUser() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-008");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Message from user-001");
        GroupMessage message = messageService.sendMessage(dto);

        boolean result = messageService.recallMessage(message.getMessageId(), "user-002");

        assertFalse(result);
    }

    @Test
    void testDeleteMessage() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-009");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setContent("Message to delete");
        GroupMessage message = messageService.sendMessage(dto);

        boolean result = messageService.deleteMessage(message.getMessageId(), "user-001");

        assertTrue(result);
    }

    @Test
    void testFileMessage() {
        MessageSendDTO dto = new MessageSendDTO();
        dto.setGroupId("test-group-010");
        dto.setSenderId("user-001");
        dto.setSenderName("Test User");
        dto.setMsgType("file");
        dto.setFileName("test.pdf");
        dto.setFilePath("/files/test.pdf");
        dto.setFileSize(1024);

        GroupMessage message = messageService.sendMessage(dto);

        assertNotNull(message);
        assertEquals("file", message.getMsgType());
        assertEquals("test.pdf", message.getFileName());
        assertEquals("/files/test.pdf", message.getFilePath());
        assertEquals(1024, message.getFileSize());
    }
}
