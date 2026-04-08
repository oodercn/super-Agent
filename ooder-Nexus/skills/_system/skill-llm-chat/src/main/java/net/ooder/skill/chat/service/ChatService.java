package net.ooder.skill.chat.service;

import net.ooder.skill.chat.model.ChatSession;
import net.ooder.skill.chat.model.ChatMessage;

import java.util.List;

public interface ChatService {

    List<ChatSession> listSessions(String userId);

    ChatSession createSession(String userId, String title);

    ChatSession getSession(String sessionId);

    void deleteSession(String sessionId);

    List<ChatMessage> getMessages(String sessionId);

    ChatMessage sendMessage(String sessionId, String content);

    ChatMessage sendMessageWithKnowledge(String sessionId, String content, boolean useKnowledge);
}
