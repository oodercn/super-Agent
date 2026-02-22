package net.ooder.nexus.service.group.impl;

import net.ooder.nexus.domain.group.model.GroupMessage;
import net.ooder.nexus.dto.group.MessageListDTO;
import net.ooder.nexus.dto.group.MessageReadDTO;
import net.ooder.nexus.dto.group.MessageSendDTO;
import net.ooder.nexus.service.group.GroupMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 群组消息服务实现
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service("groupMessageServiceImpl")
public class GroupMessageServiceImpl implements GroupMessageService {

    private static final Logger log = LoggerFactory.getLogger(GroupMessageServiceImpl.class);

    private final ConcurrentHashMap<String, List<GroupMessage>> groupMessages = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, String>> userLastRead = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<MessageListener>> groupListeners = new ConcurrentHashMap<>();

    @Override
    public GroupMessage sendMessage(MessageSendDTO dto) {
        log.info("Sending message to group: {}", dto.getGroupId());
        
        GroupMessage message = new GroupMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setGroupId(dto.getGroupId());
        message.setSenderId(dto.getSenderId());
        message.setSenderName(dto.getSenderName());
        message.setMsgType(dto.getMsgType() != null ? dto.getMsgType() : "text");
        message.setContent(dto.getContent());
        message.setFilePath(dto.getFilePath());
        message.setFileName(dto.getFileName());
        message.setFileSize(dto.getFileSize());
        message.setReplyTo(dto.getReplyTo());
        message.setSendTime(System.currentTimeMillis());
        message.setStatus("sent");

        groupMessages.computeIfAbsent(dto.getGroupId(), k -> new CopyOnWriteArrayList<>()).add(message);
        
        notifyListeners(dto.getGroupId(), message);
        
        log.info("Message sent: {}", message.getMessageId());
        return message;
    }

    @Override
    public List<GroupMessage> getMessageList(MessageListDTO dto) {
        log.info("Getting message list for group: {}", dto.getGroupId());
        
        List<GroupMessage> messages = groupMessages.getOrDefault(dto.getGroupId(), new ArrayList<>());
        
        if (dto.getAfterTime() != null) {
            messages = messages.stream()
                    .filter(m -> m.getSendTime() > dto.getAfterTime())
                    .collect(Collectors.toList());
        }
        
        if (dto.getBeforeTime() != null) {
            messages = messages.stream()
                    .filter(m -> m.getSendTime() < dto.getBeforeTime())
                    .collect(Collectors.toList());
        }
        
        if (dto.getMsgType() != null) {
            messages = messages.stream()
                    .filter(m -> dto.getMsgType().equals(m.getMsgType()))
                    .collect(Collectors.toList());
        }
        
        int limit = dto.getLimit() != null ? dto.getLimit() : 50;
        if (messages.size() > limit) {
            messages = messages.subList(messages.size() - limit, messages.size());
        }
        
        return messages;
    }

    @Override
    public List<GroupMessage> getMessageHistory(String groupId, Long beforeTime, Integer limit) {
        log.info("Getting message history for group: {}", groupId);
        
        List<GroupMessage> messages = groupMessages.getOrDefault(groupId, new ArrayList<>());
        
        if (beforeTime != null) {
            messages = messages.stream()
                    .filter(m -> m.getSendTime() < beforeTime)
                    .collect(Collectors.toList());
        }
        
        int size = limit != null ? limit : 50;
        if (messages.size() > size) {
            messages = messages.subList(messages.size() - size, messages.size());
        }
        
        return messages;
    }

    @Override
    public boolean markAsRead(MessageReadDTO dto) {
        log.info("Marking messages as read for user: {} in group: {}", dto.getUserId(), dto.getGroupId());
        
        Map<String, String> groupRead = userLastRead.computeIfAbsent(dto.getGroupId(), k -> new ConcurrentHashMap<>());
        groupRead.put(dto.getUserId(), dto.getLastReadMessageId());
        
        return true;
    }

    @Override
    public int getUnreadCount(String groupId, String userId) {
        List<GroupMessage> messages = groupMessages.getOrDefault(groupId, new ArrayList<>());
        Map<String, String> groupRead = userLastRead.get(groupId);
        
        if (groupRead == null || !groupRead.containsKey(userId)) {
            return messages.size();
        }
        
        String lastReadId = groupRead.get(userId);
        if (lastReadId == null) {
            return messages.size();
        }
        
        int count = 0;
        for (int i = messages.size() - 1; i >= 0; i--) {
            GroupMessage msg = messages.get(i);
            if (msg.getMessageId().equals(lastReadId)) {
                break;
            }
            count++;
        }
        
        return count;
    }

    @Override
    public Map<String, Integer> getAllUnreadCounts(String userId) {
        Map<String, Integer> result = new HashMap<>();
        
        for (String groupId : groupMessages.keySet()) {
            int count = getUnreadCount(groupId, userId);
            if (count > 0) {
                result.put(groupId, count);
            }
        }
        
        return result;
    }

    @Override
    public boolean recallMessage(String messageId, String operatorId) {
        log.info("Recalling message: {} by user: {}", messageId, operatorId);
        
        for (List<GroupMessage> messages : groupMessages.values()) {
            for (GroupMessage msg : messages) {
                if (msg.getMessageId().equals(messageId)) {
                    if (msg.getSenderId().equals(operatorId)) {
                        msg.setStatus("recalled");
                        msg.setContent("消息已被撤回");
                        return true;
                    }
                    return false;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean deleteMessage(String messageId, String operatorId) {
        log.info("Deleting message: {} by user: {}", messageId, operatorId);
        
        for (Map.Entry<String, List<GroupMessage>> entry : groupMessages.entrySet()) {
            List<GroupMessage> messages = entry.getValue();
            for (int i = 0; i < messages.size(); i++) {
                GroupMessage msg = messages.get(i);
                if (msg.getMessageId().equals(messageId)) {
                    if (msg.getSenderId().equals(operatorId)) {
                        messages.remove(i);
                        return true;
                    }
                    return false;
                }
            }
        }
        
        return false;
    }

    @Override
    public void addMessageListener(String groupId, MessageListener listener) {
        groupListeners.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    @Override
    public void removeMessageListener(String groupId, MessageListener listener) {
        List<MessageListener> listeners = groupListeners.get(groupId);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners(String groupId, GroupMessage message) {
        List<MessageListener> listeners = groupListeners.get(groupId);
        if (listeners != null) {
            for (MessageListener listener : listeners) {
                try {
                    listener.onMessage(message);
                } catch (Exception e) {
                    log.error("Error notifying listener", e);
                }
            }
        }
    }
}
