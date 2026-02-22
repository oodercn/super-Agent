package net.ooder.nexus.adapter.inbound.controller.group;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.group.model.GroupMessage;
import net.ooder.nexus.dto.group.MessageListDTO;
import net.ooder.nexus.dto.group.MessageReadDTO;
import net.ooder.nexus.dto.group.MessageSendDTO;
import net.ooder.nexus.service.group.GroupMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组消息控制器
 * 
 * <p>提供群组即时消息的发送、接收、历史查询等 API。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/group/message", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class GroupMessageController {

    private static final Logger log = LoggerFactory.getLogger(GroupMessageController.class);

    @Autowired
    private GroupMessageService groupMessageService;

    /**
     * 发送消息
     */
    @PostMapping("/send")
    @ResponseBody
    public ResultModel<GroupMessage> sendMessage(@RequestBody MessageSendDTO dto) {
        log.info("Send message request: groupId={}, senderId={}", dto.getGroupId(), dto.getSenderId());
        ResultModel<GroupMessage> result = new ResultModel<>();
        try {
            GroupMessage message = groupMessageService.sendMessage(dto);
            result.setData(message);
            result.setRequestStatus(200);
            result.setMessage("消息发送成功");
        } catch (Exception e) {
            log.error("Error sending message", e);
            result.setRequestStatus(500);
            result.setMessage("消息发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取消息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<GroupMessage>> getMessageList(@RequestBody MessageListDTO dto) {
        log.info("Get message list request: groupId={}", dto.getGroupId());
        ListResultModel<List<GroupMessage>> result = new ListResultModel<>();
        try {
            List<GroupMessage> messages = groupMessageService.getMessageList(dto);
            result.setData(messages);
            result.setSize(messages.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting message list", e);
            result.setRequestStatus(500);
            result.setMessage("获取消息列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取历史消息
     */
    @PostMapping("/history")
    @ResponseBody
    public ListResultModel<List<GroupMessage>> getMessageHistory(@RequestBody Map<String, Object> request) {
        String groupId = (String) request.get("groupId");
        Long beforeTime = request.get("beforeTime") != null ? 
                ((Number) request.get("beforeTime")).longValue() : null;
        Integer limit = request.get("limit") != null ? 
                ((Number) request.get("limit")).intValue() : 50;
        
        log.info("Get message history request: groupId={}", groupId);
        ListResultModel<List<GroupMessage>> result = new ListResultModel<>();
        try {
            List<GroupMessage> messages = groupMessageService.getMessageHistory(groupId, beforeTime, limit);
            result.setData(messages);
            result.setSize(messages.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting message history", e);
            result.setRequestStatus(500);
            result.setMessage("获取历史消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 标记消息已读
     */
    @PostMapping("/read")
    @ResponseBody
    public ResultModel<Boolean> markAsRead(@RequestBody MessageReadDTO dto) {
        log.info("Mark as read request: groupId={}, userId={}", dto.getGroupId(), dto.getUserId());
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = groupMessageService.markAsRead(dto);
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
     * 获取未读消息数量
     */
    @PostMapping("/unread")
    @ResponseBody
    public ResultModel<Map<String, Object>> getUnreadCount(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String userId = request.get("userId");
        
        log.info("Get unread count request: groupId={}, userId={}", groupId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> data = new HashMap<>();
            if (groupId != null) {
                int count = groupMessageService.getUnreadCount(groupId, userId);
                data.put("groupId", groupId);
                data.put("unreadCount", count);
            } else {
                Map<String, Integer> allCounts = groupMessageService.getAllUnreadCounts(userId);
                data.put("unreadCounts", allCounts);
                data.put("totalUnread", allCounts.values().stream().mapToInt(Integer::intValue).sum());
            }
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting unread count", e);
            result.setRequestStatus(500);
            result.setMessage("获取未读数量失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 撤回消息
     */
    @PostMapping("/recall")
    @ResponseBody
    public ResultModel<Boolean> recallMessage(@RequestBody Map<String, String> request) {
        String messageId = request.get("messageId");
        String operatorId = request.get("operatorId");
        
        log.info("Recall message request: messageId={}, operatorId={}", messageId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = groupMessageService.recallMessage(messageId, operatorId);
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
     * 删除消息
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteMessage(@RequestBody Map<String, String> request) {
        String messageId = request.get("messageId");
        String operatorId = request.get("operatorId");
        
        log.info("Delete message request: messageId={}, operatorId={}", messageId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = groupMessageService.deleteMessage(messageId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "删除成功" : "无权删除此消息");
        } catch (Exception e) {
            log.error("Error deleting message", e);
            result.setRequestStatus(500);
            result.setMessage("删除消息失败: " + e.getMessage());
        }
        return result;
    }
}
