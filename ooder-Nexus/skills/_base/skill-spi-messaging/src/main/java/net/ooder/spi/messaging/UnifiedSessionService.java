package net.ooder.spi.messaging;

import net.ooder.spi.messaging.model.*;

import java.util.List;

public interface UnifiedSessionService {
    
    UnifiedSession createSession(CreateSessionRequest request);
    
    UnifiedSession getSession(String sessionId);
    
    List<UnifiedSession> listSessions(String userId, SessionType type, int limit);
    
    void deleteSession(String sessionId);
    
    void addParticipant(String sessionId, Participant participant);
    
    void removeParticipant(String sessionId, String participantId);
    
    void updateSessionContext(String sessionId, java.util.Map<String, Object> context);
}
