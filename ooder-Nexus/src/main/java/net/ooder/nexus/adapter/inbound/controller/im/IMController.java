package net.ooder.nexus.adapter.inbound.controller.im;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.im.model.Conversation;
import net.ooder.nexus.domain.im.model.IMMessage;
import net.ooder.nexus.domain.im.model.Contact;
import net.ooder.nexus.service.im.IMService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 即时通讯控制器
 * 
 * <p>提供即时通讯 API，支持：</p>
 * <ul>
 *   <li>个人对个人消息</li>
 *   <li>个人对部门消息</li>
 *   <li>群组聊天</li>
 * </ul>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/im", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class IMController {

    private static final Logger log = LoggerFactory.getLogger(IMController.class);

    @Autowired
    private IMService imService;

    /**
     * 获取会话列表
     */
    @PostMapping("/conversation/list")
    @ResponseBody
    public ListResultModel<List<Conversation>> getConversations(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        log.info("Get conversations request: userId={}", userId);
        ListResultModel<List<Conversation>> result = new ListResultModel<>();
        try {
            List<Conversation> conversations = imService.getConversations(userId);
            result.setData(conversations);
            result.setSize(conversations.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting conversations", e);
            result.setRequestStatus(500);
            result.setMessage("获取会话列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 创建会话
     */
    @PostMapping("/conversation/create")
    @ResponseBody
    public ResultModel<Conversation> createConversation(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String targetId = request.get("targetId");
        String targetType = request.get("targetType");
        String name = request.get("name");
        
        log.info("Create conversation request: userId={}, targetId={}, type={}", userId, targetId, targetType);
        ResultModel<Conversation> result = new ResultModel<>();
        try {
            Conversation conversation = imService.createConversation(userId, targetId, targetType, name);
            result.setData(conversation);
            result.setRequestStatus(200);
            result.setMessage("创建成功");
        } catch (Exception e) {
            log.error("Error creating conversation", e);
            result.setRequestStatus(500);
            result.setMessage("创建会话失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取会话详情
     */
    @PostMapping("/conversation/get")
    @ResponseBody
    public ResultModel<Conversation> getConversation(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        log.info("Get conversation request: conversationId={}", conversationId);
        ResultModel<Conversation> result = new ResultModel<>();
        try {
            Conversation conversation = imService.getConversation(conversationId);
            if (conversation != null) {
                result.setData(conversation);
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("会话不存在");
            }
        } catch (Exception e) {
            log.error("Error getting conversation", e);
            result.setRequestStatus(500);
            result.setMessage("获取会话失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送消息
     */
    @PostMapping("/message/send")
    @ResponseBody
    public ResultModel<IMMessage> sendMessage(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        String senderId = request.get("senderId");
        String senderName = request.get("senderName");
        String msgType = request.get("msgType");
        String content = request.get("content");
        
        log.info("Send message request: conversationId={}, senderId={}", conversationId, senderId);
        ResultModel<IMMessage> result = new ResultModel<>();
        try {
            IMMessage message = imService.sendMessage(conversationId, senderId, senderName, msgType, content);
            if (message != null) {
                result.setData(message);
                result.setRequestStatus(200);
                result.setMessage("发送成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("会话不存在");
            }
        } catch (Exception e) {
            log.error("Error sending message", e);
            result.setRequestStatus(500);
            result.setMessage("发送消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送文件消息
     */
    @PostMapping("/message/sendFile")
    @ResponseBody
    public ResultModel<IMMessage> sendFileMessage(@RequestBody Map<String, Object> request) {
        String conversationId = (String) request.get("conversationId");
        String senderId = (String) request.get("senderId");
        String senderName = (String) request.get("senderName");
        String fileName = (String) request.get("fileName");
        String filePath = (String) request.get("filePath");
        Long fileSize = request.get("fileSize") != null ? ((Number) request.get("fileSize")).longValue() : 0L;
        
        log.info("Send file message request: conversationId={}, fileName={}", conversationId, fileName);
        ResultModel<IMMessage> result = new ResultModel<>();
        try {
            IMMessage message = imService.sendFileMessage(conversationId, senderId, senderName, fileName, filePath, fileSize);
            if (message != null) {
                result.setData(message);
                result.setRequestStatus(200);
                result.setMessage("发送成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("会话不存在");
            }
        } catch (Exception e) {
            log.error("Error sending file message", e);
            result.setRequestStatus(500);
            result.setMessage("发送文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取消息列表
     */
    @PostMapping("/message/list")
    @ResponseBody
    public ListResultModel<List<IMMessage>> getMessages(@RequestBody Map<String, Object> request) {
        String conversationId = (String) request.get("conversationId");
        String lastMessageId = (String) request.get("lastMessageId");
        Integer limit = request.get("limit") != null ? ((Number) request.get("limit")).intValue() : 50;
        
        log.info("Get messages request: conversationId={}", conversationId);
        ListResultModel<List<IMMessage>> result = new ListResultModel<>();
        try {
            List<IMMessage> messages = imService.getMessages(conversationId, lastMessageId, limit);
            result.setData(messages);
            result.setSize(messages.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting messages", e);
            result.setRequestStatus(500);
            result.setMessage("获取消息列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 标记已读
     */
    @PostMapping("/message/read")
    @ResponseBody
    public ResultModel<Boolean> markAsRead(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        String userId = request.get("userId");
        String lastReadMessageId = request.get("lastReadMessageId");
        
        log.info("Mark as read request: conversationId={}, userId={}", conversationId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = imService.markAsRead(conversationId, userId, lastReadMessageId);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("标记成功");
        } catch (Exception e) {
            log.error("Error marking as read", e);
            result.setRequestStatus(500);
            result.setMessage("标记已读失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取未读数量
     */
    @PostMapping("/message/unread")
    @ResponseBody
    public ResultModel<Map<String, Object>> getUnreadCounts(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        log.info("Get unread counts request: userId={}", userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Integer> counts = imService.getUnreadCounts(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("unreadCounts", counts);
            data.put("totalUnread", counts.values().stream().mapToInt(Integer::intValue).sum());
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting unread counts", e);
            result.setRequestStatus(500);
            result.setMessage("获取未读数量失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 撤回消息
     */
    @PostMapping("/message/recall")
    @ResponseBody
    public ResultModel<Boolean> recallMessage(@RequestBody Map<String, String> request) {
        String messageId = request.get("messageId");
        String operatorId = request.get("operatorId");
        
        log.info("Recall message request: messageId={}, operatorId={}", messageId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = imService.recallMessage(messageId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "撤回成功" : "无权撤回此消息");
        } catch (Exception e) {
            log.error("Error recalling message", e);
            result.setRequestStatus(500);
            result.setMessage("撤回消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取联系人列表
     */
    @PostMapping("/contact/list")
    @ResponseBody
    public ListResultModel<List<Contact>> getContacts(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String contactType = request.get("contactType");
        
        log.info("Get contacts request: userId={}, type={}", userId, contactType);
        ListResultModel<List<Contact>> result = new ListResultModel<>();
        try {
            List<Contact> contacts = imService.getContacts(userId, contactType);
            result.setData(contacts);
            result.setSize(contacts.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting contacts", e);
            result.setRequestStatus(500);
            result.setMessage("获取联系人失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 搜索联系人
     */
    @PostMapping("/contact/search")
    @ResponseBody
    public ListResultModel<List<Contact>> searchContacts(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String keyword = request.get("keyword");
        
        log.info("Search contacts request: userId={}, keyword={}", userId, keyword);
        ListResultModel<List<Contact>> result = new ListResultModel<>();
        try {
            List<Contact> contacts = imService.searchContacts(userId, keyword);
            result.setData(contacts);
            result.setSize(contacts.size());
            result.setRequestStatus(200);
            result.setMessage("搜索成功");
        } catch (Exception e) {
            log.error("Error searching contacts", e);
            result.setRequestStatus(500);
            result.setMessage("搜索联系人失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 创建群组
     */
    @PostMapping("/group/create")
    @ResponseBody
    public ResultModel<Conversation> createGroup(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String ownerId = (String) request.get("ownerId");
        String ownerName = (String) request.get("ownerName");
        List<String> memberIds = (List<String>) request.get("memberIds");
        
        log.info("Create group request: name={}, owner={}", name, ownerId);
        ResultModel<Conversation> result = new ResultModel<>();
        try {
            Conversation conversation = imService.createGroup(name, ownerId, ownerName, memberIds);
            result.setData(conversation);
            result.setRequestStatus(200);
            result.setMessage("创建成功");
        } catch (Exception e) {
            log.error("Error creating group", e);
            result.setRequestStatus(500);
            result.setMessage("创建群组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 添加群组成员
     */
    @PostMapping("/group/member/add")
    @ResponseBody
    public ResultModel<Boolean> addGroupMember(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        String memberId = request.get("memberId");
        String memberName = request.get("memberName");
        String operatorId = request.get("operatorId");
        
        log.info("Add group member request: groupId={}, memberId={}", conversationId, memberId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = imService.addGroupMember(conversationId, memberId, memberName, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "添加成功" : "无权添加成员");
        } catch (Exception e) {
            log.error("Error adding group member", e);
            result.setRequestStatus(500);
            result.setMessage("添加成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 移除群组成员
     */
    @PostMapping("/group/member/remove")
    @ResponseBody
    public ResultModel<Boolean> removeGroupMember(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        String memberId = request.get("memberId");
        String operatorId = request.get("operatorId");
        
        log.info("Remove group member request: groupId={}, memberId={}", conversationId, memberId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = imService.removeGroupMember(conversationId, memberId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "移除成功" : "无权移除成员");
        } catch (Exception e) {
            log.error("Error removing group member", e);
            result.setRequestStatus(500);
            result.setMessage("移除成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取群组成员
     */
    @PostMapping("/group/members")
    @ResponseBody
    public ListResultModel<List<Contact>> getGroupMembers(@RequestBody Map<String, String> request) {
        String conversationId = request.get("conversationId");
        log.info("Get group members request: groupId={}", conversationId);
        ListResultModel<List<Contact>> result = new ListResultModel<>();
        try {
            List<Contact> members = imService.getGroupMembers(conversationId);
            result.setData(members);
            result.setSize(members.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting group members", e);
            result.setRequestStatus(500);
            result.setMessage("获取成员列表失败: " + e.getMessage());
        }
        return result;
    }
}
