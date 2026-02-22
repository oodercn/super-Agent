package net.ooder.nexus.service.im.impl;

import net.ooder.nexus.domain.im.model.Conversation;
import net.ooder.nexus.domain.im.model.IMMessage;
import net.ooder.nexus.domain.im.model.Contact;
import net.ooder.nexus.service.im.IMService;

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

import javax.annotation.PostConstruct;

/**
 * 即时通讯服务实现
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service("imServiceImpl")
public class IMServiceImpl implements IMService {

    private static final Logger log = LoggerFactory.getLogger(IMServiceImpl.class);

    private final ConcurrentHashMap<String, List<Conversation>> userConversations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Conversation> conversations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<IMMessage>> conversationMessages = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, String>> conversationReadStatus = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Contact>> userContacts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> groupMembers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> groupOwners = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<MessageListener>> userListeners = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        initDefaultContacts();
        initDefaultGroups();
        log.info("IMService initialized with default data");
    }
    
    private void initDefaultContacts() {
        List<Contact> defaultContacts = new ArrayList<Contact>();
        
        Contact c1 = new Contact();
        c1.setContactId("user-002");
        c1.setContactType("user");
        c1.setName("张三");
        c1.setDepartmentName("技术部");
        c1.setOnline(true);
        defaultContacts.add(c1);
        
        Contact c2 = new Contact();
        c2.setContactId("user-003");
        c2.setContactType("user");
        c2.setName("李四");
        c2.setDepartmentName("产品部");
        c2.setOnline(false);
        defaultContacts.add(c2);
        
        Contact c3 = new Contact();
        c3.setContactId("user-004");
        c3.setContactType("user");
        c3.setName("王五");
        c3.setDepartmentName("技术部");
        c3.setOnline(true);
        defaultContacts.add(c3);
        
        Contact c4 = new Contact();
        c4.setContactId("dept-001");
        c4.setContactType("department");
        c4.setName("技术部");
        c4.setOnline(false);
        defaultContacts.add(c4);
        
        Contact c5 = new Contact();
        c5.setContactId("dept-002");
        c5.setContactType("department");
        c5.setName("产品部");
        c5.setOnline(false);
        defaultContacts.add(c5);
        
        Contact c6 = new Contact();
        c6.setContactId("group-001");
        c6.setContactType("group");
        c6.setName("项目A群组");
        c6.setOnline(false);
        defaultContacts.add(c6);
        
        Contact c7 = new Contact();
        c7.setContactId("group-002");
        c7.setContactType("group");
        c7.setName("技术交流群");
        c7.setOnline(false);
        defaultContacts.add(c7);
        
        userContacts.put("user-001", new CopyOnWriteArrayList<>(defaultContacts));
    }
    
    private void initDefaultGroups() {
        Conversation group1 = new Conversation();
        group1.setConversationId("group-001");
        group1.setConversationType("group");
        group1.setName("财务部协作组");
        group1.setTargetId("group-001");
        group1.setTargetType("group");
        group1.setCreatedAt(System.currentTimeMillis() - 86400000 * 7);
        group1.setUpdatedAt(System.currentTimeMillis());
        group1.setLastMessage("好的，收到");
        group1.setLastMessageTime(System.currentTimeMillis() - 3600000);
        group1.setStatus("active");
        
        conversations.put("group-001", group1);
        groupOwners.put("group-001", "user-001");
        groupMembers.put("group-001", new CopyOnWriteArrayList<String>() {{
            add("user-001");
            add("user-002");
            add("user-003");
        }});
        
        Conversation group2 = new Conversation();
        group2.setConversationId("group-002");
        group2.setConversationType("group");
        group2.setName("项目组A");
        group2.setTargetId("group-002");
        group2.setTargetType("group");
        group2.setCreatedAt(System.currentTimeMillis() - 86400000 * 3);
        group2.setUpdatedAt(System.currentTimeMillis());
        group2.setLastMessage("项目进度更新");
        group2.setLastMessageTime(System.currentTimeMillis() - 7200000);
        group2.setStatus("active");
        
        conversations.put("group-002", group2);
        groupOwners.put("group-002", "user-002");
        groupMembers.put("group-002", new CopyOnWriteArrayList<String>() {{
            add("user-001");
            add("user-002");
        }});
        
        Conversation dept1 = new Conversation();
        dept1.setConversationId("dept-001");
        dept1.setConversationType("department");
        dept1.setName("技术部");
        dept1.setTargetId("dept-001");
        dept1.setTargetType("department");
        dept1.setCreatedAt(System.currentTimeMillis() - 86400000 * 30);
        dept1.setUpdatedAt(System.currentTimeMillis());
        dept1.setLastMessage("周报已发送");
        dept1.setLastMessageTime(System.currentTimeMillis() - 86400000);
        dept1.setStatus("active");
        
        conversations.put("dept-001", dept1);
        groupMembers.put("dept-001", new CopyOnWriteArrayList<String>() {{
            add("user-001");
            add("user-002");
            add("user-004");
        }});
        
        userConversations.put("user-001", new CopyOnWriteArrayList<Conversation>() {{
            add(group1);
            add(group2);
            add(dept1);
        }});
    }

    @Override
    public List<Conversation> getConversations(String userId) {
        log.info("Getting conversations for user: {}", userId);
        List<Conversation> convs = userConversations.getOrDefault(userId, new ArrayList<>());
        return convs.stream()
                .filter(c -> "active".equals(c.getStatus()))
                .sorted((a, b) -> Long.compare(b.getLastMessageTime(), a.getLastMessageTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Conversation createConversation(String userId, String targetId, String targetType, String name) {
        log.info("Creating conversation: userId={}, targetId={}, type={}", userId, targetId, targetType);
        
        String conversationId = generateConversationId(userId, targetId, targetType);
        
        if (conversations.containsKey(conversationId)) {
            Conversation existing = conversations.get(conversationId);
            return existing;
        }
        
        Conversation conversation = new Conversation();
        conversation.setConversationId(conversationId);
        conversation.setConversationType(targetType);
        conversation.setName(name);
        conversation.setTargetId(targetId);
        conversation.setTargetType(targetType);
        conversation.setCreatedAt(System.currentTimeMillis());
        conversation.setUpdatedAt(System.currentTimeMillis());
        
        conversations.put(conversationId, conversation);
        
        userConversations.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(conversation);
        if (!"private".equals(targetType)) {
            userConversations.computeIfAbsent(targetId, k -> new CopyOnWriteArrayList<>()).add(conversation);
        }
        
        return conversation;
    }

    @Override
    public Conversation getConversation(String conversationId) {
        return conversations.get(conversationId);
    }

    @Override
    public IMMessage sendMessage(String conversationId, String senderId, String senderName, String msgType, String content) {
        log.info("Sending message to conversation: {}", conversationId);
        
        Conversation conversation = conversations.get(conversationId);
        if (conversation == null) {
            return null;
        }
        
        IMMessage message = new IMMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setConversationId(conversationId);
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setMsgType(msgType != null ? msgType : "text");
        message.setContent(content);
        message.setSendTime(System.currentTimeMillis());
        message.setStatus("sent");
        
        conversationMessages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>()).add(message);
        
        conversation.setLastMessage(content);
        conversation.setLastMessageTime(message.getSendTime());
        conversation.setUpdatedAt(System.currentTimeMillis());
        
        notifyMessageListeners(conversationId, message);
        
        return message;
    }

    @Override
    public IMMessage sendFileMessage(String conversationId, String senderId, String senderName, 
                                     String fileName, String filePath, long fileSize) {
        log.info("Sending file message to conversation: {}", conversationId);
        
        IMMessage message = sendMessage(conversationId, senderId, senderName, "file", "[文件] " + fileName);
        if (message != null) {
            message.setFileName(fileName);
            message.setFilePath(filePath);
            message.setFileSize(fileSize);
        }
        
        return message;
    }

    @Override
    public List<IMMessage> getMessages(String conversationId, String lastMessageId, int limit) {
        log.info("Getting messages for conversation: {}", conversationId);
        
        List<IMMessage> messages = conversationMessages.getOrDefault(conversationId, new ArrayList<>());
        
        if (lastMessageId != null) {
            int index = -1;
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getMessageId().equals(lastMessageId)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                messages = messages.subList(0, index);
            } else if (index == 0) {
                return new ArrayList<>();
            }
        }
        
        if (messages.size() > limit) {
            messages = messages.subList(messages.size() - limit, messages.size());
        }
        
        return messages;
    }

    @Override
    public boolean markAsRead(String conversationId, String userId, String lastReadMessageId) {
        log.info("Marking as read: conversation={}, user={}", conversationId, userId);
        
        Map<String, String> readStatus = conversationReadStatus.computeIfAbsent(conversationId, k -> new ConcurrentHashMap<>());
        readStatus.put(userId, lastReadMessageId);
        
        Conversation conversation = conversations.get(conversationId);
        if (conversation != null) {
            List<IMMessage> messages = conversationMessages.get(conversationId);
            if (messages != null) {
                int unread = 0;
                String lastReadId = readStatus.get(userId);
                if (lastReadId != null) {
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        if (messages.get(i).getMessageId().equals(lastReadId)) {
                            break;
                        }
                        if (!messages.get(i).getSenderId().equals(userId)) {
                            unread++;
                        }
                    }
                } else {
                    unread = (int) messages.stream().filter(m -> !m.getSenderId().equals(userId)).count();
                }
                conversation.setUnreadCount(unread);
            }
        }
        
        return true;
    }

    @Override
    public Map<String, Integer> getUnreadCounts(String userId) {
        log.info("Getting unread counts for user: {}", userId);
        
        Map<String, Integer> result = new HashMap<>();
        List<Conversation> convs = userConversations.getOrDefault(userId, new ArrayList<>());
        
        for (Conversation conv : convs) {
            int unread = calculateUnreadCount(conv.getConversationId(), userId);
            if (unread > 0) {
                result.put(conv.getConversationId(), unread);
            }
        }
        
        return result;
    }

    @Override
    public boolean recallMessage(String messageId, String operatorId) {
        log.info("Recalling message: {} by user: {}", messageId, operatorId);
        
        for (List<IMMessage> messages : conversationMessages.values()) {
            for (IMMessage msg : messages) {
                if (msg.getMessageId().equals(messageId)) {
                    if (msg.getSenderId().equals(operatorId)) {
                        msg.setRecalled(true);
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
    public List<Contact> getContacts(String userId, String contactType) {
        log.info("Getting contacts for user: {}, type: {}", userId, contactType);
        
        List<Contact> contacts = userContacts.getOrDefault(userId, new ArrayList<>());
        
        if (contactType != null) {
            contacts = contacts.stream()
                    .filter(c -> contactType.equals(c.getContactType()))
                    .collect(Collectors.toList());
        }
        
        return contacts;
    }

    @Override
    public List<Contact> searchContacts(String userId, String keyword) {
        log.info("Searching contacts for user: {}, keyword: {}", userId, keyword);
        
        List<Contact> contacts = userContacts.getOrDefault(userId, new ArrayList<>());
        String lowerKeyword = keyword.toLowerCase();
        
        return contacts.stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerKeyword) ||
                        (c.getDepartmentName() != null && c.getDepartmentName().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    @Override
    public Conversation createGroup(String name, String ownerId, String ownerName, List<String> memberIds) {
        log.info("Creating group: {}, owner: {}", name, ownerId);
        
        String conversationId = "group-" + UUID.randomUUID().toString();
        
        Conversation conversation = new Conversation();
        conversation.setConversationId(conversationId);
        conversation.setConversationType("group");
        conversation.setName(name);
        conversation.setTargetId(conversationId);
        conversation.setTargetType("group");
        conversation.setCreatedAt(System.currentTimeMillis());
        conversation.setUpdatedAt(System.currentTimeMillis());
        
        conversations.put(conversationId, conversation);
        groupOwners.put(conversationId, ownerId);
        
        List<String> members = new CopyOnWriteArrayList<>();
        members.add(ownerId);
        if (memberIds != null) {
            members.addAll(memberIds);
        }
        groupMembers.put(conversationId, members);
        
        for (String memberId : members) {
            userConversations.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(conversation);
        }
        
        return conversation;
    }

    @Override
    public boolean addGroupMember(String conversationId, String memberId, String memberName, String operatorId) {
        log.info("Adding member to group: {}, member: {}", conversationId, memberId);
        
        String ownerId = groupOwners.get(conversationId);
        if (ownerId == null || !ownerId.equals(operatorId)) {
            return false;
        }
        
        List<String> members = groupMembers.get(conversationId);
        if (members == null || members.contains(memberId)) {
            return false;
        }
        
        members.add(memberId);
        userConversations.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(conversations.get(conversationId));
        
        return true;
    }

    @Override
    public boolean removeGroupMember(String conversationId, String memberId, String operatorId) {
        log.info("Removing member from group: {}, member: {}", conversationId, memberId);
        
        String ownerId = groupOwners.get(conversationId);
        if (ownerId == null || (!ownerId.equals(operatorId) && !memberId.equals(operatorId))) {
            return false;
        }
        
        List<String> members = groupMembers.get(conversationId);
        if (members == null) {
            return false;
        }
        
        members.remove(memberId);
        
        return true;
    }

    @Override
    public List<Contact> getGroupMembers(String conversationId) {
        log.info("Getting group members: {}", conversationId);
        
        List<String> memberIds = groupMembers.get(conversationId);
        if (memberIds == null) {
            return new ArrayList<>();
        }
        
        List<Contact> contacts = new ArrayList<>();
        for (String memberId : memberIds) {
            Contact contact = new Contact();
            contact.setContactId(memberId);
            contact.setContactType("user");
            contact.setName(memberId);
            contacts.add(contact);
        }
        
        return contacts;
    }

    @Override
    public void addMessageListener(String userId, MessageListener listener) {
        userListeners.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    @Override
    public void removeMessageListener(String userId, MessageListener listener) {
        List<MessageListener> listeners = userListeners.get(userId);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private String generateConversationId(String userId, String targetId, String targetType) {
        if ("private".equals(targetType)) {
            return "private-" + (userId.compareTo(targetId) < 0 ? userId + "-" + targetId : targetId + "-" + userId);
        } else if ("department".equals(targetType)) {
            return "dept-" + targetId;
        } else {
            return "group-" + targetId;
        }
    }

    private int calculateUnreadCount(String conversationId, String userId) {
        List<IMMessage> messages = conversationMessages.get(conversationId);
        Map<String, String> readStatus = conversationReadStatus.get(conversationId);
        
        if (messages == null || messages.isEmpty()) {
            return 0;
        }
        
        String lastReadId = readStatus != null ? readStatus.get(userId) : null;
        
        if (lastReadId == null) {
            return (int) messages.stream().filter(m -> !m.getSenderId().equals(userId)).count();
        }
        
        int count = 0;
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).getMessageId().equals(lastReadId)) {
                break;
            }
            if (!messages.get(i).getSenderId().equals(userId)) {
                count++;
            }
        }
        
        return count;
    }

    private void notifyMessageListeners(String conversationId, IMMessage message) {
        List<String> memberIds = groupMembers.get(conversationId);
        if (memberIds != null) {
            for (String memberId : memberIds) {
                List<MessageListener> listeners = userListeners.get(memberId);
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
    }
}
