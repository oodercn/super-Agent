package net.ooder.skill.chat.service.impl;

import net.ooder.skill.chat.model.ChatMessage;
import net.ooder.skill.chat.model.ChatSession;
import net.ooder.skill.chat.service.ChatService;
import net.ooder.skill.chat.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> sessionMessages = new ConcurrentHashMap<>();
    private KnowledgeService knowledgeService;

    public ChatServiceImpl() {
        initDefaultSessions();
    }

    public void setKnowledgeService(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    private void initDefaultSessions() {
        ChatSession session = new ChatSession();
        session.setSessionId("session-default");
        session.setTitle("默认对话");
        session.setUserId("system");
        session.setMessages(new ArrayList<>());
        sessions.put(session.getSessionId(), session);
        sessionMessages.put(session.getSessionId(), new ArrayList<>());
    }

    @Override
    public List<ChatSession> listSessions(String userId) {
        return sessions.values().stream()
                .filter(s -> userId == null || userId.equals(s.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public ChatSession createSession(String userId, String title) {
        ChatSession session = new ChatSession();
        session.setSessionId("session-" + UUID.randomUUID().toString().substring(0, 8));
        session.setUserId(userId != null ? userId : "anonymous");
        session.setTitle(title != null ? title : "新对话");
        session.setMessages(new ArrayList<>());
        sessions.put(session.getSessionId(), session);
        sessionMessages.put(session.getSessionId(), new ArrayList<>());
        log.info("Created chat session: {}", session.getSessionId());
        return session;
    }

    @Override
    public ChatSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
        sessionMessages.remove(sessionId);
        log.info("Deleted chat session: {}", sessionId);
    }

    @Override
    public List<ChatMessage> getMessages(String sessionId) {
        return sessionMessages.getOrDefault(sessionId, new ArrayList<>());
    }

    @Override
    public ChatMessage sendMessage(String sessionId, String content) {
        return sendMessageWithKnowledge(sessionId, content, false);
    }

    @Override
    public ChatMessage sendMessageWithKnowledge(String sessionId, String content, boolean useKnowledge) {
        List<ChatMessage> messages = sessionMessages.computeIfAbsent(sessionId, k -> new ArrayList<>());

        ChatMessage userMessage = new ChatMessage();
        userMessage.setMessageId("msg-" + UUID.randomUUID().toString().substring(0, 8));
        userMessage.setSessionId(sessionId);
        userMessage.setRole(ChatMessage.MessageRole.USER);
        userMessage.setContent(content);
        messages.add(userMessage);

        String responseContent = generateResponse(content, useKnowledge);

        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setMessageId("msg-" + UUID.randomUUID().toString().substring(0, 8));
        assistantMessage.setSessionId(sessionId);
        assistantMessage.setRole(ChatMessage.MessageRole.ASSISTANT);
        assistantMessage.setContent(responseContent);
        messages.add(assistantMessage);

        ChatSession session = sessions.get(sessionId);
        if (session != null) {
            session.setUpdatedAt(new Date());
        }

        log.info("Sent message to session: {}", sessionId);
        return assistantMessage;
    }

    private String generateResponse(String content, boolean useKnowledge) {
        StringBuilder response = new StringBuilder();
        
        if (useKnowledge && knowledgeService != null) {
            List<String> relevantDocs = knowledgeService.search(content, 3);
            if (!relevantDocs.isEmpty()) {
                response.append("根据知识库中的资料，我找到了以下相关信息：\n\n");
                for (int i = 0; i < relevantDocs.size(); i++) {
                    response.append("【资料").append(i + 1).append("】").append(relevantDocs.get(i)).append("\n\n");
                }
            }
        }
        
        response.append("这是一个模拟的AI回复。在实际实现中，这里会调用LLM API生成响应。\n\n");
        response.append("您的问题是：").append(content);
        
        return response.toString();
    }
}
