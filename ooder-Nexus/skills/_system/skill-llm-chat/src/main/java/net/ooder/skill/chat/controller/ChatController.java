package net.ooder.skill.chat.controller;

import net.ooder.scene.spi.SceneServices;
import net.ooder.scene.skill.conversation.*;
import net.ooder.scene.skill.conversation.Message;
import net.ooder.scene.skill.conversation.storage.ConversationStorageService;
import net.ooder.skill.chat.dto.CreateSessionRequest;
import net.ooder.skill.chat.dto.SendMessageRequest;
import net.ooder.skill.chat.model.ChatMessage;
import net.ooder.skill.chat.model.ChatSession;
import net.ooder.skill.common.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ExecutorService executor = Executors.newCachedThreadPool();
    
    private final ConversationStorageService storageService;
    private final ConversationService conversationService;
    
    private final Map<String, String> sessionMapping = new ConcurrentHashMap<>();

    public ChatController() {
        this.storageService = SceneServices.getStorageService();
        this.conversationService = SceneServices.getConversationService();
        
        if (this.storageService == null) {
            log.warn("ConversationStorageService not available from SE, using fallback");
        }
        if (this.conversationService == null) {
            log.warn("ConversationService not available from SE, using fallback");
        }
        
        log.info("ChatController initialized with SE services: storage={}, conversation={}", 
            storageService != null, conversationService != null);
    }

    @GetMapping("/sessions")
    public ResultModel<List<ChatSession>> listSessions(
            @RequestParam(required = false) String userId) {
        log.info("List chat sessions for user: {}", userId);
        
        if (storageService == null) {
            return ResultModel.success(new ArrayList<>());
        }
        
        List<Conversation> conversations = storageService.listConversations(
            userId != null ? userId : "default");
        
        List<ChatSession> sessions = conversations.stream()
            .map(this::convertToChatSession)
            .collect(Collectors.toList());
        
        return ResultModel.success(sessions);
    }

    @PostMapping("/sessions")
    public ResultModel<ChatSession> createSession(@RequestBody CreateSessionRequest request) {
        String userId = request.getUserId();
        String title = request.getTitle();
        log.info("Create chat session: userId={}, title={}", userId, title);
        
        Conversation conversation = new Conversation();
        conversation.setConversationId("conv-" + UUID.randomUUID().toString().substring(0, 8));
        conversation.setUserId(userId != null ? userId : "anonymous");
        conversation.setTitle(title != null ? title : "新对话");
        conversation.setCreatedAt(System.currentTimeMillis());
        conversation.setUpdatedAt(System.currentTimeMillis());
        
        if (storageService != null) {
            storageService.saveConversation(conversation);
        }
        
        ChatSession session = convertToChatSession(conversation);
        sessionMapping.put(session.getSessionId(), conversation.getConversationId());
        
        return ResultModel.success(session);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResultModel<ChatSession> getSession(@PathVariable String sessionId) {
        log.info("Get chat session: {}", sessionId);
        
        String convId = sessionMapping.getOrDefault(sessionId, sessionId);
        
        if (storageService == null) {
            return ResultModel.notFound("Storage service not available");
        }
        
        Conversation conversation = storageService.getConversation(convId);
        if (conversation == null) {
            return ResultModel.notFound("Session not found: " + sessionId);
        }
        
        ChatSession session = convertToChatSession(conversation);
        
        List<Message> messages = storageService.getMessages(convId);
        if (messages != null) {
            List<ChatMessage> chatMessages = messages.stream()
                .map(this::convertToChatMessage)
                .collect(Collectors.toList());
            session.setMessages(chatMessages);
        }
        
        return ResultModel.success(session);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResultModel<Boolean> deleteSession(@PathVariable String sessionId) {
        log.info("Delete chat session: {}", sessionId);
        
        String convId = sessionMapping.getOrDefault(sessionId, sessionId);
        
        if (storageService != null) {
            storageService.deleteConversation(convId);
        }
        sessionMapping.remove(sessionId);
        
        return ResultModel.success(true);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResultModel<List<ChatMessage>> getMessages(@PathVariable String sessionId) {
        log.info("Get messages for session: {}", sessionId);
        
        String convId = sessionMapping.getOrDefault(sessionId, sessionId);
        
        if (storageService == null) {
            return ResultModel.success(new ArrayList<>());
        }
        
        List<Message> messages = storageService.getMessages(convId);
        if (messages == null) {
            return ResultModel.success(new ArrayList<>());
        }
        
        List<ChatMessage> chatMessages = messages.stream()
            .map(this::convertToChatMessage)
            .collect(Collectors.toList());
        
        return ResultModel.success(chatMessages);
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public ResultModel<ChatMessage> sendMessage(
            @PathVariable String sessionId,
            @RequestBody SendMessageRequest request) {
        String content = request.getContent();
        Boolean useKnowledge = request.getUseKnowledge();
        Boolean enableTools = request.getEnableTools() != null ? request.getEnableTools() : true;
        String skillId = request.getSkillId() != null ? request.getSkillId() : "skill-llm-chat";
        String userId = request.getUserId() != null ? request.getUserId() : "default-user";
        
        log.info("Send message to session: {}, useKnowledge: {}, enableTools: {}", 
            sessionId, useKnowledge, enableTools);
        
        String convId = sessionMapping.getOrDefault(sessionId, sessionId);
        
        if (conversationService == null) {
            ChatMessage errorMsg = new ChatMessage();
            errorMsg.setContent("对话服务不可用，请检查服务初始化状态");
            return ResultModel.success(errorMsg);
        }
        
        MessageRequest msgRequest = new MessageRequest();
        msgRequest.setContent(content);
        msgRequest.setEnableRag(Boolean.TRUE.equals(useKnowledge));
        msgRequest.setEnableTools(Boolean.TRUE.equals(enableTools));
        
        try {
            MessageResponse response = conversationService.sendMessage(convId, msgRequest);
            
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageId(response.getMessageId());
            chatMessage.setSessionId(sessionId);
            chatMessage.setRole(ChatMessage.MessageRole.ASSISTANT);
            chatMessage.setContent(response.getContent());
            chatMessage.setCreatedAt(new Date());
            
            return ResultModel.success(chatMessage);
        } catch (Exception e) {
            log.error("Failed to send message", e);
            ChatMessage errorMsg = new ChatMessage();
            errorMsg.setContent("发送消息失败: " + e.getMessage());
            return ResultModel.success(errorMsg);
        }
    }

    @PostMapping(value = "/sessions/{sessionId}/messages/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessageStream(
            @PathVariable String sessionId,
            @RequestBody SendMessageRequest request) {
        
        String content = request.getContent();
        Boolean useKnowledge = request.getUseKnowledge();
        Boolean enableTools = request.getEnableTools() != null ? request.getEnableTools() : true;
        
        log.info("Send stream message to session: {}, useKnowledge: {}", sessionId, useKnowledge);
        
        String convId = sessionMapping.getOrDefault(sessionId, sessionId);
        
        SseEmitter emitter = new SseEmitter(60000L);
        
        if (conversationService == null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                        .data("{\"error\":\"对话服务不可用\"}")
                        .name("message"));
                    emitter.complete();
                } catch (Exception e) {
                    log.error("Stream error", e);
                }
            });
            return emitter;
        }
        
        executor.execute(() -> {
            try {
                MessageRequest msgRequest = new MessageRequest();
                msgRequest.setContent(content);
                msgRequest.setEnableRag(Boolean.TRUE.equals(useKnowledge));
                msgRequest.setEnableTools(Boolean.TRUE.equals(enableTools));
                
                conversationService.sendMessageStream(convId, msgRequest, 
                    new StreamMessageHandler() {
                        @Override
                        public void onContent(String chunk) {
                            try {
                                emitter.send(SseEmitter.event()
                                    .data("{\"content\":\"" + escapeJson(chunk) + "\"}")
                                    .name("message"));
                            } catch (Exception e) {
                                log.error("Stream send error", e);
                            }
                        }
                        
                        @Override
                        public void onToolCall(String toolName, String arguments) {
                            log.info("Tool call: {} with args: {}", toolName, arguments);
                        }
                        
                        @Override
                        public void onComplete(MessageResponse response) {
                            try {
                                emitter.send(SseEmitter.event()
                                    .data("{\"done\":true}")
                                    .name("message"));
                                emitter.complete();
                                log.info("Stream completed for session: {}", sessionId);
                            } catch (Exception e) {
                                log.error("Stream complete error", e);
                            }
                        }
                        
                        @Override
                        public void onError(String error) {
                            try {
                                emitter.send(SseEmitter.event()
                                    .data("{\"error\":\"" + escapeJson(error) + "\"}")
                                    .name("message"));
                            } catch (Exception e) {
                                log.error("Stream error send error", e);
                            }
                            emitter.completeWithError(new RuntimeException(error));
                        }
                    });
                
            } catch (Exception e) {
                log.error("Stream error", e);
                try {
                    emitter.send(SseEmitter.event()
                        .data("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}")
                        .name("message"));
                } catch (Exception ignored) {}
                emitter.completeWithError(e);
            }
        });
        
        return emitter;
    }
    
    private ChatSession convertToChatSession(Conversation conv) {
        ChatSession session = new ChatSession();
        session.setSessionId(conv.getConversationId());
        session.setUserId(conv.getUserId());
        session.setTitle(conv.getTitle());
        session.setCreatedAt(new Date(conv.getCreatedAt()));
        session.setUpdatedAt(new Date(conv.getUpdatedAt()));
        return session;
    }
    
    private ChatMessage convertToChatMessage(Message msg) {
        ChatMessage chatMsg = new ChatMessage();
        chatMsg.setMessageId(msg.getMessageId());
        chatMsg.setSessionId(msg.getConversationId());
        chatMsg.setContent(msg.getContent());
        chatMsg.setCreatedAt(new Date(msg.getCreatedAt()));
        
        String role = msg.getRole();
        if ("user".equals(role)) {
            chatMsg.setRole(ChatMessage.MessageRole.USER);
        } else if ("assistant".equals(role)) {
            chatMsg.setRole(ChatMessage.MessageRole.ASSISTANT);
        } else {
            chatMsg.setRole(ChatMessage.MessageRole.SYSTEM);
        }
        
        return chatMsg;
    }
    
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
